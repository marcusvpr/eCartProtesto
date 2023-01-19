package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.model.MpNoticiaSite;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpAlertaSites;
import com.mpxds.mpbasic.repository.MpNoticiaSites;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;

@Named
@SessionScoped
public class MpIndexSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpAlertaSites mpAlertaSites;

	@Inject
	private MpNoticiaSites mpNoticiaSites;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	//
	
	private String txtAlertaTela = "";
	private String msgAlertaTela = "";
	private Integer tamAlertaTela = 0;
	
	// Só exibe as últimas (5) Noticias ..
	
	private List<MpNoticiaSite> mpNoticiaSiteList = new ArrayList<MpNoticiaSite>();;
	
	//
	
	private String txtSiteFooter = "Horário de atendimento: segunda à sexta - 10H às 16H - " +
								  	"'sac@protestorjcapital.com.br'";

    // ---
	
	public void init() {
		//
		this.txtAlertaTela = "Alerta!";
		
//        this.msgAlertaTela = "\n\n"
//        		+ "Prezados Usuários!\n\n"
//        		+ "Diante dos problemas causados pelo Covid-19 e com a autorização dada pelo Provimento 20/2020" 
//        		+ " da CGJ o horário de funcionamento dos tabelionatos de Protesto da Capital e do 7° Ofício de "
//        		+ "Registro de Distribuição, será de 10 às 16 horas, no período de 18 a 31 de Março de 2020.\n\n"
//        		+ "Pedimos desculpas por eventuais transtornos. Acessem os serviços do site \n\n"
//        		+ "Evitem deslocamento desnecessários.\n\n";
        
		List<MpAlertaSite> mpAlertaSites = this.mpAlertaSites.porDataIniFim(new Date());
		
		if (mpAlertaSites.size() > 0) {
			//
	        this.msgAlertaTela = "";
	        this.tamAlertaTela = 700;

			for (MpAlertaSite mpAlertaSiteX : mpAlertaSites) {
				//
				if (null == this.msgAlertaTela)
					assert(true); // nop
				else
					this.tamAlertaTela = mpAlertaSiteX.getTamanhoMsg();
				
				if (mpAlertaSiteX.getNumeroOficio().equals("X"))
					this.msgAlertaTela = this.msgAlertaTela + "( Sistema ) :\n\n " + mpAlertaSiteX.getAlertaMsg();
				else
					this.msgAlertaTela = this.msgAlertaTela + "( "  + mpAlertaSiteX.getNumeroOficio() + 
		        													"o.Ofício ) :\n\n " + mpAlertaSiteX.getAlertaMsg();
			}
			//
			if (!this.msgAlertaTela.isEmpty()) {
				//
		        RequestContext requestContext = RequestContext.getCurrentInstance();
		        
		        requestContext.update(":homeFormId");
		        requestContext.execute("PF('scDialogAlertaSite').show();");
			}
	        //
		}
		//
		this.mpNoticiaSiteList = this.mpNoticiaSites.listAll();
		//
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("txtSiteFooter");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		this.txtSiteFooter = mpSistemaConfig.getValorT();
    	}
		
//        FacesContext context = FacesContext.getCurrentInstance();
//        
//        context.addMessage(null, new FacesMessage(txtAlertaTela, ""));
//        context.addMessage(null, new FacesMessage("", "Prezados Usuários!"));
//        context.addMessage(null, new FacesMessage("", ""));
//        context.addMessage(null, new FacesMessage("", "Diante dos problemas causados pelo Covid-19 e com a autorização dada pelo Provimento 20/2020"));
//        context.addMessage(null, new FacesMessage("", " da CGJ o horário de funcionamento dos tabelionatos de Protesto da Capital e do 7° Ofício de "));
//        context.addMessage(null, new FacesMessage("", "Registro de Distribuição, será de 10 às 16 horas, no período de 18 a 31 de Março de 2020."));
//        context.addMessage(null, new FacesMessage("", "Pedimos desculpas por eventuais transtornos. Acessem os serviços do site"));
//        context.addMessage(null, new FacesMessage("", ""));
//        context.addMessage(null, new FacesMessage("", "Evitem deslocamento desnecessários."));
//        context.addMessage(null, new FacesMessage("", ""));
        //
	}

	//

	public String getTxtAlertaTela() { return txtAlertaTela; }
	public void setTxtAlertaTela(String txtAlertaTela) { this.txtAlertaTela = txtAlertaTela; }

	public String getMsgAlertaTela() { return msgAlertaTela; }
	public void setMsgAlertaTela(String msgAlertaTela) { this.msgAlertaTela = msgAlertaTela; }

	public Integer getTamAlertaTela() { return tamAlertaTela; }
	public void setMsgAlertaTela(Integer tamAlertaTela) { this.tamAlertaTela = tamAlertaTela; }

	public List<MpNoticiaSite> getMpNoticiaSiteList() { return mpNoticiaSiteList; }
	public void setMpNoticiaSiteList(List<MpNoticiaSite> mpNoticiaSiteList) { 
																		this.mpNoticiaSiteList = mpNoticiaSiteList; }

	public String getTxtSiteFooter() { return txtSiteFooter; }
	public void setTxtSiteFooter(String txtSiteFooter) { this.txtSiteFooter = txtSiteFooter; }

}