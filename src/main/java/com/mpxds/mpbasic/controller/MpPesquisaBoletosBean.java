package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@SessionScoped
public class MpPesquisaBoletosBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpSeguranca mpSeguranca;

	private MpCartorioOficio mpCartorioOficioSel;
	
	private MpCartorioOficio mpCartorioOficioX;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	
	private Date dataDe;
	private Date dataAte;
    private String numeroIntimacao;

	private List<MpBoleto> mpBoletoList;
	
    // ---
	
	public void init() {
		//
		
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;		
		//
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, -1);
		
		this.dataDe = dataX.getTime();

		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, +1);

		this.dataAte = dataX.getTime();

		this.mpBoletoList = new ArrayList<MpBoleto>();
		
		this.numeroIntimacao = "000000";
	}

	public void pesquisaBoleto() {
		//
		if ( mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if ( mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if ( mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if ( mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4;
		//
		if (this.numeroIntimacao.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("Informar o Número do Protocolo !");
			return;
		}
		// 
		this.numeroIntimacao = StringUtils.leftPad(this.numeroIntimacao.trim(), 6, "0");
		
		this.mpBoletoList = mpBoletos.mpBoletoByNumeroProtocoloList(mpCartorioOficioSel.getNumero(), 
																								this.numeroIntimacao);
		
//		this.mpBoletoList = mpBoletos.mpBoletoByOficioDataDocumentoList(mpCartorioOficioSel.getNumero(), dataDe, dataAte);
		//
	}
	
	// ---
	
	public List<MpBoleto> getMpBoletoList() { return mpBoletoList; }
	public void setMpBoletoList(List<MpBoleto> mpBoletoList) { this.mpBoletoList = mpBoletoList; }
		
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }

	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }

	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }
	
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																		this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
		
}