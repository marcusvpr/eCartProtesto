package com.mpxds.mpbasic.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "mp_noticia_site", indexes = {
		@Index(name = "idxNotOficio", columnList = "numero_oficio"), 
		@Index(name = "idxNotProtocolo", columnList = "data_noticia") 
		} )
public class MpNoticiaSite extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String numeroOficio;
	private Date dataNoticia;
	private String assunto;
	private String descricao;
	private String url;
	
	// ---	
	
	@Column(name = "numero_oficio", nullable = false, length = 10)
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_noticia", nullable = false)
	public Date getDataNoticia() { return dataNoticia; }
	public void setDataNoticia(Date dataNoticia) { this.dataNoticia = dataNoticia; }
	
	@Column(name = "assunto", nullable = false, length = 300)
	public String getAssunto() { return assunto; }
	public void setAssunto(String assunto) { this.assunto = assunto; }
	
	@Column(name = "descricao", nullable = false, length = 5000)
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }
	
	@Column(name = "url", nullable = false, length = 300)
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
		
	//	

	@Transient
	public String getDataNoticiaSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		return sdf.format(this.dataNoticia); 
	}

	@Transient
	public Boolean isEmptyUrl() { 
		//
		return this.url.isEmpty(); 
	}
	
}