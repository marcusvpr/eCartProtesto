package com.mpxds.mpbasic.controller;

import java.io.IOException;
import java.io.Serializable;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
//import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.security.MpUsuarioSistema;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

//import com.mpxds.mpbasic.model.MpBoletoLog;
//import com.mpxds.mpbasic.model.vo.MpCampoValor;
//import com.mpxds.mpbasic.repository.MpBoletoLogs;

@Named
@SessionScoped
public class MpHomeBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
//	@Inject
//	private MpBoletoLogs mpBoletoLogs;	
//	
//	private Map<String, List<MpCampoValor>> mappedList;
//	
//	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpUsuarioService mpUsuarioService;
	
	//
	
	private Boolean indBoleto = false;
	private Boolean indTitulo = false;
		
	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.paramOrigem}")
	private String paramOrigem;

	//
	
	private String opcaoCpfCnpj = "cpf";
	private String cpfCnpjReg;
	
    // ---
	
	public MpHomeBean() {
		//
//        this.reload();
	}

	public void init() {
		//
		if (null == this.paramOrigem) paramOrigem = "null";
		//
		if (this.paramOrigem.equals("Boleto"))
			this.indBoleto = true;
		else
			if (this.paramOrigem.equals("Titulo"))
				this.indTitulo = true;
		//
		MpUsuarioSistema usuarioLogado = this.mpSeguranca.getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			//
			if (null == usuarioLogado.getMpUsuario().getCpfCnpj() 
			||  usuarioLogado.getMpUsuario().getCpfCnpj().isEmpty()) {
				//
				RequestContext requestContext = RequestContext.getCurrentInstance();
		        
		        requestContext.update(":mpHomeFormId");
		        requestContext.execute("PF('scDialogCpfCnpj').show();");
			}
		}
		//
//		System.out.println("MpHomeBean.init() ( " + this.paramOrigem + " / logX = " + logX);
	}
	
	public boolean isIndBoleto() {
		//
		if (this.indBoleto) return true;
		
		MpUsuarioSistema usuarioLogado = mpSeguranca.getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			this.indBoleto = usuarioLogado.getMpUsuario().getIndBoleto();
		}
		//
		return this.indBoleto;
	}
	
	public boolean isIndTitulo() {
		//
		if (this.indTitulo) return true;
		
		MpUsuarioSistema usuarioLogado = mpSeguranca.getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			this.indTitulo = usuarioLogado.getMpUsuario().getIndTitulo();
		}
		//
		return this.indTitulo;
	}
	
    public void callEmissaoBoleto() {
    	//
//      System.out.println("MpHomeBean.callEmissaoBoleto()... entrou F10 action !");
        
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		String url = extContext.encodeActionURL(extContext.getRequestContextPath() + "/EmissaoBoleto");
		//
		try {
			extContext.redirect(url);
			//
		} catch (IOException e) {
			MpFacesUtil.addErrorMessage(e.getMessage());
		}    	
    }
    
	public void registrar() {
		//
//		System.out.println("MpHomeBean.registrar() ( " + this.opcaoCpfCnpj);

		if (null == this.cpfCnpjReg || this.cpfCnpjReg.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("Informar Documento CPF ou CNPJ !");			
			return;
		}
		// Valida CPF/CNPJ ...
//		System.out.println("MpHomeBean.registrar() ( " + this.cpfCnpjReg + " / " + this.opcaoCpfCnpj);
		
		if (this.opcaoCpfCnpj.toLowerCase().equals("cpf")) {
			// Retira pontos e traços do CPF !
			String cpfX = this.cpfCnpjReg.trim().replace(".", "");		
			cpfX = cpfX.replace("-", "");

			if (!MpAppUtil.isValidCPF(cpfX)) {
				//
				MpFacesUtil.addErrorMessage("CPF Inválido ! ( " + this.cpfCnpjReg);			
				return;
			}
		} else {
			//
			// Retira pontos e traços do CPF !
			String cnpjX = this.cpfCnpjReg.trim().replace(".", "");		
			cnpjX = cnpjX.replace("/", "");
			cnpjX = cnpjX.replace("-", "");

			if (!MpAppUtil.isValidCNPJ(cnpjX)) {
				//
				MpFacesUtil.addErrorMessage("CNPJ Inválido ! ( " + this.cpfCnpjReg);	
				return;
			}
		}
		//
		MpUsuario mpUsuario = this.mpSeguranca.getMpUsuarioLogado().getMpUsuario();

		mpUsuario.setCpfCnpj(this.cpfCnpjReg);
		
		mpUsuario = this.mpUsuarioService.salvarSite(mpUsuario);
		//
		MpFacesUtil.addErrorMessage("CPF / CNPJ ... alterado com sucesso ! ( " + this.cpfCnpjReg);

		// Fecha Dialog ...
		RequestContext requestContext = RequestContext.getCurrentInstance();
        
		requestContext.update(":mpHomeFormId");
		requestContext.execute("PF('scDialogCpfCnpj').hide();");
	}
		
//    private void reload() {
//    	//
//		Calendar dataDe = Calendar.getInstance();
//		
//		dataDe.setTime(new Date());
//		dataDe.add(Calendar.DAY_OF_MONTH, -14);
//		
//        List<MpBoletoLog> mpBoletoLog1OfList = new ArrayList<MpBoletoLog>(); // this.mpBoletoLogs.mpBoletoLogByDataNumOficioList(dataDe.getTime(), "1");
//        List<MpBoletoLog> mpBoletoLog2OfList = new ArrayList<MpBoletoLog>(); // this.mpBoletoLogs.mpBoletoLogByDataNumOficioList(dataDe.getTime(), "2");
//        List<MpBoletoLog> mpBoletoLog3OfList = new ArrayList<MpBoletoLog>(); // this.mpBoletoLogs.mpBoletoLogByDataNumOficioList(dataDe.getTime(), "3");
//        List<MpBoletoLog> mpBoletoLog4OfList = new ArrayList<MpBoletoLog>(); // this.mpBoletoLogs.mpBoletoLogByDataNumOficioList(dataDe.getTime(), "4");
//
//        List<MpCampoValor> mpCampoValor1OfList = new ArrayList<MpCampoValor>();
//        List<MpCampoValor> mpCampoValor2OfList = new ArrayList<MpCampoValor>();
//        List<MpCampoValor> mpCampoValor3OfList = new ArrayList<MpCampoValor>();
//        List<MpCampoValor> mpCampoValor4OfList = new ArrayList<MpCampoValor>();
//
//        String dataAntX = "";
//        BigDecimal contX = BigDecimal.ZERO;
//        //	
//        for (MpBoletoLog mpBoletoLog : mpBoletoLog1OfList) {
//        	//
//        	if (DATE_FORMAT.format(mpBoletoLog.getDataGeracao()).equals(dataAntX))
//        		contX.add(new BigDecimal(1));
//        	else {
//        		if (!dataAntX.isEmpty()) {
//        			MpCampoValor mpCampoValor = new MpCampoValor(dataAntX, contX);        			
//        			mpCampoValor1OfList.add(mpCampoValor);
//        		}
//        		contX = BigDecimal.ZERO;
//    			dataAntX = DATE_FORMAT.format(mpBoletoLog.getDataGeracao());
//        	}        		
//        }
//        //
//        dataAntX = "";
//        contX = BigDecimal.ZERO;
//        for (MpBoletoLog mpBoletoLog : mpBoletoLog2OfList) {
//        	//
//        	if (DATE_FORMAT.format(mpBoletoLog.getDataGeracao()).equals(dataAntX))
//        		contX.add(new BigDecimal(1));
//        	else {
//        		if (!dataAntX.isEmpty()) {
//        			MpCampoValor mpCampoValor = new MpCampoValor(dataAntX, contX);        			
//        			mpCampoValor2OfList.add(mpCampoValor);
//        		}
//        		contX = BigDecimal.ZERO;
//    			dataAntX = DATE_FORMAT.format(mpBoletoLog.getDataGeracao());
//        	}        		
//        }
//        //
//        dataAntX = "";
//        contX = BigDecimal.ZERO;
//        for (MpBoletoLog mpBoletoLog : mpBoletoLog3OfList) {
//        	//
//        	if (DATE_FORMAT.format(mpBoletoLog.getDataGeracao()).equals(dataAntX))
//        		contX.add(new BigDecimal(1));
//        	else {
//        		if (!dataAntX.isEmpty()) {
//        			MpCampoValor mpCampoValor = new MpCampoValor(dataAntX, contX);        			
//        			mpCampoValor3OfList.add(mpCampoValor);
//        		}
//        		contX = BigDecimal.ZERO;
//    			dataAntX = DATE_FORMAT.format(mpBoletoLog.getDataGeracao());
//        	}        		
//        }
//        //
//        dataAntX = "";
//        contX = BigDecimal.ZERO;
//        for (MpBoletoLog mpBoletoLog : mpBoletoLog4OfList) {
//        	//
//        	if (DATE_FORMAT.format(mpBoletoLog.getDataGeracao()).equals(dataAntX))
//        		contX.add(new BigDecimal(1));
//        	else {
//        		if (!dataAntX.isEmpty()) {
//        			MpCampoValor mpCampoValor = new MpCampoValor(dataAntX, contX);        			
//        			mpCampoValor4OfList.add(mpCampoValor);
//        		}
//        		contX = BigDecimal.ZERO;
//    			dataAntX = DATE_FORMAT.format(mpBoletoLog.getDataGeracao());
//        	}        		
//        }
//        //
//        mappedList.put("1ºOf", mpCampoValor1OfList);
//        mappedList.put("2ºOf", mpCampoValor2OfList);
//        mappedList.put("3ºOf", mpCampoValor3OfList);
//        mappedList.put("4ºOf", mpCampoValor4OfList);
//    }	
	
	// ---
		
//    public Map<String, List<MpCampoValor>> getMappedList() { return mappedList; }
	
	public Boolean getIndBoleto() { return indBoleto; }
	public void setIndBoleto(Boolean indBoleto) { this.indBoleto = indBoleto; }
  
	public Boolean getIndTitulo() { return indTitulo; }
	public void setIndTitulo(Boolean indTitulo) { this.indTitulo = indTitulo; }
	  
	public String getParamOrigem() { return paramOrigem; }
	public void setParamOrigem(String paramOrigem) { this.paramOrigem = paramOrigem; }

	//
	   
	public String getOpcaoCpfCnpj() { return opcaoCpfCnpj; }
	public void setOpcaoCpfCnpj(String opcaoCpfCnpj) { this.opcaoCpfCnpj = opcaoCpfCnpj; }

	public String getCpfCnpjReg() { return cpfCnpjReg; }
	public void setCpfCnpjReg(String cpfCnpjReg) { this.cpfCnpjReg = cpfCnpjReg; }

}