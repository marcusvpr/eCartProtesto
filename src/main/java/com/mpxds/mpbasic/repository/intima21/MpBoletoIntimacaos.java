package com.mpxds.mpbasic.repository.intima21;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacao;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoIntimacaos implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public MpBoletoIntimacao porId(Long id) {
		//
		return this.manager.find(MpBoletoIntimacao.class, id);
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByDataDocumentoList() {
		//
		return this.manager.createQuery("from MpBoletoIntimacao ORDER BY dataDocumento", MpBoletoIntimacao.class)
																									.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByImpressaoList() {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where indImpressao = true ORDER BY dataDocumento", 
																		MpBoletoIntimacao.class).getResultList();
	}
	
			
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByOficioDataProtocoloList(String oficio, Date dataSel) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND" + 
										" dataProtocolo = :dataSel" +
										" ORDER BY numeroIntimacao", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.setParameter("dataSel", dataSel)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByOficioDataProtocoloList(String oficio, Date dataDe, 
																									Date dataAte) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND" + 
										" dataProtocolo >= :dataDe AND dataProtocolo <= :dataAte" +
										" ORDER BY dataProtocolo, numeroIntimacao", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.setParameter("dataDe", dataDe)
				.setParameter("dataAte", dataAte)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByOficioNumeroIntimacaoCpfCnpjList(String oficio, String numIntim,
																									String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND " + 
										" numeroIntimacao = :numIntim" +
										" AND cpfCnpj = :cpfCnpj ORDER BY dataDocumento", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.setParameter("numIntim", numIntim)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}

	public MpBoletoIntimacao mpBoletoIntimacaoByOficioNumeroProtocolo(String oficio, String numIntim) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND " + 
										" numeroIntimacao = :numIntim" +
										" ORDER BY codigoOficio, numeroIntimacao", 
											MpBoletoIntimacao.class)
										.setParameter("oficio", oficio)
										.setParameter("numIntim", numIntim)
										.getSingleResult();
	}

	public List<MpBoletoIntimacao> mpBoletoIntimacaoByOficioNomeSacado(String oficio, String nomeSacado) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND " + 
										" nomeSacado = :nomeSacado" +
										" ORDER BY nomeSacado", 
											MpBoletoIntimacao.class)
										.setParameter("oficio", oficio)
										.setParameter("nomeSacado", nomeSacado)
										.getResultList();
	}

	public List<MpBoletoIntimacao> mpBoletoIntimacaoByNumeroIntimacaoCpfCnpjList(String numIntim, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where numeroIntimacao = :numIntim" +
										" AND cpfCnpj = :cpfCnpj ORDER BY codigoOficio, dataDocumento", 
										MpBoletoIntimacao.class)
				.setParameter("numIntim", numIntim)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByCpfCnpjList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where cpfCnpj = :cpfCnpj " + 
										" ORDER BY dataDocumento, codigoOficio", MpBoletoIntimacao.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByImpressaoCpfCnpjList(String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where cpfCnpj = :cpfCnpj " + 
										" AND indImpressao = true ORDER BY dataDocumento, codigoOficio", 
										MpBoletoIntimacao.class)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByImpressaoOficioCpfCnpjList(String oficio, String cpfCnpj) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND cpfCnpj = :cpfCnpj " + 
										" AND indImpressao = true ORDER BY dataDocumento", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.setParameter("cpfCnpj", cpfCnpj)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByOficioList(String oficio) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio" + 
										" ORDER BY id", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByImpressaoOficioList(String oficio) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio" + 
										" AND indImpressao = true ORDER BY dataDocumento", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio)
				.getResultList();
	}
		
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByNumeroProtocoloList(String oficio, String numeroProtocolo) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio " +
										"AND numeroIntimacao = :numeroProtocolo " +
										"ORDER BY dataDocumento", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio.trim())
				.setParameter("numeroProtocolo", numeroProtocolo.trim())
				.getResultList();
	}
	
	public List<MpBoletoIntimacao> mpBoletoIntimacaoByNumeroGuiaGeradoList(String oficio, Integer numeroGuiaGerado) {
		//
		return this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio " +
										"AND numeroGuiaGerado = :numeroGuiaGerado " +
										"ORDER BY dataDocumento", MpBoletoIntimacao.class)
				.setParameter("oficio", oficio.trim())
				.setParameter("numeroGuiaGerado", numeroGuiaGerado)
				.getResultList();
	}
	
	@MpTransactional
	public MpBoletoIntimacao guardar(MpBoletoIntimacao mpBoletoIntimacao) {
		//
		try {
			mpBoletoIntimacao = this.manager.merge(mpBoletoIntimacao);
			
			this.manager.flush();
			//
			return mpBoletoIntimacao;
			//
		} catch (OptimisticLockException e) {
			//
			throw new MpNegocioException("Erro de concorrência. Esse Boleto Intimacao! Já foi alterado anteriormente!");
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
	        		if (sqlX.contains("insert into mp_boleto_intimacao (codigo_oficio, numero_intimacao, " + 
				"numero_intimacao_code, cpf_cnpj, nome_sacado, local_bairro, local_cep, local_cidade, " + 
				"local_logradouro, local_numero, numero_documento, especie_documento, nosso_numero, nosso_numero_dig, " + 
				"valor_documento, valor_acrescimo, valor_cobrado, valor_tarifa, valor_cpmf, valor_leis, " + 
				"data_documento, data_vencimento, boleto_instrucao8, carteira, codigo_barras, " + 
				"agencia_codigo_cedente, linha_digitavel, data_vencimento_1, codigo_barras_1, " + 
				"agencia_codigo_cedente_1, linha_digitavel_1, valor_iss, data_distribuicao, " + 
				"numero_titulo, fins_falimentares, endosso, data_emissao, data_vencimento_titulo, motivo_protesto, " + 
				"nome_apresentante, nome_cedente, nome_sacador, custas, valor_saldo) values (")) {
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
		    	        	return "ERROR - Formato INSERT ( values ('... Não Existe !!! ( sql = " + sqlX + 
		    	        																	" / Linha = " + contRec;
	        			}
	        			// Critica Numero GUIA:
	        			// --------
	        			Integer posNumeroGuia = sqlX.indexOf(",'GUIA N");
	        			if (posNumeroGuia >= 0) {
	        				//
		    				String[] wordsNumeroGuia = sqlX.substring(posNumeroGuia + 2, posNumeroGuia + 2 + 16).
		    																								split(" ");
		        			
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
		    							sqlX.substring(posNumeroGuia + 2, posNumeroGuia + 2 + 16) + " / sql = " + 
		    							sqlX + " / Linha = " + contRec;
		    				}
		        			//
	        			} else {
    	    	        	this.manager.getTransaction().rollback();
    	    	        	scX.close();
    	    	        	//
		    	        	return "ERROR - Formato INSERT ( Número Guia Não Existe !!! ( sql = " + sqlX + 
		    	        			" / Linha = " + contRec;
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
	
	public List<MpBoletoIntimacao> listAll() {
		//
		return this.manager.createQuery("from MpBoletoIntimacao ORDER BY codigoOficio, numeroIntimacao", 
				MpBoletoIntimacao.class).getResultList();
	}
	
	//
	
	public MpBoletoIntimacao mpBoletoIntimacaoByOficioCodeProtocoloDocumento(String oficio, String code, 
																				String protocolo, String documento) {
		//
		MpBoletoIntimacao mpBoletoIntimacao = null;
		
		try {
			mpBoletoIntimacao = this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND" + 
					" numero_intimacao_code = :code AND numero_intimacao = :protocolo AND" +
					" cpf_cnpj = :documento", MpBoletoIntimacao.class)
					.setParameter("oficio", oficio)
					.setParameter("code", code)
					.setParameter("protocolo", protocolo)
					.setParameter("documento", documento).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpBoletoIntimacao;
	}
	
	public MpBoletoIntimacao mpBoletoIntimacaoByOficioCodeProtocoloDataDistribuicao(String oficio, String code, 
																			String protocolo, Date dataDistribuicao) {
		//
		MpBoletoIntimacao mpBoletoIntimacao = null;
		
		try {
			mpBoletoIntimacao = this.manager.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND" + 
					" numero_intimacao_code = :code AND numero_intimacao = :protocolo AND" +
					" data_distribuicao = :dataDistribuicao", MpBoletoIntimacao.class)
					.setParameter("oficio", oficio)
					.setParameter("code", code)
					.setParameter("protocolo", protocolo)
					.setParameter("dataDistribuicao", dataDistribuicao)
					.getSingleResult();
		} catch (NoResultException e) {
			// nenhum registro encontrado...
		}
		//
		return mpBoletoIntimacao;
	}

	public MpBoletoIntimacao mpBoletoIntimacaoByOficioProtocoloDataDistribuicao(String oficio, String protocolo,
																	Date dataDistribuicao) {
		//
		MpBoletoIntimacao mpBoletoIntimacao = null;

		try {
			mpBoletoIntimacao = this.manager
					.createQuery("from MpBoletoIntimacao where codigoOficio = :oficio AND"
							+ " numeroIntimacao = :protocolo AND"
							+ " dataDistribuicao = :dataDistribuicao",
							MpBoletoIntimacao.class)
					.setParameter("oficio", oficio)
					.setParameter("protocolo", protocolo)
					.setParameter("dataDistribuicao", dataDistribuicao).getSingleResult();
		} catch (NoResultException e) {
			// nenhum registro encontrado...
			// System.out.println("MpBoletoIntimacaos.mpBoletoIntimacaoByOficioProtocoloDataDistribuicao() ..." + 
			// 						" ( Exception: " + e);
		}
		//
		// System.out.println("MpBoletoIntimacaos.mpBoletoIntimacaoByOficioProtocoloDataDistribuicao() ... ( Ret=  " + 
		// 						mpBoletoIntimacao);
		//
		return mpBoletoIntimacao;
	}
	
	@MpTransactional
	public void remover(MpBoletoIntimacao mpBoletoIntimacao) throws MpNegocioException {
		//
		try {
			mpBoletoIntimacao = porId(mpBoletoIntimacao.getId());
			this.manager.remove(mpBoletoIntimacao);
			this.manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("MpBoletoIntimacao... não pode ser excluído.");
		}
	}

}