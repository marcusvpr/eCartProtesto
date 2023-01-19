package com.mpxds.mpbasic.model.ws.intima21.retorno;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Intimacao {
	//
	private String protocolo;
	private String data_protocolo;
	private String especie;
	private String numero;
	private String data_validade;
	private String fins_falimentares;
	private String data_geracao;
	private String nome_devedor;
	private String documento_devedor;
	
    @XmlElementWrapper(name="notificacoes")
	@XmlElement(name = "notificacao")
	private List<Notificacao> notificacaoList = new ArrayList<Notificacao>();

    @XmlElementWrapper(name="acessos")
	@XmlElement(name = "acesso")
	private List<Acesso> acessoList = new ArrayList<Acesso>();

	// Getter Methods

	public String getProtocolo() {
		return protocolo;
	}

	public String getData_protocolo() {
		return data_protocolo;
	}

	public String getEspecie() {
		return especie;
	}

	public String getNumero() {
		return numero;
	}

	public String getData_validade() {
		return data_validade;
	}

	public String getFins_falimentares() {
		return fins_falimentares;
	}

	public String getData_geracao() {
		return data_geracao;
	}

	public String getNome_devedor() {
		return nome_devedor;
	}

	public String getDocumento_devedor() {
		return documento_devedor;
	}

	public List<Notificacao> getNotificacaos() {
		return notificacaoList;
	}

	public List<Acesso> getAcessos() {
		return acessoList;
	}

	// Setter Methods

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public void setData_protocolo(String data_protocolo) {
		this.data_protocolo = data_protocolo;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setData_validade(String data_validade) {
		this.data_validade = data_validade;
	}

	public void setFins_falimentares(String fins_falimentares) {
		this.fins_falimentares = fins_falimentares;
	}

	public void setData_geracao(String data_geracao) {
		this.data_geracao = data_geracao;
	}

	public void setNome_devedor(String nome_devedor) {
		this.nome_devedor = nome_devedor;
	}

	public void setDocumento_devedor(String documento_devedor) {
		this.documento_devedor = documento_devedor;
	}

	public void setNotificacaos(List<Notificacao> notificacaoList) {
		this.notificacaoList = notificacaoList;
	}

	public void setAcessos(List<Acesso> acessoList) {
		this.acessoList = acessoList;
	}
}