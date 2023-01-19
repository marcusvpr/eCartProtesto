package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import com.mpxds.mpbasic.model.MpBoletoCargaLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.service.MpBoletoCargaLogService;
//import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
public class MpRobotBoletoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpBoletoCargaLogService mpBoletoCargaLogService;
	
	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.ofX}")
	private String ofX;
	
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private Long contBoleto = 0L;
	private Date dataDe = new Date();
	private Date dataAte = new Date();
	private String mensagem;
	    		
	// ---
    
	public void init() {
	  	//
		if (null == this.ofX) {
			this.mensagem = "MpRobotCargaBoletoBean.init() - Error (OfX = NULL";
			return;
		}
		if (this.ofX.equals("1") || this.ofX.equals("2") || this.ofX.equals("3")|| this.ofX.equals("4"))
//			this.ofX.equals("1d") || this.ofX.equals("2d") || this.ofX.equals("3d")|| this.ofX.equals("4d"))
			assert(true); // nop
		else {
			this.mensagem = "MpRobotCargaTituloBean.init() - Error (OfX = 1-4";
			return;
		}
		//
		String pathFTP = ""; 

		String pathFileX = "/home/usuario-ftp/boletos/of";
//		String pathFileX = "C:\\temp\\ftp-protestorjcapital\\mov\\boletos\\of";
		//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
//    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date());  
		//		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.ofX.substring(0,1) + "_PathFTPRobot");
    	if (null == mpSistemaConfig)
    		pathFTP = pathFileX + this.ofX.substring(0,1) + "/mp_Boletos_" + sdf.format(new Date()) + ".Sql" ; // "\\ ou /" ;
    	else
    		pathFTP = mpSistemaConfig.getValorT();
		//
//		MpAppUtil.PrintarLn("MpRobotCargaBoletoBean.init() - Entrou 000 (OfX = " + this.ofX + " / " + pathDateT +
//																					" / PathFTP = " + pathFTP);
		this.trataCargaBoleto(pathFTP);
    }

	private void trataCargaBoleto(String pathFTP) {
		//
		this.contBoleto = 0L;

		try {
			//
			File fileX = new File(pathFTP);
			
			InputStream isX = FileUtils.openInputStream(fileX);
			
//		    MpAppUtil.PrintarLn("Robot Boleto ( Quantidade Boletos = " + fileX.length());
			
			this.trataArquivoBoleto(isX, this.ofX, this.contBoleto);
		    //
			this.mensagem = "Robot Boleto... foi processado ! ( Quantidade Boletos = " + 
		    		 				this.contBoleto + " ( Of.= " + this.ofX + " ( Periodo: " +
		    		 				this.sdfDMYHMS.format(this.dataDe) + " / " + this.sdfDMYHMS.format(this.dataAte);
		}
		catch(Exception e) {
			//
//			e.printStackTrace();
			this.mensagem = "MpRobotBean.trataCargaBoleto() Erro-01X = Arquivo... (e = " + e;
//			return;
		}
        //
	}
	
    public void trataArquivoBoleto(InputStream isX, String codOficio, Long numReg) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaBoletoBean.trataArquivoBoleto() ( codOficio = " + codOficio + 
//        														" / isX = " + isX.toString() + " / Num.Reg = " + numReg);
        
        // Trata a zeragem da Numeração GUIA do BOLETO ...
		// =================================================
		MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("Of" + this.ofX.substring(0,1) + 
																									"_BoletoNumeroGuia");
		if (null == mpSistemaConfig) {
//			MpFacesUtil.addErrorMessage("Error ! Número Controle GUIA... não existe ! Favor contactar o SUPORTE. ( " + 
//																				"Of" + this.ofX + "_BoletoNumeroGuia");
			this.mensagem = "Error ! Número Controle GUIA... não existe ! Favor contactar o SUPORTE. ( " + 
																"Of" + this.ofX.substring(0,1) + "_BoletoNumeroGuia";
			return;
		} else {
			// Atualiza numeroGuia BD !!!
			mpSistemaConfig.setValorN(0);
			
			this.mpSistemaConfigs.guardar(mpSistemaConfig);
		}
		
		// =================================================
        
		if (codOficio.equals("1d") || codOficio.equals("2d") || codOficio.equals("3d")|| codOficio.equals("4d")) {
			//
			String rcX = this.mpBoletos.executeSQL("DELETE FROM mp_boleto WHERE codigo_oficio = '" + 
																						codOficio.substring(0,1) + "'");
			if (null == rcX || !rcX.equals("OK")) {
				//
//				MpFacesUtil.addErrorMessage("Erro_0005B - Erro Exclusão Movimento Boleto ( rc = " + rcX);
				this.mensagem = "Erro_0005 - Erro Exclusão Movimento Boleto ( rc = " + rcX;
				//
				return;
			}
		}
    	//
    	String rcSqlX = this.mpBoletos.executeSQLs(isX, numReg, codOficio.substring(0,1));
    	if (!rcSqlX.substring(0,2).equals("OK")) {
    		//
//			MpFacesUtil.addErrorMessage("Erro_0007B - Erro na Carga Movimento Boleto ( rc = " + rcSqlX);
    		this.mensagem = "Erro_0007B - Erro na Carga Movimento Boleto ( rc = " + rcSqlX;
			//
			return;
    	}
    	//
    	this.contBoleto = Long.valueOf(rcSqlX.substring(4));

    	//
    	// Grava LOG ! 
    	// ==========
    	MpBoletoCargaLog mpBoletoCargaLog = new MpBoletoCargaLog(); 
    	
    	mpBoletoCargaLog.setDataGeracao(new Date());
    	mpBoletoCargaLog.setNumeroOficio(codOficio);
    	mpBoletoCargaLog.setUsuarioNome("MpRobotCargaBoleto");
		mpBoletoCargaLog.setNumeroRegistros(this.contBoleto);
    	//    	
    	mpBoletoCargaLog = this.mpBoletoCargaLogService.salvar(mpBoletoCargaLog);
    	
//		MpFacesUtil.addInfoMessage("Upload completo!... O arquivo foi processado ! ( " + 
//																			mpBoletoCargaLog.getNumeroRegistros());
    	this.mensagem = "Upload completo!... Arquivo foi processado ! ( " + mpBoletoCargaLog.getNumeroRegistros();
    }
		
	// ---
		
	public String getOfX() { return ofX; }
	public void setOfX(String ofX) { this.ofX = ofX; }
	
	public Long getContBoleto() { return contBoleto; }
	public void setContBoleto(Long contBoleto) { this.contBoleto = contBoleto; }
	
	public String getDataDeSDF() { return this.sdfDMYHMS.format(dataDe); }
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }
	
	public String getDataAteSDF() { return this.sdfDMYHMS.format(dataAte); }
	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

}