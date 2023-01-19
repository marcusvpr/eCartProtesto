package com.mpxds.mpbasic.service.intima21;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoLog;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaoLogs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoIntimacaoLogService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletoIntimacaoLogs mpBoletoIntimacaoLogs;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpBoletoIntimacaoLog salvar(MpBoletoIntimacaoLog mpBoletoIntimacaoLog) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpBoletoIntimacaoLog.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpBoletoIntimacaoLog.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpBoletoIntimacaoLog.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpBoletoIntimacaoLogs.guardar(mpBoletoIntimacaoLog);
	}
	
}
