package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpCargaControle;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpCargaControles;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;
//import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

public class MpCargaControleService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpCargaControles mpCargaControles;

	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	// -------------
	
	@MpTransactional
	public MpCargaControle salvar(MpCargaControle mpCargaControle) throws MpNegocioException {
		//
		List<MpCargaControle> mpCargaControleExistentes = mpCargaControles.porDataIniFim(mpCargaControle.
																									getDataHoraIni());	
		if (mpCargaControleExistentes.size() > 0) {
			//
			for (MpCargaControle mpCargaControleX :mpCargaControleExistentes) {
				//
				if (mpCargaControle.equals(mpCargaControleX))
					continue;
				
				if (mpCargaControle.getDataHoraIni().equals(mpCargaControleX.getDataHoraIni())) {
					//
					throw new MpNegocioException("Já existe um ALERTA com Oficio/Data Inicial informado. ( " +
													mpCargaControleX.getNumeroOficio() + " / " + 
													mpCargaControleX.getDataHoraIniSDF() + " / " + 
													mpCargaControleX.getDataHoraFimSDF());
				}
			}
		}
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpCargaControle.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpCargaControle.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpCargaControle.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpCargaControles.guardar(mpCargaControle);
	}

	@MpTransactional
	public MpCargaControle salvarSite(MpCargaControle mpCargaControle) throws MpNegocioException {
		//		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();

		mpAuditoriaObjeto.setDtHrInc(new Date());
		mpAuditoriaObjeto.setUserInc("CadastroSite");

		mpCargaControle.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		//
		return mpCargaControles.guardar(mpCargaControle);
	}
		
	public String validaCargaControle(String numeroOficioX) {
		//
		// Verifica Ativação Validação !
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaCargaControle");
    	if (null == mpSistemaConfig)
    		return "Error! Configuração Sistema não encontrada = 'indAtivaCargaControle' !";
    	else {
    		//
//    		System.out.println("MpCargaControleService.validaCargaControle() ( " + mpSistemaConfig.getIndValor());
    		
    		if (!mpSistemaConfig.getIndValor()) // Se 'false' = 'desativado' (Não trata validação!) ...
    			return "";
    	}
		
		//
		String msgX = "";
	
		// Trata Data Corrente ...
		List<MpCargaControle> mpCargaControleListX = this.mpCargaControles.porDataIniFim(new Date());
        
        Boolean indCargaControle = false;
        
        for (MpCargaControle mpCargaControleX : mpCargaControleListX) {
        	//
        	if (mpCargaControleX.getNumeroOficio().contentEquals(numeroOficioX))
        		indCargaControle = true;
        	else {
        		//
        		msgX = "Bloqueio de Carga Controle ( Oficio = " + 
            			mpCargaControleX.getNumeroOficio() + "\n/ " +
            			mpCargaControleX.getDataHoraIniSDF() + "/ " +  
            			mpCargaControleX.getDataHoraFimSDF() + "\n/ " +
            			mpCargaControleX.getMensagem();
        	}
        }

        // Trata Data Recorrente ... 
        Date dataHoraSistemaX = new Date();
        Boolean indRecorrenteX = true;
        
		mpCargaControleListX = this.mpCargaControles.porOficioIndRecorrente(numeroOficioX, indRecorrenteX);
        
        for (MpCargaControle mpCargaControleX : mpCargaControleListX) {
        	//
        	if (dataHoraSistemaX.getTime() >= mpCargaControleX.getDataHoraIni().getTime()
        	&&  dataHoraSistemaX.getTime() <= mpCargaControleX.getDataHoraFim().getTime()) {
        		//
        		indCargaControle = true;
        		break;
        	}
        }
		//
        if (!indCargaControle)
        	msgX = msgX + "Criar Horário Carga Controle !";
        //
        return msgX;
	}
	
}
