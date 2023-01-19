package com.mpxds.mpbasic.model.enums;

public enum MpCartorioComarca {
	//
	C001("C001", "Comarca do XX1º do Registro de Protesto de Títulos", "XX1 Comarca",
			"Rua ???, 999", "??? - Rio de Janeiro / RJ",
			"", "55-21-????-????", "", 
			"???f@protestorj.com.br", "-22.9055107,-43.1744158", "", 
			"??.???.???/0001-??", "10:00/17:00", "17:00",
			"XX1º Comarca ???", 9, 3119, "0", 26564, "0"),
	C002("C002", "Comarca do XX2º do Registro de Protesto de Títulos", "XX2 Comarca",
			"Rua ???, 999", "??? - Rio de Janeiro / RJ",
			"", "55-21-????-????", "", 
			"???f@protestorj.com.br", "-22.9055107,-43.1744158", "", 
			"??.???.???/0001-??", "10:00/17:00", "17:00",
			"XX2º Comarca ???", 9, 3119, "0", 26564, "0");
	//
	private String numero;
	private String nome;
	private String apelido;
	private String endereco;
	private String bairroCidadeUF;
	private String cep;	
	private String telefone;
	private String fax;
	private String email;
	private String googleMaps;
	private String site;
	private String cnpj;
	private String horarioFuncionamento;
	private String horarioGeracaoBoleto;
	private String nomeCedente;
	private Integer carteira;
	private Integer agenciaCodigoCedente;
	private String agenciaCodigoCedenteDig;
	private Integer agenciaContaCedente;
	private String agenciaContaCedenteDig;

	// ---
	
	MpCartorioComarca(String numero,
						String nome,
						String apelido,
						String endereco, 
						String bairroCidadeUF,
						String cep,	
						String telefone,
						String fax,
						String email,
						String googleMaps,
						String site,
						String cnpj,
						String horarioFuncionamento,
						String horarioGeracaoBoleto,
						String nomeCedente,
						Integer carteira,
						Integer agenciaCodigoCedente,
						String agenciaCodigoCedenteDig,
						Integer agenciaContaCedente,
						String agenciaContaCedenteDig) {
		this.numero = numero;
		this.nome = nome;
		this.apelido = apelido;
		this.endereco = endereco;
		this.bairroCidadeUF = bairroCidadeUF;
		this.cep = cep;
		this.telefone = telefone;
		this.fax = fax;
		this.email = email;
		this.googleMaps = googleMaps;
		this.site = site;
		this.cnpj = cnpj;
		this.horarioFuncionamento = horarioFuncionamento;
		this.horarioGeracaoBoleto = horarioGeracaoBoleto;
		this.nomeCedente = nomeCedente;
		this.carteira = carteira;
		this.agenciaCodigoCedente = agenciaCodigoCedente;
		this.agenciaCodigoCedenteDig = agenciaCodigoCedenteDig;
		this.agenciaContaCedente = agenciaContaCedente;
		this.agenciaContaCedenteDig = agenciaContaCedenteDig;
	}

	// ---
	
	public String getNumero() { return numero; }	
	public String getNome() { return nome; }	
	public String getApelido() { return apelido; }	
	public String getEndereco() { return endereco; }
	public String getBairroCidadeUF() { return bairroCidadeUF; }
	public String getCep() { return cep; }
	public String getTelefone() { return telefone; }
	public String getFax() { return fax; }
	public String getEmail() { return email; }
	public String getGoogleMaps() { return googleMaps; }
	public String getSite() { return site; }
	public String getCnpj() { return cnpj; }
	public String getHorarioFuncionamento() { return horarioFuncionamento; }
	public String getHorarioGeracaoBoleto() { return horarioGeracaoBoleto; }
	public String getNomeCedente() { return nomeCedente; }
	public Integer getCarteira() { return carteira; }
	public Integer getAgenciaCodigoCedente() { return agenciaCodigoCedente; }
	public String getAgenciaCodigoCedenteDig() { return agenciaCodigoCedenteDig; }
	public Integer getAgenciaContaCedente() { return agenciaContaCedente; }
	public String getAgenciaContaCedenteDig() { return agenciaContaCedenteDig; }

}
