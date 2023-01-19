package com.mpxds.mpbasic.repository;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpTitulo;
//import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

public class MpTitulos implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	@Inject
	private MpBoletos mpBoletos;
	
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

	// ---
	
	public MpTitulo porId(Long id) {
		//
		return this.manager.find(MpTitulo.class, id);
	}
		
	public MpTitulo guardar(MpTitulo mpTitulo) {
		//
		try {
			return this.manager.merge(mpTitulo);
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse Titulo... já foi alterado anteriormente!");
		}
	}
		
	public List<MpTitulo> mpTituloByNumeroDataProtocoloList(String oficio, 
															String numeroProtocolo, 
															Date dataProtocolo) {
		//
		return this.manager.createQuery("from MpTitulo where codigoOficio = :oficio AND " +
											" numeroProtocolo = :numeroProtocolo AND " +
											" dataProtocolo = :dataProtocolo", MpTitulo.class)
				.setParameter("oficio", oficio)
				.setParameter("numeroProtocolo", numeroProtocolo)
				.setParameter("dataProtocolo", dataProtocolo)
				.getResultList();
	}

	public List<MpTitulo> mpTituloByOficioNumeroProtocoloList(String oficio, String numeroProtocolo) {
		//
		return this.manager.createQuery("from MpTitulo where codigoOficio = :oficio AND "
						+ " numeroProtocolo = :numeroProtocolo", MpTitulo.class)
				.setParameter("oficio", oficio)
				.setParameter("numeroProtocolo", numeroProtocolo)
				.getResultList();
	}
	
	public List<MpTitulo> mpTituloByOficioDataProtocoloList(String oficio, Date dataDe, Date dataAte) {
		//
		return this.manager.createQuery("from MpTitulo where codigoOficio = :oficio AND " +
										" dataProtocolo >= :dataDe AND dataProtocolo <= :dataAte" +
										"  ORDER BY dataProtocolo", MpTitulo.class)
				.setParameter("oficio", oficio)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.getResultList();
	}
	
	public List<MpTitulo> mpTituloNumeroDataProtocoloList(String oficio) {
		//
		return this.manager.createQuery("from MpTitulo where codigoOficio = :oficio ORDER BY codigoOficio, " +
										" numeroProtocolo, dataProtocolo, status", MpTitulo.class)
				.setParameter("oficio", oficio)
				.getResultList();
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
			String msg = e.toString();
			
			this.manager.getTransaction().rollback();
			
			return "ERROR MpTitulos.executeSQL() ( e = " + msg;
			//
        } finally {
        	//
            if (this.manager.getTransaction().isActive())
            	this.manager.getTransaction().commit();
        }	
	}

	public String executeSQLs(InputStream isX, Long numReg) {
		//
		Integer contRec = 0;
		Integer contMsg = 0;
		
		this.manager.getTransaction().begin();

		Scanner scX = new Scanner(isX);
		//
		while(scX.hasNextLine()) {
			//
			String sqlX = scX.nextLine();
			//
			try {
	        	//
	        	if (!sqlX.isEmpty()) {
	        		//
	        		if (sqlX.contains("insert into mp_titulo (codigo_oficio, numero_protocolo," + 
	        									" data_protocolo, status, complemento, classe_titulo) values (")) {
	        			//
	        			contRec++;
	        			contMsg++;
	        			
	        			if (contMsg > 499) {
	        				//
	        				contMsg = 0;
	        				
//	        				if (numReg > 0)
//	        					MpFacesUtil.addErrorMessage("Tratando carga de TITULO ( " + 
//	        																		contRec + " / " + numReg);
	        			}
	        			//
	        			this.manager.createNativeQuery(sqlX).executeUpdate();
	        			//
	        		} else {
	        			//
	    	        	this.manager.getTransaction().rollback();
	    	        	scX.close();
	    	        	//
	    	        	return "ERROR - Formato INSERT ( sql = " + sqlX;
	        		}
	        	}
	            //
	        } catch (Exception e) {
	        	// 
	        	this.manager.getTransaction().rollback();
	        	scX.close();
	        	//
	        	return "ERROR ( e = " + e.getMessage() + " / sql = " + sqlX;
	        }
	    }
		//
        if (this.manager.getTransaction().isActive())
        	this.manager.getTransaction().commit();
        //
        scX.close();
        //
        return "OK (" + contRec;
	}

	public String executeSQLsIncr(InputStream isX, Long numReg) {
		//
		Integer contRec = 0;
		Integer contMsg = 0;
		
		this.manager.getTransaction().begin();

		Scanner scX = new Scanner(isX);
		//
		while(scX.hasNextLine()) {
			//
			String sqlX = scX.nextLine();
			//
			try {
	        	//
	        	if (!sqlX.isEmpty()) {
	        		//
	        		if (sqlX.contains("insert into mp_titulo (codigo_oficio, numero_protocolo," + 
	        									" data_protocolo, status, complemento, classe_titulo) values (")) {
	        			// '1','037370',DATE '2018-07-16'
	        			String oficioX = sqlX.substring(117, 118);
	        			String numeroProtocoloX = sqlX.substring(121, 127);
	        			Date dataProtocoloX = sdfYMD.parse(sqlX.substring(135, 145));
	        			
	        			List<MpTitulo> mpTituloList = this.mpTituloByNumeroDataProtocoloList(oficioX, numeroProtocoloX, 
	        																						     dataProtocoloX);
//	        			System.out.println("MpTitulos.executeSQLsIncr() - 001 ( mpBoletoList.size = " + 
//	        					mpTituloList.size() + " / oficio = " + oficioX + " / num.protoc = " + numeroProtocoloX +
//	        					" / dt.protoc = " + dataProtocoloX);
	        			for (MpTitulo mpTituloX : mpTituloList) {
	        				//
	        				this.manager.remove(mpTituloX);
	        			}	        			
	        			//
	        			contRec++;
	        			contMsg++;
	        			
	        			if (contMsg > 499) {
	        				//
	        				contMsg = 0;
	        				
//	        				if (numReg > 0)
//	        					MpFacesUtil.addErrorMessage("Tratando carga de TITULO ( " + 
//	        																		contRec + " / " + numReg);
	        			}
	        			//
	        			this.manager.createNativeQuery(sqlX).executeUpdate();

	        			// MVPR_20180904 ... Trata Boleto (Liquidado!)
	        			String statusTitulo = sqlX.substring(148, 157);
//	        			System.out.println("MpTitulos.executeSQLsIncr() - 000 ( statusTitulo = " + statusTitulo);
//	        			if (statusTitulo.toUpperCase().equals("LIQUIDADO")) { 
	        			
	        			// MVPR_2020-01-23 ... Trata Boleto (Em Andamento/Aberto) - Prisco/Heraldo !
	        			// MVPR_2020-01-30 ... Trata Boleto (EXCLUIDO DEPOSITO EMITIDO) - Prisco/Heraldo !
//	        			if ( (!statusTitulo.toUpperCase().equals("EM ADAMENTO"))
//		        		&&   (!statusTitulo.toUpperCase().equals("EXCLUIDO DEPOSITO EMITIDO")) ) {
		        		if ( (sqlX.toUpperCase().indexOf("'EM ADAMENTO'") > 0)
		    		    ||   (sqlX.toUpperCase().indexOf("'EXCLUIDO DEPOSITO EMITIDO'") > 0) )
		        			assert(true); // nop
		        		else {
	        				// 
	        				List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByNumeroProtocoloList(oficioX, 
	        																					  numeroProtocoloX);
//		        			System.out.println("MpTitulos.executeSQLsIncr() - 001 ( mpBoletoList.size = " + 
//		        				mpBoletoList.size() + " / oficio = " + oficioX + " / n.protoc = " + numeroProtocoloX);
		        			//
	        				for (MpBoleto mpBoletoX : mpBoletoList) {
	        					// Seta indLiquidado = true;
//	    	        			if (statusTitulo.toUpperCase().equals("LIQUIDADO"))
	    		        		if (sqlX.toUpperCase().indexOf("'LIQUIDADO'") > 0)
	    	        				mpBoletoX.setIndLiquidado(true);
	    	        			else
	    	        				mpBoletoX.setIndAberto(true);
	        					
	        					mpBoletos.guardar(mpBoletoX);
	        				}
	        			}
	        			// MVPR_2020-03-03 ... Trata DESBLOQUEIO Boleto (EXCLUIDO DEPOSITO EMITIDO) - Prisco/Heraldo !
//		        		if (statusTitulo.toUpperCase().equals("EXCLUIDO DEPOSITO EMITIDO")) {
		    		    if (sqlX.toUpperCase().indexOf("'EXCLUIDO DEPOSITO EMITIDO'") > 0) {
		        			//
	        				List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByNumeroProtocoloList(oficioX, 
									  																numeroProtocoloX);
							//
							for (MpBoleto mpBoletoX : mpBoletoList) {
								//
								mpBoletoX.setIndAberto(false);
								
								mpBoletos.guardar(mpBoletoX);
							}
		        		}
	        			//
	        		} else {
	        			//
	    	        	this.manager.getTransaction().rollback();
	    	        	scX.close();
	    	        	//
	    	        	return "ERROR - Formato INSERT ( Sql = " + sqlX + " / Linha = " + contRec;
	        		}
	        	}
	            //
	        } catch (Exception e) {
	        	// 
	        	this.manager.getTransaction().rollback();
	        	scX.close();
	        	//
	        	return "ERROR ( e = " + e.getMessage() + " / Sql = " + sqlX + " / Linha = " + contRec;
	        }
	    }
		//
        if (this.manager.getTransaction().isActive())
        	this.manager.getTransaction().commit();
        //
        scX.close();
        //
        return "OK (" + contRec;
	}
	
	public List<MpTitulo> listAll() {
		//
		return this.manager.createQuery("from MpTitulo ORDER BY codigoOficio, numeroProtocolo", 
				MpTitulo.class).getResultList();
	}
	
}