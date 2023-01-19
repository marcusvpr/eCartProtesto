package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;
import java.util.Date;

public class MpTituloFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String numeroProtocolo;
	private Date dataProtocolo;
	private String status;
	
	// ---

	public String getNumeroProtocolo() { return numeroProtocolo; }
	public void setNumeroProtocolo(String numeroProtocolo) { this.numeroProtocolo = numeroProtocolo; }
	
	public Date getDataProtocolo() { return dataProtocolo; }
	public void setDataProtocolo(Date dataProtocolo) { this.dataProtocolo = dataProtocolo; }
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

}