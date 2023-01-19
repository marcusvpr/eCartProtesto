package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpUser;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpUsers implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpUser porId(Long id) {
		return this.manager.find(MpUser.class, id);
	}
	
	public List<MpUser> mpUserByLoginList() {
		// TODO filtrar apenas usuarios (por um grupo específico)
		return this.manager.createQuery("from MpUser ORDER BY user_login", MpUser.class).getResultList();
	}

	public MpUser porLoginEmail(String loginEmail) {
		if (null == loginEmail || loginEmail.isEmpty()) 
			return null;
		//	
		MpUser mpUser = null;
		
//		MpAppUtil.PrintarLn("MpUsers.porLoginEmail() (loginEmail = " + loginEmail);

		try {
			mpUser = this.manager.createQuery("from MpUser where lower(user_login) = :loginEmail" + 
																  " or lower(user_email) = :loginEmail" ,
			MpUser.class).setParameter("loginEmail", loginEmail.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpUser;
	}
	
	public MpUser porLogin(String login) {
		if (null == login || login.isEmpty()) 
			return null;
		//	
		MpUser mpUser = null;
		
		try {
			mpUser = this.manager.createQuery("from MpUser where lower(user_login) = :login",
																				MpUser.class)
						.setParameter("login", login.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login informado
		}
		//
		return mpUser;
	}

	public MpUser porEmail(String email) {
		if (null == email || email.isEmpty()) 
			return null;
		//	
		MpUser mpUser = null;
		//
		try {
			mpUser = this.manager.createQuery("from MpUser where lower(user_email) = :email",
																				MpUser.class)
						.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return mpUser;
	}

	public MpUser porNome(String nome) {
		//
		MpUser usuario = null;
		
		try {
			usuario = this.manager.createQuery("from MpUser where lower(user_nicename) = :nome", MpUser.class)
				.setParameter("nome", nome.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return usuario;
	}
	
	public MpUser guardar(MpUser mpUser) {
		try {
			mpUser = this.manager.merge(mpUser);
			this.manager.flush();
			return mpUser;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse USUÁRIO... já foi alterado anteriormente!");
		}
	}
		
	@MpTransactional
	public void remover(MpUser mpUser) throws MpNegocioException {
		//
		try {
			mpUser = porId(mpUser.getId());
			this.manager.remove(mpUser);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("USUÁRIO... não pode ser excluído.");
		}
	}
	
	public List<MpUser> listAll() {
		//
		return this.manager.createQuery("from MpUser ORDER BY user_login", MpUser.class).getResultList();
	}
		
}