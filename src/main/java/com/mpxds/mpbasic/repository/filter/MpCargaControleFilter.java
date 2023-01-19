package com.mpxds.mpbasic.repository.filter;

import java.util.Date;

public class MpCargaControleFilter {
	//
	private String numeroOficio;
	private Date dataHoraIni;
	private Date dataHoraFim;
	private String mensagem;
	
	// ---	
	
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	public Date getDataHoraIni() { return dataHoraIni; }
	public void setDataHoraIni(Date dataHoraIni) { this.dataHoraIni = dataHoraIni; }

	public Date getDataHoraFim() { return dataHoraFim; }
	public void setDataHoraFim(Date dataHoraFim) { this.dataHoraFim = dataHoraFim; }
	
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

}