package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.model.MpAutorizaCancelamento;
import com.mpxds.mpbasic.model.MpAutorizaCancelamentoCargaLog;
import com.mpxds.mpbasic.model.MpCargaControle;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpAutorizaCancelamentos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpAutorizaCancelamentoCargaLogService;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
@ManagedBean
public class MpCarregaAutorizaCancelamentoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpAutorizaCancelamentos mpAutorizaCancelamentos;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpAutorizaCancelamentoCargaLogService mpAutorizaCancelamentoCargaLogService;
	
	@Inject
	private MpCargaControleService mpCargaControleService;
	
	// ---
	
    private UploadedFile file;

    private File fileX = null;
    private File fileY = null;
    private File fileZ = null;
    
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
    
    private Integer progresso;
    private String mensagem;
    private Integer quantidadeAutorizaCancelamentos;
    private List<String> autorizaCancelamentos;
	
    private Boolean indFile;
    private Boolean indCancel;
    private Boolean indPBar = false;
        
	private Long numeroRegistros;
        
	private SimpleDateFormat sdfYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private ExecutorService service = Executors.newSingleThreadExecutor();    
	
    private Boolean indCargaDiaria = true;
    
    // ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;

		this.indFile = false;	
		this.indCancel = false;	
		
		this.indCargaDiaria = true;
		//			
    }
	
    public void uploadX() {
    	//
		if ( mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if ( mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if ( mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if ( mpSeguranca.isUsuarioOf4())
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		Path folder = null;
		Path filePath = null;        
        //
		try {
			//
			this.fileX = new File(extContext.getRealPath(File.separator + "resources" + File.separator + 
																"autorizaCancelamentoImportacao" + File.separator));
			folder = Paths.get(this.fileX.getAbsolutePath());
			if (!this.fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +
	    			extContext.getRealPath("//resources//autorizaCancelamentoImportacao// ( e = " + e));
//	        MpAppUtil.PrintarLn("Pasta/Diretório... (path = " +
//	    			extContext.getRealPath("//resources//autorizaCancelamentoImportacao// ( e = " + e));
	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.uploadX() - 001");

        try {
			//
        	this.fileY = new File(this.fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			folder = Paths.get(this.fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			if (!this.fileY.exists())
				filePath = Files.createFile(folder);
			//
			Integer cont = MpAppUtil.countLines(this.fileY);
			this.numeroRegistros = cont.longValue() ;
			
			try (InputStream input = this.file.getInputstream()) {
				//
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			//
			this.numeroRegistros = Files.lines(filePath).count();
		}
		catch(Exception e) {
			//
//	    	MpFacesUtil.addErrorMessage("Erro-01X = Arquivo... (file = " + folder.toString() + " / e = " + e +
//	    			" / numeroRegistros = " + this.numeroRegistros);
	        MpAppUtil.PrintarLn("Erro-01TX = Arquivo... (file = " + folder.toString() + " / e = " + e +
	        		" / numeroRegistros = " + this.numeroRegistros);
//	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.upload() - 002 ( Cart = " + 
//        														this.mpCartorioOficioSel.getNumero() + 
//        														" / FileX = " + this.fileX.getAbsolutePath() +
//        														" / FileSizeY = " + this.fileY.getAbsolutePath());
        //
        try {
        	//
        	if (this.file.getFileName().toUpperCase().contains(".XLS")) // Trata Excell
        		this.trataArquivoAutorizaCancelamentoXLS(this.file, this.mpCartorioOficioSel.getNumero());
        	else
            if (this.file.getFileName().toUpperCase().contains(".CSV")) // Trata CSV
            	this.trataArquivoAutorizaCancelamentoX(this.file.getInputstream(), 
            											this.mpCartorioOficioSel.getNumero(),
            											this.numeroRegistros);
            else {
            	//
    			MpFacesUtil.addErrorMessage("Arquivo não é do tipo: '.xls' ou '.csv' ! Favor verificar!");
    			return;
            }
            //
        } catch (IOException e) {
        	//
			MpFacesUtil.addErrorMessage("Erro_0001X - ( e = " + e.getMessage());
        }
    }

    public void upload(FileUploadEvent event) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.upload() - 000");
		if ( mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if ( mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if ( mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if ( mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4;
		//        
        if (null == this.mpCartorioOficioSel) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Cartório !");
	    	return;
        }
    	//
        this.file = event.getFile();
        if (null == this.file) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Arquivo !");
	    	return;
        }
        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		Path folder = null;
		Path filePath = null;        
        //
		try {
			//
			this.fileZ = new File(extContext.getRealPath(File.separator + "resources" + File.separator + 
															"autorizaCancelamentoImportacao" + File.separator));
			folder = Paths.get(this.fileZ.getAbsolutePath());
			if (!this.fileZ.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " + extContext.getRealPath(
	    											"//resources//autorizaCancelamentoImportacao// ( e = " + e));
	    	return;
		}
		//
//		MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.upload() - 001");

        try {
			//
        	this.fileY = new File(this.fileZ.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			folder = Paths.get(this.fileY.getAbsolutePath() + File.separator);
			if (!this.fileY.exists())
				filePath = Files.createFile(folder);
			//
			try (InputStream input = this.file.getInputstream()) {
				//
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
				
				input.close();
			}			
			//
//			this.quantidadeAutorizaCancelamentos = (int) Files.lines(filePath).count();			
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Erro.001 Arquivo... (file = " + folder.toString() + " / e = " + e);
//	        MpAppUtil.PrintarLn("Erro.001 Arquivo... (file = " + folder.toString() + " / e = " + e);
	    	return;
		}
		//
        this.indFile = true;
        
        MpFacesUtil.addInfoMessage("Upload.001 OK ! ( Nome Arquivo = " + this.file.getFileName() + 
        													" / Tam.Arquivo = " + this.file.getSize());        
    }  

//  public void trataArquivoAutorizaCancelamentoXLS(InputStream fileXLS, String codigoOficio) {
    public void trataArquivoAutorizaCancelamentoXLS(UploadedFile fileXLS, String codigoOficio) {
    	//
//      MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.trataArquivoAutorizaCancelamento() ( codOficio = "
//																    	+ codOficio + " / isX = " + isX.toString());
    	// ===============================================
        // Se a carga é diaria não apaga dados da Tabela ! 
    	// ===============================================
    	if (this.indCargaDiaria)
    		assert(true); //nop
    	else {
    		//
        	MpFacesUtil.addErrorMessage("Tratando a limpeza da Tabela Autorização de Cancelamentos");

        	String rcX = this.mpAutorizaCancelamentos.executeSQL("DELETE FROM mp_autoriza_cancelamento WHERE" + 
            															" codigo_oficio = '" + codigoOficio + "'");
            if (null == rcX || !rcX.equals("OK")) {
            	//
            	MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Autoriza Cancelamento ( rc = " + rcX);
            	//MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento AutorizaCancelamento ( rc = " + rcX);
            	//
            	return;
            }
    	}
        //
        MpAutorizaCancelamento mpAutorizaCancelamento =	null;
        
        Integer iX = 0;
        String lineX = "";
    	String[] lineArray = new String[50];

    	SimpleDateFormat sdfX = new SimpleDateFormat("dd/MM/yyyy"); 

    	//
    	try {
    		//
//			Workbook workbook = new XSSFWorkbook(fileXLS);

//	        System.out.println("Entrou.000 ( fileX.Path =" +this.fileX.getAbsolutePath());
//	        System.out.println("Entrou.001 ( fileY.Path =" +this.fileY.getAbsolutePath());
//	        System.out.println("Entrou.003 ( fileZ.Path =" +this.fileZ.getAbsolutePath());

	        String SAMPLE_XLSX_FILE_PATH = this.fileY.getAbsolutePath();
	        
    		// Creating a Workbook from an Excel file (.xls or .xlsx)
	        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

	        // Retrieving the number of sheets in the Workbook
//	        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

	        /*
	           =============================================================
	           Iterating over all the sheets in the workbook (Multiple ways)
	           =============================================================
	        */

	        // 1. You can obtain a sheetIterator and iterate over it
//	        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
//	        
//	        System.out.println("Retrieving Sheets using Iterator");
//	        
//	        while (sheetIterator.hasNext()) {
//	        	//
//	            Sheet sheet = sheetIterator.next();
//	            System.out.println("=> " + sheet.getSheetName());
//	        }
//
//	        // 2. Or you can use a for-each loop
//	        System.out.println("Retrieving Sheets using for-each loop");
//	        
//	        for(Sheet sheet: workbook) {
//	        	//
//	            System.out.println("=> " + sheet.getSheetName());
//	        }
//
//	        // 3. Or you can use a Java 8 forEach with lambda
//	        System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
//	        
//	        workbook.forEach(sheet -> {
//	        	//
//	            System.out.println("=> " + sheet.getSheetName());
//	        });

	        /*
	           ==================================================================
	           Iterating over all the rows and columns in a Sheet (Multiple ways)
	           ==================================================================
	        */

	        // Getting the Sheet at index zero
	        Sheet sheet = workbook.getSheetAt(0);

	        // Create a DataFormatter to format and get each cell's value as String
	        DataFormatter dataFormatter = new DataFormatter();

//	        // 1. You can obtain a rowIterator and columnIterator and iterate over them
//	        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
//	        
//	        Iterator<Row> rowIterator = sheet.rowIterator();
//	        
//	        while (rowIterator.hasNext()) {
//	        	//
//	            Row row = rowIterator.next();
//
//	            // Now let's iterate over the columns of the current row
//	            Iterator<Cell> cellIterator = row.cellIterator();
//
//	            while (cellIterator.hasNext()) {
//	            	//
//	                Cell cell = cellIterator.next();
//	                String cellValue = dataFormatter.formatCellValue(cell);
//
//	                System.out.print(cellValue + "\t");
//	            }
//	            System.out.println();
//	        }

	        // 2. Or you can use a for-each loop to iterate over the rows and columns
//	        System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
	        
	    	// ----------------------------------------------

	        this.numeroRegistros = 0L;
	        
	        Boolean indEofXls = false;
	        //
        	for (Row row: sheet) {
	        	//
            	iX= 0;
	        	
		        this.numeroRegistros++;
//		        System.out.print("Entrou.000 ........... row = " + this.numeroRegistros++ + "\t");

		    	lineX = "";
	        	lineArray = new String[50];

//    			System.out.print("Entrou.001 ........... Número Colunas da planilha difere de (30)! ( " +
//    																						row.getLastCellNum());	        		
	        	//
	            for (Cell cell: row) {
	            	//
	                String cellValue = dataFormatter.formatCellValue(cell);

//	                System.out.print("Entrou.001 ........... cellValue = " + cellValue + "\t");

	                if (iX == 0) {
	                	//
	        	    	if (cellValue.trim().equals("Apresentante"))
	        	    		break; // Ignora Header arquivo CSV !
	        	    	
	        	    	if (cellValue.isEmpty()) // Fim Planilha !
	        	    		indEofXls = true;
	                }
	                
	                // Trata Error-29042020 ...previne error no array !
	                if (cellValue.isEmpty())
	                	cellValue = ".";
	                //
	                lineX = lineX + cellValue + " | " ;
	                
	                lineArray[iX] = cellValue;
	                
	                iX++;
	            }
	            //
	            if (indEofXls) {
	    	        // Closing the workbook
	    	        workbook.close();			
	    	        return;
	            }
	            
	            if (lineX.isEmpty())
	            	continue;
	            //
//	            System.out.print("Entrou.002 .... Num.Linha = " + row.getRowNum() + " / LineX = " + lineX
//	            														+ " / NCol. =" + row.getLastCellNum() + "\t");	            
//    	    	try {
    	    		// Verifica se já existe registro por Data / NumeroProtocolo ...
//        	    	MpAutorizaCancelamento mpAutorizaCancelamento =	null; // mpAutorizaCancelamentos.porDataNumeroProtocolo(
//        	    															sdfX.parse(lineArray[3]), lineArray[3]);
//       	    	if (null == mpAutorizaCancelamento)
       	    	 	mpAutorizaCancelamento = new MpAutorizaCancelamento();
       	    	
        	    	mpAutorizaCancelamento.setCodigoOficio(this.mpCartorioOficioSel.getNumero());
        	    	mpAutorizaCancelamento.setApresentante(lineArray[0]);
        	    	mpAutorizaCancelamento.setCodigo(lineArray[1]);
        	    	
        	    	// Trata Numero Oficio ... = Ignorado ! (2º Ofício de Protesto de Títulos do Rio de Janeiro) ...
					if (lineArray[2].substring(0,1).equals(this.mpCartorioOficioSel.getNumero()))
						assert(true); 
					else {
						//
	    				MpFacesUtil.addErrorMessage("Numero Oficio X Registro Incompátives ! ( Oficio = "
														+ this.mpCartorioOficioSel.getNumero() + " / Reg. = " 
								    					+ lineX + " / Num.Linha = " + this.numeroRegistros);
	    				break;
					}
        	    	//
					mpAutorizaCancelamento.setData(sdfX.parse(lineArray[3]));
					
					//      01234567890
					// Ex.: 0000010547 -> 010547 !?
	    	    	mpAutorizaCancelamento.setProtocolo(lineArray[4].substring(4));
	    	    	
	    	    	mpAutorizaCancelamento.setDataProtocolo(sdfX.parse(lineArray[5]));
	    	    	mpAutorizaCancelamento.setDataRemessa(sdfX.parse(lineArray[6]));
	    	    	//
	    	    	
	    	    	// Trata Erro Quando só tem 29 Colunas(4OOf/CodigoCedente em branco - MR.29-04-2020!
	    	    	if (iX == 30) {
	    	    		//
		    	    	mpAutorizaCancelamento.setAgenciaCodigoCedente(lineArray[7]);
		    	    	if (mpAutorizaCancelamento.getAgenciaCodigoCedente().isEmpty())
			    	    	mpAutorizaCancelamento.setAgenciaCodigoCedente(".");

		    	    	mpAutorizaCancelamento.setCedente(lineArray[8]);
		    	    	mpAutorizaCancelamento.setSacador(lineArray[9]);
		    	    	if (mpAutorizaCancelamento.getSacador().isEmpty())
			    	    	mpAutorizaCancelamento.setSacador(".");
		    	    	
		    	    	mpAutorizaCancelamento.setDocumentoSacador(lineArray[10]);
		    	    	if (mpAutorizaCancelamento.getDocumentoSacador().isEmpty())
			    	    	mpAutorizaCancelamento.setDocumentoSacador(".");
		    	    	
		    	    	mpAutorizaCancelamento.setDevedor(lineArray[11]);
		    	    	mpAutorizaCancelamento.setDocumentoDevedor(lineArray[12]);
		    	    	mpAutorizaCancelamento.setEndereco(lineArray[13]);
	
		    	    	mpAutorizaCancelamento.setCep(lineArray[14]);
		    	    	if (mpAutorizaCancelamento.getCep().isEmpty())
			    	    	mpAutorizaCancelamento.setCep(".");
		    	    	//
		    	    	mpAutorizaCancelamento.setBairro(lineArray[15]);
		    	    	if (mpAutorizaCancelamento.getBairro().isEmpty())
			    	    	mpAutorizaCancelamento.setBairro(".");
		    	    	mpAutorizaCancelamento.setCidade(lineArray[16]);
		    	    	mpAutorizaCancelamento.setUf(lineArray[17]);
		    	    	//
		    	    	mpAutorizaCancelamento.setNossoNumero(lineArray[18]);
		    	    	if (mpAutorizaCancelamento.getNossoNumero().isEmpty())
			    	    	mpAutorizaCancelamento.setNossoNumero(".");
		    	    	//
		    	    	mpAutorizaCancelamento.setNumeroTitulo(lineArray[19]);
		    	    	mpAutorizaCancelamento.setValor(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[20]))));
		    	    	mpAutorizaCancelamento.setSaldo(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[21]))));
		    	    	mpAutorizaCancelamento.setEspecie(lineArray[22]);
		    	    	mpAutorizaCancelamento.setPracaProtesto(lineArray[23]);
		    	    	mpAutorizaCancelamento.setTipoAutorizacao(lineArray[24]);
		    	    	mpAutorizaCancelamento.setSituacao(lineArray[25]);
		    	    	mpAutorizaCancelamento.setImpresso(lineArray[26]);
		    	    	mpAutorizaCancelamento.setVencimento(lineArray[27]);
		    	    	mpAutorizaCancelamento.setOcorrencia(lineArray[28]);
		    	    	mpAutorizaCancelamento.setDataOcorrencia(sdfX.parse(lineArray[29]));
		    	    	//
	    	    	} else {
//	    	            System.out.print("Entrou.003.... LineX = " + lineX + " / NumLin. =" + this.numeroRegistros 
//																+ " / NCol. =" + iX + "\t");	            
//
//	    	            System.out.print("Entrou.003-0....  = '" +  lineArray[0] + "'" + "\t");
//	    	            System.out.print("Entrou.003-1....  = '" +  lineArray[1] + "'" + "\t");
//	    	            System.out.print("Entrou.003-2....  = '" +  lineArray[2] + "'" + "\t");
//	    	            System.out.print("Entrou.003-3....  = '" +  lineArray[3] + "'" + "\t");
//	    	            System.out.print("Entrou.003-4....  = '" +  lineArray[4] + "'" + "\t");
//	    	            System.out.print("Entrou.003-5....  = '" +  lineArray[5] + "'" + "\t");
//	    	            System.out.print("Entrou.003-6....  = '" +  lineArray[6] + "'" + "\t");
//	    	            System.out.print("Entrou.003-7....  = '" +  lineArray[7] + "'" + "\t");
//	    	            System.out.print("Entrou.003-8....  = '" +  lineArray[8] + "'" + "\t");
//	    	            System.out.print("Entrou.003-9....  = '" +  lineArray[9] + "'" + "\t");
//	    	            System.out.print("Entrou.003-10...  = '" +  lineArray[10] + "'" + "\t");
//	    	            System.out.print("Entrou.003-11...  = '" +  lineArray[11] + "'" + "\t");
//	    	            System.out.print("Entrou.003-12...  = '" +  lineArray[12] + "'" + "\t");
//	    	            System.out.print("Entrou.003-13...  = '" +  lineArray[13] + "'" + "\t");
//	    	            System.out.print("Entrou.003-14...  = '" +  lineArray[14] + "'" + "\t");
//	    	            System.out.print("Entrou.003-15...  = '" +  lineArray[15] + "'" + "\t");
//	    	            System.out.print("Entrou.003-16...  = '" +  lineArray[16] + "'" + "\t");
//	    	            System.out.print("Entrou.003-17...  = '" +  lineArray[17] + "'" + "\t");
//	    	            System.out.print("Entrou.003-18...  = '" +  lineArray[18] + "'" + "\t");
//	    	            System.out.print("Entrou.003-19...  = '" +  lineArray[19] + "'" + "\t");
//	    	            System.out.print("Entrou.003-20...  = '" +  lineArray[20] + "'" + "\t");
//	    	            System.out.print("Entrou.003-21...  = '" +  lineArray[21] + "'" + "\t");
//	    	            System.out.print("Entrou.003-22...  = '" +  lineArray[22] + "'" + "\t");
//	    	            System.out.print("Entrou.003-23...  = '" +  lineArray[23] + "'" + "\t");
//	    	            System.out.print("Entrou.003-24...  = '" +  lineArray[24] + "'" + "\t");
//	    	            System.out.print("Entrou.003-25...  = '" +  lineArray[25] + "'" + "\t");
//	    	            System.out.print("Entrou.003-26...  = '" +  lineArray[26] + "'" + "\t");
//	    	            System.out.print("Entrou.003-27...  = '" +  lineArray[27] + "'" + "\t");
//	    	            System.out.print("Entrou.003-28...  = '" +  lineArray[28] + "'" + "\t");
	    		    	//
		    	    	mpAutorizaCancelamento.setAgenciaCodigoCedente("..");

		    	    	mpAutorizaCancelamento.setCedente(lineArray[7]);
	    		    	mpAutorizaCancelamento.setSacador(lineArray[8]);
	    		    	if (mpAutorizaCancelamento.getSacador().isEmpty())
	    	    	    	mpAutorizaCancelamento.setSacador(".");
	    		    	
	    		    	mpAutorizaCancelamento.setDocumentoSacador(lineArray[9]);
	    		    	if (mpAutorizaCancelamento.getDocumentoSacador().isEmpty())
	    	    	    	mpAutorizaCancelamento.setDocumentoSacador(".");
	    		    	
	    		    	mpAutorizaCancelamento.setDevedor(lineArray[10]);
	    		    	mpAutorizaCancelamento.setDocumentoDevedor(lineArray[11]);
	    		    	mpAutorizaCancelamento.setEndereco(lineArray[12]);

	    		    	mpAutorizaCancelamento.setCep(lineArray[13]);
	    		    	if (mpAutorizaCancelamento.getCep().isEmpty())
	    	    	    	mpAutorizaCancelamento.setCep(".");
	    		    	//
		    	    	if (iX == 29) {  // Faltando Agência Cod.Cedente !
		    	    		//
		    		    	mpAutorizaCancelamento.setBairro(lineArray[14]);
		    		    	if (mpAutorizaCancelamento.getBairro().isEmpty())
		    	    	    	mpAutorizaCancelamento.setBairro(".");
		    		    	mpAutorizaCancelamento.setCidade(lineArray[15]);
		    		    	mpAutorizaCancelamento.setUf(lineArray[16]);
		    		    	//
		    		    	mpAutorizaCancelamento.setNossoNumero(lineArray[17]);
		    		    	if (mpAutorizaCancelamento.getNossoNumero().isEmpty())
		    	    	    	mpAutorizaCancelamento.setNossoNumero(".");
		    		    	//
		    		    	mpAutorizaCancelamento.setNumeroTitulo(lineArray[18]);
		    		    	mpAutorizaCancelamento.setValor(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[19]))));
		    		    	mpAutorizaCancelamento.setSaldo(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[20]))));
		    		    	mpAutorizaCancelamento.setEspecie(lineArray[21]);
		    		    	mpAutorizaCancelamento.setPracaProtesto(lineArray[22]);
		    		    	mpAutorizaCancelamento.setTipoAutorizacao(lineArray[23]);
		    		    	mpAutorizaCancelamento.setSituacao(lineArray[24]);
		    		    	mpAutorizaCancelamento.setImpresso(lineArray[25]);
		    		    	mpAutorizaCancelamento.setVencimento(lineArray[26]);
		    		    	mpAutorizaCancelamento.setOcorrencia(lineArray[27]);
		    		    	//
		    				mpAutorizaCancelamento.setDataOcorrencia(sdfX.parse(lineArray[28]));
		    				//
		    	    	} else { // Faltando BAIRRO ! 
		    	    		//
		    		    	mpAutorizaCancelamento.setBairro("..");

		    		    	mpAutorizaCancelamento.setCidade(lineArray[14]);
		    		    	mpAutorizaCancelamento.setUf(lineArray[15]);
		    		    	//
		    		    	mpAutorizaCancelamento.setNossoNumero(lineArray[16]);
		    		    	if (mpAutorizaCancelamento.getNossoNumero().isEmpty())
		    	    	    	mpAutorizaCancelamento.setNossoNumero(".");
		    		    	//
		    		    	mpAutorizaCancelamento.setNumeroTitulo(lineArray[17]);
		    		    	mpAutorizaCancelamento.setValor(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[18]))));
		    		    	mpAutorizaCancelamento.setSaldo(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[19]))));
		    		    	mpAutorizaCancelamento.setEspecie(lineArray[20]);
		    		    	mpAutorizaCancelamento.setPracaProtesto(lineArray[21]);
		    		    	mpAutorizaCancelamento.setTipoAutorizacao(lineArray[22]);
		    		    	mpAutorizaCancelamento.setSituacao(lineArray[23]);
		    		    	mpAutorizaCancelamento.setImpresso(lineArray[24]);
		    		    	mpAutorizaCancelamento.setVencimento(lineArray[25]);
		    		    	mpAutorizaCancelamento.setOcorrencia(lineArray[26]);
		    		    	//
		    				mpAutorizaCancelamento.setDataOcorrencia(sdfX.parse(lineArray[27]));
		    	    	}
	    	    		//
	    	    	}
	    	    	//
	    	    	mpAutorizaCancelamentos.guardar(mpAutorizaCancelamento);
	    	    	//
    	    	//
	        } // Loop Row

//	        // 3. Or you can use Java 8 forEach loop with lambda
//	        System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
//	        
//	        sheet.forEach(row -> {
//	        	//
//	            row.forEach(cell -> {
//	            	//
//	                String cellValue = dataFormatter.formatCellValue(cell);
//	                System.out.print(cellValue + "\t");
//	            });
//	            //
//	            System.out.println();
//	        });
	        
	        //
//	        sheet.forEach(row -> {
//	        	//
//	            row.forEach(cell -> {
//	            	//
//	                System.out.println("Loop(3) ....................... " + this.printCellValue(cell));
//	            });
//	            //
//	            System.out.println();
//	        });
	        
	        // Closing the workbook
	        workbook.close();			
			//
	    	// Grava LOG ! 
	    	// ==========
		    
	    	MpAutorizaCancelamentoCargaLog mpAutorizaCancelamentoCargaLog = new MpAutorizaCancelamentoCargaLog(); 
	    	
	    	mpAutorizaCancelamentoCargaLog.setDataGeracao(new Date());
	    	mpAutorizaCancelamentoCargaLog.setNumeroOficio(codigoOficio);
	    	mpAutorizaCancelamentoCargaLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
			mpAutorizaCancelamentoCargaLog.setNumeroRegistros(this.numeroRegistros);
	    	//    	
	    	mpAutorizaCancelamentoCargaLog = this.mpAutorizaCancelamentoCargaLogService.salvar(
	    																			mpAutorizaCancelamentoCargaLog);
	    	
			MpFacesUtil.addInfoMessage("Upload/Carga completa!... O arquivo " + this.file.getFileName() +
										" foi processado ! ( " + mpAutorizaCancelamentoCargaLog.getNumeroRegistros());
	    	//
		} catch (Exception e) {
			//
	    	System.out.println("Error_005 Exception ( contLineX = " + this.numeroRegistros + 
						" / LineX = " + lineX + " / Num.= " + trataNum(lineArray[20]) + " / " +
						trataNum(lineArray[21]) + " / NCol = " + iX + " / e = " + e);
			MpFacesUtil.addErrorMessage("Error Exception lineX = ... ( contLineX = " + this.numeroRegistros +
						" / LineX = " + lineX + " / Num.= " + trataNum(lineArray[20]) + " / " + 
						trataNum(lineArray[21]) + " / NCol = " + iX + " / e = " + e);
		}
    	//
    }
    
//    private String printCellValue(Cell cell) {
//    	//
//    	String valorCell = "";
//    	
//        switch (cell.getCellTypeEnum()) {
//            case BOOLEAN:
//            	valorCell = cell.getBooleanCellValue() + "";
//
//            	System.out.print(cell.getBooleanCellValue());
//                break;
//            case STRING:
//            	valorCell = cell.getRichStringCellValue().getString();
//
//                System.out.print(cell.getRichStringCellValue().getString());
//                break;
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                	valorCell = cell.getDateCellValue() + "";
//
//                    System.out.print(cell.getDateCellValue());
//                } else {
//                	valorCell = cell.getNumericCellValue() + "";
//
//                    System.out.print(cell.getNumericCellValue());
//                }
//                break;
//            case FORMULA:
//            	valorCell = cell.getCellFormula() + "";
//
//                System.out.print(cell.getCellFormula());
//                break;
//            case BLANK:
//            	valorCell = ".";
//
//                System.out.print("");
//                break;
//            default: {
//            	valorCell = "?";
//
//            	System.out.print("?");
//            }
//        }
//        //
//        System.out.print("\t");
//        
//        return valorCell;
//    }    
    
    public void trataArquivoAutorizaCancelamentoX(InputStream isX, String codigoOficio, Long numReg) {
    	//
//      MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.trataArquivoAutorizaCancelamento() ( codOficio = "
//																    	+ codOficio + " / isX = " + isX.toString());
    	// ===============================================
        // Se a carga é diaria não apaga dados da Tabela ! 
    	// ===============================================
    	if (this.indCargaDiaria)
    		assert(true); //nop
    	else {
    		//
        	MpFacesUtil.addErrorMessage("Tratando a limpeza da Tabela Autorização de Cancelamentos");

        	String rcX = this.mpAutorizaCancelamentos.executeSQL("DELETE FROM mp_autoriza_cancelamento WHERE" + 
            															" codigo_oficio = '" + codigoOficio + "'");
            if (null == rcX || !rcX.equals("OK")) {
            	//
            	MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Autoriza Cancelamento ( rc = " + rcX);
            	//MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento AutorizaCancelamento ( rc = " + rcX);
            	//
            	return;
            }
    	}
    	
    	// ----------------------------------------------
    	SimpleDateFormat sdfX = new SimpleDateFormat("dd/MM/yyyy"); 
 
        Scanner scX = new Scanner(isX);
    	//
        this.numeroRegistros = 0L;
    	
    	while(scX.hasNextLine()) {
    		//    		
    		String lineX = scX.nextLine();
    		
    		lineX = lineX.replace("££", "£.£");
    		lineX = lineX.replace("£ £", "£.£");
    		//    		
    		this.numeroRegistros++;

    		if (!lineX.isEmpty()) {
    	    	//
    	    	String[] lineArray = lineX.split("£"); // ";" MR-20200416
    	    	
    	    	if (lineArray[0].equals("Apresentante"))
    	    		continue; // Ignora Header arquivo CSV !
    	    	
    	    	if (lineArray.length == 30)
    	    		assert(true); // nop
    	    	else {
    	    		//
    				MpFacesUtil.addErrorMessage("Linha com erro quantidade de colunas (30) ! ( Size = "
    							+ lineArray.length + " / Reg. = " + lineX+ " / Num.Linha = " + this.numeroRegistros);
    				break;
    	    	}
    	    	//
//    	    	System.out.println("lineX = ... ( " + lineX);
//    	    	
//    	    	int contX = 0;
//    	    	for (String fieldX : lineArray) {
//    	    		//
//        	    	System.out.println("fieldX = ... ( " + fieldX + " / contX = " + contX);
//        	    	
//        	    	contX++;
//    	    	}
    	    	//
    	    	try {
    	    		// Verifica se já existe registro por Data / NumeroProtocolo ...
        	    	MpAutorizaCancelamento mpAutorizaCancelamento =	null; // mpAutorizaCancelamentos.porDataNumeroProtocolo(
//        	    															sdfX.parse(lineArray[3]), lineArray[3]);
       	    	 	if (null == mpAutorizaCancelamento)
       	    	 		mpAutorizaCancelamento = new MpAutorizaCancelamento();
       	    	
        	    	mpAutorizaCancelamento.setCodigoOficio(this.mpCartorioOficioSel.getNumero());
        	    	mpAutorizaCancelamento.setApresentante(lineArray[0]);
        	    	mpAutorizaCancelamento.setCodigo(lineArray[1]);
        	    	
        	    	// Trata Numero Oficio ... = Ignorado ! (2º Ofício de Protesto de Títulos do Rio de Janeiro) ...
					if (lineArray[2].substring(0,1).equals(this.mpCartorioOficioSel.getNumero()))
						assert(true); 
					else {
						//
	    				MpFacesUtil.addErrorMessage("Numero Oficio X Registro Incompátives ! ( Oficio = "
														+ this.mpCartorioOficioSel.getNumero() + " / Reg. = " 
								    					+ lineX + " / Num.Linha = " + this.numeroRegistros);
	    				break;
					}
        	    	//
					mpAutorizaCancelamento.setData(sdfX.parse(lineArray[3]));
					
					//      01234567890
					// Ex.: 0000010547 -> 010547 !?
	    	    	mpAutorizaCancelamento.setProtocolo(lineArray[4].substring(4));
	    	    	
	    	    	mpAutorizaCancelamento.setDataProtocolo(sdfX.parse(lineArray[5]));
	    	    	mpAutorizaCancelamento.setDataRemessa(sdfX.parse(lineArray[6]));
	    	    	//
	    	    	mpAutorizaCancelamento.setAgenciaCodigoCedente(lineArray[7]);
	    	    	if (mpAutorizaCancelamento.getAgenciaCodigoCedente().isEmpty())
		    	    	mpAutorizaCancelamento.setAgenciaCodigoCedente(".");
	    	    	//
	    	    	mpAutorizaCancelamento.setCedente(lineArray[8]);
	    	    	mpAutorizaCancelamento.setSacador(lineArray[9]);
	    	    	mpAutorizaCancelamento.setDocumentoSacador(lineArray[10]);
	    	    	mpAutorizaCancelamento.setDevedor(lineArray[11]);
	    	    	mpAutorizaCancelamento.setDocumentoDevedor(lineArray[12]);
	    	    	mpAutorizaCancelamento.setEndereco(lineArray[13]);
	    	    	mpAutorizaCancelamento.setCep(lineArray[14]);
	    	    	//
	    	    	mpAutorizaCancelamento.setBairro(lineArray[15]);
	    	    	if (mpAutorizaCancelamento.getBairro().isEmpty())
		    	    	mpAutorizaCancelamento.setBairro(".");
	    	    	//
	    	    	mpAutorizaCancelamento.setCidade(lineArray[16]);
	    	    	mpAutorizaCancelamento.setUf(lineArray[17]);
	    	    	//
	    	    	mpAutorizaCancelamento.setNossoNumero(lineArray[18]);
	    	    	if (mpAutorizaCancelamento.getNossoNumero().isEmpty())
		    	    	mpAutorizaCancelamento.setNossoNumero(".");
	    	    	//
	    	    	mpAutorizaCancelamento.setNumeroTitulo(lineArray[19]);
	    	    	mpAutorizaCancelamento.setValor(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[20]))));
	    	    	mpAutorizaCancelamento.setSaldo(BigDecimal.valueOf(Double.parseDouble(trataNum(lineArray[21]))));
	    	    	mpAutorizaCancelamento.setEspecie(lineArray[22]);
	    	    	mpAutorizaCancelamento.setPracaProtesto(lineArray[23]);
	    	    	mpAutorizaCancelamento.setTipoAutorizacao(lineArray[24]);
	    	    	mpAutorizaCancelamento.setSituacao(lineArray[25]);
	    	    	mpAutorizaCancelamento.setImpresso(lineArray[26]);
	    	    	mpAutorizaCancelamento.setVencimento(lineArray[27]);
	    	    	mpAutorizaCancelamento.setOcorrencia(lineArray[28]);
	    	    	mpAutorizaCancelamento.setDataOcorrencia(sdfX.parse(lineArray[29]));
	    	    	//
	    	    	mpAutorizaCancelamentos.guardar(mpAutorizaCancelamento);
	    	    	//
				} catch (Exception e) {
					//
        	    	System.out.println("Error Exception lineX = ... ( contLineX = " + this.numeroRegistros + 
																				" / LineX = " + lineX + " / e = " + e);
        			MpFacesUtil.addErrorMessage("Error Exception lineX = ... ( contLineX = " + this.numeroRegistros +
        																		" / LineX = " + lineX + " / e = " + e);
        	    	break; // continue;
//					e.printStackTrace();
				}
    	    	//
    	    	// break;
    	    }
    	}
	    //
	    scX.close();
    	// ----------------------------------------------
    	
    	//
    	
//    	String rcSqlX = this.mpAutorizaCancelamentos.executeSQLs(isX, numReg);
//    	if (!rcSqlX.substring(0,2).equals("OK")) {
//    		//
//			MpFacesUtil.addErrorMessage("Erro_0007 - Erro na Carga Movimento AutorizaCancelamento ( rc = " + rcSqlX);
//			//
//			return;
//    	}

    	//
    	// Grava LOG ! 
    	// ==========
    	MpAutorizaCancelamentoCargaLog mpAutorizaCancelamentoCargaLog = new MpAutorizaCancelamentoCargaLog(); 
    	
    	mpAutorizaCancelamentoCargaLog.setDataGeracao(new Date());
    	mpAutorizaCancelamentoCargaLog.setNumeroOficio(codigoOficio);
    	mpAutorizaCancelamentoCargaLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
		mpAutorizaCancelamentoCargaLog.setNumeroRegistros(this.numeroRegistros);
    	//    	
    	mpAutorizaCancelamentoCargaLog = this.mpAutorizaCancelamentoCargaLogService.salvar(
    																			mpAutorizaCancelamentoCargaLog);
    	
		MpFacesUtil.addInfoMessage("Upload/Carga completa!... O arquivo " + this.file.getFileName() +
									" foi processado ! ( " + mpAutorizaCancelamentoCargaLog.getNumeroRegistros());
    }
    
    public void trataArquivoAutorizaCancelamento(InputStream isX, String codOficio) {
    	//
//    	MpAppUtil.PrintarLn("MpTesteBean.trataArquivoAutorizaCancelamento() ( codOficio = " + codOficio);

        String rcX = this.mpAutorizaCancelamentos.executeSQL(
        					"DELETE FROM mp_autoriza_cancelamento WHERE codigo_oficio = '" + codOficio + "'");
    	if (null == rcX || !rcX.equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Autoriza Cancelamento ( rc = " + rcX);
//	        MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento AutorizaCancelamento ( rc = " + rcX);
	        //
			return;
    	}
    	//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.trataArquivoAutorizaCancelamento() - 000");
    	//        
    	this.executaThread(isX);
        
    	if (indPBar)
    		this.trataProgressBar();
        
//    	this.executeSQLs(isX);
    	//
    	// Grava LOG ! 
    	// ==========
    	MpAutorizaCancelamentoCargaLog mpAutorizaCancelamentoCargaLog = new MpAutorizaCancelamentoCargaLog(); 
    	
    	mpAutorizaCancelamentoCargaLog.setDataGeracao(new Date());
    	mpAutorizaCancelamentoCargaLog.setNumeroOficio(codOficio);
    	mpAutorizaCancelamentoCargaLog.setUsuarioNome(this.mpSeguranca.getLoginUsuario());    	
		mpAutorizaCancelamentoCargaLog.setNumeroRegistros((long) this.quantidadeAutorizaCancelamentos);
    	//
    	mpAutorizaCancelamentoCargaLog = this.mpAutorizaCancelamentoCargaLogService.salvar(
    																			mpAutorizaCancelamentoCargaLog);
    	
		MpFacesUtil.addErrorMessage("Upload completo!... O arquivo " + this.file.getFileName() +
									" foi processado ! ( " + mpAutorizaCancelamentoCargaLog.getNumeroRegistros());
    }
    
    public String executeSQLs(InputStream isX) {
		//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.executeSQLs() - 000");

        Scanner scX = new Scanner(isX);
    	//
    	this.autorizaCancelamentos = new ArrayList<>();		

        Integer i = 0;
    		
    	while(scX.hasNextLine()) {
    		//    		
    		String sqlX = scX.nextLine();

    		// Trata ProgressBar...
    		i++;

    		this.autorizaCancelamentos.add("autorizaCancelamento" + (i + 1));
    		
    		this.mensagem = " Processando autorizaCancelamento : " + (i + 1) + " de " + 
    																		this.quantidadeAutorizaCancelamentos;

    		this.atualizarProgresso(i);
    		
    		if (i >= this.quantidadeAutorizaCancelamentos) break;
    		//
            try {
                Thread.sleep(200);
                //
            } catch (Exception ex) {
            	//
            }
    		//    		
    	    if (!sqlX.isEmpty()) this.mpAutorizaCancelamentos.executeSQL(sqlX);
    	    //
    	}
	    //
	    scX.close();
		//
		return "OK";
	}

    public String trataNum(String numero) {
		//
    	if (numero.indexOf("R$ ") >= 0)
    		numero = numero.replace("R$ ", "");

		// Ex.: R$ 2.111.970,28 => 2111970.28 OU
    	// Verifica se tá no formato (Linux?!)... Ex.: R$ 2,111,970.28  => 2111970.28 ... 
    	Integer contVirgulas = MpAppUtil.contadorOcorrencias(numero, ",");
    	
    	Boolean indPointDecimal = false;
    	if ( (numero.trim().length() < 6) && (contVirgulas == 0) )
    		indPointDecimal = true;
    	else
        	if ( (numero.trim().length() > 5) && (contVirgulas > 1) )
        		indPointDecimal = true;
    	//
    	if ( indPointDecimal ) {
    		//
        	if (numero.indexOf(".") >= 0)
        		numero = numero.replace(",", "");
        	if (numero.indexOf(".") >= 0)
        		numero = numero.replace(",", "");
    	} else {
    		//
        	if (numero.indexOf(".") >= 0)
        		numero = numero.replace(".", "");
        	if (numero.indexOf(".") >= 0)
        		numero = numero.replace(".", "");
        	if (numero.indexOf(",") >= 0)
        		numero = numero.replace(",", ".");
    	}    	
    	//
//    	System.out.println("MpCarregaAutorizaCancelamento.trataNumero() - ( " + numeroAnt + " / " + numero);
    	//
    	return numero;
    }

    public void trataProgressBar() {
		//
    	this.autorizaCancelamentos = new ArrayList<>();	

        for (int i = 0; i <= this.quantidadeAutorizaCancelamentos; i++) {
        	// 
        	
    		// Trata ProgressBar...
    		this.autorizaCancelamentos.add("autorizaCancelamento" + (i + 1));
    		
    		this.mensagem = " Processando autorizaCancelamento : " + (i + 1) + " de " + 
    																			this.quantidadeAutorizaCancelamentos;

    		this.atualizarProgresso(i);    		
    		//
//    		MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.trataProgressBar() - 000 ( " + i);

    		try {
                Thread.sleep(20);
                
            	if (indCancel) break;
                //
            } catch (Exception ex) {
            	//
            }
    		//    		
    	}
	    //
	}

    private String executaThread(InputStream isXX) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.executaThreadX() - 000");

//    	this.service = Executors.newSingleThreadExecutor();
    	this.service = Executors.newSingleThreadScheduledExecutor();

    	String dataYMDHM_INI = sdfYMDHM.format(new Date());

        Runnable task = new Runnable() {
        	//
            public void run() {
            	//
    	        try {
    	        	//    	        	
					Class.forName("org.hsqldb.jdbcDriver");
					
	    	        Connection conX = DriverManager.getConnection(
										"jdbc:hsqldb:file:~/db/mpProtRJCap/mpProtRJCap_DB", "sa", "");
					//
					Scanner scXX = new Scanner(isXX);
					//
					autorizaCancelamentos = new ArrayList<>();		
					
//					MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.executaThreadX() - 001");
					//
					while(scXX.hasNextLine()) {
						// 
			        	if (indCancel) break;
						
						String sqlXX = scXX.nextLine();						
						//
						if (sqlXX.isEmpty()) continue;
						
						if (sqlXX.indexOf("insert into mp_autorizaCancelamento (codigo_oficio") <  0) {
							//
							System.out.println(
								"MpAutorizaCancelamentoCargaThread.executaThread.run() - ERROR ( sqlX : " + sqlXX);
						  	continue;
						}
						//
//						MpAppUtil.PrintarLn("MpCarregaAutorizaCancelamentoBean.executaThreadX() - 002 ( " + sqlXX);

						PreparedStatement stmt = conX.prepareStatement(sqlXX);
						 
						stmt.executeUpdate();
						stmt.close();
						//
					}
					//
					conX.close();
					scXX.close();
					//
    	        } catch (ClassNotFoundException e) {
					//
					e.printStackTrace();
				} catch (SQLException e) {
					//
					e.printStackTrace();
				}
    	        //
            }
        };
        //
        this.service.execute(task);
 
        this.service.shutdown();
        //
    	String dataYMDHM_FIM = sdfYMDHM.format(new Date());

    	MpFacesUtil.addInfoMessage("Carga completada com sucesso ! (Tempo: " + dataYMDHM_INI + " / " + dataYMDHM_FIM);
		MpAppUtil.PrintarLn(
				"MpCarregaAutorizaCancelamentoBean.executaThread()... Carga completada com sucesso ! (Tempo: " + 
																			dataYMDHM_INI + " / " + dataYMDHM_FIM);
		//
		return "OK";
	}
 
    /**
     * cria uma mensagem que sera exibida para usuario atraves do componente 
     * p:messages
     * @param texto mensagem informada
     */
    private void criarMensagem(String texto) {
        //
        FacesMessage msg = new FacesMessage(texto);
        
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    //reseta o progresso e a mensagem do progressbar
    private void resetarProgresso() {
    	//
    	this.progresso = 0;
    	this.mensagem = "";
    }

    /**
     * atualiza o progresso
     * @param i posicao da autorizaCancelamento na lista
     */
    private void atualizarProgresso(int i) {
    	//
        //calculo para o percentual do processo em relacao a quantidade de autorizaCancelamentos
        this.progresso = (i * 100) / this.quantidadeAutorizaCancelamentos;
        
        try {
            Thread.sleep(50);
            //
        } catch (Exception ex) {
        }
    }

    /**
     * processa as autorizaCancelamentos,podendo ser adicionadas ou canceladas
     * @param acao 1 = autorizaCancelamentos serao processadas 2 = autorizaCancelamentos serao canceladas
     */
    public void processarAutorizaCancelamentos() {
    	//
        	this.resetarProgresso();
            //
        	try {
        		//
        		this.trataArquivoAutorizaCancelamento(this.file.getInputstream(), 
        																this.mpCartorioOficioSel.getNumero());
        		//
        	} catch (IOException e) {
        		//
                MpFacesUtil.addErrorMessage("MpCarregaAutorizaCancelamentoBean Erro_0001 - ( e = " + e);
        	}
            //
        	this.criarMensagem(
        		" Todas os " + quantidadeAutorizaCancelamentos + "  autorizaCancelamentos foram processados !");
            //
            this.indFile = false;
            
            this.resetarProgresso();
            //
    }

    public void cancelar() {
    	//    	
    	this.service.shutdown();
        //
        this.indFile = false;
        this.indCancel = true;

        this.resetarProgresso();

    	MpFacesUtil.addErrorMessage("Cancelamento Efetuado ! ");    	
    	MpAppUtil.PrintarLn("Cancelamento Efetuado ! ");
    }
    
    // ---
    
    public void addMessageCargaDiaria() {
    	//
        String summary = this.indCargaDiaria ? "Carga Diaria Ativada!" : "Carga Diaria Desativada! (3 Anos - Cuidado Apaga Tabela)";
        
		MpFacesUtil.addErrorMessage(summary);
    }
    
    // ---
    
    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }

    public Boolean getIndFile() { return indFile; }
    public void setIndFile(Boolean indFile) { this.indFile = indFile; }

    public Boolean getIndCancel() { return indCancel; }
    public void setIndCancel(Boolean indCancel) { this.indCancel = indCancel; }

    public Boolean getIndPBar() { return indPBar; }
    public void setIndPBar(Boolean indPBar) { this.indPBar = indPBar; }

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
	
    public Integer getProgresso() {
    	if (progresso == null) progresso = 0;
    	//
    	return progresso;
    }
    public void setProgresso(Integer progresso) { this.progresso = progresso; }

    public String getMensagem() {
    	if (mensagem == null) mensagem = "";
    	//
    	return mensagem;
    }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Integer getQuantidadeAutorizaCancelamentos() { return quantidadeAutorizaCancelamentos; }
    public void setQuantidadeAutorizaCancelamentos(Integer quantidadeAutorizaCancelamentos) { 
    										this.quantidadeAutorizaCancelamentos = quantidadeAutorizaCancelamentos; }

    public List<String> getAutorizaCancelamentos() { return autorizaCancelamentos; }
    public void setAutorizaCancelamentos(List<String> autorizaCancelamentos) { 
    															this.autorizaCancelamentos = autorizaCancelamentos; }
	
	public Boolean getIndCargaDiaria() { return indCargaDiaria; }
	public void setIndCargaDiaria(Boolean indCargaDiaria) { this.indCargaDiaria = indCargaDiaria; }
    
}