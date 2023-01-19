package com.mpxds.mpbasic.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpbasic.model.enums.MpSistemaConfigHeader;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;

@Entity
@Table(name="mp_sistema_config", indexes = {
		@Index(name = "idxSistHeader", columnList = "mpsistemaconfigheader"), 
		@Index(name = "idxSistParametro", columnList = "parametro")
		} )
public class MpSistemaConfig extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;
	
	private MpSistemaConfigHeader mpSistemaConfigHeader = MpSistemaConfigHeader.MpFaltaDefinir;

	private String parametro; 
	private String descricao;
	
	private MpTipoCampo mpTipoCampo = MpTipoCampo.TEXTO ;
	
	private String valorT = "";
	private Integer valorN = 0;
	private BigDecimal valorD = BigDecimal.ZERO;
	private Boolean indValor = false;
	private Date valorDt = new Date();

	private String objeto;

	// ---
	
	public MpSistemaConfig() {
		//
	  super();
	}

	public MpSistemaConfig(MpSistemaConfigHeader mpSistemaConfigHeader
						, String parametro
						, String descricao
						, MpTipoCampo mpTipoCampo
						, String valorT
						, Integer valorN
						, BigDecimal valorD
						, Boolean indValor
						, String objeto
						, Date valorDt) {
		//
		this.mpSistemaConfigHeader = mpSistemaConfigHeader;
		this.parametro = parametro;
		this.descricao = descricao;
		this.mpTipoCampo = mpTipoCampo;
		this.valorT = valorT;
		this.valorN = valorN;
		this.valorD = valorD;
		this.indValor = indValor;
		this.objeto = objeto;
		this.valorDt = valorDt;
	}
	
	// 
	
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	public MpSistemaConfigHeader getMpSistemaConfigHeader() { return this.mpSistemaConfigHeader; }
	public void setMpSistemaConfigHeader(MpSistemaConfigHeader newMpSistemaConfigHeader) { 
															this.mpSistemaConfigHeader = newMpSistemaConfigHeader; }

	@NotBlank(message = "Por favor, informe o PARAMÊTRO")
	@Column(nullable = false, length = 100)
	public String getParametro() { return this.parametro; }
	public void setParametro(String newParametro) { this.parametro = newParametro; }

	@NotBlank(message = "Por favor, informe o DESCRIÇÃO")
	public String getDescricao() { return this.descricao; }
	public void setDescricao(String newDescricao) { this.descricao = newDescricao; }

	@NotNull(message = "Por favor, informe o TIPO CAMPO")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public MpTipoCampo getMpTipoCampo() { return this.mpTipoCampo; }
	public void setMpTipoCampo(MpTipoCampo newMpTipoCampo) { this.mpTipoCampo = newMpTipoCampo; }

	@NotNull(message = "Por favor, informe o VALOR t")
	@Column(nullable = false, length = 10000)
	public String getValorT() { return this.valorT; }
	public void setValorT(String newValorT) { this.valorT = newValorT; }

	@NotNull(message = "Por favor, informe o VALOR n")
	@Column(nullable = false)
	public Integer getValorN() { return this.valorN; }
	public void setValorN(Integer newValorN) { this.valorN = newValorN; }

	@NotNull(message = "Por favor, informe o VALOR d")
	@Column(nullable = false)
	public BigDecimal getValorD() { return this.valorD; }
	public void setValorD(BigDecimal newValorD) { this.valorD = newValorD; }

	@NotNull(message = "Por favor, informe o VALOR b")
	@Column(nullable = false)
	public Boolean getIndValor() { return this.indValor; }
	public void setIndValor(Boolean newIndValor) { this.indValor = newIndValor; }

	@Column(nullable = true, length = 30)
	public String getObjeto() { return this.objeto; }
	public void setObjeto(String newObjeto) { this.objeto = newObjeto; }

	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
	public Date getValorDt() { return this.valorDt; }
	public void setValorDt(Date newValorDt) { this.valorDt = newValorDt; }

	@Transient
	public String getValor() {
		if (this.mpTipoCampo.equals(MpTipoCampo.TEXTO))
			return this.valorT.trim();

		if (this.mpTipoCampo.equals(MpTipoCampo.NUMERO))
			return "" + this.valorN;

		if (this.mpTipoCampo.equals(MpTipoCampo.DECIMAL))
			return "" + this.valorD;

		return "" + this.indValor;
	}

	@Transient
	public String getParametroValor() {
		//
		if (this.mpTipoCampo.equals(MpTipoCampo.TEXTO)
		||  this.mpTipoCampo.equals(MpTipoCampo.TEXTOEDITOR))
			return this.parametro.trim() + " / " + this.valorT.trim();

		if (this.mpTipoCampo.equals(MpTipoCampo.NUMERO))
			return this.parametro.trim() + " / " + this.valorN;

		if (this.mpTipoCampo.equals(MpTipoCampo.DECIMAL))
			return this.parametro.trim() + " / " + this.valorD;

		return this.parametro.trim() + " / " + this.indValor;
	}

	@Transient
	public Boolean getIndTextoEditor() {
		//
		if (this.mpTipoCampo.equals(MpTipoCampo.TEXTOEDITOR))
			return true;
		//
		return false;
	}

	@Transient
	public Boolean isIndTextoEditor() {
		//
		if (this.mpTipoCampo.equals(MpTipoCampo.TEXTOEDITOR))
			return true;
		//
		return false;
	}

	@Transient
	public Object obterValor(MpTipoCampo mpTipoCampo) {
		//
		Object objX = null;

		switch (mpTipoCampo) {
		//
			case BOOLEAN:
				objX = (Boolean) this.getIndValor();
				break;
			case TEXTO:
				objX = (String) this.getValorT();
				break;
			case TEXTOEDITOR:
				objX = (String) this.getValorT();
				break;
			case DECIMAL:
				objX = (BigDecimal) this.getValorD();
				break;
			case NUMERO:
				objX = (Integer) this.getValorN();
				break;
			case DATE:
				objX = (Date) this.getValorDt();
				break;
			default:
				objX = null;
		}
		//
		return objX;
	}

}
