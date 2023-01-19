package com.mpxds.mpbasic.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="mpTituloWs")
public class MpTituloWs {
	//
	private int id;
	private String codigoOficio;
	private String numeroProtocolo;
	private String dataProtocolo;
	private String status;
	private String complemento;
	private String classeTitulo;

	private Boolean indTituloProtestadoOrdinario;
	private Boolean indTituloProtestadoCustodiado;

	// ---

	public MpTituloWs() {
		//
		super();
	}

	public MpTituloWs(int id, String codigoOficio, String numeroProtocolo, String dataProtocolo,
						String status, String complemento, String classeTitulo,
						Boolean indTituloProtestadoOrdinario, Boolean indTituloProtestadoCustodiado) {
		//
		super();
		
		this.id = id;
		this.codigoOficio = codigoOficio;
		this.numeroProtocolo = numeroProtocolo;
		this.dataProtocolo = dataProtocolo;
		this.status = status;
		this.complemento = complemento;
		this.classeTitulo = classeTitulo;
		this.indTituloProtestadoOrdinario = indTituloProtestadoOrdinario;
		this.indTituloProtestadoCustodiado = indTituloProtestadoCustodiado;
	}
		
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getCodigoOficio() { return codigoOficio; }
	public void setCodigoOficio(String codigoOficio) { this.codigoOficio = codigoOficio; }
	
	public String getNumeroProtocolo() { return numeroProtocolo; }
	public void setNumeroProtocolo(String numeroProtocolo) { this.numeroProtocolo = numeroProtocolo; }
	
	public String getDataProtocolo() { return dataProtocolo; }
	public void setDataProtocolo(String dataProtocolo) { this.dataProtocolo = dataProtocolo; }
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public String getComplemento() { return complemento; }
	public void setComplemento(String complemento) { this.complemento = complemento; }
 
	public String getClasseTitulo() { return classeTitulo; }
	public void setClasseTitulo(String classeTitulo) { this.classeTitulo = classeTitulo; }
	 
	public Boolean getIndTituloProtestadoOrdinario() { return indTituloProtestadoOrdinario; }
	public void setIndTituloProtestadoOrdinario(Boolean indTituloProtestadoOrdinario) { 
														this.indTituloProtestadoOrdinario = indTituloProtestadoOrdinario; }
	 
	public Boolean getIndTituloProtestadoCustodiado() { return indTituloProtestadoCustodiado; }
	public void setIndTituloProtestadoCustodiado(Boolean indTituloProtestadoCustodiado) { 
														this.indTituloProtestadoCustodiado = indTituloProtestadoCustodiado; }

	// ---
	
	@Override
	public String toString() {
		return id + "::" + codigoOficio + "::" + numeroProtocolo + "::" + dataProtocolo + "::" + status
				+ "::" + complemento + "::" + classeTitulo + "::" + indTituloProtestadoOrdinario
				 + "::" + indTituloProtestadoCustodiado;
	}
	
}