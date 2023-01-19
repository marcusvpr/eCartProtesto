package com.mpxds.mpbasic.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.model.MpBoletoCargaLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpBoletoCargaLogService;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
public class MpCarregaBoletoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpBoletos mpBoletos;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpBoletoCargaLogService mpBoletoCargaLogService;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	@Inject
	private MpCargaControleService mpCargaControleService;
	
	// ---
	
	private UploadedFile file;
    
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
     
	private Long numeroRegistros;

	// ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
    }
	
    public void upload() {
    	//
		if (mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if (mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if (mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if (mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4;
		//
        if (null == this.mpCartorioOficioSel) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Cartório !");
	    	return;
        }
        
        // =======================================//
    	// Trata Carga Controle ! MR-2020-05-30 ! //
        // =======================================//
        String msgX = this.mpCargaControleService.validaCargaControle(this.mpCartorioOficioSel.getNumero());
		//
        if (!msgX.isEmpty()) {
        	//
        	MpFacesUtil.addErrorMessage(msgX);
	    	return;
        }
        // =======================================//

        //
        if (null == this.file) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Arquivo !");
	    	return;
        }
        //
        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		File fileX = null;
		Path folder = null;
		Path filePath = null;        
        //
		try {
			fileX = new File(extContext.getRealPath(File.separator + "resources" + File.separator + 
																			"boletoImportacao" + File.separator));
			folder = Paths.get(fileX.getAbsolutePath());
			if (!fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +
	    			extContext.getRealPath("//resources//boletoImportacao// ( e = " + e));
//	        MpAppUtil.PrintarLn("Pasta/Diretório... (path = " +
//	    			extContext.getRealPath("//resources//boletoImportacao// ( e = " + e));
	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaBoletoBean.upload() - 001");

        try {
			//
        	File fileXX = new File(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			folder = Paths.get(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			if (!fileXX.exists())
				filePath = Files.createFile(folder);
			//
			Integer cont = MpAppUtil.countLines(fileXX);
			this.numeroRegistros = cont.longValue() ;
			//
			try (InputStream input = this.file.getInputstream()) {
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			//
//			this.numeroRegistros = Files.lines(filePath).count();
		}
		catch(Exception e) {
			//
//	    	MpFacesUtil.addErrorMessage("Erro-01X = Arquivo... (file = " + folder.toString() + " / e = " + e +
//	    			" / numeroRegistros = " + this.numeroRegistros);
	        MpAppUtil.PrintarLn("Erro-01X = Arquivo... (file = " + folder.toString() + " / e = " + e +
	        		" / numeroRegistros = " + this.numeroRegistros);
//	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaBoletoBean.upload() - 002 ( Cart = " + this.mpCartorioOficioSel.getNumero() + 
//        		" / FileSize = " + this.file.getSize());

        try {
        	//
            this.trataArquivoBoleto(this.file.getInputstream(), this.mpCartorioOficioSel.getNumero(), 
            																				this.numeroRegistros);
            //
        } catch (IOException e) {
        	//
			MpFacesUtil.addErrorMessage("Erro_0001B - ( e = " + e.getMessage());
        }
    }
    
    public void trataArquivoBoleto(InputStream isX, String codOficio, Long numReg) {
    	//
//      MpAppUtil.PrintarLn("MpCarregaBoletoBean.trataArquivoBoleto() ( codOficio = " + codOficio + 
//        		" / isX = " + isX.toString() + " / Num.Reg = " + numReg);
        
        // Trata a zeragem da Numeração GUIA do BOLETO ...
		// =================================================
		MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("Of" + 
													this.mpCartorioOficioSel.getNumero() + "_BoletoNumeroGuia");
		if (null == mpSistemaConfig) {
			MpFacesUtil.addErrorMessage("Error ! Número Controle GUIA... não existe ! Favor contactar o SUPORTE. ( " + 
					"Of" + this.mpCartorioOficioSel.getNumero() + "_BoletoNumeroGuia");
			return;
		} else {
			// Atualiza numeroGuia BD !!!
			mpSistemaConfig.setValorN(0);
			
			this.mpSistemaConfigs.guardar(mpSistemaConfig);
		}
		
		// =================================================

        String rcX = this.mpBoletos.executeSQL("DELETE FROM mp_boleto WHERE codigo_oficio = '" + codOficio + "'");
    	if (null == rcX || !rcX.equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0005B - Erro Exclusão Movimento Boleto ( rc = " + rcX);
//	        MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento Boleto ( rc = " + rcX);
			return;
    	}
    	//
    	String rcSqlX = this.mpBoletos.executeSQLs(isX, numReg, codOficio);
    	if (!rcSqlX.substring(0,2).equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0007B - Erro na Carga Movimento Boleto ( rc = " + rcSqlX);
			return;
    	}
        // Trata número de Registros !
        this.numeroRegistros = 0L;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(this.file.getInputstream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //
            while (bufferedReader.ready()) {
            	bufferedReader.readLine();
                // 
                this.numeroRegistros++;
            }
            //
            bufferedReader.close();
            //
        } catch (IOException e) {
			MpFacesUtil.addErrorMessage("Erro_0007C - Erro na Carga Movimento Boleto ( e = " + e);
			return;
		} 
    	//
    	// Grava LOG ! 
    	// ==========
    	MpBoletoCargaLog mpBoletoCargaLog = new MpBoletoCargaLog(); 
    	
    	mpBoletoCargaLog.setDataGeracao(new Date());
    	mpBoletoCargaLog.setNumeroOficio(codOficio);
    	mpBoletoCargaLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
		mpBoletoCargaLog.setNumeroRegistros(this.numeroRegistros);
    	//    	
    	mpBoletoCargaLog = this.mpBoletoCargaLogService.salvar(mpBoletoCargaLog);
    	
		MpFacesUtil.addInfoMessage("Upload completo!... O arquivo " + this.file.getFileName() +
												" foi processado ! ( " + mpBoletoCargaLog.getNumeroRegistros());
    }
    
    // ---
    
    public Long getNumeroRegistros() { return numeroRegistros; }
    public void setNumeroRegistros(Long numeroRegistros) { this.numeroRegistros = numeroRegistros; }

    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }
    
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																	 this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
     
}