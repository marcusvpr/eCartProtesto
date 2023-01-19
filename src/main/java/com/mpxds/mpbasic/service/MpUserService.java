package com.mpxds.mpbasic.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpUser;
import com.mpxds.mpbasic.repository.MpUsers;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpUserService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpUsers mpUsers;

	// -------------
	
	@MpTransactional
	public MpUser salvar(MpUser mpUser) throws MpNegocioException {
		//
		MpUser mpUserExistente = mpUsers.porLogin(mpUser.getUser_login());	
		if (mpUserExistente != null && !mpUserExistente.equals(mpUser))
			throw new MpNegocioException("Já existe uma usuario com o LOGIN informado.");
		mpUserExistente = mpUsers.porEmail(mpUser.getUser_email());	
		if (mpUserExistente != null && !mpUserExistente.equals(mpUser))
			throw new MpNegocioException("Já existe uma usuario com o EMAIL informado.");
		mpUserExistente = mpUsers.porNome(mpUser.getUser_nicename());	
		if (mpUserExistente != null && !mpUserExistente.equals(mpUser))
			throw new MpNegocioException("Já existe uma usuario com o NOME informado.");
		//
		return mpUsers.guardar(mpUser);
	}

	@MpTransactional
	public MpUser salvarSite(MpUser mpUser) throws MpNegocioException {
		//
		return mpUsers.guardar(mpUser);
	}
	
}
