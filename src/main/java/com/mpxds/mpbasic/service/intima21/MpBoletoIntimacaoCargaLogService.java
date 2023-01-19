package com.mpxds.mpbasic.service.intima21;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoCargaLog;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaoCargaLogs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoIntimacaoCargaLogService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletoIntimacaoCargaLogs mpBoletoIntimacaoCargaLogs;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpBoletoIntimacaoCargaLog salvar(MpBoletoIntimacaoCargaLog mpBoletoIntimacaoCargaLog) 
																						throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpBoletoIntimacaoCargaLog.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpBoletoIntimacaoCargaLog.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpBoletoIntimacaoCargaLog.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpBoletoIntimacaoCargaLogs.guardar(mpBoletoIntimacaoCargaLog);
	}
	
}
