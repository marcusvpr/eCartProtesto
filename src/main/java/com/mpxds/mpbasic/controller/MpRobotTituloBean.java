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

import com.mpxds.mpbasic.model.MpRobotLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpTipoRobot;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.service.MpRobotLogService;
//import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
public class MpRobotTituloBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpTitulos mpTitulos;

	@Inject
	private MpRobotLogService mpRobotLogService;
	
	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.ofX}")
	private String ofX;
	
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private Long contTitulo = 0L;
	private Date dataDe = new Date();
	private Date dataAte = new Date();
	private String mensagem;
	    		
	// ---
    
	public void init() {
	  	//
		if (null == this.ofX) {
			this.mensagem = "MpRobotCargaTituloBean.init() - Error (OfX = NULL";
			return;
		}
		if (this.ofX.equals("1_") || this.ofX.equals("2_") || this.ofX.equals("3_")|| this.ofX.equals("4_")
		||  this.ofX.equals("1d") || this.ofX.equals("2d") || this.ofX.equals("3d")|| this.ofX.equals("4d"))
			assert(true); // nop
		else {
			this.mensagem = "MpRobotCargaTituloBean.init() - Error (OfX = 1_ à 4_ ou 1d à 4d";
			return;
		}
		//
		String pathFTP = ""; 
		String pathFileX = "/home/usuario-ftp/titulos/of";
		String arquivoX = "status_titulo_increment_" + this.ofX.substring(0,1) + ".Sql";

		Boolean indTesteWindows = false; // true;
		//
		if (indTesteWindows) {
			//
			pathFileX = "C:\\temp\\ftp-protestorjcapital\\mov\\titulos\\of";
			pathFTP = pathFileX + this.ofX.substring(0,1) + "\\" + arquivoX;
		} else {		
			//
	//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	//        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
	        
	//    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date());  
			//		
	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.ofX.substring(0,1) + "_PathFTPRobot");
	    	if (null == mpSistemaConfig) {
	//    		pathFTP = pathFileX + this.ofX.substring(0,1) + "/status_titulo_" + sdf.format(new Date()) + ".Sql" ; // "\\ ou /" ;
				pathFTP = pathFileX + this.ofX.substring(0,1) + "/" + arquivoX;
	    	} else
	    		pathFTP = mpSistemaConfig.getValorT();
		}
		//
//		MpAppUtil.PrintarLn("MpRobotCargaTituloBean.init() - Entrou 000 (OfX = " + this.ofX + " / " + pathDateT +
//																					" / PathFTP = " + pathFTP);
		this.trataCargaTitulo(pathFTP);
    }

	private void trataCargaTitulo(String pathFTP) {
		//
		this.contTitulo = 0L;

		try {
			//
			File fileX = new File(pathFTP);
			
			InputStream isX = FileUtils.openInputStream(fileX);
			
//		    MpAppUtil.PrintarLn("Robot Titulo ( Quantidade Titulos = " + fileX.length());
			
			this.trataArquivoTitulo(isX, this.ofX, this.contTitulo);
		    //
			this.mensagem = "Robot Titulo... foi processado ! ( Quantidade Titulos = " + 
		    		 				this.contTitulo + " ( Of.= " + this.ofX + " ( Periodo: " +
		    		 				this.sdfDMYHMS.format(this.dataDe) + " / " + this.sdfDMYHMS.format(this.dataAte);
		}
		catch(Exception e) {
			//
//			e.printStackTrace();
			this.mensagem = "MpRobotBean.trataCargaTitulo() Erro-01X = Arquivo... (e = " + e;
//			return;
		}
        //
	}
	
    public void trataArquivoTitulo(InputStream isX, String codOficio, Long numReg) {
    	//        
//    	System.out.println("MpCarregaTituloBean.trataArquivoTitulo() ( codOficio = " + codOficio + 
//        														" / isX = " + isX.toString() + " / Num.Reg = " + numReg);
        		
		// =================================================
        
		if (codOficio.equals("1d") || codOficio.equals("2d") || codOficio.equals("3d") || codOficio.equals("4d")) {
			//
	    	String rcX = this.mpTitulos.executeSQL("DELETE FROM mp_titulo WHERE codigo_oficio = '" + 
	    																				codOficio.substring(0,1) + "'");
	    	if (null == rcX || !rcX.equals("OK")) {
	    		//
//				MpFacesUtil.addErrorMessage("Erro_0005B - Erro Exclusão Movimento Titulo ( rc = " + rcX);
	    		this.mensagem = "Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX;
		        //
				return;
	    	}
	    	//
	    	String rcSqlX = this.mpTitulos.executeSQLs(isX, numReg);
	    	if (!rcSqlX.substring(0,2).equals("OK")) {
	    		//
//				MpFacesUtil.addErrorMessage("Erro_0007B - Erro na Carga Movimento Titulo ( rc = " + rcSqlX);
	    		this.mensagem = "Erro_0007B - Erro na Carga Movimento Titulo ( rc = " + rcSqlX;
				//
				return;
	    	}
	    	//
	    	this.contTitulo = Long.valueOf(rcSqlX.substring(4));
	    	//
		} else
			if (codOficio.equals("1_") || codOficio.equals("2_") || codOficio.equals("3_") || codOficio.equals("4_")) {
			//
	    	String rcSqlX = this.mpTitulos.executeSQLsIncr(isX, numReg);
	    	if (!rcSqlX.substring(0,2).equals("OK")) {
	    		//
//				MpFacesUtil.addErrorMessage("Erro_0007B - Erro na Carga Movimento Titulo ( rc = " + rcSqlX);
	    		this.mensagem = "Erro_0007C - Erro na Carga Movimento Titulo ( rc = " + rcSqlX;
				//
				return;
	    	}
	    	//
	    	this.contTitulo = Long.valueOf(rcSqlX.substring(4));
	    	//
		}
    	//
    	// Grava LOG ! 
    	// ==========
    	MpRobotLog mpRobotLog = new MpRobotLog(); 
    	
    	mpRobotLog.setMpTipoRobot(MpTipoRobot.TIT_INC);
    	mpRobotLog.setDataGeracao(new Date());
    	mpRobotLog.setNumeroOficio(this.ofX.substring(0,1));
    	mpRobotLog.setUsuarioNome("MpJobRobotTitulo");
		mpRobotLog.setNumeroRegistros(new Long(this.contTitulo));
    	    	
    	mpRobotLog = this.mpRobotLogService.salvar(mpRobotLog);
    	
//		MpFacesUtil.addInfoMessage("Upload completo!... O arquivo foi processado ! ( " + 
//																			this.contTitulo);
    	this.mensagem = "Upload completo!... Arquivo foi processado ! ( " + this.contTitulo;
    }
		
	// ---
		
	public String getOfX() { return ofX; }
	public void setOfX(String ofX) { this.ofX = ofX; }
	
	public Long getContTitulo() { return contTitulo; }
	public void setContTitulo(Long contTitulo) { this.contTitulo = contTitulo; }
	
	public String getDataDeSDF() { return this.sdfDMYHMS.format(dataDe); }
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }
	
	public String getDataAteSDF() { return this.sdfDMYHMS.format(dataAte); }
	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

}