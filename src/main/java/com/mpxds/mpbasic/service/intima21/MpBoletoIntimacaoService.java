package com.mpxds.mpbasic.service.intima21;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacao;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoIntimacaoService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletoIntimacaos mpBoletoIntimacaos;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpBoletoIntimacao salvar(MpBoletoIntimacao mpBoletoIntimacao) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpBoletoIntimacao.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpBoletoIntimacao.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpBoletoIntimacao.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpBoletoIntimacaos.guardar(mpBoletoIntimacao);
	}
	
}
