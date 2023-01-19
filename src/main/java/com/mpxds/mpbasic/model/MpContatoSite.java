package com.mpxds.mpbasic.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpStatusContatoSite;

@Entity
@Table(name = "mp_contato_site")
public class MpContatoSite extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;
			
	private Date dataContato;
	private String nome;
	private String email;	
	private String telefone;
	private String assunto;	
	private String mensagem;

	private Date dataSolucao;
	private String solucao;
	private String responsavel;
	
    private Boolean indEditavel;
	
	private MpCartorioOficio mpCartorioOficio;
	private MpStatusContatoSite mpStatusContatoSite;
	
	// ---

	@NotNull(message = "Por favor, informe a DATA")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_contato", nullable = false)
	public Date getDataContato() { return dataContato; }
	public void setDataContato(Date dataContato) { this.dataContato = dataContato; }
	
	@NotBlank(message = "Por favor, informe o NOME")
	@Column(nullable = false, length = 100)
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	
	@NotBlank(message = "Por favor, informe o E-MAIL")
	@Column(nullable = false, length = 255)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	@NotBlank(message = "Por favor, informe o TELEFONE")
	@Column(nullable = false, length = 50)
	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	
	@NotBlank(message = "Por favor, informe o ASSUNTO")
	@Column(nullable = false, length = 100)
	public String getAssunto() { return assunto; }
	public void setAssunto(String assunto) { this.assunto = assunto; }
	
	@NotBlank(message = "Por favor, informe a MENSAGEM")
	@Column(nullable = false, length = 255)
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_solucao", nullable = true)
	public Date getDataSolucao() { return dataSolucao; }
	public void setDataSolucao(Date dataSolucao) { this.dataSolucao = dataSolucao; }
	
	@Column(nullable = true, length = 255)
	public String getSolucao() { return solucao; }
	public void setSolucao(String solucao) { this.solucao = solucao; }

	@Column(nullable = true, length = 100)
	public String getResponsavel() { return responsavel; }
	public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
	
	@Column(name="ind_editavel", nullable=true)
    public Boolean getIndEditavel() { return this.indEditavel; }
    public void setIndEditavel(Boolean newIndEditavel) { this.indEditavel = newIndEditavel; }
	
	@NotNull(message = "Por favor, informe o CARTÓRIO DE OFICIO")
	@Enumerated(EnumType.STRING)
	@Column(name = "mp_Cartorio_Oficio", nullable = false, length = 10)
	public MpCartorioOficio getMpCartorioOficio() {	return mpCartorioOficio; }
	public void setMpCartorioOficio(MpCartorioOficio mpCartorioOficio) { this.mpCartorioOficio = mpCartorioOficio; }
	  
	@NotNull(message = "Por favor, informe o STATUS")
	@Enumerated(EnumType.STRING)
	@Column(name = "mp_Status_Contato_Site", nullable = false, length = 20)
	public MpStatusContatoSite getMpStatusContatoSite() { return mpStatusContatoSite; }
	public void setMpStatusContatoSite(MpStatusContatoSite mpStatusContatoSite) { 
																	this.mpStatusContatoSite = mpStatusContatoSite; }
	// ---
	
	@Transient
	public Boolean isNovo() { 
		//
		if (this.mpStatusContatoSite.equals(MpStatusContatoSite.NOVO))
			return true;
		//
		return false; 
	}

	@Transient
	public String getDataContatoSDF() { 
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

		return sdf.format(this.dataContato); 
	}
		
}