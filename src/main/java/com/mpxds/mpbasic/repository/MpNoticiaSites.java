package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpNoticiaSite;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpNoticiaSites implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpNoticiaSite porId(Long id) {
		return this.manager.find(MpNoticiaSite.class, id);
	}
		
	public MpNoticiaSite guardar(MpNoticiaSite mpNoticiaSite) {
		try {
			mpNoticiaSite = this.manager.merge(mpNoticiaSite);
			this.manager.flush();
			return mpNoticiaSite;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse NOTICIA SITE... já foi alterado anteriormente!");
		}
	}
		
	@MpTransactional
	public void remover(MpNoticiaSite mpNoticiaSite) throws MpNegocioException {
		//
		try {
			mpNoticiaSite = porId(mpNoticiaSite.getId());
			this.manager.remove(mpNoticiaSite);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("NOTICIA SITE... não pode ser excluído.");
		}
	}
	
	public List<MpNoticiaSite> listAll() {
		//
		return this.manager.createQuery("from MpNoticiaSite ORDER BY dataNoticia DESC",
																				MpNoticiaSite.class).getResultList();
	}
	
	public List<MpNoticiaSite> listAllOficio(String oficio) {
		//
		return this.manager.createQuery("from MpNoticiaSite WHERE numeroOficio = :oficio ORDER BY dataNoticia DESC",
																				MpNoticiaSite.class)
																				.setParameter("oficio", oficio)
																				.getResultList();
	}
		
}