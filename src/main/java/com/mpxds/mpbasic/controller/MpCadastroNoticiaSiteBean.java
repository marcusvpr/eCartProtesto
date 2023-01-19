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
import com.mpxds.mpbasic.model.MpNoticiaSite;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.repository.MpNoticiaSites;
import com.mpxds.mpbasic.service.MpNoticiaSiteService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpCadastroNoticiaSiteBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpNoticiaSites mpNoticiaSites;

	@Inject
	private MpNoticiaSiteService mpNoticiaSiteService;
	
	// ---

	private MpNoticiaSite mpNoticiaSite = new MpNoticiaSite();
	private MpNoticiaSite mpNoticiaSiteAnt;

	private Boolean indEditavel = true;
	private Boolean indEditavelNav = true;
	private Boolean indNaoEditavel = false;
	
	private String txtModoTela = "";

	private MpTipoCampo mpTipoCampo;
	private List<MpTipoCampo> mpTipoCampoList;
	
	private List<String> objetoList = new ArrayList<String>();
	
	// ------
		
	public MpCadastroNoticiaSiteBean() {
		if (null == this.mpNoticiaSite)
			limpar();
		else
			this.mpTrataMpNoticiaSite();
	}
	
	public void inicializar() {
		//
		this.objetoList.add("MpNoticiaTipo");
		this.objetoList.add("MpNoticiaTempo");
		//
		if (null == this.mpNoticiaSite) {
			this.limpar();
			//
		} else
			this.mpTrataMpNoticiaSite();
		//
		this.setMpNoticiaSiteAnt(this.mpNoticiaSite);
		//
		this.mpTipoCampoList = Arrays.asList(MpTipoCampo.values());
		
	}
	
	private void limpar() {
		//
		this.mpNoticiaSite = new MpNoticiaSite();
		//
		this.mpNoticiaSite.setNumeroOficio("");		
		this.mpNoticiaSite.setDataNoticia(new Date());;		
		this.mpNoticiaSite.setAssunto("");		
		this.mpNoticiaSite.setDescricao("");		
		this.mpNoticiaSite.setUrl("");		
		this.mpNoticiaSite.setIndAtivo(true);		
	}
	
	public void salvar() {
		//			
		this.mpNoticiaSite = mpNoticiaSiteService.salvar(this.mpNoticiaSite);
		//
		MpFacesUtil.addInfoMessage("Noticia Site... salva com sucesso!");
	}

	public void mpNew() {
		//
		this.setMpNoticiaSiteAnt(this.mpNoticiaSite);
		
		this.mpNoticiaSite = new MpNoticiaSite();
		//
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Novo )";
	}
	public void mpEdit() {
		//
		if (null == this.mpNoticiaSite.getId()) return;
		//
		this.setMpNoticiaSiteAnt(this.mpNoticiaSite);
		
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Edição )";
	}
	
	public void mpDelete() {
		//
		if (null == this.mpNoticiaSite.getId()) return;
		//
		try {
			this.mpNoticiaSites.remover(mpNoticiaSite);
			
			MpFacesUtil.addInfoMessage("Noticia Site... " + this.mpNoticiaSite.getNumeroOficio() + " / " +
																	this.mpNoticiaSite.getDataNoticiaSDF()
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

		this.setMpNoticiaSiteAnt(this.mpNoticiaSite);
		//
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
	
	public void mpDesfaz() {
		//
		this.mpNoticiaSite = this.mpNoticiaSiteAnt;
		
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
		
	public void mpClone() {
		//
		if (null == this.mpNoticiaSite.getId()) return;

		try {
			this.setMpNoticiaSiteAnt(this.mpNoticiaSite);

			this.mpNoticiaSite = (MpNoticiaSite) this.mpNoticiaSite.clone();
			//
			this.mpNoticiaSite.setId(null);
			
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

	public void mpTrataMpNoticiaSite() {
		if (null == this.mpNoticiaSite) return;
		if (null == this.mpNoticiaSite.getNumeroOficio()) return;
		//
//		System.out.println("MpCadastroNoticiaSite.mpTrataMpNoticiaSite() ( Objeto = " + 
//					this.mpNoticiaSite.getObjeto() + " / IndTipo = " +
//					this.indNoticiaTipo + " / IndTempo = " + this.indNoticiaTempo + " )");		
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

	public MpNoticiaSite getMpNoticiaSite() { return mpNoticiaSite; }
	public void setMpNoticiaSite(MpNoticiaSite mpNoticiaSite) {
												this.mpNoticiaSite = mpNoticiaSite; }

	public MpNoticiaSite getMpNoticiaSiteAnt() { return mpNoticiaSiteAnt; }
	public void setMpNoticiaSiteAnt(MpNoticiaSite mpNoticiaSiteAnt) {
		//
		try {
			this.mpNoticiaSiteAnt = (MpNoticiaSite) this.mpNoticiaSite.clone();
			this.mpNoticiaSiteAnt.setId(this.mpNoticiaSite.getId());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEditando() { return this.mpNoticiaSite.getId() != null; }

	public List<String> getObjetoList() { return objetoList; }	
}