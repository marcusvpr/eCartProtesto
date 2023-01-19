package com.mpxds.mpbasic.model.intima21;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
//import javax.validation.constraints.NotNull;

import com.mpxds.mpbasic.model.MpBaseEntity;

@Entity
@Table(name = "mp_boleto_intimacao_log")
public class MpBoletoIntimacaoLog extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private Date dataGeracao;
	private String usuarioNome;
	
	private String usuarioEmail;
	private Boolean indUserWeb;
	
	private String numeroOficio;

	private String numeroDocumento;
	private BigDecimal valorDocumento;

	private String boletoRegistro;
	private String boletoRegistroRetorno;

	private String ambienteBradesco;
	private String mensagem;

	private String protocolo;
	private String numeroGuia;
	private BigDecimal valorCPMF;
	private BigDecimal valorCobrado;
	private BigDecimal valorTarifa;
	private BigDecimal valorLeis;

	private Boolean indCancelamento;
	
	// ---	

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_geracao", nullable = false)
	public Date getDataGeracao() { return dataGeracao; }
	public void setDataGeracao(Date dataGeracao) { this.dataGeracao = dataGeracao; }
	
	@Column(name = "usuario_nome", nullable = false, length = 30)
	public String getUsuarioNome() { return usuarioNome; }
	public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
	
	@Column(name = "usuario_email", nullable = true, length = 250)
	public String getUsuarioEmail() { return usuarioEmail; }
	public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
	
	@Column(name = "ind_userWeb", nullable = true)
	public Boolean getIndUserWeb() { 
		if (null == this.indUserWeb) return true;
		//
		return indUserWeb;
	}
	public void setIndUserWeb(Boolean indUserWeb) { this.indUserWeb = indUserWeb; }
	
	@Column(name = "numero_oficio", nullable = false, length = 30)
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }
	
	@Column(name = "numero_documento", nullable = false, length = 30)
	public String getNumeroDocumento() { return numeroDocumento; }
	public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
	
	@Column(name = "valor_documento", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorDocumento() { return valorDocumento; }
	public void setValorDocumento(BigDecimal valorDocumento) { this.valorDocumento = valorDocumento; }
	
	@Column(name = "boleto_Registro", nullable = true, length = 5000)
	public String getBoletoRegistro() { return boletoRegistro; }
	public void setBoletoRegistro(String boletoRegistro) { this.boletoRegistro = boletoRegistro; }

	@Column(name = "boleto_Registro_Retorno", nullable = true, length = 5000)
	public String getBoletoRegistroRetorno() { return boletoRegistroRetorno; }
	public void setBoletoRegistroRetorno(String boletoRegistroRetorno) { 
														this.boletoRegistroRetorno = boletoRegistroRetorno; }

	@Column(name = "ambiente_bradesco", nullable = true, length = 20)
	public String getAmbienteBradesco() { return ambienteBradesco; }
	public void setAmbienteBradesco(String ambienteBradesco) { this.ambienteBradesco = ambienteBradesco; }

	@Column(nullable = true, length = 2000)
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) {this.mensagem = mensagem; }

	@Column(name = "protocolo", nullable = true, length = 20)
	public String getProtocolo() { return protocolo; }
	public void setProtocolo(String protocolo) { this.protocolo = protocolo; }

	@Column(name = "numero_guia", nullable = true, length = 10)
	public String getNumeroGuia() { return numeroGuia; }
	public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
		
	@Column(name = "valor_tarifa", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorTarifa() { return valorTarifa; }
	public void setValorTarifa(BigDecimal valorTarifa) { this.valorTarifa = valorTarifa; }
	
	@Column(name = "valor_cpmf", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorCPMF() { return valorCPMF; }
	public void setValorCPMF(BigDecimal valorCPMF) { this.valorCPMF = valorCPMF; }
	
	@Column(name = "valor_leis", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorLeis() { return valorLeis; }
	public void setValorLeis(BigDecimal valorLeis) { this.valorLeis = valorLeis; }
		
	@Column(name = "valor_cobrado", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorCobrado() { return valorCobrado; }
	public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
	
	@Column(name = "ind_cancelamento", nullable = true)
	public Boolean getIndCancelamento() { 
		if (null == this.indCancelamento) return true;
		//
		return indCancelamento;
	}
	public void setIndCancelamento(Boolean indCancelamento) { this.indCancelamento = indCancelamento; }

	//

	@Transient
	public String getDataGeracaoSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return sdf.format(this.dataGeracao); 
	}
	
}