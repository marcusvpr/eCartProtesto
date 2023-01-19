package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAutorizaCancelamento;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpAutorizaCancelamentos implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpAutorizaCancelamento porId(Long id) {
		//
		return this.manager.find(MpAutorizaCancelamento.class, id);
	}
	
	public List<MpAutorizaCancelamento> mpAutorizaCancelamentoByDataDocumentoList() {
		//
		return this.manager.createQuery("from MpAutorizaCancelamento ORDER BY data",
																	MpAutorizaCancelamento.class).getResultList();
	}
	
	public List<MpAutorizaCancelamento> mpAutorizaCancelamentoByOficioCpfCnpjList(String oficio, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpAutorizaCancelamento where codigoOficio = :oficio" + 
										" AND documentoDevedor = :cpfCnpj ORDER BY dataProtocolo, protocolo",
										MpAutorizaCancelamento.class)
				.setParameter("oficio", oficio)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpAutorizaCancelamento> mpAutorizaCancelamentoByCpfCnpjList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpAutorizaCancelamento where documentoDevedor = :cpfCnpj " + 
										" ORDER BY codigoOficio, dataProtocolo, protocolo",
										MpAutorizaCancelamento.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	@MpTransactional
	public MpAutorizaCancelamento guardar(MpAutorizaCancelamento mpAutorizaCancelamento) {
		//
		try {
			mpAutorizaCancelamento = this.manager.merge(mpAutorizaCancelamento);
			
			this.manager.flush();
			//
			return mpAutorizaCancelamento;
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException(
					"Erro de concorrência. Esse AutorizaCancelamento... já foi alterado anteriormente!");
		}
	}

	public List<MpAutorizaCancelamento> listAll() {
		//
		return this.manager.createQuery("from MpAutorizaCancelamento ORDER BY codigoOficio, data", 
				MpAutorizaCancelamento.class).getResultList();
	}

	public String executeSQL(String sqlX) {
		//
		try {
			this.manager.getTransaction().begin();
			this.manager.createNativeQuery(sqlX).executeUpdate();
			//
			return "OK";
			//
		} catch (Exception e) {
			//
			this.manager.getTransaction().rollback();
			
			return "ERROR ( e = " + e;
        } finally {
        	//
            if (this.manager.getTransaction().isActive())
            	this.manager.getTransaction().commit();
        }	
	}
		
	public MpAutorizaCancelamento porDataNumeroProtocolo(Date dataProtocolo, String protocolo) {
		//	
		MpAutorizaCancelamento mpAutorizaCancelamento = null;
		
		try {
			mpAutorizaCancelamento = this.manager.createQuery("from MpAutorizaCancelamento" + 
										" where dataProtocolo = :dataProtocolo and protocolo = :protocolo",
										MpAutorizaCancelamento.class)
						.setParameter("dataProtocolo", dataProtocolo)
						.setParameter("protocolo", protocolo)
						.getSingleResult();
		} catch (NoResultException e) {
			// nenhum registro encontrado !
		}
		//
		return mpAutorizaCancelamento;
	}
	

	public List<MpAutorizaCancelamento> mpTituloByOficioNumeroProtocoloList(String oficio, String numeroProtocolo) {
		//
		return this.manager.createQuery("from MpAutorizaCancelamento where codigoOficio = :oficio AND "
						+ " protocolo = :numeroProtocolo", MpAutorizaCancelamento.class)
				.setParameter("oficio", oficio)
				.setParameter("numeroProtocolo", numeroProtocolo)
				.getResultList();
	}

}