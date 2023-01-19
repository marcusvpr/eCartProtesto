package com.mpxds.mpbasic.controller.intima21;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacao;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@SessionScoped
public class MpPesquisaBoletoIntimacaosBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpBoletoIntimacaos mpBoletoIntimacaos;

	@Inject
	private MpSeguranca mpSeguranca;

	private MpCartorioOficio mpCartorioOficioSel;
	
	private MpCartorioOficio mpCartorioOficioX;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	
	private Date dataDe;
	private Date dataAte;
    private String numeroIntimacao;

	private SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private MpBoletoIntimacao mpBoletoIntimacaoSel;
	private List<MpBoletoIntimacao> mpBoletoIntimacaoList;
	
	private StreamedContent streamedContent;
	
	private String nomePdfResourceX = "BASE.PDF";

	private MpBoletoIntimacao mpBoletoIntimacaoSelDownX;
	private StreamedContent fileDownX;

    // ---
	
	public void init() {
		//
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;		
		//
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, -1);
		
		this.dataDe = dataX.getTime();

		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, +1);

		this.dataAte = dataX.getTime();

		this.mpBoletoIntimacaoList = new ArrayList<MpBoletoIntimacao>();
		//
	}

	public void pesquisaBoletoIntimacao() {
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
		if (this.numeroIntimacao.isEmpty()) {
			//
			this.mpBoletoIntimacaoList = this.mpBoletoIntimacaos.mpBoletoIntimacaoByOficioDataProtocoloList(
													this.mpCartorioOficioSel.getNumero(), this.dataDe, this.dataAte);
		} else {
			//
			this.numeroIntimacao = StringUtils.leftPad(this.numeroIntimacao.trim(), 6, "0");
			
			this.mpBoletoIntimacaoList = mpBoletoIntimacaos.mpBoletoIntimacaoByNumeroProtocoloList(
																mpCartorioOficioSel.getNumero(), this.numeroIntimacao);
		}
		//
		System.out.println("MpPesquisaBoletoIntimacaosBean.pesquisaBoletoIntimacao() ( " + this.numeroIntimacao +
				 					" / " + this.sdfYYYYMMDD.format(dataDe) + " / " + this.sdfYYYYMMDD.format(dataAte));
		//
	}

	public void exibeIntimacaoPDF() {
		//		
		this.nomePdfResourceX = "BASE.PDF";
		String fileNameZipX = this.nomePdfResourceX;

    	String nomePdfView = ""; 	

//    	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() ( " + 
//								mpBoletoIntimacaoSel.getNomeSacado() + " / " + mpBoletoIntimacaoSel.getBase64() +
//								" / pdf = " + nomePdfView);
    	//
    	try {
        	//
    		Resource pathResourceX = new ClassPathResource("/pdfs/BASE.PDF");

    		InputStream inputResourceX = pathResourceX.getInputStream();

    		File fileResourceX = pathResourceX.getFile();		
    		
    		String pathResourceXX = fileResourceX.getPath().replace("BASE.PDF", "");
    		
        	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() PathR = " + pathResourceXX +
        										" / " + fileResourceX.getPath() + " / " + fileResourceX.getName());
    				
        	Base64.Decoder dec = Base64.getDecoder();
            
            byte[] decbytesZIP = dec.decode(mpBoletoIntimacaoSel.getBase64()); // PDF.zipado !       	
        	
//          byte[] decbytes = MpAppUtil.deflateZIP(decbytesZIP); // PDF.unzipado !

            // 
        	nomePdfView = pathResourceXX + "MpBoletoIntimacao_View_" + mpSeguranca.getLoginUsuario() + "_" + 
        							this.sdfYYYYMMDD.format(new Date()) + "_" + 
        							mpBoletoIntimacaoSel.getNossoNumero().trim().replaceAll("/", "") + ".pdf.zip";     	

//        	nomePdfView = System.getProperty("user.home") + File.separator + "MpBoletoIntimacao_View_" + 
//        						mpSeguranca.getLoginUsuario() + "_" + this.sdfYYYYMMDD.format(new Date()) +
//        						"_" + mpBoletoIntimacaoSel.getNossoNumero().trim().replaceAll("/", "") + ".pdf.zip";     	

            File fileZipX = new File(nomePdfView);
            if (!fileZipX.exists()) {
            	//
                try ( FileOutputStream fos = new FileOutputStream(fileZipX); ) {
                	//
                	// To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
                	// String b64 = "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3==";
                	// byte[] decoder = Base64.getDecoder().decode(b64);

    	            fos.write(decbytesZIP);
    	            //
               } catch (Exception e) {
                	//
                	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() - PDF ( e1 = " + e); 
        			MpFacesUtil.addErrorMessage("Erro Gravação PDF ( e1 = " + e);
        			return;
                }
    	        //
            }
            
	        // Trata unzip File !
            File fileZipX1 = new File(nomePdfView);
            
//            this.fileNameZipX = System.getProperty("user.home") + File.separator + 
//            														MpAppUtil.capturaFileNameZip(fileZipX1);
            this.nomePdfResourceX = MpAppUtil.capturaFileNameZip(fileZipX1);
            
            fileNameZipX = pathResourceXX + this.nomePdfResourceX;
            File fileUnZipX = new File(fileNameZipX);

//        	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF()  ( ZipExiste = " + 
//        				fileZipX1.exists() + " / UnZipExiste =  " + fileUnZipX.exists() + " / " + fileUnZipX); 
            //           
            if (fileZipX1.exists()) {
            	//
                if (!fileUnZipX.exists())
                	MpAppUtil.unzipaFile(pathResourceXX, fileZipX1);
                //
            }
	        //       
	        InputStream in = new FileInputStream(fileNameZipX);

            this.streamedContent = new DefaultStreamedContent(in, "application/pdf");

            //---	            
	        Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	        byte[] b = (byte[]) session.get("reportBytes");
	        
	        if (b != null) {
	        	//
	            this.streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(b), "application/pdf");
	        }
                //
        } catch (Exception e) {
        	//
        	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() - PDF ( e2 = " + e); 
			MpFacesUtil.addErrorMessage("Erro Geração PDF ( e2 = " + e);
        }
	}
            
	// ---
	
	public List<MpBoletoIntimacao> getMpBoletoIntimacaoList() { return mpBoletoIntimacaoList; }
	public void setMpBoletoIntimacaoList(List<MpBoletoIntimacao> mpBoletoIntimacaoList) { 
																this.mpBoletoIntimacaoList = mpBoletoIntimacaoList; }

	public MpBoletoIntimacao getMpBoletoIntimacaoSel() { return mpBoletoIntimacaoSel; }
	public void setMpBoletoIntimacaoSel(MpBoletoIntimacao mpBoletoIntimacaoSel) { 
																this.mpBoletoIntimacaoSel = mpBoletoIntimacaoSel; }
		
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }

	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }

	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }

	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																	this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
	
	//
	
    public StreamedContent getStreamedContent() {
    	//
        if (FacesContext.getCurrentInstance().getRenderResponse())
            return new DefaultStreamedContent();
        else
            return streamedContent;
       //
    }
    public void setStreamedContent(StreamedContent streamedContent) { this.streamedContent = streamedContent; }
	
    public String getNomePdfResourceX() { return nomePdfResourceX; }
	public void setNomePdfResourceX(String nomePdfResourceX) { this.nomePdfResourceX = nomePdfResourceX; }
	
	//
	
    public StreamedContent getFileDownX() throws FileNotFoundException {
    	//
    	String nomePdfView = ""; 	

    	System.out.println("MpPesquisaBoletoIntimacaosBean.getFileDownX() ( " + 
						mpBoletoIntimacaoSelDownX.getNomeSacado() + " / " + mpBoletoIntimacaoSelDownX.getBase64() +
						" / pdf = " + nomePdfView);
    	//
    	try {
        	//
    		Base64.Decoder dec = Base64.getDecoder();
        
    		byte[] decbytesZIP = dec.decode(mpBoletoIntimacaoSelDownX.getBase64()); // PDF.zipado !       	
    	
    		nomePdfView = System.getProperty("user.home") + File.separator + "MpBoletoIntimacao_View_" + 
    						mpSeguranca.getLoginUsuario() + "_" + this.sdfYYYYMMDD.format(new Date()) +
    						"_" + mpBoletoIntimacaoSelDownX.getNossoNumero().trim().replaceAll("/", "") + ".pdf.zip";
    		//
            File fileZipX = new File(nomePdfView);
            if (!fileZipX.exists()) {
            	//
                try ( FileOutputStream fos = new FileOutputStream(fileZipX); ) {
                	//
                	// To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
                	// String b64 = "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3==";
                	// byte[] decoder = Base64.getDecoder().decode(b64);

    	            fos.write(decbytesZIP);
    	            //
               } catch (Exception e) {
                	//
                	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() - PDF ( e1 = " + e); 
        			MpFacesUtil.addErrorMessage("Erro Gravação PDF ( e1 = " + e);
        			return null;
                }
    	        //
            }
            //
	        // Trata unzip File !
            File fileZipX1 = new File(nomePdfView);
            
//           this.fileNameZipX = System.getProperty("user.home") + File.separator + 
//            														MpAppUtil.capturaFileNameZip(fileZipX1);
            this.nomePdfResourceX = MpAppUtil.capturaFileNameZip(fileZipX1);
            
            String fileNameZipX = System.getProperty("user.home") + File.separator + this.nomePdfResourceX;
            File fileUnZipX = new File(fileNameZipX);

        	System.out.println("MpPesquisaBoletoIntimacaosBean.getFileDownX  ( ZipExiste = " + 
        				fileZipX1.exists() + " / UnZipExiste =  " + fileUnZipX.exists() + " / " + fileUnZipX); 
            //           
            if (fileZipX1.exists()) {
            	//
                if (!fileUnZipX.exists())
                	MpAppUtil.unzipaFile(System.getProperty("user.home") + File.separator, fileZipX1);
                //
            }
    		//
            InputStream stream = new FileInputStream(fileNameZipX);

            this.fileDownX = new DefaultStreamedContent(stream, "application/pdf", this.nomePdfResourceX);
            //
        } catch (Exception e) {
        	//
        	System.out.println("MpPesquisaBoletoIntimacaosBean.exibeIntimacaoPDF() - PDF ( e1 = " + e); 
			MpFacesUtil.addErrorMessage("Erro Gravação PDF ( e1 = " + e);
			//
			return null;
        }
    	//        
        return this.fileDownX;
    }

    public void download(MpBoletoIntimacao mpBoletoIntimacaoSelDownX) {
    	//
        this.mpBoletoIntimacaoSelDownX = mpBoletoIntimacaoSelDownX;
    }	
	
}