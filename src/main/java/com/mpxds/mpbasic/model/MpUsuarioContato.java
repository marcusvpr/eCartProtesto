package com.mpxds.mpbasic.model;

//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;

//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotBlank;
//
//import com.mpxds.mpbasic.model.enums.MpSexo;
//import com.mpxds.mpbasic.model.enums.MpStatus;
//import com.mpxds.mpbasic.model.enums.MpTipoCampo;

@Entity
@Table(name = "mp_usuario_contato", indexes = {
		@Index(name = "idxUConCpfCnpj", columnList = "cpf_cnpj"), 
		@Index(name = "idxUConEmail", columnList = "email") 
		} )
public class MpUsuarioContato extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;
	
	private String cpfCnpj;
	private String nome;
	private String email;
	private String telefone;

	// ---
	
	@Column(name = "cpf_cnpj", nullable = true)
	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
	
	@Column(nullable = false, length = 100)
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	
	@Column(nullable = false, length = 500)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	@Column(nullable = true, length = 200)
	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	
	
}