package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpAutorizaCancelamentoCargaLog;
import com.mpxds.mpbasic.repository.MpAutorizaCancelamentoCargaLogs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpAutorizaCancelamentoCargaLogService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpAutorizaCancelamentoCargaLogs mpAutorizaCancelamentoCargaLogs;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpAutorizaCancelamentoCargaLog salvar(MpAutorizaCancelamentoCargaLog mpAutorizaCancelamentoCargaLog) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpAutorizaCancelamentoCargaLog.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpAutorizaCancelamentoCargaLog.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpAutorizaCancelamentoCargaLog.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpAutorizaCancelamentoCargaLogs.guardar(mpAutorizaCancelamentoCargaLog);
	}
	
}
