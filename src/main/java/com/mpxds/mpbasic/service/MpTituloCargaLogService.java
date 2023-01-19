package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpTituloCargaLog;
import com.mpxds.mpbasic.repository.MpTituloCargaLogs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpTituloCargaLogService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpTituloCargaLogs mpTituloCargaLogs;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpTituloCargaLog salvar(MpTituloCargaLog mpTituloCargaLog) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpTituloCargaLog.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpTituloCargaLog.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpTituloCargaLog.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpTituloCargaLogs.guardar(mpTituloCargaLog);
	}
	
}
