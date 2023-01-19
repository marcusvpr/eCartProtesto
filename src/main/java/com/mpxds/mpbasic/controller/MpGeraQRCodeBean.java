package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
//import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.util.MpGenerateBarCode;
//import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
//@ManagedBean
@SessionScoped
public class MpGeraQRCodeBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private StreamedContent arquivoContent = new DefaultStreamedContent();
	
	private UploadedFile fileX;	
	private File fileXX;	

	private String pathY;
	private String msgX;
		
	// ======================================
	
	public void preRender() {
		//
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		File fileB = new File(extContext.getRealPath(File.separator + "resources" +
															File.separator + "pdfs" + File.separator + "BASE.PDF"));
				
		this.pathY = fileB.getPath() + " | " + fileB.getAbsolutePath();
		//
	}
	
	// -------- Trata Upload PDF ...
	
	public void handleFileUpload(FileUploadEvent event) {
		//
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		
		File fileB = new File(extContext.getRealPath(File.separator + "resources" +
															File.separator + "pdfs" + File.separator + "BASE.PDF"));
		
		String pathX = fileB.getPath().substring(0, fileB.getPath().length() - 8);		
		//
		try {
			//
			this.fileX = event.getFile();
			
			this.arquivoContent = new DefaultStreamedContent(event.getFile().getInputstream(),
												"application/pdf", event.getFile().getFileName());
			//
//			String pathX = extContext.getRealPath("//resources//") + "pdfs" + File.separator;
			String arquivoX = event.getFile().getFileName();
			//
			System.out.println("MpGenerateBarCode.handleFileUpload() - 000 (PathX =  " + pathX + " ( ArqX.= " + arquivoX);
					
//			byte[] bytes = IOUtils.toByteArray(this.arquivoContent.getStream());
			
//			this.criaArquivo(bytes, pathX + arquivoX);
//			this.criaArquivo(this.fileX.getContents(), pathX + arquivoX);
			
			//
			this.criaArquivo(this.fileX, pathX + arquivoX);
			
			this.fileXX = new File(pathX + arquivoX);
			
			MpGenerateBarCode.atualizaPDF(this.fileXX, 405, 710);
			
//		    extContext.redirect("http://localhost:8080/mpProtesto/resources/pdfs/out_" + arquivoX);
		    extContext.redirect("http://www.protestorjcapital.com.br:8080/apps/resources/pdfs/out_" + arquivoX);
			//
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
	}
	
	public void criaArquivo(byte[] bytes, String pathArquivoX) {
		//
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(pathArquivoX);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
		
	public void criaArquivo(UploadedFile fileX, String pathArquivoX) {
		//
	    InputStream input = null;
	    OutputStream output = null;

	    try {
		    input = fileX.getInputstream();
		    output = new FileOutputStream(new File(pathArquivoX));

		    IOUtils.copy(input, output);
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        IOUtils.closeQuietly(input);
	        IOUtils.closeQuietly(output);
	    }		
	}
	
	// ---------------------------
    
	public StreamedContent getArquivoContent() { return arquivoContent; }
    public void setArquivoContent(StreamedContent arquivoContent) { this.arquivoContent = arquivoContent; }
    
	public UploadedFile getFileX() { return fileX; }
	public void setFileX(UploadedFile fileX) { this.fileX = fileX; }    

	public String getPathY() { return pathY; }
	public void setPathY(String newPathY) { this.pathY = newPathY; }
	
	public String getMsgX() { return msgX; }
	public void setMsgX(String newMsgX) { this.msgX = newMsgX; }
	
}