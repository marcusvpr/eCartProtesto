package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpAlertaSites implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpAlertaSite porId(Long id) {
		return this.manager.find(MpAlertaSite.class, id);
	}
		
	public List<MpAlertaSite> porDataIniFim(Date dataAlerta) {
		if (null == dataAlerta) 
			return null;
		//	
		List<MpAlertaSite> mpAlertaSites = new ArrayList<MpAlertaSite>();
		
		try {
			mpAlertaSites = this.manager.createQuery("FROM MpAlertaSite WHERE " +
													" dataAlertaIni <= :dataAlerta AND dataAlertaFim >= :dataAlerta",
													MpAlertaSite.class)
						.setParameter("dataAlerta", dataAlerta)
						.getResultList();
		} catch (NoResultException e) {
			// nenhum Alerta Site encontrado com o login informado
		}
		//
		return mpAlertaSites;
	}

	public MpAlertaSite guardar(MpAlertaSite mpAlertaSite) {
		try {
			mpAlertaSite = this.manager.merge(mpAlertaSite);
			this.manager.flush();
			return mpAlertaSite;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse ALERTA SITE... já foi alterado anteriormente!");
		}
	}
		
	@MpTransactional
	public void remover(MpAlertaSite mpAlertaSite) throws MpNegocioException {
		//
		try {
			mpAlertaSite = porId(mpAlertaSite.getId());
			this.manager.remove(mpAlertaSite);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("ALERTA SITE... não pode ser excluído.");
		}
	}
	
	public List<MpAlertaSite> listAll() {
		//
		return this.manager.createQuery("from MpAlertaSite ORDER BY dataAlertaIni",
																				MpAlertaSite.class).getResultList();
	}
	
	public List<MpAlertaSite> listAllOficio(String oficio) {
		//
		return this.manager.createQuery("from MpAlertaSite WHERE numeroOficio = :oficio ORDER BY dataAlertaIni",
																					MpAlertaSite.class)
																					.setParameter("oficio", oficio)
																					.getResultList();
	}
		
}