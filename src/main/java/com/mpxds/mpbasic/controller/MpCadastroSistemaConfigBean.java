package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.service.MpSistemaConfigService;
import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpCadastroSistemaConfigBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpSistemaConfigService mpSistemaConfigService;
	
	// ---

	private MpSistemaConfig mpSistemaConfig = new MpSistemaConfig();
	private MpSistemaConfig mpSistemaConfigAnt;

	private Boolean indEditavel = true;
	private Boolean indEditavelNav = true;
	private Boolean indNaoEditavel = false;
	
	private String txtModoTela = "";

	private MpTipoCampo mpTipoCampo;
	private List<MpTipoCampo> mpTipoCampoList;
	
	private List<String> objetoList = new ArrayList<String>();
	
	// ------
		
	public MpCadastroSistemaConfigBean() {
		if (null == this.mpSistemaConfig)
			limpar();
		else
			this.mpTrataMpSistemaConfig();
	}
	
	public void inicializar() {
		//
		this.objetoList.add("MpAlertaTipo");
		this.objetoList.add("MpAlertaTempo");
		//
		if (null == this.mpSistemaConfig) {
			this.limpar();
			//
			this.mpFirst(); // Posiciona no primeiro registro !!!
		} else
			this.mpTrataMpSistemaConfig();
		//
		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		//
		this.mpTipoCampoList = Arrays.asList(MpTipoCampo.values());
		
	}
	
	private void limpar() {
		this.mpSistemaConfig = new MpSistemaConfig();
		//
		this.mpSistemaConfig.setParametro("");		
		this.mpSistemaConfig.setDescricao("");		
		this.mpSistemaConfig.setObjeto("");		
	}
	
	public void salvar() {
		//			
		this.mpSistemaConfig = mpSistemaConfigService.salvar(this.mpSistemaConfig);
		//
		MpFacesUtil.addInfoMessage("Sistema Configuração... salva com sucesso!");
	}

	// -------- Trata Navegação ...

	public void mpFirst() {
		this.mpSistemaConfig = this.mpSistemaConfigs.porNavegacao("mpFirst", " "); 
		if (null == this.mpSistemaConfig)
			this.limpar();
		else
			this.mpTrataMpSistemaConfig();
		//
		this.txtModoTela = "( Início )";
	}
	public void mpPrev() {
		if (null == this.mpSistemaConfig.getParametro()) return;
		//
		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		//
		this.mpSistemaConfig = this.mpSistemaConfigs.porNavegacao("mpPrev", 
				  													 mpSistemaConfig.getParametro());
		if (null == this.mpSistemaConfig) {
			this.mpSistemaConfig = this.mpSistemaConfigAnt;
			//
			this.txtModoTela = "( Anterior - Inicio )";
		} else
			this.txtModoTela = "( Anterior )";
		//
		this.mpTrataMpSistemaConfig();
	}

	public void mpNew() {
		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		
		this.mpSistemaConfig = new MpSistemaConfig();
		//
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Novo )";
	}
	public void mpEdit() {
		if (null == this.mpSistemaConfig.getId()) return;
		//
		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Edição )";
	}
	
	public void mpDelete() {
		if (null == this.mpSistemaConfig.getId()) return;
		//
		try {
			this.mpSistemaConfigs.remover(mpSistemaConfig);
			
			MpFacesUtil.addInfoMessage("Sistema Configuração... " +
													this.mpSistemaConfig.getParametro()
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

		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		//
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
	
	public void mpDesfaz() {
		this.mpSistemaConfig = this.mpSistemaConfigAnt;
		
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
	
	public void mpNext() {
		if (null == this.mpSistemaConfig.getParametro()) return;
		//
		this.setMpSistemaConfigAnt(this.mpSistemaConfig);
		//
		this.mpSistemaConfig = this.mpSistemaConfigs.porNavegacao("mpNext", 
				  													  mpSistemaConfig.getParametro());
		if (null == this.mpSistemaConfig) {
			this.mpSistemaConfig = this.mpSistemaConfigAnt;
			//
			this.txtModoTela = "( Próximo - Fim )";
		} else
			this.txtModoTela = "( Próximo )";
		//
		this.mpTrataMpSistemaConfig();
	}
	
	public void mpEnd() {
		this.mpSistemaConfig = this.mpSistemaConfigs.porNavegacao("mpEnd", "ZZZZZ"); 
		if (null == this.mpSistemaConfig)
			this.limpar();
		else
			this.mpTrataMpSistemaConfig();
		//
		this.txtModoTela = "( Fim )";
	}
	
	public void mpClone() {
		//
		if (null == this.mpSistemaConfig.getId()) return;

		try {
			this.setMpSistemaConfigAnt(this.mpSistemaConfig);

			this.mpSistemaConfig = (MpSistemaConfig) this.mpSistemaConfig.clone();
			//
			this.mpSistemaConfig.setId(null);
			
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

	public void mpTrataMpSistemaConfig() {
		if (null == this.mpSistemaConfig) return;
		if (null == this.mpSistemaConfig.getObjeto()) return;
		//
//		System.out.println("MpCadastroSistemaConfig.mpTrataMpSistemaConfig() ( Objeto = " + 
//					this.mpSistemaConfig.getObjeto() + " / IndTipo = " +
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

	public MpSistemaConfig getMpSistemaConfig() { return mpSistemaConfig; }
	public void setMpSistemaConfig(MpSistemaConfig mpSistemaConfig) {
												this.mpSistemaConfig = mpSistemaConfig; }

	public MpSistemaConfig getMpSistemaConfigAnt() { return mpSistemaConfigAnt; }
	public void setMpSistemaConfigAnt(MpSistemaConfig mpSistemaConfigAnt) {
		//
		try {
			this.mpSistemaConfigAnt = (MpSistemaConfig) this.mpSistemaConfig.clone();
			this.mpSistemaConfigAnt.setId(this.mpSistemaConfig.getId());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEditando() { return this.mpSistemaConfig.getId() != null; }

	public List<String> getObjetoList() { return objetoList; }	
}