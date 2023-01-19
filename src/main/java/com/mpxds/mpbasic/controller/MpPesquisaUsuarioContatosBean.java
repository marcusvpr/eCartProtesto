package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpUsuarioContato;
import com.mpxds.mpbasic.repository.MpUsuarioContatos;

@Named
@SessionScoped
public class MpPesquisaUsuarioContatosBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpUsuarioContatos mpUsuarioContatos;

    private String cpfCnpj;

	private List<MpUsuarioContato> mpUsuarioContatoList;
	
    // ---
	
	public void init() {
		//		
		this.cpfCnpj = "";
	}

	public void pesquisaUsuarioContato() {
		//
		if (this.cpfCnpj.isEmpty())
			this.mpUsuarioContatoList = mpUsuarioContatos.listAll();		
		else
			this.mpUsuarioContatoList = mpUsuarioContatos.mpUsuarioContatoByCpfCnpjoList(this.cpfCnpj);		
		//
	}
	
	// ---
	
	public List<MpUsuarioContato> getMpUsuarioContatoList() { return mpUsuarioContatoList; }
	public void setMpUsuarioContatoList(List<MpUsuarioContato> mpUsuarioContatoList) {
																this.mpUsuarioContatoList = mpUsuarioContatoList; }

	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
		
}