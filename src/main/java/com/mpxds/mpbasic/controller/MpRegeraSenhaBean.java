package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.security.MpCustomPasswordEncoder;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
//import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
 
@ManagedBean
@Named
@SessionScoped
public class MpRegeraSenhaBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
//	@Inject
//	private MpMailer mpMailer;
	
	@Inject
	private MpUsuarios mpUsuarios;	

	@Inject
	private MpUsuarioService mpUsuarioService;
	
	//
	
	private MpUsuario mpUsuario;
	
	private String emailRegerar;
	private Boolean indRegerarSenha = false;
	     
    public void regeraAction(ActionEvent actionEvent) {
    	//    	
		this.regerarSenha();
    }
    
	public void regerarSenha() {
		//
		if (null == this.emailRegerar || this.emailRegerar.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("E-mail não informado !");			
			return;			
		}
		//
		if (!MpAppUtil.isEmail(this.emailRegerar)) {
			//
			MpFacesUtil.addErrorMessage("E-mail informado formato inválido !");			
			return;			
		}
		//
		this.mpUsuario = this.mpUsuarios.porEmail(this.emailRegerar);
		if (null == this.mpUsuario) {
			MpFacesUtil.addErrorMessage("E-mail não consta na nossa base de dados ! ( " + this.emailRegerar);			
			return;			
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
		//
	}
	
//	public Boolean enviaEmailSenha(String senhaRegerada) {
//		//
//		try {
//			//
//			MailMessage message = mpMailer.novaMensagem();
//			//
//			message.to(this.emailRegerar)
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
		String[] emailList = {this.emailRegerar , "suporte@protestorjcapital.com.br"};
		
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
			System.out.println("MpRegeraSenhaBean() - Error-004 (Envio E-mail LOCAWEB) ( e= " + e.toString());
			//
			return false;
		}
	}
         
    // ---
    
	public String getEmailRegerar() { return emailRegerar; }
	public void setEmailRegerar(String emailRegerar) { this.emailRegerar = emailRegerar; }

	public Boolean getIndRegerarSenha() { return indRegerarSenha; }
	public void setIndRegerarSenha(Boolean indRegerarSenha) { this.indRegerarSenha = indRegerarSenha; }
    
    
}