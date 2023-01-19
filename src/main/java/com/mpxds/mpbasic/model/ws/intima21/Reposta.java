package com.mpxds.mpbasic.model.ws.intima21;

public class Reposta {
	//
	private String protocolo;
	private String data_protocolo;
	
	private Mensagens mensagens;

	//

	public String getProtocolo() { return protocolo; }
	public void setProtocolo(String protocolo) { this.protocolo = protocolo; }

	public String getData_protocolo() { return data_protocolo; }
	public void setData_protocolo(String data_protocolo) { this.data_protocolo = data_protocolo; }
	
	public Mensagens getMensagens() { return mensagens; }
	public void setMensagens(Mensagens mensagens) { this.mensagens = mensagens; }

}