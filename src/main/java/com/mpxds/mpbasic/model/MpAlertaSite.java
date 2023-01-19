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
@Table(name = "mp_alerta_site")
public class MpAlertaSite extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String numeroOficio;
	private Date dataAlertaIni;
	private Date dataAlertaFim;
	private String alertaMsg;
	private Integer tamanhoMsg;
	
	// ---	
	
	@Column(name = "numero_oficio", nullable = false, length = 30)
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_alerta_ini", nullable = false)
	public Date getDataAlertaIni() { return dataAlertaIni; }
	public void setDataAlertaIni(Date dataAlertaIni) { this.dataAlertaIni = dataAlertaIni; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_alerta_fim", nullable = false)
	public Date getDataAlertaFim() { return dataAlertaFim; }
	public void setDataAlertaFim(Date dataAlertaFim) { this.dataAlertaFim = dataAlertaFim; }
	
	@Column(name = "alerta_msg", nullable = false, length = 5000)
	public String getAlertaMsg() { return alertaMsg; }
	public void setAlertaMsg(String alertaMsg) { this.alertaMsg = alertaMsg; }
	
	@Column(name = "tamanho_msg", nullable = true)
	public Integer getTamanhoMsg() { return tamanhoMsg; }
	public void setTamanhoMsg(Integer tamanhoMsg) { this.tamanhoMsg = tamanhoMsg; }
	
	//	

	@Transient
	public String getDataAlertaIniSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(this.dataAlertaIni); 
	}

	@Transient
	public String getDataAlertaFimSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(this.dataAlertaFim); 
	}
	
}