package com.mpxds.mpbasic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "mp_grupo")
public class MpGrupo extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;

	// ---
	
	@NotBlank(message = "Por favor, informe o NOME")
	@Column(nullable = false, length = 40)
	public String getNome() {  return nome; }
	public void setNome(String nome) { this.nome = nome; }

	@NotBlank(message = "Por favor, informe a DESCRIÇÃO")
	@Column(nullable = false, length = 80)
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

}