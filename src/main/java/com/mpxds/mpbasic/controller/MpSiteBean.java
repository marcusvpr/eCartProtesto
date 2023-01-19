package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpContatoSite;
//import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpStatusContatoSite;
//import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.service.MpContatoSiteService;
//import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
//import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@SessionScoped
public class MpSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpContatoSiteService mpContatoSiteService;

//	@Inject
//	private MpMailer mpMailer;

//	@Inject
//	private MpSistemaConfigs mpSistemaConfigs;

	// --- 
	
	private MpContatoSite mpContatoSite = new MpContatoSite();

	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	private MpCartorioOficio mpCartorioOficioX;

	private List<MpCartorioOficio> mpCartorioOficioList = new ArrayList<MpCartorioOficio>();

    // ---
	
	@PostConstruct
	public void postConstruct() {
		//
		this.mpContatoSite = new MpContatoSite();
		
		this.mpContatoSite.setMpCartorioOficio(null); 
		this.mpContatoSite.setMpStatusContatoSite(MpStatusContatoSite.NOVO);
		this.mpContatoSite.setIndEditavel(false); 
		//
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		
		this.mpCartorioOficioList = Arrays.asList(MpCartorioOficio.values());
		//
//		MpAppUtil.PrintarLn("MpSiteBean.init() - 000");
	}
	
	public void recebeContato() {
		//
		this.mpContatoSite.setDataContato(new Date());
		
		MpContatoSite mpContatoSiteX = this.mpContatoSiteService.salvar(this.mpContatoSite);
		if (null == mpContatoSiteX)
			MpFacesUtil.addErrorMessage("Erro no envio do Contato ! Contactar o Suporte Técnico");
		else {
			//
			if (this.enviaContatoSiteLOCAWEB());
				MpFacesUtil.addErrorMessage("Mensagem enviada com sucesso !");
		}
	}
		
//	public void enviaContatoSite() {
//		//
////		System.out.println("MpSiteBean.enviaContatoSite() - ( Nome = " + this.mpContatoSite.getNome());
//
//		MailMessage message = mpMailer.novaMensagem();
//
//		List<MpSistemaConfig> mpSistemaConfigList = mpSistemaConfigs.porParametros("GrupoEmailContato");
//		
//		for (MpSistemaConfig mpSistemaConfigX : mpSistemaConfigList) {
//			//
//			message.to(mpSistemaConfigX.getDescricao()) // Criar grupo para receber esse email !
//				.subject("MPXDS - Alerta ( Contato Site - www.protestorjcapital.com.br )")
//				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/mpEnvioContatoSite.template")))
//				.put("mpContatoSite", this.mpContatoSite)
//				.put("locale", new Locale("pt", "BR"))
//				.send();
//		}
//	}	
	
	public Boolean enviaContatoSiteLOCAWEB() {
		//
//		List<MpSistemaConfig> mpSistemaConfigList = mpSistemaConfigs.porParametros("GrupoEmailContato");
//		
//		String[] emailList = new String[10]; // ????
//		Integer contX = 0;
//		//
//		for (MpSistemaConfig mpSistemaConfigX : mpSistemaConfigList) {
//			//
//			if (!MpAppUtil.emailValidator(mpSistemaConfigX.getDescricao().trim())) {
//				//
//				MpFacesUtil.addErrorMessage("Error-005 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( " +
//																			mpSistemaConfigX.getDescricao().trim()); 
//				return false;
//			}
//			//			
//			emailList[contX] = mpSistemaConfigX.getDescricao().trim();			
//			contX++;
//		}
//		if (mpSistemaConfigList.size() == 0) {
//			emailList[0] = "suporte@protestorjcapital.com.br";
//			emailList[1] = "marcus_vpr@hotmail.com";
//		}
		//
		String[] emailList = {"marcus_vpr@hotmail.com" , "suporte@protestorjcapital.com.br"};
		
		String subject = "Protesto RJ Capital - Alerta ( Contato Site - www.protestorjcapital.com.br )";
		
	    Map<String,Object> mapX = new HashMap<String,Object>(); 
	    
	    mapX.put("mpContatoSite", this.mpContatoSite);
	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

	    String message = new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpEnvioContatoSite.template")).merge(mapX).toString();
		
		String from = "suporte@protestorjcapital.com.br";
		
		try {
			new MpSendMailLOCAWEB(emailList, subject, message, from);
			//
			return true;
			//
		} catch (Exception e) {
			//
//			e.printStackTrace();
			MpFacesUtil.addErrorMessage("Error-003 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " +
																										e.toString());
			System.out.println("MpSiteBean() - Error-003 (Envio E-mail LOCAWEB) ( e= " + e.toString());
			//
			return false;
		}
	}
		
	// ---
	
	public MpContatoSite getMpContatoSite() { return mpContatoSite; }
	public void setMpContatoSite(MpContatoSite mpContatoSite) { this.mpContatoSite = mpContatoSite; }
		
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }

	public List<MpCartorioOficio> getMpCartorioOficioList() { return mpCartorioOficioList; }
	public void setMpCartorioOficioList(List<MpCartorioOficio> mpCartorioOficioList) { 
																	this.mpCartorioOficioList = mpCartorioOficioList; }
	
}