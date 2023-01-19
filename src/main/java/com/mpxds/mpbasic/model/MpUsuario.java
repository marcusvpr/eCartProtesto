package com.mpxds.mpbasic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpbasic.model.enums.MpSexo;
import com.mpxds.mpbasic.model.enums.MpStatus;
//import com.mpxds.mpbasic.model.enums.MpTipoCampo;

@Entity
@Table(name = "mp_usuario", indexes = {
		@Index(name = "idxUsuLogin", columnList = "login"), 
		@Index(name = "idxUsuEmail", columnList = "email") 
		} )
public class MpUsuario extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;
	
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
	
	private MpStatus mpStatus;
	private MpSexo mpSexo;	
	
	private List<MpGrupo> mpGrupos = new ArrayList<>();

	private Boolean indUserComarca;

	// ---
	
	@NotBlank(message = "Por favor, informe o LOGIN")
	@Column(nullable = false, length = 15, unique = true)
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
		
	@NotBlank(message = "Por favor, informe o NOME")
	@Column(nullable = false, length = 100)
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	
	@NotBlank(message = "Por favor, informe o E-MAIL")
	@Column(nullable = false, unique = true, length = 255)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	@Column(nullable = false, length = 100) 
	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	@Column(name = "cpf_cnpj", nullable = true)
	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

	@Column(nullable = true, length = 50)
	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	
	@Column(nullable = true, length = 50)
	public String getCelular() { return celular; }
	public void setCelular(String celular) { this.celular = celular; }
	
	@Past(message="Data futuro inválida!")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento", nullable = true)
	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

	@Column(nullable = true, length = 255)
	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }

	@Column(nullable = true, name = "ind_UserWeb")
	public Boolean getIndUserWeb() { 
		if (null == this.indUserWeb) return true;
		//
		return indUserWeb;
	}
	public void setIndUserWeb(Boolean indUserWeb) { this.indUserWeb = indUserWeb; }

	@Column(nullable = true, name = "ind_Ativacao")
	public Boolean getIndAtivacao() { return indAtivacao; }
	public void setIndAtivacao(Boolean indAtivacao) { this.indAtivacao = indAtivacao; }
  
	@Column(nullable = true, length = 10, name = "codigo_Ativacao")
	public String getCodigoAtivacao() { return codigoAtivacao; }
	public void setCodigoAtivacao(String codigoAtivacao) { this.codigoAtivacao = codigoAtivacao; }

	@Column(nullable = true, name = "ind_Boleto")
	public Boolean getIndBoleto() { return indBoleto; }
	public void setIndBoleto(Boolean indBoleto) { this.indBoleto = indBoleto; }
  
	@Column(nullable = true, name = "ind_Titulo")
	public Boolean getIndTitulo() { return indTitulo; }
	public void setIndTitulo(Boolean indTitulo) { this.indTitulo = indTitulo; }

	// ---
	  
	@Column(nullable = true, name = "num_ip_ultimo_login")
	public String getNumIpUltimoLogin() { return this.numIpUltimoLogin; }
	public void setNumIpUltimoLogin(String newNumIpUltimoLogin) { this.numIpUltimoLogin = newNumIpUltimoLogin; }
	  
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, name = "data_ultimo_login")
	public Date getDataUltimoLogin() { return this.dataUltimoLogin; }
	public void setDataUltimoLogin(Date newDataUltimoLogin) { this.dataUltimoLogin = newDataUltimoLogin; }
	  
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, name = "data_ultimo_login_ant")
	public Date getDataUltimoLoginAnt() { return this.dataUltimoLoginAnt; }
	public void setDataUltimoLoginAnt(Date newDataUltimoLoginAnt) {
												this.dataUltimoLoginAnt = newDataUltimoLoginAnt; }
		
	@NotNull(message = "Por favor, informe o STATUS")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	public MpStatus getMpStatus() { return mpStatus; }
	public void setMpStatus(MpStatus mpStatus) { this.mpStatus = mpStatus; }
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 10)
	public MpSexo getMpSexo() {	return mpSexo; }
	public void setMpSexo(MpSexo mpSexo) { this.mpSexo = mpSexo; }
			
	@ManyToMany(fetch = FetchType.EAGER) // (cascade = CascadeType.ALL)
	@JoinTable(name = "mp_usuario_grupo", joinColumns = @JoinColumn(name="mp_usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "mp_grupo_id"))
	public List<MpGrupo> getMpGrupos() { return mpGrupos; }
	public void setMpGrupos(List<MpGrupo> mpGrupos) { this.mpGrupos = mpGrupos; }

	@Column(nullable = true, name = "ind_UserComarca")
	public Boolean getIndUserComarca() { 
		if (null == this.indUserComarca) return false;
		//
		return indUserComarca;
	}
	public void setIndUserComarca(Boolean indUserComarca) { this.indUserWeb = indUserComarca; }
	
	//
	
	@Transient
	public Boolean isIndCpf() {
		//
		if (null == this.cpfCnpj || this.cpfCnpj.trim().length() > 11)
			return false;
		//
		return true;
	}
	
}