package com.mpxds.mpbasic.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.gson.Gson;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;

import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.security.MpSeguranca;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
public class MpBaixaBoletoImpressoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpSeguranca mpSeguranca;
	
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	private MpCartorioOficio mpCartorioOficioX;
	
	private Date dataDe;
	private Date dataAte;
	
	private StreamedContent file;	
     
	private Boolean indSQL = false; 
	private Boolean indTXT = true; 
	
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat sdfYMD1 = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	// ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		//		
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(new Date());
//		dataX.add(Calendar.DAY_OF_MONTH, -14);
		this.dataDe = dataX.getTime();

		dataX.setTime(new Date());
//		dataX.add(Calendar.DAY_OF_MONTH, +1);
		this.dataAte = dataX.getTime();
		//
		this.indSQL = false;
		this.indTXT = true;
    }

	// ---
	
	public void setFile(StreamedContent file) {
		//
        this.file = file;
    }

	public StreamedContent getFile() { 
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
		String msg = "";
		if (null == this.mpCartorioOficioSel) 
			msg = msg + " (Selecionar Oficio) ";
		if (null == this.dataDe) 
			msg = msg + " (Selecionar Data De) ";
		if (null == this.dataAte) 
			msg = msg + " (Selecionar Data Até) ";
		if (this.dataDe.after(this.dataAte)) 
			msg = msg + " (Data De maior Data Até) = " + sdfDMY.format(this.dataDe) + " - " + sdfDMY.format(this.dataAte);
		//
		if (!msg.isEmpty()) {
			MpFacesUtil.addErrorMessage("Error : " + msg);
			return null;
		}
        //
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(this.dataDe);
		dataX.set(Calendar.HOUR_OF_DAY, 0);
		dataX.set(Calendar.MINUTE, 0);
		dataX.set(Calendar.SECOND, 0);
		this.dataDe = dataX.getTime();

		dataX.setTime(this.dataAte);
		dataX.set(Calendar.HOUR_OF_DAY, 23);
		dataX.set(Calendar.MINUTE, 59);
		dataX.set(Calendar.SECOND, 59);
		this.dataAte = dataX.getTime();
		//
//		String arquivoBoleto = mpSeguranca.getLoginUsuario() + "_mpBoletoImpresso.json";
//		
//    	if (this.indSQL) 
//    		arquivoBoleto = mpSeguranca.getLoginUsuario() + "_mpBoletoImpresso.sql";
//    	if (this.indTXT) 
//    		arquivoBoleto = mpSeguranca.getLoginUsuario() + "_mpBoletoImpresso.txt";

		// 1Oficio-aaaammdd.txt (Prisco Ajutse MVPR-30052018 ...
		String nomeArquivo =  this.mpCartorioOficioSel.getNumero() + "Oficio-" + this.sdfYMD1.format(new Date());

		String arquivoBoleto = nomeArquivo + ".json";
    	if (this.indSQL) arquivoBoleto = nomeArquivo + ".sql";
    	if (this.indTXT) arquivoBoleto = nomeArquivo + ".txt";
		//
        Integer contBoleto = 0;
        
//	    List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByImpressaoList();
	    List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByOficioDataImpressaoList(this.mpCartorioOficioSel.getNumero(),
	    																					this.dataDe, this.dataAte);
//	    System.out.println("MpBaixaBoletoImpressoBean ( " + this.mpCartorioOficioSel.getNumero() + " / " +
//									this.sdfDMYHMS.format(this.dataDe) + " / " + this.sdfDMYHMS.format(this.dataAte));

	    //
		String newLineX = "\r\n"; // System.getProperty("line.separator").toString();

	    String boletoTXT = "";
//	    String boletoALL = "";

	    String boletoALL = "[HEADER, Data = " + sdfDMYHMS.format(new Date()) + ", Total Registros = " + 
	    																		mpBoletoList.size() + " ]" + newLineX;
	    //
	    for (MpBoleto mpBoletoX : mpBoletoList) {
	    	//
//	    	if (this.mpCartorioOficioSel.equals(MpCartorioOficio.OfX))
//	    		assert(true); // nop
//	    	else
//		    	if (!this.mpCartorioOficioSel.getNumero().equals(mpBoletoX.getCodigoOficio()))
//		    		continue;
	    	//
	    	contBoleto++;
	    	
	    	if (this.indSQL) {
	    		//
	    		boletoTXT = "insert into mp_boleto (codigo_oficio, numero_intimacao, " + 
	    				"numero_intimacao_code, cpf_cnpj, nome_sacado, local_bairro, local_cep, local_cidade, " + 
	    				"local_logradouro, local_numero, numero_documento, especie_documento, nosso_numero, " + 
	    				"nosso_numero_dig, valor_documento, valor_acrescimo, valor_cobrado, valor_tarifa, valor_cpmf, " + 
	    				"valor_leis, data_documento, data_vencimento, boleto_instrucao8, carteira, codigo_barras, " + 
	    				"agencia_codigo_cedente, linha_digitavel, data_vencimento_1, codigo_barras_1, " + 
	    				"agencia_codigo_cedente_1, linha_digitavel_1, ind_16H) values (";
	    		//
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getCodigoOficio().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNumeroIntimacao().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNumeroIntimacaoCode().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getCpfCnpj().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNomeSacado().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getMpEnderecoLocal().getBairro().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getMpEnderecoLocal().getCep().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getMpEnderecoLocal().getCidade().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getMpEnderecoLocal().getLogradouro().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getMpEnderecoLocal().getNumero().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNumeroDocumento().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getEspecieDocumento().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNossoNumero().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getNossoNumeroDig().trim() + "', ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorDocumento() + ", ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorAcrescimo() + ", ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorCobrado() + ", ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorTarifa() + ", ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorCPMF() + ", ";
	    		boletoTXT = boletoTXT + mpBoletoX.getValorLeis() + ", ";
	    		boletoTXT = boletoTXT + "DATE '" + sdfYMD.format(mpBoletoX.getDataDocumento()) + "', ";
	    		boletoTXT = boletoTXT + "DATE '" + sdfYMD.format(mpBoletoX.getDataVencimento()) + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getBoletoInstrucao8().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getCarteira().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getCodigoBarras().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getBoletoInstrucao8().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getAgenciaCodigoCedente().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getLinhaDigitavel().trim() + "', ";
	    		boletoTXT = boletoTXT + "DATE '" + sdfYMD.format(mpBoletoX.getDataVencimento1()) + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getCodigoBarras1().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getAgenciaCodigoCedente1().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getLinhaDigitavel1().trim() + "', ";
	    		boletoTXT = boletoTXT + "'" + mpBoletoX.getIndApos16h() + "')\n";
	    		//
	    		boletoALL = boletoALL + boletoTXT;
	    	} else 
	    	if (this.indTXT) {
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
    				MpFacesUtil.addErrorMessage("Error : " + msg + mpBoletoX.getNomeSacado());
    				return null;
    			}
	    		//    			
    			String dataVencimento = sdfDMY.format(mpBoletoX.getDataVencimento());
    			if ( mpBoletoX.getIndApos16h().equals("*"))
        			dataVencimento = sdfDMY.format(mpBoletoX.getDataVencimento1());    				
    			//
		    	boletoTXT = "[\"" + dataProtocolo + "\", " +
		    				 "\"" + numeroProtocolo + "\", " +
		    				 "\"" + mpBoletoX.getNumeroGuiaGerado() + "\", " +
						 	 "\"" + sdfDMYHMS.format(mpBoletoX.getDataImpressao()) + "\", " +
							 "\"" + dataVencimento + "\"]" + newLineX;
	    		//
	    		boletoALL = boletoALL + boletoTXT;
	    	} else {
	    		//
		    	Gson gson = new Gson();
	    		
		    	boletoTXT = boletoTXT + "\n" + gson.toJson(mpBoletoX);
	    	}
	    	//
//	    	MpAppUtil.PrintarLn("MpBaixaBoletoImpressosBean = ( " + boletoALL);
	    }		
	    //
	    if (contBoleto == 0) {
	    	//
        	MpFacesUtil.addErrorMessage("Nenhum Boleto Impresso... encontrado ! ( " + this.mpCartorioOficioSel);

        	return null;
	    }
	    //
	    boletoALL = boletoALL + "[TRAILLER, Data = " + sdfDMYHMS.format(new Date()) + ", Total Registros = " + 
																				mpBoletoList.size() + " ]" + newLineX;
	    //		
        InputStream stream = new ByteArrayInputStream(boletoALL.getBytes());        	
			
    	if (this.indSQL || this.indTXT)
    		this.file = new DefaultStreamedContent(stream, "application/text", arquivoBoleto);  
    	else
    		this.file = new DefaultStreamedContent(stream, "application/json", arquivoBoleto);  
	    //
        return this.file;  
    }
	
    // ---
    
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
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }
	
	public Date getDataDe() { return dataDe; }
	public void setDataDe(Date dataDe) { this.dataDe = dataDe; }

	public Date getDataAte() { return dataAte; }
	public void setDataAte(Date dataAte) { this.dataAte = dataAte; }
    
	// ---
	
	public Boolean getIndSQL() { return indSQL; }
	public void setIndSQL(Boolean indSQL) { this.indSQL = indSQL; }
	
	public Boolean getIndTXT() { return indTXT; }
	public void setIndTXT(Boolean indTXT) { this.indTXT = indTXT; }
	
}