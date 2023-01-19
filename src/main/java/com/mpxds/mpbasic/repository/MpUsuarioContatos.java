package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpUsuarioContato;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpUsuarioContatos implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpUsuarioContato porId(Long id) {
		//
		return this.manager.find(MpUsuarioContato.class, id);
	}
	
	public List<MpUsuarioContato> mpUsuarioContatoByLoginList() {
		//
		// TODO filtrar apenas usuarioContatos (por um grupo específico)
		return this.manager.createQuery("from MpUsuarioContato ORDER BY login", 
																			MpUsuarioContato.class).getResultList();
	}

	public MpUsuarioContato porCpfCnpj(String cpfCnpj) {
		//
		if (null == cpfCnpj || cpfCnpj.isEmpty()) 
			return null;
		//	
		MpUsuarioContato mpUsuarioContato = null;
		
//		MpAppUtil.PrintarLn("MpUsuarioContatos.porLoginEmail() (loginEmail = " + loginEmail);

		try {
			mpUsuarioContato = this.manager.createQuery("from MpUsuarioContato where cpfCnpj = :cpfCnpj" ,
			MpUsuarioContato.class).setParameter("cpfCnpj", cpfCnpj).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpUsuarioContato;
	}
	
	public MpUsuarioContato porNome(String nome) {
		//
		MpUsuarioContato usuarioContato = null;
		
		try {
			usuarioContato = this.manager.createQuery("from MpUsuarioContato where lower(nome) = :nome", 
				MpUsuarioContato.class)
				.setParameter("nome", nome.toLowerCase()).getSingleResult();
			//
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return usuarioContato;
	}
	
	@MpTransactional
	public MpUsuarioContato guardar(MpUsuarioContato mpUsuarioContato) {
		//
		try {
			mpUsuarioContato =  this.manager.merge(mpUsuarioContato);
			this.manager.flush();
			return mpUsuarioContato;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse USUÁRIO... já foi alterado anteriormente!");
		}
	}
	
	@MpTransactional
	public void remover(MpUsuarioContato mpUsuarioContato) throws MpNegocioException {
		//
		try {
			mpUsuarioContato = porId(mpUsuarioContato.getId());
			this.manager.remove(mpUsuarioContato);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("USUÁRIO... não pode ser excluído.");
		}
	}
	
	public List<MpUsuarioContato> listAll() {
		//
		return this.manager.createQuery("from MpUsuarioContato ORDER BY cpfCnpj", MpUsuarioContato.class).
																								getResultList();
	}
	
	
	public List<MpUsuarioContato> mpUsuarioContatoByCpfCnpjoList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpUsuarioContato where cpfCnpj = :cpfCnpj" + 
										" ORDER BY cpfCnpj", MpUsuarioContato.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
}