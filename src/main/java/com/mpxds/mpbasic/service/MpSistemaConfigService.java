package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpSistemaConfigService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpSistemaConfig salvar(MpSistemaConfig mpSistemaConfig) throws MpNegocioException {
		//
		MpSistemaConfig mpSistemaConfigExistente =
								mpSistemaConfigs.porParametro(mpSistemaConfig.getParametro());
		
		if (mpSistemaConfigExistente != null && !mpSistemaConfigExistente.equals(
																			mpSistemaConfig)) {
			throw new MpNegocioException("Já existe um registro com o Parâmetro informado.");
		}

		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		String loginUsuario = "";
		if (null == mpSeguranca || null == mpSeguranca.getLoginUsuario())
			loginUsuario = "null";
		else
			loginUsuario = mpSeguranca.getLoginUsuario();
		//
		if (null == mpSistemaConfig.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(loginUsuario);
		} else {
			mpAuditoriaObjeto = mpSistemaConfig.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(loginUsuario);				
		}
		mpSistemaConfig.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
				
		return mpSistemaConfigs.guardar(mpSistemaConfig);
	}

}
