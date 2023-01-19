package com.mpxds.mpbasic.repository;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletos implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpBoleto porId(Long id) {
		//
		return this.manager.find(MpBoleto.class, id);
	}
	
	public List<MpBoleto> mpBoletoByDataDocumentoList() {
		//
		return this.manager.createQuery("from MpBoleto ORDER BY dataDocumento", MpBoleto.class).getResultList();
	}
	
	public List<MpBoleto> mpBoletoByImpressaoList() {
		//
		return this.manager.createQuery("from MpBoleto where indImpressao = true ORDER BY dataDocumento", 
																		MpBoleto.class).getResultList();
	}
	
	public List<MpBoleto> mpBoletoByOficioDataImpressaoList(String oficio, Date dataDe, Date dataAte) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND" + 
										" dataImpressao >= :dataDe AND dataImpressao <= :dataAte" +
										" ORDER BY dataImpressao", MpBoleto.class)
				.setParameter("oficio", oficio)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.getResultList();
	}
		
	public List<MpBoleto> mpBoletoByDataImpressaoList(Date dataDe, Date dataAte) {
		//
		return this.manager.createQuery("from MpBoleto where dataImpressao >= :dataDe AND dataImpressao <= :dataAte" +
										" ORDER BY dataImpressao", MpBoleto.class)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByOficioCpfCnpjList(String oficio, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio" + 
										" AND cpfCnpj = :cpfCnpj ORDER BY dataImpressao", MpBoleto.class)
				.setParameter("oficio", oficio)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByOficioDataDocumentoList(String oficio, Date dataDe, Date dataAte) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND" + 
										" dataDocumento >= :dataDe AND dataDocumento <= :dataAte" +
										" ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByOficioNumeroIntimacaoCpfCnpjList(String oficio, String numIntim, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND numeroIntimacao = :numIntim" +
										" AND cpfCnpj = :cpfCnpj ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio)
				.setParameter("numIntim", numIntim)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}

	public List<MpBoleto> mpBoletoByNumeroIntimacaoCpfCnpjList(String numIntim, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where numeroIntimacao = :numIntim" +
										" AND cpfCnpj = :cpfCnpj ORDER BY codigoOficio, dataDocumento", MpBoleto.class)
				.setParameter("numIntim", numIntim)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByCpfCnpjList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where cpfCnpj = :cpfCnpj " + 
										" ORDER BY dataDocumento, codigoOficio", MpBoleto.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByImpressaoCpfCnpjList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where cpfCnpj = :cpfCnpj " + 
										" AND indImpressao = true ORDER BY dataDocumento, codigoOficio", MpBoleto.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByImpressaoOficioCpfCnpjList(String oficio, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND cpfCnpj = :cpfCnpj " + 
										" AND indImpressao = true ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByImpressaoOficioList(String oficio) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio" + 
										" AND indImpressao = true ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio)
				.getResultList();
	}
		
	public List<MpBoleto> mpBoletoByNumeroProtocoloList(String oficio, String numeroProtocolo) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio " +
										"AND numeroIntimacao = :numeroProtocolo " +
										"ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio.trim())
				.setParameter("numeroProtocolo", numeroProtocolo.trim())
				.getResultList();
	}
	
	public List<MpBoleto> mpBoletoByNumeroGuiaGeradoList(String oficio, Integer numeroGuiaGerado) {
		//
		return this.manager.createQuery("from MpBoleto where codigoOficio = :oficio " +
										"AND numeroGuiaGerado = :numeroGuiaGerado " +
										"ORDER BY dataDocumento", MpBoleto.class)
				.setParameter("oficio", oficio.trim())
				.setParameter("numeroGuiaGerado", numeroGuiaGerado)
				.getResultList();
	}
	
	@MpTransactional
	public MpBoleto guardar(MpBoleto mpBoleto) {
		//
		try {
			mpBoleto = this.manager.merge(mpBoleto);
			
			this.manager.flush();
			//
			return mpBoleto;
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse Boleto... já foi alterado anteriormente!");
		}
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

	public String executeSQLs(InputStream isX, Long numReg, String codOficio) {
		//
		Integer contRec = 0;
//		Integer contMsg = 0;
		
		Integer numeroGuiaGerado = 0;
		Integer numeroGuiaGeradoAnt = 0;
		//
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
	        		if (sqlX.contains("insert into mp_boleto (codigo_oficio, numero_intimacao, " + 
	        				"numero_intimacao_code, cpf_cnpj, nome_sacado, local_bairro, local_cep, local_cidade, " + 
	        				"local_logradouro, local_numero, numero_documento, especie_documento, nosso_numero, nosso_numero_dig, " + 
	        				"valor_documento, valor_acrescimo, valor_cobrado, valor_tarifa, valor_cpmf, valor_leis, " + 
	        				"data_documento, data_vencimento, boleto_instrucao8, carteira, codigo_barras, " + 
	        				"agencia_codigo_cedente, linha_digitavel, data_vencimento_1, codigo_barras_1, " + 
	        				"agencia_codigo_cedente_1, linha_digitavel_1, valor_iss, data_distribuicao) values (")) {
	        			//
	        			// Critica Numero Oficio:
	        			// --------
	        			Integer posNumeroOficio = sqlX.indexOf("values ('");
	        			if (posNumeroOficio >= 0) {
	        				//
		        			String numeroOficio = sqlX.substring(posNumeroOficio + 9, posNumeroOficio + 10);

		        			if (!numeroOficio.equals(codOficio)) {
	    	    	        	this.manager.getTransaction().rollback();
	    	    	        	scX.close();
	    	    	        	//
			    	        	return "ERROR - Formato INSERT ( Numero.OFICIO... inválido  !!! ( Num.Oficio = " +
	    	    	        					numeroOficio + " / Cod.Oficio = " + codOficio + " / sql = " + sqlX +
	    	    	        					" / Linha = " + contRec;
		        			}
		        			//
	        			} else {
    	    	        	this.manager.getTransaction().rollback();
    	    	        	scX.close();
    	    	        	//
		    	        	return "ERROR - Formato INSERT ( values ('... Não Existe !!! ( sql = " + sqlX + " / Linha = " + contRec;
	        			}
	        			// Critica Numero GUIA:
	        			// --------
	        			Integer posNumeroGuia = sqlX.indexOf(",'GUIA N");
	        			if (posNumeroGuia >= 0) {
	        				//
		    				String[] wordsNumeroGuia = sqlX.substring(posNumeroGuia + 2, posNumeroGuia + 2 + 16).split(" ");
		        			
		    				try {
		    					numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);

			    				if (numeroGuiaGeradoAnt == 0)
			    					numeroGuiaGeradoAnt = numeroGuiaGerado;
			    				else
			    					if (numeroGuiaGeradoAnt.equals(numeroGuiaGerado))
			    						assert(true); // nop
			    					else {
			    	    	        	this.manager.getTransaction().rollback();
			    	    	        	scX.close();
			    	    	        	//
					    	        	return "ERROR - Formato INSERT ( Número Guia Inválido !!! ( Guia Anterior = " + 
					    	        						numeroGuiaGeradoAnt + " / Guia Atual = " + numeroGuiaGerado + 
					    	        						" / sql = " + sqlX + " / Linha = " + contRec;
			    					}
			    				//
		    				} catch (Exception e) {
		    					//
	    	    	        	this.manager.getTransaction().rollback();
	    	    	        	scX.close();
	    	    	        	//
			    	        	return "ERROR - Formato INSERT ( Número Guia Inválida !!! ( Guia = " + 
		    							sqlX.substring(posNumeroGuia + 2, posNumeroGuia + 2 + 16) + " / sql = " + sqlX + " / Linha = " + contRec;
		    				}
		        			//
	        			} else {
    	    	        	this.manager.getTransaction().rollback();
    	    	        	scX.close();
    	    	        	//
		    	        	return "ERROR - Formato INSERT ( Número Guia Não Existe !!! ( sql = " + sqlX + " / Linha = " + contRec;
	        			}
	        			//
	        			contRec++;
//	        			contMsg++;
	        			
//	        			if (contMsg > 499) {
//	        				//
//	        				contMsg = 0;
//	        				
//	        				if (numReg > 0)
//	        					MpFacesUtil.addErrorMessage("Tratando carga de BOLETO ( " + 
//	        																		contRec + " / " + numReg);
//	        			}
	        			//
	        			this.manager.createNativeQuery(sqlX).executeUpdate();
	        			//
	        		} else {
	        			//
	    	        	this.manager.getTransaction().rollback();
	    	        	scX.close();
	    	        	//
	    	        	return "ERROR - Formato INSERT ( sql = " + sqlX + " / Linha = " + contRec;
	        		}
	        	}
	            //
	        } catch (Exception e) {
	        	//
//	        	e.printStackTrace();
	        	this.manager.getTransaction().rollback();
	        	scX.close();
	        	//
	        	return "ERROR-Repository ( e = " + e.getMessage() + " / sql = " + sqlX + " / Linha = " + contRec;
	        }
	    }
		//
        if (this.manager.getTransaction().isActive())
        	this.manager.getTransaction().commit();
        //
        scX.close();
        //
        return "OK (" + contRec  ;
	}
	
	public List<MpBoleto> listAll() {
		//
		return this.manager.createQuery("from MpBoleto ORDER BY codigoOficio, numeroIntimacao", 
				MpBoleto.class).getResultList();
	}
	
	//
	
	public MpBoleto mpBoletoByOficioCodeProtocoloDocumento(String oficio, String code, String protocolo,
																						String documento) {
		//
		MpBoleto mpBoleto = null;
		
		try {
			mpBoleto = this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND" + 
					" numero_intimacao_code = :code AND numero_intimacao = :protocolo AND" +
					" cpf_cnpj = :documento", MpBoleto.class)
					.setParameter("oficio", oficio)
					.setParameter("code", code)
					.setParameter("protocolo", protocolo)
					.setParameter("documento", documento).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpBoleto;
	}
	
	public MpBoleto mpBoletoByOficioCodeProtocoloDataDistribuicao(String oficio, String code, String protocolo,
																						Date dataDistribuicao) {
		//
		MpBoleto mpBoleto = null;
		
		try {
			mpBoleto = this.manager.createQuery("from MpBoleto where codigoOficio = :oficio AND" + 
					" numero_intimacao_code = :code AND numero_intimacao = :protocolo AND" +
					" data_distribuicao = :dataDistribuicao", MpBoleto.class)
					.setParameter("oficio", oficio)
					.setParameter("code", code)
					.setParameter("protocolo", protocolo)
					.setParameter("dataDistribuicao", dataDistribuicao)
					.getSingleResult();
		} catch (NoResultException e) {
			// nenhum registro encontrado...
		}
		//
		return mpBoleto;
	}

	public MpBoleto mpBoletoByOficioProtocoloDataDistribuicao(String oficio, String protocolo,
																	Date dataDistribuicao) throws Exception {
		//
		MpBoleto mpBoleto = null;

		try {
			mpBoleto = this.manager
					.createQuery("from MpBoleto where codigoOficio = :oficio AND"
							+ " numeroIntimacao = :protocolo AND"
							+ " dataDistribuicao = :dataDistribuicao",
							MpBoleto.class)
					.setParameter("oficio", oficio)
					.setParameter("protocolo", protocolo)
					.setParameter("dataDistribuicao", dataDistribuicao).getSingleResult();
		} catch (NoResultException e) {
			// nenhum registro encontrado...
			// System.out.println("MpBoletos.mpBoletoByOficioProtocoloDataDistribuicao() ... ( Exception: " + e);
		} catch (NonUniqueResultException e1) {
			//
			SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
			
			throw new NonUniqueResultException("NonUniqueException: ( " + oficio + " / " + 
																			protocolo + " / " + sdf.format(dataDistribuicao));
		}
		//
		// System.out.println("MpBoletos.mpBoletoByOficioProtocoloDataDistribuicao() ... ( Retorno =  " + mpBoleto);
		//
		return mpBoleto;
	}	
	
}