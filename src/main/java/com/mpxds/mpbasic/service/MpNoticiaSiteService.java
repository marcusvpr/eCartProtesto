package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpNoticiaSite;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.repository.MpNoticiaSites;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpNoticiaSiteService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpNoticiaSites mpNoticiaSites;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpNoticiaSite salvar(MpNoticiaSite mpNoticiaSite) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpNoticiaSite.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpNoticiaSite.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpNoticiaSite.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpNoticiaSites.guardar(mpNoticiaSite);
	}

	@MpTransactional
	public MpNoticiaSite salvarSite(MpNoticiaSite mpNoticiaSite) throws MpNegocioException {
		//		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();

		mpAuditoriaObjeto.setDtHrInc(new Date());
		mpAuditoriaObjeto.setUserInc("CadastroSite");

		mpNoticiaSite.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		//
		return mpNoticiaSites.guardar(mpNoticiaSite);
	}
	
	
}
