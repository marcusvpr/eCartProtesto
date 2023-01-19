package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.service.util.MpUtilService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

//import io.minio.MinioClient;
//import io.minio.errors.MinioException;
 
@Named
@ViewScoped
@ManagedBean
public class MpCarregaCertidaoEletronicaBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpCargaControleService mpCargaControleService;

	@Inject
	private MpUtilService mpUtilService;
	
	// ---
	
    private UploadedFile file;
    
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
    
	private Date dataMovimento = new Date();	
    private Boolean indFile;
        
    // ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		//
//		if (null == MpAppUtil.getFileFromURL("marcus_teste.PDF"))
//			MpAppUtil.PrintarLn("MpCarregaCertidaoEletronicaBean.postConstruct() ( URL =  NULL!");
//		else
//			MpAppUtil.PrintarLn("MpCarregaCertidaoEletronicaBean.postConstruct() ( URL =  " +
//        												MpAppUtil.getFileFromURL("marcus_teste.PDF").getAbsolutePath());
    }
	
    public void upload(FileUploadEvent event) {
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
    	String msgX = "";
        if (null == this.mpCartorioOficioSel)
        	msgX = msgX + "( Selecionar Cartório ! )";
    	//
        this.file = event.getFile();
        if (null == this.file)
        	msgX = msgX + "( Selecionar Arquivo ! )";
        
        // =======================================//
        // Trata Carga Controle ! MR-2020-05-30 ! //
        // =======================================//
        String msgX1 = this.mpCargaControleService.validaCargaControle(this.mpCartorioOficioSel.getNumero());
		//
        if (!msgX1.isEmpty())
        	msgX = msgX + "( " + msgX1 + " )";
        //
        if (!msgX.isEmpty()) {
        	//
        	MpFacesUtil.addErrorMessage(msgX);
	    	return;
        }
        //
        this.indFile = true;

        MpFacesUtil.addInfoMessage("Upload OK ! ( Nome Arquivo = " + this.file.getFileName() + 
        															" / Tam.Arquivo = " + this.file.getSize());        
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
    	String msgX = "";
        if (null == this.mpCartorioOficioSel)
        	msgX = msgX + "( Selecionar Cartório ! )";

        if (null == this.file)
        	msgX = msgX + "( Selecionar Arquivo ! )";
        
        if (null == this.dataMovimento)
        	msgX = msgX + "( Selecionar Data Movimento ! )";
        
        // =======================================//
        // Trata Carga Controle ! MR-2020-05-30 ! //
        // =======================================//
        String msgX1 = this.mpCargaControleService.validaCargaControle(this.mpCartorioOficioSel.getNumero());
		//
        if (!msgX1.isEmpty())
        	msgX = msgX + "( " + msgX1 + " )";
        //
        if (!msgX.isEmpty()) {
        	//
        	MpFacesUtil.addErrorMessage(msgX);
	    	return;
        }
        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    	String pathDate = sdf.format(this.dataMovimento);  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		File fileX = null;
		Path folder = null;
		Path filePath = null;        
        //
		try {
			//
			fileX = new File(extContext.getRealPath(File.separator + "resources" +
					File.separator + "of" + this.mpCartorioOficioSel.getNumero() +
					File.separator + "certidaoEletronica" +
					File.separator));
			folder = Paths.get(fileX.getAbsolutePath());
			if (!fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +	extContext.getRealPath(
	    			"//resources//pdfs//of" + this.mpCartorioOficioSel.getNumero() + "//certidaoEletronica// ( e = " + e));
	    	return;
		}
		//
		try {
			//
			fileX = new File(extContext.getRealPath(File.separator + "resources" +
					File.separator + "of" + this.mpCartorioOficioSel.getNumero() +
					File.separator + "certidaoEletronica" +
					File.separator + pathDate.substring(0,4) + 
					File.separator + pathDate.substring(5) +
					File.separator));
			
			folder = Paths.get(fileX.getAbsolutePath());
			if (!fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +	extContext.getRealPath(
	    			"//resources//of" + this.mpCartorioOficioSel.getNumero() + "//certidaoEletronica//" + 
	    			pathDate.substring(0,4) + "//" + pathDate.substring(5) + "// ( e = " + e));
	    	return;
		}
		//
        try {
			//
        	File fileXX = new File(fileX.getAbsolutePath() + File.separator + this.file.getFileName());

        	folder = Paths.get(fileX.getAbsolutePath() + File.separator + this.file.getFileName());
        	
			if (!fileXX.exists())
				filePath = Files.createFile(folder);
			//
			try (InputStream input = this.file.getInputstream()) {
				//
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			//
//			this.quantidadeTitulos = (int) Files.lines(filePath).count();			
		}
		catch(Exception e) {
			//
//			e.printStackTrace();
	    	MpFacesUtil.addErrorMessage("Erro Arquivo... (file = " + folder.toString() + " / e = " + e);
	        MpAppUtil.PrintarLn("Erro Arquivo... (file = " + folder.toString() + " / e = " + e);
	    	return;
		}
        
        // Trata carga Arquivo ... S3.Minio (SaveInCloud) !
//        this.mpUtilService.fileUploader(fileX.getAbsolutePath(), this.file.getFileName());
        
        //
        MpFacesUtil.addInfoMessage("Processamento OK ! ( Data Movimento = " + sdf.format(this.dataMovimento) +
        										" / Nome Arquivo = " + this.file.getFileName() + 
        										" / TamanhoArquivo = " + this.file.getSize());        
    }
        
    // --- SaveInCloud S3.minio !
    public void mpFileUploaderS3Minio() {
    	//
//	    try {
	      // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
//	      MinioClient minioClient = new MinioClient(this.mpSeguranca.getSicMinioS3URL(),
//    									this.mpSeguranca.getSicMinioS3User(), this.mpSeguranca.getSicMinioS3Passw());
//	      // Check if the bucket already exists.
//	      boolean isExist =  minioClient.bucketExists("bucket-protrjcapital");
//	      if (isExist)
//	        System.out.println("MpCarregaCertidaoEletronicaBean.mpFileUploaderS3Minio() - Bucket already exists.");
//	      else {
//	    	  //
//	    	  // Make a new bucket called bucket-protrjcapital to hold a PDFs file of Certidao Eletronicas !.
//	    	  minioClient.makeBucket(MakeBucketArgs.builder().bucket("bucket-protrjcapital").build());
//	      }
	      //
//	      String pathX = "bucket-protrjcapital/of/" + this.mpCartorioOficioSel.getNumero() + "/certidao-eletronica/";

	      // Upload the zip file to the bucket with putObject
//	      minioClient.putObject(pathX , "marcus_teste.PDF", 
//    		"C:\\Users\\Fred\\Downloads\\Fwd__Certidão_Eletrônica_-_E-CARTORIO\\marcus_teste.PDF");
	      
	      System.out.println("C:/Users/Fred/Downloads/Fwd__Certidão_Eletrônica_-_E-CARTORIO/marcus_teste.PDF" + 
	    		  					" is successfully uploaded as 'marcus_teste.PDF' to 'bucket-protrjcapital' bucket.");
	      //
//	    } catch(MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | 
//																		      IOException | XmlPullParserException e) {
//	    	//
//	    	System.out.println("Error occurred: " + e);
//	    }
    	//
   	}    

    // ---
    
    public Date getDataMovimento() { return dataMovimento; }
    public void setDataMovimento(Date dataMovimento) { this.dataMovimento = dataMovimento; }
    
    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }

    public Boolean getIndFile() { return indFile; }
    public void setIndFile(Boolean indFile) { this.indFile = indFile; }

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