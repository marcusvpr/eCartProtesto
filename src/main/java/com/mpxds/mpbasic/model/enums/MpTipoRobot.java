package com.mpxds.mpbasic.model.enums;

public enum MpTipoRobot {

	BOL_IMP("Boleto Impresso"), 
	TIT_INC("Titulo Incremental"); 
	
	private String descricao;
	
	// ---
	
	MpTipoRobot(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() { return descricao; }
	
}
