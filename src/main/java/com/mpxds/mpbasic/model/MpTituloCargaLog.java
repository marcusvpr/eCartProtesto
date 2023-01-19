package com.mpxds.mpbasic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mp_titulo_carga_log")
public class MpTituloCargaLog extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private Date dataGeracao;
	private String usuarioNome;
	private String numeroOficio;
	private Long numeroRegistros;
	
	// ---	

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_geracao", nullable = false)
	public Date getDataGeracao() { return dataGeracao; }
	public void setDataGeracao(Date dataGeracao) { this.dataGeracao = dataGeracao; }
	
	@Column(name = "usuario_nome", nullable = false, length = 30)
	public String getUsuarioNome() { return usuarioNome; }
	public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
	
	@Column(name = "numero_oficio", nullable = false, length = 30)
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }
	
	@Column(name = "numero_registros", nullable = false)
	public Long getNumeroRegistros() { return numeroRegistros; }
	public void setNumeroRegistros(Long numeroRegistros) { this.numeroRegistros = numeroRegistros; }

}