package com.mpxds.mpbasic.model;

import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "mp_boleto", indexes = {
		@Index(name = "idxBolOficio", columnList = "codigo_oficio"), 
		@Index(name = "idxBolProtocolo", columnList = "numero_intimacao"), 
		@Index(name = "idxBolDtDocumento", columnList = "data_documento"), 
		@Index(name = "idxBolCpfCnpj", columnList = "cpf_cnpj") 
		} )
public class MpBoleto extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String numeroIntimacao;
	private String numeroIntimacaoCode = "";
	private String numeroIntimacaoCodeX = "";
	private String cpfCnpj;

	private String nomeSacado;
	private String numeroDocumento;
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

	private String codigoOficio;
	private Integer numeroGeracaoPdf;

	private MpEnderecoLocal mpEnderecoLocal;

	// Prisco ...
	private String carteira;
	private String codigoBarras;
	private String agenciaCodigoCedente; 
	private String linhaDigitavel;
	
	// Prisco (Após às 16h00) ...
	private Date dataVencimento1;
	private String codigoBarras1;
	private String agenciaCodigoCedente1; 
	private String linhaDigitavel1;

	private Boolean indImpressao;
	private Date dataImpressao;
	private Date dataHoraImpressao;
	private Integer contadorImpressao;

	private Date dataHoraRegistro;

	private String especieDocumento;
	private Integer numeroGuiaGerado = 0;
	private String indApos16h = "";
	private Boolean indLiquidado = false; // MVPR_20180904
	private String nomePdfGerado;
	private Boolean indAberto = false; // MVPR_20200121
	
	private BigDecimal valorIss;

	private Date dataDistribuicao;

	private Boolean indBradescoErro69 = false; // MVPR_20200528

	// ---
	
	@NotBlank(message = "Por favor, informe o NUMERO INTIMAÇÃO")
	@Column(name = "numero_intimacao", nullable = false, length = 30)
	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }
	
	@Column(name = "numero_intimacao_code", nullable = true, length = 5)
	public String getNumeroIntimacaoCode() { return numeroIntimacaoCode; }
	public void setNumeroIntimacaoCode(String numeroIntimacaoCode) { this.numeroIntimacaoCode = numeroIntimacaoCode; }

	@Column(name = "numero_intimacao_codex", nullable = true, length = 5)
	public String getNumeroIntimacaoCodeX() { return numeroIntimacaoCodeX; }
	public void setNumeroIntimacaoCodeX(String numeroIntimacaoCodeX) { this.numeroIntimacaoCodeX = numeroIntimacaoCodeX; }
	
	@NotBlank(message = "Por favor, informe o CPF")
	@Column(name = "cpf_cnpj", nullable = false, length = 15)
	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
	
	@NotBlank(message = "Por favor, informe o NOME SACADO")
	@Column(name = "nome_sacado", nullable = false, length = 200)
	public String getNomeSacado() {  return nomeSacado; }
	public void setNomeSacado(String nomeSacado) { this.nomeSacado = nomeSacado; }
	
	@NotBlank(message = "Por favor, informe o NUMERO DOCUMENTO")
	@Column(name = "numero_documento", nullable = false, length = 30)
	public String getNumeroDocumento() { return numeroDocumento; }
	public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
	
	@NotBlank(message = "Por favor, informe o NOSSO NUMERO")
	@Column(name = "nosso_numero", nullable = false, length = 30)
	public String getNossoNumero() { return nossoNumero; }
	public void setNossoNumero(String nossoNumero) { this.nossoNumero = nossoNumero; }
	
	@NotBlank(message = "Por favor, informe o NOSSO NUMERO DIGITO")
	@Column(name = "nosso_numero_dig", nullable = false, length = 1)
	public String getNossoNumeroDig() { return nossoNumeroDig; }
	public void setNossoNumeroDig(String nossoNumeroDig) { this.nossoNumeroDig = nossoNumeroDig; }
	
	@NotNull(message = "Por favor, informe o VALOR DOCUMENTO")
	@Column(name = "valor_documento", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorDocumento() { return valorDocumento; }
	public void setValorDocumento(BigDecimal valorDocumento) { this.valorDocumento = valorDocumento; }
	
	@NotNull(message = "Por favor, informe o VALOR ACRESCIMO")
	@Column(name = "valor_acrescimo", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorAcrescimo() { return valorAcrescimo; }
	public void setValorAcrescimo(BigDecimal valorAcrescimo) { this.valorAcrescimo = valorAcrescimo; }
	
	@NotNull(message = "Por favor, informe o VALOR COBRADO")
	@Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorCobrado() { return valorCobrado; }
	public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
	
	@NotNull(message = "Por favor, informe o VALOR TARIFA")
	@Column(name = "valor_tarifa", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorTarifa() { return valorTarifa; }
	public void setValorTarifa(BigDecimal valorTarifa) { this.valorTarifa = valorTarifa; }
	
	@NotNull(message = "Por favor, informe o VALOR CPMF")
	@Column(name = "valor_cpmf", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorCPMF() { return valorCPMF; }
	public void setValorCPMF(BigDecimal valorCPMF) { this.valorCPMF = valorCPMF; }
	
	@NotNull(message = "Por favor, informe o VALOR LEIS")
	@Column(name = "valor_leis", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorLeis() { return valorLeis; }
	public void setValorLeis(BigDecimal valorLeis) { this.valorLeis = valorLeis; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_documento", nullable = false, length = 10)
	public Date getDataDocumento() { return dataDocumento; }
	public void setDataDocumento(Date dataDocumento) { this.dataDocumento = dataDocumento; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_vencimento", nullable = false, length = 10)
	public Date getDataVencimento() { return dataVencimento; }
	public void setDataVencimento(Date dataVencimento) { this.dataVencimento = dataVencimento; }

	@NotBlank(message = "Por favor, informe o BOLETO INSTRUCAO(8)")
	@Column(name = "boleto_instrucao8", nullable = false, length = 100)
	public String getBoletoInstrucao8() { return boletoInstrucao8; }
	public void setBoletoInstrucao8(String boletoInstrucao8) { this.boletoInstrucao8 = boletoInstrucao8; }
	
	@NotBlank(message = "Por favor, informe o OFICIO")
	@Column(name = "codigo_oficio", nullable = false, length = 20)
	public String getCodigoOficio() { return codigoOficio; }
	public void setCodigoOficio(String codigoOficio) { this.codigoOficio = codigoOficio; }
	
	@Column(name = "numero_geracao_pdf", nullable = true)
	public Integer getNumeroGeracaoPdf() { return numeroGeracaoPdf; }
	public void setNumeroGeracaoPdf(Integer numeroGeracaoPdf) { this.numeroGeracaoPdf = numeroGeracaoPdf; }
	
	@Embedded
	public MpEnderecoLocal getMpEnderecoLocal() { return mpEnderecoLocal; }
	public void setMpEnderecoLocal(MpEnderecoLocal mpEnderecoLocal) { this.mpEnderecoLocal = mpEnderecoLocal; }
	
	@NotBlank(message = "Por favor, informe a Carteira")
	@Column(nullable = false, length = 50)
	public String getCarteira() { return carteira; }
	public void setCarteira(String carteira) { this.carteira = carteira; }

	@NotBlank(message = "Por favor, informe o Codigo Barras")
	@Column(name = "codigo_barras", nullable = false, length = 100)
	public String getCodigoBarras() { return codigoBarras; }
	public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

	@NotBlank(message = "Por favor, informe a Agencia Codigo Cedente")
	@Column(name = "agencia_codigo_cedente", nullable = false, length = 100)
	public String getAgenciaCodigoCedente() { return agenciaCodigoCedente; }
	public void setAgenciaCodigoCedente(String agenciaCodigoCedente) { 
																this.agenciaCodigoCedente = agenciaCodigoCedente; }

	@NotBlank(message = "Por favor, informe a Linha Digitavel")
	@Column(name = "linha_digitavel", nullable = false, length = 100)
	public String getLinhaDigitavel() { return linhaDigitavel; }
	public void setLinhaDigitavel(String linhaDigitavel) { this.linhaDigitavel = linhaDigitavel; }

	// ---
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_vencimento_1", nullable = false, length = 10)
	public Date getDataVencimento1() { return dataVencimento1; }
	public void setDataVencimento1(Date dataVencimento1) { this.dataVencimento1 = dataVencimento1; }
	

	@NotBlank(message = "Por favor, informe o Codigo Barras 1")
	@Column(name = "codigo_barras_1", nullable = false, length = 100)
	public String getCodigoBarras1() { return codigoBarras1; }
	public void setCodigoBarras1(String codigoBarras1) { this.codigoBarras1 = codigoBarras1; }

	@NotBlank(message = "Por favor, informe a Agencia Codigo Cedente 1")
	@Column(name = "agencia_codigo_cedente_1", nullable = false, length = 100)
	public String getAgenciaCodigoCedente1() { return agenciaCodigoCedente1; }
	public void setAgenciaCodigoCedente1(String agenciaCodigoCedente1) { 
															this.agenciaCodigoCedente1 = agenciaCodigoCedente1; }

	@NotBlank(message = "Por favor, informe a Linha Digitavel 1")
	@Column(name = "linha_digitavel_1", nullable = false, length = 100)
	public String getLinhaDigitavel1() { return linhaDigitavel1; }
	public void setLinhaDigitavel1(String linhaDigitavel1) { this.linhaDigitavel1 = linhaDigitavel1; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_impressao", nullable = true, length = 10)
	public Date getDataImpressao() { return dataImpressao; }
	public void setDataImpressao(Date dataImpressao) { this.dataImpressao = dataImpressao; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_impressao", nullable = true)
	public Date getDataHoraImpressao() { return dataHoraImpressao; }
	public void setDataHoraImpressao(Date dataHoraImpressao) { this.dataHoraImpressao = dataHoraImpressao; }

    @Column(name = "ind_impressao", nullable = true)
	public Boolean getIndImpressao() { return indImpressao; }
	public void setIndImpressao(Boolean indImpressao) { this.indImpressao = indImpressao; }

    @Column(name = "contador_impressao", nullable = true)
	public Integer getContadorImpressao() { return contadorImpressao; }
	public void setContadorImpressao(Integer contadorImpressao) { this.contadorImpressao = contadorImpressao; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_registro", nullable = true)
	public Date getDataHoraRegistro() { return dataHoraRegistro; }
	public void setDataHoraRegistro(Date dataHoraRegistro) { this.dataHoraRegistro = dataHoraRegistro; }
	
	@NotBlank(message = "Por favor, informe a Especie Documento")
	@Column(name = "especie_documento", nullable = false, length = 5)
	public String getEspecieDocumento() { return especieDocumento; }
	public void setEspecieDocumento(String especieDocumento) { this.especieDocumento = especieDocumento; }
		
	@Column(name = "numero_guia_gerado", nullable = true)
	public Integer getNumeroGuiaGerado() { return numeroGuiaGerado; }
	public void setNumeroGuiaGerado(Integer numeroGuiaGerado) { this.numeroGuiaGerado = numeroGuiaGerado; }
	
	@Column(name = "ind_apos_16h", nullable = true, length = 1)
	public String getIndApos16h() { return indApos16h; }
	public void setIndApos16h(String indApos16h) { this.indApos16h = indApos16h; }
	
	@Column(name = "ind_liquidado", nullable = true)
	public Boolean getIndLiquidado() { return indLiquidado; }
	public void setIndLiquidado(Boolean indLiquidado) { this.indLiquidado = indLiquidado; }
	
	@Column(name = "nome_pdf_gerado", nullable = true)
	public String getNomePdfGerado() { return nomePdfGerado; }
	public void setNomePdfGerado(String nomePdfGerado) { this.nomePdfGerado = nomePdfGerado; }
	
	@NotNull(message = "Por favor, informe o VALOR ISS")
	@Column(name = "valor_iss", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorIss() { return valorIss; }
	public void setValorIss(BigDecimal valorIss) { this.valorIss = valorIss; }
	
	@Column(name = "ind_aberto", nullable = true)
	public Boolean getIndAberto() { return indAberto; }
	public void setIndAberto(Boolean indAberto) { this.indAberto = indAberto; }
		
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_distribuicao", nullable = false, length = 10)
	public Date getDataDistribuicao() { return dataDistribuicao; }
	public void setDataDistribuicao(Date dataDistribuicao) { this.dataDistribuicao = dataDistribuicao; }
	
	@Column(name = "ind_bradesco_erro69", nullable = true)
	public Boolean getIndBradescoErro69() { return indBradescoErro69; }
	public void setIndBradescoErro69(Boolean indBradescoErro69) { this.indBradescoErro69 = indBradescoErro69; }
	
	// indBradescoErro69
	
	@Transient
	public BigDecimal getValorTaxas() { 
		//
		return this.valorCobrado.subtract(this.valorDocumento); 
	}

}