package com.mpxds.mpbasic.model.ws.intima21.retorno;

public class Acesso {
	private String origem;
	private String ip;
	private String navegador;
	private String sistema_operacional;
	private String data;

	// Getter Methods

	public String getOrigem() {
		return origem;
	}

	public String getIp() {
		return ip;
	}

	public String getNavegador() {
		return navegador;
	}

	public String getSistema_operacional() {
		return sistema_operacional;
	}

	public String getData() {
		return data;
	}

	// Setter Methods

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public void setSistema_operacional(String sistema_operacional) {
		this.sistema_operacional = sistema_operacional;
	}

	public void setData(String data) {
		this.data = data;
	}
}
