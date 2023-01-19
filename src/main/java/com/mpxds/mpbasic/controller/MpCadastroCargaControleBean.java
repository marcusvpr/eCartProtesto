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
import com.mpxds.mpbasic.model.MpCargaControle;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.repository.MpCargaControles;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpCadastroCargaControleBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpCargaControles mpCargaControles;

	@Inject
	private MpCargaControleService mpCargaControleService;
	
	// ---

	private MpCargaControle mpCargaControle = new MpCargaControle();
	private MpCargaControle mpCargaControleAnt;

	private Boolean indEditavel = true;
	private Boolean indEditavelNav = true;
	private Boolean indNaoEditavel = false;
	
	private String txtModoTela = "";

	// ------
		
	public MpCadastroCargaControleBean() {
		if (null == this.mpCargaControle)
			limpar();
		else
			this.mpTrataMpCargaControle();
	}
	
	public void inicializar() {
		//
		if (null == this.mpCargaControle) {
			this.limpar();
			//
		} else
			this.mpTrataMpCargaControle();
		//
		this.setMpCargaControleAnt(this.mpCargaControle);		
	}
	
	private void limpar() {
		//
		this.mpCargaControle = new MpCargaControle();
		//
		this.mpCargaControle.setNumeroOficio("");		
		this.mpCargaControle.setDataHoraIni(new Date());;		
		this.mpCargaControle.setDataHoraFim(new Date());;		
		this.mpCargaControle.setMensagem("");		
	}
	
	public void mpNew() {
		//
		this.setMpCargaControleAnt(this.mpCargaControle);
		
		this.mpCargaControle = new MpCargaControle();
		//
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Novo )";
	}
	public void mpEdit() {
		//
		if (null == this.mpCargaControle.getId()) return;
		//
		this.setMpCargaControleAnt(this.mpCargaControle);
		
		this.indEditavel = false;
		this.indEditavelNav = false;
		this.indNaoEditavel = true;
		//
		this.txtModoTela = "( Edição )";
	}
	
	public void mpDelete() {
		//
		if (null == this.mpCargaControle.getId()) return;
		//
		try {
			this.mpCargaControles.remover(mpCargaControle);
			
			MpFacesUtil.addInfoMessage("Hora Site... " + this.mpCargaControle.getNumeroOficio() + " / " +
																	this.mpCargaControle.getDataHoraIniSDF()
																	+ " excluído com sucesso.");
			//
		} catch (MpNegocioException ne) {
			MpFacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void mpGrava() {
		//
		try {
			//
			String msgX = "";
			
			if (null == this.mpCargaControle.getDataHoraIni())
				msgX = msgX + " ( Data Inicial ) ";
			if (null == this.mpCargaControle.getDataHoraFim())
				msgX = msgX + " ( Data Final ) ";
			if (null == this.mpCargaControle.getMensagem() 
			||  this.mpCargaControle.getMensagem().isEmpty())
				msgX = msgX + " (  Mensagem ) ";
			if (null == this.mpCargaControle.getDataHoraIni()
			||  null == this.mpCargaControle.getDataHoraFim())
				assert(true); // nop
			else {
				//
				if (this.mpCargaControle.getDataHoraIni().after(this.mpCargaControle.getDataHoraFim()))
					msgX = msgX + " (  Data Inicial maior que Final ) ";
				else {
					//
			        List<MpCargaControle> mpCargaControleListX = this.mpCargaControles.porDataIniFim(
			        																		this.mpCargaControle.getDataHoraIni());
		
			        for (MpCargaControle mpCargaControleX : mpCargaControleListX) {
			        	//
			        	if (mpCargaControleX.getNumeroOficio().contentEquals("??")) //this.mpCartorioOficioSel.getNumero()))
			        		assert(true);
			        	else {
			        		//
			            	MpFacesUtil.addErrorMessage("Data Incial sobrepondo ( Oficio = " + 
			            			mpCargaControleX.getNumeroOficio() + "\n/ " +
			            			mpCargaControleX.getDataHoraIniSDF() + "/ " +  mpCargaControleX.getDataHoraFimSDF() + "\n/ " +
			            			mpCargaControleX.getMensagem());
			            	//
			        		return;
			        	}
			        }
				}
			}
			if (!msgX.isEmpty()) {
				//
				MpFacesUtil.addInfoMessage("Verificar! ( " + msgX);
				return;
			}
			//
			this.mpCargaControle = mpCargaControleService.salvar(this.mpCargaControle);
			//
			MpFacesUtil.addInfoMessage("CARGA CONTROLE... salva com sucesso!");
			//
		} catch (Exception e) {
			//
			MpFacesUtil.addInfoMessage("Erro na Gravação! ( " + e.toString());
			return;
		}

		this.setMpCargaControleAnt(this.mpCargaControle);
		//
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
	
	public void mpDesfaz() {
		//
		this.mpCargaControle = this.mpCargaControleAnt;
		
		this.indEditavel = true;
		this.indNaoEditavel = false;
		//
		this.txtModoTela = "";
	}
		
	public void mpClone() {
		//
		if (null == this.mpCargaControle.getId()) return;

		try {
			this.setMpCargaControleAnt(this.mpCargaControle);

			this.mpCargaControle = (MpCargaControle) this.mpCargaControle.clone();
			//
			this.mpCargaControle.setId(null);
			
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

	public void mpTrataMpCargaControle() {
		if (null == this.mpCargaControle) return;
		if (null == this.mpCargaControle.getNumeroOficio()) return;
		//
//		System.out.println("MpCadastroCargaControle.mpTrataMpCargaControle() ( Objeto = " + 
//					this.mpCargaControle.getObjeto() + " / IndTipo = " +
//					this.indHoraTipo + " / IndTempo = " + this.indHoraTempo + " )");		
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

	public MpCargaControle getMpCargaControle() { return mpCargaControle; }
	public void setMpCargaControle(MpCargaControle mpCargaControle) {
												this.mpCargaControle = mpCargaControle; }

	public MpCargaControle getMpCargaControleAnt() { return mpCargaControleAnt; }
	public void setMpCargaControleAnt(MpCargaControle mpCargaControleAnt) {
		//
		try {
			this.mpCargaControleAnt = (MpCargaControle) this.mpCargaControle.clone();
			this.mpCargaControleAnt.setId(this.mpCargaControle.getId());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEditando() { return this.mpCargaControle.getId() != null; }
}