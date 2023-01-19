package com.mpxds.mpbasic.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MpBoletoRegistroRetorno {
	//
	/*
			{"cdErro":"-2",
				"msgErro":"Contrato n&amp;atilde;o encontrado",
				"idProduto":"0",
				"negociacao":"0",
				"clubBanco":"0",
				"tpContrato":"0",
				"nuSequenciaContrato":"0",
				"cdProduto":"0", 
				"nuTituloGerado":"0",
				"agenciaCreditoBeneficiario":"0",
				"contaCreditoBeneficiario":"0",
				"digCreditoBeneficiario":"",
				"cdCipTitulo":"0",
				"statusTitulo":"0",
				"descStatusTitulo":"",
				"nomeBeneficiario":"",
				"logradouroBeneficiario":"", 
				"nuLogradouroBeneficiario":"", 
				"complementoLogradouroBeneficiario":"", 
				"bairroBeneficiario":"",
				"cepBeneficiario":"0", 
				"cepComplementoBeneficiario":"0",
				"municipioBeneficiario":"",
				"ufBeneficiario":"",
				"razaoContaBeneficiario":"0",
				"nomePagador":"", 
				"cpfcnpjPagador":"0", 
				"enderecoPagador":"", 
				"bairroPagador":"", 
				"municipioPagador":"", 
				"ufPagador":"", 
				"cepPagador":"0", 
				"cepComplementoPagador":"", 
				"endEletronicoPagador":"", 
				"nomeSacadorAvalista":"", 
				"cpfcnpjSacadorAvalista":"0", 
				"enderecoSacadorAvalista":"", 
				"municipioSacadorAvalista":"", 
				"ufSacadorAvalista":"", 
				"cepSacadorAvalista":"0", 
				"cepComplementoSacadorAvalista":"0", 
				"numeroTitulo":"", "dtRegistro":"", 
				"especieDocumentoTitulo":"", 
				"descEspecie":"", 
				"vlIOF":"0", 
				"dtEmissao":"", 
				"dtVencimento":"", 
				"vlTitulo":"0", 
				"vlAbatimento":"0", 
				"dtInstrucaoProtestoNegativacao":"", 
				"diasInstrucaoProtestoNegativacao":"0", 
				"dtMulta":"",
				"vlMulta":"0",
				"qtdeCasasDecimaisMulta":"0",
				"cdValorMulta":"0",
				"descCdMulta":"",
				"dtJuros":"",
				"vlJurosAoDia":"0",
				"dtDesconto1Bonificacao":"",
				"vlDesconto1Bonificacao":"0",
				"qtdeCasasDecimaisDesconto1Bonificacao":"0",
				"cdValorDesconto1Bonificacao":"0",
				"descCdDesconto1Bonificacao":"",
				"dtDesconto2":"", "vlDesconto2":"0",
				"qtdeCasasDecimaisDesconto2":"0",
				"cdValorDesconto2":"0",
				"descCdDesconto2":"",
				"dtDesconto3":"",
				"vlDesconto3":"0",
				"qtdeCasasDecimaisDesconto3":"0",
				"cdValorDesconto3":"0",
				"descCdDesconto3":"",
				"diasDispensaMulta":"0",
				"diasDispensaJuros":"0",
				"cdBarras":"",
				"linhaDigitavel":"",
				"cdAcessorioEscrituralEmpresa":"0",
				"tpVencimento":"0",
				"indInstrucaoProtesto":"0",
				"tipoAbatimentoTitulo":"0",
				"cdValorJuros":"0",
				"tpDesconto1":"0",
				"tpDesconto2":"0",
				"tpDesconto3":"0",
				"nuControleParticipante":"",
				"diasJuros":"0",
				"cdJuros":"0",
				"vlJuros":"0",
				"cpfcnpjBeneficiario":"",
				"vlTituloEmitidoBoleto":"0",
				"dtVencimentoBoleto":"",
				"indTituloPertenceBaseTitulos":"",
				"dtLimitePagamentoBoleto":"",
				"cdIdentificacaoTituloDDACIP":"0",
				"indPagamentoParcial":"",
				"qtdePagamentoParciais":"0"
			}
	 */
	
	// ---
	
	private String cdErro;
	private String msgErro;
	private String idProduto;
	private String negociacao;
	private String clubBanco;
	private String tpContrato;
	private String nuSequenciaContrato;
	private String cdProduto; 
	private String nuTituloGerado;
	private String agenciaCreditoBeneficiario;
	private String contaCreditoBeneficiario;
	private String digCreditoBeneficiario;
	private String cdCipTitulo;
	private String statusTitulo;
	private String descStatusTitulo;
	private String nomeBeneficiario;
	private String logradouroBeneficiario; 
	private String nuLogradouroBeneficiario; 
	private String complementoLogradouroBeneficiario; 
	private String bairroBeneficiario;
	private String cepBeneficiario; 
	private String cepComplementoBeneficiario;
	private String municipioBeneficiario;
	private String ufBeneficiario;
	private String razaoContaBeneficiario;
	private String nomePagador; 
	private String cpfcnpjPagador; 
	private String enderecoPagador; 
	private String bairroPagador; 
	private String municipioPagador; 
	private String ufPagador; 
	private String cepPagador; 
	private String cepComplementoPagador; 
	private String endEletronicoPagador; 
	private String nomeSacadorAvalista; 
	private String cpfcnpjSacadorAvalista; 
	private String enderecoSacadorAvalista; 
	private String municipioSacadorAvalista; 
	private String ufSacadorAvalista; 
	private String cepSacadorAvalista; 
	private String cepComplementoSacadorAvalista; 
	private String numeroTitulo; 
	private String dtRegistro; 
	private String especieDocumentoTitulo; 
	private String descEspecie; 
	private String vlIOF; 
	private String dtEmissao; 
	private String dtVencimento; 
	private String vlTitulo; 
	private String vlAbatimento; 
	private String dtInstrucaoProtestoNegativacao; 
	private String diasInstrucaoProtestoNegativacao; 
	private String dtMulta;
	private String vlMulta;
	private String qtdeCasasDecimaisMulta;
	private String cdValorMulta;
	private String descCdMulta;
	private String dtJuros;
	private String vlJurosAoDia;
	private String dtDesconto1Bonificacao;
	private String vlDesconto1Bonificacao;
	private String qtdeCasasDecimaisDesconto1Bonificacao;
	private String cdValorDesconto1Bonificacao;
	private String descCdDesconto1Bonificacao;
	private String dtDesconto2; 
	private String vlDesconto2;
	private String qtdeCasasDecimaisDesconto2;
	private String cdValorDesconto2;
	private String descCdDesconto2;
	private String dtDesconto3;
	private String vlDesconto3;
	private String qtdeCasasDecimaisDesconto3;
	private String cdValorDesconto3;
	private String descCdDesconto3;
	private String diasDispensaMulta;
	private String diasDispensaJuros;
	private String cdBarras;
	private String linhaDigitavel;
	private String cdAcessorioEscrituralEmpresa;
	private String tpVencimento;
	private String indInstrucaoProtesto;
	private String tipoAbatimentoTitulo;
	private String cdValorJuros;
	private String tpDesconto1;
	private String tpDesconto2;
	private String tpDesconto3;
	private String nuControleParticipante;
	private String diasJuros;
	private String cdJuros;
	private String vlJuros;
	private String cpfcnpjBeneficiario;
	private String vlTituloEmitidoBoleto;
	private String dtVencimentoBoleto;
	private String indTituloPertenceBaseTitulos;
	private String dtLimitePagamentoBoleto;
	private String cdIdentificacaoTituloDDACIP;
	private String indPagamentoParcial;
	private String qtdePagamentoParciais;

	// ---
	public MpBoletoRegistroRetorno() {
		//
		super();
	}

	// ---

	public String getCdErro() {
		return cdErro;
	}
	public void setCdErro(String cdErro) {
		this.cdErro = cdErro;
	}

	public String getMsgErro() {
		return msgErro;
	}
	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	public String getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}

	public String getNegociacao() {
		return negociacao;
	}
	public void setNegociacao(String negociacao) {
		this.negociacao = negociacao;
	}

	public String getClubBanco() {
		return clubBanco;
	}
	public void setClubBanco(String clubBanco) {
		this.clubBanco = clubBanco;
	}

	public String getTpContrato() {
		return tpContrato;
	}
	public void setTpContrato(String tpContrato) {
		this.tpContrato = tpContrato;
	}

	public String getNuSequenciaContrato() {
		return nuSequenciaContrato;
	}
	public void setNuSequenciaContrato(String nuSequenciaContrato) {
		this.nuSequenciaContrato = nuSequenciaContrato;
	}

	public String getCdProduto() {
		return cdProduto;
	}
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}

	public String getNuTituloGerado() {
		return nuTituloGerado;
	}
	public void setNuTituloGerado(String nuTituloGerado) {
		this.nuTituloGerado = nuTituloGerado;
	}

	public String getAgenciaCreditoBeneficiario() {
		return agenciaCreditoBeneficiario;
	}

	public void setAgenciaCreditoBeneficiario(String agenciaCreditoBeneficiario) {
		this.agenciaCreditoBeneficiario = agenciaCreditoBeneficiario;
	}

	public String getContaCreditoBeneficiario() {
		return contaCreditoBeneficiario;
	}

	public void setContaCreditoBeneficiario(String contaCreditoBeneficiario) {
		this.contaCreditoBeneficiario = contaCreditoBeneficiario;
	}

	public String getDigCreditoBeneficiario() {
		return digCreditoBeneficiario;
	}

	public void setDigCreditoBeneficiario(String digCreditoBeneficiario) {
		this.digCreditoBeneficiario = digCreditoBeneficiario;
	}

	public String getCdCipTitulo() {
		return cdCipTitulo;
	}

	public void setCdCipTitulo(String cdCipTitulo) {
		this.cdCipTitulo = cdCipTitulo;
	}

	public String getStatusTitulo() {
		return statusTitulo;
	}

	public void setStatusTitulo(String statusTitulo) {
		this.statusTitulo = statusTitulo;
	}

	public String getDescStatusTitulo() {
		return descStatusTitulo;
	}

	public void setDescStatusTitulo(String descStatusTitulo) {
		this.descStatusTitulo = descStatusTitulo;
	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getLogradouroBeneficiario() {
		return logradouroBeneficiario;
	}

	public void setLogradouroBeneficiario(String logradouroBeneficiario) {
		this.logradouroBeneficiario = logradouroBeneficiario;
	}

	public String getNuLogradouroBeneficiario() {
		return nuLogradouroBeneficiario;
	}

	public void setNuLogradouroBeneficiario(String nuLogradouroBeneficiario) {
		this.nuLogradouroBeneficiario = nuLogradouroBeneficiario;
	}

	public String getComplementoLogradouroBeneficiario() {
		return complementoLogradouroBeneficiario;
	}

	public void setComplementoLogradouroBeneficiario(String complementoLogradouroBeneficiario) {
		this.complementoLogradouroBeneficiario = complementoLogradouroBeneficiario;
	}

	public String getBairroBeneficiario() {
		return bairroBeneficiario;
	}

	public void setBairroBeneficiario(String bairroBeneficiario) {
		this.bairroBeneficiario = bairroBeneficiario;
	}

	public String getCepBeneficiario() {
		return cepBeneficiario;
	}

	public void setCepBeneficiario(String cepBeneficiario) {
		this.cepBeneficiario = cepBeneficiario;
	}

	public String getCepComplementoBeneficiario() {
		return cepComplementoBeneficiario;
	}

	public void setCepComplementoBeneficiario(String cepComplementoBeneficiario) {
		this.cepComplementoBeneficiario = cepComplementoBeneficiario;
	}

	public String getMunicipioBeneficiario() {
		return municipioBeneficiario;
	}

	public void setMunicipioBeneficiario(String municipioBeneficiario) {
		this.municipioBeneficiario = municipioBeneficiario;
	}

	public String getUfBeneficiario() {
		return ufBeneficiario;
	}

	public void setUfBeneficiario(String ufBeneficiario) {
		this.ufBeneficiario = ufBeneficiario;
	}

	public String getRazaoContaBeneficiario() {
		return razaoContaBeneficiario;
	}

	public void setRazaoContaBeneficiario(String razaoContaBeneficiario) {
		this.razaoContaBeneficiario = razaoContaBeneficiario;
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getCpfcnpjPagador() {
		return cpfcnpjPagador;
	}

	public void setCpfcnpjPagador(String cpfcnpjPagador) {
		this.cpfcnpjPagador = cpfcnpjPagador;
	}

	public String getEnderecoPagador() {
		return enderecoPagador;
	}

	public void setEnderecoPagador(String enderecoPagador) {
		this.enderecoPagador = enderecoPagador;
	}

	public String getBairroPagador() {
		return bairroPagador;
	}

	public void setBairroPagador(String bairroPagador) {
		this.bairroPagador = bairroPagador;
	}

	public String getMunicipioPagador() {
		return municipioPagador;
	}

	public void setMunicipioPagador(String municipioPagador) {
		this.municipioPagador = municipioPagador;
	}

	public String getUfPagador() {
		return ufPagador;
	}

	public void setUfPagador(String ufPagador) {
		this.ufPagador = ufPagador;
	}

	public String getCepPagador() {
		return cepPagador;
	}

	public void setCepPagador(String cepPagador) {
		this.cepPagador = cepPagador;
	}

	public String getCepComplementoPagador() {
		return cepComplementoPagador;
	}

	public void setCepComplementoPagador(String cepComplementoPagador) {
		this.cepComplementoPagador = cepComplementoPagador;
	}

	public String getEndEletronicoPagador() {
		return endEletronicoPagador;
	}

	public void setEndEletronicoPagador(String endEletronicoPagador) {
		this.endEletronicoPagador = endEletronicoPagador;
	}

	public String getNomeSacadorAvalista() {
		return nomeSacadorAvalista;
	}

	public void setNomeSacadorAvalista(String nomeSacadorAvalista) {
		this.nomeSacadorAvalista = nomeSacadorAvalista;
	}

	public String getCpfcnpjSacadorAvalista() {
		return cpfcnpjSacadorAvalista;
	}

	public void setCpfcnpjSacadorAvalista(String cpfcnpjSacadorAvalista) {
		this.cpfcnpjSacadorAvalista = cpfcnpjSacadorAvalista;
	}

	public String getEnderecoSacadorAvalista() {
		return enderecoSacadorAvalista;
	}

	public void setEnderecoSacadorAvalista(String enderecoSacadorAvalista) {
		this.enderecoSacadorAvalista = enderecoSacadorAvalista;
	}

	public String getMunicipioSacadorAvalista() {
		return municipioSacadorAvalista;
	}

	public void setMunicipioSacadorAvalista(String municipioSacadorAvalista) {
		this.municipioSacadorAvalista = municipioSacadorAvalista;
	}

	public String getUfSacadorAvalista() {
		return ufSacadorAvalista;
	}

	public void setUfSacadorAvalista(String ufSacadorAvalista) {
		this.ufSacadorAvalista = ufSacadorAvalista;
	}

	public String getCepSacadorAvalista() {
		return cepSacadorAvalista;
	}

	public void setCepSacadorAvalista(String cepSacadorAvalista) {
		this.cepSacadorAvalista = cepSacadorAvalista;
	}

	public String getCepComplementoSacadorAvalista() {
		return cepComplementoSacadorAvalista;
	}

	public void setCepComplementoSacadorAvalista(String cepComplementoSacadorAvalista) {
		this.cepComplementoSacadorAvalista = cepComplementoSacadorAvalista;
	}

	public String getNumeroTitulo() {
		return numeroTitulo;
	}

	public void setNumeroTitulo(String numeroTitulo) {
		this.numeroTitulo = numeroTitulo;
	}

	public String getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(String dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public String getEspecieDocumentoTitulo() {
		return especieDocumentoTitulo;
	}

	public void setEspecieDocumentoTitulo(String especieDocumentoTitulo) {
		this.especieDocumentoTitulo = especieDocumentoTitulo;
	}

	public String getDescEspecie() {
		return descEspecie;
	}

	public void setDescEspecie(String descEspecie) {
		this.descEspecie = descEspecie;
	}

	public String getVlIOF() {
		return vlIOF;
	}

	public void setVlIOF(String vlIOF) {
		this.vlIOF = vlIOF;
	}

	public String getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public String getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(String dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getVlTitulo() {
		return vlTitulo;
	}

	public void setVlTitulo(String vlTitulo) {
		this.vlTitulo = vlTitulo;
	}

	public String getVlAbatimento() {
		return vlAbatimento;
	}

	public void setVlAbatimento(String vlAbatimento) {
		this.vlAbatimento = vlAbatimento;
	}

	public String getDtInstrucaoProtestoNegativacao() {
		return dtInstrucaoProtestoNegativacao;
	}

	public void setDtInstrucaoProtestoNegativacao(String dtInstrucaoProtestoNegativacao) {
		this.dtInstrucaoProtestoNegativacao = dtInstrucaoProtestoNegativacao;
	}

	public String getDiasInstrucaoProtestoNegativacao() {
		return diasInstrucaoProtestoNegativacao;
	}

	public void setDiasInstrucaoProtestoNegativacao(String diasInstrucaoProtestoNegativacao) {
		this.diasInstrucaoProtestoNegativacao = diasInstrucaoProtestoNegativacao;
	}

	public String getDtMulta() {
		return dtMulta;
	}

	public void setDtMulta(String dtMulta) {
		this.dtMulta = dtMulta;
	}

	public String getVlMulta() {
		return vlMulta;
	}

	public void setVlMulta(String vlMulta) {
		this.vlMulta = vlMulta;
	}

	public String getQtdeCasasDecimaisMulta() {
		return qtdeCasasDecimaisMulta;
	}

	public void setQtdeCasasDecimaisMulta(String qtdeCasasDecimaisMulta) {
		this.qtdeCasasDecimaisMulta = qtdeCasasDecimaisMulta;
	}

	public String getCdValorMulta() {
		return cdValorMulta;
	}

	public void setCdValorMulta(String cdValorMulta) {
		this.cdValorMulta = cdValorMulta;
	}

	public String getDescCdMulta() {
		return descCdMulta;
	}

	public void setDescCdMulta(String descCdMulta) {
		this.descCdMulta = descCdMulta;
	}

	public String getDtJuros() {
		return dtJuros;
	}

	public void setDtJuros(String dtJuros) {
		this.dtJuros = dtJuros;
	}

	public String getVlJurosAoDia() {
		return vlJurosAoDia;
	}

	public void setVlJurosAoDia(String vlJurosAoDia) {
		this.vlJurosAoDia = vlJurosAoDia;
	}

	public String getDtDesconto1Bonificacao() {
		return dtDesconto1Bonificacao;
	}

	public void setDtDesconto1Bonificacao(String dtDesconto1Bonificacao) {
		this.dtDesconto1Bonificacao = dtDesconto1Bonificacao;
	}

	public String getVlDesconto1Bonificacao() {
		return vlDesconto1Bonificacao;
	}

	public void setVlDesconto1Bonificacao(String vlDesconto1Bonificacao) {
		this.vlDesconto1Bonificacao = vlDesconto1Bonificacao;
	}

	public String getQtdeCasasDecimaisDesconto1Bonificacao() {
		return qtdeCasasDecimaisDesconto1Bonificacao;
	}

	public void setQtdeCasasDecimaisDesconto1Bonificacao(String qtdeCasasDecimaisDesconto1Bonificacao) {
		this.qtdeCasasDecimaisDesconto1Bonificacao = qtdeCasasDecimaisDesconto1Bonificacao;
	}

	public String getCdValorDesconto1Bonificacao() {
		return cdValorDesconto1Bonificacao;
	}

	public void setCdValorDesconto1Bonificacao(String cdValorDesconto1Bonificacao) {
		this.cdValorDesconto1Bonificacao = cdValorDesconto1Bonificacao;
	}

	public String getDescCdDesconto1Bonificacao() {
		return descCdDesconto1Bonificacao;
	}

	public void setDescCdDesconto1Bonificacao(String descCdDesconto1Bonificacao) {
		this.descCdDesconto1Bonificacao = descCdDesconto1Bonificacao;
	}

	public String getDtDesconto2() {
		return dtDesconto2;
	}

	public void setDtDesconto2(String dtDesconto2) {
		this.dtDesconto2 = dtDesconto2;
	}

	public String getVlDesconto2() {
		return vlDesconto2;
	}

	public void setVlDesconto2(String vlDesconto2) {
		this.vlDesconto2 = vlDesconto2;
	}

	public String getQtdeCasasDecimaisDesconto2() {
		return qtdeCasasDecimaisDesconto2;
	}

	public void setQtdeCasasDecimaisDesconto2(String qtdeCasasDecimaisDesconto2) {
		this.qtdeCasasDecimaisDesconto2 = qtdeCasasDecimaisDesconto2;
	}

	public String getCdValorDesconto2() {
		return cdValorDesconto2;
	}

	public void setCdValorDesconto2(String cdValorDesconto2) {
		this.cdValorDesconto2 = cdValorDesconto2;
	}

	public String getDescCdDesconto2() {
		return descCdDesconto2;
	}

	public void setDescCdDesconto2(String descCdDesconto2) {
		this.descCdDesconto2 = descCdDesconto2;
	}

	public String getDtDesconto3() {
		return dtDesconto3;
	}

	public void setDtDesconto3(String dtDesconto3) {
		this.dtDesconto3 = dtDesconto3;
	}

	public String getVlDesconto3() {
		return vlDesconto3;
	}

	public void setVlDesconto3(String vlDesconto3) {
		this.vlDesconto3 = vlDesconto3;
	}

	public String getQtdeCasasDecimaisDesconto3() {
		return qtdeCasasDecimaisDesconto3;
	}

	public void setQtdeCasasDecimaisDesconto3(String qtdeCasasDecimaisDesconto3) {
		this.qtdeCasasDecimaisDesconto3 = qtdeCasasDecimaisDesconto3;
	}

	public String getCdValorDesconto3() {
		return cdValorDesconto3;
	}

	public void setCdValorDesconto3(String cdValorDesconto3) {
		this.cdValorDesconto3 = cdValorDesconto3;
	}

	public String getDescCdDesconto3() {
		return descCdDesconto3;
	}

	public void setDescCdDesconto3(String descCdDesconto3) {
		this.descCdDesconto3 = descCdDesconto3;
	}

	public String getDiasDispensaMulta() {
		return diasDispensaMulta;
	}

	public void setDiasDispensaMulta(String diasDispensaMulta) {
		this.diasDispensaMulta = diasDispensaMulta;
	}

	public String getDiasDispensaJuros() {
		return diasDispensaJuros;
	}

	public void setDiasDispensaJuros(String diasDispensaJuros) {
		this.diasDispensaJuros = diasDispensaJuros;
	}

	public String getCdBarras() {
		return cdBarras;
	}

	public void setCdBarras(String cdBarras) {
		this.cdBarras = cdBarras;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getCdAcessorioEscrituralEmpresa() {
		return cdAcessorioEscrituralEmpresa;
	}

	public void setCdAcessorioEscrituralEmpresa(String cdAcessorioEscrituralEmpresa) {
		this.cdAcessorioEscrituralEmpresa = cdAcessorioEscrituralEmpresa;
	}

	public String getTpVencimento() {
		return tpVencimento;
	}

	public void setTpVencimento(String tpVencimento) {
		this.tpVencimento = tpVencimento;
	}

	public String getIndInstrucaoProtesto() {
		return indInstrucaoProtesto;
	}

	public void setIndInstrucaoProtesto(String indInstrucaoProtesto) {
		this.indInstrucaoProtesto = indInstrucaoProtesto;
	}

	public String getTipoAbatimentoTitulo() {
		return tipoAbatimentoTitulo;
	}

	public void setTipoAbatimentoTitulo(String tipoAbatimentoTitulo) {
		this.tipoAbatimentoTitulo = tipoAbatimentoTitulo;
	}

	public String getCdValorJuros() {
		return cdValorJuros;
	}

	public void setCdValorJuros(String cdValorJuros) {
		this.cdValorJuros = cdValorJuros;
	}

	public String getTpDesconto1() {
		return tpDesconto1;
	}

	public void setTpDesconto1(String tpDesconto1) {
		this.tpDesconto1 = tpDesconto1;
	}

	public String getTpDesconto2() {
		return tpDesconto2;
	}

	public void setTpDesconto2(String tpDesconto2) {
		this.tpDesconto2 = tpDesconto2;
	}

	public String getTpDesconto3() {
		return tpDesconto3;
	}

	public void setTpDesconto3(String tpDesconto3) {
		this.tpDesconto3 = tpDesconto3;
	}

	public String getNuControleParticipante() {
		return nuControleParticipante;
	}

	public void setNuControleParticipante(String nuControleParticipante) {
		this.nuControleParticipante = nuControleParticipante;
	}

	public String getDiasJuros() {
		return diasJuros;
	}

	public void setDiasJuros(String diasJuros) {
		this.diasJuros = diasJuros;
	}

	public String getCdJuros() {
		return cdJuros;
	}

	public void setCdJuros(String cdJuros) {
		this.cdJuros = cdJuros;
	}

	public String getVlJuros() {
		return vlJuros;
	}

	public void setVlJuros(String vlJuros) {
		this.vlJuros = vlJuros;
	}

	public String getCpfcnpjBeneficiario() {
		return cpfcnpjBeneficiario;
	}

	public void setCpfcnpjBeneficiario(String cpfcnpjBeneficiario) {
		this.cpfcnpjBeneficiario = cpfcnpjBeneficiario;
	}

	public String getVlTituloEmitidoBoleto() {
		return vlTituloEmitidoBoleto;
	}

	public void setVlTituloEmitidoBoleto(String vlTituloEmitidoBoleto) {
		this.vlTituloEmitidoBoleto = vlTituloEmitidoBoleto;
	}

	public String getDtVencimentoBoleto() {
		return dtVencimentoBoleto;
	}

	public void setDtVencimentoBoleto(String dtVencimentoBoleto) {
		this.dtVencimentoBoleto = dtVencimentoBoleto;
	}

	public String getIndTituloPertenceBaseTitulos() {
		return indTituloPertenceBaseTitulos;
	}

	public void setIndTituloPertenceBaseTitulos(String indTituloPertenceBaseTitulos) {
		this.indTituloPertenceBaseTitulos = indTituloPertenceBaseTitulos;
	}

	public String getDtLimitePagamentoBoleto() {
		return dtLimitePagamentoBoleto;
	}

	public void setDtLimitePagamentoBoleto(String dtLimitePagamentoBoleto) {
		this.dtLimitePagamentoBoleto = dtLimitePagamentoBoleto;
	}

	public String getCdIdentificacaoTituloDDACIP() {
		return cdIdentificacaoTituloDDACIP;
	}

	public void setCdIdentificacaoTituloDDACIP(String cdIdentificacaoTituloDDACIP) {
		this.cdIdentificacaoTituloDDACIP = cdIdentificacaoTituloDDACIP;
	}

	public String getIndPagamentoParcial() {
		return indPagamentoParcial;
	}

	public void setIndPagamentoParcial(String indPagamentoParcial) {
		this.indPagamentoParcial = indPagamentoParcial;
	}

	public String getQtdePagamentoParciais() {
		return qtdePagamentoParciais;
	}

	public void setQtdePagamentoParciais(String qtdePagamentoParciais) {
		this.qtdePagamentoParciais = qtdePagamentoParciais;
	}
		
}