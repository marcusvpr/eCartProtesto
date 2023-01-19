package com.mpxds.mpbasic.model.enums;

public enum MpEspecieTituloBradesco {
	//
	CH ("01", "CHEQUE"),
	DM ("02", "DUPLICATA DE VENDA MERCANTIL"),
	DMI("03", "DUPLICATA MERCANTIL POR INDICACAO"),
	DS ("04", "DUPLICATA DE PRESTACAO DE SERVICOS"),
	DSI("05", "DUPLICATA PREST. SERVICOS POR INDICACAO"),
	DR ("06", "DUPLICATA RURAL"),
	LC ("07", "LETRA DE CAMBIO"),
	NCC("08", "NOTA DE CREDITO COMERCIAL"),
	NCE("09", "NOTA DE CREDITO EXPORTACAO"),
	NCI("10", "NOTA DE CREDITO INDUSTRIAL"),
	NCR("11", "NOTA DE CREDITO RURAL"),
	NP ("12", "NOTA PROMISSORIA"),
	NPR("13", "NOTA PROMISSORIA RURAL"),
	TM ("14", "TRIPLICATA DE VENDA MERCANTIL"),
	TS ("15", "TRIPLICATA DE PRESTACAO DE SERVICOS"),
	NS ("16", "NOTA DE SERVICO"),
	RC ("17", "RECIBO"),
	FAT("18", "FATURA"),
	ND ("19", "NOTA DE DEBITO"),
	AP ("20", "APOLICE DE SEGURO"),
	ME ("21", "MENSALIDADE ESCOLAR"),
	PC ("22", "PARCELA DE CONSORCIO"),
	DD ("23", "DOCUMENTO DE DIVIDA"),
	CCB("24", "CEDULA DE CREDITO BANCARIO"),
	FI ("25", "FINANCIAMENTO"),
	RD ("26", "RATEIO DE DESPESAS"),
	DRI("27", "DUPLICATA RURAL INDICACAO"),
	EC ("28", "ENCARGOS CONDOMINIAIS"),
	ECI("29", "ENCARGOS CONDOMINIAIS POR INDICACAO"),
	CC ("30", "CARTAO DE CREDITO"),
	BDP("31", "BOLETO DE PROPOSTA"),
	OUT("99", "OUTROS");
				
	private String sigla;
	private String descricao;

	// ---
	
	MpEspecieTituloBradesco(String sigla, String descricao) {
		//
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public static MpEspecieTituloBradesco fromString(String text) {
		//
		for (MpEspecieTituloBradesco mpEspecieTituloBradesco : MpEspecieTituloBradesco.values()) {
			//
			if (mpEspecieTituloBradesco.sigla.equalsIgnoreCase(text)) {
				//
				return mpEspecieTituloBradesco;
			}
		}
		//
		return null;
	}

	// ---
	
	public String getSigla() { return sigla; }	
	public String getDescricao() { return descricao; }

}
