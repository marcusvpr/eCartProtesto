package com.mpxds.mpbasic.model.cielo.enums;

public enum CardBrand {
	// BANDEIRA 		//	CRÉDITO À VISTA / CRÉDITO PARCELADO LOJA / DÉBITO / VOUCHER / INTERNACIONAL
//	Visa,  				// 	Sim	Sim	Sim	Não	Sim
//	MasterCard,			// 	Sim	Sim	Sim	Não	Sim
//	AmericanExpress,	//	Sim	Sim	Não	Não	Sim
//	Elo,				//	Sim	Sim	Não	Não	Sim
//	DinersClub,			//	Sim	Sim	Não	Não	Sim
//	Discover,			//	Sim	Não	Não	Não	Sim
//	JCB,				//	Sim	Sim	Não	Não	Sim
//	Aura,				//	Sim	Sim	Não	Não	Sim
//	Hipercard,			//	Sim	Sim	Não	Não	Não
//	Hiper				//	Sim	Sim	Não	Não	Não}
	
	VISA("Visa",              			true,  true,  true,  false, true),
	MASTERCARD("MasterCard",     	 	true,  true,  true,  false, true),
	AMERICANEXPRESS("American Express",	true,  true,  false, false, true),
	ELO("Elo",				  			true,  true,  false, false, true),
	DINERSCLUB("Diners Club",  			true,  true,  false, false, true),
	DISCOVER("Discover", 	 			true,  false, false, false, true),
	JCB("JCB",				  			true,  true,  false, false, true),
	AURA("Aura", 			 			true,  true,  false, false, true),
	HIPERCARD("HiperCard",  			true,  true,  false, false, false),
	HIPER("Hiper",            			true,  true,  false, false, false);
	
	private String bandeira;
	private Boolean indCreditoAVista;
	private Boolean indCreditoParceladoLoja;
	private Boolean indDebito;
	private Boolean indVoucher;
	private Boolean indInternacional;

	// ---
	
	CardBrand(String bandeira,Boolean indCreditoAVista, Boolean indCreditoParceladoLoja,
							  Boolean indDebito, Boolean indVoucher, Boolean indInternacional) {
		//
		this.bandeira = bandeira;
		this.indCreditoAVista = indCreditoAVista;
		this.indCreditoParceladoLoja = indCreditoParceladoLoja;
		this.indDebito = indDebito;
		this.indVoucher = indVoucher;
		this.indInternacional = indInternacional;
	}
	
	public static CardBrand fromString(String text) {
		//
		for (CardBrand cardBrand : CardBrand.values()) {
			//
			if (cardBrand.bandeira.equalsIgnoreCase(text)) {
				//
				return cardBrand;
			}
		}
		//
		return null;
	}

	// ---
	
	public String getBandeira() { return bandeira; }	
	public Boolean getIndCreditoAVista() { return indCreditoAVista; }
	public Boolean getIndCreditoParceladoLoja() { return indCreditoParceladoLoja; }
	public Boolean getIndDebito() { return indDebito; }
	public Boolean getIndVoucher() { return indVoucher; }
	public Boolean getIndInternacional() { return indInternacional; }
	
}