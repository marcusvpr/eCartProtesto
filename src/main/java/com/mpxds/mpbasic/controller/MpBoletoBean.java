package com.mpxds.mpbasic.controller;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.pdf.CodigoDeBarras;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpBoletoLog;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.model.cielo.CardOnFile;
import com.mpxds.mpbasic.model.cielo.CreditCard;
import com.mpxds.mpbasic.model.cielo.Customer;
import com.mpxds.mpbasic.model.cielo.Payment;
import com.mpxds.mpbasic.model.cielo.PaymentRequest;
import com.mpxds.mpbasic.model.cielo.enums.CardBrand;
import com.mpxds.mpbasic.model.cielo.enums.CardNumeroParcela;
import com.mpxds.mpbasic.model.cielo.enums.CardNumeroParcelaDebito;
import com.mpxds.mpbasic.model.cielo.enums.CardType;
import com.mpxds.mpbasic.model.enums.MpCartorioComarca;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpEspecieTituloBradesco;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistro;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistroRetorno;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpBoletoLogService;
import com.mpxds.mpbasic.service.MpBoletoService;
import com.mpxds.mpbasic.service.MpSistemaConfigService;
import com.mpxds.mpbasic.service.MpTituloService;
import com.mpxds.mpbasic.service.ws.cielo.MpCieloWS;
import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.cobranca.MpGeradorDigitoVerificador;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
import com.sun.jersey.api.client.ClientResponse;

@ManagedBean
@Named
@ViewScoped
public class MpBoletoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
			
	@Inject
	private ExternalContext externalContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private FacesContext facesContext;

	@Inject
	private MpBoletos mpBoletos;

	@Inject
	private MpBoletoService mpBoletoService;

	@Inject
	private MpBoletoLogService mpBoletoLogService;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	@SuppressWarnings("unused")
	@Inject
	private MpSistemaConfigService mpSistemaConfigService;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpTitulos mpTitulos;

	@Inject
	private MpTituloService mpTituloService;

	@Inject
	private MpCieloWS mpCieloWS;
	
	// ---
	
	private MpBoleto mpBoletoSelX;
	private MpBoleto mpBoletoSelAntX;
		
    private List<MpBoleto> mpBoletoList;
    private List<MpBoleto> mpBoletoListP;
    
    private HtmlDataTable dataTable;
    //
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioComarca mpCartorioComarcaSel;
	
	private MpCartorioOficio mpCartorioOficioX;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
    
    private String msgErro = "";
    private String boletoRegistro = "";
    private String boletoRegistroRetorno = "";
    @SuppressWarnings("unused")
	private String codigoErroRegistro = "";

    private String numeroIntimacao;
    private String numeroIntimacaoCode;
    
    private Date dataIntimacao;

    private String ambienteBradesco;
    private String mensagemLog = "";
    private String emailLog = "";

    private String dataNumeroIntimacaoBarCode;
    
    private String cpfCnpj = "";
    private String cpfCnpj1 = "";
    
    private StreamedContent fileX;
    
    private File arquivoPdf;
    private String nomePdf;
    private String nomePdfX;

    private Boolean indApos16h;
    
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfMD = new SimpleDateFormat("MM-dd");
	private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDMYp = new SimpleDateFormat("dd.MM.yyyy");
	
	private Integer numeroGuiaGerado;
//	private Integer numeroGuiaGeradoAnt;
	
	// Registro Boleto (WebService) ... BRADESCO ! 
	
    private Boolean indRegistro;

    private static final String PATH_TO_KEYSTORE_1OF = "certificado1of.pfx";
	private static final String PATH_TO_KEYSTORE_2OF = "certificado2of.pfx";
	private static final String PATH_TO_KEYSTORE_3OF = "certificado3of.pfx";
	private static final String PATH_TO_KEYSTORE_4OF = "certificado4of.pfx";
	
	private static final String KEYSTORE_PASSWORD = "mpxdsa1";
	
	private static final String SIGNATUREALGO = "SHA256WithRSA"; // "Sha256WithRSA";
	private static final String URI_REGISTRO_HOMOLOGACAO =
			"https://cobranca.bradesconetempresa.b.br/ibpjregistrotitulows/registrotitulohomologacao";
	private static final String URI_REGISTRO_PRODUCAO =
			"https://cobranca.bradesconetempresa.b.br/ibpjregistrotitulows/registrotitulo";

	private Boolean indGeraBoleto;	

	private Boolean indCartorio;
	private Boolean indComarca;
	private Boolean indUsuario;
	private Boolean indAdministrador;

	private Boolean indBoletoPrinter;

	private List<MpCartorioComarca> mpCartorioComarcaList = new ArrayList<MpCartorioComarca>();
	
	private Boolean indEmailBoleto = false;
	private String emailBoleto = "";

	private Boolean indCartaoCielo;
	private boolean skip;
	private Boolean indTermoCartaoCielo = false;
	
	private String wizardNomeCartaoCredito = "";
	private String wizardNumeroCartaoCredito = "";
	private String wizardCVVCartaoCredito = ""; // 3 Digitos
	private String wizardValidadeCartaoCredito = ""; // MM/YYYY
	
	private CardBrand cardBrandSel = null;
	private CardNumeroParcela cardNumeroParcelaDocSel = null;
	private CardNumeroParcelaDebito cardNumeroParcelaDivSel = null;

	private CardBrand[] cardBrandList = CardBrand.values();
	private CardNumeroParcela[] cardNumeroParcelaDocList = CardNumeroParcela.values();
	private CardNumeroParcelaDebito[] cardNumeroParcelaDivList = CardNumeroParcelaDebito.values();

	private Boolean wizardIndCartaoCredito = false;

    private ClientResponse clientResponse;	

    // ---
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void initPost() {
//		//
//		RequestContext.getCurrentInstance().execute("handleMsg('invoked from post construct');");
//		
//        String printJS = "printJS('http://localhost:8080/MpProtRJCap/resources/pdfs/of" + 
//				this.mpCartorioOficioSel.getNumero() + 
//				"/" + this.mpBoletoSelX.getNomePdfGerado() + "');";
//
//        RequestContext.getCurrentInstance().execute("printJS('http://localhost:8080/MpProtRJCap/resources/pdfs/BASE.PDF');");
//		
        // MVPR-09112019 ! 
		System.out.println("MpBoletoBean.init() - Entrou!");
		
    	RequestContext.getCurrentInstance().execute("dataIntimacaoId.focus();");
//    	RequestContext.getCurrentInstance().scrollTo("dataIntimacaoWv");
	}

	public void init() {
		//
		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
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
		
		this.indRegistro = true;
		this.indGeraBoleto = false;		
		
	    this.msgErro = "";
	    this.boletoRegistro = "";
	    this.boletoRegistroRetorno = "";
	    //
		this.getIndCartorio();
		this.getIndComarca();
		this.getIndUsuario();
		
		this.indCartaoCielo = this.mpSeguranca.isIndAtivaCartaoCielo();
		//
		
//		if (null == this.mpSeguranca.getCpfCnpjUsuario() || this.mpSeguranca.getCpfCnpjUsuario().isEmpty())
//			assert(true);
//		else
//			this.cpfCnpj = this.mpSeguranca.getCpfCnpjUsuario();
		//
		String paramCartorio = "";
		String agenciaContaBradesco = "";

		if ( mpSeguranca.isUsuarioOf1())
			paramCartorio = "Of1_AgenciaContaBradesco";
		else
			if ( mpSeguranca.isUsuarioOf2())
				paramCartorio = "Of2_AgenciaContaBradesco";
			else
				if ( mpSeguranca.isUsuarioOf3())
					paramCartorio = "Of3_AgenciaContaBradesco";
				else
					if ( mpSeguranca.isUsuarioOf4())
						paramCartorio = "Of4_AgenciaContaBradesco";
//					else
//						if ( mpSeguranca.isUsuarioComarca()) // Ex.Usuário.Comarca: co02.balcao !
//							paramCartorio = "Co" + mpSeguranca.getLoginUsuario().substring(2, 3) + 
//																					"_DataExpiracaoCertificadoA1";
		//
		if (!paramCartorio.isEmpty()) {
			//
			if (!this.indCartaoCielo) {
				//
				this.indCartaoCielo = this.mpSeguranca.isIndAtivaCartaoCieloBoleto(paramCartorio.substring(2, 3));
			}
			//
	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramCartorio);
	    	if (null == mpSistemaConfig)
	    		assert(true);
	    	else {
	    		SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	    		// 012345678901234
	    		// dd/MM/yyyy 999
	    	    try {
	    	    	//
					Date dataExpiracaoCertificado = sdfDMY.parse(mpSistemaConfig.getValorT().substring(0, 10));
		    	    Integer diasMSgExpiracaoCertificado  = Integer.parseInt(mpSistemaConfig.getValorT().
		    	    																			substring(11, 14));
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
		    						" Contactar Suporte Técnico ( " + mpSistemaConfig.getValorT().substring(0, 10));
		    	    //
	    	    } catch (ParseException e) {
	    			MpFacesUtil.addErrorMessage("Error Param. Data Expiração Certficado A1 ! " + 
	    																			" Contactar Suporte Técnico");
				}
	    	}
		}
		//
		this.wizardNumeroCartaoCredito = mpSeguranca.getCieloCartaoTeste();
    }
    
    public void listaBoletoCodigoBarra() {
		//
    	if (null == this.dataNumeroIntimacaoBarCode || this.dataNumeroIntimacaoBarCode.isEmpty()) {
    		//
			MpFacesUtil.addErrorMessage("Informar Código de Barras !");
			return;
    	}
    	if (this.dataNumeroIntimacaoBarCode.length() == 14)
			assert(true); // nop
		else {
    		//
			MpFacesUtil.addErrorMessage("Código Barras informado... inválido ! (" + this.dataNumeroIntimacaoBarCode);
			return;
    	}
    	//
    	try {
    		//
    		this.dataIntimacao = this.sdfYYYYMMDD.parse(this.dataNumeroIntimacaoBarCode.substring(0, 8));
    		this.numeroIntimacao = this.dataNumeroIntimacaoBarCode.substring(8);
	    	//
//    		System.out.println("MpBoletoBean.listaBoletoCodigoBarra() ( " +
//    													this.dataNumeroIntimacaoBarCode.substring(0, 8) + " / " + 
//    													this.dataNumeroIntimacaoBarCode.substring(8));
    		
	    	this.listaBoleto();
	    	//
		} catch (ParseException e) {
			MpFacesUtil.addErrorMessage("Código de Barras informado(data)... inválido ! ( " + 
			 this.dataNumeroIntimacaoBarCode.substring(0, 8) + " / " + this.dataNumeroIntimacaoBarCode.substring(8));
			return;
		}
    }
    
    public void listaBoleto() {
		//
//		System.out.println("MpBoletoBean.listaBoletoList() - ( Entrou 000 ");

    	if (this.indUsuario) {
    		//
    		this.cpfCnpj = this.cpfCnpj1;
    	}
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
	    //
//	    if (mpSeguranca.isIndComarca()) {
//	    	//
//		    if (null == this.mpCartorioOficioSel || null == this.mpCartorioComarcaSel) 
//		    	assert(true); // nop
//		    else
//		    	if (null == this.mpCartorioOficioSel) {
//		    		//
//			    	if (!this.mpCartorioComarcaSel.getNome().isEmpty()) {
//			    		//
//			    		MpFacesUtil.addErrorMessage("Selecionar Ofício ou Comarca !");  
//			    		return;
//			    	}
//		    	} else {
//		    		//
//			    	if (!this.mpCartorioOficioSel.getNome().isEmpty()) {
//			    		//
//			    		MpFacesUtil.addErrorMessage("Selecionar Ofício ou Comarca !");  
//			    		return;
//			    	}
//		    	}
//	    }
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
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Bradesco_Registro_URI");
    	if (null == mpSistemaConfig) {
			MpFacesUtil.addErrorMessage("Erro Ambiente/Bradesco (NULL) ! ( " + ofComNumX + 
													"_Bradesco_Registro_URI ) ... Contactar o Suporte Técnico !");
    		return;
    	}
		//
    	this.ambienteBradesco = mpSistemaConfig.getValorT().toUpperCase().trim();	    
		
//    	System.out.println("MpBoletoBean.listaBoletoList() - ( Entrou 000 ( Ambiente Bradesco = " + this.ambienteBradesco +
//															" / Of. = " + this.mpCartorioOficioSel.getNumero().trim());
		// Verfica SÁBADO/DOMINGO e FERIADO !!!
		Date diaAtual = new Date();
		
		String diaSemAtual = MpAppUtil.diaSemana(diaAtual);
		//
		if (diaSemAtual.equals("Sábado") || diaSemAtual.equals("Domingo")) {
			//
			MpFacesUtil.addErrorMessage("Serviço Indisponivel fim de semana ! ( " + diaSemAtual + " ) " );
			return;
		}
		//
		String dataYMD = sdfYMD.format(diaAtual);
		String dataMD = sdfMD.format(diaAtual);
		
		if (dataMD.equals("11-02") || // Finados 
			dataMD.equals("12-25") || // Natal
			dataMD.equals("01-01")) { // Ano Novo
			//
			MpFacesUtil.addErrorMessage("Serviço Indisponivel feriado oficial ! ( " + dataMD + " ) " );
			return;
		}
		//		
    	mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Registro_Bradesco");
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
    		if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
    			this.indRegistro = false;
    		else
    			this.indRegistro = true;
    	}
    	
		// Trata Feriado Global ! MVPR-15112019 ...	
    	mpSistemaConfig = mpSistemaConfigs.porParametro("OfX_Feriado_" + dataYMD);
		if (null == mpSistemaConfig)
			assert(true); // nop
		else {
			MpFacesUtil.addErrorMessage("Serviço Indisponivel ! Feriado... ( " + dataYMD + " - " + 
																			mpSistemaConfig.getDescricao());
			return;    		
		}
		//
		mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Feriado_" + dataYMD);
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
			MpFacesUtil.addErrorMessage("Serviço Indisponivel ! Feriado ( " + dataYMD + " - " + 
																			mpSistemaConfig.getDescricao());
			return;    		
    	}
		
		// Verificar Horário de Funcionamento !
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    
	    String dateX = new Date().toString();	

	    String dtInicioS = "";
	    String dtFimS = "";

	    Date dtAtual = new Date();
	    Date dtInicio = dtAtual;
	    Date dtFim = dtAtual;
	    //
	    try {
	    	// Ex.: Tue Nov 14 09:28:07 BRST 2017
	    	//      012345678901234567890123456789
	    	dtAtual = sdf.parse(dateX.substring(11, 16)); 

	    	mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_HorarioBoleto");
	    	if (null == mpSistemaConfig) {
	    		//
//	    		if (null == this.mpCartorioOficioSel) {
//	    			//
//			    	dtInicioS = this.mpCartorioComarcaSel.getHorarioFuncionamento().substring(0, 5);
//			    	dtFimS = this.mpCartorioComarcaSel.getHorarioFuncionamento().substring(6, 11);
//	    		} else {
	    			//
			    	dtInicioS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(0, 5);
			    	dtFimS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(6, 11);
//	    		}
	    		//
		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	   		} else {
	   			//
    			if (!mpSistemaConfig.getIndValor()) {
	    			//
	    			MpFacesUtil.addErrorMessage("Serviço Temporariamente Indisponivel ! Tente mais tarde. ( " +
	    																								ofComNumX); 
	    			return;
	    		}
	    		//
		    	dtInicioS = mpSistemaConfig.getValorT().substring(0, 5);
		    	dtFimS = mpSistemaConfig.getValorT().substring(6, 11);
		    	
		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	    	}
	    	// Trata zeragem dos segundos/milisegundos ! 
			Calendar dataX = Calendar.getInstance();
			
			dataX.setTime(dtInicio);
			dataX.set(Calendar.SECOND, 0);
			dataX.set(Calendar.MILLISECOND, 0);
			dtInicio = dataX.getTime();

			dataX.setTime(dtFim);
			dataX.set(Calendar.SECOND, 0);
			dataX.set(Calendar.MILLISECOND, 0);
			dtFim = dataX.getTime();
			//	    	
	    	if ((dtAtual.after(dtInicio) && dtAtual.before(dtFim)))
	   			assert(true);
	   		else {
	    		MpFacesUtil.addErrorMessage("Fora do horário permitido ! ( De: " + 
	    			dtInicioS + " às " + dtFimS + " ) - " + sdf.format(dtAtual));
	    		return;
	   		}
	    	//
	    	// Tratamento para após às 16h00m ...
	    	//
	    	if (dtAtual.after(sdfHMS.parse("16:00:00"))) {
	    		//
	    		this.indApos16h = true;
	    		
	    		// MpFacesUtil.addErrorMessage("Após 16h00m... o vencimento será para próximo dia útil !!!"); 
	    	}
		    //
		} catch (ParseException e) {
			//
			MpFacesUtil.addErrorMessage("Error.001 - Tratamento Horas ! Contactar Suporte Técnico ! ( hI = " + 
					dtInicioS + " / hf = " + dtFimS + " / hA = " + dtAtual.toString() + " / e = " + e);
			return;
		}
	    // -----------	    
		// Criticar...
	    // -----------
		if (null == this.cpfCnpj) this.cpfCnpj = "";
		if (null == this.numeroIntimacao) this.numeroIntimacao = "";
		
		if (null == this.dataIntimacao || this.dataIntimacao.toString().isEmpty()) {
			if (this.numeroIntimacao.isEmpty() && this.cpfCnpj.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Informar o Número do Protocolo e CPF/CNPJ !");
				return;
			}
		} else {
			if (this.numeroIntimacao.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Informar o Número do Protocolo !");
				return;
			}
		}
		// MVPR-20180808 (Preencher com zeros à esquerda NumeroProtocolo (numeroIntimacao)! 
		this.numeroIntimacao = StringUtils.leftPad(this.numeroIntimacao.trim(), 6, "0");
		//
		if (!this.cpfCnpj.isEmpty()) {
			//
	    	String cpfCnpjX = MpAppUtil.formataCpfCnpj(this.cpfCnpj); 
			
	    	String retMsgX = MpAppUtil.validaCpfCnpj(this.cpfCnpj); 
			if (!retMsgX.equals("OK")) {
				//
				MpFacesUtil.addErrorMessage(retMsgX);
				return;
			}
			//
			if (this.mpCartorioOficioSel.getNumero().equals("X"))
				this.mpBoletoList = mpBoletos.mpBoletoByNumeroIntimacaoCpfCnpjList(this.numeroIntimacao, cpfCnpjX);
			else
				this.mpBoletoList = mpBoletos.mpBoletoByOficioNumeroIntimacaoCpfCnpjList(
											this.mpCartorioOficioSel.getNumero(), this.numeroIntimacao, cpfCnpjX);
			if (this.mpBoletoList.size() == 0) {
				//
				MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) na Base de Dados ! (rc=01)");
				return;
			}
			// Captura todas intimações para o CPF/CNPJ !  
			if (this.mpCartorioOficioSel.getNumero().equals("X"))
				this.mpBoletoList = mpBoletos.mpBoletoByCpfCnpjList(this.cpfCnpj);
			else
//				if (null == this.mpCartorioOficioSel)
//					this.mpBoletoList = mpBoletos.mpBoletoByOficioCpfCnpjList(this.mpCartorioComarcaSel.getNumero(),
//																				this.cpfCnpj);
//				else
					this.mpBoletoList = mpBoletos.mpBoletoByOficioCpfCnpjList(this.mpCartorioOficioSel.getNumero(), this.cpfCnpj);
			//
			if (this.mpBoletoList.size() == 0) {
				//
				MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) na Base de Dados ! (rc=02) ( " + ofComNumX + " / " + 
																						this.cpfCnpj + " / " + this.numeroIntimacao);
				return;
			}
			//
		} else {
//			System.out.println("MpBoletoBean.listaBoleto() - ( " + this.mpCartorioOficioSel.getNumero() + " / " +
//					sdfDMY.format(this.dataIntimacao) + " / " + this.numeroIntimacao);
			//
			if (null == this.dataIntimacao)
				assert(true); // nop
			else {
				//
				this.mpBoletoList = new ArrayList<MpBoleto>();
				
				String numOfCoX = "";
//				if (null == this.mpCartorioOficioSel)
//					numOfCoX = this.mpCartorioComarcaSel.getNumero();
//				else
					numOfCoX = this.mpCartorioOficioSel.getNumero();
				
				List<MpBoleto> mpBoletoListXX = this.mpBoletos.mpBoletoByNumeroProtocoloList(numOfCoX, this.numeroIntimacao);
				for (MpBoleto mpBoletoXX : mpBoletoListXX) {
					//
//					System.out.println("MpBoletoBean.listaBoletoList() - ( " + sdfDMY.format(this.dataIntimacao) + " / " + 
//							mpBoletoX.getNomeSacado());
	//
					if (mpBoletoXX.getNomeSacado().indexOf(this.sdfDMY.format(this.dataIntimacao)) >= 0) {
						//
						this.mpBoletoList.add(mpBoletoXX);
					}						
				}
//				System.out.println("MpBoletoBean.listaBoletoList() - ( size = " + this.mpBoletoList.size());
				//
				if (this.mpBoletoList.size() == 0) {
					//
					MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) na Base de Dados ! (rc=03) (" +
														numOfCoX + " / " + sdfDMY.format(this.dataIntimacao) +
																				" / " + this.numeroIntimacao);
					return;
				}
			}
		}
		//
    }
    
    @SuppressWarnings({ "deprecation", "unused" })
	public void geraBoleto() {
    	//
    	if (this.indGeraBoleto) {
    		//
    		try {
				TimeUnit.SECONDS.sleep(10);
				//
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		//
    		this.indGeraBoleto = false;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = TRUE");    		
    		return;
    	} else {
    		//
    		this.indGeraBoleto = true;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = FALSE");
    	}
    	//
    	this.mensagemLog = "";
    	
//		MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 ( idBolP = " + 
//														" / numCode = " + this.numeroIntimacaoCode); 
		//
    	
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
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Bradesco_Registro_URI");
    	if (null == mpSistemaConfig) {
			MpFacesUtil.addErrorMessage("Erro Ambiente/Bradesco (NULL) ! ( " + ofComNumX + 
													"_Bradesco_Registro_URI ) ... Contactar o Suporte Técnico !");
    		return;
    	}
		//
    	this.ambienteBradesco = mpSistemaConfig.getValorT().toUpperCase().trim();	    
		
//    	System.out.println("MpBoletoBean.listaBoletoList() - ( Entrou 000 ( Ambiente Bradesco = " + this.ambienteBradesco +
//															" / Of. = " + this.mpCartorioOficioSel.getNumero().trim());
		// Verfica SÁBADO/DOMINGO e FERIADO !!!
		Date diaAtual = new Date();
		
		String diaSemAtual = MpAppUtil.diaSemana(diaAtual);
		//
		if (diaSemAtual.equals("Sábado") || diaSemAtual.equals("Domingo")) {
			//
			MpFacesUtil.addErrorMessage("Serviço Indisponivel fim de semana ! ( " + diaSemAtual + " ) " );
			return;
		}
		//
		String dataYMD = sdfYMD.format(diaAtual);
		String dataMD = sdfMD.format(diaAtual);
		
		if (dataMD.equals("11-02") || // Finados 
			dataMD.equals("12-25") || // Natal
			dataMD.equals("01-01")) { // Ano Novo
			//
			MpFacesUtil.addErrorMessage("Serviço Indisponivel feriado oficial ! ( " + dataMD + " ) " );
			return;
		}
		//		
    	mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Registro_Bradesco");
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
    		if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
    			this.indRegistro = false;
    		else
    			this.indRegistro = true;
    	}
    	
		// Trata Feriado Global ! MVPR-15112019 ...	
    	mpSistemaConfig = mpSistemaConfigs.porParametro("OfX_Feriado_" + dataYMD);
		if (null == mpSistemaConfig)
			assert(true); // nop
		else {
			MpFacesUtil.addErrorMessage("Serviço Indisponivel ! Feriado... ( " + dataYMD + " - " + 
																			mpSistemaConfig.getDescricao());
			return;    		
		}
		//
		mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Feriado_" + dataYMD);
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
			MpFacesUtil.addErrorMessage("Serviço Indisponivel ! Feriado ( " + dataYMD + " - " + 
																			mpSistemaConfig.getDescricao());
			return;    		
    	}
		
		// Verificar Horário de Funcionamento !
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    
	    String dateX = new Date().toString();	

	    String dtInicioS = "";
	    String dtFimS = "";

	    Date dtAtual = new Date();
	    Date dtInicio = dtAtual;
	    Date dtFim = dtAtual;
	    //
	    try {
	    	// Ex.: Tue Nov 14 09:28:07 BRST 2017
	    	//      012345678901234567890123456789
	    	dtAtual = sdf.parse(dateX.substring(11, 16)); 

	    	mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_HorarioBoleto");
	    	if (null == mpSistemaConfig) {
	    		//
//	    		if (null == this.mpCartorioOficioSel) {
//	    			//
//			    	dtInicioS = this.mpCartorioComarcaSel.getHorarioFuncionamento().substring(0, 5);
//			    	dtFimS = this.mpCartorioComarcaSel.getHorarioFuncionamento().substring(6, 11);
//	    		} else {
	    			//
			    	dtInicioS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(0, 5);
			    	dtFimS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(6, 11);
//	    		}
	    		//
		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	   		} else {
	   			//
    			if (!mpSistemaConfig.getIndValor()) {
	    			//
	    			MpFacesUtil.addErrorMessage("Serviço Temporariamente Indisponivel ! Tente mais tarde. ( " +
	    																								ofComNumX); 
	    			return;
	    		}
	    		//
		    	dtInicioS = mpSistemaConfig.getValorT().substring(0, 5);
		    	dtFimS = mpSistemaConfig.getValorT().substring(6, 11);
		    	
		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	    	}
	    	// Trata zeragem dos segundos/milisegundos ! 
			Calendar dataX = Calendar.getInstance();
			
			dataX.setTime(dtInicio);
			dataX.set(Calendar.SECOND, 0);
			dataX.set(Calendar.MILLISECOND, 0);
			dtInicio = dataX.getTime();

			dataX.setTime(dtFim);
			dataX.set(Calendar.SECOND, 0);
			dataX.set(Calendar.MILLISECOND, 0);
			dtFim = dataX.getTime();
			//	    	
	    	if ((dtAtual.after(dtInicio) && dtAtual.before(dtFim)))
	   			assert(true);
	   		else {
	    		MpFacesUtil.addErrorMessage("Fora do horário permitido ! ( De: " + 
	    			dtInicioS + " às " + dtFimS + " ) - " + sdf.format(dtAtual));
	    		return;
	   		}
	    	//
	    	// Tratamento para após às 16h00m ...
	    	//
	    	if (dtAtual.after(sdfHMS.parse("16:00:00"))) {
	    		//
	    		this.indApos16h = true;
	    		
	    		// MpFacesUtil.addErrorMessage("Após 16h00m... o vencimento será para próximo dia útil !!!"); 
	    	}
		    //
		} catch (ParseException e) {
			//
			MpFacesUtil.addErrorMessage("Error.001 - Tratamento Horas ! Contactar Suporte Técnico ! ( hI = " + 
					dtInicioS + " / hf = " + dtFimS + " / hA = " + dtAtual.toString() + " / e = " + e);
			return;
		}
    	
    	//
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.3 - Boleto Selecionado ! Contactar Suporte Técnico (null.Bol");  
			return;
		}

    	//
    	this.mpBoletoSelX = mpBoletos.porId(this.mpBoletoSelX.getId());
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.4 - Boleto Selecionado ! Contactar Suporte Técnico (idNull.Bol");  
			return;
		}
    	
    	// Trata Boleto Já Registrado Erro=69! Marcus/Prisco MR-20200528 !
    	if (null == this.mpBoletoSelX.getIndBradescoErro69())
    		assert(true); // nop
    	else {
    		//
			MpFacesUtil.addErrorMessage("Error.69X - Boleto Selecionado Já foi Registrado no Bradesco ! Contactar o Cartório Relacionado");  
			return;
    	}

    	// ---------------------------------------------------------------------
    	// Evitar Erro Troca Dados Boleto ! (Reportado Heraldo) -> MR-20200518 !
    	// ---------------------------------------------------------------------
    	if (!this.mpBoletoSelX.getNumeroIntimacao().equals(this.numeroIntimacao)) {
    		//
			MpFacesUtil.addErrorMessage("Error.3X - Boleto Selecionado ! Numero Intimação ( " + 
								this.mpBoletoSelX.getNumeroIntimacao() + " / " + this.numeroIntimacao);  
			return;
    	}
    	//
    	Date dataIntX = MpAppUtil.pegaSacadoDataProcotolo(this.mpBoletoSelX.getNomeSacado());
    	if (null == dataIntX) {
    		//
			MpFacesUtil.addErrorMessage("Error.3X1 - Boleto Selecionado ! Data Intimação ( " + 
																			this.mpBoletoSelX.getNomeSacado());  
			return;
    	}
    	//
    	if (null == this.dataIntimacao)
    		assert(true); // nop
    	else
	    	if (!dataIntX.equals(this.dataIntimacao)) {
	    		//
				MpFacesUtil.addErrorMessage("Error.3X2 - Boleto Selecionado ! Data Intimação ( " + 
									this.mpBoletoSelX.getNomeSacado() + " / " + dataIntX + " / " + this.dataIntimacao);  
				return;
	    	}
    	// ---------------------------------------------------------------------
    	
    	
    	// MVPR_20180904 ...
    	if (null == this.mpBoletoSelX.getIndLiquidado() || this.mpBoletoSelX.getIndLiquidado() == false)
    		assert(true); // nop
    	else {
			//
			MpFacesUtil.addErrorMessage("Boleto Selecionado... < LIQUIDADO > ! Favor Contactar Cartório.");  
			return;
		}
    	// MVPR_20200123 ...
    	if (null == this.mpBoletoSelX.getIndAberto() || this.mpBoletoSelX.getIndAberto() == false)
    		assert(true); // nop
    	else {
			//
			MpFacesUtil.addErrorMessage("Boleto Selecionado... < Nâo está ... EM ABERTO > ! Favor Contactar Cartório.");  
			return;
		}

    	// Verificar opção envio de Email ...
		
    	if (this.indEmailBoleto) {
			//
			if (this.emailBoleto.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Envio de Boleto por E-mail Ativo! Favor informar e-mail !!!");
				//
			} else {
				//
	    		String[] emailList = this.emailBoleto.trim().split(",");

				for (String emailX : emailList) {
					//
//					System.out.println("MpBoletoBean.gerarBoleto() - Email  ( " + emailX.trim());
					
					if (!MpAppUtil.isEmail(emailX.trim())) {
						//
						MpFacesUtil.addErrorMessage("E-mail Envio Boleto... formato inválido ! Contactar Suporte ( " +
																					emailX.trim());			
						return;			
					}
				}
			}
		}

    	//
//	    if (null == this.mpCartorioOficioSel)
//	    	ofComNumX = "Co" + this.mpCartorioComarcaSel.getNumero().trim();
//	    else
	    	ofComNumX = "Of" + this.mpCartorioOficioSel.getNumero().trim();
    	//
    	if (null == this.mpBoletoSelX.getNomePdfGerado() || this.mpBoletoSelX.getNomePdfGerado().isEmpty())
    		assert(true); // nop
    	else {
			//
    		if ((this.indApos16h) &&
    		    (null == this.mpBoletoSelX.getIndApos16h() || this.mpBoletoSelX.getIndApos16h().isEmpty()))
    		   assert(true); // Verificar se pode regerar? !
    		else {
    			//
//				MpFacesUtil.addErrorMessage("Boleto Selecionado ... foi ReGerado(2a.Via) !");
    			//
    	    	mpSistemaConfig = mpSistemaConfigs.porParametro(ofComNumX + "_Boleto_Printer");
    	    	if (null == mpSistemaConfig)
    	    		this.indBoletoPrinter = false;
    	    	else
    	    		this.indBoletoPrinter = mpSistemaConfig.getIndValor();
	
				ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

				if (this.indEmailBoleto) {
					//
					this.trataEnvioBoletoEmail();

	    			MpFacesUtil.addInfoMessage("Boleto ... Enviado! ( " + this.emailBoleto);
					//
				} else
				if (this.indBoletoPrinter) {
					//
				    URI uri;
					try {
						uri = new URI(extContext.getRequestScheme(),
						        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
						        extContext.getRequestContextPath(), null, null);
					    
					    String contextPathX = uri.toASCIIString();

//					    if (null == this.mpCartorioOficioSel)
//					    	RequestContext.getCurrentInstance().execute("printJS('" + contextPathX + File.separator +
//						    						"resources" + File.separator + "pdfs" + File.separator + "co" +
//							        				this.mpCartorioComarcaSel.getNumero() + File.separator + 
//							        				this.mpBoletoSelX.getNomePdfGerado() + "');");
//					    else
					    	RequestContext.getCurrentInstance().execute("printJS('" + contextPathX + File.separator +
		    						"resources" + File.separator + "pdfs" + File.separator + "of" +
			        				this.mpCartorioOficioSel.getNumero() + File.separator + 
			        				this.mpBoletoSelX.getNomePdfGerado() + "');");
		        		//
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				} else {
					//
			        try {
			        	//
		        		String pathX = ""; 
//		        		if (null == this.mpCartorioOficioSel)
//		        			pathX = extContext.getRealPath(File.separator + "resources" + File.separator + 
//		        												"pdfs" + File.separator + "co" +
//		        												this.mpCartorioComarcaSel.getNumero() + File.separator);
//		        		else
		        			pathX = extContext.getRealPath(File.separator + "resources" + File.separator + 
									"pdfs" + File.separator + "of" +
									this.mpCartorioOficioSel.getNumero() + File.separator);
		        		//	
		        		File fileXX = new File(pathX + this.mpBoletoSelX.getNomePdfGerado());
			        	//
		        	    FacesContext facesContextX = FacesContext.getCurrentInstance();
		        	    HttpServletResponse responseX = (HttpServletResponse) facesContextX.getExternalContext()
		        	    																		.getResponse();
		        	    //	
		        	    responseX.reset();
			        	responseX.setContentType("application/pdf");
			        	responseX.setContentLength((int) fileXX.length());
			        	responseX.setHeader("Content-Disposition", "attachment; filename=\"" + 
			        												this.mpBoletoSelX.getNomePdfGerado() + "\"");
			        	//	
			        	InputStream fisX = new FileInputStream(fileXX);
			        	ServletOutputStream osX = responseX.getOutputStream();
			        	//	
			            byte[] bufferData = new byte[1024];
			            int read=0;
			            //
			            while((read = fisX.read(bufferData))!= -1){
			            	osX.write(bufferData, 0, read);
			            }
			            osX.flush();
			            osX.close();
			            fisX.close();
			            //            
			            responseX.flushBuffer();
			             
			            facesContextX.responseComplete(); // FacesContext.getCurrentInstance().responseComplete();
			            //
			        } catch (IOException e) {
			        	e.printStackTrace();
			        }
				}					
		        //
				return;
    		}
		}
    	    	    	
		// 
    	// Trata código segurança somente para o Usuário !
    	//
    	if (this.mpSeguranca.isUsuario()) {
    		//
    		if (null == this.numeroIntimacaoCode || this.numeroIntimacaoCode.isEmpty()) {
    			//
    			MpFacesUtil.addErrorMessage("Informar o código Segurança ! ( Num. Documento = " + 
															this.mpBoletoSelX.getNumeroDocumento());
    			return;
    		}
    		// MR-11092019 (Bruno.2Of-Error) !
    		if (null == this.dataIntimacao) {
    			//
    			this.dataIntimacao = MpAppUtil.pegaSacadoDataProcotolo(this.mpBoletoSelX.getNomeSacado());

    			if (null == this.dataIntimacao) {
    				//
	    			MpFacesUtil.addErrorMessage("Error conversão Data Intimação ( Num.Doc = " + 
																	this.mpBoletoSelX.getNumeroDocumento());
	    			return;
    	        }
    		}
    		//
    		String numeroIntimacaoDigX = this.mpBoletoSelX.getNumeroIntimacaoCode().trim();

//			MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 (dig3 = " + numeroIntimacaoDigX + " / code = " + 
//						this.numeroIntimacaoCode.trim() + " / numInt = " + this.mpBoletoSelX.getNumeroIntimacao() + 
//						" / IntCode = " + this.mpBoletoSelX.getNumeroIntimacaoCode() + " )");
    		//
    		if (numeroIntimacaoDigX.equals(this.numeroIntimacaoCode.trim()))
    			assert(true); // nop
    		else {
    			MpFacesUtil.addErrorMessage("Código Segurança inválido ! Favor verificar. ( Num. Documento = " 
    												+ this.mpBoletoSelX.getNumeroDocumento() 
    												+ " / Código Informado = " + this.numeroIntimacaoCode);
    			return;
    		}
    	}
		// Salva Boleto para exclusão da LISTA ....
		this.mpBoletoSelAntX = this.mpBoletoSelX;
		
        try {
        	//
        	this.msgErro = "";
        	
        	BoletoViewer boletoViewer = this.boletoCriar(); // this.boletoCriado();
        	if (null == boletoViewer) {
        		if (this.msgErro.isEmpty())
        			MpFacesUtil.addErrorMessage("Error Geração BOLETO! Favor Verificar ( Num.Documento = " + 
        															this.mpBoletoSelX.getNumeroDocumento());
        		else
        			MpFacesUtil.addErrorMessage("Error Geração BOLETO!! Favor Verificar ( " + this.msgErro);
        		
        		// =======================//
        		// Refresh do Formulário! //
        		// =======================//
				org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("frmSelBol");
        		// =======================//
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
        			
        	        this.gravaBoletoLog();

        	        // =======================//
            		// Refresh do Formulário! //
            		// =======================//
    				org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("frmSelBol");
    				//
        			return;
        		}
        	//
    		String nossoNumero = this.mpBoletoSelX.getNossoNumero().replaceAll("/", "");

    		nossoNumero = nossoNumero.replaceAll("-", "");
    		if (nossoNumero.isEmpty()) nossoNumero = "00000000000"; // ???????
        	//
    		String contadorPrt = "";
    		if (null == this.mpBoletoSelX.getContadorImpressao())
        		contadorPrt = "0";
    		else
        		contadorPrt = this.mpBoletoSelX.getContadorImpressao().toString();
    		//
//        	this.nomePdf = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + nossoNumero + "_" +
//        																			contadorPrt + ".pdf";        	
        	this.nomePdfX = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + this.sdfYYYYMMDD.format(new Date()) 
        																				+ "_" + nossoNumero + ".pdf";     	
        	this.nomePdf = this.nomePdfX;
        	
        	this.arquivoPdf = boletoViewer.getPdfAsFile(System.getProperty("user.home") + File.separator + 
        																					this.nomePdf);
        	//
//        	if (this.mpCartorioOficioSel.getNumero().equals("2"))
        	this.atualizaPdf(this.arquivoPdf);
        	//
        	InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(
        																	this.arquivoPdf.getAbsolutePath());  
       	
        	this.fileX = new DefaultStreamedContent(stream, "application/pdf", this.nomePdf);

//    		MpAppUtil.PrintarLn("MpBoletoBean.download() - 000.0 ( " + this.arquivoPdf.getAbsolutePath());
    		//    		
		} catch (Exception e) {
			e.printStackTrace();
		}  

        // Atualiza Nome + Data/Hora Impressão Boleto ...
        this.mpBoletoSelX.setDataImpressao(new Date());
        this.mpBoletoSelX.setDataHoraImpressao(new Date());
        this.mpBoletoSelX.setIndImpressao(true);
        this.mpBoletoSelX.setNomePdfGerado(this.nomePdfX);
        
        // Trata contador de Impressão ...
        if (null == this.mpBoletoSelX.getContadorImpressao())
        	this.mpBoletoSelX.setContadorImpressao(1);
        else
        	this.mpBoletoSelX.setContadorImpressao(this.mpBoletoSelX.getContadorImpressao() + 1);
        //
        if (null == this.mpBoletoSelX.getNumeroDocumento() || this.mpBoletoSelX.getNumeroDocumento().isEmpty()) 
        	this.mpBoletoSelX.setNumeroDocumento("00000"); // ?????
        if (null == this.mpBoletoSelX.getNossoNumero() || this.mpBoletoSelX.getNossoNumero().isEmpty()) 
        	this.mpBoletoSelX.setNossoNumero("00000"); // ?????

        // Atualiza número guia gerado !
        if (null == this.numeroGuiaGerado) { // Reimpressão Boleto! MVPR-29082018
        	MpBoleto mpBoletoGuia = this.mpBoletos.porId(this.mpBoletoSelX.getId());
        	if (null == mpBoletoGuia) {
    			MpFacesUtil.addErrorMessage("Error-X Geração BOLETO(NULL)...Captura número GUIA(NULL)! Verificar!");
    			return;
        	} else
        		if (null == mpBoletoGuia.getNumeroGuiaGerado()) {
        			MpFacesUtil.addErrorMessage("Error-XX Geração BOLETO...Captura número GUIA(NULL)! Verificar!");
        			return;
        		} else
        			this.mpBoletoSelX.setNumeroGuiaGerado(mpBoletoGuia.getNumeroGuiaGerado());
        	// assert(true);
        } else {
        	//
        	this.mpBoletoSelX.setNumeroGuiaGerado(this.numeroGuiaGerado);
        }
        	        
        // Atualiza indApos16h !
        if (this.indApos16h)
            this.mpBoletoSelX.setIndApos16h("*");
        else
            this.mpBoletoSelX.setIndApos16h("");
        //
        this.mpBoletoSelX = this.mpBoletoService.salvar(this.mpBoletoSelX);
        
        // Trata Atualização da LISTA ...
        this.mpBoletoList.remove(this.mpBoletoSelAntX);
        this.mpBoletoList.add(this.mpBoletoSelX);

        // Grava Boleto Log...
        // ===================
        this.gravaBoletoLog();
        
        // ---
        // Localiza Titulo e Altera STATUS TITULO ...
//      MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() Titulo ( nomeSacado = " + 
//      		this.mpBoletoSelX.getNomeSacado() +
//      		" / posX = " + posX);
//        
        Date dataX = MpAppUtil.pegaSacadoDataProcotolo(this.mpBoletoSelX.getNomeSacado());
        if (null == dataX) {
        	//
    		MpFacesUtil.addInfoMessage("Erro Atualização Titulo ! ( " + this.mpBoletoSelX.getNomeSacado());
    		//
        } else {
			//
			String numOfCoY = "";
//			if (null == this.mpCartorioOficioSel)
//				numOfCoY = this.mpCartorioComarcaSel.getNumero();
//			else
				numOfCoY = this.mpCartorioOficioSel.getNumero();
			//
            List<MpTitulo> mpTituloList = this.mpTitulos.mpTituloByNumeroDataProtocoloList(numOfCoY,
            														this.mpBoletoSelX.getNumeroIntimacao(), dataX);
			for (MpTitulo mpTituloX : mpTituloList) {
				//
//				MpAppUtil.PrintarLn("Boleto Impresso... atualiza Status Titulo ... ( " + 
//									mpTituloX.getId() + " / " + mpTituloX.getNumeroProtocolo() + " / " + 
//									mpTituloX.getDataProtocolo());
				//
				mpTituloX.setStatus("DEPOSITO EMITIDO"); // Vide.Prisco(01022018!10h00)
				//mpTituloX.setComplemento("Impresso Complemento???");
				//
				mpTituloX = this.mpTituloService.salvar(mpTituloX);
			}
    		//
        }
        
        // ---

    	MpFacesUtil.addInfoMessage("Boleto Gerado !");
        //
    	String ofCoZ = "";
//    	if (null == this.mpCartorioOficioSel)
//    		ofCoZ = "Co" + this.mpCartorioComarcaSel.getNumero();
//    	else
    		ofCoZ = "Of" + this.mpCartorioOficioSel.getNumero();
    	//
	   	mpSistemaConfig = mpSistemaConfigs.porParametro(ofCoZ + "_Boleto_Printer");
		if (null == mpSistemaConfig)
			this.indBoletoPrinter = false;
		else
			this.indBoletoPrinter = mpSistemaConfig.getIndValor();
			
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();    		

		if (this.indEmailBoleto) {
			//
			this.trataEnvioBoletoEmail();

			MpFacesUtil.addInfoMessage("Boleto ... Enviado ! ( " + this.emailBoleto);
			//
		} else
		if (this.indBoletoPrinter) {
			//
		    URI uri;
			try {
				//
	        	InputStream fis = new FileInputStream(this.arquivoPdf);
	        	
	        	String pathX = "";
//	        	if (null == this.mpCartorioOficioSel)
//	        		pathX = extContext.getRealPath(File.separator + "resources" + File.separator +
//	    												"pdfs" + File.separator + "co" + 
//	    												this.mpCartorioComarcaSel.getNumero() + File.separator);
//	        	else
	        		pathX = extContext.getRealPath(File.separator + "resources" + File.separator +
														"pdfs" + File.separator + "of" + 
														this.mpCartorioOficioSel.getNumero() + File.separator);
        		//
	    		File targetFile = new File(pathX + this.nomePdfX);
	    		OutputStream fos = new FileOutputStream(targetFile);

	        	
	            byte[] bufferData = new byte[1024];
	            int read=0;
	            
	            while((read = fis.read(bufferData))!= -1) {
	            	//
	            	fos.write(bufferData, 0, read);
	            }
	            //
	            fos.flush();
	            fos.close();

	            fis.close();
	    		
	            //
				uri = new URI(extContext.getRequestScheme(),
				        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
				        extContext.getRequestContextPath(), null, null);

				String contextPathX = uri.toASCIIString();
				
//				if (null == this.mpCartorioOficioSel)
//					RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
//												"/resources/pdfs/co" + this.mpCartorioComarcaSel.getNumero() + 
//												"/" + this.mpBoletoSelX.getNomePdfGerado() + "');");
//				else
					RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
												"/resources/pdfs/of" + this.mpCartorioOficioSel.getNumero() + 
												"/" + this.mpBoletoSelX.getNomePdfGerado() + "');");
	    		//
			} catch (URISyntaxException |  IOException e) {
				e.printStackTrace();
			}		    
		} else {
			//
			try {
				//
	        	this.response.setContentType("application/pdf");
	        	this.response.setContentLength((int) this.arquivoPdf.length());
	        	this.response.setHeader("Content-Disposition", "attachment; filename=\"" + this.nomePdf + "\"");
	
	        	InputStream fis = new FileInputStream(this.arquivoPdf);
	        	
	        	
	        	String pathX = ""; 
//	        	if (null == this.mpCartorioOficioSel)
//	        		pathX = extContext.getRealPath("//resources//pdfs//co" + 
//	    														this.mpCartorioComarcaSel.getNumero() + "//");
//	        	else
        			pathX = extContext.getRealPath("//resources//pdfs//of" + 
        														this.mpCartorioOficioSel.getNumero() + "//");
	    		File targetFile = new File(pathX + this.nomePdfX);
	
	    		OutputStream fos = new FileOutputStream(targetFile);
	
	        	ServletOutputStream os = this.response.getOutputStream();
	
	            byte[] bufferData = new byte[1024];
	            int read=0;
	            
	            while((read = fis.read(bufferData))!= -1) {
	            	//
	            	os.write(bufferData, 0, read);
	            	fos.write(bufferData, 0, read);
	            }
	            //
	            os.flush();
	            os.close();
	            
	            fos.flush();
	            fos.close();
	
	            fis.close();
	            //
//	    		System.out.println("MpBoletoBean.geraBoleto() ( tagetFile = " + targetFile.getAbsolutePath());
	
	    		this.response.flushBuffer();
	             
	            this.facesContext.responseComplete(); // FacesContext.getCurrentInstance().responseComplete();
	    		//
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
        }
    }

	private void atualizaPdf(File fileX) {
		//
		PdfReader pdfReader = null;
		PdfStamper pdfStamper = null;
		//
		try {
			//
			pdfReader = new PdfReader(fileX.getAbsolutePath());

			File fileOutX = new File(fileX.getPath().replace(fileX.getName(), "out_" + fileX.getName()));

			pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(fileOutX.getPath()));
			//
		    //Create BaseFont instance.
		    BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, 
		    																		BaseFont.NOT_EMBEDDED);
		    //Get the number of pages in pdf.
		    int pages = pdfReader.getNumberOfPages(); 
 
		    //Iterate the pdf through pages.
		    for (int i=1; i<=pages; i++) { 
				//Contain the pdf data.
				PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
	 
				pageContentByte.beginText();
				//Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 10);
	 
				pageContentByte.setTextMatrix(430, 440);
	 
				//Write text
		        DecimalFormat df = new DecimalFormat();
		        df.applyPattern("R$ ##,###,##0.00");
				
		        String valCobradoX = df.format(this.mpBoletoSelX.getValorCobrado());
				
//		        System.out.println("AtualizaPdf(0) ......................................... ( " + valCobradoX);
		        valCobradoX = valCobradoX.replace(",", "X");
		        valCobradoX = valCobradoX.replace(".", ",");
		        valCobradoX = valCobradoX.replace("X", ".");
//				System.out.println("AtualizaPdf(1) ......................................... ( " + valCobradoX);
				
				pageContentByte.showText(valCobradoX);
				pageContentByte.endText();
		    }
		    //
			pdfStamper.close();
			pdfReader.close();

			// Apaga arquivo original !
			boolean success = fileX.delete();
			if (!success) {
				System.out.println("Exclusão falhou!");
				System.exit(0);
			}
			// Renomeia out_arquivo_original para arquivo_original !
			if (fileOutX.renameTo(fileX))
				assert (true); // nop
			else {
				System.out.println("Renomeação falhou!");
				System.exit(0);
			}
			//
			// Copia para a area publica do site para o PrintJS ... funcionar !
			// --------------------------------------------------------------------------------
			
//			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//			
//			String pathX = extContext.getRealPath(File.separator + this.capturaPathResourcePdf() + File.separator);
//			
//			//		
//    		File fileOutWeb = new File(pathX + File.separator + fileX.getName());
//			
//    		pdfReader = new PdfReader(fileX.getAbsolutePath());
//    		
//    		pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(fileOutWeb.getAbsolutePath()));
//
////			System.out.println("AtualizaPdf(00) ......................................... ( pdfStamperWeb = " + 
////																						fileOutWeb.getAbsolutePath());
//		    //
//			pdfStamper.close();
//			pdfReader.close();
//
//			//			this.arquivoPdf = fileOutX;
			//
		} catch (DocumentException dex) {
			dex.printStackTrace();
			// return "Erro 001 - Exception ! ( dex = " + dex;
		} catch (Exception ex) {
			ex.printStackTrace();
			// return "Erro 002 - Exception ! ( ex = " + ex;
		}
		//
	}    
    
	public void gravaBoletoLog() {
		//
        MpBoletoLog mpBoletoLog = new MpBoletoLog();
        
        mpBoletoLog.setDataGeracao(new Date());
        mpBoletoLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
        mpBoletoLog.setUsuarioEmail(mpSeguranca.getEmailUsuario()); // MR-07112019 !
        mpBoletoLog.setIndUserWeb(mpSeguranca.getIndUserWebUsuario()); // MR-07112019 !

//        if (null == this.mpCartorioOficioSel)
//        	mpBoletoLog.setNumeroOficio(this.mpCartorioComarcaSel.getNumero());
//        else
        	mpBoletoLog.setNumeroOficio(this.mpCartorioOficioSel.getNumero());
        
        mpBoletoLog.setNumeroDocumento(this.mpBoletoSelX.getNumeroDocumento());
        mpBoletoLog.setValorDocumento(this.mpBoletoSelX.getValorDocumento());
        mpBoletoLog.setBoletoRegistro(this.boletoRegistro);
        mpBoletoLog.setBoletoRegistroRetorno(this.boletoRegistroRetorno);
        mpBoletoLog.setAmbienteBradesco(this.ambienteBradesco);
        mpBoletoLog.setMensagem(this.mensagemLog);
        // MR-26032019 (Solicitação 2Of. Rel.BoletosImpressos !
		mpBoletoLog.setProtocolo(sdfDMY.format(this.dataIntimacao) + "-" + this.numeroIntimacao);
		//
        mpBoletoLog.setNumeroGuia(this.numeroGuiaGerado + "");
        mpBoletoLog.setValorCPMF(this.mpBoletoSelX.getValorCPMF());
        mpBoletoLog.setValorCobrado(this.mpBoletoSelX.getValorCobrado());
        mpBoletoLog.setValorTarifa(this.mpBoletoSelX.getValorTarifa());
        mpBoletoLog.setValorLeis(this.mpBoletoSelX.getValorLeis());

        mpBoletoLog.setIndCancelamento(false);
        //
        
        mpBoletoLog = this.mpBoletoLogService.salvar(mpBoletoLog);
        //
        this.enviaEmailLog(mpBoletoLog);
	}

	public void enviaEmailLog(MpBoletoLog mpBoletoLogEmail) {
		//
		String paramEmailLog = "";
//		if (null ==this.mpCartorioOficioSel)
//			paramEmailLog = "Co" + this.mpCartorioComarcaSel.getNumero() + "_EmailBoleto";
//		else
			paramEmailLog = "Of" + this.mpCartorioOficioSel.getNumero() + "_EmailBoleto";

		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramEmailLog);
	    if (null == mpSistemaConfig)
	   		assert(true);
	    else {
			//
	    	this.emailLog = mpSistemaConfig.getValor();
	    	//
	    	if (!this.emailLog.isEmpty()) {
	    		
	    		String[] emailLogList = this.emailLog .split(",");
	    		
	    		for (String emailLogX : emailLogList) {
	    			//
		    		if (!MpAppUtil.isEmail(emailLogX.trim())) {
		    			//
		    			MpFacesUtil.addErrorMessage("E-mail Boleto Log... formato inválido ! Contactar Suporte ( " +
		    																						emailLogX.trim());			
		    			return;			
		    		}
	    		}
	    		//
	    		String subject = "Protesto RJ Capital ( Emissao de BOLETO Site - Oficio: " 
	    											+ mpBoletoLogEmail.getNumeroOficio() + " )"; // MVPR-05112019
	    		
	    	    Map<String,Object> mapX = new HashMap<String,Object>(); 
	    	    
	    	    mapX.put("mpBoletoLog", mpBoletoLogEmail);
	    	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

	    	    String message = new VelocityTemplate(getClass().getResourceAsStream(
	    											"/emails/mpBoletoImpressoLog.template")).merge(mapX).toString();
	    		
	    		String from = "suporte@protestorjcapital.com.br";
	    		//
	    		try {
	    			new MpSendMailLOCAWEB(emailLogList, subject, message, from);
	    			//
	    			return;
	    		} catch (Exception e) {
	    			//
	    			MpFacesUtil.addErrorMessage("Error-005-1 (Envio E-mail). Contactar Suporte Técnico ! ! ( e= " +
	    																								e.toString());
	    			System.out.println("MpRegeraSenhaBean() - Error-005-1 (Envio E-mail LOCAWEB) ( e= " + e.toString());
	    			//
	    			return;
	    		}
	    	}
		}
	}

	private void trataEnvioBoletoEmail() {
		//
    	this.emailLog = this.emailBoleto;
    	//
    	if (!this.emailLog.isEmpty()) {
    		
    		String[] emailLogList = this.emailLog .split(",");
    		
    		for (String emailLogX : emailLogList) {
    			//
	    		if (!MpAppUtil.isEmail(emailLogX.trim())) {
	    			//
	    			MpFacesUtil.addErrorMessage("E-mail Boleto Log... formato inválido ! Contactar Suporte ( " +
	    																						emailLogX.trim());			
	    			return;			
	    		}
    		}
    		//
    		String subject = "Protesto RJ Capital ( Emissao Boleto Site - Oficio: " 
    											+ this.mpCartorioOficioSel.getNumero() + " )"; // MVPR-05112019
    		
    	    Map<String,Object> mapX = new HashMap<String,Object>(); 
    	    
    	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

    	    String message = new VelocityTemplate(getClass().getResourceAsStream(
    													"/emails/mpBoletoAnexo.template")).merge(mapX).toString();
    		
    		String from = "suporte@protestorjcapital.com.br";
    		//
//			System.out.println("MpBoletoBean.trataEnvioBoletoEmail() - Entrou 000 ( " + 
//																			this.mpBoletoSelX.getNomePdfGerado());
    		//
    		try {
    			//
//    			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//    			
//    			String pathX = extContext.getRealPath(File.separator + this.capturaPathResourcePdf() + File.separator);

    			String pathX = System.getProperty("user.home");
    			
    			new MpSendMailLOCAWEB(emailLogList, subject, message, from, pathX + File.separator + 
    																		this.mpBoletoSelX.getNomePdfGerado()); 
    			//
    			return;
    		} catch (Exception e) {
    			//
    			MpFacesUtil.addErrorMessage("Error-005-2 (Envio E-mail). Contactar Suporte Técnico ! ! ( e= " +
    																								e.toString());
    			System.out.println("MpBoletoBean() - Error-005-2 (Envio E-mail LOCAWEB) ( e= " + e.toString());
    			//
    			return;
    		}
    	}
		
	}
		
	public BoletoViewer boletoCriar() {
		//
//		MpAppUtil.PrintarLn("MpBoletoBean.boletoCriar() - 000");

		String nomeCedente = "";
		Integer agenciaCodigoCedente = 0;
		Integer agenciaContaCedente = 0;
		
//		if (null == this.mpCartorioOficioSel) {
//			//
//			nomeCedente = this.mpCartorioComarcaSel.getNomeCedente();
//			agenciaCodigoCedente = this.mpCartorioComarcaSel.getAgenciaCodigoCedente();
//			agenciaContaCedente = this.mpCartorioComarcaSel.getAgenciaContaCedente();
//		} else {
			//
			nomeCedente = this.mpCartorioOficioSel.getNomeCedente();
			agenciaCodigoCedente = this.mpCartorioOficioSel.getAgenciaCodigoCedente();
			agenciaContaCedente = this.mpCartorioOficioSel.getAgenciaContaCedente();
			
			// Trata Captura Agencia/Conta em MpSistemaConfig ! MVPR-20201107 !
			String agenciaCodigoContaCedente = this.mpSeguranca.capturaAgenciaContaBradesco();
			if (null == agenciaCodigoContaCedente || agenciaCodigoContaCedente.isEmpty())
				assert(true); // nop
			else {
				//
				agenciaCodigoCedente = Integer.parseInt(agenciaCodigoContaCedente.substring(0,4));
				agenciaContaCedente  = Integer.parseInt(agenciaCodigoContaCedente.substring(4));
			}
			//
//		}
		
		// Cedente
		Cedente cedente;
//		if (null == this.mpCartorioOficioSel)
//			cedente = new Cedente(nomeCedente, this.mpCartorioComarcaSel.getCnpj());
//		else
			cedente = new Cedente(nomeCedente, this.mpCartorioOficioSel.getCnpj());
		// Sacado
		Sacado sacado = new Sacado(this.mpBoletoSelX.getNomeSacado());
		
		// Endereço do sacado
		Endereco endereco = new Endereco();
//		endereco.setUF(UnidadeFederativa.MG);// this.mpBoletoSel.getMpEnderecoLocal().getMpEstadoUF().getCodigo() ;
//		endereco.setLocalidade(this.mpBoletoSel.getMpEnderecoLocal().getCidade());
//		endereco.setCep(new CEP(this.mpBoletoSel.getMpEnderecoLocal().getCep()));
		endereco.setBairro(this.mpBoletoSelX.getMpEnderecoLocal().getBairro());
//		endereco.setLogradouro(this.mpBoletoSel.getMpEnderecoLocal().getLogradouro());
//		endereco.setNumero(this.mpBoletoSel.getMpEnderecoLocal().getNumero() + " / " + 
//							this.mpBoletoSel.getMpEnderecoLocal().getComplemento());
		
		sacado.addEndereco(endereco);
		
		// Criando o título
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
		
//		if (null == this.mpCartorioOficioSel) {
//			//
//			contaBancaria.setAgencia(new Agencia(agenciaCodigoCedente, 
//					 								this.mpCartorioComarcaSel.getAgenciaCodigoCedenteDig()));
//			contaBancaria.setNumeroDaConta(new NumeroDaConta(agenciaContaCedente,
//					 								this.mpCartorioComarcaSel.getAgenciaContaCedenteDig()));
//			contaBancaria.setCarteira(new Carteira(this.mpCartorioComarcaSel.getCarteira()));
//		} else {
			//
			contaBancaria.setAgencia(new Agencia(agenciaCodigoCedente, 
					 								this.mpCartorioOficioSel.getAgenciaCodigoCedenteDig()));
			contaBancaria.setNumeroDaConta(new NumeroDaConta(agenciaContaCedente,
					 								this.mpCartorioOficioSel.getAgenciaContaCedenteDig()));
			contaBancaria.setCarteira(new Carteira(this.mpCartorioOficioSel.getCarteira()));
//		}

		//
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento(this.mpBoletoSelX.getNumeroDocumento());
		//
		String nossoNumero = this.mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		
		nossoNumero = nossoNumero.replaceAll("-", "");
		if (nossoNumero.isEmpty()) nossoNumero = "00000000000"; // ???????
		
		if (nossoNumero.length() < 12)
			titulo.setNossoNumero(nossoNumero);
		else
			titulo.setNossoNumero(nossoNumero.substring(0,11)); // Numérico e Tam.max=11?
		//
		titulo.setDigitoDoNossoNumero(this.mpBoletoSelX.getNossoNumeroDig()); 
		// mpGeradorDigitoVerificador.gerarDigito(contaBancaria.getCarteira().getCodigo(), titulo.getNossoNumero()));
		//
		titulo.setValor(this.mpBoletoSelX.getValorDocumento());
		titulo.setValorCobrado(this.mpBoletoSelX.getValorCobrado());
	// 
		titulo.setAcrecimo(this.mpBoletoSelX.getValorAcrescimo());
		// 
		titulo.setDesconto(this.mpBoletoSelX.getValorTarifa());
		titulo.setDeducao(this.mpBoletoSelX.getValorCPMF());
		titulo.setMora(this.mpBoletoSelX.getValorLeis());
//		titulo.setAcrecimo(this.mpBoletoSelX.getValorIss());
		
//		ParametrosBancariosMap parametrosBancariosMap = new ParametrosBancariosMap(); 

		titulo.setDataDoDocumento(this.mpBoletoSelX.getDataDocumento());

		// Trata dados após 16h00m ...
		// ---------------------------
		if (this.indApos16h)
			titulo.setDataDoVencimento(this.mpBoletoSelX.getDataVencimento1()); 
		else
			titulo.setDataDoVencimento(this.mpBoletoSelX.getDataVencimento());
		// ---------------------------
		
		titulo.setTipoDeDocumento(TipoDeTitulo.DSI_DUPLICATA_DE_SERVICO_PARA_INDICACAO);
		titulo.setAceite(Aceite.N);
	
		// Dados do boleto (Defaut) ...		
		Boleto boleto = new Boleto(titulo);
		
		/*
		 * A área do boleto destinada ao "Nosso Número" deverá exibir a informação
		 * "Código da Carteira/Nosso Número". Ex: 109/1234567-8.
		 */
//		String nossoNumeroParaExibicao = String.format("%d/%s-%s", 
//		    titulo.getContaBancaria().getCarteira().getCodigo(),
//		    this.mpBoletoSelX.getNossoNumero(),
//		    titulo.getDigitoDoNossoNumero());  
//		boleto.addTextosExtras("txtFcNossoNumero", nossoNumeroParaExibicao); 
//		boleto.addTextosExtras("txtRsNossoNumero", nossoNumeroParaExibicao); 		

		boleto.addTextosExtras("txtFcNossoNumero", this.mpBoletoSelX.getNossoNumero()); 		
		boleto.addTextosExtras("txtRsNossoNumero", this.mpBoletoSelX.getNossoNumero()); 		
		boleto.addTextosExtras("txtFcNumeroDocumento", this.mpBoletoSelX.getNumeroDocumento()); 		
		boleto.addTextosExtras("txtRsNumeroDocumento", this.mpBoletoSelX.getNumeroDocumento()); 		
		boleto.addTextosExtras("txtFcEspecie", "R$"); 		
		boleto.addTextosExtras("txtRsEspecie", "R$");
		
		// MVPR-21062019 NÂO SAI VALOR COBRADO 20.OFicio -> BrunoReportou! ... 
		String valCobradoX = (this.mpBoletoSelX.getValorCobrado() + "").replace(".", ",");
//		System.out.println("MpBoletoBean.000 ................................... ( valCobradoX = " + valCobradoX);		
		boleto.addTextosExtras("txtRsValorCobrado", valCobradoX);		
		//
		boleto.addTextosExtras("txtFcEspecieDocumento", this.mpBoletoSelX.getEspecieDocumento()); 	
		
		// Trata dados após 16h00m ...
		// ---------------------------
		if (this.indApos16h) {
			boleto.addTextosExtras("txtFcLinhaDigitavel", this.mpBoletoSelX.getLinhaDigitavel1()); 		
			boleto.addTextosExtras("txtRsLinhaDigitavel", this.mpBoletoSelX.getLinhaDigitavel1()); 		
			boleto.addTextosExtras("txtFcAgenciaCodigoCedente", this.mpBoletoSelX.getAgenciaCodigoCedente1()); 		
			boleto.addTextosExtras("txtRsAgenciaCodigoCedente", this.mpBoletoSelX.getAgenciaCodigoCedente1()); 
			//
			Image img1 = CodigoDeBarras.valueOf(this.mpBoletoSelX.getCodigoBarras()).toImage();
			boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img1).getImage());		
		} else {
			boleto.addTextosExtras("txtFcLinhaDigitavel", this.mpBoletoSelX.getLinhaDigitavel());
			boleto.addTextosExtras("txtRsLinhaDigitavel", this.mpBoletoSelX.getLinhaDigitavel());
			boleto.addTextosExtras("txtFcAgenciaCodigoCedente", this.mpBoletoSelX.getAgenciaCodigoCedente()); 		
			//
			Image img = CodigoDeBarras.valueOf(this.mpBoletoSelX.getCodigoBarras()).toImage();
			boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img).getImage());		
		}
		
		boleto.addTextosExtras("txtFcSacadoL3", this.mpBoletoSelX.getCpfCnpj()); 		

		//---
		
		boleto.setLocalPagamento("Preferencialmente nas Agências Bradesco.");
		boleto.setInstrucaoAoSacado("Evite multas, pague em dias suas contas.");
		
		boleto.setInstrucao1("1-Guia valida apenas para pagamento em DINHEIRO na data do vencimento.");
		boleto.setInstrucao2("2-Nao receber apos o vencimento.");
		boleto.setInstrucao3("3-Retorne ao Tabelionato, com a guia autenticada, no 1o. dia util apos o pagamento");
		boleto.setInstrucao4("para retirar seu 'titulo' quitado a fim de evitar o protesto do titulo.");
		boleto.setInstrucao5("4-Dirija-se ao 7o. Oficio para evitar a distribuicaoo do protesto.");
		boleto.setInstrucao6(" ");
		boleto.setInstrucao7(" ");
		boleto.setInstrucao8("                                           GUIA No. ?????  Hora  ??:??:??");

		// Dados do boleto (Captura Sistema Configuração) ...
		
		MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoLocalPagamento");
		if (null != mpSistemaConfig) boleto.setLocalPagamento(mpSistemaConfig.getDescricao());	
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucaoAoSacado");
		if (null != mpSistemaConfig) boleto.setInstrucaoAoSacado(mpSistemaConfig.getDescricao());	
		
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao1");
		if (null != mpSistemaConfig) boleto.setInstrucao1(mpSistemaConfig.getDescricao());	
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao2");
		if (null != mpSistemaConfig) boleto.setInstrucao2(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao3");
		if (null != mpSistemaConfig) boleto.setInstrucao3(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao4");
		if (null != mpSistemaConfig) boleto.setInstrucao4(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao5");
		if (null != mpSistemaConfig) boleto.setInstrucao5(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao6");
		if (null != mpSistemaConfig) boleto.setInstrucao6(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao7");
		if (null != mpSistemaConfig) boleto.setInstrucao7(mpSistemaConfig.getDescricao());
		mpSistemaConfig = this.mpSistemaConfigs.porParametro("BoletoInstrucao8");
		if (null != mpSistemaConfig) boleto.setInstrucao8(mpSistemaConfig.getDescricao());
				
		// =================================================

		// Trata hora impressão boleto ! 
		String horaHMS = sdfHMS.format(new Date());
		
		// Trata registro Boleto (WebServices) ... BRADESCO !
		if (this.indRegistro) { // Vide SistConfig !
			if (null == this.mpBoletoSelX.getDataHoraRegistro())
				this.boletoRegistrar(this.mpBoletoSelX);
			else {
				// Trata registro novamente para boletos reimpressos após às 16:00:00.00 (16H) ...
		    	Calendar calX = Calendar.getInstance();	

				calX.setTime(new Date());
				calX.set(Calendar.HOUR_OF_DAY, 16);
				calX.set(Calendar.MINUTE, 0);
				calX.set(Calendar.SECOND, 0);
				calX.set(Calendar.MILLISECOND, 0);
				
				Date dataHora16H = calX.getTime();
				//
				Date dataHoraAtual = new Date();
				
				if (dataHoraAtual.after(dataHora16H)) {
					//
					if (this.mpBoletoSelX.getDataHoraRegistro().before(dataHora16H))
						this.boletoRegistrar(this.mpBoletoSelX);
				}
			}
		} else {
			// Sem REGISTRO no BRADESCO gera número da guia = 0 !
			this.mpBoletoSelX.setNumeroGuiaGerado(0);
			this.numeroGuiaGerado = 0 ;
		}
		
		// ===============================================================//
		// Trata Erro=69 Registro Bradesco! Marcus/Prisco - MR-20200528 ! //
		// toDo: Refresh Lista na Tela?                                   //
		// ===============================================================//
		if (null == this.mpBoletoSelX.getIndBradescoErro69())
			assert(true); // nop
		else {
			//
	        this.mpBoletoSelX = this.mpBoletoService.salvar(this.mpBoletoSelX);
	        
	        // Trata Atualização da LISTA ...
	        this.mpBoletoList.remove(this.mpBoletoSelAntX);
	        this.mpBoletoList.add(this.mpBoletoSelX);
			
			return null;
		}
		// ===============================================================//

		//
		if (null == this.numeroGuiaGerado)
			this.numeroGuiaGerado = this.mpBoletoSelX.getNumeroGuiaGerado();
		//
		if (null != this.mpBoletoSelX.getBoletoInstrucao8()) 
			if (this.ambienteBradesco.toUpperCase().equals("HOMOLOGACAO"))
				boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado + 
												"     HORA   " + horaHMS + "  ( Ambiente: HOMOLOCACAO )");
			else
				boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado + 
												"     HORA   " + horaHMS);
		
		// Usa template Gerado no Writer.OpenOffice .odt) ...
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		
		String pathX = extContext.getRealPath("//resources//pdfs//");
		
		File mpTemplate = new File(pathX + "MpBoletoTemplateSemSacadorAvalista.pdf");

		BoletoViewer boletoViewer = new BoletoViewer(boleto, mpTemplate);
        //
		return boletoViewer;
    }
	
	private void boletoRegistrar(MpBoleto mpBoletoR) {
		//
//		System.out.println("MpBoletoBean.boletoRegistrar() - ( CpfCnpj = " + mpBoletoR.getCpfCnpj());		
		
		// Trata Sequência número GUIA do Boleto ...
		// =================================================
		this.numeroGuiaGerado = 0;
//		this.numeroGuiaGeradoAnt = 0;
		//
		this.codigoErroRegistro = "";
		//
		
		//-----------------------------------//
		// Trata captura Numeração Guia ...  //
		//-----------------------------------//
		String msgGuiaX = this.mpBoletoService.capturaNumeroGuiaBoleto(this.mpCartorioOficioSel, this.mpBoletoSelX);

		if (msgGuiaX.indexOf("OK=") >= 0)
			this.numeroGuiaGerado = Integer.parseInt(msgGuiaX.substring(3));
		else {
			//
			this.mensagemLog = this.mensagemLog + msgGuiaX;
		}
		//
		String ofCoNumX = "";
//		if (null == this.mpCartorioOficioSel)
//			ofCoNumX = "Co" + this.mpCartorioComarcaSel.getNumero();
//		else
			ofCoNumX = "Of" + this.mpCartorioOficioSel.getNumero();
		//
//		MpSistemaConfig mpSistemaConfigX = this.mpSistemaConfigs.porParametro(ofCoNumX + "_BoletoNumeroGuia");
//		if (null == mpSistemaConfigX) {
//			this.mensagemLog = this.mensagemLog + "( " + "Error! Controle GUIA... não existe! Contactar o SUPORTE. = " 
//																	+ ofCoNumX + "_BoletoNumeroGuia" + " ) ";
//			//
//		} else {
//			if (mpSistemaConfigX.getValorN() == 0) {
//				//
//				String[] wordsNumeroGuia = this.mpBoletoSelX.getBoletoInstrucao8().split(" ");
//				 
////				MpAppUtil.PrintarLn(this.mpBoletoSelX.getBoletoInstrucao8() + " / " + wordsNumeroGuia.length);
////				numeroGuia = Integer.parseInt(this.mpBoletoSelX.getBoletoInstrucao8().substring(8, 16).trim());
//				
//				try {
//					this.numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);
////					this.numeroGuiaGeradoAnt = this.numeroGuiaGerado;
//
//					// Atualiza numeroGuia BD !!!
//					mpSistemaConfigX.setValorN(this.numeroGuiaGerado);
//					
//					this.mpSistemaConfigService.salvar(mpSistemaConfigX);
//					//
////					MpAppUtil.PrintarLn("Controle GUIA(0)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
////							this.numeroGuiaGerado);
//				} catch (Exception e) {
//					//
//					this.mensagemLog = this.mensagemLog + "( " + "Error! Controle GUIA/Instrução8 ! Contactar SUPORTE = " + 
//														this.mpBoletoSelX.getBoletoInstrucao8() + " / e = " + e + " )";
//				}
//			} else {
//				this.numeroGuiaGerado = mpSistemaConfigX.getValorN();
////				this.numeroGuiaGeradoAnt = this.numeroGuiaGerado;
//				
//				this.numeroGuiaGerado++;
//		        // --------------------------------- //
//		        // Verificar numero Guia Duplicado?  //
//		        // --------------------------------- //
//				String numOfCoX = "";
////				if (null == this.mpCartorioOficioSel)
////					numOfCoX = this.mpCartorioComarcaSel.getNumero();
////				else
//					numOfCoX = this.mpCartorioOficioSel.getNumero();
//					
//		        List<MpBoleto> mpBoletoGuias = this.mpBoletos.mpBoletoByNumeroGuiaGeradoList(numOfCoX,
//		        																			this.numeroGuiaGerado);
//		        if (mpBoletoGuias.size() > 0) {
//		        	//
//					this.numeroGuiaGerado++;
//		        }
//
//				// Atualiza numeroGuia BD !!!
//				mpSistemaConfigX.setValorN(this.numeroGuiaGerado);
//				
//				this.mpSistemaConfigService.salvar(mpSistemaConfigX);
//				//
////				MpAppUtil.PrintarLn("Controle GUIA(1)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
////						this.numeroGuiaGerado);
//			}
//		}
		
		// ================================================== //
		// Trata registro Boleto (WebServices) ... BRADESCO ! // 
		// ================================================== //
		//
		MpBoletoRegistro mpBoletoRegistro = new MpBoletoRegistro(
		"123456789","0001","39","2","0","0","0","09","123400000001234567","237","0","1","0","0",
		"123456","25.05.2017","20.06.2017","0","100","04","0","0","","","0","0","0","0","0","0",
		"0","0","0","","0","0","","0","0","","0","0","0","","0","0","Cliente?","Rua?","90",
		"","12345","500","Bairro?","Teste","??","1","12345648901234","","","","0","","0","0",
		"","","","0","0","");
		//
//		if (null == mpCartorioOficioSel) {
//			//
//			mpBoletoRegistro.setNuCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(0, 10).replace(".", ""));
//			mpBoletoRegistro.setFilialCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(11, 15));
//			mpBoletoRegistro.setCtrlCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(16, 18));
//		} else {
			//
			mpBoletoRegistro.setNuCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(0, 10).replace(".", ""));
			mpBoletoRegistro.setFilialCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(11, 15));
			mpBoletoRegistro.setCtrlCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(16, 18));
//		}
		//
		mpBoletoRegistro.setCdTipoAcesso("2");
		mpBoletoRegistro.setClubBanco("2269651");
		mpBoletoRegistro.setCdTipoContrato("48");
		mpBoletoRegistro.setNuSequenciaContrato("0");
		mpBoletoRegistro.setIdProduto(this.mpBoletoSelX.getCarteira()); // "09"
		
		// Número da Negociação Formato: Agencia: 4 posições (Sem digito) -> 0123456789012345
		// Zeros: 7 posições Conta: 7 posições (Sem digito)               -> 3119-4/0002820-7
//		String agencia = String.format("%04d", this.mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4)); 
		if (this.indApos16h) {
			mpBoletoRegistro.setNuNegociacao(this.mpBoletoSelX.getAgenciaCodigoCedente1().substring(0, 4) + 
											String.format("%07d", 0) + 
											this.mpBoletoSelX.getAgenciaCodigoCedente1().substring(7, 14)); 
		} else {
			mpBoletoRegistro.setNuNegociacao(this.mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4) + 
											String.format("%07d", 0) + 
											this.mpBoletoSelX.getAgenciaCodigoCedente().substring(7, 14)); 
		}
		//
		mpBoletoRegistro.setCdBanco("237");		
		mpBoletoRegistro.seteNuSequenciaContrato("0");
		mpBoletoRegistro.setTpRegistro("1");
		mpBoletoRegistro.setCdProduto("0");
		
//		// MVPR(20180724)-AjusteErroCipProdução ! 
//		String nossoNumero = this.mpBoletoSelX.getNossoNumero().replaceAll("/", "");
//		nossoNumero = nossoNumero.replaceAll("-", "");
//		if (nossoNumero.isEmpty()) nossoNumero = ""; // ???????
//		if (nossoNumero.length() > 10)
//			nossoNumero = nossoNumero.substring(0, 11);
//		mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !

		//				                    01234567890123456
		// MVPR-20180807 09/02018041124-4 = 09020180411244 
		String nossoNumero = this.mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		nossoNumero = nossoNumero.replaceAll("-", "");
		if (nossoNumero.isEmpty()) nossoNumero = ""; // ???????
		if (nossoNumero.length() > 13)
			nossoNumero = nossoNumero.substring(2, 13);
		mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !
		// MVPR-20180807

		//
		mpBoletoRegistro.setNuCliente(this.mpBoletoSelX.getNumeroDocumento().replace("/", ""));
		mpBoletoRegistro.setDtEmissaoTitulo(this.sdfDMYp.format(this.mpBoletoSelX.getDataDocumento()));

		if (this.indApos16h)
			mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(this.mpBoletoSelX.getDataVencimento1()));
		else
			mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(this.mpBoletoSelX.getDataVencimento()));
		//
		mpBoletoRegistro.setTpVencimento("0");
		
		//MVPR(20180502)-Ajuste Linha Digitavel !
//		String valorX = this.mpBoletoSelX.getValorDocumento().toString().replace(".","");
		String valorX = this.mpBoletoSelX.getValorCobrado().toString().replace(".","");
		mpBoletoRegistro.setVlNominalTitulo(valorX);
		//
		try {
			MpEspecieTituloBradesco mpEspecieTituloBradesco = MpEspecieTituloBradesco.valueOf(
																		this.mpBoletoSelX.getEspecieDocumento().trim());
			if (null==mpEspecieTituloBradesco) {
				//
				this.mensagemLog = this.mensagemLog + 
									"( Registro/Bradesco. Não existe Espécie Título (Null/Assumiu=99) / sigla = " +
									this.mpBoletoSelX.getEspecieDocumento() + " ) ";
				//
				mpBoletoRegistro.setCdEspecieTitulo("99");
			} else
				mpBoletoRegistro.setCdEspecieTitulo(mpEspecieTituloBradesco.getSigla());
		}
		catch (Exception e) {
			this.mensagemLog = this.mensagemLog + 
								"( Registro/Bradesco. Não existe Espécie Título (Exception/Assumiu=99) / sigla = " +
								this.mpBoletoSelX.getEspecieDocumento() + " ) ";
			//
			mpBoletoRegistro.setCdEspecieTitulo("99");
		}
		//
		mpBoletoRegistro.setTpProtestoAutomaticoNegativacao("0");
		mpBoletoRegistro.setPrazoProtestoAutomaticoNegativacao("0");
		mpBoletoRegistro.setControleParticipante("");
		mpBoletoRegistro.setCdPagamentoParcial("");		
		mpBoletoRegistro.setQtdePagamentoParcial("0");
		mpBoletoRegistro.setPercentualJuros("0");
		
		mpBoletoRegistro.setVlJuros("0");
		mpBoletoRegistro.setQtdeDiasJuros("0");
		mpBoletoRegistro.setPercentualMulta("0");
		mpBoletoRegistro.setVlMulta("0");
		mpBoletoRegistro.setQtdeDiasMulta("0");
		mpBoletoRegistro.setPercentualDesconto1("0");
		mpBoletoRegistro.setVlDesconto1("0");		
		mpBoletoRegistro.setDataLimiteDesconto1("");
		mpBoletoRegistro.setPercentualDesconto2("0");
		mpBoletoRegistro.setVlDesconto2("0");
		mpBoletoRegistro.setDataLimiteDesconto2("");
		mpBoletoRegistro.setPercentualDesconto3("0");
		mpBoletoRegistro.setVlDesconto3("0");
		mpBoletoRegistro.setDataLimiteDesconto3("");

		mpBoletoRegistro.setPrazoBonificacao("0");
		mpBoletoRegistro.setPercentualBonificacao("0");
		mpBoletoRegistro.setVlBonificacao("0");
		mpBoletoRegistro.setDtLimiteBonificacao("");

		mpBoletoRegistro.setVlAbatimento("0");
		mpBoletoRegistro.setVlIOF("0");

		// MVPR-30072018 !
		if (this.mpBoletoSelX.getNomeSacado().length() > 69)
			mpBoletoRegistro.setNomePagador(this.mpBoletoSelX.getNomeSacado().substring(0, 69));
		else
			mpBoletoRegistro.setNomePagador(this.mpBoletoSelX.getNomeSacado());
		//
		if (this.mpBoletoSelX.getMpEnderecoLocal().getLogradouro().length() > 39)
			mpBoletoRegistro.setLogradouroPagador(this.mpBoletoSelX.getMpEnderecoLocal().getLogradouro().substring(0, 39));
		else
			mpBoletoRegistro.setLogradouroPagador(this.mpBoletoSelX.getMpEnderecoLocal().getLogradouro());
		//
		mpBoletoRegistro.setNuLogradouroPagador(this.mpBoletoSelX.getMpEnderecoLocal().getNumero());

		if (null==this.mpBoletoSelX.getMpEnderecoLocal().getComplemento())
			mpBoletoRegistro.setComplementoLogradouroPagador("");
		else
			mpBoletoRegistro.setComplementoLogradouroPagador(this.mpBoletoSelX.getMpEnderecoLocal().
																					getComplemento());
		
		mpBoletoRegistro.setCepPagador(this.mpBoletoSelX.getMpEnderecoLocal().getCep().substring(0, 5));
		mpBoletoRegistro.setComplementoCepPagador("0");
		mpBoletoRegistro.setBairroPagador(this.mpBoletoSelX.getMpEnderecoLocal().getBairro());

		if (null==this.mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF()) {
			mpBoletoRegistro.setMunicipioPagador("RioJaneiro?");
			mpBoletoRegistro.setUfPagador("RJ");
		} else {
			mpBoletoRegistro.setMunicipioPagador(this.mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().
																						getDescricao());
			mpBoletoRegistro.setUfPagador(this.mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().name());
		}
		//
//		System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0001 = " + this.mpBoletoSelX.getCpfCnpj());
		
		mpBoletoRegistro.setCdIndCpfcnpjPagador("2"); // CNPJ
		mpBoletoRegistro.setNuCpfcnpjPagador(this.mpBoletoSelX.getCpfCnpj());		
		if (this.mpBoletoSelX.getCpfCnpj().trim().length() < 12) {
			mpBoletoRegistro.setCdIndCpfcnpjPagador("1"); // CPF
			if (this.mpBoletoSelX.getCpfCnpj().trim().length() == 11)
				mpBoletoRegistro.setNuCpfcnpjPagador("000" + this.mpBoletoSelX.getCpfCnpj().trim());
			else
			if (this.mpBoletoSelX.getCpfCnpj().trim().length() == 10)
				mpBoletoRegistro.setNuCpfcnpjPagador("0000" + this.mpBoletoSelX.getCpfCnpj().trim());
			else
			if (this.mpBoletoSelX.getCpfCnpj().trim().length() == 9)
				mpBoletoRegistro.setNuCpfcnpjPagador("00000" + this.mpBoletoSelX.getCpfCnpj().trim());
		}
		//
		mpBoletoRegistro.setEndEletronicoPagador("");		
		mpBoletoRegistro.setNomeSacadorAvalista("");
		mpBoletoRegistro.setLogradouroSacadorAvalista("");
		mpBoletoRegistro.setNuLogradouroSacadorAvalista("");
		mpBoletoRegistro.setComplementoLogradouroSacadorAvalista("");
		mpBoletoRegistro.setCepSacadorAvalista("0");
		mpBoletoRegistro.setComplementoCepSacadorAvalista("0");
		mpBoletoRegistro.setBairroSacadorAvalista("");
		mpBoletoRegistro.setMunicipioSacadorAvalista("");
		mpBoletoRegistro.setUfSacadorAvalista("");
		mpBoletoRegistro.setCdIndCpfcnpjSacadorAvalista("0");
		mpBoletoRegistro.setNuCpfcnpjSacadorAvalista("0");
		mpBoletoRegistro.setEndEletronicoSacadorAvalista("");
		
		// --------------------------------------------------

		try {
			//
//			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002");

			// Convert Objeto em JSON ! 
			// ========================
			ObjectMapper objectMapper = new ObjectMapper();
			
			//configure Object mapper for pretty print
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			
			//writing to console, can write to any output stream such as file
			StringWriter stringBoleto = new StringWriter();
			objectMapper.writeValue(stringBoleto, mpBoletoRegistro);
			//
			this.boletoRegistro = stringBoleto.toString();
			this.boletoRegistro = this.boletoRegistro.replaceAll(", ", ",");
			this.boletoRegistro = this.boletoRegistro.replaceAll(" : ", ":");

//			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003 ( " + this.boletoRegistro);
						
			// PKCS7Signer signer = new PKCS7Signer();
			KeyStore keyStore = this.loadKeyStore();
			CMSSignedDataGenerator signatureGenerator = this.setUpProvider(keyStore);
			
			// Json de teste...
//			String json = "{\"nuCPFCNPJ\": \"123456789\",\"filialCPFCNPJ\": \"0001\",\"ctrlCPFCNPJ\": \"39\",\"cdTipoAcesso\": \"2\",\"clubBanco\": \"0\",\"cdTipoContrato\": \"0\",\"nuSequenciaContrato\": \"0\",\"idProduto\": \"09\",\"nuNegociacao\": \"123400000001234567\",\"cdBanco\": \"237\",\"eNuSequenciaContrato\": \"0\",\"tpRegistro\": \"1\",\"cdProduto\": \"0\",\"nuTitulo\": \"0\",\"nuCliente\": \"123456\",\"dtEmissaoTitulo\": \"25.05.2017\",\"dtVencimentoTitulo\": \"20.06.2017\",\"tpVencimento\": \"0\",\"vlNominalTitulo\": \"100\",\"cdEspecieTitulo\": \"04\",\"tpProtestoAutomaticoNegativacao\": \"0\",\"prazoProtestoAutomaticoNegativacao\": \"0\",\"controleParticipante\": \"\",\"cdPagamentoParcial\": \"\",\"qtdePagamentoParcial\": \"0\",\"percentualJuros\": \"0\",\"vlJuros\": \"0\",\"qtdeDiasJuros\": \"0\",\"percentualMulta\": \"0\",\"vlMulta\": \"0\",\"qtdeDiasMulta\": \"0\",\"percentualDesconto1\": \"0\",\"vlDesconto1\": \"0\",\"dataLimiteDesconto1\": \"\",\"percentualDesconto2\": \"0\",\"vlDesconto2\": \"0\",\"dataLimiteDesconto2\": \"\",\"percentualDesconto3\": \"0\",\"vlDesconto3\": \"0\",\"dataLimiteDesconto3\": \"\",\"prazoBonificacao\": \"0\",\"percentualBonificacao\": \"0\",\"vlBonificacao\": \"0\",\"dtLimiteBonificacao\": \"\",\"vlAbatimento\": \"0\",\"vlIOF\": \"0\",\"nomePagador\": \"Cliente Teste\",\"logradouroPagador\": \"rua Teste\",\"nuLogradouroPagador\": \"90\",\"complementoLogradouroPagador\": \"\",\"cepPagador\": \"12345\",\"complementoCepPagador\": \"500\",\"bairroPagador\": \"bairro Teste\",\"municipioPagador\": \"Teste\",\"ufPagador\": \"SP\",\"cdIndCpfcnpjPagador\": \"1\",\"nuCpfcnpjPagador\": \"12345648901234\",\"endEletronicoPagador\": \"\",\"nomeSacadorAvalista\": \"\",\"logradouroSacadorAvalista\": \"\",\"nuLogradouroSacadorAvalista\": \"0\",\"complementoLogradouroSacadorAvalista\": \"\",\"cepSacadorAvalista\": \"0\",\"complementoCepSacadorAvalista\": \"0\",\"bairroSacadorAvalista\": \"\",\"municipioSacadorAvalista\": \"\",\"ufSacadorAvalista\": \"\",\"cdIndCpfcnpjSacadorAvalista\": \"0\",\"nuCpfcnpjSacadorAvalista\": \"0\",\"endEletronicoSacadorAvalista\": \"\"}";

//			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0004 ( json = " + this.boletoRegistro);

			byte[] signedBytes = this.signPkcs7(this.boletoRegistro.getBytes("UTF-8"), signatureGenerator);
//			System.out.println("Signed Encoded Bytes: " + new String(Base64.encode(signedBytes)));
	
			HttpEntity entity = new StringEntity(new String(Base64.encode(signedBytes)), Charset.forName("UTF-8"));
//			System.out.println("XXX004 = " + entity);
			
			// Trata Ambiente de Produção x Homologação ...
//	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Bradeco_Registro_URI");
			
	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofCoNumX + "_Bradesco_Registro_URI");
	    	if (null == mpSistemaConfig) {
	    		this.msgErro = " ( Contactar o Suporte... Não foi encontrado parâmetro do sistema = '" + 
	    																	ofCoNumX +"_Bradesco_Registro_URI'";
	    		this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
	    		return;    		
	    	}
			//
    		HttpPost post = new HttpPost(URI_REGISTRO_HOMOLOGACAO);
	    	if (mpSistemaConfig.getValorT().trim().toUpperCase().equals("PRODUCAO"))
	    		post = new HttpPost(URI_REGISTRO_PRODUCAO);
	    	else
		    	if (mpSistemaConfig.getValorT().trim().toUpperCase().equals("HOMOLOGACAO"))
		    		assert(true); // nop
		    	else {
		    		this.msgErro = " ( Contactar o Suporte...  BRAD.0001 - Inválido parâmetro do sistema = ( " + 
		    									mpSistemaConfig.getValorT().trim() + " ) ...assumido PRODUCAO !";
		    		this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
		    		//
		    		post = new HttpPost(URI_REGISTRO_PRODUCAO);
//		    		return;    		
		    	}
//			System.out.println("XXX005 = " + post);
			
			post.setEntity(entity);
//			System.out.println("XXX006 = POST");
	
			HttpClientBuilder builder = HttpClientBuilder.create();
//			System.out.println("XXX007 = " + builder);
			
			// ??? Erro PKIX path building failed -> keytool -insert ... 2 certificados vindo do BRADESCO ...
			// Baixei e usei o utilitário ... KeyStore Explorer 5.3.2 !
			HttpResponse response = builder.build().execute(post); 
//			System.out.println("XXX008 = " + response.getStatusLine());
			
			String boletoRetorno = EntityUtils.toString(response.getEntity(), "UTF-8");		
//			System.out.println("XXX009 = " + boletoRetorno);
			
			Integer posI = boletoRetorno.indexOf("{");
			Integer posF = boletoRetorno.indexOf("}");
//			System.out.println("XXX009-1 = " + posI + " / " + posF + " / " + boletoRetorno.length());
			
			this.boletoRegistroRetorno = boletoRetorno.substring(posI, posF + 1);
//			System.out.println("XXX0010 = " + this.boletoRegistroRetorno);
					
			// Convert JSON Retorno para Objeto ! 
			// ========================
			ObjectMapper objectMapperRet = new ObjectMapper();
			
			//JSON from String to Object
			MpBoletoRegistroRetorno mpBoletoRegistroRetorno = objectMapperRet.readValue(
																		this.boletoRegistroRetorno,
																		MpBoletoRegistroRetorno.class);
			if (mpBoletoRegistroRetorno.getCdErro().equals("0")  ||
				mpBoletoRegistroRetorno.getCdErro().equals("00"))
				this.mpBoletoSelX.setDataHoraRegistro(new Date());
			else
				if (mpBoletoRegistroRetorno.getCdErro().equals("69")) { // Titulo Cadastrado ! Avisar Erro !
	    			this.msgErro = " ( Cod.Erro/Bradesco) = " + mpBoletoRegistroRetorno.getCdErro() + 
								"/ msg.erro = " + mpBoletoRegistroRetorno.getMsgErro() + " / Contactar o Suporte ";
	    			this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
	    			
	    			// =================================================================== //
	    			// Trata Bloqueio do Titulo para Erro=69 ! Marcus/Prisco MR-20200528 ! //
	    			// =================================================================== //
	    			this.mpBoletoSelX.setIndBradescoErro69(true);
	    			// =================================================================== //

	    			//
				} else {
					this.msgErro = " ( Cod.Erro/Bradesco = " + mpBoletoRegistroRetorno.getCdErro() + 
    										"/ msg.erro = " + mpBoletoRegistroRetorno.getMsgErro();
					this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
				}
			//
			this.codigoErroRegistro = mpBoletoRegistroRetorno.getCdErro();
			//
//			System.out.println("XXX011 / Cod.Erro = " + mpBoletoRegistroRetorno.getCdErro() + 
//								" / Erro: " + this.msgErro);
			//
		} catch (Exception e) {
			//
    		this.msgErro = " ( Exceção = " + e;
    		this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
		}

//		// Se o Boleto já foi registrado (CodErro=69)... Não incrementa o Número da GUIA ! ??? MVPR-10042019 !
//		if (this.codigoErroRegistro.equals("69")) {
//			//
//			mpSistemaConfigX = this.mpSistemaConfigs.porParametro(ofCoNumX + "_BoletoNumeroGuia");
//			if (null == mpSistemaConfigX) {
//				this.mensagemLog = this.mensagemLog + "( " + 
//									"Error ! Controle GUIA... não existe ! Contactar o SUPORTE. = " + 
//									ofCoNumX + "_BoletoNumeroGuia" + " ) ";
//			} else {
//				if (mpSistemaConfigX.getValorN() == 0)
//					assert(true);
//				else {
//					// Atualiza numeroGuia BD !!!
//					mpSistemaConfigX.setValorN(this.numeroGuiaGeradoAnt);
//			
//					this.mpSistemaConfigService.salvar(mpSistemaConfigX);
//				}
//			}
//		}
		//
	}
	
	private KeyStore loadKeyStore() throws Exception {
		//
		KeyStore keystore = KeyStore.getInstance("JKS");

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		String numOf = "1";
		String pathOf = PATH_TO_KEYSTORE_1OF;
		if (numOf.equals("2")) pathOf = PATH_TO_KEYSTORE_2OF;
		if (numOf.equals("3")) pathOf = PATH_TO_KEYSTORE_3OF;
		if (numOf.equals("4")) pathOf = PATH_TO_KEYSTORE_4OF;
		
		InputStream is = new FileInputStream(extContext.getRealPath(File.separator + "resources" + 
										File.separator + "opt" + File.separator + pathOf));				
//		System.out.println("XXX001 = " + is);

//		InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
//		System.out.println("XXX002 = " + keystore);
		//
		return keystore;
	}

	private CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception {
		//
//		System.out.println("YYY001 = " + keystore);

		Security.addProvider(new BouncyCastleProvider());
		Enumeration<String> aliases = keystore.aliases();
		String aliaz = "";
		//
		while (aliases.hasMoreElements()) {
			aliaz = aliases.nextElement();
			if (keystore.isKeyEntry(aliaz)) {
				break;
			}
		}
		//
		Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(aliaz);

		final List<Certificate> certlist = new ArrayList<Certificate>();
		//
		for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
			certlist.add(certchain[i]);
		}

		@SuppressWarnings("rawtypes")
		Store certstore = new JcaCertStore(certlist);

		Certificate cert = (Certificate) keystore.getCertificate(aliaz);

		ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC")
				.build((PrivateKey) (keystore.getKey(aliaz, KEYSTORE_PASSWORD.toCharArray())));

		CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

		generator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
						.setProvider("BC").build()).build(signer, (X509Certificate) cert));

		generator.addCertificates(certstore);
		//
//		System.out.println("YYY002 = " + generator);
		//
		return generator;
	}

	private byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {
		//
//		System.out.println("YYY003 = " + content);

		CMSTypedData cmsdata = new CMSProcessableByteArray(content);
		CMSSignedData signeddata = generator.generate(cmsdata, true);
		//
		return signeddata.getEncoded();
	}
	
    public void listaBoletoEmitido() {
		//
//		System.out.println("MpBoletoBean.listaBoletoList() - ( Entrou 000 ");

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
	    //
//	    if (mpSeguranca.isIndComarca()) {
//	    	//
//		    if (null == this.mpCartorioOficioSel || null == this.mpCartorioComarcaSel) 
//		    	assert(true); // nop
//		    else
//		    	if (null == this.mpCartorioOficioSel) {
//		    		//
//			    	if (!this.mpCartorioComarcaSel.getNome().isEmpty()) {
//			    		//
//			    		MpFacesUtil.addErrorMessage("Selecionar Ofício ou Comarca !");  
//			    		return;
//			    	}
//		    	} else {
//		    		//
//			    	if (!this.mpCartorioOficioSel.getNome().isEmpty()) {
//			    		//
//			    		MpFacesUtil.addErrorMessage("Selecionar Ofício ou Comarca !");  
//			    		return;
//			    	}
//		    	}
//	    }
		
	    // -----------	    
		// Criticar...
	    // -----------
		if (null == this.cpfCnpj) this.cpfCnpj = "";
		//
		if (this.indUsuario) {
			//
			if (this.cpfCnpj.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Informar CPF/CNPJ !");
				return;
			}
		}
		//
		String numOfCoX = "";
//		if (null == this.mpCartorioOficioSel)
//			numOfCoX = this.mpCartorioComarcaSel.getNumero().trim();
//		else
			numOfCoX = this.mpCartorioOficioSel.getNumero().trim();
		//
		if (!this.cpfCnpj.isEmpty()) {
			//
	    	String cpfCnpjX = MpAppUtil.formataCpfCnpj(this.cpfCnpj); 
			
	    	String retMsgX = MpAppUtil.validaCpfCnpj(this.cpfCnpj); 
			if (!retMsgX.equals("OK")) {
				//
				MpFacesUtil.addErrorMessage(retMsgX);
				return;
			}
			//
			if (numOfCoX.equals("X"))
				this.mpBoletoListP = mpBoletos.mpBoletoByImpressaoCpfCnpjList(cpfCnpjX);
			else
				this.mpBoletoListP = mpBoletos.mpBoletoByImpressaoOficioCpfCnpjList(numOfCoX, cpfCnpjX);
			if (this.mpBoletoListP.size() == 0) {
				//
				MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) Emitido(s) na Base de Dados ! (rc=04)");
				return;
			}
			//		
		} else {
//			System.out.println("MpBoletoBean.listaBoleto() - ( " + this.mpCartorioOficioSel.getNumero() + " / " +
//					sdfDMY.format(this.dataIntimacao) + " / " + this.numeroIntimacao);
			//
			if (numOfCoX.equals("X"))
				this.mpBoletoListP = mpBoletos.mpBoletoByImpressaoList();
			else
				this.mpBoletoListP = mpBoletos.mpBoletoByImpressaoOficioList(numOfCoX);
		}
		//
//		System.out.println("MpBoletoBean.listaBoletoList() - ( size = " + this.mpBoletoList.size());
		//
		if (this.mpBoletoListP.size() == 0) {
			//
			MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) na Base de Dados ! (rc=05) (" +
																				numOfCoX + " / " + this.cpfCnpj);
			return;
		}
    }
    
    @SuppressWarnings("deprecation")
	public void geraBoletoP() {
        //
    	if (this.indGeraBoleto) {
    		//
    		try {
				TimeUnit.SECONDS.sleep(10);
				//
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		//
    		this.indGeraBoleto = false;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = TRUE");    		
    		return;
    	} else {
    		//
    		this.indGeraBoleto = true;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = FALSE");
    	}
    	//
//		MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 ( idBolP = " + 
//														" / numCode = " + this.numeroIntimacaoCode); 
		//
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.3P - Boleto Selecionado ! Contactar Suporte Técnico (null.Bol");  
			return;
		}
    	//
    	this.mpBoletoSelX = mpBoletos.porId(this.mpBoletoSelX.getId());
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.4P - Boleto Selecionado ! Contactar Suporte Técnico (idNull.Bol");  
			return;
		}
    	//    	
    	if (null == this.mpBoletoSelX.getNomePdfGerado() || this.mpBoletoSelX.getNomePdfGerado().isEmpty()) {
			MpFacesUtil.addErrorMessage("Error.5P - Boleto Selecionado Não Existe PDF ! Contactar Suporte Técnico"); 

			// Correção/Erro Informado Heraldo - MR-20191210 !
        	this.nomePdfX = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + this.sdfYYYYMMDD.format(new Date()) 
														+ "_" + this.mpBoletoSelX.getNossoNumero() + ".pdf";
//			return;
    	}
		//
    	if (this.indUsuario) {
    		//
    		if (null == this.numeroIntimacaoCode || this.numeroIntimacaoCode.isEmpty()) {
    			//
    			MpFacesUtil.addErrorMessage("Informar o código Segurança ! ( Num. Documento = " + 
															this.mpBoletoSelX.getNumeroDocumento());
    			return;
    		}
    		//
    		String numeroIntimacaoDigX = this.mpBoletoSelX.getNumeroIntimacaoCode().trim();

//			MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 (dig3 = " + numeroIntimacaoDigX + " / code = " + 
//						this.numeroIntimacaoCode.trim() + " / numInt = " + this.mpBoletoSelX.getNumeroIntimacao() + 
//						" / IntCode = " + this.mpBoletoSelX.getNumeroIntimacaoCode() + " )");
    		//
    		if (numeroIntimacaoDigX.equals(this.numeroIntimacaoCode.trim()))
    			assert(true); // nop
    		else {
    			MpFacesUtil.addErrorMessage("Código Segurança inválido ! Favor verificar. ( Num. Documento = " + 
						this.mpBoletoSelX.getNumeroDocumento() + " / Código Informado = " + this.numeroIntimacaoCode);
    			return;
    		}
    	}
    	
    	//
		if (this.indEmailBoleto) {
			//
			if (this.emailBoleto.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Envio de Boleto por E-mail Ativo! Favor informar e-mail !!!");
				//
			} else {
				//
	    		String[] emailList = this.emailBoleto.trim().split(",");

				for (String emailX : emailList) {
					//
//					System.out.println("MpBoletoBean.gerarBoleto() - Email  ( " + emailX.trim());
					
					if (!MpAppUtil.isEmail(emailX.trim())) {
						//
						MpFacesUtil.addErrorMessage("E-mail Envio Boleto... formato inválido ! Contactar Suporte ( " +
																					emailX.trim());			
						return;			
					}
				}
			}
			//
			this.trataEnvioBoletoEmail();

   			MpFacesUtil.addInfoMessage("Boleto(2a.Via) ... Enviado ! ( " + this.emailBoleto);
			//
			return;
		}
    	
		//
    	String ofCoNumX = "";
//    	if (null == this.mpCartorioOficioSel)
//    		ofCoNumX = "Co" + this.mpCartorioComarcaSel.getNumero();
//    	else
    		ofCoNumX = "Of" + this.mpCartorioOficioSel.getNumero();
     	
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofCoNumX + "_Boleto_Printer");
    	if (null == mpSistemaConfig)
    		this.indBoletoPrinter = false;
    	else
    		this.indBoletoPrinter = mpSistemaConfig.getIndValor();
    	//
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		if (this.indBoletoPrinter) {
			//
		    URI uri;
			try {
				uri = new URI(extContext.getRequestScheme(),
				        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
				        extContext.getRequestContextPath(), null, null);
			    
			    String contextPathX = uri.toASCIIString();

//			    if (null == this.mpCartorioOficioSel)
//			    	RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
//										"/resources/pdfs/co" + this.mpCartorioComarcaSel.getNumero() + 
//										"/" + this.mpBoletoSelX.getNomePdfGerado() + "');");
//			    else
			    	RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
										"/resources/pdfs/of" + this.mpCartorioOficioSel.getNumero() + 
										"/" + this.mpBoletoSelX.getNomePdfGerado() + "');");
        		//
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			//
	        try {
	        	//
	        	String pathX = "";
//	        	if (null == this.mpCartorioOficioSel)
//	        		pathX = extContext.getRealPath("//resources//pdfs//co" + 
//																	this.mpCartorioComarcaSel.getNumero() + "//");
//	        	else	        		
	        		pathX = extContext.getRealPath("//resources//pdfs//of" + 
        															this.mpCartorioOficioSel.getNumero() + "//");
        		//	
        		File fileXX = new File(pathX + this.mpBoletoSelX.getNomePdfGerado());
            	if (!fileXX.exists()) {
        			MpFacesUtil.addErrorMessage("Boleto Selecionado... não existe arquivo/PDF para geração 2a.Via !" +
        																				" Contactar Suporte Técnico");  
        			return;
            	}
	        	//
        	    FacesContext facesContextX = FacesContext.getCurrentInstance();
        	    HttpServletResponse responseX = (HttpServletResponse) facesContextX.getExternalContext().getResponse();
        	    //	
        	    responseX.reset();
	        	responseX.setContentType("application/pdf");
	        	responseX.setContentLength((int) fileXX.length());
	        	responseX.setHeader("Content-Disposition", "attachment; filename=\"" + 
	        																this.mpBoletoSelX.getNomePdfGerado() + "\"");
	        	//	
	        	InputStream fisX = new FileInputStream(fileXX);
	        	ServletOutputStream osX = responseX.getOutputStream();
	        	//	
	            byte[] bufferData = new byte[1024];
	            int read=0;
	            //
	            while((read = fisX.read(bufferData))!= -1){
	            	osX.write(bufferData, 0, read);
	            }
	            osX.flush();
	            osX.close();
	            fisX.close();
	            //            
	            responseX.flushBuffer();
	             
	            facesContextX.responseComplete(); // FacesContext.getCurrentInstance().responseComplete();
	            //
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	        //
			return;
		}
    }

	private String capturaPathResourcePdf() {
		;
		//
		String pathFileX = File.separator + "resources" + File.separator + "pdfs" + File.separator;

//		if (null == this.mpCartorioOficioSel)
//			pathFileX = pathFileX + "co" + this.mpCartorioComarcaSel.getNumero();
//		else
			pathFileX = pathFileX + "of" + this.mpCartorioOficioSel.getNumero();
		//
		return pathFileX;
	}

	// ---

	public void addMessageEmail() {
		//
		String summary = this.indEmailBoleto ? "Envio E-mail Ativado!" : "Envio E-mail Desativado!";

		if (this.indEmailBoleto) {
			//
			if (this.emailBoleto.isEmpty()) {
				//
				if (mpSeguranca.isCartorio()) // || mpSeguranca.isComarca())
					assert (true); // nop
				else
					this.emailBoleto = this.mpSeguranca.getEmailUsuario();
			}
		} else
			this.emailBoleto = "";

		MpFacesUtil.addErrorMessage(summary);
	}

	public void addNumeroParcela() {
		//
		System.out.println("MpBoletoBean().addNumeroParcela() - Entrou 000 !" + this.isWizardIndCartaoCredito());
	}
	
	// ---
	
    public StreamedContent getDownloadValue() throws Exception {
    	//
    	if (this.indGeraBoleto) {
    		//
    		try {
				TimeUnit.SECONDS.sleep(10);
				//
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		//
    		this.indGeraBoleto = false;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = TRUE");    		
    		return null;
    	} else {
    		//
    		this.indGeraBoleto = true;
//    		
//    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = FALSE");
    	}
    	//
//		MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 ( idBolP = " + 
//														" / numCode = " + this.numeroIntimacaoCode); 
		//
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.3P - Boleto Selecionado ! Contactar Suporte Técnico (null.Bol");  
			return null;
		}
    	//
    	this.mpBoletoSelX = mpBoletos.porId(this.mpBoletoSelX.getId());
    	if (null == this.mpBoletoSelX) {
			//
			MpFacesUtil.addErrorMessage("Error.4P - Boleto Selecionado ! Contactar Suporte Técnico (idNull.Bol");  
			return null;
		}
    	//    	
    	if (null == this.mpBoletoSelX.getNomePdfGerado() || this.mpBoletoSelX.getNomePdfGerado().isEmpty()) {
    		//
			// Correção/Erro Informado Heraldo - MR-20191210 !
//        	this.nomePdf = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" +
//        													this.mpBoletoSelX.getNossoNumero().trim() + "_0" + ".pdf";
    		this.nomePdf =  "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + this.sdfYYYYMMDD.format(new Date()) 
																+ "_" +  this.mpBoletoSelX.getNossoNumero() + ".pdf";
    		
        	MpFacesUtil.addErrorMessage("Error.5P - Boleto Selecionado Não Existe PDF ! Contactar Suporte Técnico (" +
        																								this.nomePdf); 
			return null;
    	}
    	// =========================================================================
    	// Para esse serviço não é necessário informar o código de segurança ! 
    	// =========================================================================
    	// this.numeroIntimacaoCode = this.mpBoletoSelX.getNumeroIntimacaoCode().trim();
		//
    	if (this.indUsuario) {
    		//
    		if (null == this.numeroIntimacaoCode || this.numeroIntimacaoCode.isEmpty()) {
    			//
    			MpFacesUtil.addErrorMessage("Informar o código Segurança ! ( Num. Documento = " + 
																			this.mpBoletoSelX.getNumeroDocumento());
    			return null;
    		}
    		//
    		String numeroIntimacaoDigX = this.mpBoletoSelX.getNumeroIntimacaoCode().trim();

//			MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 (dig3 = " + numeroIntimacaoDigX + " / code = " + 
//						this.numeroIntimacaoCode.trim() + " / numInt = " + this.mpBoletoSelX.getNumeroIntimacao() + 
//						" / IntCode = " + this.mpBoletoSelX.getNumeroIntimacaoCode() + " )");
    		//
    		if (numeroIntimacaoDigX.equals(this.numeroIntimacaoCode.trim()))
    			assert(true); // nop
    		else {
    			MpFacesUtil.addErrorMessage("Código Segurança inválido ! Favor verificar. ( Num. Documento = " + 
						this.mpBoletoSelX.getNumeroDocumento() + " / Código Informado = " + this.numeroIntimacaoCode);
    			return null;
    		}
    	}
    	//
		if (this.indEmailBoleto) {
			//
			if (this.emailBoleto.isEmpty()) {
				//
				MpFacesUtil.addErrorMessage("Envio de Boleto por E-mail Ativo! Favor informar e-mail !!!");
				//
			} else {
				//
	    		String[] emailList = this.emailBoleto.trim().split(",");

				for (String emailX : emailList) {
					//
//					System.out.println("MpBoletoBean.gerarBoleto() - Email  ( " + emailX.trim());
					
					if (!MpAppUtil.isEmail(emailX.trim())) {
						//
						MpFacesUtil.addErrorMessage("E-mail Envio Boleto... formato inválido ! Contactar Suporte ( " +
																										emailX.trim());			
						return null;			
					}
				}
			}
			//
			this.trataEnvioBoletoEmail();

   			MpFacesUtil.addInfoMessage("Boleto(2a.Via) ... Enviado ! ( " + this.emailBoleto);
			//
			return null;
		}
    	
    	//
    	String ofCoNumX = "";
//    	if (null == this.mpCartorioOficioSel)
//    		ofCoNumX = "Co" + this.mpCartorioComarcaSel.getNumero();
//    	else
    		ofCoNumX = "Of" + this.mpCartorioOficioSel.getNumero();
     	
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofCoNumX + "_Boleto_Printer");
    	if (null == mpSistemaConfig)
    		this.indBoletoPrinter = false;
    	else
    		this.indBoletoPrinter = mpSistemaConfig.getIndValor();
    	//
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		if (this.indBoletoPrinter) {
			//
		    URI uri;
			try {
				uri = new URI(extContext.getRequestScheme(),
				        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
				        extContext.getRequestContextPath(), null, null);
			    
			    String contextPathX = uri.toASCIIString();

				String pathFileX = contextPathX + File.separator + this.capturaPathResourcePdf();

//				System.out.println("MpBoletoBean.geraBoletoP() - 2a.Via printJS ( " + pathFileX);	    		
				
				RequestContext.getCurrentInstance().execute("printJS('" + pathFileX + File.separator + mpBoletoSelX.getNomePdfGerado() + "');");
        		//
	        	MpFacesUtil.addErrorMessage("Boleto gerado para printer !");
				//
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			//
	        StreamedContent download = new DefaultStreamedContent();
	
	        File fileXX = new File(System.getProperty("user.home") + File.separator + this.mpBoletoSelX.getNomePdfGerado());
	
	        InputStream input = new FileInputStream(fileXX);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        
	        download = new DefaultStreamedContent(input, externalContext.getMimeType(fileXX.getName()), fileXX.getName());
	        
	        System.out.println("PREP................................. = " + download.getName());
        	MpFacesUtil.addErrorMessage("Boleto 2a.Via... gerado !");
	        
	        return download;
		}
		//
    	return null;
    }    

    // -- Wizard Controle de Passos ...
    @SuppressWarnings("unused")
	public String onFlowProcess(FlowEvent event) {
 	   //
 		final String oldStep = event.getOldStep();
 	    final String newStep = event.getNewStep();
 	    //
// 	    System.out.println(" MpWizardRodizioSaberBean.onFlowProcess ( Setp Old/New = " + 
// 	    																	oldStep + " / " + newStep);
 	    //
 	    if (newStep.equals("tabDividaTaxaId")) {
 	    	//
 	    	String msgX = "";
 	    	
 	    	if (!this.indTermoCartaoCielo)
 	    		msgX += "( Favor confirmar nossos termos ! )";
 	    	//
 	    	if (!msgX.isEmpty()) {
 	    		//
 	            MpFacesUtil.addInfoMessage(" Alerta!... " + msgX);
 	            
// 	    	    System.out.println(" MpWizardRodizioSaberBean.onFlowProcess ( MsgX = " + msgX); 
 	            //
 				return event.getOldStep();
 	    	}
 	    }

 	    //
 	    if (newStep.equals("tabDadosCartaoId")) {
 	    	//
 	    	String msgX = "";
 	    	
 	    	if (null == this.cardNumeroParcelaDocSel)
 	    		msgX += "( Débito/Parcelas Dívida )";
 	    	if (null == this.cardNumeroParcelaDocSel)
 	    		msgX += "( Débito/Parcelas Taxa )";
 	    	//
 	    	if (!msgX.isEmpty()) {
 	    		//
 	            MpFacesUtil.addInfoMessage(" Alerta!... " + msgX);
 	            
// 	    	    System.out.println(" MpWizardRodizioSaberBean.onFlowProcess ( MsgX = " + msgX); 
 	            //
 				return event.getOldStep();
 	    	}
 	    }

 	    //
 	    if (newStep.equals("tabConfirmacaoId")) {
 	    	//
 	    	String msgX = "";
 	    	
 	    	if (null == this.cardBrandSel)
 	    		msgX += "( Bandeira )";
 	    	else
 	 	    	if (!this.cardBrandSel.getIndDebito()) 
 	 	    		if (this.cardNumeroParcelaDocSel.getDescricao().contains("Débito") 
 	 	    		||  this.cardNumeroParcelaDivSel.getDescricao().contains("Débito"))
 	 	    			msgX += "( Bandeira não permite Debito )";
 	 	    //	
 	    	if (this.wizardNomeCartaoCredito.isEmpty())
 	    		msgX += "( Nome )";
 	    	if (this.wizardNumeroCartaoCredito.isEmpty())
 	    		msgX += "( Número )";
 	    	//
 	    	if (this.isWizardIndCartaoCredito()) {
 	    		//
 	 	    	if (this.wizardCVVCartaoCredito.isEmpty())
 	 	    		msgX += "( CVV )";
 	 	    	if (this.wizardValidadeCartaoCredito.isEmpty())
 	 	    		msgX += "( Validade )";
 	    	}
 	    	//
 	    	if (!msgX.isEmpty()) {
 	    		//
 	            MpFacesUtil.addInfoMessage(" Alerta Cartão!... " + msgX); 	            
 	            //
 				return event.getOldStep();
 	    	}
 	    	
 	    	//
 	    	msgX = "";
 	    	
 	    	Long valorDocumento = this.mpBoletoSelX.getValorDocumento().longValue();
 	    	//
 	    	if (this.cardNumeroParcelaDocSel.getDescricao().contains("Débito") 
	    	&&  this.cardNumeroParcelaDivSel.getDescricao().contains("Débito")) {
 	    		//
 	    		valorDocumento = this.mpBoletoSelX.getValorCobrado().longValue();

 	    		msgX += this.checkCartaoCielo(valorDocumento);
 	    		//
 	    	} else {
 	    		//
 	    		valorDocumento = this.mpBoletoSelX.getValorDocumento().longValue();

 	    		msgX += this.checkCartaoCielo(valorDocumento);
 	    		//
 	    		valorDocumento = this.mpBoletoSelX.getValorTaxas().longValue();

 	    		msgX += this.checkCartaoCielo(valorDocumento);
 	    		//
 	    		// Se a primeira operação ocorrer com sucesso e a segunda não proceder, cancelamento da primeira ????
 	    	}
 	    	
 	    	//
 	    	if (!msgX.isEmpty()) {
 	    		//
 	            MpFacesUtil.addInfoMessage(" Alerta!!... " + msgX); 	            
 	            //
 				return event.getOldStep();
 	    	}
 	    }
 	    
 	    //   
 	    if (this.skip) {
     	   //
     	   this.skip = false;   //reset in case user goes back
     	   //
            return "confirm";
 	    }
 	    else {
 	    	//
 	    	return event.getNewStep();
 	    }
    }
		        
	// ---

    // API Cielo 3.0 ...
    
    public String checkCartaoCielo(Long valorDocumento) {
    	//
//		System.out.println("MpBoletoBean.geraCartaoCielo() - Entrou 000 ( indCartaoCielo = " + this.indCartaoCielo);
    	//
    	String msgCielo = "";
    	
    	try {
    		//
        	PaymentRequest paymentRequest = new PaymentRequest();

        	paymentRequest.setMerchantOrderId("2014111703"); // ??? No.Documento+DtNumProtocolo?
        	
        	//
        	Customer customer = new Customer();
        	
        	customer.setName(this.wizardNomeCartaoCredito); // ???

        	paymentRequest.setCustomer(customer);

        	//
        	Payment payment = new Payment();

        	payment.setType(CardType.CreditCard); // ?? Vai ter Debito?

        	payment.setAmount(valorDocumento);  // ???
        	
        	payment.setInstallments(1);
        	payment.setSoftDescriptor("123456789ABCD"); //???
        	
        	//
        	CreditCard creditCard = new CreditCard();
            
        	creditCard.setCardNumber(this.wizardNumeroCartaoCredito);
        	
        	creditCard.setHolder("Teste Holder"); // ???
        	
        	creditCard.setExpirationDate(this.wizardValidadeCartaoCredito);
        	creditCard.setSecurityCode(this.wizardCVVCartaoCredito);
        	
        	creditCard.setBrand(this.cardBrandSel.name()); // Visa???
        	
        	//
        	
        	CardOnFile cardOnFile = new CardOnFile();
        	
        	cardOnFile.setUsage("Used");
        	cardOnFile.setReason("Unscheduled");
        	
        	creditCard.setCardOnFile(cardOnFile);

        	//
        	
        	payment.setIsCryptoCurrencyNegotiation(true);
        	
        	payment.setCreditCard(creditCard);
       
        	//        	
        	paymentRequest.setPayment(payment);
        	
        	this.clientResponse = this.mpCieloWS.solicitarPagamento(paymentRequest);

//			System.out.println("MpBoletoBean.checkCartaoCielo() - ClientResponse = " + 
//																				this.clientResponse.getStatus());
			//
		} catch (Exception e) {
			//
			msgCielo = "Erro-API001 ( e = " + e;
		}
    	//
    	return msgCielo;
    }
   
    public void saveCartaoCielo() {
    	//
		MpFacesUtil.addErrorMessage("MpBoletoBean.saveCartaoCielo() - Falta Concluir! ");
    	//
    }
    
    // ---

	public MpBoleto getMpBoletoSelX() { return this.mpBoletoSelX; }
	public void setMpBoletoSelX(MpBoleto mpBoletoSelX) { this.mpBoletoSelX = mpBoletoSelX; }

	public List<MpBoleto> getMpBoletoList() { return this.mpBoletoList; }
	public void setMpBoletoList(List<MpBoleto> mpBoletoList) { this.mpBoletoList = mpBoletoList; }

	public List<MpBoleto> getMpBoletoListP() { return this.mpBoletoListP; }
	public void setMpBoletoListP(List<MpBoleto> mpBoletoListP) { this.mpBoletoListP = mpBoletoListP; }

	public HtmlDataTable getDataTable() { return dataTable; }
    public void setDataTable(HtmlDataTable dataTable) { this.dataTable = dataTable; }
    
	public String getMsgErro() { return msgErro; }
	public void setMsgErro(String msgErro) { this.msgErro = msgErro; }
    
	public String getBoletoRegistro() { return boletoRegistro; }
	public void setBoletoRegistro(String boletoRegistro) { this.boletoRegistro = boletoRegistro; }
    
	public String getBoletoRegistroRetorno() { return boletoRegistroRetorno; }
	public void setBoletoRegistroRetorno(String boletoRegistroRetorno) { 
												this.boletoRegistroRetorno = boletoRegistroRetorno; }
    
	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }
	
	public String getNumeroIntimacaoCode() { return numeroIntimacaoCode; }
	public void setNumeroIntimacaoCode(String numeroIntimacaoCode) { this.numeroIntimacaoCode = numeroIntimacaoCode; }
    
	public Date getDataIntimacao() { return dataIntimacao; }
	public void setDataIntimacao(Date dataIntimacao) { this.dataIntimacao = dataIntimacao; }
    
	public String getAmbienteBradesco() { return ambienteBradesco; }
	public void setAmbienteBradesco(String ambienteBradesco) { this.ambienteBradesco = ambienteBradesco; }
	
	public String getMensagemLog() { return mensagemLog; }
	public void setMensagemLog(String mensagemLog) { this.mensagemLog = mensagemLog; }
	
	public String getEmailLog() { return emailLog; }
	public void setEmailLog(String emailLog) { this.emailLog = emailLog; }
    
	public String getDataNumeroIntimacaoBarCode() { return dataNumeroIntimacaoBarCode; }
	public void setDataNumeroIntimacaoBarCode(String dataNumeroIntimacaoBarCode) { 
														this.dataNumeroIntimacaoBarCode = dataNumeroIntimacaoBarCode; }
	
	public String getCpfCnpj() { return cpfCnpj; }
	public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
	
	public String getCpfCnpj1() { return cpfCnpj1; }
	public void setCpfCnpj1(String cpfCnpj1) { this.cpfCnpj1 = cpfCnpj1; }
	
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioComarca getMpCartorioComarcaSel() { return mpCartorioComarcaSel; }
	public void setMpCartorioComarcaSel(MpCartorioComarca mpCartorioComarcaSel) { 
																this.mpCartorioComarcaSel = mpCartorioComarcaSel; }

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

	public Boolean getIndApos16h() { return indApos16h; }
	public void setIndApos16h(Boolean indApos16h) { this.indApos16h = indApos16h; }
	
	// --- 
	
	public StreamedContent getFile() { return fileX; }	      
	
	// ---
	
	public Boolean getIndRegistro() { return indRegistro; }
	public void setIndRegistro(Boolean indRegistro) { this.indRegistro = indRegistro; }
	
	public Boolean getIndGeraBoleto() { return indGeraBoleto; }
	public void setIndGeraBoleto(Boolean indGeraBoleto) { this.indGeraBoleto = indGeraBoleto; }

	public Boolean getIndCartorio() {
		//
		this.indCartorio = externalContext.isUserInRole("CARTORIOS");

		return indCartorio; 
	}
	public void setIndCartorio(Boolean indCartorio) { this.indCartorio = indCartorio; }

	public Boolean getIndComarca() {
		//
		this.indComarca = externalContext.isUserInRole("COMARCAS");

		return indComarca; 
	}
	public void setIndComarca(Boolean indComarca) { this.indComarca = indComarca; }

	public Boolean getIndUsuario() {
		//
		this.indUsuario = externalContext.isUserInRole("USUARIOS");

		return indUsuario; 
	}
	public void setIndUsuario(Boolean indUsuario) { this.indUsuario = indUsuario; }

	public Boolean getIndAdministrador() {
		//
		this.indAdministrador = externalContext.isUserInRole("ADMINISTRADORES");

		return indAdministrador; 
	}
	public void setIndAdministrador(Boolean indAdministrador) { this.indAdministrador = indAdministrador; }

	//
	
	public List<MpCartorioComarca> getMpCartorioComarcaList() { return mpCartorioComarcaList; }
	public void setMpCartorioComarcaSel(List<MpCartorioComarca> mpCartorioComarcaList) { 
														 		this.mpCartorioComarcaList = mpCartorioComarcaList; }
	
	public Boolean getIndEmailBoleto() { return indEmailBoleto; }
	public void setIndEmailBoleto(Boolean indEmailBoleto) { this.indEmailBoleto = indEmailBoleto; }
	
	public String getEmailBoleto() { return emailBoleto; }
	public void setEmailBoleto(String emailBoleto) { this.emailBoleto = emailBoleto; }

	//
	
	public Boolean getIndCartaoCielo() { return indCartaoCielo; }
	public void setIndCartaoCielo(Boolean indCartaoCielo) { this.indCartaoCielo = indCartaoCielo; }
	
	public Boolean getIndTermoCartaoCielo() { return indTermoCartaoCielo; }
	public void setIndTermoCartaoCielo(Boolean indTermoCartaoCielo) { this.indTermoCartaoCielo = indTermoCartaoCielo; }
	
	// ---
	
	public boolean isSkip() { return skip; }
	public void setSkip(boolean skip) { this.skip = skip; }

	//
	
	public String getWizardNomeCartaoCredito() { return wizardNomeCartaoCredito; }
	public void setWizardNomeCartaoCredito(String wizardNomeCartaoCredito) {
														this.wizardNomeCartaoCredito = wizardNomeCartaoCredito; }

	public String getWizardNumeroCartaoCredito() { return wizardNumeroCartaoCredito; }
	public void setWizardNumeroCartaoCredito(String wizardNumeroCartaoCredito) {
													this.wizardNumeroCartaoCredito = wizardNumeroCartaoCredito; }

	public String getWizardCVVCartaoCredito() {	return wizardCVVCartaoCredito;	}
	public void setWizardCVVCartaoCredito(String wizardCVVCartaoCredito) {
															this.wizardCVVCartaoCredito = wizardCVVCartaoCredito; }

	public String getWizardValidadeCartaoCredito() { return wizardValidadeCartaoCredito; }
	public void setWizardValidadeCartaoCredito(String wizardValidadeCartaoCredito) {
													this.wizardValidadeCartaoCredito = wizardValidadeCartaoCredito; }

	public Boolean isWizardIndCartaoCredito() { 
		//
		Boolean indCartao = false;
		
		if (null == this.cardNumeroParcelaDocSel 
		||	null == this.cardNumeroParcelaDivSel)
			indCartao = false;
		else
		if (this.cardNumeroParcelaDocSel.getDescricao().contains("Parcela") 
		||  this.cardNumeroParcelaDivSel.getDescricao().contains("Parcela"))
			indCartao = true;
		//
//		System.out.println("MpBoletoBean.isWizardIndCartaoCredito() - Entrou 001 ( indCartao = " + indCartao);

		return indCartao;
	}
	
	public Boolean getWizardIndCartaoCredito() { return wizardIndCartaoCredito;	}
	public void setWizardIndCartaoCredito(Boolean wizardIndCartaoCredito) {
															this.wizardIndCartaoCredito = wizardIndCartaoCredito; }
	
	public CardBrand getCardBrandSel() { return cardBrandSel; }
	public void setCardBrandSel(CardBrand cardBrandSel) { this.cardBrandSel = cardBrandSel; }

	public CardBrand[] getCardBrandList() { return cardBrandList; }
	public void setCardBrandList(CardBrand[] cardBrandList) { this.cardBrandList = cardBrandList; }

	public CardNumeroParcela[] getCardNumeroParcelaDocList() { return cardNumeroParcelaDocList; }
	public void setCardNumeroParcelaDocList(CardNumeroParcela[] cardNumeroParcelaDocList) {
														this.cardNumeroParcelaDocList = cardNumeroParcelaDocList; }

	public CardNumeroParcelaDebito[] getCardNumeroParcelaDivList() { return cardNumeroParcelaDivList; }
	public void setCardNumeroParcelaDivList(CardNumeroParcelaDebito[] cardNumeroParcelaDivList) {
														this.cardNumeroParcelaDivList = cardNumeroParcelaDivList; }

	public CardNumeroParcela getCardNumeroParcelaDocSel() { return cardNumeroParcelaDocSel; }
	public void setCardNumeroParcelaDocSel(CardNumeroParcela cardNumeroParcelaDocSel) {
															this.cardNumeroParcelaDocSel = cardNumeroParcelaDocSel; }
	
	public CardNumeroParcelaDebito getCardNumeroParcelaDivSel() { return cardNumeroParcelaDivSel; }
	public void setCardNumeroParcelaDivSel(CardNumeroParcelaDebito cardNumeroParcelaDivSel) {
															this.cardNumeroParcelaDivSel = cardNumeroParcelaDivSel; }

}