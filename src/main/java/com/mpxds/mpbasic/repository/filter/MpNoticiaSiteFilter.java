package com.mpxds.mpbasic.repository.filter;

import java.util.Date;

public class MpNoticiaSiteFilter {
	//
	private String numeroOficio;
	private Date dataNoticia;
	private String assunto;
	private String descricao;
	private String url;
	private Boolean indAtivo;
	
	// ---	
	
	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	public Date getDataNoticia() { return dataNoticia; }
	public void setDataNoticia(Date dataNoticia) { this.dataNoticia = dataNoticia; }
	
	public String getAssunto() { return assunto; }
	public void setAssunto(String assunto) { this.assunto = assunto; }
	
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.assunto = descricao; }
	
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	
	public Boolean getIndAtivo() { return indAtivo; }
	public void setIndAtivo(Boolean indAtivo) { this.indAtivo = indAtivo; }
	
}