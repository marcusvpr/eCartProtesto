package com.mpxds.mpbasic.repository.filter;

import java.util.Date;

public class MpAlertaSiteFilter {
	//
	private String numeroOficio;
	private Date dataAlertaIni;
	private Date dataAlertaFim;
	private String alertaMsg;
	
	// ---	
	
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	public Date getDataAlertaIni() { return dataAlertaIni; }
	public void setDataAlertaIni(Date dataAlertaIni) { this.dataAlertaIni = dataAlertaIni; }

	public Date getDataAlertaFim() { return dataAlertaFim; }
	public void setDataAlertaFim(Date dataAlertaFim) { this.dataAlertaFim = dataAlertaFim; }
	
	public String getAlertaMsg() { return alertaMsg; }
	public void setAlertaMsg(String alertaMsg) { this.alertaMsg = alertaMsg; }
	
}