package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.repository.MpAlertaSites;
import com.mpxds.mpbasic.service.MpAlertaSiteService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpCadastroAlertaSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpAlertaSites mpAlertaSites;

	@Inject
	private MpAlertaSiteService mpAlertaSiteService;
	
	// ---

	private MpAlertaSite mpAlertaSite = new MpAlertaSite();
	private MpAlertaSite mpAlertaSiteAnt;

	private Boolean indEditavel = true;
	private Boolean indEditavelNav = true;
	private Boolean indNaoEditavel = false;
	
	private String txtModoTela = "";

	private MpTipoCampo mpTipoCampo;
	private List<MpTipoCampo> mpTipoCampoList;
	
	private List<String> objetoList = new ArrayList<String>();
	
	// ------
		
	public MpCadastroAlertaSiteBean() {
		if (null == this.mpAlertaSite)
			limpar();
		else
			this.mpTrataMpAlertaSite();
	}
	
	public void inicializar() {
		//
		this.objetoList.add("MpAlertaTipo");
		this.objetoList.add("MpAlertaTempo");
		//
		if (null == this.mpAlertaSite) {
			this.limpar();
			//
		} else
			this.mpTrataMpAlertaSite();
		//
		this.setMpAlertaSiteAnt(this.mpAlertaSite);
		//
		this.mpTipoCampoList = Arrays.asList(MpTipoCampo.values());
		
	}
	
	private void limpar() {
		//
		this.mpAlertaSite = new MpAlertaSite();
		//
		this.mpAlertaSite.setNumeroOficio("");		
		this.mpAlertaSite.setDataAlertaIni(new Date());;		
		this.mpAlertaSite.setDataAlertaFim(new Date());;		
		this.mpAlertaSite.setAlertaMsg("");		
	}
	
	public void salvar() {
		//			
		this.mpAlertaSite = mpAlertaSiteService.salvar(this.mpAlertaSite);
		//
		MpFacesUtil.addInfoMessage("Alerta Site... salva com sucesso!");
	}

	public void mpNew() {
		//
		this.setMpAlertaSiteAnt(this.mpAlertaSite);
		
		this.mpAlertaSite = new MpAlertaSite();
		//
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Novo )";
	}
	public void mpEdit() {
		//
		if (null == this.mpAlertaSite.getId()) return;
		//
		this.setMpAlertaSiteAnt(this.mpAlertaSite);
		
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Edição )";
	}
	
	public void mpDelete() {
		//
		if (null == this.mpAlertaSite.getId()) return;
		//
		try {
			this.mpAlertaSites.remover(mpAlertaSite);
			
			MpFacesUtil.addInfoMessage("Alerta Site... " + this.mpAlertaSite.getNumeroOficio() + " / " +
																	this.mpAlertaSite.getDataAlertaIniSDF()
																	+ " excluído com sucesso.");
			//
		} catch (MpNegocioException ne) {
			MpFacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void mpGrava() {
		//
		try {
			this.salvar();
			//
		} catch (Exception e) {
			//
			MpFacesUtil.addInfoMessage("Erro na Gravação! ( " + e.toString());
			return;
		}

		this.setMpAlertaSiteAnt(this.mpAlertaSite);
		//
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
	
	public void mpDesfaz() {
		//
		this.mpAlertaSite = this.mpAlertaSiteAnt;
		
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
		
	public void mpClone() {
		//
		if (null == this.mpAlertaSite.getId()) return;

		try {
			this.setMpAlertaSiteAnt(this.mpAlertaSite);

			this.mpAlertaSite = (MpAlertaSite) this.mpAlertaSite.clone();
			//
			this.mpAlertaSite.setId(null);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		//
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Clone )";
	}

	public void mpTrataMpAlertaSite() {
		if (null == this.mpAlertaSite) return;
		if (null == this.mpAlertaSite.getNumeroOficio()) return;
		//
//		System.out.println("MpCadastroAlertaSite.mpTrataMpAlertaSite() ( Objeto = " + 
//					this.mpAlertaSite.getObjeto() + " / IndTipo = " +
//					this.indAlertaTipo + " / IndTempo = " + this.indAlertaTempo + " )");		
	}
	
	// -------------
	
	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
	
	public boolean getIndEditavelNav() { return indEditavelNav; }
	public void setIndEditavelNav(Boolean indEditavelNav) { this.indEditavelNav = indEditavelNav; }
	
	public boolean getIndNaoEditavel() { return indNaoEditavel; }
	public void setIndNaoEditavel(Boolean indNaoEditavel) {
													this.indNaoEditavel = indNaoEditavel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public MpTipoCampo getMpTipoCampo() { return mpTipoCampo; }
	public void setMpTipoCampo(MpTipoCampo mpTipoCampo) { this.mpTipoCampo = mpTipoCampo; }
	public List<MpTipoCampo> getMpTipoCampoList() { return mpTipoCampoList; }

	public MpAlertaSite getMpAlertaSite() { return mpAlertaSite; }
	public void setMpAlertaSite(MpAlertaSite mpAlertaSite) {
												this.mpAlertaSite = mpAlertaSite; }

	public MpAlertaSite getMpAlertaSiteAnt() { return mpAlertaSiteAnt; }
	public void setMpAlertaSiteAnt(MpAlertaSite mpAlertaSiteAnt) {
		//
		try {
			this.mpAlertaSiteAnt = (MpAlertaSite) this.mpAlertaSite.clone();
			this.mpAlertaSiteAnt.setId(this.mpAlertaSite.getId());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEditando() { return this.mpAlertaSite.getId() != null; }

	public List<String> getObjetoList() { return objetoList; }	
}