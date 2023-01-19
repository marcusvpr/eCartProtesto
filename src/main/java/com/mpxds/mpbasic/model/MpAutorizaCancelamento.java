package com.mpxds.mpbasic.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpbasic.util.MpAppUtil;

@Entity
@Table(name = "mp_autoriza_cancelamento", indexes = {
		@Index(name = "idxAutOficio", columnList = "codigo_oficio"), 
		@Index(name = "idxAutProtocolo", columnList = "protocolo"), 
		@Index(name = "idxAutDtProtocolo", columnList = "data_protocolo") 
		} )
public class MpAutorizaCancelamento extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String codigoOficio;

	private String apresentante;
	private String codigo;
	
	private Date data;
	private String protocolo;
	private Date dataProtocolo;
	private Date dataRemessa;
	private String agenciaCodigoCedente;
	private String cedente;

	private String sacador;
	private String documentoSacador;
	private String devedor;
	private String documentoDevedor;
	private String endereco;
	private String cep;
	private String bairro;
	private String cidade;
	private String uf;
	private String nossoNumero;
	private String numeroTitulo;
	
	private BigDecimal valor;
	private BigDecimal saldo;
	
	private String especie;
	private String pracaProtesto;
	private String tipoAutorizacao;
	private String situacao;
	private String impresso;
	private String vencimento;
	private String ocorrencia;
	private Date dataOcorrencia;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// ---
		
	@NotBlank(message = "Por favor, informe o OFICIO")
	@Column(name = "codigo_oficio", nullable = false, length = 20)
	public String getCodigoOficio() { return codigoOficio; }
	public void setCodigoOficio(String codigoOficio) { this.codigoOficio = codigoOficio; }
	
	@NotBlank(message = "Por favor, informe o APRESENTANTE")
	@Column(name = "apresentante", nullable = false, length = 150)
	public String getApresentante() { return apresentante; }
	public void setApresentante(String apresentante) { this.apresentante = apresentante; }
	
	@NotBlank(message = "Por favor, informe o CODIGO")
	@Column(name = "codigo", nullable = false, length = 15)
	public String getCodigo() { return codigo; }
	public void setCodigo(String codigo) { this.codigo = codigo; }
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false, length = 10)
	public Date getData() { return data; }
	public void setData(Date data) { this.data = data; }
	
	@NotBlank(message = "Por favor, informe o PROTOCOLO")
	@Column(name = "protocolo", nullable = false, length = 15)
	public String getProtocolo() { return protocolo; }
	public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_protocolo", nullable = false, length = 10)
	public Date getDataProtocolo() { return dataProtocolo; }
	public void setDataProtocolo(Date dataProtocolo) { this.dataProtocolo = dataProtocolo; }
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_remessa", nullable = false, length = 10)
	public Date getDataRemessa() { return dataRemessa; }
	public void setDataRemessa(Date dataRemessa) { this.dataRemessa = dataRemessa; }
	
	@NotBlank(message = "Por favor, informe o AGENCIA CODOGO CEDENTE")
	@Column(name = "agencia_codigo_cedente", nullable = false, length = 30)
	public String getAgenciaCodigoCedente() { return agenciaCodigoCedente; }
	public void setAgenciaCodigoCedente(String agenciaCodigoCedente) { 
															this.agenciaCodigoCedente = agenciaCodigoCedente; }

	@NotBlank(message = "Por favor, informe o CEDENTE")
	@Column(name = "cedente", nullable = false, length = 150)
	public String getCedente() { return cedente; }
	public void setCedente(String cedente) { this.cedente = cedente; }
	
	@NotBlank(message = "Por favor, informe o SACADOR")
	@Column(name = "sacador", nullable = false, length = 150)
	public String getSacador() { return sacador; }
	public void setSacador(String sacador) { this.sacador = sacador; }

	@NotBlank(message = "Por favor, informe o DOCUMENTO SACADOR")
	@Column(name = "documento_sacador", nullable = false, length = 50)
	public String getDocumentoSacador() { return documentoSacador; }
	public void setDocumentoSacador(String documentoSacador) { this.documentoSacador = documentoSacador; }
	
	@NotBlank(message = "Por favor, informe o DEVEDOR")
	@Column(name = "devedor", nullable = false, length = 150)
	public String getDevedor() { return devedor; }
	public void setDevedor(String devedor) { this.devedor = devedor; }
	
	@NotBlank(message = "Por favor, informe o DOCUMENTO DEVEDOR")
	@Column(name = "documento_devedor", nullable = false, length = 50)
	public String getDocumentoDevedor() { return documentoDevedor; }
	public void setDocumentoDevedor(String documentoDevedor) { this.documentoDevedor = documentoDevedor; }

	@NotBlank(message = "Por favor, informe o ENDERECO")
	@Column(name = "endereco", nullable = false, length = 150)
	public String getEndereco() { return endereco; }
	public void setEndereco(String endereco) { this.endereco = endereco; }

	@NotBlank(message = "Por favor, informe o CEP")
	@Column(name = "cep", nullable = false, length = 10)
	public String getCep() { return cep; }
	public void setCep(String cep) { this.cep = cep; }

	@NotBlank(message = "Por favor, informe o BAIRRO")
	@Column(name = "bairro", nullable = false, length = 100)
	public String getBairro() { return bairro; }
	public void setBairro(String bairro) { this.bairro = bairro; }

	@NotBlank(message = "Por favor, informe o CIDADE")
	@Column(name = "cidade", nullable = false, length = 100)
	public String getCidade() { return cidade; }
	public void setCidade(String cidade) { this.cidade = cidade; }

	@NotBlank(message = "Por favor, informe o UF")
	@Column(name = "uf", nullable = false, length = 50)
	public String getUf() { return uf; }
	public void setUf(String uf) { this.uf = uf; }

	@NotBlank(message = "Por favor, informe o NOSSO NUMERO")
	@Column(name = "nosso_numero", nullable = false, length = 30)
	public String getNossoNumero() { return nossoNumero; }
	public void setNossoNumero(String nossoNumero) { this.nossoNumero = nossoNumero; }
	
	@NotBlank(message = "Por favor, informe o NUMERO TITULO")
	@Column(name = "numero_titulo", nullable = false, length = 30)
	public String getNumeroTitulo() { return numeroTitulo; }
	public void setNumeroTitulo(String numeroTitulo) { this.numeroTitulo = numeroTitulo; }

	@NotNull(message = "Por favor, informe o VALOR")
	@Column(name = "valor", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValor() { return valor; }
	public void setValor(BigDecimal valor) { this.valor = valor; }

	@NotNull(message = "Por favor, informe o SALDO")
	@Column(name = "saldo", nullable = false, precision = 10, scale = 2)
	public BigDecimal getSaldo() { return saldo; }
	public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

	@NotBlank(message = "Por favor, informe o ESPECIE")
	@Column(name = "especie", nullable = false, length = 10)
	public String getEspecie() { return especie; }
	public void setEspecie(String especie) { this.especie = especie; }

	@NotBlank(message = "Por favor, informe o PRACA PROTESTO")
	@Column(name = "praca_protesto", nullable = false, length = 50)
	public String getPracaProtesto() { return pracaProtesto; }
	public void setPracaProtesto(String pracaProtesto) { this.pracaProtesto = pracaProtesto; }
	
	@NotBlank(message = "Por favor, informe o TIPO AUTORIZACAO")
	@Column(name = "tipo_autorizacao", nullable = false, length = 50)
	public String getTipoAutorizacao() { return tipoAutorizacao; }
	public void setTipoAutorizacao(String tipoAutorizacao) { this.tipoAutorizacao = tipoAutorizacao; }

	@NotBlank(message = "Por favor, informe o SITUACAO")
	@Column(name = "situacao", nullable = false, length = 50)
	public String getSituacao() { return situacao; }
	public void setSituacao(String situacao) { this.situacao = situacao; }

	@NotBlank(message = "Por favor, informe o IMPRESSO")
	@Column(name = "impresso", nullable = false, length = 10)
	public String getImpresso() { return impresso; }
	public void setImpresso(String impresso) { this.impresso = impresso; }

	@NotBlank(message = "Por favor, informe o VENCIMENTO")
	@Column(name = "vencimento", nullable = false, length = 50)
	public String getVencimento() { return vencimento; }
	public void setVencimento(String vencimento) { this.vencimento = vencimento; }

	@NotBlank(message = "Por favor, informe o OCORRENCIA")
	@Column(name = "ocorrencia", nullable = false, length = 50)
	public String getOcorrencia() { return ocorrencia; }
	public void setOcorrencia(String ocorrencia) { this.ocorrencia = ocorrencia; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_ocorrencia", nullable = false, length = 10)
	public Date getDataOcorrencia() { return dataOcorrencia; }
	public void setDataOcorrencia(Date dataOcorrencia) { this.dataOcorrencia = dataOcorrencia; }
	
	//
	
	@Transient
	public String getDataSDF() { return sdf.format(this.data); } 
	
	@Transient
	public String getDataProtocoloSDF() { return sdf.format(this.dataProtocolo); } 

	@Transient
	public String getDataRemessaSDF() { return sdf.format(this.dataRemessa); } 

	@Transient
	public String getDataOcorrenciaSDF() { return sdf.format(this.dataOcorrencia); } 

	@Transient
	public String getSaldoMOEDA() {
		//
		if (null == this.saldo)	this.saldo = BigDecimal.ZERO;
		
		return MpAppUtil.formataMoeda(this.saldo);
	} 
	
}