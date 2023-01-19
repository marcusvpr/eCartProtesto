package com.mpxds.mpbasic.model.enums;

public enum MpStatusContatoSite {
	//
	NOVO("Novo"),
	ANDAMENTO("Em Andamento"),
	CONCLUIDO("Concluido"),
	CANCELADO("Cancelado"),
	PENDENTE("Pendente");
	
	private String descricao;
	
	// ---

	MpStatusContatoSite(String descricao) { 
		this.descricao = descricao;
	}
	
	public String getDescricao() { return descricao; }

}