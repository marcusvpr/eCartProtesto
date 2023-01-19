package com.mpxds.mpbasic.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpBoletoLog;
import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.model.vo.MpDataContador;
import com.mpxds.mpbasic.security.MpSeguranca;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpUsuarios implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private MpSeguranca mpSeguranca;
	
	public MpUsuario porId(Long id) {
		//
		return this.manager.find(MpUsuario.class, id);
	}
	
	public List<MpUsuario> mpUsuarioByLoginList() {
		//
		// TODO filtrar apenas usuarios (por um grupo específico)
		return this.manager.createQuery("from MpUsuario ORDER BY login", MpUsuario.class).getResultList();
	}

	public MpUsuario porLoginEmail(String loginEmail) {
		//
		if (null == loginEmail || loginEmail.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		
//		MpAppUtil.PrintarLn("MpUsuarios.porLoginEmail() (loginEmail = " + loginEmail);

		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(login) = :loginEmail" + 
																  " or lower(email) = :loginEmail" ,
			MpUsuario.class).setParameter("loginEmail", loginEmail.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpUsuario;
	}
	
	public MpUsuario porLogin(String login) {
		//
		if (null == login || login.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		
		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(login) = :login",
																				MpUsuario.class)
						.setParameter("login", login.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porEmail(String email) {
		//
		if (null == email || email.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		//
		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(email) = :email",
																				MpUsuario.class)
						.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porNome(String nome) {
		//
		MpUsuario usuario = null;
		
		try {
			usuario = this.manager.createQuery("from MpUsuario where lower(nome) = :nome", MpUsuario.class)
				.setParameter("nome", nome.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return usuario;
	}
	
	@MpTransactional
	public MpUsuario guardar(MpUsuario mpUsuario) {
		//
		try {
			mpUsuario =  this.manager.merge(mpUsuario);
			this.manager.flush();
			return mpUsuario;
			//
		} catch (OptimisticLockException e) {
			throw new MpNegocioException("Erro de concorrência. Esse USUÁRIO... já foi alterado anteriormente!");
		}
	}
	
	
	@MpTransactional
	public void remover(MpUsuario mpUsuario) throws MpNegocioException {
		//
		try {
			mpUsuario = porId(mpUsuario.getId());
			this.manager.remove(mpUsuario);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("USUÁRIO... não pode ser excluído.");
		}
	}
	
	public List<MpUsuario> listAll() {
		//
		return this.manager.createQuery("from MpUsuario ORDER BY login", MpUsuario.class).getResultList();
	}
		
	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> valoresTotaisPorDataWeb(Integer numeroDeDias) {
		//
		Session session = manager.unwrap(Session.class);
				
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, Long> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(MpUsuario.class);
				
		if (this.mpSeguranca.getIndMySQL()) {
			//
			criteria.setProjection(Projections.projectionList()	
				.add(Projections.sqlGroupProjection(
					"date({alias}.dthr_inc) as data", 
					"{alias}.dthr_inc", 
					new String[] { "data" },
					new Type[] { StandardBasicTypes.DATE }))
					.add(Projections.count("indUserWeb").as("valor"))
				)
				.add(Restrictions.ge("mpAuditoriaObjeto.dtHrInc", dataInicial.getTime()))
			    .add(Restrictions.eq("indUserWeb", true));
		}
		else {
			criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection(
					"trunc({alias}.dthr_inc) as data" ,
					"trunc({alias}.dthr_inc)" ,
					new String[] { "data" } , 
					new Type[] { StandardBasicTypes.DATE } ))
					.add(Projections.count("indUserWeb").as("valor"))
				)
				.add(Restrictions.ge("mpAuditoriaObjeto.dtHrInc", dataInicial.getTime()))
		    	.add(Restrictions.eq("indUserWeb", true));
		}
		//
		List<MpDataContador> mpValoresPorData = new ArrayList<>();
		try {
			//
			mpValoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(MpDataContador.class)).list();
			//
		} catch(Exception e) {
			//
			System.out.println("MpUsuario.valoresTotaisPorDataWeb() ( e = " + e);
			mpValoresPorData = new ArrayList<>();
		}
		
		for (MpDataContador mpDataContador : mpValoresPorData) {
			//
//			System.out.println("MpUsuarios.valoresTotaisPorDataWeb ( " + mpDataContador.getData() + " / "
//																					+ mpDataContador.getValor());			
			resultado.put(mpDataContador.getData(), mpDataContador.getValor());
		}
		//
		return resultado;
	}
	
	private Map<Date, Long> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		//
		dataInicial = (Calendar) dataInicial.clone();
		
		Map<Date, Long> mapaInicial = new TreeMap<>();
		//
		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
		//
		return mapaInicial;
	}	
		
}