package com.mpxds.mpbasic.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;

import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
import com.mpxds.mpbasic.security.MpAppUserDetailsService;
import com.mpxds.mpbasic.security.MpCustomPasswordEncoder;
import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.model.MpGrupo;
import com.mpxds.mpbasic.model.enums.MpSexo;
import com.mpxds.mpbasic.model.enums.MpStatus;
import com.mpxds.mpbasic.repository.MpGruposX;
import com.mpxds.mpbasic.repository.MpUsuarios;

//import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
//import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class MpServicoTituloBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpUsuarios mpUsuarios;

	@Inject
	private MpUsuarioService mpUsuarioService;

	@Inject
	private MpGruposX mpGrupos;

//	@Inject
//	private MpMailer mpMailer;
	
	private String email;
	private String senha;

	private String nomeReg;
	private String emailReg;
	private String senhaReg;
	private String senhaConfirmaReg;
	private String codigoAtivacaoReg;

	private MpUsuario mpUsuario  = new MpUsuario();

	private String emailRegerar;
	private Boolean indRegerarSenha = false;
	private Boolean indCollapsedRegerar = true;
	
	// ---
    
	public void init() {
		//
//		MpAppUtil.PrintarLn("MpServicoTituloBean.init()");

		this.mpUsuario = new MpUsuario();

		this.mpUsuario.setIndAtivacao(false);

		this.indCollapsedRegerar = true;
    }
	
	public void acessar() {
		//
		MpAppUtil.PrintarLn("MpServicoTituloBean.acessar()");
    }
	
	public void registrar() {
		//
//		MpAppUtil.PrintarLn("MpServicoTituloBean.registrar()");

		if (!this.senhaReg.equals(senhaConfirmaReg)) {
			//
			MpFacesUtil.addErrorMessage("Senha não confere com a Senha de confirmação !");			
			return;
		}
		//	
		this.mpUsuario = this.mpUsuarios.porEmail(emailReg);
		if (null == this.mpUsuario)
			assert(true); // nop
		else {
			if (this.mpUsuario.getMpStatus().equals(MpStatus.REGISTRO)) {
				MpFacesUtil.addErrorMessage("E-mail se encontra fase de registro informar código de ATIVAÇÃO !");			
				return;
			} else {
				MpFacesUtil.addErrorMessage("E-mail já se encontra registrado na nossa base de dados !");			
				return;
			}
		}
		//
		this.mpUsuario = new MpUsuario();

		this.mpUsuario.setLogin(MpAppUtil.randomString(10, true, false));
		this.mpUsuario.setEmail(this.emailReg);
		this.mpUsuario.setNome(this.nomeReg);
		// Criptografa Senha...
	    MpCustomPasswordEncoder mpCustomPasswordEncoder = new MpCustomPasswordEncoder();
	     
	    String encoded = mpCustomPasswordEncoder.encode(this.senhaReg);			
		
		this.mpUsuario.setSenha(encoded);
		// --- Defaults ...
		this.mpUsuario.setMpSexo(MpSexo.DEFINIR);
		this.mpUsuario.setMpStatus(MpStatus.REGISTRO);
		
		this.mpUsuario.setIndAtivacao(true);
		this.mpUsuario.setCodigoAtivacao(MpAppUtil.randomString(10, false, true));

		// Trata Exibição no MENU ....
		this.mpUsuario.setIndTitulo(true);
		this.mpUsuario.setIndBoleto(false);

		// Configura Perfil = USUARIO ! 
		// ----------------------------
		List<MpGrupo> mpGrupoList = new ArrayList<MpGrupo>();
		
		MpGrupo mpGrupo = this.mpGrupos.porNome("USUARIOS");
		if (null == mpGrupo) {
			//
			MpFacesUtil.addErrorMessage("Error-005. Grupo (USUARIOS). Contactar o Suporte Tecnico!");
			return;
		}
		//
		mpGrupoList.add(mpGrupo);
		
		this.mpUsuario.setMpGrupos(mpGrupoList);
		// ----------------------------

//		this.codigoAtivacaoReg = this.mpUsuario.getCodigoAtivacao(); // ??? Desativar/Testes!
		
		this.mpUsuario = this.mpUsuarioService.salvarSite(this.mpUsuario);
		//
		if (this.enviaEmailAtivacaoLOCAWEB())
			MpFacesUtil.addErrorMessage("E-mail com codigo de ATIVAÇÂO, enviado ! Favor verificar." + 
										" Informar esse código para dar prosseguimento ao REGISTRO");
		else
			this.mpUsuario.setIndAtivacao(false);
		//
    }

	public void registrarConfirmar() {
		//
//		MpAppUtil.PrintarLn("MpServicoTituloBean.registrarConfirmar()");
		
		if (!this.codigoAtivacaoReg.equals(this.mpUsuario.getCodigoAtivacao())) {
			//
			MpFacesUtil.addErrorMessage("Código de Ativação informado inválido ! ( " + this.codigoAtivacaoReg);			
			return;
		}
		//
		this.mpUsuario = this.mpUsuarios.porEmail(emailReg);
		if (null == this.mpUsuario) {
			MpFacesUtil.addErrorMessage("Error-001 (Registro). Favor contactar o Suporte Técnico !");			
			return;			
		}

		this.mpUsuario.setMpStatus(MpStatus.ATIVO);
		
		this.mpUsuario.setIndAtivacao(false);
		// Limpa campos Registro da Tela?
		
		this.mpUsuario = this.mpUsuarioService.salvarSite(this.mpUsuario);

		MpFacesUtil.addErrorMessage("ATIVAÇÃO... efetuada com sucesso !");
		//
		// ??? Proceder login automaticamente?
		this.reauthenticate(this.mpUsuario.getEmail(), this.mpUsuario.getSenha());
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			context.getExternalContext().redirect("/apps/EmissaoTitulo");
			//
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String reenviarAtivacao() {
		//
//		MpAppUtil.PrintarLn("MpServicoTituloBean.reenviarAtivacao()");
		
		if (this.enviaEmailAtivacaoLOCAWEB())
			MpFacesUtil.addErrorMessage("Código de Ativação reenviado para seu E-mail. Favor Verificar ! ( ");
		//
		return "";
	}
	
	public String regerarSenha() {
		//
		this.indCollapsedRegerar = false;
		
		if (null == this.emailRegerar || this.emailRegerar.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("E-mail não informado !");			
			return "error";			
		}
		//
		if (!MpAppUtil.isEmail(this.emailRegerar)) {
			//
			MpFacesUtil.addErrorMessage("E-mail informado formato inválido !");			
			return "error";			
		}
		//
		this.mpUsuario = this.mpUsuarios.porEmail(this.emailRegerar);
		if (null == this.mpUsuario) {
			MpFacesUtil.addErrorMessage("E-mail não consta na nossa base de dados ! ( " + this.emailRegerar);			
			return "error";			
		}
		//
		String senhaRegerada = MpAppUtil.randomString(6, true, true);
		// Criptografa Senha...
	    MpCustomPasswordEncoder mpCustomPasswordEncoder = new MpCustomPasswordEncoder();
	     
	    String encoded = mpCustomPasswordEncoder.encode(senhaRegerada);			

		this.mpUsuario.setSenha(encoded);
		//
		this.mpUsuario = this.mpUsuarioService.salvarSite(this.mpUsuario);

		if (this.enviaEmailSenhaLOCAWEB(senhaRegerada))
			MpFacesUtil.addErrorMessage("Nova SENHA enviada para seu E-mail ! Favor verificar.");			
		//
		this.indRegerarSenha = false;
		
		return "";
	}
		
//	public Boolean enviaEmailAtivacao() {
//		//
//		try {
//			//
//			MailMessage message = mpMailer.novaMensagem();
//			//
//			message.to(this.emailReg)
//				.subject("Protesto RJ Capital - Codigo Ativacao = " + this.mpUsuario.getCodigoAtivacao() + 
//							" ( Consulta Titulo Status Site - www.protestorjcapital.com.br )")
//				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/mpEnvioAtivacao.template")))
//				.put("mpUsuario", this.mpUsuario)
//				.put("locale", new Locale("pt", "BR"))
//				.charset("utf-8")
//				.send();
//			//
//			return true;
//			//
//		} catch (Exception e) {
//			//
//			MpFacesUtil.addErrorMessage("Error-002 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " +
//																									e.toString());
//			return false;
//		}		
//	}
	
	public Boolean enviaEmailAtivacaoLOCAWEB() {
		//
		String[] emailList = {this.emailReg , "suporte@protestorjcapital.com.br"};
		
		String subject = "Protesto RJ Capital - Codigo Ativacao = " + this.mpUsuario.getCodigoAtivacao() + 
													" ( Consulta Titulo Status Site - www.protestorjcapital.com.br )";
		
	    Map<String,Object> mapX = new HashMap<String,Object>(); 
	    
	    mapX.put("mpUsuario", this.mpUsuario);
	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

	    String message = new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpEnvioAtivacao.template")).merge(mapX).toString();
		
		String from = "suporte@protestorjcapital.com.br";
		
		try {
			new MpSendMailLOCAWEB(emailList, subject, message, from);
			//
			return true;
		} catch (Exception e) {
			//
			MpFacesUtil.addErrorMessage("Error-003 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " +
																										e.toString());
			System.out.println("MpServicoBoletoBean() - Error-003 (Envio E-mail LOCAWEB) ( e= " + e.toString());
			//
			return false;
		}
	}

//	public Boolean enviaEmailSenha(String senhaRegerada) {
//		//
//		try {
//			//
//			MailMessage message = mpMailer.novaMensagem();
//			//
//			message.to(this.emailReg)
//					.subject("MPXDS - Regeração de SENHA ( Emissao de BOLETO Site - www.protestorjcapital.com.br )")
//					.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/mpEnvioSenha.template")))
//					.put("senhaRegerada", senhaRegerada)
//					.put("locale", new Locale("pt", "BR"))
//					.charset("utf-8")
//					.send();
//			//
//			return true;
//			//
//		} catch (Exception e) {
//			//
//			MpFacesUtil.addErrorMessage(
//					"Error-003 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " + e.toString());
//			return false;
//		}
//	}

	public Boolean enviaEmailSenhaLOCAWEB(String senhaRegerada) {
		//
		String[] emailList = {this.emailReg , "suporte@protestorjcapital.com.br"};
		
		String subject = "Protesto RJ Capital - Regeração de SENHA ( Emissao de BOLETO Site - www.protestorjcapital.com.br )";
		
	    Map<String,Object> mapX = new HashMap<String,Object>(); 
	    
	    mapX.put("senhaRegerada", senhaRegerada);
	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

	    String message = new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpEnvioSenha.template")).merge(mapX).toString();
		
		String from = "suporte@protestorjcapital.com.br";
		
		try {
			new MpSendMailLOCAWEB(emailList, subject, message, from);
			//
			return true;
		} catch (Exception e) {
			//
			MpFacesUtil.addErrorMessage("Error-004 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " +
																										e.toString());
			System.out.println("MpServicoBoletoBean() - Error-004 (Envio E-mail LOCAWEB) ( e= " + e.toString());
			//
			return false;
		}
	}
	
	public void reauthenticate(String username, String password) {
		//
		// Criptografa Senha...
	    MpCustomPasswordEncoder mpCustomPasswordEncoder = new MpCustomPasswordEncoder();
	     
	    String encoded = mpCustomPasswordEncoder.encode(password);			
		
		MpAppUtil.PrintarLn("MpSeguranca.reauthenticate() ( " + username);
		
		MpAppUserDetailsService mpAppUserDetailsService = MpCDIServiceLocator.getBean(MpAppUserDetailsService.class);
	    
	    UserDetails userDetails = mpAppUserDetailsService.loadUserByUsername(username);
	    
	    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
	            userDetails, password == null ? userDetails.getPassword() : encoded, userDetails.getAuthorities()));

	    // UserCache userCache = MpCDIServiceLocator.getBean(UserCache.class);
	    // userCache.removeUserFromCache(username); ??
	}
		
	// ---

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }
    
	public String getNomeReg() { return nomeReg; }
	public void setNomeReg(String nomeReg) { this.nomeReg = nomeReg; }

	public String getEmailReg() { return emailReg; }
	public void setEmailReg(String emailReg) { this.emailReg = emailReg; }

	public String getSenhaReg() { return senhaReg; }
	public void setSenhaReg(String senhaReg) { this.senhaReg = senhaReg; }
    
	public String getSenhaConfirmaReg() { return senhaConfirmaReg; }
	public void setSenhaConfirmaReg(String senhaConfirmaReg) { this.senhaConfirmaReg = senhaConfirmaReg; }
    
	public String getCodigoAtivacaoReg() { return codigoAtivacaoReg; }
	public void setCodigoAtivacaoReg(String codigoAtivacaoReg) { this.codigoAtivacaoReg = codigoAtivacaoReg; }

	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }

	public String getEmailRegerar() { return emailRegerar; }
	public void setEmailRegerar(String emailRegerar) { this.emailRegerar = emailRegerar; }

	public Boolean getIndRegerarSenha() { return indRegerarSenha; }
	public void setIndRegerarSenha(Boolean indRegerarSenha) { this.indRegerarSenha = indRegerarSenha; }

	public Boolean getIndCollapsedRegerar() { return indCollapsedRegerar; }
	public void setIndCollapsedRegerar(Boolean indCollapsedRegerar) { 
																this.indCollapsedRegerar = indCollapsedRegerar; }
   
}