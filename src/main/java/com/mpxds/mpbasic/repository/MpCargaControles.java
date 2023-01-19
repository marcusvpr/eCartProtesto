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
import com.mpxds.mpbasic.model.MpCargaControle;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpCargaControles implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpCargaControle porId(Long id) {
		//
		return this.manager.find(MpCargaControle.class, id);
	}
		
	public List<MpCargaControle> porDataIniFim(Date dataHora) {
		//
		if (null == dataHora) 
			return null;
		//	
		List<MpCargaControle> mpCargaControles = new ArrayList<MpCargaControle>();
		
		try {
			mpCargaControles = this.manager.createQuery("FROM MpCargaControle WHERE " +
													" dataHoraIni <= :dataHora AND dataHoraFim >= :dataHora",
													MpCargaControle.class)
						.setParameter("dataHora", dataHora)
						.getResultList();
		} catch (NoResultException e) {
			// nenhuma CARGA CONTROLE encontrada ...
		}
		//
		return mpCargaControles;
	}
	
	public List<MpCargaControle> porOficioIndRecorrente(String numeroOficio, Boolean indRecorrente) {
		//
		if (null == numeroOficio || numeroOficio.isEmpty()) 
			return null;
		//	
		List<MpCargaControle> mpCargaControles = new ArrayList<MpCargaControle>();
		
		try {
			mpCargaControles = this.manager.createQuery("FROM MpCargaControle WHERE " +
													" numeroOficio = :numeroOficio AND indRecorrente = :indRecorrente",
													MpCargaControle.class)
						.setParameter("numeroOficio", numeroOficio)
						.setParameter("indRecorrente", indRecorrente)
						.getResultList();
		} catch (NoResultException e) {
			// nenhuma CARGA CONTROLE encontrada ...
		}
		//
		return mpCargaControles;
	}

	public MpCargaControle guardar(MpCargaControle mpCargaControle) {
		//
		try {
			mpCargaControle = this.manager.merge(mpCargaControle);
			this.manager.flush();
			return mpCargaControle;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse CARGA CONTROLE... já foi alterado anteriormente!");
		}
	}
		
	@MpTransactional
	public void remover(MpCargaControle mpCargaControle) throws MpNegocioException {
		//
		try {
			mpCargaControle = porId(mpCargaControle.getId());
			this.manager.remove(mpCargaControle);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("CARGA CONTROLE... não pode ser excluído.");
		}
	}
	
	public List<MpCargaControle> listAll() {
		//
		return this.manager.createQuery("from MpCargaControle ORDER BY dataHoraIni",
																				MpCargaControle.class).getResultList();
	}
	
	public List<MpCargaControle> listAllOficio(String oficio) {
		//
		return this.manager.createQuery("from MpCargaControle WHERE numeroOficio = :oficio ORDER BY dataHoraIni",
																					MpCargaControle.class)
																					.setParameter("oficio", oficio)
																					.getResultList();
	}
		
}