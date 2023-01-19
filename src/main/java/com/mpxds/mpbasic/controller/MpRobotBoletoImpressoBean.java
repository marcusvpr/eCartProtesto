package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

//import com.google.common.io.Files;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpRobotLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpTipoRobot;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.service.MpRobotLogService;
import com.mpxds.mpbasic.util.MpAppUtil;
 
@Named
@ViewScoped
public class MpRobotBoletoImpressoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpRobotLogService mpRobotLogService;
	
	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.ofX}")
	private String ofX;
	
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
	
	private Integer contBoleto = 0;
	private Date dataDe = new Date();
	private Date dataAte = new Date();
	    		
	// ---
    
	public void init() {
	  	//
		if (null == this.ofX) {
			MpAppUtil.PrintarLn("MpRobotBoletoImpressoBean.init() - Error (OfX = NULL");
			return;
		}
		//
		String pathFTP = ""; 

		String pathFileX = "/home/usuario-ftp/boletos_impressos/of";
//		String pathFileX = "C:\\temp\\ftp-protestorjcapital\\mov\\boletos_impressos\\of";
		//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;   
		//		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.ofX + "_PathFTPRobot");
    	if (null == mpSistemaConfig)
    		pathFTP = pathFileX + this.ofX + "/" ; // "\\" ;
    	else
    		pathFTP = mpSistemaConfig.getValorT();
		//
		MpAppUtil.PrintarLn("MpRobotBoletoImpressoBean.init() - Entrou 000 (OfX = " + this.ofX + " / " + pathDateT +
																					" / PathFTP = " + pathFTP);
		this.trataBoletoImpresso(pathFTP);
    }

	private void trataBoletoImpresso(String pathFTP) {
		//
		this.contBoleto = 0;

		try {
			//
        	this.dataDe = new Date();
        	this.dataAte = new Date();
        	
    		Calendar dataX = Calendar.getInstance();
    		
    		dataX.setTime(new Date());
    		dataX.set(Calendar.HOUR_OF_DAY, 0);
    		dataX.set(Calendar.MINUTE, 0);
    		dataX.set(Calendar.SECOND, 0);
//    		dataX.add(Calendar.DAY_OF_MONTH, -90);
    		this.dataDe = dataX.getTime();

    		dataX.setTime(new Date());
    		dataX.set(Calendar.HOUR_OF_DAY, 23);
    		dataX.set(Calendar.MINUTE, 59);
    		dataX.set(Calendar.SECOND, 59);
//    		dataX.add(Calendar.DAY_OF_MONTH, +1);
    		this.dataAte = dataX.getTime();
        	//
		    List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByOficioDataImpressaoList(this.ofX, 
		    																		  this.dataDe, this.dataAte);
		    //
			String newLineX = "\r\n"; // System.getProperty("line.separator").toString();		    
		    String boletoTXT = "";
		    String boletoALL = "[HEADER, Data = " + this.sdfDMYHMS.format(new Date()) + ", Total Registros = " + 
																					mpBoletoList.size() + " ]" + newLineX;
			String msg = "";
			//
			for (MpBoleto mpBoletoX : mpBoletoList) {
		    	//
			    if (!this.ofX.equals(mpBoletoX.getCodigoOficio()))
			    	continue;
		    	//
		    	contBoleto++;
		    	//
	    		msg = "";
	    		String dataProtocolo = "";
	    	    String numeroProtocolo = "";
	    	    //                    0123456789012345678901234567890123456789012345678901234
	    	    // Ex.: nome_sacado = ROBERTO RIBEIRO DOS SANTOS Protocolo: 06/04/2018-020198
	    	    //                                               0123456789012345678901234567890123456789012345678901234
	    	    Integer posProtocolo = mpBoletoX.getNomeSacado().indexOf("Protocolo:");
    	    	if (posProtocolo >= 0) {
	    	    	//
    	    	    if (mpBoletoX.getNomeSacado().trim().length() == posProtocolo + 28) {
	    	    		dataProtocolo = mpBoletoX.getNomeSacado().substring(posProtocolo + 11, posProtocolo + 11 + 10);
	    	    		numeroProtocolo = mpBoletoX.getNomeSacado().substring(posProtocolo + 11 + 11, 
	    	    																			posProtocolo + 11 + 11 + 6);
	    	    	} else
	    				msg = "Error : Captura Data/Numero Protocolo (Tam.=28)! Favor Verificar! ( ";
    	    	} else
    				msg = "Error : Captura Data/Numero Protocolo (Protocolo:)! Favor Verificar ! (";    	    		
    	    	//
    			if (!msg.isEmpty()) {
    				MpAppUtil.PrintarLn("MpRobotBoletoImpresso - Error : " + msg + mpBoletoX.getNomeSacado());
    				return;
    			}
	    		// Trata NULLs ...    			
    			if (null == mpBoletoX.getDataImpressao()) {
					MpAppUtil.PrintarLn("MpRobotBoletoImpresso - Error : dataImpressao = NULL (Ignorado)");
					continue;
				}
				//
    			String dataVencimento = this.sdfDMY.format(mpBoletoX.getDataVencimento());
    			if (mpBoletoX.getIndApos16h().equals("*"))
        			dataVencimento = this.sdfDMY.format(mpBoletoX.getDataVencimento1());    				
    			//
		    	boletoTXT = "[\"" + dataProtocolo + "\", " +
		    				 "\"" + numeroProtocolo + "\", " +
		    				 "\"" + mpBoletoX.getNumeroGuiaGerado() + "\", " +
						 	 "\"" + this.sdfDMYHMS.format(mpBoletoX.getDataImpressao()) + "\", " +
							 "\"" + dataVencimento + "\"]" + newLineX;
	    		//
	    		boletoALL = boletoALL + boletoTXT;
		    	//
		    }		
		    //
		    if (this.contBoleto == 0) {
		    	//
		    	MpAppUtil.PrintarLn("MpRobotBoletoImpresso - Nenhum Boleto Impresso... encontrado ! ( Of.= " + this.ofX +
		    		" (Periodo: " +	this.sdfDMYHMS.format(this.dataDe) + " / " + this.sdfDMYHMS.format(this.dataAte));
	        	return;
		    }
		    //
		    boletoALL = boletoALL + "[TRAILLER, Data = " + this.sdfDMYHMS.format(new Date()) + ", Total Registros = " + 
																				mpBoletoList.size() + " ]" + newLineX;
			//
			// 1Oficio-aaaammdd.txt (Prisco Ajutse MVPR-30052018 ...
			String nomeArquivo =  this.ofX + "Oficio-" + this.sdfYMD.format(new Date()) + ".txt";

			File targetFile = new File(pathFTP + nomeArquivo);
			// 
		    com.google.common.io.Files.write(boletoALL, targetFile, Charset.forName("UTF-8"));
			//
		    Path path = Paths.get(pathFTP + nomeArquivo);
		    
	        Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
	        
	        perms.add(PosixFilePermission.OWNER_WRITE);
	        perms.add(PosixFilePermission.OWNER_READ);
	        perms.add(PosixFilePermission.OWNER_EXECUTE);
	        perms.add(PosixFilePermission.GROUP_WRITE);
	        perms.add(PosixFilePermission.GROUP_READ);
	        perms.add(PosixFilePermission.GROUP_EXECUTE);
	        perms.add(PosixFilePermission.OTHERS_WRITE);
	        perms.add(PosixFilePermission.OTHERS_READ);
	        perms.add(PosixFilePermission.OTHERS_EXECUTE);
	        
	        Files.setPosixFilePermissions(path, perms);	        
		    //
	    	//
	    	// Grava LOG ! 
	    	// ==========
	    	MpRobotLog mpRobotLog = new MpRobotLog(); 
	    	
	    	mpRobotLog.setMpTipoRobot(MpTipoRobot.BOL_IMP);
	    	mpRobotLog.setDataGeracao(new Date());
	    	mpRobotLog.setNumeroOficio(this.ofX);
	    	mpRobotLog.setUsuarioNome("MpJobRobotImpresso");
			mpRobotLog.setNumeroRegistros(new Long(this.contBoleto));
	    	    	
	    	mpRobotLog = this.mpRobotLogService.salvar(mpRobotLog);
	        //
		    MpAppUtil.PrintarLn("Robot Boleto Impresso... foi processado ! ( Quantidade Boletos Impressos = " + 
		    		 				this.contBoleto + " ( Of.= " + this.ofX + " ( Periodo: " +
		    		 				this.sdfDMYHMS.format(this.dataDe) + " / " + this.sdfDMYHMS.format(this.dataAte));
		}
		catch(Exception e) {
			//
			e.printStackTrace();
//			MpAppUtil.PrintarLn("MpRobotBean.trataCargaBoleto() Erro-01X = Arquivo... (e = " + e);
//			return;
		}
        //
	}
	
	// ---
		
	public String getOfX() { return ofX; }
	public void setOfX(String ofX) { this.ofX = ofX; }
	
	public Integer getContBoleto() { return contBoleto; }
	public void setContBoleto(Integer contBoleto) { this.contBoleto = contBoleto; }
	
	public String getDataDeSDF() { return this.sdfDMYHMS.format(dataDe); }
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }
	
	public String getDataAteSDF() { return this.sdfDMYHMS.format(dataAte); }
	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }

}