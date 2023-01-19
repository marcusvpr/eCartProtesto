package com.mpxds.mpbasic.rest.model.mysql;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement(name = "MpUsuario")
public class MpUsuarioMySqlDTO {
	//
	private long id;
	private String login;
	private String nome;
	private String email;
	private String senha;
	
	private String cpfCnpj;
	private String telefone;
	private String celular;
	private Date dataNascimento;	
	private String observacao;

	private Boolean indUserWeb;
	private Boolean indAtivacao;
	private String codigoAtivacao;

	private Boolean indBoleto;
	private Boolean indTitulo;
		
	private String numIpUltimoLogin;
	private Date dataUltimoLogin;
	private Date dataUltimoLoginAnt; 
	
	private String mpStatus;
	private String mpSexo;	

	//
	 
	public MpUsuarioMySqlDTO() {
	 	//
		super();
	}
	 
 	public MpUsuarioMySqlDTO(long id, String login, String nome, String email, String senha, String cpfCnpj, String telefone,
			String celular, Date dataNascimento, String observacao, Boolean indUserWeb, Boolean indAtivacao,
			String codigoAtivacao, Boolean indBoleto, Boolean indTitulo, String numIpUltimoLogin, Date dataUltimoLogin,
			Date dataUltimoLoginAnt, String mpStatus, String mpSexo) {
 		//
		super();
		
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.cpfCnpj = cpfCnpj;
		this.telefone = telefone;
		this.celular = celular;
		this.dataNascimento = dataNascimento;
		this.observacao = observacao;
		this.indUserWeb = indUserWeb;
		this.indAtivacao = indAtivacao;
		this.codigoAtivacao = codigoAtivacao;
		this.indBoleto = indBoleto;
		this.indTitulo = indTitulo;
		this.numIpUltimoLogin = numIpUltimoLogin;
		this.dataUltimoLogin = dataUltimoLogin;
		this.dataUltimoLoginAnt = dataUltimoLoginAnt;
		this.mpStatus = mpStatus;
		this.mpSexo = mpSexo;
	}

	//
	
	public String getJson(MpUsuarioMySqlDTO obj) {
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

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
		
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	
	public String getCelular() { return celular; }
	public void setCelular(String celular) { this.celular = celular; }
	
	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }

	public Boolean getIndUserWeb() { 
		if (null == this.indUserWeb) return true;
		//
		return indUserWeb;
	}
	public void setIndUserWeb(Boolean indUserWeb) { this.indUserWeb = indUserWeb; }

	public Boolean getIndAtivacao() { return indAtivacao; }
	public void setIndAtivacao(Boolean indAtivacao) { this.indAtivacao = indAtivacao; }
  
	public String getCodigoAtivacao() { return codigoAtivacao; }
	public void setCodigoAtivacao(String codigoAtivacao) { this.codigoAtivacao = codigoAtivacao; }

	public Boolean getIndBoleto() { return indBoleto; }
	public void setIndBoleto(Boolean indBoleto) { this.indBoleto = indBoleto; }
  
	public Boolean getIndTitulo() { return indTitulo; }
	public void setIndTitulo(Boolean indTitulo) { this.indTitulo = indTitulo; }

	// ---
	  
	public String getNumIpUltimoLogin() { return this.numIpUltimoLogin; }
	public void setNumIpUltimoLogin(String newNumIpUltimoLogin) { this.numIpUltimoLogin = newNumIpUltimoLogin; }
	  
	public Date getDataUltimoLogin() { return this.dataUltimoLogin; }
	public void setDataUltimoLogin(Date newDataUltimoLogin) { this.dataUltimoLogin = newDataUltimoLogin; }
	  
	public Date getDataUltimoLoginAnt() { return this.dataUltimoLoginAnt; }
	public void setDataUltimoLoginAnt(Date newDataUltimoLoginAnt) {
												this.dataUltimoLoginAnt = newDataUltimoLoginAnt; }
		
	public String getMpStatus() { return mpStatus; }
	public void setMpStatus(String mpStatus) { this.mpStatus = mpStatus; }
	
	public String getMpSexo() {	return mpSexo; }
	public void setMpSexo(String mpSexo) { this.mpSexo = mpSexo; }
				
}