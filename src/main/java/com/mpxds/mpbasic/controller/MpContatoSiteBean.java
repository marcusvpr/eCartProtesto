package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedProperty;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpContatoSite;
import com.mpxds.mpbasic.model.enums.MpStatusContatoSite;
import com.mpxds.mpbasic.repository.MpContatoSites;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpContatoSiteService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpContatoSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
		
	@Inject
	private MpContatoSiteService mpContatoSiteService;

	@Inject
	private MpContatoSites mpContatoSites;
	
	@Inject
	private MpSeguranca mpSeguranca;

	// ---

	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.idC}")
	private String idC;
	
    private MpContatoSite mpContatoSiteSel = new MpContatoSite();
	
    private List<MpContatoSite> mpContatoSiteList;
    
    private HtmlDataTable dataTable;
    
	// ---
    
	public void init() {
		//
		this.mpContatoSiteList = mpContatoSites.mpContatoSiteByDataList();
    }
    
	// ---
			
	public void mpConclui() { 
		//
		if (null == idC) return;
		
		this.mpContatoSiteSel = mpContatoSites.porId(Long.parseLong(idC));
		if (null == mpContatoSiteSel) return;		

		this.mpContatoSiteList.remove(mpContatoSiteSel);
		
		if (null == this.mpSeguranca.getNomeUsuario())
			this.mpContatoSiteSel.setResponsavel("Nulo... Verificar !");
		else
			this.mpContatoSiteSel.setResponsavel(this.mpSeguranca.getNomeUsuario());
		
		this.mpContatoSiteSel.setMpStatusContatoSite(MpStatusContatoSite.CONCLUIDO);
		//						
		this.mpContatoSiteSel = this.mpContatoSiteService.salvar(this.mpContatoSiteSel);
		
		this.mpContatoSiteList.add(mpContatoSiteSel);
		//
		MpFacesUtil.addErrorMessage("Contato Site... Concluido !  (Data: " + 
																this.mpContatoSiteSel.getDataContatoSDF());		
		
		System.out.println("Contato Concluído com sucesso ! ( Data: " + 
				this.mpContatoSiteSel.getDataContatoSDF() + " / Nome: " + this.mpContatoSiteSel.getNome() + " )");
	}

//	public Boolean mpEdita() { 
//		//		
//		MpContatoSite mpContatoSiteX = mpContatoSites.porId(this.getMpContatoSite().getId());
//
//		if (null == mpContatoSiteX.getIndEditavel() || mpContatoSiteX.getIndEditavel() == false) {
//			mpContatoSiteX.setIndEditavel(true);
//		} else {
//			mpContatoSiteX.setResponsavel(this.mpSeguranca.getLoginUsuario());
//			mpContatoSiteX.setIndEditavel(false);
//					
////			this.mpContatoSiteService.salvar(mpContatoSiteX);
//			//
//			System.out.println("MpContatoSite.mpEdita() - 000 ( " + mpContatoSiteX.getNome() + " / " +
//																		mpContatoSiteX.getIndEditavel());
//		}		
		//
//		System.out.println("MpContatoSite.mpEdita() - 001 ( " + mpContatoSiteX.getIndEditavel() + " / " +
//																		mpContatoSiteX.getIndEditavel());
//		//
//		return mpContatoSiteX.getIndEditavel();
//	}

    // ---
	
	public String getIdC() { return idC; }
	public void setIdC(String idC) { this.idC = idC; }
    
	public MpContatoSite getMpContatoSiteSel() { return this.mpContatoSiteSel; }
	public void setMpContatoSiteSel(MpContatoSite mpContatoSiteSel) { this.mpContatoSiteSel = mpContatoSiteSel; }

	public List<MpContatoSite> getMpContatoSiteList() { return this.mpContatoSiteList; }
	public void setMpContatoSiteList(List<MpContatoSite> mpContatoSiteList) { 
																	 this.mpContatoSiteList = mpContatoSiteList; }

	public HtmlDataTable getDataTable() { return dataTable; }
    public void setDataTable(HtmlDataTable dataTable) { this.dataTable = dataTable; }

}