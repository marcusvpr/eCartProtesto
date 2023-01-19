package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpContatoSite;

public class MpContatoSites implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpContatoSite porId(Long id) {
		//
		return this.manager.find(MpContatoSite.class, id);
	}
	
	public MpContatoSite porDataContato(Date dataContato) {
		//
		return this.manager.find(MpContatoSite.class, dataContato);
	}
	
	public List<MpContatoSite> mpContatoSiteByDataList() {
		//
		return this.manager.createQuery("from MpContatoSite ORDER BY dataContato", 
																			MpContatoSite.class).getResultList();
	}

	public MpContatoSite porEmail(String email) {
		//
		MpContatoSite mpContatoSite = null;
		
		try {
			mpContatoSite = this.manager.createQuery("from MpContatoSite where lower(email) = :email", 
																								MpContatoSite.class)
				.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return mpContatoSite;
	}
	
	public MpContatoSite guardar(MpContatoSite mpContatoSite) {
		//
		try {
			return this.manager.merge(mpContatoSite);
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse Contato Site... já foi alterado anteriormente!");
		}
	}
	
}