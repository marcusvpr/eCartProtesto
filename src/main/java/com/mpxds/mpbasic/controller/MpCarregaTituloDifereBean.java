package com.mpxds.mpbasic.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
@ManagedBean
public class MpCarregaTituloDifereBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpTitulos mpTitulos;
	
	// ---
	
	private Integer contRegistro;
	
	private List<MpTitulo> mpTituloDifereList = new ArrayList<MpTitulo>();
	
	// ---
            
	@PostConstruct
	public void postConstruct() {
	  	//
		this.contRegistro = 0;
		//			
    }
	
    public void trataCarga() {
    	//
    	String arquivo_1 = "arquivos/status_titulo_1-1.Sql";
    	String arquivo_2 = "arquivos/status_titulo_1-2.Sql";
    	String arquivo_3 = "arquivos/status_titulo_1-3.Sql";    	
        //
        String rcX = this.mpTitulos.executeSQL("DELETE FROM mp_titulo WHERE codigo_oficio = '1'");
    	if (null == rcX || !rcX.equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX);
	        //
			return;
    	}
    	//
        this.trataArquivoTitulo(arquivo_1); 
        this.trataArquivoTitulo(arquivo_2); 
        this.trataArquivoTitulo(arquivo_3); 
		//
		MpFacesUtil.addInfoMessage("Carga Completada com Sucesso: ( " + this.contRegistro);
		//
        this.trataDiferenca(); 
    }
    
    public void trataArquivoTitulo(String arquivo) {
    	//
    	ClassLoader classLoader = getClass().getClassLoader();
        
        File fileDir = new File(classLoader.getResource(arquivo).getFile());
        //
    	try {
    		//
    		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

    		String str;

    		while ((str = in.readLine()) != null) {
    			//
    			this.contRegistro++;
    			
        	    if (!str.isEmpty()) this.mpTitulos.executeSQL(str);

    		    System.out.println(this.contRegistro + " - " + str);
    		}
    		//    		
    		in.close();
    		//
    	} catch (UnsupportedEncodingException e) {
    		System.out.println(e.getMessage());
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	//        
    }

    public void trataDiferenca() {
    	//
        List<MpTitulo> mpTituloList = mpTitulos.mpTituloNumeroDataProtocoloList("1");
    	
        MpAppUtil.PrintarLn("MpCarregaTituloDifereBean.trataDiferenca() - 000 ( List.Size = " +
        																			mpTituloList.size());

    	MpTitulo mpTituloAnt = new MpTitulo() ;
    	
    	String numeroProtocoloAnt = "";
    	String statusAnt = "";
    	//
    	for (MpTitulo mpTitulo : mpTituloList) {
    		//
            MpAppUtil.PrintarLn("MpCarregaTituloDifereBean.trataDiferenca() ( Num = " + numeroProtocoloAnt +
					" / Status = " + statusAnt);

            if (numeroProtocoloAnt.equals("")) {
    	    	//
    			mpTituloAnt = mpTitulo;
    	    	mpTituloAnt.setComplemento(mpTitulo.getStatus());            	    	
    	    	//
    			numeroProtocoloAnt = mpTitulo.getNumeroProtocolo();
    	    	statusAnt = mpTitulo.getStatus();
    		} else {
    			if (numeroProtocoloAnt.equals(mpTitulo.getNumeroProtocolo())) {
    				//
        	    	if (!statusAnt.equals(mpTitulo.getStatus())) {
        	    		//
            	    	statusAnt = mpTitulo.getStatus();
            	    	
            	    	mpTituloAnt.setComplemento(mpTituloAnt.getComplemento() + " / " + mpTitulo.getStatus());            	    	
        	    	}
    			} else {
    				//
    				if (mpTituloAnt.getComplemento().indexOf(" / ") >=0 )
    					this.mpTituloDifereList.add(mpTituloAnt);
    				//
        	    	mpTituloAnt = mpTitulo;
        	    	mpTituloAnt.setComplemento(mpTitulo.getStatus());            	    	
        	    	//
        			numeroProtocoloAnt = mpTitulo.getNumeroProtocolo();
        	    	statusAnt = mpTitulo.getStatus();    				
    			}
    		}
    			
    	}
    }
        
    // ---
    
	public Integer getContRegistro() { return contRegistro; }
	public void setContRegistro(Integer contRegistro) { this.contRegistro = contRegistro; }
    
	public List<MpTitulo> getMpTituloDifereList() { return mpTituloDifereList; }
	public void setMpTituloDifereList(List<MpTitulo> mpTituloDifereList) { 
																		this.mpTituloDifereList = mpTituloDifereList; }
    
}