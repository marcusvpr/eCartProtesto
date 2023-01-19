package com.mpxds.mpbasic.model.cielo.enums;

public enum CardNumeroParcelaDebito {
	//
	DEB("Débito"),
	P01("01 Parcela");
	
	// ---
	
	private String descricao;
	
	// ---
	
	CardNumeroParcelaDebito(String descricao) {
		//
		this.descricao = descricao;
	}

	public String getDescricao() { return descricao; }}