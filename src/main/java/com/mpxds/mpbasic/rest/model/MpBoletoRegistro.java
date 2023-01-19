package com.mpxds.mpbasic.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MpBoletoRegistro {
	//
	/*
	 * "{\"nuCPFCNPJ\": \"123456789\", \"filialCPFCNPJ\": \"0001\",
	 * \"ctrlCPFCNPJ\": \"39\", \"cdTipoAcesso\": \"2\", \"clubBanco\": \"0\",
	 * \"cdTipoContrato\": \"0\", \"nuSequenciaContrato\": \"0\",
	 * \"idProduto\": \"09\", \"nuNegociacao\": \"123400000001234567\",
	 * \"cdBanco\": \"237\", \"eNuSequenciaContrato\": \"0\", \"tpRegistro\": \"1\",
	 * \"cdProduto\": \"0\", \"nuTitulo\": \"0\", \"nuCliente\": \"123456\",
	 * \"dtEmissaoTitulo\": \"25.05.2017\", \"dtVencimentoTitulo\": \"20.06.2017\",
	 * \"tpVencimento\": \"0\", \"vlNominalTitulo\": \"100\",
	 * \"cdEspecieTitulo\": \"04\", \"tpProtestoAutomaticoNegativacao\": \"0\",
	 * \"prazoProtestoAutomaticoNegativacao\": \"0\",
	 * \"controleParticipante\": \"\", \"cdPagamentoParcial\": \"\",
	 * \"qtdePagamentoParcial\": \"0\", \"percentualJuros\": \"0\",
	 * \"vlJuros\": \"0\", \"qtdeDiasJuros\": \"0\", \"percentualMulta\": \"0\",
	 * \"vlMulta\": \"0\", \"qtdeDiasMulta\": \"0\", \"percentualDesconto1\": \"0\",
	 * \"vlDesconto1\": \"0\", \"dataLimiteDesconto1\": \"\",
	 * \"percentualDesconto2\": \"0\", \"vlDesconto2\": \"0\",
	 * \"dataLimiteDesconto2\": \"\", \"percentualDesconto3\": \"0\",
	 * \"vlDesconto3\": \"0\", \"dataLimiteDesconto3\": \"\",
	 * \"prazoBonificacao\": \"0\", \"percentualBonificacao\": \"0\",
	 * \"vlBonificacao\": \"0\", \"dtLimiteBonificacao\": \"\",
	 * \"vlAbatimento\": \"0\", \"vlIOF\": \"0\",
	 * \"nomePagador\": \"Cliente Teste\", \"logradouroPagador\": \"rua Teste\",
	 * \"nuLogradouroPagador\": \"90\", \"complementoLogradouroPagador\": \"\",
	 * \"cepPagador\": \"12345\", \"complementoCepPagador\": \"500\",
	 * \"bairroPagador\": \"bairro Teste\", \"municipioPagador\": \"Teste\",
	 * \"ufPagador\": \"SP\", \"cdIndCpfcnpjPagador\": \"1\",
	 * \"nuCpfcnpjPagador\": \"12345648901234\", \"endEletronicoPagador\": \"\",
	 * \"nomeSacadorAvalista\": \"\", \"logradouroSacadorAvalista\": \"\",
	 * \"nuLogradouroSacadorAvalista\": \"0\",
	 * \"complementoLogradouroSacadorAvalista\": \"\",
	 * \"cepSacadorAvalista\": \"0\", \"complementoCepSacadorAvalista\": \"0\",
	 * \"bairroSacadorAvalista\": \"\", \"municipioSacadorAvalista\": \"\",
	 * \"ufSacadorAvalista\": \"\", \"cdIndCpfcnpjSacadorAvalista\": \"0\",
	 * \"nuCpfcnpjSacadorAvalista\": \"0\",
	 * \"endEletronicoSacadorAvalista\": \"\"}";
	 */
	
	// ---
	
	private String nuCPFCNPJ;
	private String filialCPFCNPJ;
	private String ctrlCPFCNPJ;
	private String cdTipoAcesso;
	private String clubBanco;
	private String cdTipoContrato;
	private String nuSequenciaContrato;
	private String idProduto;
	private String nuNegociacao;
	private String cdBanco;
	private String eNuSequenciaContrato;
	private String tpRegistro;
	private String cdProduto;
	private String nuTitulo;
	private String nuCliente;
	private String dtEmissaoTitulo;
	private String dtVencimentoTitulo;
	private String tpVencimento;
	private String vlNominalTitulo;
	private String cdEspecieTitulo;
	private String tpProtestoAutomaticoNegativacao;
	private String prazoProtestoAutomaticoNegativacao;
	private String controleParticipante;
	private String cdPagamentoParcial;
	private String qtdePagamentoParcial;
	private String percentualJuros;
	private String vlJuros;
	private String qtdeDiasJuros;
	private String percentualMulta;
	private String vlMulta;
	private String qtdeDiasMulta;
	private String percentualDesconto1;
	private String vlDesconto1;
	private String dataLimiteDesconto1;
	private String percentualDesconto2;
	private String vlDesconto2;
	private String dataLimiteDesconto2;
	private String percentualDesconto3;
	private String vlDesconto3;
	private String dataLimiteDesconto3;
	private String prazoBonificacao;
	private String percentualBonificacao;
	private String vlBonificacao;
	private String dtLimiteBonificacao;
	private String vlAbatimento;
	private String vlIOF;
	private String nomePagador;
	private String logradouroPagador;
	private String nuLogradouroPagador;
	private String complementoLogradouroPagador;
	private String cepPagador;
	private String complementoCepPagador;
	private String bairroPagador;
	private String municipioPagador;
	private String ufPagador;
	private String cdIndCpfcnpjPagador;
	private String nuCpfcnpjPagador;
	private String endEletronicoPagador;
	private String nomeSacadorAvalista;
	private String logradouroSacadorAvalista;
	private String nuLogradouroSacadorAvalista;
	private String complementoLogradouroSacadorAvalista;
	private String cepSacadorAvalista;
	private String complementoCepSacadorAvalista;
	private String bairroSacadorAvalista;
	private String municipioSacadorAvalista;
	private String ufSacadorAvalista;
	private String cdIndCpfcnpjSacadorAvalista;
	private String nuCpfcnpjSacadorAvalista;
	private String endEletronicoSacadorAvalista;

	// ---
	public MpBoletoRegistro() {
		//
		super();
	}
	
	public MpBoletoRegistro(String nuCPFCNPJ, String filialCPFCNPJ, String ctrlCPFCNPJ, String cdTipoAcesso,
			String clubBanco, String cdTipoContrato, String nuSequenciaContrato, String idProduto, String nuNegociacao,
			String cdBanco, String eNuSequenciaContrato, String tpRegistro, String cdProduto, String nuTitulo,
			String nuCliente, String dtEmissaoTitulo, String dtVencimentoTitulo, String tpVencimento,
			String vlNominalTitulo, String cdEspecieTitulo, String tpProtestoAutomaticoNegativacao,
			String prazoProtestoAutomaticoNegativacao, String controleParticipante, String cdPagamentoParcial,
			String qtdePagamentoParcial, String percentualJuros, String vlJuros, String qtdeDiasJuros,
			String percentualMulta, String vlMulta, String qtdeDiasMulta, String percentualDesconto1,
			String vlDesconto1, String dataLimiteDesconto1, String percentualDesconto2, String vlDesconto2,
			String dataLimiteDesconto2, String percentualDesconto3, String vlDesconto3, String dataLimiteDesconto3,
			String prazoBonificacao, String percentualBonificacao, String vlBonificacao, String dtLimiteBonificacao,
			String vlAbatimento, String vlIOF, String nomePagador, String logradouroPagador, String nuLogradouroPagador,
			String complementoLogradouroPagador, String cepPagador, String complementoCepPagador, String bairroPagador,
			String municipioPagador, String ufPagador, String cdIndCpfcnpjPagador, String nuCpfcnpjPagador,
			String endEletronicoPagador, String nomeSacadorAvalista, String logradouroSacadorAvalista,
			String nuLogradouroSacadorAvalista, String complementoLogradouroSacadorAvalista, String cepSacadorAvalista,
			String complementoCepSacadorAvalista, String bairroSacadorAvalista, String municipioSacadorAvalista,
			String ufSacadorAvalista, String cdIndCpfcnpjSacadorAvalista, String nuCpfcnpjSacadorAvalista,
			String endEletronicoSacadorAvalista) {
		//
		super();
		
		this.nuCPFCNPJ = nuCPFCNPJ;
		this.filialCPFCNPJ = filialCPFCNPJ;
		this.ctrlCPFCNPJ = ctrlCPFCNPJ;
		this.cdTipoAcesso = cdTipoAcesso;
		this.clubBanco = clubBanco;
		this.cdTipoContrato = cdTipoContrato;
		this.nuSequenciaContrato = nuSequenciaContrato;
		this.idProduto = idProduto;
		this.nuNegociacao = nuNegociacao;
		this.cdBanco = cdBanco;
		this.eNuSequenciaContrato = eNuSequenciaContrato;
		this.tpRegistro = tpRegistro;
		this.cdProduto = cdProduto;
		this.nuTitulo = nuTitulo;
		this.nuCliente = nuCliente;
		this.dtEmissaoTitulo = dtEmissaoTitulo;
		this.dtVencimentoTitulo = dtVencimentoTitulo;
		this.tpVencimento = tpVencimento;
		this.vlNominalTitulo = vlNominalTitulo;
		this.cdEspecieTitulo = cdEspecieTitulo;
		this.tpProtestoAutomaticoNegativacao = tpProtestoAutomaticoNegativacao;
		this.prazoProtestoAutomaticoNegativacao = prazoProtestoAutomaticoNegativacao;
		this.controleParticipante = controleParticipante;
		this.cdPagamentoParcial = cdPagamentoParcial;
		this.qtdePagamentoParcial = qtdePagamentoParcial;
		this.percentualJuros = percentualJuros;
		this.vlJuros = vlJuros;
		this.qtdeDiasJuros = qtdeDiasJuros;
		this.percentualMulta = percentualMulta;
		this.vlMulta = vlMulta;
		this.qtdeDiasMulta = qtdeDiasMulta;
		this.percentualDesconto1 = percentualDesconto1;
		this.vlDesconto1 = vlDesconto1;
		this.dataLimiteDesconto1 = dataLimiteDesconto1;
		this.percentualDesconto2 = percentualDesconto2;
		this.vlDesconto2 = vlDesconto2;
		this.dataLimiteDesconto2 = dataLimiteDesconto2;
		this.percentualDesconto3 = percentualDesconto3;
		this.vlDesconto3 = vlDesconto3;
		this.dataLimiteDesconto3 = dataLimiteDesconto3;
		this.prazoBonificacao = prazoBonificacao;
		this.percentualBonificacao = percentualBonificacao;
		this.vlBonificacao = vlBonificacao;
		this.dtLimiteBonificacao = dtLimiteBonificacao;
		this.vlAbatimento = vlAbatimento;
		this.vlIOF = vlIOF;
		this.nomePagador = nomePagador;
		this.logradouroPagador = logradouroPagador;
		this.nuLogradouroPagador = nuLogradouroPagador;
		this.complementoLogradouroPagador = complementoLogradouroPagador;
		this.cepPagador = cepPagador;
		this.complementoCepPagador = complementoCepPagador;
		this.bairroPagador = bairroPagador;
		this.municipioPagador = municipioPagador;
		this.ufPagador = ufPagador;
		this.cdIndCpfcnpjPagador = cdIndCpfcnpjPagador;
		this.nuCpfcnpjPagador = nuCpfcnpjPagador;
		this.endEletronicoPagador = endEletronicoPagador;
		this.nomeSacadorAvalista = nomeSacadorAvalista;
		this.logradouroSacadorAvalista = logradouroSacadorAvalista;
		this.nuLogradouroSacadorAvalista = nuLogradouroSacadorAvalista;
		this.complementoLogradouroSacadorAvalista = complementoLogradouroSacadorAvalista;
		this.cepSacadorAvalista = cepSacadorAvalista;
		this.complementoCepSacadorAvalista = complementoCepSacadorAvalista;
		this.bairroSacadorAvalista = bairroSacadorAvalista;
		this.municipioSacadorAvalista = municipioSacadorAvalista;
		this.ufSacadorAvalista = ufSacadorAvalista;
		this.cdIndCpfcnpjSacadorAvalista = cdIndCpfcnpjSacadorAvalista;
		this.nuCpfcnpjSacadorAvalista = nuCpfcnpjSacadorAvalista;
		this.endEletronicoSacadorAvalista = endEletronicoSacadorAvalista;
	}

	// ---
	
	public String getNuCPFCNPJ() {
		return nuCPFCNPJ;
	}
	public void setNuCPFCNPJ(String nuCPFCNPJ) {
		this.nuCPFCNPJ = nuCPFCNPJ;
	}
	public String getFilialCPFCNPJ() {
		return filialCPFCNPJ;
	}
	public void setFilialCPFCNPJ(String filialCPFCNPJ) {
		this.filialCPFCNPJ = filialCPFCNPJ;
	}
	public String getCtrlCPFCNPJ() {
		return ctrlCPFCNPJ;
	}
	public void setCtrlCPFCNPJ(String ctrlCPFCNPJ) {
		this.ctrlCPFCNPJ = ctrlCPFCNPJ;
	}
	public String getCdTipoAcesso() {
		return cdTipoAcesso;
	}
	public void setCdTipoAcesso(String cdTipoAcesso) {
		this.cdTipoAcesso = cdTipoAcesso;
	}
	public String getClubBanco() {
		return clubBanco;
	}
	public void setClubBanco(String clubBanco) {
		this.clubBanco = clubBanco;
	}
	public String getCdTipoContrato() {
		return cdTipoContrato;
	}
	public void setCdTipoContrato(String cdTipoContrato) {
		this.cdTipoContrato = cdTipoContrato;
	}
	public String getNuSequenciaContrato() {
		return nuSequenciaContrato;
	}
	public void setNuSequenciaContrato(String nuSequenciaContrato) {
		this.nuSequenciaContrato = nuSequenciaContrato;
	}
	public String getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}
	public String getNuNegociacao() {
		return nuNegociacao;
	}
	public void setNuNegociacao(String nuNegociacao) {
		this.nuNegociacao = nuNegociacao;
	}
	public String getCdBanco() {
		return cdBanco;
	}
	public void setCdBanco(String cdBanco) {
		this.cdBanco = cdBanco;
	}
	public String geteNuSequenciaContrato() {
		return eNuSequenciaContrato;
	}
	public void seteNuSequenciaContrato(String eNuSequenciaContrato) {
		this.eNuSequenciaContrato = eNuSequenciaContrato;
	}
	public String getTpRegistro() {
		return tpRegistro;
	}
	public void setTpRegistro(String tpRegistro) {
		this.tpRegistro = tpRegistro;
	}
	public String getCdProduto() {
		return cdProduto;
	}
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}
	public String getNuTitulo() {
		return nuTitulo;
	}
	public void setNuTitulo(String nuTitulo) {
		this.nuTitulo = nuTitulo;
	}
	public String getNuCliente() {
		return nuCliente;
	}
	public void setNuCliente(String nuCliente) {
		this.nuCliente = nuCliente;
	}
	public String getDtEmissaoTitulo() {
		return dtEmissaoTitulo;
	}
	public void setDtEmissaoTitulo(String dtEmissaoTitulo) {
		this.dtEmissaoTitulo = dtEmissaoTitulo;
	}
	public String getDtVencimentoTitulo() {
		return dtVencimentoTitulo;
	}
	public void setDtVencimentoTitulo(String dtVencimentoTitulo) {
		this.dtVencimentoTitulo = dtVencimentoTitulo;
	}
	public String getTpVencimento() {
		return tpVencimento;
	}
	public void setTpVencimento(String tpVencimento) {
		this.tpVencimento = tpVencimento;
	}
	public String getVlNominalTitulo() {
		return vlNominalTitulo;
	}
	public void setVlNominalTitulo(String vlNominalTitulo) {
		this.vlNominalTitulo = vlNominalTitulo;
	}
	public String getCdEspecieTitulo() {
		return cdEspecieTitulo;
	}
	public void setCdEspecieTitulo(String cdEspecieTitulo) {
		this.cdEspecieTitulo = cdEspecieTitulo;
	}
	public String getTpProtestoAutomaticoNegativacao() {
		return tpProtestoAutomaticoNegativacao;
	}
	public void setTpProtestoAutomaticoNegativacao(String tpProtestoAutomaticoNegativacao) {
		this.tpProtestoAutomaticoNegativacao = tpProtestoAutomaticoNegativacao;
	}
	public String getPrazoProtestoAutomaticoNegativacao() {
		return prazoProtestoAutomaticoNegativacao;
	}
	public void setPrazoProtestoAutomaticoNegativacao(String prazoProtestoAutomaticoNegativacao) {
		this.prazoProtestoAutomaticoNegativacao = prazoProtestoAutomaticoNegativacao;
	}
	public String getControleParticipante() {
		return controleParticipante;
	}
	public void setControleParticipante(String controleParticipante) {
		this.controleParticipante = controleParticipante;
	}
	public String getCdPagamentoParcial() {
		return cdPagamentoParcial;
	}
	public void setCdPagamentoParcial(String cdPagamentoParcial) {
		this.cdPagamentoParcial = cdPagamentoParcial;
	}
	public String getQtdePagamentoParcial() {
		return qtdePagamentoParcial;
	}
	public void setQtdePagamentoParcial(String qtdePagamentoParcial) {
		this.qtdePagamentoParcial = qtdePagamentoParcial;
	}
	public String getPercentualJuros() {
		return percentualJuros;
	}
	public void setPercentualJuros(String percentualJuros) {
		this.percentualJuros = percentualJuros;
	}
	public String getVlJuros() {
		return vlJuros;
	}
	public void setVlJuros(String vlJuros) {
		this.vlJuros = vlJuros;
	}
	public String getQtdeDiasJuros() {
		return qtdeDiasJuros;
	}
	public void setQtdeDiasJuros(String qtdeDiasJuros) {
		this.qtdeDiasJuros = qtdeDiasJuros;
	}
	public String getPercentualMulta() {
		return percentualMulta;
	}
	public void setPercentualMulta(String percentualMulta) {
		this.percentualMulta = percentualMulta;
	}
	public String getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(String vlMulta) {
		this.vlMulta = vlMulta;
	}
	public String getQtdeDiasMulta() {
		return qtdeDiasMulta;
	}
	public void setQtdeDiasMulta(String qtdeDiasMulta) {
		this.qtdeDiasMulta = qtdeDiasMulta;
	}
	public String getPercentualDesconto1() {
		return percentualDesconto1;
	}
	public void setPercentualDesconto1(String percentualDesconto1) {
		this.percentualDesconto1 = percentualDesconto1;
	}
	public String getVlDesconto1() {
		return vlDesconto1;
	}
	public void setVlDesconto1(String vlDesconto1) {
		this.vlDesconto1 = vlDesconto1;
	}
	public String getDataLimiteDesconto1() {
		return dataLimiteDesconto1;
	}
	public void setDataLimiteDesconto1(String dataLimiteDesconto1) {
		this.dataLimiteDesconto1 = dataLimiteDesconto1;
	}
	public String getPercentualDesconto2() {
		return percentualDesconto2;
	}
	public void setPercentualDesconto2(String percentualDesconto2) {
		this.percentualDesconto2 = percentualDesconto2;
	}
	public String getVlDesconto2() {
		return vlDesconto2;
	}
	public void setVlDesconto2(String vlDesconto2) {
		this.vlDesconto2 = vlDesconto2;
	}
	public String getDataLimiteDesconto2() {
		return dataLimiteDesconto2;
	}
	public void setDataLimiteDesconto2(String dataLimiteDesconto2) {
		this.dataLimiteDesconto2 = dataLimiteDesconto2;
	}
	public String getPercentualDesconto3() {
		return percentualDesconto3;
	}
	public void setPercentualDesconto3(String percentualDesconto3) {
		this.percentualDesconto3 = percentualDesconto3;
	}
	public String getVlDesconto3() {
		return vlDesconto3;
	}
	public void setVlDesconto3(String vlDesconto3) {
		this.vlDesconto3 = vlDesconto3;
	}
	public String getDataLimiteDesconto3() {
		return dataLimiteDesconto3;
	}
	public void setDataLimiteDesconto3(String dataLimiteDesconto3) {
		this.dataLimiteDesconto3 = dataLimiteDesconto3;
	}
	public String getPrazoBonificacao() {
		return prazoBonificacao;
	}
	public void setPrazoBonificacao(String prazoBonificacao) {
		this.prazoBonificacao = prazoBonificacao;
	}
	public String getPercentualBonificacao() {
		return percentualBonificacao;
	}
	public void setPercentualBonificacao(String percentualBonificacao) {
		this.percentualBonificacao = percentualBonificacao;
	}
	public String getVlBonificacao() {
		return vlBonificacao;
	}
	public void setVlBonificacao(String vlBonificacao) {
		this.vlBonificacao = vlBonificacao;
	}
	public String getDtLimiteBonificacao() {
		return dtLimiteBonificacao;
	}
	public void setDtLimiteBonificacao(String dtLimiteBonificacao) {
		this.dtLimiteBonificacao = dtLimiteBonificacao;
	}
	public String getVlAbatimento() {
		return vlAbatimento;
	}
	public void setVlAbatimento(String vlAbatimento) {
		this.vlAbatimento = vlAbatimento;
	}
	public String getVlIOF() {
		return vlIOF;
	}
	public void setVlIOF(String vlIOF) {
		this.vlIOF = vlIOF;
	}
	public String getNomePagador() {
		return nomePagador;
	}
	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}
	public String getLogradouroPagador() {
		return logradouroPagador;
	}
	public void setLogradouroPagador(String logradouroPagador) {
		this.logradouroPagador = logradouroPagador;
	}
	public String getNuLogradouroPagador() {
		return nuLogradouroPagador;
	}
	public void setNuLogradouroPagador(String nuLogradouroPagador) {
		this.nuLogradouroPagador = nuLogradouroPagador;
	}
	public String getComplementoLogradouroPagador() {
		return complementoLogradouroPagador;
	}
	public void setComplementoLogradouroPagador(String complementoLogradouroPagador) {
		this.complementoLogradouroPagador = complementoLogradouroPagador;
	}
	public String getCepPagador() {
		return cepPagador;
	}
	public void setCepPagador(String cepPagador) {
		this.cepPagador = cepPagador;
	}
	public String getComplementoCepPagador() {
		return complementoCepPagador;
	}
	public void setComplementoCepPagador(String complementoCepPagador) {
		this.complementoCepPagador = complementoCepPagador;
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
	public String getCdIndCpfcnpjPagador() {
		return cdIndCpfcnpjPagador;
	}
	public void setCdIndCpfcnpjPagador(String cdIndCpfcnpjPagador) {
		this.cdIndCpfcnpjPagador = cdIndCpfcnpjPagador;
	}
	public String getNuCpfcnpjPagador() {
		return nuCpfcnpjPagador;
	}
	public void setNuCpfcnpjPagador(String nuCpfcnpjPagador) {
		this.nuCpfcnpjPagador = nuCpfcnpjPagador;
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
	public String getLogradouroSacadorAvalista() {
		return logradouroSacadorAvalista;
	}
	public void setLogradouroSacadorAvalista(String logradouroSacadorAvalista) {
		this.logradouroSacadorAvalista = logradouroSacadorAvalista;
	}
	public String getNuLogradouroSacadorAvalista() {
		return nuLogradouroSacadorAvalista;
	}
	public void setNuLogradouroSacadorAvalista(String nuLogradouroSacadorAvalista) {
		this.nuLogradouroSacadorAvalista = nuLogradouroSacadorAvalista;
	}
	public String getComplementoLogradouroSacadorAvalista() {
		return complementoLogradouroSacadorAvalista;
	}
	public void setComplementoLogradouroSacadorAvalista(String complementoLogradouroSacadorAvalista) {
		this.complementoLogradouroSacadorAvalista = complementoLogradouroSacadorAvalista;
	}
	public String getCepSacadorAvalista() {
		return cepSacadorAvalista;
	}
	public void setCepSacadorAvalista(String cepSacadorAvalista) {
		this.cepSacadorAvalista = cepSacadorAvalista;
	}
	public String getComplementoCepSacadorAvalista() {
		return complementoCepSacadorAvalista;
	}
	public void setComplementoCepSacadorAvalista(String complementoCepSacadorAvalista) {
		this.complementoCepSacadorAvalista = complementoCepSacadorAvalista;
	}
	public String getBairroSacadorAvalista() {
		return bairroSacadorAvalista;
	}
	public void setBairroSacadorAvalista(String bairroSacadorAvalista) {
		this.bairroSacadorAvalista = bairroSacadorAvalista;
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
	public String getCdIndCpfcnpjSacadorAvalista() {
		return cdIndCpfcnpjSacadorAvalista;
	}
	public void setCdIndCpfcnpjSacadorAvalista(String cdIndCpfcnpjSacadorAvalista) {
		this.cdIndCpfcnpjSacadorAvalista = cdIndCpfcnpjSacadorAvalista;
	}
	public String getNuCpfcnpjSacadorAvalista() {
		return nuCpfcnpjSacadorAvalista;
	}
	public void setNuCpfcnpjSacadorAvalista(String nuCpfcnpjSacadorAvalista) {
		this.nuCpfcnpjSacadorAvalista = nuCpfcnpjSacadorAvalista;
	}
	public String getEndEletronicoSacadorAvalista() {
		return endEletronicoSacadorAvalista;
	}
	public void setEndEletronicoSacadorAvalista(String endEletronicoSacadorAvalista) {
		this.endEletronicoSacadorAvalista = endEletronicoSacadorAvalista;
	}
	
}