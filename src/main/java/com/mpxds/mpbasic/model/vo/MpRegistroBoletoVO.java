package com.mpxds.mpbasic.model.vo;

import java.io.Serializable;

import com.mpxds.mpbasic.model.MpBoleto;

public class MpRegistroBoletoVO implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String status;
	private String mensagem;
	
	private Integer numeroGuiaGerado;
	private MpBoleto mpBoleto;

	// ---
	
	public MpRegistroBoletoVO(String status, String mensagem) {
		//
		super();
		
		this.status = status;
		this.mensagem = mensagem;
	}

	// ---
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }
	
	public Integer getNumeroGuiaGerado() { return numeroGuiaGerado; }
	public void setNumeroGuiaGerado(Integer numeroGuiaGerado) { this.numeroGuiaGerado = numeroGuiaGerado; }

	public MpBoleto getMpBoleto() { return mpBoleto; }
	public void setMpBoleto(MpBoleto mpBoleto) { this.mpBoleto = mpBoleto; }
	
}
