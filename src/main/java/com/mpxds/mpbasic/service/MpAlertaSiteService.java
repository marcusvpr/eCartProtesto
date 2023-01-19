package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.repository.MpAlertaSites;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpAlertaSiteService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpAlertaSites mpAlertaSites;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpAlertaSite salvar(MpAlertaSite mpAlertaSite) throws MpNegocioException {
		//
		List<MpAlertaSite> mpAlertaSiteExistentes = mpAlertaSites.porDataIniFim(mpAlertaSite.getDataAlertaIni());	
		if (mpAlertaSiteExistentes.size() > 0) {
			//
			for (MpAlertaSite mpAlertaSiteX :mpAlertaSiteExistentes) {
				//
				if (mpAlertaSite.equals(mpAlertaSiteX))
					continue;
				
				if (mpAlertaSite.getDataAlertaIni().equals(mpAlertaSiteX.getDataAlertaIni())) {
					//
					throw new MpNegocioException("Já existe um ALERTA com Oficio/Data Inicial informado. ( " +
													mpAlertaSiteX.getNumeroOficio() + " / " + 
													mpAlertaSiteX.getDataAlertaIniSDF() + " / " + 
													mpAlertaSiteX.getDataAlertaFimSDF());
				}
			}
		}
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpAlertaSite.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpAlertaSite.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpAlertaSite.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpAlertaSites.guardar(mpAlertaSite);
	}

	@MpTransactional
	public MpAlertaSite salvarSite(MpAlertaSite mpAlertaSite) throws MpNegocioException {
		//		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();

		mpAuditoriaObjeto.setDtHrInc(new Date());
		mpAuditoriaObjeto.setUserInc("CadastroSite");

		mpAlertaSite.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		//
		return mpAlertaSites.guardar(mpAlertaSite);
	}
	
}
