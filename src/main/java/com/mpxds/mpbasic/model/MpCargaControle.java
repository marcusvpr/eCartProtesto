package com.mpxds.mpbasic.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "mp_carga_controle")
public class MpCargaControle extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String numeroOficio;
	private Date dataHoraIni;
	private Date dataHoraFim;
	private String mensagem;
	private Boolean indRecorrente;
	
	// ---	
	
	@Column(name = "numero_oficio", nullable = false, length = 30)
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_ini", nullable = false)
	public Date getDataHoraIni() { return dataHoraIni; }
	public void setDataHoraIni(Date dataHoraIni) { this.dataHoraIni = dataHoraIni; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_fim", nullable = false)
	public Date getDataHoraFim() { return dataHoraFim; }
	public void setDataHoraFim(Date dataHoraFim) { this.dataHoraFim = dataHoraFim; }
	
	@Column(name = "mensagem", nullable = false, length = 500)
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }
	
	@Column(name = "ind_recorrente", nullable = true)
	public Boolean getIndRecorrente() { return indRecorrente; }
	public void setIndRecorrente(Boolean indRecorrente) { this.indRecorrente = indRecorrente; }
	
	//	

	@Transient
	public String getDataHoraIniSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(this.dataHoraIni); 
	}

	@Transient
	public String getDataHoraFimSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(this.dataHoraFim); 
	}
	
}