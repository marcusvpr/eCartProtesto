package com.mpxds.mpbasic.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpbasic.exception.MpNegocioException;
import com.mpxds.mpbasic.model.MpAuditoriaObjeto;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jpa.MpTransactional;

public class MpBoletoService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpSeguranca mpSeguranca;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	@Inject
	private MpSistemaConfigService mpSistemaConfigService;

	// 
	
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	// -------------
	
	@MpTransactional
	public MpBoleto salvar(MpBoleto mpBoleto) throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto = new MpAuditoriaObjeto();
		
		if (null == mpBoleto.getId()) { 
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpBoleto.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpBoleto.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpBoletos.guardar(mpBoleto);
	}
	
	public String capturaNumeroGuiaBoleto(MpCartorioOficio mpCartorioOficioSel, MpBoleto mpBoletoSelX) {
		//
		String mensagemLog = "";
		Integer numeroGuiaGerado = 0;
		//
		String ofCoNumX = "";
//		if (null == this.mpCartorioOficioSel)
//			ofCoNumX = "Co" + this.mpCartorioComarcaSel.getNumero();
//		else
			ofCoNumX = "Of" + mpCartorioOficioSel.getNumero();
		//
		MpSistemaConfig mpSistemaConfigX = this.mpSistemaConfigs.porParametro(ofCoNumX + "_BoletoNumeroGuia");
		if (null == mpSistemaConfigX) {
			mensagemLog = mensagemLog + "( " + "Error! Controle GUIA... não existe! Contactar o SUPORTE. = " 
																		+ ofCoNumX + "_BoletoNumeroGuia" + " ) ";
		} else {
			if (mpSistemaConfigX.getValorN() == 0) {
				//
				String[] wordsNumeroGuia = mpBoletoSelX.getBoletoInstrucao8().split(" ");
				 
//				MpAppUtil.PrintarLn(this.mpBoletoSelX.getBoletoInstrucao8() + " / " + wordsNumeroGuia.length);
//				numeroGuia = Integer.parseInt(this.mpBoletoSelX.getBoletoInstrucao8().substring(8, 16).trim());
				//
				try {
					//
					numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);

					// Atualiza numeroGuia BD !!!
					mpSistemaConfigX.setValorN(numeroGuiaGerado);
					
					this.mpSistemaConfigService.salvar(mpSistemaConfigX);
					//
//					MpAppUtil.PrintarLn("Controle GUIA(0)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
//							this.numeroGuiaGerado);
				} catch (Exception e) {
					//
					mensagemLog = mensagemLog + "( " + "Error! Controle GUIA/Instrução8 ! Contactar SUPORTE = " + 
															mpBoletoSelX.getBoletoInstrucao8() + " / e = " + e + " )";
				}
			} else {
				numeroGuiaGerado = mpSistemaConfigX.getValorN();
//				this.numeroGuiaGeradoAnt = this.numeroGuiaGerado;
				
				numeroGuiaGerado++;
		        // --------------------------------- //
		        // Verificar numero Guia Duplicado?  //
		        // --------------------------------- //
				String numOfCoX = "";
//				if (null == mpCartorioOficioSel)
//					numOfCoX = mpCartorioComarcaSel.getNumero();
//				else
					numOfCoX = mpCartorioOficioSel.getNumero();
				//
		        List<MpBoleto> mpBoletoGuias = this.mpBoletos.mpBoletoByNumeroGuiaGeradoList(numOfCoX,
		        																				numeroGuiaGerado);
		        if (mpBoletoGuias.size() > 0) {
		        	//
					numeroGuiaGerado++;
		        }

				// Atualiza numeroGuia BD !!!
				mpSistemaConfigX.setValorN(numeroGuiaGerado);
				
				this.mpSistemaConfigService.salvar(mpSistemaConfigX);
				//
//				MpAppUtil.PrintarLn("Controle GUIA(1)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
//						this.numeroGuiaGerado);
			}
		}
        //
        System.out.println("MpBoletoService.capturaNumeroGuiaBoleto()........................................" + 
        			"....... Controle Numeração GUIA : ( Oficio = " + mpCartorioOficioSel.getNumero() + " / " + 
        			this.sdfDMYHMS.format(new Date()) + " / Sacado : " + mpBoletoSelX.getNomeSacado());
        //

        if (!mensagemLog.isEmpty())
			return mensagemLog;
		//
		return "OK=" + numeroGuiaGerado;
	}
	
}
