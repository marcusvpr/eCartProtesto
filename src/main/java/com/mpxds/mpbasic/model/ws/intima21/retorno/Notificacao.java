package com.mpxds.mpbasic.model.ws.intima21.retorno;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Notificacao {
	private String nome_devedor_notificado;
	private String documento_devedor_notificado;
	private String tipo_devedor_notificado;
	private String celular_devedor_notificado;
	private String data_notificacao;

    @XmlElementWrapper(name="emails_devedor_notificado")
    @XmlElement(name="email")
	List<String> emailList = new ArrayList<String>();
		
	// Getter Methods

	public String getNome_devedor_notificado() {
		return nome_devedor_notificado;
	}

	public String getDocumento_devedor_notificado() {
		return documento_devedor_notificado;
	}

	public String getTipo_devedor_notificado() {
		return tipo_devedor_notificado;
	}

	public String getCelular_devedor_notificado() {
		return celular_devedor_notificado;
	}

	public List<String> getEmails() {
		return emailList;
	}

	public String getData_notificacao() {
		return data_notificacao;
	}

	// Setter Methods

	public void setNome_devedor_notificado(String nome_devedor_notificado) {
		this.nome_devedor_notificado = nome_devedor_notificado;
	}

	public void setDocumento_devedor_notificado(String documento_devedor_notificado) {
		this.documento_devedor_notificado = documento_devedor_notificado;
	}

	public void setTipo_devedor_notificado(String tipo_devedor_notificado) {
		this.tipo_devedor_notificado = tipo_devedor_notificado;
	}

	public void setCelular_devedor_notificado(String celular_devedor_notificado) {
		this.celular_devedor_notificado = celular_devedor_notificado;
	}

	public void setEmails(List<String> emailList) {
		this.emailList = emailList;
	}

	public void setData_notificacao(String data_notificacao) {
		this.data_notificacao = data_notificacao;
	}
}