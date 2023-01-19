package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpBoletoCargaLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.MpTituloCargaLog;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.service.MpBoletoCargaLogService;
import com.mpxds.mpbasic.service.MpTituloCargaLogService;
import com.mpxds.mpbasic.util.MpAppUtil;
 
@Named
@ViewScoped
public class MpRobotBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpTitulos mpTitulos;
	
	@Inject
	private MpBoletoCargaLogService mpBoletoCargaLogService;
	
	@Inject
	private MpTituloCargaLogService mpTituloCargaLogService;
	
	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.ofX}")
	private String ofX;

	// 
	
	private String pathFTP = ""; 

	private String fileBoleto = "mp_Boletos.Sql";
	private Integer contBoleto = 0;
	
	private String fileTitulo = "status_titulo.Sql";
	private Integer contTitulo = 0;
    		
	// ---
    
	public void init() {
	  	//
		if (null == ofX) {
			MpAppUtil.PrintarLn("MpRobotBean.init() - Error (OfX = NULL");
			return;
		}
		
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;   
		//		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.ofX + "_PathFTPRobot");
    	if (null == mpSistemaConfig)
    		this.pathFTP = "C:\\temp\\ftp-protestorjcapital\\ftp\\of" + this.ofX + "\\" ;
    	else
    		this.pathFTP = mpSistemaConfig.getValorT();
		//
		MpAppUtil.PrintarLn("MpRobotBean.init() - Entrou 000 (OfX = " + this.ofX + " / " + pathDateT +
																					" / PathFTP = " + this.pathFTP);
		this.trataCargaBoleto();
		
		this.trataCargaTitulo();

		MpAppUtil.PrintarLn("Robot FTP CARGA completada! " + " foi processado ! ( Boleto = " + contBoleto + 
																			  " / Titulo = " + contTitulo);
    }

	private void trataCargaBoleto() {
		//
        try {
			//
        	File fileXX = new File(this.pathFTP + File.separator + this.fileBoleto);

        	if (fileXX.exists()) {
        		//
        		this.contBoleto = MpAppUtil.countLines(fileXX);
        		
        		InputStream isX = new FileInputStream(fileXX);
        		
            	this.mpBoletos.executeSQLs(isX, 0L, this.ofX);
            	//
            	// Grava LOG ! 
            	// ==========
            	MpBoletoCargaLog mpBoletoCargaLog = new MpBoletoCargaLog(); 
            	
            	mpBoletoCargaLog.setDataGeracao(new Date());
            	mpBoletoCargaLog.setNumeroOficio(this.ofX);
            	mpBoletoCargaLog.setUsuarioNome("MpRobotBean");
        		mpBoletoCargaLog.setNumeroRegistros(contBoleto.longValue()); 
            	//    	
            	mpBoletoCargaLog = this.mpBoletoCargaLogService.salvar(mpBoletoCargaLog);
            	
            	MpAppUtil.PrintarLn("Robot FTP CARGA Boleto completado!... O arquivo " + 
        										fileXX.getAbsolutePath() +
        										" foi processado ! ( " + contBoleto);
        	}
			//
		}
		catch(Exception e) {
			//
	        MpAppUtil.PrintarLn("MpRobotBean.trataCargaBoleto() Erro-01X = Arquivo... (e = " + e);
		}
	}
	
	private void trataCargaTitulo() {
		//
        try {
			//
        	File fileXX = new File(this.pathFTP + File.separator + this.fileTitulo);

        	if (fileXX.exists()) {
        		//
        		this.contTitulo = MpAppUtil.countLines(fileXX);
        		
        		InputStream isX = new FileInputStream(fileXX);
        		
            	this.mpTitulos.executeSQLs(isX, 0L);
            	//
            	// Grava LOG ! 
            	// ==========
            	MpTituloCargaLog mpTituloCargaLog = new MpTituloCargaLog(); 
            	
            	mpTituloCargaLog.setDataGeracao(new Date());
            	mpTituloCargaLog.setNumeroOficio(this.ofX);
            	mpTituloCargaLog.setUsuarioNome("MpRobotBean");
        		mpTituloCargaLog.setNumeroRegistros(contBoleto.longValue()); 
            	//    	
            	mpTituloCargaLog = this.mpTituloCargaLogService.salvar(mpTituloCargaLog);
            	
            	MpAppUtil.PrintarLn("Robot FTP CARGA Titulo completado!... O arquivo " + 
        										fileXX.getAbsolutePath() +
        										" foi processado ! ( " + contTitulo);
        	}
			//
		}
		catch(Exception e) {
			//
	        MpAppUtil.PrintarLn("MpRobotBean.trataCargaTitulo() Erro-02X = Arquivo... (e = " + e);
		}
	}

	//
		
	public String getOfX() { return ofX; }
	public void setOfX(String ofX) { this.ofX = ofX; }

}