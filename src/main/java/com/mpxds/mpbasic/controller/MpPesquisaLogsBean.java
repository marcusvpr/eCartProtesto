package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpBoletoCargaLog;
import com.mpxds.mpbasic.model.MpBoletoLog;
import com.mpxds.mpbasic.model.MpRobotLog;
import com.mpxds.mpbasic.model.MpTituloCargaLog;
import com.mpxds.mpbasic.model.MpTituloLog;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoCargaLog;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoLog;
import com.mpxds.mpbasic.repository.MpBoletoCargaLogs;
import com.mpxds.mpbasic.repository.MpBoletoLogs;
import com.mpxds.mpbasic.repository.MpRobotLogs;
import com.mpxds.mpbasic.repository.MpTituloCargaLogs;
import com.mpxds.mpbasic.repository.MpTituloLogs;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaoCargaLogs;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaoLogs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@SessionScoped
public class MpPesquisaLogsBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletoLogs mpBoletoLogs;

	@Inject
	private MpBoletoCargaLogs mpBoletoCargaLogs;

	@Inject
	private MpTituloLogs mpTituloLogs;

	@Inject
	private MpTituloCargaLogs mpTituloCargaLogs;

	@Inject
	private MpRobotLogs mpRobotLogs;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpBoletoIntimacaoLogs mpBoletoIntimacaoLogs;

	@Inject
	private MpBoletoIntimacaoCargaLogs mpBoletoIntimacaoCargaLogs;

	// ---
	
	private MpCartorioOficio mpCartorioOficioSel;
	
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
	
	private Date dataDe;
	private Date dataAte;

	// --- 
	
	private List<MpBoletoLog> mpBoletoLogList = new ArrayList<MpBoletoLog>();
	private List<MpBoletoCargaLog> mpBoletoCargaLogList = new ArrayList<MpBoletoCargaLog>();
	private List<MpTituloLog> mpTituloLogList = new ArrayList<MpTituloLog>();
	private List<MpTituloCargaLog> mpTituloCargaLogList = new ArrayList<MpTituloCargaLog>();
	private List<MpRobotLog> mpRobotLogList = new ArrayList<MpRobotLog>();

	private List<MpBoletoIntimacaoLog> mpBoletoIntimacaoLogList = new ArrayList<MpBoletoIntimacaoLog>();
	private List<MpBoletoIntimacaoCargaLog> mpBoletoIntimacaoCargaLogList = new ArrayList<MpBoletoIntimacaoCargaLog>();
	
    // ---
	
	public void init() {
		//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
		
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;		
		
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, -14);
		
		this.dataDe = dataX.getTime();

		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, +1);

		this.dataAte = dataX.getTime();
		//
	}

	public void exibe() {
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
		String msg = "";
		if (null == this.mpCartorioOficioSel) 
			msg = msg + " (Oficio) ";
		if (null == this.dataDe) 
			msg = msg + " (Data De) ";
		if (null == this.dataAte) 
			msg = msg + " (Data Até) ";
		if (this.dataDe.after(this.dataAte)) 
			msg = msg + " (Data De maior Data Até) ";
		//
		if (!msg.isEmpty()) {
			MpFacesUtil.addErrorMessage("Error : " + msg);
			return;
		}
		//
		this.mpBoletoLogList = mpBoletoLogs.mpBoletoLogBySelDataNumOficioList(dataDe, dataAte, 
			  																	mpCartorioOficioSel.getNumero());
		this.mpBoletoCargaLogList = mpBoletoCargaLogs.mpBoletoCargaLogBySelDataNumOficioList(dataDe, dataAte, 
																				mpCartorioOficioSel.getNumero());
		this.mpTituloLogList = mpTituloLogs.mpTituloLogBySelDataNumOficioList(dataDe, dataAte, 
																				mpCartorioOficioSel.getNumero());
		this.mpTituloCargaLogList = mpTituloCargaLogs.mpTituloCargaLogBySelDataNumOficioList(dataDe, dataAte,
																				mpCartorioOficioSel.getNumero());
		this.mpRobotLogList = mpRobotLogs.mpRobotLogBySelDataNumOficioList(dataDe, dataAte, 
																				mpCartorioOficioSel.getNumero());
		this.mpBoletoIntimacaoLogList = mpBoletoIntimacaoLogs.mpBoletoIntimacaoLogBySelDataNumOficioList(dataDe, dataAte, 
																	mpCartorioOficioSel.getNumero());
		this.mpBoletoIntimacaoCargaLogList = mpBoletoIntimacaoCargaLogs.mpBoletoIntimacaoCargaLogBySelDataNumOficioList(
																	dataDe, dataAte, mpCartorioOficioSel.getNumero());
	}
	
	// ---
	
	public List<MpBoletoLog> getMpBoletoLogList() { return mpBoletoLogList; }
	public void setMpBoletoLogList(List<MpBoletoLog> mpBoletoLogList) { this.mpBoletoLogList = mpBoletoLogList; }

	public List<MpTituloLog> getMpTituloLogList() { return mpTituloLogList; }
	public void setMpTituloLogList(List<MpTituloLog> mpTituloLogList) { this.mpTituloLogList = mpTituloLogList; }
	
	public List<MpBoletoCargaLog> getMpBoletoCargaLogList() { return mpBoletoCargaLogList; }
	public void setMpBoletoCargaLogList(List<MpBoletoCargaLog> mpBoletoCargaLogList) { 
																this.mpBoletoCargaLogList = mpBoletoCargaLogList; }

	public List<MpTituloCargaLog> getMpTituloCargaLogList() { return mpTituloCargaLogList; }
	public void setMpTituloCargaLogList(List<MpTituloCargaLog> mpTituloCargaLogList) { 
																this.mpTituloCargaLogList = mpTituloCargaLogList; }
	
	public List<MpRobotLog> getMpRobotLogList() { return mpRobotLogList; }
	public void setMpRobotLogList(List<MpRobotLog> mpRobotLogList) { this.mpRobotLogList = mpRobotLogList; }
	
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																	 this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
	
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }

	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }
	
	public List<MpBoletoIntimacaoLog> getMpBoletoIntimacaoLogList() { return mpBoletoIntimacaoLogList; }
	public void setMpBoletoIntimacaoLogList(List<MpBoletoIntimacaoLog> mpBoletoIntimacaoLogList) { 
															this.mpBoletoIntimacaoLogList = mpBoletoIntimacaoLogList; }

	public List<MpBoletoIntimacaoCargaLog> getMpBoletoIntimacaoCargaLogList() { return mpBoletoIntimacaoCargaLogList; }
	public void setMpBoletoIntimacaoCargaLogList(List<MpBoletoIntimacaoCargaLog> mpBoletoIntimacaoCargaLogList) { 
												this.mpBoletoIntimacaoCargaLogList = mpBoletoIntimacaoCargaLogList; }
}