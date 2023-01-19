package com.mpxds.mpbasic.model.cielo.enums;

public enum CardNumeroParcela {
	//
	DEB("Débito"),
	P01("01 Parcela"),
	P02("02 Parcelas"),
	P03("03 Parcelas"),
	P04("04 Parcelas"),
	P05("05 Parcelas"),
	P06("06 Parcelas");
	
	// ---
	
	private String descricao;
	
	// ---
	
	CardNumeroParcela(String descricao) {
		//
		this.descricao = descricao;
	}

	public String getDescricao() { return descricao; }}