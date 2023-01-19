package com.mpxds.mpbasic.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpHorario;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.security.MpSeguranca;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
public class MpBloqueiaBoletoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpSeguranca mpSeguranca;
	
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	private MpCartorioOficio mpCartorioOficioX;
	
	private MpSistemaConfig mpSistemaConfig;

	private Boolean indBloqueioSel;
	private Boolean indBloqueioEmailSel;
	private Boolean indBloqueioCieloSel;

	private MpHorario mpHorarioIni;
	private MpHorario mpHorarioFim;
	
	private MpHorario[] mpHorarioList = MpHorario.values();

	// ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		
		if ( mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if ( mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if ( mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if ( mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4;
		//
    	mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() + 
    																							"_HorarioBoleto");
    	if (null == mpSistemaConfig) {
    		//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" +  this.mpCartorioOficioSel.getNumero() + 
																								"_HorarioBoleto");
    	} else {
    		//
    		this.indBloqueioSel = this.mpSistemaConfig.getIndValor();

    		// 0123456789012
    		// 09:00/17:00   -> H01_00("01:00")
    		//
    		try {
    			//
        		this.mpHorarioIni = MpHorario.valueOf("H" + this.mpSistemaConfig.getValorT().trim().substring(0, 2) 
        													+ "_" + this.mpSistemaConfig.getValorT().substring(3, 5));
        		this.mpHorarioFim = MpHorario.valueOf("H" + this.mpSistemaConfig.getValorT().trim().substring(6, 8)
        													+ "_" + this.mpSistemaConfig.getValorT().substring(9, 11));
    		} catch(Exception e) {
    			//
        		System.out.println("MpBloqueiaTituloBean.inicializar() - ERROR_000 ( TextoT = '" + 
														this.mpSistemaConfig.getValorT() + "' / e =" + e);
        		//
        		this.mpHorarioIni = MpHorario.H09_00;
        		this.mpHorarioFim = MpHorario.H17_00;
    		}
    	}
    	
		//
		mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() +
																							"_IndEnviaEmailBoleto");
		if (null == mpSistemaConfig) {
			//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" + this.mpCartorioOficioSel.getNumero() +
																							"_IndEnviaEmailBoleto");
		} else {
			//
			this.indBloqueioEmailSel = this.mpSistemaConfig.getIndValor();
		}

		mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() +
																							"_IndAtivaCieloBoleto");
		if (null == mpSistemaConfig) {
			//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" + this.mpCartorioOficioSel.getNumero() + 
																							"_IndAtivaCieloBoleto");
		} else {
			//
			this.indBloqueioCieloSel = this.mpSistemaConfig.getIndValor();
		}
		//
    }

	// ---
	
	public void atualizaBloqueio() { 
		//
		//
		String msg = "";
		if (null == this.mpCartorioOficioSel) 
			msg = msg + " (Selecionar Oficio) ";
		//
		if (!msg.isEmpty()) {
			MpFacesUtil.addErrorMessage("Error : " + msg);
			return;
		}
        //
    	this.mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() + 
    																								"_HorarioBoleto");
    	if (null == this.mpSistemaConfig) {
    		//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" +  this.mpCartorioOficioSel.getNumero() + 
																									"_HorarioBoleto");
    	} else {
    		//
    		this.mpSistemaConfig.setIndValor(this.indBloqueioSel);
    		
    		this.mpSistemaConfig.setValorT(this.mpHorarioIni.getHora() + ":" + this.mpHorarioFim.getHora());
    		
    		this.mpSistemaConfig = this.mpSistemaConfigs.guardar(this.mpSistemaConfig);
    		// 
    	}

    	//
		mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() + 
																						"_IndEnviaEmailBoleto");
		if (null == mpSistemaConfig) {
			//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" + this.mpCartorioOficioSel.getNumero() +
																						"_IndEnviaEmailBoleto");
		} else {
			//
    		this.mpSistemaConfig.setIndValor(this.indBloqueioEmailSel);
    		
    		this.mpSistemaConfig = this.mpSistemaConfigs.guardar(this.mpSistemaConfig);
		}
		//
		mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() + 	
																						"_IndAtivaCieloBoleto");
		if (null == mpSistemaConfig) {
			//
			MpFacesUtil.addErrorMessage("Error captura parâmetro : Of" + this.mpCartorioOficioSel.getNumero() +
																						"_IndAtivaCieloBoleto");
		} else {
			//
    		this.mpSistemaConfig.setIndValor(this.indBloqueioCieloSel);
    		
    		this.mpSistemaConfig = this.mpSistemaConfigs.guardar(this.mpSistemaConfig);
		}    	
		// 
		MpFacesUtil.addErrorMessage("Atualização Efetuada ! ( " + this.indBloqueioSel + " / Email = " +
											this.indBloqueioEmailSel + " / Cielo = " + this.indBloqueioCieloSel +
											" / Of = " + this.mpCartorioOficioSel.getNumero()); 	
	}
	
    // ---
    
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																	 this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }
	
	public MpSistemaConfig getMpSistemaConfig() { return mpSistemaConfig; }
	public void setMpSistemaConfig(MpSistemaConfig mpSistemaConfig) { this.mpSistemaConfig = mpSistemaConfig; }
	
	public Boolean getIndBloqueioSel() { return indBloqueioSel; }
	public void setIndBloqueioSel(Boolean indBloqueioSel) { this.indBloqueioSel = indBloqueioSel; }
	
	public MpHorario getMpHorarioIni() { return mpHorarioIni; }
	public void setMpHorarioIni(MpHorario mpHorarioIni) { this.mpHorarioIni = mpHorarioIni; }
	
	public MpHorario getMpHorarioFim() { return mpHorarioFim; }
	public void setMpHorarioFim(MpHorario mpHorarioFim) { this.mpHorarioFim = mpHorarioFim; }
		
	public MpHorario[] getMpHorarioList() { return mpHorarioList; }
	public void setMpHorarioList(MpHorario[] mpHorarioList) { this.mpHorarioList = mpHorarioList; }
	
	//
	
	public Boolean getIndBloqueioEmailSel() { return indBloqueioEmailSel; }
	public void setIndBloqueioEmailSel(Boolean indBloqueioEmailSel) { this.indBloqueioEmailSel = indBloqueioEmailSel; }

	public Boolean getIndBloqueioCieloSel() { return indBloqueioCieloSel; }
	public void setIndBloqueioCieloSel(Boolean indBloqueioCieloSel) { this.indBloqueioCieloSel = indBloqueioCieloSel; }
	
}