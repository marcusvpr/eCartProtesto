package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;

public class MpAutorizaCancelamentoFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String apresentante;
	
	// ---

	public String getApresentante() { return apresentante; }
	public void setApresentante(String apresentante) { this.apresentante = apresentante; }
}