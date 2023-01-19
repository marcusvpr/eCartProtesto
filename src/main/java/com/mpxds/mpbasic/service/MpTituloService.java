package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpTituloService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpTitulos mpTitulos;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpTitulo salvar(MpTitulo mpTitulo) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpTitulo.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpTitulo.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpTitulo.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpTitulos.guardar(mpTitulo);
	}
	
}
