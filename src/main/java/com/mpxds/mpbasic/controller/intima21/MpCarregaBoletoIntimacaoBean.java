package com.mpxds.mpbasic.controller.intima21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.xmlbeans.XmlOptions;
import org.jrimum.bopepo.view.BoletoViewer;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacao;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacaoCargaLog;
import com.mpxds.mpbasic.model.ws.intima21.Intimacao;
import com.mpxds.mpbasic.model.ws.intima21.Intimacoes;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.intima21.MpBoletoIntimacaos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.service.intima21.MpBoletoIntimacaoCargaLogService;
import com.mpxds.mpbasic.service.util.MpGeradorBoletoService;
import com.mpxds.mpbasic.service.util.MpUtilService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

import br.com.cra21.crarj.IntimacaoDocument;
import br.com.cra21.crarj.IntimacaoResponseDocument;
//import br.com.cra21.crarj.IntimacaoResponseDocument.IntimacaoResponse;
import br.com.cra21.crarj.impl.IntimacaoDocumentImpl;
//import br.com.cra21.crarj.impl.IntimacaoResponseDocumentImpl;
import samples.quickstart.service.xmlbeans.ServerCraStub; 

@Named
@ViewScoped
public class MpCarregaBoletoIntimacaoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpBoletoIntimacaos mpBoletoIntimacaos;

	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpBoletoIntimacaoCargaLogService mpBoletoIntimacaoCargaLogService;
	
	@Inject
	private MpGeradorBoletoService mpGeradorBoleto;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	@Inject
	private MpUtilService mpUtilService;
	
	@Inject
	private MpCargaControleService mpCargaControleService;

	// ---

	private ServerCraStub stubX;
	
	private UploadedFile file;
	private StreamedContent fileSC;
	
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
	private MpCartorioOficio mpCartorioOficioX;	
     
	private Long numeroRegistros;
    
	private Boolean indCargaBoleto = false;
	private Boolean indDownBoleto = false;	

    private String ambienteBradesco;
    private Boolean indApos16h;
    private Boolean indRegistro = true;

    private String msgErro = "";

    private String mensagemLog = "";
    
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");

    private StreamedContent fileX;
    
    private File arquivoPdf;
    
    private String nomePdf;
    private String nomePdfX;
    
    private String nomeArquivoP21Sel = "";

    private String xmlContentIntima21 = "";

	private Integer numRegExibeProcessamento = 20;
	
    private Boolean indCargaDiaria;
    private Boolean indRegistroBradesco;

    private String[] arquivoIntimacaoArray = new String[1000];
    
    private List<MpBoletoIntimacao> mpBoletoIntimcacaoList; // Movimento Arquivo !
    	
	private Date dataSel;
    
	// ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		
		this.indCargaBoleto = false;
		this.indDownBoleto = false;
    }

	public void init() {
		//
	    this.indCargaDiaria = true;;
	    this.indRegistroBradesco = true;;

		this.mpCartorioOficioSel = null;

        this.mpCartorioOficioX = MpCartorioOficio.OfX;
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
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

//		MpAppUtil.PrintarLn("MpBoletoBean.listaBoleto() ( " + this.numeroIntimacao + " / " + this.cpfCnpj);
	    if (null == this.mpCartorioOficioSel) { // && null == this.mpCartorioComarcaSel) {
			//
//		    if (mpSeguranca.isIndComarca()) 
//		    	MpFacesUtil.addErrorMessage("Selecionar Ofício ou Comarca");
//		    else
		    	MpFacesUtil.addErrorMessage("Selecionar Ofício");
		    //
			return;
		}
	    // Limpa Registro Mensagem Registro Boleto Bradesco ! 
		this.mpUtilService.gravaMensagemRegistroBoleto(this.mpCartorioOficioSel.getNumero(), "...");
		
		// Captura Numero de Registros para Exibição Processamento !
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + this.mpCartorioOficioSel.getNumero() + 
																						"_mensagemRegistroBoleto");	
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			if (null == mpSistemaConfig.getValorN() || mpSistemaConfig.getValorN() == 0)
				this.numRegExibeProcessamento = 20;
			else
				this.numRegExibeProcessamento = mpSistemaConfig.getValorN();
			//
			if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
				assert(true); // nop
			else {
				//
		    	MpFacesUtil.addErrorMessage("Atualizando 'DataProtocolo' (Início : " + this.sdfHMS.format(new Date()));
		    	// -----------------------------------
				// Trata Atualização 'DataProtocolo' !
		    	// -----------------------------------
				List<MpBoletoIntimacao> mpBoletoIntimacaoListDP = this.mpBoletoIntimacaos.listAll();
				
				for (MpBoletoIntimacao mpBoletoIntimacaoDP : mpBoletoIntimacaoListDP) {
					//
        			// Trata captura dataProcolo (Sacado Nome) !
        			Date dataProtocoloX = MpAppUtil.pegaSacadoDataProcotolo(mpBoletoIntimacaoDP.getNomeSacado());
        			if (null == dataProtocoloX)
        				MpFacesUtil.addErrorMessage("Data Protocolo ERROR ( NomeSacado = " + 
        																	mpBoletoIntimacaoDP.getNomeSacado());
        			else {
        				//
        				mpBoletoIntimacaoDP.setDataProtocolo(dataProtocoloX);
        				
        				this.mpBoletoIntimacaos.guardar(mpBoletoIntimacaoDP);
        			}
        			//
				}
				//
		    	MpFacesUtil.addErrorMessage("Atualizando 'DataProtocolo' (Fim : " + this.sdfHMS.format(new Date()));
			}
			//
			this.dataSel = new Date();
		}
				
		//
//    	if (this.mpSeguranca.isIndComarca()) {
//    		//
//        	for (MpCartorioComarca mpCartorioComarca :MpCartorioComarca.values()) {
//        		//
//            	this.mpCartorioComarcaList.add(mpCartorioComarca);
//        	}
//    	}
		//
		this.indApos16h = false;
		this.indDownBoleto = false;
		
	    this.msgErro = "";
	    //
		
//		if (null == this.mpSeguranca.getCpfCnpjUsuario() || this.mpSeguranca.getCpfCnpjUsuario().isEmpty())
//			assert(true);
//		else
//			this.cpfCnpj = this.mpSeguranca.getCpfCnpjUsuario();
		//
		String paramCartorio = "";
		if ( mpSeguranca.isUsuarioOf1())
			paramCartorio = "Of1_DataExpiracaoCertificadoA1";
		else
			if ( mpSeguranca.isUsuarioOf2())
				paramCartorio = "Of2_DataExpiracaoCertificadoA1";
			else
				if ( mpSeguranca.isUsuarioOf3())
					paramCartorio = "Of3_DataExpiracaoCertificadoA1";
				else
					if ( mpSeguranca.isUsuarioOf4())
						paramCartorio = "Of4_DataExpiracaoCertificadoA1";
//					else
//						if ( mpSeguranca.isUsuarioComarca()) // Ex.Usuário.Comarca: co02.balcao !
//							paramCartorio = "Co" + mpSeguranca.getLoginUsuario().substring(2, 3) + 
//																					"_DataExpiracaoCertificadoA1";
		//
		if (!paramCartorio.isEmpty()) {
			//
	    	String valorT = (String) this.mpUtilService.buscaMpConsigValor(paramCartorio, MpTipoCampo.TEXTO);
	    	if (null == valorT)
	    		assert(true);
	    	else {
	    		
	    		// 012345678901234
	    		// dd/MM/yyyy 999
	    	    try {
	    	    	//
					Date dataExpiracaoCertificado = this.sdfDMY.parse(valorT.substring(0, 10));
		    	    Integer diasMSgExpiracaoCertificado = Integer.parseInt(valorT.substring(11, 14));
		    	    //
			    	Calendar calE = Calendar.getInstance();	

					calE.setTime(dataExpiracaoCertificado);
					calE.set(Calendar.HOUR_OF_DAY, 0);
					calE.set(Calendar.MINUTE, 0);
					calE.set(Calendar.SECOND, 0);
					calE.set(Calendar.MILLISECOND, 0);
					
					calE.add(Calendar.DAY_OF_MONTH, diasMSgExpiracaoCertificado * -1);
					
					Date dataHoraExp = calE.getTime();
					//
					Date dataHoraAtual = new Date();
					
					if (dataHoraAtual.after(dataHoraExp))
		    			MpFacesUtil.addErrorMessage("Atingido Prazo para Expiração Certficado A1 !" + 
		    						" Contactar Suporte Técnico ( " +valorT.substring(0, 10));
		    	    //
	    	    } catch (ParseException e) {
	    	    	//
	    			MpFacesUtil.addErrorMessage("Error Param. Data Expiração Certficado A1 ! " + 
	    																			" Contactar Suporte Técnico");
				}
	    	}
		}
		//
		// Trata Ambiente de Produção x Homologação ...
	    //
//    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Bradeco_Registro_URI");
	    String ofComNumX = "";
//	    if (null == this.mpCartorioOficioSel)
//	    	ofComNumX = "Co" + this.mpCartorioComarcaSel.getNumero().trim();
//	    else
	    	ofComNumX = "Of" + this.mpCartorioOficioSel.getNumero().trim();
	    //
    	mpSistemaConfig = this.mpUtilService.buscaMpConsig(ofComNumX + "_Bradesco_Registro_URI");
    	if (null == mpSistemaConfig) {
    		//
			MpFacesUtil.addErrorMessage("Erro Ambiente/Bradesco (NULL) ! ( " + ofComNumX + 
													"_Bradesco_Registro_URI ) ... Contactar o Suporte Técnico !");
    		return;
    	}
		//
    	this.ambienteBradesco = mpSistemaConfig.getValorT().toUpperCase().trim();	    
		//    		
		mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Registro_Bradesco");
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
    		//
    		if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
    			this.indRegistro = false;
    		else
    			this.indRegistro = true;
    	}
    	//
    }
		
    public void upload() {
    	//
    	// ------------------------------------------
    	// Habilita/Desabilita REGISTRO no Bradesco !  
    	// ------------------------------------------
    	if (this.indRegistroBradesco)
    		this.indRegistro = true;
    	else
    		this.indRegistro = false;
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
//        MpAppUtil.PrintarLn("MpCarregaBoletoIntimacaoBean.upload() - 001");

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
//        MpAppUtil.PrintarLn("MpCarregaBoletoIntimacaoBean.upload() - 002 ( Cart = " + this.mpCartorioOficioSel.getNumero() + 
//        		" / FileSize = " + this.file.getSize());

        try {
        	//
            this.trataArquivoBoletoIntimacao(this.file.getInputstream(), this.mpCartorioOficioSel.getNumero(), 
           																				this.numeroRegistros);
            //
        } catch (IOException e) {
        	//
			MpFacesUtil.addErrorMessage("Erro_0001B - ( e = " + e.getMessage());
        }
    }
    
    public void trataArquivoBoletoIntimacao(InputStream isX, String codOficio, Long numReg) {
    	//
//      MpAppUtil.PrintarLn("MpCarregaBoletoIntimacaoBean.trataArquivoBoletoIntimacao() ( codOficio = " + codOficio + 
//        		" / isX = " + isX.toString() + " / Num.Reg = " + numReg);
        
        // Trata a zeragem da Numeração GUIA do BOLETO ...
		// =================================================
		MpSistemaConfig mpSistemaConfig = this.mpUtilService.buscaMpConsig("Of" + 
												this.mpCartorioOficioSel.getNumero() + "_BoletoIntimacaoNumeroGuia");
		if (null == mpSistemaConfig) {
			MpFacesUtil.addErrorMessage("Error ! Número Controle GUIA... não existe ! Favor contactar o SUPORTE. ( " + 
					"Of" + this.mpCartorioOficioSel.getNumero() + "_BoletoIntimacaoNumeroGuia");
			return;
		} else {
			// Atualiza numero BD !!!
			mpSistemaConfig.setValorN(0);
			
			this.mpSistemaConfigs.guardar(mpSistemaConfig);
		}
		
		// =================================================
		if (this.indCargaDiaria) // Não Apaga Tabela !
			assert(true); // nop
		else {
			//
	        String rcX = this.mpBoletoIntimacaos.executeSQL("DELETE FROM mp_boleto_intimacao WHERE " + 
	        																"codigo_oficio = '" + codOficio + "'");
	        if (null == rcX || !rcX.equals("OK")) {
	        	//
	        	MpFacesUtil.addErrorMessage("Erro_0005B - Erro Exclusão Movimento BoletoIntimacao ( rc = " + rcX);
	        	//MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento BoletoIntimacao ( rc = " + rcX);
	        	return;
	        }
	        //
        	MpFacesUtil.addErrorMessage("Exclusão Tabela Movimento Boleto Intimação... efetuada com Sucesso! ");
		}
    	//
    	String rcSqlX = this.mpBoletoIntimacaos.executeSQLs(isX, numReg, codOficio);
    	if (!rcSqlX.substring(0,2).equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0007B - Erro na Carga Movimento BoletoIntimacao ( rc = " + rcSqlX);
			return;
    	}
        // Trata número de Registros !
        this.numeroRegistros = 0L;
        
        try {
        	//
            InputStreamReader inputStreamReader = new InputStreamReader(this.file.getInputstream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //
            while (bufferedReader.ready()) {
            	//
            	String lineX = bufferedReader.readLine();
            	
            	this.arquivoIntimacaoArray[this.numeroRegistros.intValue()] = lineX;
                
//        		System.out.print("\n......................... ( " + this.numeroRegistros + " / " + lineX);
            	// 
            	this.numeroRegistros++;
            }
            //
            bufferedReader.close();
            //
        } catch (IOException e) {
			MpFacesUtil.addErrorMessage("Erro_0007C - Erro na Carga Movimento Boleto Intimacao ( e = " + e);
			return;
		} 
    	//
    	// Grava LOG ! 
    	// ==========
    	MpBoletoIntimacaoCargaLog mpBoletoIntimacaoCargaLog = new MpBoletoIntimacaoCargaLog(); 
    	
    	mpBoletoIntimacaoCargaLog.setDataGeracao(new Date());
    	mpBoletoIntimacaoCargaLog.setNumeroOficio(codOficio);
    	mpBoletoIntimacaoCargaLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
		mpBoletoIntimacaoCargaLog.setNumeroRegistros(this.numeroRegistros);
    	//    	
    	mpBoletoIntimacaoCargaLog = this.mpBoletoIntimacaoCargaLogService.salvar(mpBoletoIntimacaoCargaLog);
    	//
    	this.indCargaBoleto = true;
    	
		MpFacesUtil.addInfoMessage("Upload completo!... O arquivo " + this.file.getFileName() +
							" foi processado ! ( " + mpBoletoIntimacaoCargaLog.getNumeroRegistros() +
							" / IndRegistro = " + this.indRegistro + " / " + this.sdfDMY.format(this.dataSel));
    }

    public void intima21() {
    	//
    	if (!this.indCargaBoleto) {
    		//
    		MpFacesUtil.addInfoMessage("Arquivo Boleto Intimacação não foi carregado !");
    		return;
    	}
    	//
    	this.nomeArquivoP21Sel = capturaNomeArquivoIntima21();

    	if (this.nomeArquivoP21Sel.isEmpty()) {
    		//
			MpFacesUtil.addErrorMessage("Informar Data Protocolo!");
			return;
    	}

    	// ------------------------------------------
    	// Habilita/Desabilita REGISTRO no Bradesco !  
    	// ------------------------------------------
    	if (this.indRegistroBradesco)
    		this.indRegistro = true;
    	else
    		this.indRegistro = false;
    	
    	//
    	this.mpBoletoIntimcacaoList = new ArrayList<MpBoletoIntimacao>();
    	
    	for (int i = 0; i < this.numeroRegistros; i++) {
    		//
//    		System.out.print("\n................................. ( " + i + " / " + this.arquivoIntimacaoArray[i] +
//    																				" / " + this.numeroRegistros);
    		String lineRegX = this.arquivoIntimacaoArray[i];
    		
            //                                   012345678901234567890123456789
        	//                                   ) values ('2','025768','51984',
        	Integer posValue = lineRegX.indexOf(") values (");
        	String lineRegXValues = lineRegX.substring(posValue + 10);
        	
        	String[] valuesRegX = lineRegXValues.split(",");
        	//
//        	System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21 ( Values = " + lineRegXValues);
//        	System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21 ( Num.Protoc = " + valuesRegX[1]);
//        	System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21 ( NomeSacado = " + valuesRegX[4]);
        	
//        	String numeroProtocolo = MpAppUtil.pegaSacadoNumeroProcotolo(valuesRegX[4].replace("'", ""));

//        	String numeroProtocolo = valuesRegX[1].replace("'", "");
        	String nomeSacado = valuesRegX[4].replace("'", "");
        	
        	List<MpBoletoIntimacao> mpBoletoIntimacaoRegs = this.mpBoletoIntimacaos.
        						mpBoletoIntimacaoByOficioNomeSacado(mpCartorioOficioSel.getNumero(), nomeSacado);
        	if (mpBoletoIntimacaoRegs.size() > 1)
            	System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21 ( NomeSacado = " + valuesRegX[4]);
        	//
        	int cntRegX = 0;
        	for (MpBoletoIntimacao mpBoletoIntimacaoReg : mpBoletoIntimacaoRegs) {
        		//
        		if (cntRegX == 0) {
        			// Trata captura dataProcolo (Sacado Nome) !
        			Date dataProtocoloX = MpAppUtil.pegaSacadoDataProcotolo(mpBoletoIntimacaoReg.getNomeSacado());
        			if (null == dataProtocoloX)
                    	System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21() / DataProtocolo ERROR" + 
                    										" ( NomeSacado = " + mpBoletoIntimacaoReg.getNomeSacado());
        			else
        				mpBoletoIntimacaoReg.setDataProtocolo(dataProtocoloX);
        			//
        			mpBoletoIntimcacaoList.add(mpBoletoIntimacaoReg);
        		} else { // Apaga Registros Duplicados ! 
        			//
        			this.mpBoletoIntimacaos.remover(mpBoletoIntimacaoReg);
        			//
//        			System.out.println("\nMpCarregaBoletoIntimaacaoBean.intima21 EXCLUIDO ( ID/NomeSacado = " + 
//        								mpBoletoIntimacaoReg.getId() + " / " + mpBoletoIntimacaoReg.getNomeSacado());
        		}
        		//
        		cntRegX++;
        	}
    	}
    	
    	//
//    	List<MpBoletoIntimacao> mpBoletoIntimcacaoList = this.mpBoletoIntimacaos.
//    												mpBoletoIntimacaoByOficioList(mpCartorioOficioSel.getNumero());
    	//
    	Integer totalRegX = mpBoletoIntimcacaoList.size();
    	Integer contRegX = 0;
    	Integer contDisplayRegX = 0;
    	//    	
		this.mpUtilService.gravaAndamentoRegistroBoleto(mpCartorioOficioSel.getNumero(), "");

		for (MpBoletoIntimacao mpBoletoIntimacaoX : mpBoletoIntimcacaoList) {
    		//
            try {
            	//
            	if (contDisplayRegX >= this.numRegExibeProcessamento) {
            		//
//            		System.out.println("Of" + mpCartorioOficioSel.getNumero() + "... Processando : " + contRegX +
//            																					" / " + totalRegX);
            		// Salva Andamento do Processamento !
            		String hhmmssX = this.sdfHMS.format(new Date());
            		
            		String msgRegX = this.mpUtilService.buscaAndamentoRegistroBoleto(mpCartorioOficioSel.getNumero());

            		msgRegX = msgRegX + " [" + hhmmssX + "=" + contRegX + "/" + totalRegX + "]\n";
            		
            		this.mpUtilService.gravaAndamentoRegistroBoleto(mpCartorioOficioSel.getNumero(), msgRegX);
            		//
                	contDisplayRegX = 0;
            	}
            	//
            	contRegX++;
            	contDisplayRegX++;
            	//
            	this.intima2GeraFile(this.mpGeradorBoleto.boletoCriar(mpBoletoIntimacaoX, mpCartorioOficioSel, 
    																	this.indApos16h, this.indRegistro,
    																	this.ambienteBradesco), mpBoletoIntimacaoX);
            	// Trata Zip Arquivo/PDF ...
            	MpAppUtil.zipaFile(this.arquivoPdf);
            	
            	// Convert PDF/Zip Base64 ...
            	String base64X = MpAppUtil.fileToBase64(this.arquivoPdf);
            	
            	// Atualiza base dados ...
            	mpBoletoIntimacaoX.setBase64(base64X);
            	
            	mpBoletoIntimacaoX = this.mpBoletoIntimacaos.guardar(mpBoletoIntimacaoX);
            	//
    		} catch (Exception e) {
    			//
        		MpFacesUtil.addInfoMessage("Erro rotina BoletoViewer ( " + mpBoletoIntimacaoX.getNomeSacado());
        		//
    			e.printStackTrace();
    		}  
    	}    	
    	//
    	this.intima21GeraXml();
    	
		this.indDownBoleto = true;
    	//
		MpFacesUtil.addInfoMessage("Geração 'PDF/Zip/Base64/ArquivoXml' para a Intima21..." + 
							" realizado com Sucesso ! ( Ret.Bradesco = " + 
							this.mpUtilService.buscaMensagemRegistroBoleto(this.mpCartorioOficioSel.getNumero()));
    }

    public void intima21GeraXml() {    	
    	//
    	// Vide ... https://p21sistemas.github.io/intima21/ !
    	//
    	this.criaXmlIntima21();
    	
    	// Rotina Criação Arquivo Intima21 ...
    	// -----------------------------------
        try {
            // Gravação Arquivo Envio Intima21 ...
            // -----------------------------------
//           System.out.println( xmlContent );
            // Salva arquivo na pasta relacionada ao cartório em questão ! Ex.: ../Ofx/..
            String pathX = System.getProperty("user.home") + File.separator + "Of" + 
            										this.mpCartorioOficioSel.getNumero().trim() + File.separator;
            
            this.nomeArquivoP21Sel = this.capturaNomeArquivoIntima21();
            // 
            OutputStreamWriter bufferOut = new OutputStreamWriter(new FileOutputStream(pathX + 
            																		this.nomeArquivoP21Sel), "UTF-8");   
            bufferOut.write(this.xmlContentIntima21);

            bufferOut.close();            
        	//
		} catch (Exception e) {
			//
    		MpFacesUtil.addInfoMessage("Erro geração arquivo XML Intima21 ( " + e);
		}  
    	//
    }

    public void criaXmlIntima21() {
    	//
    	if (null == dataSel) {
    		//
    		MpFacesUtil.addInfoMessage("Informar Data Protocolo!");
    		return;
    	}
    	//
		System.out.println("\nMpCarregaBoletoIntimacaoBean.criaXmlIntima21() ( " + 
							this.mpCartorioOficioSel.getNumero() + " / " + this.sdfDMY.format(this.dataSel));
		//
//    	this.mpBoletoIntimcacaoList = this.mpBoletoIntimacaos.mpBoletoIntimacaoByOficioList(
//																				mpCartorioOficioSel.getNumero());
    	this.mpBoletoIntimcacaoList = this.mpBoletoIntimacaos.mpBoletoIntimacaoByOficioDataProtocoloList(
    														this.mpCartorioOficioSel.getNumero(), this.dataSel);
    	//
    	Intimacoes intimacoes = new Intimacoes();
    	//
    	for (MpBoletoIntimacao mpBoletoIntimacaoX : this.mpBoletoIntimcacaoList) {
    		//
        	Intimacao intimacao = new Intimacao();
        	
        	intimacao.setProtocolo(mpBoletoIntimacaoX.getNumeroIntimacao());        	
	        //
//			System.out.println("\nMpCarregaBoletoIntimaacaoBean.criaXmlIntima21() - 000 ( " + 
//					MpAppUtil.pegaSacadoNome(mpBoletoIntimacaoX.getNomeSacado()) + " / " + 
//					this.sdfDMY.format(mpBoletoIntimacaoX.getDataProtocolo()));
	        //
        	intimacao.setData_protocolo(this.sdfDMY.format(mpBoletoIntimacaoX.getDataProtocolo()));
        	//
        	intimacao.setEspecie(mpBoletoIntimacaoX.getEspecieDocumento());
        	intimacao.setNumero(mpBoletoIntimacaoX.getNumeroTitulo().trim());
        	intimacao.setData_validade(this.sdfDMY.format(mpBoletoIntimacaoX.getDataVencimento()));
        	
        	intimacao.setFins_falimentares(Integer.parseInt(mpBoletoIntimacaoX.getFinsFalimentares()));
        	
        	intimacao.setNome_devedor(MpAppUtil.pegaSacadoNome(mpBoletoIntimacaoX.getNomeSacado()));
        	intimacao.setDocumento_devedor(mpBoletoIntimacaoX.getCpfCnpj());
        	intimacao.setBase64(mpBoletoIntimacaoX.getBase64());     	
        	//        	
        	intimacoes.getIntimacao().add(intimacao);
        	
            try {
            	//
                JAXBContext jaxbContext = JAXBContext.newInstance(Intimacoes.class);
                
                //Create Marshaller
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                //Required formatting??
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                //Print XML String to Console
                StringWriter sw = new StringWriter();
                 
                //Write XML to StringWriter
                jaxbMarshaller.marshal(intimacoes, sw);
                 
                //Verify XML Content
                this.xmlContentIntima21 = sw.toString();
                
                // Remove Header do arquivo ! 
                this.xmlContentIntima21.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
            	//
    		} catch (Exception e) {
    			//
        		MpFacesUtil.addInfoMessage("Erro criação arquivo XML Intima21 ( " + e);
    		}  
        	//
    	}
    }
    
    public void intima2GeraFile(BoletoViewer boletoViewer, MpBoletoIntimacao mpBoletoIntimacaoSelX) {    	
    	//
        try {
        	//
        	this.msgErro = "";
        	
        	if (null == boletoViewer) {
        		//
        		if (this.msgErro.isEmpty())
        			MpFacesUtil.addErrorMessage("Error Geração BOLETO! Favor Verificar ( Num.Documento = " + 
        															mpBoletoIntimacaoSelX.getNumeroDocumento());
        		else
        			MpFacesUtil.addErrorMessage("Error Geração BOLETO!! Favor Verificar ( " + this.msgErro);
        		//
        		return;
        	} else
        		if (!this.msgErro.isEmpty()) {
        			//
        			MpFacesUtil.addErrorMessage("Error Geração BOLETO!!! Favor Verificar ( " + this.msgErro);
        			//
        	        // Grava Boleto Log...
        	        // ===================
        			this.mensagemLog = this.mensagemLog + " ( " + this.msgErro;
        			
        			//
        	        this.mpGeradorBoleto.gravaBoletoLog(mpBoletoIntimacaoSelX, this.mpCartorioOficioSel, 
					        	        							this.ambienteBradesco, 
					        	        							mpBoletoIntimacaoSelX.getDataProtocolo(),
					        	        							mpBoletoIntimacaoSelX.getNumeroIntimacao());
        			//
        			return;
        		}
        	//
    		String nossoNumero = mpBoletoIntimacaoSelX.getNossoNumero().replaceAll("/", "");

    		nossoNumero = nossoNumero.replaceAll("-", "");
    		if (nossoNumero.isEmpty()) nossoNumero = "00000000000"; // ???????
        	//
//    		String contadorPrt = "";
//    		if (null == mpBoletoIntimacaoSelX.getContadorImpressao())
//        		contadorPrt = "0";
//    		else
//        		contadorPrt = mpBoletoIntimacaoSelX.getContadorImpressao().toString();
    		//
//        	this.nomePdf = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + nossoNumero + "_" +
//        																			contadorPrt + ".pdf";        	
        	this.nomePdfX = "MpBoletoIntimacao_" + mpSeguranca.getLoginUsuario() + "_" + 
       												this.sdfYYYYMMDD.format(new Date()) + "_" + nossoNumero + ".pdf";     	
        	this.nomePdf = this.nomePdfX;
        	
        	this.arquivoPdf = boletoViewer.getPdfAsFile(System.getProperty("user.home") + File.separator + 
       																								this.nomePdf);
        	//
//        	if (this.mpCartorioOficioSel.getNumero().equals("2"))
        	this.mpGeradorBoleto.atualizaPdf(this.arquivoPdf, mpBoletoIntimacaoSelX, mpCartorioOficioSel);
        	//
        	InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(
        																	this.arquivoPdf.getAbsolutePath());  
       	
        	this.fileX = new DefaultStreamedContent(stream, "application/pdf", this.nomePdf);

//    		MpAppUtil.PrintarLn("MpBoletoBean.download() - 000.0 ( " + this.arquivoPdf.getAbsolutePath());
    		//    		
		} catch (Exception e) {
			//
			e.printStackTrace();
		}  
    }
    
    public StreamedContent getFileSC() throws FileNotFoundException {
    	//
    	this.nomeArquivoP21Sel = capturaNomeArquivoIntima21();

    	if (this.nomeArquivoP21Sel.isEmpty()) {
    		//
			MpFacesUtil.addErrorMessage("Informar Data Protocolo!");
			return null;
    	}
    	//
    	
//    	System.out.println("MpCarregaBoletoIntimacaoBean.getFileSC()........................... ( " + 
//    																						this.fileX.getName());
        String pathX = System.getProperty("user.home") + File.separator + "Of" + 
													this.mpCartorioOficioSel.getNumero().trim() + File.separator;

    	InputStream stream = new FileInputStream(pathX  + this.nomeArquivoP21Sel);

        this.fileSC = new DefaultStreamedContent(stream, "application/txt", this.nomeArquivoP21Sel);
        //
        return this.fileSC;
    }
    
    public void download() {
    	//
    	this.nomeArquivoP21Sel = capturaNomeArquivoIntima21();

    	if (this.nomeArquivoP21Sel.isEmpty()) {
    		//
    		MpFacesUtil.addErrorMessage("Erro Captura o arquivo 'Intima21', para a data selecionada!");
    		return;
    	}
    	//
    	try {
	        // Salva arquivo na pasta relacionada ao cartório em questão ! Ex.: ../Ofx/..
	        String pathX = System.getProperty("user.home") + File.separator + "Of" + 
	        										this.mpCartorioOficioSel.getNumero().trim() + File.separator;
	        
	        InputStream stream = new FileInputStream(pathX  + this.nomeArquivoP21Sel);
	
	        this.fileSC = new DefaultStreamedContent(stream, "application/txt", this.nomeArquivoP21Sel);
	        //
		} catch (Throwable e) {
			//
			MpFacesUtil.addErrorMessage("Erro Rotina Download ( ArquivoP21Sel / e = " + this.nomeArquivoP21Sel +
																										" /" + e);
		}
    	//
//    	System.out.println("MpCarregaBoletoIntimacaoBean.download()................. ( " + this.nomeArquivoP21);
    }    

    // ---
    
    public void enviaArquivoIntima21() {
    	//
    	this.nomeArquivoP21Sel = capturaNomeArquivoIntima21();

    	if (this.nomeArquivoP21Sel.isEmpty()) {
    		//
    		MpFacesUtil.addErrorMessage("Erro Captura o arquivo 'Intima21', para a data selecionada!");
    		return;
    	}
    	//
		try {
	    	// Trata captura do ambiente (PRD/DSV) da API Intima21 !
			Boolean indProducaoIntima21 = mpUtilService.buscaAmbienteProducaoIntima21(); // Produçao/Teste(Sandbox) !
			
	    	// Trata captura Url(PRD/DSV) da API Intima21 !
			String urlIntima21 = mpUtilService.buscaUrlIntima21(indProducaoIntima21);

			if (urlIntima21.isEmpty()) {
				//
    			MpFacesUtil.addErrorMessage("Error captura URL da API 21! Contactar o Suporte!");
    			return;
			}
			//
	    	this.criaStubIntima21(urlIntima21); // "http://crarj.cra21.com.br/crarj/xml/protestos_cartorio.php?wsdl");

	    	// Trata captura Usuario/Senha da API Intima21 !
	    	String usuarioSenha21 = mpUtilService.buscaUserPswIntima21(this.mpCartorioOficioSel.getNumero().trim());

	    	if (usuarioSenha21.isEmpty()) {
				//
    			MpFacesUtil.addErrorMessage("Error captura usuário/senha da API 21! Contactar o Suporte!");
    			return;
			}
			
	    	String usuarioIntima21 = usuarioSenha21.substring(0, usuarioSenha21.indexOf("/"));
	    	String senhaIntima21 = usuarioSenha21.substring(usuarioSenha21.indexOf("/") + 1);
	    	
//	    	System.out.println("MpCarregaBoletoIntimacaoBean.enviaArquivoIntima21()................. ( Arq = " +
//					this.nomeArquivoP21Sel + " / Url = " + urlIntima21 + " / IndProd =  " + indProducaoIntima21 +
//					" / User = " + usuarioIntima21 + " / Psw = " + senhaIntima21);
	    	//
			HttpTransportProperties.Authenticator basicAuthentication = new HttpTransportProperties.Authenticator();
			
			// -------------------------------------------
			basicAuthentication.setUsername(usuarioIntima21.trim());
			basicAuthentication.setPassword(senhaIntima21.trim());
			// -------------------------------------------
			
			basicAuthentication.setPreemptiveAuthentication(true);
	
			this.stubX._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.
															http.HTTPConstants.AUTHENTICATE, basicAuthentication);
			// mpTesteIntima21.stubX._getServiceClient().getOptions().setManageSession(true);
			//
			// String msgResponse = 
			this.geraIntimacao(this.nomeArquivoP21Sel);
	
//			MpFacesUtil.addErrorMessage(msgResponse);

			MpFacesUtil.addInfoMessage("Envio... 'PDF/Zip/Base64/ArquivoXml' para a Intima21..." + 
							" realizado com Sucesso ! ( Retorno Bradesco = " + 
							this.mpUtilService.buscaMensagemRegistroBoleto(this.mpCartorioOficioSel.getNumero()));		
			//
		} catch (Throwable e) {
			//
			System.out.println(".............................................erro inesperado");
			e.printStackTrace();
		}
    }    

    public String capturaNomeArquivoIntima21() {
    	//
    	if (null == this.dataSel) return "";
    	//
    	String nomeArquivoP21Down = "";
    	
        String dataX = this.sdfDMY.format(this.dataSel); // dd/mm/yyyy ...

        nomeArquivoP21Down = "IT000" + dataX.substring(0, 2) + dataX.substring(3, 5) + "." + 
        																			dataX.substring(6, 8) + "1";        
    	//
		System.out.println(".............................................nomeArquivoP21Down = " + nomeArquivoP21Down);

		return nomeArquivoP21Down;
    }
    
	public void criaStubIntima21(String endereco) throws AxisFault {
		//
		this.stubX = new ServerCraStub(endereco);
	}
	
	private String geraIntimacao(String nomeArquivoP21) throws InstantiationException, IllegalAccessException, 
																									IOException {
		//
    	this.criaXmlIntima21();
		
		XmlOptions options = this.stubX._getXmlOptions();

		IntimacaoDocument intimacaoDocument = IntimacaoDocument.Factory.newInstance(options);

		IntimacaoDocumentImpl.Intimacao intimacao18 = intimacaoDocument.addNewIntimacao();

		intimacao18.setUserArq(nomeArquivoP21.trim());
		intimacao18.setUserDados(this.xmlContentIntima21.trim());

		intimacaoDocument.setIntimacao(intimacao18);
		//
		IntimacaoResponseDocument resposta = IntimacaoResponseDocument.Factory.newInstance(options);

//		IntimacaoResponseDocumentImpl.IntimacaoResponse intimacaoResponse = resposta.addNewIntimacaoResponse();

//		System.out.println("MpTesteIntima21.geraIntimacao() (.......... " + intimacaoResponse.getReturn());
		
		resposta = this.stubX.intimacao(intimacaoDocument);
		//
		return resposta.getIntimacaoResponse().getReturn();
	}	
    
    // ---
    
    public void addMessageCargaDiaria() {
    	//
        String summary = this.indCargaDiaria ? "Carga Diaria Ativada!" : 
        														"Carga Diaria Desativada! (Cuidado Apaga Tabela)";
        
		MpFacesUtil.addErrorMessage(summary);
    }

    public void addMessageRegistroBradesco() {
    	//
        String summary = this.indRegistroBradesco ? "Registro BRADESCO Ativado!" : 
        											"Registro BRADESCO Desativado! (Cuidado Gera Inconsistência P21)";
        
		MpFacesUtil.addErrorMessage(summary);
    }

    public void addMessageDataSel() {
    	//
		MpFacesUtil.addErrorMessage(this.sdfDMY.format(this.dataSel));
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
    
    public Boolean getIndCargaBoleto() { return indCargaBoleto; }
    public void setIndCargaBoleto(Boolean indCargaBoleto) { this.indCargaBoleto = indCargaBoleto; }
    
    public Boolean getIndDownBoleto() { return indDownBoleto; }
    public void setIndDownBoleto(Boolean indDownBoleto) { this.indDownBoleto = indDownBoleto; }
	
	public Boolean getIndCargaDiaria() { return indCargaDiaria; }
	public void setIndCargaDiaria(Boolean indCargaDiaria) { this.indCargaDiaria = indCargaDiaria; }
	
	public Boolean getIndRegistroBradesco() { return indRegistroBradesco; }
	public void setIndRegistroBradesco(Boolean indRegistroBradesco) { this.indRegistroBradesco = indRegistroBradesco; }
		
	public Date getDataSel() { return dataSel; }
	public void setDataSel(Date dataSel) { this.dataSel = dataSel; }

}