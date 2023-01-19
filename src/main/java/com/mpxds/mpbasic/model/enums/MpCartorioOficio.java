package com.mpxds.mpbasic.model.enums;

public enum MpCartorioOficio {
	//
	Of1("1", "Tabelionato do 1º do Registro de Protesto de Títulos", "1º Ofício",
			"Av. Erasmo Braga, 227 – 1 andar – grupos 101 a 113", "Centro - Rio de Janeiro / RJ",
			"", "(21)2531-1687", "2531-2597", "2531-2568", "2351-2578", "(21)99937-0791", "", 
			"cartorio1of@protestorjcapital.com.br", "-22.9055107,-43.1744158", "", 
			"27.586.775/0001-47", "12:00/16:00", "17:00",
			"1º OFICIO DE PROTESTO DE TITULOS DO RIO DE JANEIRO", 9, 3119, "0", 26564, "0"),
	
	Of2("2", "Tabelionato do 2º do Registro de Protesto de Títulos", "2º Ofício",
			"Rua do Carmo, 9 – 3º andar", "Centro - Rio de Janeiro / RJ",
			"20011-020", "(21)2531-2427", "", "", "", "(21)98395-0306", "",
			"cartorio2of@protestorjcapital.com.br", "-22.9084817,-43.1834691", "", 
			"27.128.867/0001-83", "12:00/16:00", "17:00",
			"2º OFICIO DE PROTESTO DE TITULOS DO RIO DE JANEIRO", 9, 3119, "0", 26564, "0"),

	Of3("3", "Tabelionato do 3º do Registro de Protesto de Títulos",  "3º Ofício",
			"Rua da Assembléia, 10 – 21º andar – salas2101 a 2110", "Centro - Rio de Janeiro / RJ",
			"", "(21)2510-2802", "", "", "", "", "", 
			"cartorio3of@protestorjcapital.com.br", "-22.9040579,-43.1753412", "www.institutodeprotestorj.com.br",
			"27.074.558/0001-78", "12:00/16:00", "17:00",
			"3º OFICIO DE PROTESTO DE TITULOS DO RIO DE JANEIRO", 9, 3119, "0", 26565, "9"),
	
	Of4("4", "Tabelionato do 4º do Registro de Protesto de Títulos",  "4º Ofício",
			"Rua da Assembléia, 10 – 21º andar – sala 2114", "Centro - Rio de Janeiro / RJ",
			"", "(21)2531-1977", "", "", "", "(21)97611-4203", "",
			"cartorio4of@protestorjcapital.com.br", "-22.9042366,-43.1757766", "", 
			"27.128.883/0001-76", "12:00/16:00", "17:00",
			"4º OFICIO DE PROTESTO DE TITULOS DO RIO DE JANEIRO", 9, 3119, "0", 26564, "0"),

	OfX("X", "Não se aplica", "Não se aplica", "", "", "", "", "", "", "", "", "", "", "", "", "", "10:00/17:00",  "17:00",
			"", 0, 0, "", 0, "");
	
	private String numero;
	private String nome;
	private String apelido;
	private String endereco;
	private String bairroCidadeUF;
	private String cep;	
	private String telefone;
	private String telefone1;
	private String telefone2;
	private String telefone3;
	private String celular;
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
	
	MpCartorioOficio(String numero,
						String nome,
						String apelido,
						String endereco, 
						String bairroCidadeUF,
						String cep,	
						String telefone,
						String telefone1,
						String telefone2,
						String telefone3,
						String celular,
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
		this.telefone1 = telefone1;
		this.telefone2 = telefone2;
		this.telefone3 = telefone3;
		this.celular = celular;
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
	public String getTelefone1() { return telefone1; }
	public String getTelefone2() { return telefone2; }
	public String getTelefone3() { return telefone3; }

	public String getTelefones() { return telefone + " / " + telefone1 + " / " + telefone2 + " / " + telefone3; }

	//
	public String getCelular() { return celular; }
	public String getCelularNumero() { return celular.replace("(", "").replace(")", "").replace("-", ""); }

	//
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
