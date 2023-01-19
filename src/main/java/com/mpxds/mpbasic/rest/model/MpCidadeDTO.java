package com.mpxds.mpbasic.rest.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MpCidadeDTO {
	//
	private Integer id;
	private String nome;
	private String estado;
	private Integer estadoId;
	private Integer version;
	
	// ---
	
	public MpCidadeDTO() { }

	public String getNomeUf() {
		//
		return this.nome + " - " + this.estado;
	}

	public String getJson(MpCidadeDTO obj) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Integer estadoId) {
		this.estadoId = estadoId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
