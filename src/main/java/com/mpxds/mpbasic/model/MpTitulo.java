package com.mpxds.mpbasic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "mp_titulo", indexes = {
		@Index(name = "idxTitOficio", columnList = "codigo_oficio"), 
		@Index(name = "idxTitProtocolo", columnList = "numero_protocolo"), 
		@Index(name = "idxTitDtProtocolo", columnList = "data_protocolo") 
		} )
public class MpTitulo extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String codigoOficio;
	private String numeroProtocolo;
	private Date dataProtocolo;
	private String status;
	private String complemento;
	private String classeTitulo;

	// ---
	
	@NotBlank(message = "Por favor, informe o Oficio")
	@Column(name = "codigo_oficio", nullable = false, length = 20)
	public String getCodigoOficio() { return codigoOficio; }
	public void setCodigoOficio(String codigoOficio) { this.codigoOficio = codigoOficio; }
	
	@NotBlank(message = "Por favor, informe o NUMERO PROTOCOLO")
	@Column(name = "numero_protocolo", nullable = false, length = 30)
	public String getNumeroProtocolo() { return numeroProtocolo; }
	public void setNumeroProtocolo(String numeroProtocolo) { this.numeroProtocolo = numeroProtocolo; }
	
	@NotNull(message = "Por favor, informe a DATA PROTOCOLO")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_protocolo", nullable = false, length = 10)
	public Date getDataProtocolo() { return dataProtocolo; }
	public void setDataProtocolo(Date dataProtocolo) { this.dataProtocolo = dataProtocolo; }
	
	@NotBlank(message = "Por favor, informe STATUS")
	@Column(name = "status", nullable = false, length = 30)
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	@Column(name = "complemento", nullable = true, length = 100)
	public String getComplemento() { return complemento; }
	public void setComplemento(String complemento) { this.complemento = complemento; }
	
	@Column(name = "classe_titulo", nullable = true, length = 100)
	public String getClasseTitulo() { return classeTitulo; }
	public void setclasseTitulo(String classeTitulo) { this.classeTitulo = classeTitulo; }

}