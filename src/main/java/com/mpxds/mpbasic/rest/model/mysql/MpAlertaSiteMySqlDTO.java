package com.mpxds.mpbasic.rest.model.mysql;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement(name = "MpAlertaSite")
public class MpAlertaSiteMySqlDTO {
	//
	private long id;
	private String alertaMsg;
	private String alertaTxt;
	private String dataAlertaFim;
	private String dataAlertaIni;
	private String numeroOficio;
	private String tamanhoMsg;

	//
	 
	public MpAlertaSiteMySqlDTO() {
		//
		super();
	}
	 
	public MpAlertaSiteMySqlDTO(long id, String tamanhoMsg, String alertaMsg,
									String alertaTxt, String dataAlertaFim, 
									String dataAlertaIni, String numeroOficio) {
		//
		super();

		this.id = id;
		this.tamanhoMsg = tamanhoMsg;
		this.alertaMsg = alertaMsg;
		this.alertaTxt = alertaTxt;
		this.dataAlertaFim = dataAlertaFim;
		this.dataAlertaIni = dataAlertaIni;
		this.numeroOficio = numeroOficio;
	}
	 
	//
		
	public String getJson(MpAlertaSiteMySqlDTO obj) {
		//
		String json = "";
	
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			json = mapper.writeValueAsString(obj);
			//
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//
		return json;
	}

	//

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getAlertaMsg() { return alertaMsg; }
	public void setAlertaMsg(String alertaMsg) { this.alertaMsg = alertaMsg; }

	public String getAlertaTxt() { return alertaTxt; }
	public void setAlertaTxt(String alertaTxt) { this.alertaTxt = alertaTxt; }

	public String getDataAlertaFim() { return dataAlertaFim; }
	public void setDataAlertaFim(String dataAlertaFim) { this.dataAlertaFim = dataAlertaFim; }

	public String getDataAlertaIni() { return dataAlertaIni; }
	public void setDataAlertaIni(String dataAlertaIni) { this.dataAlertaIni = dataAlertaIni; }

	public String getNumeroOficio() { return numeroOficio; }
	public void setNumeroOficio(String numeroOficio) { this.numeroOficio = numeroOficio; }

	public String getTamanhoMsg() { return tamanhoMsg; }
	public void setTamanhoMsg(String tamanhoMsg) { this.tamanhoMsg = tamanhoMsg; }
	 	 
}