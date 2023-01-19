package com.mpxds.mpbasic.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;

import com.mpxds.mpbasic.model.MpGrupo;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.model.enums.MpSexo;
import com.mpxds.mpbasic.model.enums.MpStatus;
import com.mpxds.mpbasic.repository.MpGruposX;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.security.MpAppUserDetailsService;
import com.mpxds.mpbasic.security.MpCustomPasswordEncoder;
//import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
//import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class MpServicoSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpUsuarios mpUsuarios;

	@Inject
	private MpUsuarioService mpUsuarioService;

	@Inject
	private MpGruposX mpGrupos;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

//	@Inject
//	private MpMailer mpMailer;

	private String email;
	private String senha;

	private String nomeReg;
	private String emailReg;
	private String cpfCnpjReg;
	private String senhaReg;
	private String senhaConfirmaReg;
	private String codigoAtivacaoReg; 

	private MpUsuario mpUsuario  = new MpUsuario();
	
	private String txtEmissaoBoleto = "";
	private String txtServicoAutorizaCancelamento = "";

	private String opcaoCpfCnpj = "cpf";

	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.tab}")
	private String paramIndexTab = "0";

	private Integer indexTab = 0;

	// ---
    
	public void init() {
		//
//		MpAppUtil.PrintarLn("MpServicoSiteBean.init()");

		this.mpUsuario = new MpUsuario();

		this.mpUsuario.setIndAtivacao(false);
		//
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("txtEmissaoBoleto");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		this.txtEmissaoBoleto = mpSistemaConfig.getValorT();
    	}
    	//
    	mpSistemaConfig = mpSistemaConfigs.porParametro("txtServicoAutorizaCancelamento");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		this.txtServicoAutorizaCancelamento = mpSistemaConfig.getValorT();
    	}
    	//
    	if (null == this.paramIndexTab || this.paramIndexTab.equals("0")) {
    		//
    		Map<String, String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    		String paramIndexTabX = params.get("tab");    		
    		
        	if (null == paramIndexTabX || paramIndexTabX.isEmpty())
        		this.indexTab = 0;
        	else
        		this.indexTab = Integer.parseInt(paramIndexTabX);    		
    	} else
    		this.indexTab = Integer.parseInt(this.paramIndexTab);    		
    		
    	// Refresh Tela para posicionar ABA Correta ...
    	PrimeFaces.current().ajax().update("frmReg");
    	
    	RequestContext context = RequestContext.getCurrentInstance();
    	context.update("frmReg");
    	
//    	MpAppUtil.PrintarLn("MpServicoSiteBean.init() ( " + this.indexTab);
    }
	
	public void acessar() {
		//
		MpAppUtil.PrintarLn("MpServicoSiteBean.acessar()");
    }
	
	public void registrar() {
		//
//		MpAppUtil.PrintarLn("MpServicoSiteBean.registrar()");
		if (null == this.cpfCnpjReg || this.cpfCnpjReg.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("Informar Documento CPF ou CNPJ !");			
			return;
		}
		// Valida CPF/CNPJ ...
		// System.out.println("MpServicoSiteBean.registrar() ( " + this.cpfCnpjReg + " / " + this.opcaoCpfCnpj);
		
		if (this.opcaoCpfCnpj.toLowerCase().equals("cpf")) {
			// Retira pontos e traços do CPF !
			String cpfX = this.cpfCnpjReg.trim().replace(".", "");		
			cpfX = cpfX.replace("-", "");

			if (!MpAppUtil.isValidCPF(cpfX)) {
				//
				MpFacesUtil.addErrorMessage("CPF Inválido ! ( " + this.cpfCnpjReg);			
				return;
			}
		} else {
			//
			// Retira pontos e traços do CPF !
			String cnpjX = this.cpfCnpjReg.trim().replace(".", "");		
			cnpjX = cnpjX.replace("/", "");
			cnpjX = cnpjX.replace("-", "");

			if (!MpAppUtil.isValidCNPJ(cnpjX)) {
				//
				MpFacesUtil.addErrorMessage("CNPJ Inválido ! ( " + this.cpfCnpjReg);	
				return;
			}
		}
		//
		if (null == this.senhaReg || null == this.senhaConfirmaReg) {
			//
			MpFacesUtil.addErrorMessage("Senha não confere com a Senha de confirmação !");			
			return;
		}

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
				//
				this.codigoAtivacaoReg = "??????????";
				MpFacesUtil.addErrorMessage("E-mail se encontra na fase de registro informar código de ATIVAÇÃO !");			
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
		this.mpUsuario.setCpfCnpj(this.cpfCnpjReg);
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
		this.mpUsuario.setIndBoleto(true);
		this.mpUsuario.setIndTitulo(false);
		
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
//		MpAppUtil.PrintarLn("MpServicoSiteBean.registrarConfirmar()");
		
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
			context.getExternalContext().redirect("/apps/Home");
			//
		} catch (IOException e) {
			//
			System.out.println("MpProtRJCap.registrarConfirmar() - Erro Redirect ! ( /apps/home/");
			
			try {
				context.getExternalContext().redirect("/MpProtRJCap/Home");
				//
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String reenviarAtivacao() {
		//
		MpAppUtil.PrintarLn("MpServicoSiteBean.reenviarAtivacao() ( Usuário = " + this.mpUsuario.getLogin());
		
		if (this.enviaEmailAtivacaoLOCAWEB())
			MpFacesUtil.addErrorMessage("Código de Ativação reenviado para seu E-mail. Favor Verificar ! ( ");
		//
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
//							" ( Emissao de BOLETO Site - www.protestorjcapital.com.br )")
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
//			System.out.println("MpServicoSiteBean() - Error-002 (Envio E-mail) ( e= " + e.toString());
//			//
//			return false;
//		}		
//	}

	public Boolean enviaEmailAtivacaoLOCAWEB() {
		//
		String[] emailList = {this.emailReg.trim() , "suporte@protestorjcapital.com.br"};
		
		String subject = "Protesto RJ Capital - Codigo Ativacao = " + this.mpUsuario.getCodigoAtivacao() + 
													" ( Emissao de BOLETO Site - www.protestorjcapital.com.br )";
		
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
			System.out.println("MpServicoSiteBean() - Error-003 (Envio E-mail LOCAWEB) ( e= " + e.toString());
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

	public String getCpfCnpjReg() { return cpfCnpjReg; }
	public void setCpfCnpjReg(String cpfCnpjReg) { this.cpfCnpjReg = cpfCnpjReg; }

	public String getSenhaReg() { return senhaReg; }
	public void setSenhaReg(String senhaReg) { this.senhaReg = senhaReg; }
    
	public String getSenhaConfirmaReg() { return senhaConfirmaReg; }
	public void setSenhaConfirmaReg(String senhaConfirmaReg) { this.senhaConfirmaReg = senhaConfirmaReg; }
    
	public String getCodigoAtivacaoReg() { return codigoAtivacaoReg; }
	public void setCodigoAtivacaoReg(String codigoAtivacaoReg) { this.codigoAtivacaoReg = codigoAtivacaoReg; }

	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }
   
	public String getTxtEmissaoBoleto() { return txtEmissaoBoleto; }
	public void setTxtEmissaoBoleto(String txtEmissaoBoleto) { this.txtEmissaoBoleto = txtEmissaoBoleto; }

	public String getTxtServicoAutorizaCancelamento() { return txtServicoAutorizaCancelamento; }
	public void setTxtServicoAutorizaCancelamento(String txtServicoAutorizaCancelamento) { 
											this.txtServicoAutorizaCancelamento = txtServicoAutorizaCancelamento; }
	   
	public String getOpcaoCpfCnpj() { return opcaoCpfCnpj; }
	public void setOpcaoCpfCnpj(String opcaoCpfCnpj) { this.opcaoCpfCnpj = opcaoCpfCnpj; }
	   
	public String getParamIndexTab() { return paramIndexTab; }
	public void setParamIndexTab(String paramIndexTab) { this.paramIndexTab = paramIndexTab; }
	   
	public Integer getIndexTab() { return indexTab; }
	public void setIndexTab(Integer indexTab) { this.indexTab = indexTab; }

}