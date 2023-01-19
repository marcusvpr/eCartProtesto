package com.mpxds.mpbasic.rest.model;

public class MpTituloDTO {
	//
	private String codigoOficio;
	private String numeroProtocolo;
	private String dataProtocolo;
	private String status;
	private String complemento;
	private String classeTitulo;

	// ---
	
	public MpTituloDTO(String codigoOficio, String numeroProtocolo, String dataProtocolo, String status,
								String complemento, String classeTitulo) {
		//
		super();
		
		this.codigoOficio = codigoOficio;
		this.numeroProtocolo = numeroProtocolo;
		this.dataProtocolo = dataProtocolo;
		this.status = status;
		this.complemento = complemento;
		this.classeTitulo = classeTitulo;
	}
	
	//
	
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
	public void setclasseTitulo(String classeTitulo) { this.classeTitulo = classeTitulo; }

}