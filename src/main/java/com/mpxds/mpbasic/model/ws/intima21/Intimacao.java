package com.mpxds.mpbasic.model.ws.intima21;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "intimacao")
@XmlType(propOrder = { "protocolo", "data_protocolo", "especie", "numero", "data_validade",
		               "fins_falimentares", "nome_devedor", "documento_devedor", "base64"})
public class Intimacao {
	//
	private String protocolo;
	private String data_protocolo;
	private String especie;
	private String numero;
	private String data_validade;
	private Integer fins_falimentares;
	private String nome_devedor;
	private String documento_devedor;
	private String base64;

	//

	public String getProtocolo() { return protocolo; }
	public void setProtocolo(String protocolo) { this.protocolo = protocolo; }

	public String getData_protocolo() { return data_protocolo; }
	public void setData_protocolo(String data_protocolo) { this.data_protocolo = data_protocolo; }

	public String getEspecie() { return especie; }
	public void setEspecie(String especie) { this.especie = especie; }

	public String getNumero() { return numero; }
	public void setNumero(String numero) { this.numero = numero; }

	public String getData_validade() { return data_validade; }
	public void setData_validade(String data_validade) { this.data_validade = data_validade; }

	public Integer getFins_falimentares() { return fins_falimentares; }
	public void setFins_falimentares(Integer fins_falimentares) { this.fins_falimentares = fins_falimentares; }

	public String getNome_devedor() { return nome_devedor; }
	public void setNome_devedor(String nome_devedor) { this.nome_devedor = nome_devedor; }

	public String getDocumento_devedor() { return documento_devedor; }
	public void setDocumento_devedor(String documento_devedor) { this.documento_devedor = documento_devedor; }

	public String getBase64() { return base64; }
	public void setBase64(String base64) { this.base64 = base64; }

}