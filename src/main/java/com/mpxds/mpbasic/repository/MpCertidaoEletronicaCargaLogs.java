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
import com.mpxds.mpbasic.model.MpCertidaoEletronicaCargaLog;
import com.mpxds.mpbasic.model.vo.MpDataContador;

public class MpCertidaoEletronicaCargaLogs implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpCertidaoEletronicaCargaLog porId(Long id) {
		//
		return this.manager.find(MpCertidaoEletronicaCargaLog.class, id);
	}
	
	public List<MpCertidaoEletronicaCargaLog> mpCertidaoEletronicaCargaLogByDataNumOficioList(Date dataDe, String numOfic) {
		//
		return this.manager.createQuery(
				"from MpCertidaoEletronicaCargaLog WHERE dataGeracao >= :dataDe AND numeroOficio = :numOfic " + 
															"ORDER BY dataGeracao",	MpCertidaoEletronicaCargaLog.class)
				.setParameter("dataDe", dataDe)
				.setParameter("numOfic", numOfic).getResultList();
	}

	public List<MpCertidaoEletronicaCargaLog> mpCertidaoEletronicaCargaLogBySelDataNumOficioList(Date dataDe, Date dataAte,
																									String numOfic) {
		//
		return this.manager.createQuery(
				"from MpCertidaoEletronicaCargaLog WHERE dataGeracao >= :dataDe AND dataGeracao <= :dataAte" +
							" AND numeroOficio = :numOfic ORDER BY dataGeracao", MpCertidaoEletronicaCargaLog.class)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.setParameter("numOfic", numOfic).getResultList();
	}
		
	public MpCertidaoEletronicaCargaLog guardar(MpCertidaoEletronicaCargaLog mpCertidaoEletronicaCargaLog) {
		//
		try {
			return this.manager.merge(mpCertidaoEletronicaCargaLog);
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse CertidaoEletronica CargaLog... " + 
																			"já foi alterado anteriormente!");
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> valoresTotaisPorData(Integer numeroDeDias, String numeroOficio) {
		//
		Session session = manager.unwrap(Session.class);
				
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, Long> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(MpCertidaoEletronicaCargaLog.class);
				
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection(
						"trunc({alias}.data_Geracao) as data" ,
						"trunc({alias}.data_Geracao)" ,
						new String[] { "data" } , 
						new Type[] { StandardBasicTypes.DATE } ))
				.add(Projections.count("numeroOficio").as("valor"))
		)
		.add(Restrictions.ge("dataGeracao", dataInicial.getTime()))
		.add(Restrictions.eq("numeroOficio", numeroOficio));

		//
		List<MpDataContador> mpValoresPorData = new ArrayList<>();
		try {
			//
			mpValoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(MpDataContador.class)).list();
			//
		} catch(Exception e) {
			//
			System.out.println("MpCertidaoEletronicaCargaLogs.valoresTotaisPorData() ( e = " + e);
			mpValoresPorData = new ArrayList<>();
		}
		
		for (MpDataContador mpDataContador : mpValoresPorData) {
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