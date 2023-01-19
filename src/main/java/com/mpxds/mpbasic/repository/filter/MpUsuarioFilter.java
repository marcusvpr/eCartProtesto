package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;

public class MpUsuarioFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String login;
	private String email;
	private String nome;
	
	// ---

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
}