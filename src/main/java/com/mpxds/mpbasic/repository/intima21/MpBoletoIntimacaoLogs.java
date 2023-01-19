package com.mpxds.mpbasic.repository.intima21;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoLog;
import com.mpxds.mpbasic.model.vo.MpDataContador;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoIntimacaoLogs implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
		
	@Inject
	private MpSeguranca mpSeguranca;

	public MpBoletoIntimacaoLog porId(Long id) {
		//
		return this.manager.find(MpBoletoIntimacaoLog.class, id);
	}
	
	public List<MpBoletoIntimacaoLog> mpBoletoIntimacaoLogByDataNumOficioList(Date dataDe, String numOfic) {
		//
		return this.manager.createQuery(
				"from MpBoletoIntimacaoLog WHERE dataGeracao >= :dataDe AND numeroOficio = :numOfic ORDER " + 
																		" BY dataGeracao", MpBoletoIntimacaoLog.class)
				.setParameter("dataDe", dataDe)
				.setParameter("numOfic", numOfic).getResultList();
	}

	public List<MpBoletoIntimacaoLog> mpBoletoIntimacaoLogBySelDataNumOficioList(Date dataDe, Date dataAte, 
																										String numOfic) {
		//
		return this.manager.createQuery(
				"from MpBoletoIntimacaoLog WHERE dataGeracao >= :dataDe AND dataGeracao <= :dataAte" +
									" AND numeroOficio = :numOfic ORDER BY dataGeracao", MpBoletoIntimacaoLog.class)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.setParameter("numOfic", numOfic).getResultList();
	}
		
	@MpTransactional
	public MpBoletoIntimacaoLog guardar(MpBoletoIntimacaoLog mpBoletoIntimacaoLog) {
		//
		try {
			return this.manager.merge(mpBoletoIntimacaoLog);
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse BoletoIntimacao Log... " + 
																				" já foi alterado anteriormente!");
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> valoresTotaisPorData(Integer numeroDeDias, String numeroOficio, Boolean indCancelamento) {
		//
		Session session = manager.unwrap(Session.class);
				
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, Long> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(MpBoletoIntimacaoLog.class);
				
		if (this.mpSeguranca.getIndMySQL()) {
			//
			criteria.setProjection(Projections.projectionList()	
				.add(Projections.sqlGroupProjection(
					"date({alias}.data_Geracao) as data", 
					"{alias}.data_Geracao", 
					new String[] { "data" },
					new Type[] { StandardBasicTypes.DATE }))
					.add(Projections.count("numeroOficio").as("valor"))
				)
				.add(Restrictions.ge("dataGeracao", dataInicial.getTime()))
				.add(Restrictions.eq("indCancelamento", indCancelamento))
				.add(Restrictions.eq("numeroOficio", numeroOficio));
		}
		else {
			criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection(
					"trunc({alias}.data_Geracao) as data" ,
					"trunc({alias}.data_Geracao)" ,
					new String[] { "data" } , 
					new Type[] { StandardBasicTypes.DATE } ))
					.add(Projections.count("numeroOficio").as("valor"))
				)
				.add(Restrictions.ge("dataGeracao", dataInicial.getTime()))
				.add(Restrictions.eq("indCancelamento", indCancelamento))
				.add(Restrictions.eq("numeroOficio", numeroOficio));
		}
		//
		List<MpDataContador> mpValoresPorData = criteria
				.setResultTransformer(Transformers.aliasToBean(MpDataContador.class)).list();
		
//		System.out.println("MpBoletoIntimacaoLogs.valoresTotaisPorData()...  ( " + numeroDeDias + " / " + 
//															numeroOficio + 	" / Size = " + mpValoresPorData.size());
		//
		for (MpDataContador mpDataContador : mpValoresPorData) {
			//
			resultado.put(mpDataContador.getData(), mpDataContador.getValor());
		}
		//
		return resultado;
	}
	
	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> valoresTotaisPorDataWeb(Integer numeroDeDias, String numeroOficio) {
		//
		Session session = manager.unwrap(Session.class);
				
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, Long> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(MpBoletoIntimacaoLog.class);
				
		if (this.mpSeguranca.getIndMySQL()) {
			//
			criteria.setProjection(Projections.projectionList()	
				.add(Projections.sqlGroupProjection(
					"date({alias}.data_Geracao) as data", 
					"{alias}.data_Geracao", 
					new String[] { "data" },
					new Type[] { StandardBasicTypes.DATE }))
					.add(Projections.count("numeroOficio").as("valor"))
				)
		    	.add(Restrictions.eq("indUserWeb", true))
				.add(Restrictions.ge("dataGeracao", dataInicial.getTime()))
				.add(Restrictions.eq("numeroOficio", numeroOficio));
		}
		else {
			criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection(
					"trunc({alias}.data_Geracao) as data" ,
					"trunc({alias}.data_Geracao)" ,
					new String[] { "data" } , 
					new Type[] { StandardBasicTypes.DATE } ))
					.add(Projections.count("numeroOficio").as("valor"))
				)
	    		.add(Restrictions.eq("indUserWeb", true))
	    		.add(Restrictions.ge("dataGeracao", dataInicial.getTime()))
	    		.add(Restrictions.eq("numeroOficio", numeroOficio));
		}
		//
		List<MpDataContador> mpValoresPorData = criteria
				.setResultTransformer(Transformers.aliasToBean(MpDataContador.class)).list();
		
//		System.out.println("MpBoletoIntimacaoLogs.valoresTotaisPorDataWeb()... ( " + numeroDeDias + " / " +
//															numeroOficio + " / Size = " + mpValoresPorData.size());
		//
		for (MpDataContador mpDataContador : mpValoresPorData) {
			//
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
		
	public List<MpBoletoIntimacaoLog> listAll() {
		//
		return this.manager.createQuery("from MpBoletoIntimacaoLog ORDER BY dataGeracao", MpBoletoIntimacaoLog.class).
																									getResultList();
	}
	
}