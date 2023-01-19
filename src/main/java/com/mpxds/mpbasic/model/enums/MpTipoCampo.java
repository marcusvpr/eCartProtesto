package com.mpxds.mpbasic.model.enums;

public enum MpTipoCampo {

	BOOLEAN("Boolean"), 
	DATE("Date"), 
	DECIMAL("Decimal"), 
	NUMERO("Número"), 
	TEXTO("Texto"), 
	TEXTOEDITOR("Texto Editor"); 
	
	private String descricao;
	
	// ---
	
	MpTipoCampo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() { return descricao; }
	
}
