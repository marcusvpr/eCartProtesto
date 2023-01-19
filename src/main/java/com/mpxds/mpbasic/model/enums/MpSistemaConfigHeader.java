package com.mpxds.mpbasic.model.enums;

public enum MpSistemaConfigHeader {
	//
	MpFaltaDefinir("Falta Definir!", 
			true, true, true, true, true, true),
	MpBoletoInstrucao1("1-Guia válida apenas para pagamento em DINHEIRO na data do vencimento", 
			false, false, false, true, false, false),
	MpBoletoInstrucao2("2-Não receber após o vencimento", 
			false, false, false, true, false, false),
	MpBoletoInstrucao3("3-Retorne ao Tabelionato, com a guia autenticada, no 1º dia útil após às 14:00", 
			false, false, false, true, false, false),
	MpBoletoInstrucao4("para retirar seu \"título\" quitado", 
			false, false, false, true, false, false),
	MpBoletoInstrucao5("4-Dirija-se ao 7º Oficio para realizar o cancelamento da distribuição", 
			false, false, false, true, false, false),
	MpBoletoInstrucao6(" ", 
			false, false, false, true, false, false),
	MpBoletoInstrucao7(" ", 
			false, false, false, true, false, false),
	MpBoletoInstrucao8("GUIA No 190210 Hora 16:42:38", 
			false, false, false, true, false, false),
	MpBoletoInstrucaoAoSacado("Evite multas, pague em dias suas contas", 
			false, false, false, true, false, false),
	MpBoletoLocalPagamento("Preferencialmente nas Agências do Bradesco", 
			false, false, false, true, false, false);
	//
	private String descricao;
	private Boolean indBoolean;
	private Boolean indNumero;
	private Boolean indDecimal;
	private Boolean indTexto;
	private Boolean indTextoEditor;
	private Boolean indData;

	// ---
	
	MpSistemaConfigHeader(String descricao,
						Boolean indBoolean,
						Boolean indNumero,
						Boolean indDecimal,
						Boolean indTexto,
						Boolean indTextoEditor,
						Boolean indData) {
		//
		this.descricao = descricao;
		this.indBoolean = indBoolean;
		this.indNumero = indNumero;
		this.indDecimal = indDecimal;
		this.indTexto = indTexto;
		this.indTextoEditor = indTextoEditor;
		this.indData = indData;
	}

	// ---
	
	public String getDescricao() { return descricao; }	
	public Boolean getIndBoolean() { return indBoolean; }	
	public Boolean getIndNumero() { return indNumero; }	
	public Boolean getIndDecimal() { return indDecimal; }	
	public Boolean getIndTexto() { return indTexto; }	
	public Boolean getIndTextoEditor() { return indTextoEditor; }	
	public Boolean getIndData() { return indData; }	

}
