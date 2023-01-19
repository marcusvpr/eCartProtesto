package com.mpxds.mpbasic.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MpCampoValor implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String campo;
	private BigDecimal valor;

	// ---
	
	public MpCampoValor(String campo, BigDecimal valor) {
		super();
		this.campo = campo;
		this.valor = valor;
	}

	// ---
	
	public String getCampo() { return campo; }
	public void setCampo(String campo) { this.campo = campo; }

	public BigDecimal getValor() { return valor; }
	public void setValor(BigDecimal valor) { this.valor = valor; }

}
