package com.mpxds.mpbasic.rest.service;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
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

import javax.servlet.ServletContext;
import javax.swing.ImageIcon;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpEspecieTituloBradesco;
import com.mpxds.mpbasic.model.vo.MpRegistroBoletoVO;
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
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
 
@Path("/mpBoletoWs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MpBoletoRestService {
	//
	private MpSistemaConfigs mpSistemaConfigs;
	private MpSistemaConfigService mpSistemaConfigService;
	private MpBoletos mpBoletos;
	private MpBoletoService mpBoletoService;
	private MpBoletoLogService mpBoletoLogService;
	private MpTitulos mpTitulos;
	private MpTituloService mpTituloService;
	
	private MpSeguranca mpSeguranca;
	
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfMD = new SimpleDateFormat("MM-dd");
	private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDMYp = new SimpleDateFormat("dd.MM.yyyy");
	
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
	
	@Context 
	ServletContext servletContext;

	// ---
	
	@GET
	@Path("/{oficio}/{protocolo}/{dtProtocolo}/{indAberto}/getStatus")
	public Response getValidaOficioProtocoloStatus(@PathParam("oficio") String oficioP,
													@PathParam("protocolo") String numeroIntimacaoP,
													@PathParam("dtProtocolo") String dataIntimacaoP,
													@PathParam("indAberto") String indAbertoP) {
		//
		String output = "MpBoletoRestService.getValidaOficioProtocoloCpfCnpj() ( " + oficioP + " / " 
												+ numeroIntimacaoP + " / " + dataIntimacaoP + " / " + indAbertoP;
		System.out.println(output);
		
		// Captura Dados Cartorio ...
		MpCartorioOficio mpCartorioOficioSel = MpCartorioOficio.OfX;
		try {
			//
			mpCartorioOficioSel = MpCartorioOficio.valueOf(oficioP);
			//
		} catch (Exception e) {
			//
    		output = "MpErro-001  ! ( " + oficioP + " ! Contactar Suporte Técnico !";
    		return Response.status(400).entity(output).build();
		}
		//
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/{oficio}/{protocolo}/{cpfCnpj}/get")
	public Response getValidaOficioProtocoloCpfCnpj(@PathParam("oficio") String oficioP,
													@PathParam("protocolo") String numeroIntimacaoP,
													@PathParam("cpfCnpj") String cpfCnpjP) {
		//
		String output = "MpBoletoRestService.getValidaOficioProtocoloCpfCnpj() ( " + oficioP + " / " 
																			+ numeroIntimacaoP + " / " + cpfCnpjP;
		System.out.println(output);
		
		// Captura Dados Cartorio ...
		MpCartorioOficio mpCartorioOficioSel = MpCartorioOficio.OfX;
		try {
			//
			mpCartorioOficioSel = MpCartorioOficio.valueOf(oficioP);
			//
		} catch (Exception e) {
			//
    		output = "MpErro-001  ! ( " + oficioP + " ! Contactar Suporte Técnico !";
    		return Response.status(400).entity(output).build();
		}
		
		//
		// Trata Ambiente de Produção x Homologação ...
	    //
		this.mpSistemaConfigs = MpCDIServiceLocator.getBean(MpSistemaConfigs.class);
		this.mpSeguranca = MpCDIServiceLocator.getBean(MpSeguranca.class);
		
    	MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro(oficioP +
    														"_Bradesco_Registro_URI");
    	if (null == mpSistemaConfig) {
    		output = "Erro Ambiente/Bradesco (NULL) ! ( " + oficioP +
												"_Bradesco_Registro_URI ) ... Contactar Suporte Técnico !";
    		return Response.status(400).entity(output).build();
    	}
		//
    	String ambienteBradesco = mpSistemaConfig.getValorT().toUpperCase().trim();	    
		
		// Verfica SÁBADO/DOMINGO e FERIADO !!!
		Date diaAtual = MpAppUtil.pegaDataAtual();
		
		String diaSemAtual = MpAppUtil.diaSemana(diaAtual);
		//
		if (this.mpSeguranca.getIndSabadoDomingo())
			if (diaSemAtual.equals("Sábado") || diaSemAtual.equals("Domingo")) {
				//
				output = "Serviço Indisponivel fim de semana ! ( " + diaSemAtual + " )";
	    		return Response.status(400).entity(output).build();
			}
		//
		String dataYMD = sdfYMD.format(diaAtual);
		String dataMD = sdfMD.format(diaAtual);
		
		if (dataMD.equals("11-02") || // Finados 
			dataMD.equals("12-25") || // Natal
			dataMD.equals("01-01")) { // Ano Novo
			//
			output = "Serviço Indisponivel feriado oficial ! ( " + dataMD + " )";
    		return Response.status(400).entity(output).build();
		}
		//		
		Boolean indRegistro = false;
		
		mpSistemaConfig = this.mpSistemaConfigs.porParametro(oficioP + "_Registro_Bradesco");
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
    		if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
    			indRegistro = false;
    		else
    			indRegistro = true;
    	}
    	
		// Trata Feriado Global ! MVPR-15112019 ...	
    	mpSistemaConfig = this.mpSistemaConfigs.porParametro("OfX_Feriado_" + dataYMD);
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
			output = "Serviço Indisponivel ! Feriado ( " + dataYMD + " - " + mpSistemaConfig.getDescricao();
    		return Response.status(400).entity(output).build();
    	}

    	mpSistemaConfig = this.mpSistemaConfigs.porParametro(oficioP + "_Feriado_" + dataYMD);
    	if (null == mpSistemaConfig)
    		assert(true); // nop
    	else {
			output = "Serviço Indisponivel ! Feriado ( " + dataYMD + " - " + mpSistemaConfig.getDescricao();
    		return Response.status(400).entity(output).build();
    	}
		
		// Verificar Horário de Funcionamento !
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    
	    String dateX = MpAppUtil.pegaDataAtual().toString();

	    String dtInicioS = "";
	    String dtFimS = "";

	    Date dtAtual = MpAppUtil.pegaDataAtual();
	    Date dtInicio = dtAtual;
	    Date dtFim = dtAtual;
	    //
	    try {
	    	// Ex.: Tue Nov 14 09:28:07 BRST 2017
	    	//      012345678901234567890123456789
	    	dtAtual = sdf.parse(dateX.substring(11, 16)); 

	    	mpSistemaConfig = this.mpSistemaConfigs.porParametro(oficioP + "_HorarioBoleto");
	    	if (null == mpSistemaConfig) {
	    		//
		    	dtInicioS =mpCartorioOficioSel.getHorarioFuncionamento().substring(0, 5);
		    	dtFimS = mpCartorioOficioSel.getHorarioFuncionamento().substring(6, 11);

		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	   		} else {
	   			//
    			if (!mpSistemaConfig.getIndValor()) {
	    			//
	    			output = "Serviço Temporariamente Indisponivel ! Tente mais tarde. ( " + oficioP;
	        		return Response.status(400).entity(output).build();
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
	    		output = "Fora do horário permitido ! ( De: " + dtInicioS + " às " + dtFimS + " ) - " 
	    																				+ sdf.format(dtAtual);
	    		return Response.status(400).entity(output).build();
	   		}
	    	//
	    	// Tratamento para após às 16h00m ...
	    	//
	    	Boolean indApos16h = false;
	    	
	    	if (dtAtual.after(sdfHMS.parse("16:00:00"))) {
	    		//
	    		indApos16h = true;
	    		// MpFacesUtil.addErrorMessage("Após 16h00m... o vencimento será para próximo dia útil !!!"); 
	    	}
		    //
	    	output = "{ \"indApos16h\" : \"" + indApos16h + "\"," 
	    			 + "\"ambienteBradesco\" : \"" + ambienteBradesco + "\","
			 		 + "\"indRegistro\" : \"" + indRegistro + "\" }";
	    	//
		} catch (ParseException e) {
			//
			output = "Error.001 - Tratamento Horas ! Contactar Suporte Técnico ! ( hI = " + 
							dtInicioS + " / hf = " + dtFimS + " / hA = " + dtAtual.toString() + " / e = " + e;
    		return Response.status(400).entity(output).build();
		}
		//
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/{oficio}/{code}/{protocolo}/{documento}/get")
	public Response getBoleto(@PathParam("oficio") String oficio, @PathParam("code") String code,
							  @PathParam("protocolo") String protocolo, @PathParam("documento") String documento) {
    	//
    	String output = "";
    	String mensagemLog = "";
    	String nomePdfX = "";

    	String nomeUsuario = "mpApiRest";
    	File arquivoPdf = null;
    	
    	Integer numeroGuiaGerado = null;
    	Integer numeroGuiaGeradoAnt = null;
    	
        String boletoRegistro = "";
        String boletoRegistroRetorno = "";
        String codigoErroRegistro = "";
        String ambienteBradesco = "";
        String numeroIntimacao = "";
    	
		//
		this.mpBoletos = MpCDIServiceLocator.getBean(MpBoletos.class);
		this.mpBoletoService = MpCDIServiceLocator.getBean(MpBoletoService.class);
		this.mpSistemaConfigs = MpCDIServiceLocator.getBean(MpSistemaConfigs.class);
		this.mpSistemaConfigService = MpCDIServiceLocator.getBean(MpSistemaConfigService.class);
		this.mpTitulos = MpCDIServiceLocator.getBean(MpTitulos.class);
		this.mpTituloService = MpCDIServiceLocator.getBean(MpTituloService.class);
		this.mpBoletoLogService = MpCDIServiceLocator.getBean(MpBoletoLogService.class);
		this.mpSeguranca = MpCDIServiceLocator.getBean(MpSeguranca.class);

    	//
    	MpBoleto mpBoletoSelX = mpBoletos.mpBoletoByOficioCodeProtocoloDocumento(oficio, code, protocolo, 
    																								documento);
    	if (null == mpBoletoSelX) {
			//
			output = "MpError001 - Error validação! ( " + oficio + "/" + code + "/" + protocolo + "/" +
																									documento;  
    		return Response.status(400).entity(output).build();
		}
    	
    	// MVPR_20180904 ...
    	if (null == mpBoletoSelX.getIndLiquidado() || mpBoletoSelX.getIndLiquidado() == false)
    		assert(true); // nop
    	else {
			//
			output = "MpError003 - Boleto Selecionado... < LIQUIDADO > ! Favor verificar.";  
    		return Response.status(400).entity(output).build();
		}
    	
    	// Captura Oficio ...
    	MpCartorioOficio mpCartorioOficioSel = MpCartorioOficio.OfX; 
	    try {
	    	//
	    	mpCartorioOficioSel = MpCartorioOficio.valueOf("Of" + mpBoletoSelX.getCodigoOficio());
	    	//
		} catch (Exception e) {
			//
			output = "MpError005 - Oficio Boleto... não existe! Favor verificar ( Of" + 
																				mpBoletoSelX.getCodigoOficio();  
    		return Response.status(400).entity(output).build();
		}
    	
    	//
    	Boolean indRegistro = true;
    	Boolean indApos16h = false;
    	Boolean indBoletoPrinter = false;
    	
		// Verificar Horário de Funcionamento !
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    
	    String dateX = MpAppUtil.pegaDataAtual().toString();	

	    String dtInicioS = "";
	    String dtFimS = "";

	    Date dtAtual = MpAppUtil.pegaDataAtual();
	    Date dtInicio = dtAtual;
	    Date dtFim = dtAtual;
	    //
	    try {
	    	// Ex.: Tue Nov 14 09:28:07 BRST 2017
	    	//      012345678901234567890123456789
	    	dtAtual = sdf.parse(dateX.substring(11, 16)); 

	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + 
		    																mpCartorioOficioSel.getNumero() + 
		    																"_HorarioBoleto");
	    	if (null == mpSistemaConfig) {
	    		//
		    	dtInicioS = mpCartorioOficioSel.getHorarioFuncionamento().substring(0, 5);
		    	dtFimS = mpCartorioOficioSel.getHorarioFuncionamento().substring(6, 11);

		    	dtInicio = sdf.parse(dtInicioS);
		    	dtFim = sdf.parse(dtFimS);
	   		} else {
	   			//
    			if (!mpSistemaConfig.getIndValor()) {
	    			//
    				output ="Serviço Temporariamente Indisponivel ! Tente mais tarde. ( Of.= "
			    															+ mpCartorioOficioSel.getNumero(); 
	        		return Response.status(400).entity(output).build();
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
	    		output = "Fora do horário permitido ! ( De: " + dtInicioS + " às " + dtFimS + " ) - " + 
	    																					sdf.format(dtAtual);
        		return Response.status(400).entity(output).build();
	   		}
	    	//
	    	// Tratamento para após às 16h00m ...
	    	//
	    	if (dtAtual.after(sdfHMS.parse("16:00:00"))) {
	    		//
	    		indApos16h = true;
	    		
	    		// MpFacesUtil.addErrorMessage("Após 16h00m... o vencimento será para próximo dia útil !!!"); 
	    	}
		    //
	    	mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + 
														mpCartorioOficioSel.getNumero() + "_Registro_Bradesco");
			if (null == mpSistemaConfig)
				assert (true); // nop
			else {
				if (null == mpSistemaConfig.getIndValor() || mpSistemaConfig.getIndValor() == false)
					indRegistro = false;
				else
					indRegistro = true;
			}
	    	//
		} catch (ParseException e) {
			//
			output = "Error.001 - Tratamento Horas ! Contactar Suporte Técnico ! ( hI = " + 
					dtInicioS + " / hf = " + dtFimS + " / hA = " + dtAtual.toString() + " / e = " + e;
    		return Response.status(400).entity(output).build();
		}
    	//
    	if (null == mpBoletoSelX.getNomePdfGerado() || mpBoletoSelX.getNomePdfGerado().isEmpty())
    		assert(true); // nop
    	else {
			//
    		if ((indApos16h) &&
    		    (null == mpBoletoSelX.getIndApos16h() || mpBoletoSelX.getIndApos16h().isEmpty()))
    		   assert(true); // Verificar se pode regerar? !
    		else {
    			//
//				MpFacesUtil.addErrorMessage("Boleto Selecionado ... foi ReGerado(2a.Via) !");
    			//
    	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + 
    													mpCartorioOficioSel.getNumero() + "_Boleto_Printer");
    	    	if (null == mpSistemaConfig)
    	    		indBoletoPrinter = false;
    	    	else
    	    		indBoletoPrinter = mpSistemaConfig.getIndValor();
	
//				ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//
//				if (indBoletoPrinter) {
//					//
//				    URI uri;
//					try {
//						uri = new URI(extContext.getRequestScheme(),
//						        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
//						        extContext.getRequestContextPath(), null, null);
//					    
//					    String contextPathX = uri.toASCIIString();
//
//		        		RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
//	    						"/resources/pdfs/of" + mpCartorioOficioSel.getNumero() + 
//								"/" + mpBoletoSelX.getNomePdfGerado() + "');");
//		        		//
//					} catch (URISyntaxException e) {
//						e.printStackTrace();
//					}
//				} else {
//					//
//			        try {
//			        	//
//		        		String pathX = extContext.getRealPath("//resources//pdfs//of" + 
//		        														mpCartorioOficioSel.getNumero() + "//");
//		        		//	
//		        		File fileXX = new File(pathX + mpBoletoSelX.getNomePdfGerado());
//			        	//
//		        	    FacesContext facesContextX = FacesContext.getCurrentInstance();
//		        	    HttpServletResponse responseX = (HttpServletResponse) facesContextX.getExternalContext()
//		        	    																		.getResponse();
//		        	    //	
//		        	    responseX.reset();
//			        	responseX.setContentType("application/pdf");
//			        	responseX.setContentLength((int) fileXX.length());
//			        	responseX.setHeader("Content-Disposition", "attachment; filename=\"" + 
//			        														mpBoletoSelX.getNomePdfGerado() + "\"");
//			        	//	
//			        	InputStream fisX = new FileInputStream(fileXX);
//			        	ServletOutputStream osX = responseX.getOutputStream();
//			        	//	
//			            byte[] bufferData = new byte[1024];
//			            int read=0;
//			            //
//			            while((read = fisX.read(bufferData))!= -1){
//			            	osX.write(bufferData, 0, read);
//			            }
//			            osX.flush();
//			            osX.close();
//			            fisX.close();
//			            //            
//			            responseX.flushBuffer();
//			             
//			            facesContextX.responseComplete(); // FacesContext.getCurrentInstance().responseComplete();
//			            //
//			        } catch (IOException e) {
//			        	e.printStackTrace();
//			        }
//				}					
		        //
				output = mpBoletoSelX.getNomePdfGerado();
				return Response.status(200).entity(output).build();
    		}
		}
		// Salva Boleto para exclusão da LISTA ....
		MpBoleto mpBoletoSelAntX = mpBoletoSelX;
		//
        try {
        	//
        	String msgErro = "";
        	
        	BoletoViewer boletoViewer = this.boletoCriar(mpCartorioOficioSel, mpBoletoSelX, indApos16h,
        													indRegistro, numeroGuiaGerado, ambienteBradesco,
        													boletoRegistro, boletoRegistroRetorno);
        													// this.boletoCriado();
        	if (null == boletoViewer) {
        		if (msgErro.isEmpty())
        			output = "Error Geração BOLETO! Favor Verificar ( Num.Documento = " + 
        															mpBoletoSelX.getNumeroDocumento();
        		else
        			output = "Error Geração BOLETO!! Favor Verificar ( " + msgErro;
        		//
        		return Response.status(400).entity(output).build();
        		//
        	} else
        		if (!msgErro.isEmpty()) {
        			//
        			output = "Error Geração BOLETO!!! Favor Verificar ( " + msgErro;
        			//
        	        // Grava Boleto Log...
        	        // ===================
        			mensagemLog = mensagemLog + " ( " + msgErro;
        			
        	        this.gravaBoletoLog(mpBoletoSelX, mpCartorioOficioSel,
							boletoRegistro, boletoRegistroRetorno,
							ambienteBradesco, mensagemLog, numeroGuiaGerado);
        			//
            		return Response.status(400).entity(output).build();
        		}
        	//
    		String nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");

    		nossoNumero = nossoNumero.replaceAll("-", "");
    		if (nossoNumero.isEmpty()) nossoNumero = "00000000000"; // ???????
        	//
    		String contadorPrt = "";
    		if (null == mpBoletoSelX.getContadorImpressao())
        		contadorPrt = "0";
    		else
        		contadorPrt = mpBoletoSelX.getContadorImpressao().toString();
    		//
        	nomePdfX = "MpBoleto_" + nomeUsuario + "_" + sdfYYYYMMDD.format(MpAppUtil.pegaDataAtual()) 
        													+ "_" + nossoNumero + "_" +	contadorPrt + ".pdf";

            String path = this.servletContext.getRealPath(File.separator) 
													+ File.separator  + "resources" + File.separator + "pdfs";
            
        	arquivoPdf = boletoViewer.getPdfAsFile(path + File.separator + nomePdfX);
        	//
//        	if (this.mpCartorioOficioSel.getNumero().equals("2"))
        	this.atualizaPdf(arquivoPdf, mpBoletoSelX);
        	//
//        	InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(
//        																		arquivoPdf.getAbsolutePath());  
//       	
//        	fileX = new DefaultStreamedContent(stream, "application/pdf", nomePdf);

//    		MpAppUtil.PrintarLn("MpBoletoBean.download() - 000.0 ( " + this.arquivoPdf.getAbsolutePath());
    		//    		
		} catch (Exception e) {
			e.printStackTrace();
		}  

        // Atualiza Nome + Data/Hora Impressão Boleto ...
        mpBoletoSelX.setDataImpressao(MpAppUtil.pegaDataAtual());
        mpBoletoSelX.setDataHoraImpressao(MpAppUtil.pegaDataAtual());
        mpBoletoSelX.setIndImpressao(true);
        mpBoletoSelX.setNomePdfGerado(nomePdfX);
        
        // Trata contador de Impressão ...
        if (null == mpBoletoSelX.getContadorImpressao())
        	mpBoletoSelX.setContadorImpressao(1);
        else
        	mpBoletoSelX.setContadorImpressao(mpBoletoSelX.getContadorImpressao() + 1);
        //
        if (null == mpBoletoSelX.getNumeroDocumento() || mpBoletoSelX.getNumeroDocumento().isEmpty()) 
        	mpBoletoSelX.setNumeroDocumento("00000"); // ?????
        if (null == mpBoletoSelX.getNossoNumero() || mpBoletoSelX.getNossoNumero().isEmpty()) 
        	mpBoletoSelX.setNossoNumero("00000"); // ?????

        // Atualiza número guia gerado !
        if (mpBoletoSelX.getContadorImpressao() > 1) { // Reimpressão Boleto! MVPR-29082018
        	MpBoleto mpBoletoGuia = mpBoletos.porId(mpBoletoSelX.getId());
        	if (null == mpBoletoGuia) {
    			output = "Error-X01 - Geração BOLETO(NULL)... Captura número GUIA(NULL) ! Verificar ! ( " +
																						mpBoletoSelX.getId();
        		return Response.status(400).entity(output).build();
        	} else
        		if (null == mpBoletoGuia.getNumeroGuiaGerado()) { // ??? MVPR-27092019
        			output = "Error-X02 - Geração BOLETO... Captura número GUIA(NULL) ! Favor Verificar ! ( " +
        												mpBoletoSelX.getId() + " / " + numeroGuiaGerado;
            		return Response.status(400).entity(output).build();
        		} else
        			mpBoletoSelX.setNumeroGuiaGerado(mpBoletoGuia.getNumeroGuiaGerado());
        	// assert(true);
        } else
        	mpBoletoSelX.setNumeroGuiaGerado(numeroGuiaGerado);
        
        // Atualiza indApos16h !
        if (indApos16h)
            mpBoletoSelX.setIndApos16h("*");
        else
            mpBoletoSelX.setIndApos16h("");
        //
        mpBoletoSelX = mpBoletoService.salvar(mpBoletoSelX);
        
        // Grava Boleto Log...
        // ===================
        gravaBoletoLog(mpBoletoSelX, mpCartorioOficioSel, boletoRegistro, boletoRegistroRetorno,
															ambienteBradesco, mensagemLog, numeroGuiaGerado);
        // ---
        // Localiza Titulo e altera STATUS ...
        Integer posX = mpBoletoSelX.getNomeSacado().indexOf("Protocolo:");

//        MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() Titulo ( nomeSacado = " + 
//        		mpBoletoSelX.getNomeSacado() +
//        		" / posX = " + posX);
//        
        if (posX >=0) {
        	// Ex.:
        	// SILVIO BARBOSA Protocolo: 14/12/2017-071131
        	// 01234567890123456789012345678901234567890123456789
        	String dataDocX = mpBoletoSelX.getNomeSacado().substring(posX + 11, posX + 21);
            //
            Date dataX;
			try {
				dataX = sdfDMY.parse(dataDocX);
				//
	            List<MpTitulo> mpTituloList = mpTitulos.mpTituloByNumeroDataProtocoloList(
					mpCartorioOficioSel.getNumero(), mpBoletoSelX.getNumeroIntimacao(), dataX);
	            
				for (MpTitulo mpTituloX : mpTituloList) {
					//
//					MpAppUtil.PrintarLn("Boleto Impresso... atualiza Status Titulo ... ( " + 
//										mpTituloX.getId() + " / " + mpTituloX.getNumeroProtocolo() + " / " + 
//										mpTituloX.getDataProtocolo());
					//
					mpTituloX.setStatus("DEPOSITO EMITIDO"); // Vide.Prisco(01022018!10h00)
					//mpTituloX.setComplemento("Impresso Complemento???");
					//
					mpTituloX = mpTituloService.salvar(mpTituloX);
				}

			} catch (ParseException e) {
	    		output = "Erro Atualização Titulo ! ( " + e;
	    		return Response.status(400).entity(output).build();
	    	}
    		//
        }
        
        // ---

        output = nomePdfX;
		return Response.status(200).entity(output).build();

//        String path = this.servletContext.getRealPath(File.separator) 
//        											+ File.separator  + "resources" + File.separator + "pdfs";
//        File fileDownload = new File(path + File.separator + nomePdfX);
//        
//        ResponseBuilder response = Response.ok((Object) fileDownload);
//        response.header("Content-Disposition", "attachment;filename=" + nomePdfX);
//        //
//        return response.build();
    }

	private void atualizaPdf(File fileX, MpBoleto mpBoletoSelX) {
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
				
		        String valCobradoX = df.format(mpBoletoSelX.getValorCobrado());
				
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
//			arquivoPdf = fileOutX;
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
    
	public void gravaBoletoLog(MpBoleto mpBoletoSelX, MpCartorioOficio mpCartorioOficioSel,
								String boletoRegistro, String boletoRegistroRetorno,
								String ambienteBradesco, String mensagemLog, Integer numeroGuiaGerado) {
		//
        MpBoletoLog mpBoletoLog = new MpBoletoLog();
        
        mpBoletoLog.setDataGeracao(MpAppUtil.pegaDataAtual());
        mpBoletoLog.setUsuarioNome("MpApiRest");
        mpBoletoLog.setUsuarioEmail("");
        mpBoletoLog.setNumeroOficio(mpCartorioOficioSel.getNumero());
        mpBoletoLog.setNumeroDocumento(mpBoletoSelX.getNumeroDocumento());
        mpBoletoLog.setValorDocumento(mpBoletoSelX.getValorDocumento());
        mpBoletoLog.setBoletoRegistro(boletoRegistro);
        mpBoletoLog.setBoletoRegistroRetorno(boletoRegistroRetorno);
        mpBoletoLog.setAmbienteBradesco(ambienteBradesco);
        mpBoletoLog.setMensagem(mensagemLog);
        
        // MR-26032019 (Solicitação 2Of. Rel.BoletosImpressos ...
        String protocolX = mpBoletoSelX.getNomeSacado().substring(0, 20);
        Integer posX = mpBoletoSelX.getNomeSacado().indexOf("Protocolo:");
        if (posX >= 0)
        	protocolX = mpBoletoSelX.getNomeSacado().substring(posX+11);
        mpBoletoLog.setProtocolo(protocolX);
        
        mpBoletoLog.setNumeroGuia(numeroGuiaGerado + "");
        mpBoletoLog.setValorCPMF(mpBoletoSelX.getValorCPMF());
        mpBoletoLog.setValorCobrado(mpBoletoSelX.getValorCobrado());
        mpBoletoLog.setValorTarifa(mpBoletoSelX.getValorTarifa());
        mpBoletoLog.setValorLeis(mpBoletoSelX.getValorLeis());
        //
        
        mpBoletoLog = mpBoletoLogService.salvar(mpBoletoLog);
        //
        enviaEmailLog(mpBoletoLog, mpCartorioOficioSel);
	}

	public void enviaEmailLog(MpBoletoLog mpBoletoLogEmail, MpCartorioOficio mpCartorioOficioSel) {
		//
		String paramEmailLog = "Of" + mpCartorioOficioSel.getNumero() + "_EmailBoleto";
		String emailLog = "";

		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramEmailLog);
	    if (null == mpSistemaConfig)
	   		assert(true);
	    else {
			//
	    	emailLog = mpSistemaConfig.getValor();
	    	//
	    	if (!emailLog.isEmpty()) {
	    		
	    		String[] emailLogList = emailLog .split(",");
	    		
	    		for (String emailLogX : emailLogList) {
	    			//
		    		if (!MpAppUtil.isEmail(emailLogX.trim())) {
		    			//
		    			System.out.println("E-mail Boleto Log... formato inválido ! Contactar Suporte ( " +
		    																				emailLogX.trim());			
		    			return;			
		    		}
	    		}
	    		//
	    		String subject = "Protesto RJ Capital ( Emissao de BOLETO Site - www.protestorjcapital.com.br )";
	    		
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
	    			System.out.println("Error-005 (Envio E-mail). Contactar Suporte Técnico ! ! ( e= " +
	    																								e.toString());
	    			System.out.println("MpRegeraSenhaBean() - Error-005 (Envio E-mail LOCAWEB) ( e= " + e.toString());
	    			//
	    			return;
	    		}
	    	}
		}
	}

	public BoletoViewer boletoCriar(MpCartorioOficio mpCartorioOficioSel, MpBoleto mpBoletoSelX,
									Boolean indApos16h, Boolean indRegistro,
									Integer numeroGuiaGerado, String ambienteBradesco,
									String boletoRegistro, String boletoRegistroRetorno) {
		//
//		MpAppUtil.PrintarLn("MpBoletoBean.boletoCriar() - 000");

		String nomeCedente = mpCartorioOficioSel.getNomeCedente();
		//
		Integer agenciaCodigoCedente = mpCartorioOficioSel.getAgenciaCodigoCedente();
		Integer agenciaContaCedente = mpCartorioOficioSel.getAgenciaContaCedente();
		// Cedente
		Cedente cedente = new Cedente(nomeCedente, mpCartorioOficioSel.getCnpj());
		// Sacado
		Sacado sacado = new Sacado(mpBoletoSelX.getNomeSacado());
		
		// Endereço do sacado
		Endereco endereco = new Endereco();
//		endereco.setUF(UnidadeFederativa.MG);// mpBoletoSel.getMpEnderecoLocal().getMpEstadoUF().getCodigo() ;
//		endereco.setLocalidade(mpBoletoSel.getMpEnderecoLocal().getCidade());
//		endereco.setCep(new CEP(mpBoletoSel.getMpEnderecoLocal().getCep()));
		endereco.setBairro(mpBoletoSelX.getMpEnderecoLocal().getBairro());
//		endereco.setLogradouro(mpBoletoSel.getMpEnderecoLocal().getLogradouro());
//		endereco.setNumero(mpBoletoSel.getMpEnderecoLocal().getNumero() + " / " + 
//							mpBoletoSel.getMpEnderecoLocal().getComplemento());
		
		sacado.addEndereco(endereco);
		
		// Criando o título
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
		
		contaBancaria.setAgencia(new Agencia(agenciaCodigoCedente, 
											 mpCartorioOficioSel.getAgenciaCodigoCedenteDig()));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(agenciaContaCedente,
		 									 mpCartorioOficioSel.getAgenciaContaCedenteDig()));
		
		contaBancaria.setCarteira(new Carteira(mpCartorioOficioSel.getCarteira()));

		//
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento(mpBoletoSelX.getNumeroDocumento());
		//
		String nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		
		nossoNumero = nossoNumero.replaceAll("-", "");
		if (nossoNumero.isEmpty()) nossoNumero = "00000000000"; // ???????
		
		if (nossoNumero.length() < 12)
			titulo.setNossoNumero(nossoNumero);
		else
			titulo.setNossoNumero(nossoNumero.substring(0,11)); // Numérico e Tam.max=11?
		//
		titulo.setDigitoDoNossoNumero(mpBoletoSelX.getNossoNumeroDig()); 
		// mpGeradorDigitoVerificador.gerarDigito(contaBancaria.getCarteira().getCodigo(), titulo.getNossoNumero()));
		//
		titulo.setValor(mpBoletoSelX.getValorDocumento());
		titulo.setValorCobrado(mpBoletoSelX.getValorCobrado());
	// 
		titulo.setAcrecimo(mpBoletoSelX.getValorAcrescimo());
		// 
		titulo.setDesconto(mpBoletoSelX.getValorTarifa());
		titulo.setDeducao(mpBoletoSelX.getValorCPMF());
		titulo.setMora(mpBoletoSelX.getValorLeis());
//		titulo.setAcrecimo(mpBoletoSelX.getValorIss());
		
//		ParametrosBancariosMap parametrosBancariosMap = new ParametrosBancariosMap(); 

		titulo.setDataDoDocumento(mpBoletoSelX.getDataDocumento());

		// Trata dados após 16h00m ...
		// ---------------------------
		if (indApos16h)
			titulo.setDataDoVencimento(mpBoletoSelX.getDataVencimento1()); 
		else
			titulo.setDataDoVencimento(mpBoletoSelX.getDataVencimento());
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
//		    mpBoletoSelX.getNossoNumero(),
//		    titulo.getDigitoDoNossoNumero());  
//		boleto.addTextosExtras("txtFcNossoNumero", nossoNumeroParaExibicao); 
//		boleto.addTextosExtras("txtRsNossoNumero", nossoNumeroParaExibicao); 		

		boleto.addTextosExtras("txtFcNossoNumero", mpBoletoSelX.getNossoNumero()); 		
		boleto.addTextosExtras("txtRsNossoNumero", mpBoletoSelX.getNossoNumero()); 		
		boleto.addTextosExtras("txtFcNumeroDocumento", mpBoletoSelX.getNumeroDocumento()); 		
		boleto.addTextosExtras("txtRsNumeroDocumento", mpBoletoSelX.getNumeroDocumento()); 		
		boleto.addTextosExtras("txtFcEspecie", "R$"); 		
		boleto.addTextosExtras("txtRsEspecie", "R$");
		
		// MVPR-21062019 NÂO SAI VALOR COBRADO 20.OFicio -> BrunoReportou! ... 
		String valCobradoX = (mpBoletoSelX.getValorCobrado() + "").replace(".", ",");
//		System.out.println("MpBoletoBean.000 ................................... ( valCobradoX = " + valCobradoX);		
		boleto.addTextosExtras("txtRsValorCobrado", valCobradoX);		
		//
		boleto.addTextosExtras("txtFcEspecieDocumento", mpBoletoSelX.getEspecieDocumento()); 	
		
		// Trata dados após 16h00m ...
		// ---------------------------
		if (indApos16h) {
			boleto.addTextosExtras("txtFcLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel1()); 		
			boleto.addTextosExtras("txtRsLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel1()); 		
			boleto.addTextosExtras("txtFcAgenciaCodigoCedente", mpBoletoSelX.getAgenciaCodigoCedente1()); 		
			boleto.addTextosExtras("txtRsAgenciaCodigoCedente", mpBoletoSelX.getAgenciaCodigoCedente1()); 
			//
			Image img1 = CodigoDeBarras.valueOf(mpBoletoSelX.getCodigoBarras()).toImage();
			boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img1).getImage());		
		} else {
			boleto.addTextosExtras("txtFcLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel());
			boleto.addTextosExtras("txtRsLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel());
			boleto.addTextosExtras("txtFcAgenciaCodigoCedente", mpBoletoSelX.getAgenciaCodigoCedente()); 		
			//
			Image img = CodigoDeBarras.valueOf(mpBoletoSelX.getCodigoBarras()).toImage();
			boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img).getImage());		
		}
		
		boleto.addTextosExtras("txtFcSacadoL3", mpBoletoSelX.getCpfCnpj()); 		

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
		
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoLocalPagamento");
		if (null != mpSistemaConfig) boleto.setLocalPagamento(mpSistemaConfig.getDescricao());	
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucaoAoSacado");
		if (null != mpSistemaConfig) boleto.setInstrucaoAoSacado(mpSistemaConfig.getDescricao());	
		
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao1");
		if (null != mpSistemaConfig) boleto.setInstrucao1(mpSistemaConfig.getDescricao());	
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao2");
		if (null != mpSistemaConfig) boleto.setInstrucao2(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao3");
		if (null != mpSistemaConfig) boleto.setInstrucao3(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao4");
		if (null != mpSistemaConfig) boleto.setInstrucao4(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao5");
		if (null != mpSistemaConfig) boleto.setInstrucao5(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao6");
		if (null != mpSistemaConfig) boleto.setInstrucao6(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao7");
		if (null != mpSistemaConfig) boleto.setInstrucao7(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao8");
		if (null != mpSistemaConfig) boleto.setInstrucao8(mpSistemaConfig.getDescricao());
				
		// =================================================

		// Trata hora impressão boleto ! 
		String horaHMS = sdfHMS.format(MpAppUtil.pegaDataAtual());
		
		// Trata registro Boleto (WebServices) ... BRADESCO !
		if (indRegistro) { // Vide SistConfig !
			if (null == mpBoletoSelX.getDataHoraRegistro()) { 
				MpRegistroBoletoVO mpRegistroBoletoVO = boletoRegistrar(mpBoletoSelX, mpCartorioOficioSel,
																		indApos16h, boletoRegistro,
																		boletoRegistroRetorno);
				if (mpRegistroBoletoVO.getStatus().equals("OK")) {
					//
					mpBoletoSelX = mpRegistroBoletoVO.getMpBoleto();
					numeroGuiaGerado = mpRegistroBoletoVO.getNumeroGuiaGerado();
				} else
					System.out.println("MpErro.1041 - Registro Boleto .......................... ( " + 
																		mpRegistroBoletoVO.getMensagem());
			} else {
				// Trata registro novamente para boletos reimpressos após às 16:00:00.00 (16H) ...
		    	Calendar calX = Calendar.getInstance();	

				calX.setTime(MpAppUtil.pegaDataAtual());
				calX.set(Calendar.HOUR_OF_DAY, 16);
				calX.set(Calendar.MINUTE, 0);
				calX.set(Calendar.SECOND, 0);
				calX.set(Calendar.MILLISECOND, 0);
				
				Date dataHora16H = calX.getTime();
				//
				Date dataHoraAtual = MpAppUtil.pegaDataAtual();
				
				if (dataHoraAtual.after(dataHora16H)) {
					//
					if (mpBoletoSelX.getDataHoraRegistro().before(dataHora16H)) {
						//
						MpRegistroBoletoVO mpRegistroBoletoVO = boletoRegistrar(mpBoletoSelX, mpCartorioOficioSel,
																				indApos16h, boletoRegistro,
																				boletoRegistroRetorno);
						if (mpRegistroBoletoVO.getStatus().equals("OK")) {
							//
							mpBoletoSelX = mpRegistroBoletoVO.getMpBoleto();
							numeroGuiaGerado = mpRegistroBoletoVO.getNumeroGuiaGerado();
						} else
							System.out.println("MpErro.1042 - Registo Boleto .......................... ( " + 
																				mpRegistroBoletoVO.getMensagem());
					}
				}
			}
		} else {
			// Sem REGISTRO no BRADESCO gera número da guia = 0 !
			mpBoletoSelX.setNumeroGuiaGerado(0);
			numeroGuiaGerado = 0 ;
		}
		//
		if (null == numeroGuiaGerado)
			numeroGuiaGerado = mpBoletoSelX.getNumeroGuiaGerado();
		//
		if (null != mpBoletoSelX.getBoletoInstrucao8()) 
			if (ambienteBradesco.toUpperCase().equals("HOMOLOGACAO"))
				boleto.setInstrucao8("                                GUIA Nº  " + numeroGuiaGerado + 
												"     HORA   " + horaHMS + "  ( Ambiente: HOMOLOCACAO )");
			else
				boleto.setInstrucao8("                                GUIA Nº  " + numeroGuiaGerado + 
												"     HORA   " + horaHMS);
		
		// Usa template Gerado no Writer.OpenOffice .odt) ...
//		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//		
//		String pathX = extContext.getRealPath("//resources//pdfs//");

        String pathX = this.servletContext.getRealPath(File.separator) 
        											+ File.separator + "resources" + File.separator + "pdfs";
		File mpTemplate = new File(pathX + File.separator + "MpBoletoTemplateSemSacadorAvalista.pdf");

		BoletoViewer boletoViewer = new BoletoViewer(boleto, mpTemplate);
        //
		return boletoViewer;
    }
	
	private MpRegistroBoletoVO boletoRegistrar(MpBoleto mpBoletoSelX, MpCartorioOficio mpCartorioOficioSel,
												Boolean indApos16h, String boletoRegistro,
												String boletoRegistroRetorno) {
		//
		MpRegistroBoletoVO mpRegistroBoletoVO = new MpRegistroBoletoVO("", "");
//		System.out.println("MpBoletoBean.boletoRegistrar() - ( CpfCnpj = " + mpBoletoR.getCpfCnpj());		
		
		// Trata Sequência número GUIA do Boleto ...
		// =================================================
		Integer numeroGuiaGerado = 0;
		Integer numeroGuiaGeradoAnt = 0;
		//
		String codigoErroRegistro = "";
		String mensagemLog = "";
		String msgErro = "";
		//
		MpSistemaConfig mpSistemaConfigX = mpSistemaConfigs.porParametro("Of" + 
											mpCartorioOficioSel.getNumero() + "_BoletoNumeroGuia");
		if (null == mpSistemaConfigX) {
			mensagemLog = mensagemLog + "( " + "Error ! Controle GUIA... não existe ! Contactar o SUPORTE. = " + 
											"Of" + mpCartorioOficioSel.getNumero() + "_BoletoNumeroGuia" + " ) ";
			//
		} else {
			if (mpSistemaConfigX.getValorN() == 0) {
				//
				String[] wordsNumeroGuia = mpBoletoSelX.getBoletoInstrucao8().split(" ");
				 
//				MpAppUtil.PrintarLn(mpBoletoSelX.getBoletoInstrucao8() + " / " + wordsNumeroGuia.length);
//				numeroGuia = Integer.parseInt(mpBoletoSelX.getBoletoInstrucao8().substring(8, 16).trim());
				
				try {
					numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);
					numeroGuiaGeradoAnt = numeroGuiaGerado;

					// Atualiza numeroGuia BD !!!
					mpSistemaConfigX.setValorN(numeroGuiaGerado);
					
					mpSistemaConfigService.salvar(mpSistemaConfigX);
					//
//					MpAppUtil.PrintarLn("Controle GUIA(0)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
//							numeroGuiaGerado);
				} catch (Exception e) {
					//
					mensagemLog = mensagemLog + "( " + "Error! Controle GUIA/Instrução8 ! Contactar SUPORTE = " + 
														mpBoletoSelX.getBoletoInstrucao8() + " / e = " + e + " )";
				}
			} else {
				numeroGuiaGerado = mpSistemaConfigX.getValorN();
				numeroGuiaGeradoAnt = numeroGuiaGerado;
				
				numeroGuiaGerado++;
		        // --------------------------------- //
		        // Verificar numero Guia Duplicado?  //
		        // --------------------------------- //
		        List<MpBoleto> mpBoletoGuias = mpBoletos.mpBoletoByNumeroGuiaGeradoList(
		        																	mpCartorioOficioSel.getNumero(),
		        																	numeroGuiaGerado);
		        if (mpBoletoGuias.size() > 0) {
		        	//
					numeroGuiaGerado++;
		        }

				// Atualiza numeroGuia BD !!!
				mpSistemaConfigX.setValorN(numeroGuiaGerado);
				
				mpSistemaConfigService.salvar(mpSistemaConfigX);
				//
				MpAppUtil.PrintarLn("Controle GUIA(1)... Atualizar SistemaConfig ( numeroGuiaGerado = " + 
						numeroGuiaGerado);
			}
		}
		
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
		mpBoletoRegistro.setNuCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(0, 10).replace(".", ""));
		mpBoletoRegistro.setFilialCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(11, 15));
		mpBoletoRegistro.setCtrlCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(16, 18));
		//
		mpBoletoRegistro.setCdTipoAcesso("2");
		mpBoletoRegistro.setClubBanco("2269651");
		mpBoletoRegistro.setCdTipoContrato("48");
		mpBoletoRegistro.setNuSequenciaContrato("0");
		mpBoletoRegistro.setIdProduto(mpBoletoSelX.getCarteira()); // "09"
		
		// Número da Negociação Formato: Agencia: 4 posições (Sem digito) -> 0123456789012345
		// Zeros: 7 posições Conta: 7 posições (Sem digito)               -> 3119-4/0002820-7
//		String agencia = String.format("%04d", mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4)); 
		if (indApos16h) {
			mpBoletoRegistro.setNuNegociacao(mpBoletoSelX.getAgenciaCodigoCedente1().substring(0, 4) + 
											String.format("%07d", 0) + 
											mpBoletoSelX.getAgenciaCodigoCedente1().substring(7, 14)); 
		} else {
			mpBoletoRegistro.setNuNegociacao(mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4) + 
											String.format("%07d", 0) + 
											mpBoletoSelX.getAgenciaCodigoCedente().substring(7, 14)); 
		}
		//
		mpBoletoRegistro.setCdBanco("237");		
		mpBoletoRegistro.seteNuSequenciaContrato("0");
		mpBoletoRegistro.setTpRegistro("1");
		mpBoletoRegistro.setCdProduto("0");
		
//		// MVPR(20180724)-AjusteErroCipProdução ! 
//		String nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");
//		nossoNumero = nossoNumero.replaceAll("-", "");
//		if (nossoNumero.isEmpty()) nossoNumero = ""; // ???????
//		if (nossoNumero.length() > 10)
//			nossoNumero = nossoNumero.substring(0, 11);
//		mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !

		//				                    01234567890123456
		// MVPR-20180807 09/02018041124-4 = 09020180411244 
		String nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		nossoNumero = nossoNumero.replaceAll("-", "");
		if (nossoNumero.isEmpty()) nossoNumero = ""; // ???????
		if (nossoNumero.length() > 13)
			nossoNumero = nossoNumero.substring(2, 13);
		mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !
		// MVPR-20180807

		//
		mpBoletoRegistro.setNuCliente(mpBoletoSelX.getNumeroDocumento().replace("/", ""));
		mpBoletoRegistro.setDtEmissaoTitulo(sdfDMYp.format(mpBoletoSelX.getDataDocumento()));

		if (indApos16h)
			mpBoletoRegistro.setDtVencimentoTitulo(sdfDMYp.format(mpBoletoSelX.getDataVencimento1()));
		else
			mpBoletoRegistro.setDtVencimentoTitulo(sdfDMYp.format(mpBoletoSelX.getDataVencimento()));
		//
		mpBoletoRegistro.setTpVencimento("0");
		
		//MVPR(20180502)-Ajuste Linha Digitavel !
//		String valorX = mpBoletoSelX.getValorDocumento().toString().replace(".","");
		String valorX = mpBoletoSelX.getValorCobrado().toString().replace(".","");
		mpBoletoRegistro.setVlNominalTitulo(valorX);
		//
		try {
			MpEspecieTituloBradesco mpEspecieTituloBradesco = MpEspecieTituloBradesco.valueOf(
																		mpBoletoSelX.getEspecieDocumento().trim());
			if (null==mpEspecieTituloBradesco) {
				//
				mensagemLog = mensagemLog + 
								"( Registro/Bradesco. Não existe Espécie Título (Null/Assumiu=99) / sigla = " +
								mpBoletoSelX.getEspecieDocumento() + " ) ";
				//
				mpBoletoRegistro.setCdEspecieTitulo("99");
			} else
				mpBoletoRegistro.setCdEspecieTitulo(mpEspecieTituloBradesco.getSigla());
		}
		catch (Exception e) {
			mensagemLog = mensagemLog + 
								"( Registro/Bradesco. Não existe Espécie Título (Exception/Assumiu=99) / sigla = " +
								mpBoletoSelX.getEspecieDocumento() + " ) ";
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
		if (mpBoletoSelX.getNomeSacado().length() > 69)
			mpBoletoRegistro.setNomePagador(mpBoletoSelX.getNomeSacado().substring(0, 69));
		else
			mpBoletoRegistro.setNomePagador(mpBoletoSelX.getNomeSacado());
		//
		if (mpBoletoSelX.getMpEnderecoLocal().getLogradouro().length() > 39)
			mpBoletoRegistro.setLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getLogradouro().
																								substring(0, 39));
		else
			mpBoletoRegistro.setLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getLogradouro());
		//
		mpBoletoRegistro.setNuLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getNumero());

		if (null==mpBoletoSelX.getMpEnderecoLocal().getComplemento())
			mpBoletoRegistro.setComplementoLogradouroPagador("");
		else
			mpBoletoRegistro.setComplementoLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().
																					getComplemento());
		
		mpBoletoRegistro.setCepPagador(mpBoletoSelX.getMpEnderecoLocal().getCep().substring(0, 5));
		mpBoletoRegistro.setComplementoCepPagador("0");
		mpBoletoRegistro.setBairroPagador(mpBoletoSelX.getMpEnderecoLocal().getBairro());

		if (null==mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF()) {
			mpBoletoRegistro.setMunicipioPagador("RioJaneiro?");
			mpBoletoRegistro.setUfPagador("RJ");
		} else {
			mpBoletoRegistro.setMunicipioPagador(mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().
																						getDescricao());
			mpBoletoRegistro.setUfPagador(mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().name());
		}
		//
//		System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0001 = " + mpBoletoSelX.getCpfCnpj());
		
		mpBoletoRegistro.setCdIndCpfcnpjPagador("2"); // CNPJ
		mpBoletoRegistro.setNuCpfcnpjPagador(mpBoletoSelX.getCpfCnpj());		
		if (mpBoletoSelX.getCpfCnpj().trim().length() < 12) {
			mpBoletoRegistro.setCdIndCpfcnpjPagador("1"); // CPF
			if (mpBoletoSelX.getCpfCnpj().trim().length() == 11)
				mpBoletoRegistro.setNuCpfcnpjPagador("000" + mpBoletoSelX.getCpfCnpj().trim());
			else
			if (mpBoletoSelX.getCpfCnpj().trim().length() == 10)
				mpBoletoRegistro.setNuCpfcnpjPagador("0000" + mpBoletoSelX.getCpfCnpj().trim());
			else
			if (mpBoletoSelX.getCpfCnpj().trim().length() == 9)
				mpBoletoRegistro.setNuCpfcnpjPagador("00000" + mpBoletoSelX.getCpfCnpj().trim());
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
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002");

			// Convert Objeto em JSON ! 
			// ========================
			ObjectMapper objectMapper = new ObjectMapper();
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002-1");
			
			//configure Object mapper for pretty print
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002-2");
			
			//writing to console, can write to any output stream such as file
			StringWriter stringBoleto = new StringWriter();
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002-3");

			objectMapper.writeValue(stringBoleto, mpBoletoRegistro);
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002-4");
			//
			boletoRegistro = stringBoleto.toString();
			boletoRegistro = boletoRegistro.replaceAll(", ", ",");
			boletoRegistro = boletoRegistro.replaceAll(" : ", ":");

			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003 ( " + boletoRegistro);
						
			// PKCS7Signer signer = new PKCS7Signer();
			KeyStore keyStore = loadKeyStore();
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003-0");

			CMSSignedDataGenerator signatureGenerator = setUpProvider(keyStore);
			
			// Json de teste...
//			String json = "{\"nuCPFCNPJ\": \"123456789\",\"filialCPFCNPJ\": \"0001\",\"ctrlCPFCNPJ\": \"39\",\"cdTipoAcesso\": \"2\",\"clubBanco\": \"0\",\"cdTipoContrato\": \"0\",\"nuSequenciaContrato\": \"0\",\"idProduto\": \"09\",\"nuNegociacao\": \"123400000001234567\",\"cdBanco\": \"237\",\"eNuSequenciaContrato\": \"0\",\"tpRegistro\": \"1\",\"cdProduto\": \"0\",\"nuTitulo\": \"0\",\"nuCliente\": \"123456\",\"dtEmissaoTitulo\": \"25.05.2017\",\"dtVencimentoTitulo\": \"20.06.2017\",\"tpVencimento\": \"0\",\"vlNominalTitulo\": \"100\",\"cdEspecieTitulo\": \"04\",\"tpProtestoAutomaticoNegativacao\": \"0\",\"prazoProtestoAutomaticoNegativacao\": \"0\",\"controleParticipante\": \"\",\"cdPagamentoParcial\": \"\",\"qtdePagamentoParcial\": \"0\",\"percentualJuros\": \"0\",\"vlJuros\": \"0\",\"qtdeDiasJuros\": \"0\",\"percentualMulta\": \"0\",\"vlMulta\": \"0\",\"qtdeDiasMulta\": \"0\",\"percentualDesconto1\": \"0\",\"vlDesconto1\": \"0\",\"dataLimiteDesconto1\": \"\",\"percentualDesconto2\": \"0\",\"vlDesconto2\": \"0\",\"dataLimiteDesconto2\": \"\",\"percentualDesconto3\": \"0\",\"vlDesconto3\": \"0\",\"dataLimiteDesconto3\": \"\",\"prazoBonificacao\": \"0\",\"percentualBonificacao\": \"0\",\"vlBonificacao\": \"0\",\"dtLimiteBonificacao\": \"\",\"vlAbatimento\": \"0\",\"vlIOF\": \"0\",\"nomePagador\": \"Cliente Teste\",\"logradouroPagador\": \"rua Teste\",\"nuLogradouroPagador\": \"90\",\"complementoLogradouroPagador\": \"\",\"cepPagador\": \"12345\",\"complementoCepPagador\": \"500\",\"bairroPagador\": \"bairro Teste\",\"municipioPagador\": \"Teste\",\"ufPagador\": \"SP\",\"cdIndCpfcnpjPagador\": \"1\",\"nuCpfcnpjPagador\": \"12345648901234\",\"endEletronicoPagador\": \"\",\"nomeSacadorAvalista\": \"\",\"logradouroSacadorAvalista\": \"\",\"nuLogradouroSacadorAvalista\": \"0\",\"complementoLogradouroSacadorAvalista\": \"\",\"cepSacadorAvalista\": \"0\",\"complementoCepSacadorAvalista\": \"0\",\"bairroSacadorAvalista\": \"\",\"municipioSacadorAvalista\": \"\",\"ufSacadorAvalista\": \"\",\"cdIndCpfcnpjSacadorAvalista\": \"0\",\"nuCpfcnpjSacadorAvalista\": \"0\",\"endEletronicoSacadorAvalista\": \"\"}";

//			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0004 ( json = " + boletoRegistro);
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003-1");

			byte[] signedBytes = signPkcs7(boletoRegistro.getBytes("UTF-8"), signatureGenerator);
			System.out.println("Signed Encoded Bytes: " + new String(Base64.encode(signedBytes)));
	
			HttpEntity entity = new StringEntity(new String(Base64.encode(signedBytes)), Charset.forName("UTF-8"));
			System.out.println("XXX004 = " + entity);
			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003-2");
			
			// Trata Ambiente de Produção x Homologação ...
//	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Bradeco_Registro_URI");
	    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + 
	    									mpCartorioOficioSel.getNumero().trim() + "_Bradesco_Registro_URI");
	    	if (null == mpSistemaConfig) {
	    		msgErro = " ( Contactar o Suporte... Não foi encontrado parâmetro do sistema = 'Of" + 
	    										mpCartorioOficioSel.getNumero().trim() +"_Bradesco_Registro_URI'";
	    		mensagemLog = mensagemLog + msgErro + " ) ";
	    		//
				mpRegistroBoletoVO.setStatus("ERRO");
				mpRegistroBoletoVO.setMensagem(mensagemLog);
				//
				System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0004 ( " + mensagemLog);
								
	    		return mpRegistroBoletoVO;    		
	    	}
			//			
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0004-1");
			
    		HttpPost post = new HttpPost(URI_REGISTRO_HOMOLOGACAO);
	    	if (mpSistemaConfig.getValorT().toUpperCase().equals("PRODUCAO"))
	    		post = new HttpPost(URI_REGISTRO_PRODUCAO);
	    	else
		    	if (mpSistemaConfig.getValorT().trim().toUpperCase().equals("HOMOLOGACAO"))
		    		assert(true); // nop
		    	else {
		    		msgErro = " ( Contactar o Suporte...  BRAD.0001 - Inválido parâmetro do sistema = " + 
		    															mpSistemaConfig.getValorT().trim();
		    		mensagemLog = mensagemLog + msgErro + " ) ";
		    		//
					mpRegistroBoletoVO.setStatus("ERRO");
					mpRegistroBoletoVO.setMensagem(mensagemLog);
					//
					System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0005 ( " + mensagemLog);

					return mpRegistroBoletoVO;    		
		    	}
	    	//
			System.out.println("XXX005 = " + post);
			
			post.setEntity(entity);
			System.out.println("XXX006 = POST");
	
			HttpClientBuilder builder = HttpClientBuilder.create();
			System.out.println("XXX007 = " + builder);
			
			// ??? Erro PKIX path building failed -> keytool -insert ... 2 certificados vindo do BRADESCO ...
			// Baixei e usei o utilitário ... KeyStore Explorer 5.3.2 !
			HttpResponse response = builder.build().execute(post); // ??? Error !
			
			System.out.println("XXX008 = " + response.getStatusLine());
			
			String boletoRetorno = EntityUtils.toString(response.getEntity(), "UTF-8");		
			System.out.println("XXX009 = " + boletoRetorno);
			
			Integer posI = boletoRetorno.indexOf("{");
			Integer posF = boletoRetorno.indexOf("}");
			System.out.println("XXX009-1 = " + posI + " / " + posF + " / " + boletoRetorno.length());
			
			boletoRegistroRetorno = boletoRetorno.substring(posI, posF + 1);
			System.out.println("XXX0010 = " + boletoRegistroRetorno);
					
			// Convert JSON Retorno para Objeto ! 
			// ========================
			ObjectMapper objectMapperRet = new ObjectMapper();
			
			//JSON from String to Object
			MpBoletoRegistroRetorno mpBoletoRegistroRetorno = objectMapperRet.readValue(
																		boletoRegistroRetorno,
																		MpBoletoRegistroRetorno.class);
			if (mpBoletoRegistroRetorno.getCdErro().equals("0")  ||
				mpBoletoRegistroRetorno.getCdErro().equals("00"))
				mpBoletoSelX.setDataHoraRegistro(MpAppUtil.pegaDataAtual());
			else {
				if (mpBoletoRegistroRetorno.getCdErro().equals("69")) { // Titulo Cadastrado ! Avisar Erro !
	    			msgErro = " ( Cod.Erro/Bradesco) = " + mpBoletoRegistroRetorno.getCdErro() + 
								"/ msg.erro = " + mpBoletoRegistroRetorno.getMsgErro() + " / Contactar o Suporte ";
	    			mensagemLog = mensagemLog + msgErro + " ) ";
				} else {
					msgErro = " ( Cod.Erro/Bradesco = " + mpBoletoRegistroRetorno.getCdErro() + 
    										"/ msg.erro = " + mpBoletoRegistroRetorno.getMsgErro();
					mensagemLog = mensagemLog + msgErro + " ) ";
				}
				mpRegistroBoletoVO.setStatus("ERRO");
				mpRegistroBoletoVO.setMensagem(mensagemLog);
				//
				System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0006 ( " + mensagemLog);

				return mpRegistroBoletoVO;    		
			}
			//
			codigoErroRegistro = mpBoletoRegistroRetorno.getCdErro();
			//
			System.out.println("XXX011 / Cod.Erro = " + mpBoletoRegistroRetorno.getCdErro() + 
								" / Erro: " + msgErro);
			//
		} catch (Exception e) {
			//
    		msgErro = " ( Exceção = " + e;
    		mensagemLog = mensagemLog + msgErro + " ) ";
    		//
			mpRegistroBoletoVO.setStatus("ERRO");
			mpRegistroBoletoVO.setMensagem(mensagemLog);
			//
			System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0009 ( " + mensagemLog);
			
    		return mpRegistroBoletoVO;    		
		}
		// Se o Boleto já foi registrado (CodErro=69)... Não incrementa o Número da GUIA ! ??? MVPR-10042019 !
		if (codigoErroRegistro.equals("69")) {
			//
			mpSistemaConfigX = mpSistemaConfigs.porParametro("Of" + mpCartorioOficioSel.getNumero() +
																							"_BoletoNumeroGuia");
			if (null == mpSistemaConfigX) {
				mensagemLog = mensagemLog + "( " + "Error! Controle GUIA... não existe ! Contactar o SUPORTE. = " + 
											"Of" + mpCartorioOficioSel.getNumero() + "_BoletoNumeroGuia" + " ) ";
				//
				mpRegistroBoletoVO.setStatus("ERRO");
				mpRegistroBoletoVO.setMensagem(mensagemLog);
				//
				System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0007 ( " + mensagemLog);

				return mpRegistroBoletoVO;    		
			} else {
				if (mpSistemaConfigX.getValorN() == 0)
					assert(true);
				else {
					// Atualiza numeroGuia BD !!!
					mpSistemaConfigX.setValorN(numeroGuiaGeradoAnt);
			
					mpSistemaConfigService.salvar(mpSistemaConfigX);
				}
			}
		}
		//
		mpRegistroBoletoVO.setStatus("OK");
		mpRegistroBoletoVO.setMensagem("");
		mpRegistroBoletoVO.setNumeroGuiaGerado(numeroGuiaGerado);
		mpRegistroBoletoVO.setMpBoleto(mpBoletoSelX);
		//
		System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0008 ( " + mensagemLog);

		return mpRegistroBoletoVO;
	}
	
	private KeyStore loadKeyStore() throws Exception {
		//
		KeyStore keystore = KeyStore.getInstance("JKS");

//		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		String numOf = "1"; // ????? Usando certificado do 2o.Oficio ! 
		
		String pathOf = PATH_TO_KEYSTORE_1OF;
		if (numOf.equals("2")) pathOf = PATH_TO_KEYSTORE_2OF;
		if (numOf.equals("3")) pathOf = PATH_TO_KEYSTORE_3OF;
		if (numOf.equals("4")) pathOf = PATH_TO_KEYSTORE_4OF;

        String pathX = this.servletContext.getRealPath(File.separator) 
        												+ File.separator  + "resources" + File.separator + "opt";
		InputStream is = new FileInputStream(pathX + File.separator + pathOf);
		
		System.out.println("loadKeyStore() ...... XXX001 = " + pathX + File.separator + pathOf);

//		InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());

		System.out.println("XXX002 = " + keystore);
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
	
//    public void listaBoletoEmitido(MpCartorioOficio mpCartorioOficioSel, String cpfCnpj) {
//		//
////		System.out.println("MpBoletoBean.listaBoletoList() - ( Entrou 000 ");
//
//		//
//		if (!cpfCnpj.isEmpty()) {
//			//
//	    	String cpfCnpjX = formataCpfCnpj(cpfCnpj); 
//			
//			if (validaCpfCnpj(cpfCnpj))
//				return;
//			//
//			if (mpCartorioOficioSel.getNumero().equals("X"))
//				mpBoletoListP = mpBoletos.mpBoletoByImpressaoCpfCnpjList(cpfCnpjX);
//			else
//				mpBoletoListP = mpBoletos.mpBoletoByImpressaoOficioCpfCnpjList(mpCartorioOficioSel.getNumero(), 
//																													cpfCnpjX);
//			if (mpBoletoListP.size() == 0) {
//				//
//				MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) Emitido(s) na Base de Dados !");
//				return;
//			}
//			//		
//		} else {
////			System.out.println("MpBoletoBean.listaBoleto() - ( " + mpCartorioOficioSel.getNumero() + " / " +
////					sdfDMY.format(dataIntimacao) + " / " + numeroIntimacao);
//			//
//			if (mpCartorioOficioSel.getNumero().equals("X"))
//				mpBoletoListP = mpBoletos.mpBoletoByImpressaoList();
//			else
//				mpBoletoListP = mpBoletos.mpBoletoByImpressaoOficioList(mpCartorioOficioSel.getNumero());
//		}
//		//
////		System.out.println("MpBoletoBean.listaBoletoList() - ( size = " + mpBoletoList.size());
//		//
//		if (mpBoletoListP.size() == 0) {
//			//
//			MpFacesUtil.addErrorMessage("Não constam nenhum Boleto(s) na Base de Dados ! (" +
//						mpCartorioOficioSel.getNumero() + " / " + cpfCnpj);
//			return;
//		}
//    }
//    
//    public void geraBoletoP() {
//        //
//    	if (indGeraBoleto) {
//    		//
//    		try {
//				TimeUnit.SECONDS.sleep(10);
//				//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    		//
//    		indGeraBoleto = false;
////    		
////    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = TRUE");    		
//    		return;
//    	} else {
//    		//
//    		indGeraBoleto = true;
////    		
////    		System.out.println("MpBoletoBean.geraBoleto() - indGeraBoleto = FALSE");
//    	}
//    	//
////		MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 ( idBolP = " + 
////														" / numCode = " + numeroIntimacaoCode); 
//		//
//    	if (null == mpBoletoSelX) {
//			//
//			MpFacesUtil.addErrorMessage("Error.3P - Boleto Selecionado ! Contactar Suporte Técnico (null.Bol");  
//			return;
//		}
//    	//
//    	mpBoletoSelX = mpBoletos.porId(mpBoletoSelX.getId());
//    	if (null == mpBoletoSelX) {
//			//
//			MpFacesUtil.addErrorMessage("Error.4P - Boleto Selecionado ! Contactar Suporte Técnico (idNull.Bol");  
//			return;
//		}
//    	//    	
//    	if (null == mpBoletoSelX.getNomePdfGerado() || mpBoletoSelX.getNomePdfGerado().isEmpty()) {
//			MpFacesUtil.addErrorMessage("Error.5P - Boleto Selecionado Não Existe PDF ! Contactar Suporte Técnico");  
//
//			// Correção/Erro Informado Heraldo - MR-20191210 !
//			this.nomePdfX = "MpBoleto_" + mpSeguranca.getLoginUsuario() + "_" + sdfYYYYMMDD.format(new Date()) 
//												+ "_" + this.mpBoletoSelX.getNossoNumero() + ".pdf";
//			return;
//    	}
//		//
//    	if (indUsuario) {
//    		//
//    		if (null == numeroIntimacaoCode || numeroIntimacaoCode.isEmpty()) {
//    			//
//    			MpFacesUtil.addErrorMessage("Informar o código Segurança ! ( Num. Documento = " + 
//															mpBoletoSelX.getNumeroDocumento());
//    			return;
//    		}
//    		//
//    		String numeroIntimacaoDigX = mpBoletoSelX.getNumeroIntimacaoCode().trim();
//
////			MpAppUtil.PrintarLn("MpBoletoBean.geraBoleto() - 000 (dig3 = " + numeroIntimacaoDigX + " / code = " + 
////						numeroIntimacaoCode.trim() + " / numInt = " + mpBoletoSelX.getNumeroIntimacao() + 
////						" / IntCode = " + mpBoletoSelX.getNumeroIntimacaoCode() + " )");
//    		//
//    		if (numeroIntimacaoDigX.equals(numeroIntimacaoCode.trim()))
//    			assert(true); // nop
//    		else {
//    			MpFacesUtil.addErrorMessage("Código Segurança inválido ! Favor verificar. ( Num. Documento = " + 
//						mpBoletoSelX.getNumeroDocumento() + " / Código Informado = " + numeroIntimacaoCode);
//    			return;
//    		}
//    	}
//    	
//    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + 
//														mpCartorioOficioSel.getNumero() + "_Boleto_Printer");
//    	if (null == mpSistemaConfig)
//    		indBoletoPrinter = false;
//    	else
//    		indBoletoPrinter = mpSistemaConfig.getIndValor();
//    	//
//		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//
//		if (indBoletoPrinter) {
//			//
//		    URI uri;
//			try {
//				uri = new URI(extContext.getRequestScheme(),
//				        null, extContext.getRequestServerName(), extContext.getRequestServerPort(), 
//				        extContext.getRequestContextPath(), null, null);
//			    
//			    String contextPathX = uri.toASCIIString();
//
//        		RequestContext.getCurrentInstance().execute("printJS('" + contextPathX +
//						"/resources/pdfs/of" + mpCartorioOficioSel.getNumero() + 
//						"/" + mpBoletoSelX.getNomePdfGerado() + "');");
//        		//
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//		} else {
//			//
//	        try {
//	        	//
//        		String pathX = extContext.getRealPath("//resources//pdfs//of" + 
//        																mpCartorioOficioSel.getNumero() + "//");
//        		//	
//        		File fileXX = new File(pathX + mpBoletoSelX.getNomePdfGerado());
//            	if (!fileXX.exists()) {
//        			MpFacesUtil.addErrorMessage("Boleto Selecionado... não existe arquivo/PDF para geração 2a.Via !" +
//        																				" Contactar Suporte Técnico");  
//        			return;
//            	}
//	        	//
//        	    FacesContext facesContextX = FacesContext.getCurrentInstance();
//        	    HttpServletResponse responseX = (HttpServletResponse) facesContextX.getExternalContext().getResponse();
//        	    //	
//        	    responseX.reset();
//	        	responseX.setContentType("application/pdf");
//	        	responseX.setContentLength((int) fileXX.length());
//	        	responseX.setHeader("Content-Disposition", "attachment; filename=\"" + 
//	        																this.mpBoletoSelX.getNomePdfGerado() + "\"");
//	        	//	
//	        	InputStream fisX = new FileInputStream(fileXX);
//	        	ServletOutputStream osX = responseX.getOutputStream();
//	        	//	
//	            byte[] bufferData = new byte[1024];
//	            int read=0;
//	            //
//	            while((read = fisX.read(bufferData))!= -1){
//	            	osX.write(bufferData, 0, read);
//	            }
//	            osX.flush();
//	            osX.close();
//	            fisX.close();
//	            //            
//	            responseX.flushBuffer();
//	             
//	            facesContextX.responseComplete(); // FacesContext.getCurrentInstance().responseComplete();
//	            //
//	        } catch (IOException e) {
//	        	e.printStackTrace();
//	        }
//	        //
//			return;
//		}
//    }
        
	@GET
	@Path("/{cpfCnpj}/cpfValida")
	public Response getValidaCpfCnpj(@PathParam("cpfCnpj") String cpfCnpjX) {
    	//
		String output = "OK";
		
		if (cpfCnpjX.length() == 11 || cpfCnpjX.length() == 14)
			assert(true); // nop
		else {
			output = "CPF/CNPJ formato Inválido ! ( " + cpfCnpjX;
    		return Response.status(400).entity(output).build();
		}
		// Trata CPF ...
		if (cpfCnpjX.length() == 11)
			if (MpAppUtil.isValidCPF(cpfCnpjX))
				assert(true); // nop
			else {
				output = "CPF Inválido ! ( " + cpfCnpjX;
	    		return Response.status(400).entity(output).build();
			}
		// Trata CNPJ ...
		if (cpfCnpjX.length() == 14) 
			if (MpAppUtil.isValidCNPJ(cpfCnpjX))
				assert(true); // nop
			else {
				output = "CNPJ Inválido ! ( " + cpfCnpjX;
	    		return Response.status(400).entity(output).build();
			}
		//    	
		return Response.status(200).entity(output).build();
    }
    
	@GET
	@Path("/{cpfCnpj}/cpfFormata")
	public Response getFormataCpfCnpj(@PathParam("cpfCnpj") String cpfCnpjF) {
    	//
		String cpfCnpjX = cpfCnpjF.replace(".", "");
		
		cpfCnpjX = cpfCnpjX.replace(".", "");
		cpfCnpjX = cpfCnpjX.replace(".", "");
		cpfCnpjX = cpfCnpjX.replace("-", "");
		cpfCnpjX = cpfCnpjX.replace("/", "");
		//
		return Response.status(200).entity(cpfCnpjX).build();
    }

    @GET
	@Path("/{file}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileWithGet(@PathParam("file") String file) {
    	//
    	try {
    		//
            String pathX = this.servletContext.getRealPath(File.separator) 
            										+ File.separator + "resources" + File.separator + "pdfs";
            File fileDownload = new File(pathX + File.separator + file);
            
            ResponseBuilder response = Response.ok((Object) fileDownload);
            response.header("Content-Disposition", "attachment;filename=" + file);
            //
            return response.build();
            //
    	} catch(Exception e) {
    		//
			String output = "Exception ! ( e = " + e;
    		return Response.status(500).entity(output).build();
    	}
    }	

    @GET
	@Path("/{msg}/telegram")
    public Response telegramGet(@PathParam("msg") String msg) {
    	//
    	try {
    		//
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            //Add Telegram token (given Token is fake)
            String apiToken = "841081359:AAF4nu1N3Y63zk8lW0GmpjhAwjUSIzE4_Jw";
          
            //Add chatId (given chatId is fake)
            String chatId = "@mpxdsbot";

            urlString = String.format(urlString, apiToken, chatId, msg);

            try {
            	//
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                
                InputStream is = new BufferedInputStream(conn.getInputStream());
                //
            } catch (IOException e) {
            	//
                e.printStackTrace();
            }           
            //
			String output = "Mensagem enviada ( " + msg;
    		return Response.status(200).entity(output).build();
            //
    	} catch(Exception e) {
    		//
			String output = "Exception ! ( e = " + e;
    		return Response.status(500).entity(output).build();
    	}
    }	
    
    @GET
	@Path("/{path}/realpath")
    public Response pathGet(@PathParam("path") String pathX) {
    	//
    	String actualPath = this.servletContext.getRealPath("/");    
    	if (null == actualPath) actualPath = "null";
    	
		String output = "Path user.home = " + System.getProperty("user.home") + " / Path ( " + pathX + " / " +
				actualPath ;
		//
		return Response.status(200).entity(output).build();
	}  
	
}