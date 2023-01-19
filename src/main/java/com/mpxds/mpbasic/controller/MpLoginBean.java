package com.mpxds.mpbasic.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.security.MpCustomPasswordEncoder;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@ManagedBean
@Named
@SessionScoped
public class MpLoginBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	@Inject
	private MpSeguranca mpSeguranca;	
	
	@Inject
	private MpUsuarios mpUsuarios;	

	@Inject
	private MpUsuarioService mpUsuarioService;
	
	// ---
	
	private MpUsuario mpUsuario;
	
	private String loginEmail;
	@SuppressWarnings("unused")
	private Integer contloginErro = 0;
	
	private Boolean indAdministrador;
	
    // Configuração Sistema ...
    // ------------------------
    @SuppressWarnings("unused")
	private Integer scNumLoginError;
    @SuppressWarnings("unused")
	private Integer scNumDiasTrocaSenha;
	@SuppressWarnings("unused")
	private String scSistemaURL = "";

	private String senhaNovaAlterar;
	private String senhaNovaAlterarConfirma;
	
	private Boolean indBoleto = false;
	private Boolean indTitulo = false;

	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.paramOrigem}")
	private String paramOrigem;
		
	// ---
		
	public void preRender() {
		//
//		MpAppUtil.PrintarLn("MpLoginBean.preRender() - 000");

		if ("true".equals(request.getParameter("invalid"))) {			
			//
			if (null == this.loginEmail || this.loginEmail.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Usuário / Senha... invállidos !");
				return;
			}
			//
//			this.contloginErro++;
//			if (this.contloginErro > 3) {
//				//
//				MpFacesUtil.addErrorMessage("Tentativas Login Excedidas! Usuário será bloqueado ! Contactar o Suporte!");
//				System.out.println("Tentativas Login... inválida! (Login=" + this.loginEmail);
//				return;
//			}
		}
		//
		this.contloginErro = 0;	
	}

	public void login() throws ServletException, IOException {
		//
		MpAppUtil.PrintarLn("MpLoginBean.login() - 000");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/MpLogin.xhtml");
		
		MpAppUtil.PrintarLn("MpLoginBean.login() - 001 ( " + dispatcher);

		if (null == dispatcher) {
			MpFacesUtil.addErrorMessage("Usuário ou Senha inválido ! ( Null Dispatcher");
			MpAppUtil.PrintarLn("Usuário ou Senha inválido ! ( Null Dispatcher");
		} else {
			//
			String usuario = request.getParameter("username");
			if (null == usuario || usuario.isEmpty()) {
				MpFacesUtil.addErrorMessage("Usuário inválido! (mpError.0002) ( Usuário : " + usuario);
				MpAppUtil.PrintarLn("Usuário inválido! (mpError.0002) ( Usuário : " + usuario);
				//
				return;
			}
			//
			this.mpUsuario = mpUsuarios.porLoginEmail(usuario);
			if (null == this.mpUsuario) {
				//
				MpFacesUtil.addErrorMessage("Usuário inválido! (mpError.0003)");
				MpAppUtil.PrintarLn("Usuário inválido! (mpError.0003)");
				//
				return;
			} else {
				if (!this.mpUsuario.getMpStatus().toString().equals("ATIVO")) {
					MpFacesUtil.addErrorMessage("Usuário erro! - Status! ( " + this.mpUsuario.getMpStatus()
																			+ " ) - Contate o SUPORTE !");
					MpAppUtil.PrintarLn("Usuário erro! - Status! ( " + this.mpUsuario.getMpStatus()
																			+ " ) - Contate o SUPORTE !");
					return;
				}
			}
		}		
		//
		dispatcher.forward(request, response);
			
		facesContext.responseComplete();
	}
	
	public void loginUser() throws ServletException, IOException {
		//
		MpAppUtil.PrintarLn("MpLoginBean.loginUser() - 000");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/MpServicoSite.xhtml");
		
		MpAppUtil.PrintarLn("MpLoginBean.loginUser() - 001 ( " + dispatcher);
		
		if (null == dispatcher)
			MpFacesUtil.addErrorMessage("Usuário ou Senha inválido ! ( Null Dispatcher");
		else {
			//
			String usuario = request.getParameter("username");
			if (null == usuario || usuario.isEmpty()) {
				MpFacesUtil.addErrorMessage("Usuário inválido! (mpError.0002) ( Usuário : " + usuario);
				//
				return;
			}
			//
			this.mpUsuario = mpUsuarios.porLoginEmail(usuario);
			if (null == this.mpUsuario) {
				//
				MpFacesUtil.addErrorMessage("Usuário inválido! (mpError.0003)");
				//
				return;
			} else {
				if (!this.mpUsuario.getMpStatus().toString().equals("ATIVO")) {
					MpFacesUtil.addErrorMessage("Usuário erro! - Status! ( " + this.mpUsuario.getMpStatus()
																				+ " ) - Contate o SUPORTE !");
					return;
				}
			}
		}		
		//
		dispatcher.forward(request, response);
			
		facesContext.responseComplete();
	}

	
	public void alterarSenha() {
		//
//		MpAppUtil.PrintarLn("MpLoginBean.alterarSenha()");

		this.mpUsuario = this.mpUsuarios.porLoginEmail(mpSeguranca.getLoginUsuario());
		if (null == this.mpUsuario) {
			MpFacesUtil.addErrorMessage("Error.0006 - Problemas autenticação usuário. Contactar o Suporte Técnico !");			
			return;
		}

		// Criptografa Senha... 
	    MpCustomPasswordEncoder mpCustomPasswordEncoder = new MpCustomPasswordEncoder();	     
		//
		if (!this.senhaNovaAlterar.equals(senhaNovaAlterarConfirma)) {
			//
			MpFacesUtil.addErrorMessage("Nova Senha não confere com a Nova Senha de confirmação !");			
			return;
		}
		
		// Criptografa Senha... 
	    String encoded = mpCustomPasswordEncoder.encode(senhaNovaAlterar);			

		this.mpUsuario.setSenha(encoded);
		//
		this.mpUsuario = this.mpUsuarioService.salvarSite(this.mpUsuario);
				
		MpFacesUtil.addErrorMessage("Alteração de Senha ... Efetuada !");	
		//
		return;
	}
				
	// ---
	
	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }
	
	public String getLoginEmail() { return loginEmail; }
	public void setLoginEmail(String loginEmail) { this.loginEmail = loginEmail; }
		
	public Boolean getIndAdministrador() { return indAdministrador; }
	public void setIndAdministrador(Boolean indAdministrador) {
													this.indAdministrador = indAdministrador; }

	public String getSenhaNovaAlterar() { return senhaNovaAlterar; }
	public void setSenhaNovaAlterar(String senhaNovaAlterar) { this.senhaNovaAlterar = senhaNovaAlterar; }

	public String getSenhaNovaAlterarConfirma() { return senhaNovaAlterarConfirma; }
	public void setSenhaNovaAlterarConfirma(String senhaNovaAlterarConfirma) { 
														this.senhaNovaAlterarConfirma = senhaNovaAlterarConfirma; }	

	public Boolean getIndBoleto() { return indBoleto; }
	public void setIndBoleto(Boolean indBoleto) { this.indBoleto = indBoleto; }
  
	public Boolean getIndTitulo() { return indTitulo; }
	public void setIndTitulo(Boolean indTitulo) { this.indTitulo = indTitulo; }
	  
	public String getParamOrigem() { return paramOrigem; }
	public void setParamOrigem(String paramOrigem) { this.paramOrigem = paramOrigem; }
	
}