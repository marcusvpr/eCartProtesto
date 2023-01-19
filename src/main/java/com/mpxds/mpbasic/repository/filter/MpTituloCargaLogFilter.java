package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;

public class MpTituloCargaLogFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String nomeUsuario;
	
	// ---

	public String getNomeUsuario() { return nomeUsuario; }
	public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
	
}