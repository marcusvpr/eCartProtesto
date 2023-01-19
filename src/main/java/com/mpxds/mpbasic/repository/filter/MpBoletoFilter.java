package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;

public class MpBoletoFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String nomeSacado;
	
	// ---

	public String getNomeSacado() { return nomeSacado; }
	public void setNomeSacado(String nomeSacado) { this.nomeSacado = nomeSacado; }
}