package com.mpxds.mpbasic.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mpxds.mpbasic.model.MpEnderecoLocal;

@XmlRootElement()
public class MpBoletoDTO {
	//
	private Integer id;
	private String codigoOficio;
	private String numeroIntimacao;
	private String numeroIntimacaoCode = "";
	private String cpfCnpj;
	private String nomeSacado;
	private MpEnderecoLocal mpEnderecoLocal;
	private String numeroDocumento;
	private String especieDocumento;
	private String nossoNumero;
	private String nossoNumeroDig;
	private BigDecimal valorDocumento;
	private BigDecimal valorAcrescimo;
	private BigDecimal valorCobrado;
	private BigDecimal valorTarifa;
	private BigDecimal valorCPMF;
	private BigDecimal valorLeis;
	private Date dataDocumento;
	private Date dataVencimento;
	private String boletoInstrucao8;
	private String carteira;
	private String codigoBarras;
	private String agenciaCodigoCedente; 
	private String linhaDigitavel;
	private Date dataVencimento1;
	private String codigoBarras1;
	private String agenciaCodigoCedente1; 
	private String linhaDigitavel1;
	private BigDecimal valorIss;
	
	// ---
	
	public MpBoletoDTO(String codigoOficio, String numeroIntimacao, 
			String numeroIntimacaoCode, String cpfCnpj,
			String nomeSacado, MpEnderecoLocal mpEnderecoLocal, 
			String numeroDocumento, String especieDocumento,
			String nossoNumero, String nossoNumeroDig, 
			BigDecimal valorDocumento, BigDecimal valorAcrescimo, 
			BigDecimal valorCobrado, BigDecimal valorTarifa, 
			BigDecimal valorCPMF, BigDecimal valorLeis, 
			Date dataDocumento, Date dataVencimento, 
			String boletoInstrucao8, String carteira, 
			String codigoBarras, String agenciaCodigoCedente, 
			String linhaDigitavel,
			Date dataVencimento1, String codigoBarras1, 
			String agenciaCodigoCedente1, String linhaDigitavel1,
			BigDecimal valorIss) {
		//
		super();

		this.codigoOficio = codigoOficio;
		this.numeroIntimacao = numeroIntimacao;
		this.numeroIntimacaoCode = numeroIntimacaoCode;
		this.cpfCnpj = cpfCnpj;
		this.nomeSacado = nomeSacado;
		this.mpEnderecoLocal = mpEnderecoLocal; 
		this.numeroDocumento = numeroDocumento;
		this.especieDocumento = especieDocumento;
		this.nossoNumero = nossoNumero;
		this.nossoNumeroDig = nossoNumeroDig;
		this.valorDocumento = valorDocumento;
		this.valorAcrescimo = valorAcrescimo;
		this.valorCobrado = valorCobrado;
		this.valorTarifa = valorTarifa; 
		this.valorCPMF = valorCPMF;
		this.valorLeis = valorLeis;
		this.dataDocumento = dataDocumento;
		this.dataVencimento = dataVencimento;
		this.boletoInstrucao8 = boletoInstrucao8; 
		this.carteira = carteira;
		this.codigoBarras = codigoBarras;
		this.agenciaCodigoCedente = agenciaCodigoCedente;  
		this.linhaDigitavel = linhaDigitavel;
		this.dataVencimento1 = dataVencimento1;
		this.codigoBarras1 = codigoBarras1;
		this.agenciaCodigoCedente1 = agenciaCodigoCedente1;
		this.linhaDigitavel1 = linhaDigitavel1;
		this.valorIss = valorIss;	
	}
	
	//
	
	public String getJson(MpBoletoDTO obj) {
		//
		String json = "";
	
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			json = mapper.writeValueAsString(obj);
			//
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//
		return json;
	}
	
	//

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }
	
	public String getNumeroIntimacaoCode() { return numeroIntimacaoCode; }
	public void setNumeroIntimacaoCode(String numeroIntimacaoCode) { this.numeroIntimacaoCode = numeroIntimacaoCode; }

	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
	
	public String getNomeSacado() {  return nomeSacado; }
	public void setNomeSacado(String nomeSacado) { this.nomeSacado = nomeSacado; }
	
	public String getNumeroDocumento() { return numeroDocumento; }
	public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
	
	public String getNossoNumero() { return nossoNumero; }
	public void setNossoNumero(String nossoNumero) { this.nossoNumero = nossoNumero; }
	
	public String getNossoNumeroDig() { return nossoNumeroDig; }
	public void setNossoNumeroDig(String nossoNumeroDig) { this.nossoNumeroDig = nossoNumeroDig; }
	
	public BigDecimal getValorDocumento() { return valorDocumento; }
	public void setValorDocumento(BigDecimal valorDocumento) { this.valorDocumento = valorDocumento; }
	
	public BigDecimal getValorAcrescimo() { return valorAcrescimo; }
	public void setValorAcrescimo(BigDecimal valorAcrescimo) { this.valorAcrescimo = valorAcrescimo; }
	
	public BigDecimal getValorCobrado() { return valorCobrado; }
	public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
	
	public BigDecimal getValorTarifa() { return valorTarifa; }
	public void setValorTarifa(BigDecimal valorTarifa) { this.valorTarifa = valorTarifa; }
	
	public BigDecimal getValorCPMF() { return valorCPMF; }
	public void setValorCPMF(BigDecimal valorCPMF) { this.valorCPMF = valorCPMF; }
	
	public BigDecimal getValorLeis() { return valorLeis; }
	public void setValorLeis(BigDecimal valorLeis) { this.valorLeis = valorLeis; }

	public Date getDataDocumento() { return dataDocumento; }
	public void setDataDocumento(Date dataDocumento) { this.dataDocumento = dataDocumento; }

	public Date getDataVencimento() { return dataVencimento; }
	public void setDataVencimento(Date dataVencimento) { this.dataVencimento = dataVencimento; }

	public String getBoletoInstrucao8() { return boletoInstrucao8; }
	public void setBoletoInstrucao8(String boletoInstrucao8) { this.boletoInstrucao8 = boletoInstrucao8; }
	
	public String getCodigoOficio() { return codigoOficio; }
	public void setCodigoOficio(String codigoOficio) { this.codigoOficio = codigoOficio; }
	
	public MpEnderecoLocal getMpEnderecoLocal() { return mpEnderecoLocal; }
	public void setMpEnderecoLocal(MpEnderecoLocal mpEnderecoLocal) { this.mpEnderecoLocal = mpEnderecoLocal; }
	
	public String getCarteira() { return carteira; }
	public void setCarteira(String carteira) { this.carteira = carteira; }

	public String getCodigoBarras() { return codigoBarras; }
	public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

	public String getAgenciaCodigoCedente() { return agenciaCodigoCedente; }
	public void setAgenciaCodigoCedente(String agenciaCodigoCedente) { 
																this.agenciaCodigoCedente = agenciaCodigoCedente; }

	public String getLinhaDigitavel() { return linhaDigitavel; }
	public void setLinhaDigitavel(String linhaDigitavel) { this.linhaDigitavel = linhaDigitavel; }

	// ---
	
	public Date getDataVencimento1() { return dataVencimento1; }
	public void setDataVencimento1(Date dataVencimento1) { this.dataVencimento1 = dataVencimento1; }

	public String getCodigoBarras1() { return codigoBarras1; }
	public void setCodigoBarras1(String codigoBarras1) { this.codigoBarras1 = codigoBarras1; }

	public String getAgenciaCodigoCedente1() { return agenciaCodigoCedente1; }
	public void setAgenciaCodigoCedente1(String agenciaCodigoCedente1) { 
															this.agenciaCodigoCedente1 = agenciaCodigoCedente1; }

	public String getLinhaDigitavel1() { return linhaDigitavel1; }
	public void setLinhaDigitavel1(String linhaDigitavel1) { this.linhaDigitavel1 = linhaDigitavel1; }
	
	public String getEspecieDocumento() { return especieDocumento; }
	public void setEspecieDocumento(String especieDocumento) { this.especieDocumento = especieDocumento; }
		
	public BigDecimal getValorIss() { return valorIss; }
	public void setValorIss(BigDecimal valorIss) { this.valorIss = valorIss; }
	
}