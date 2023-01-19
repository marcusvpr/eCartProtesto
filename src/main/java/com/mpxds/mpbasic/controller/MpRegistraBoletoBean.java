package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.cert.Certificate;
import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistro;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistroRetorno;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

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

//@SuppressWarnings("deprecation")
@Named
@ViewScoped
public class MpRegistraBoletoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpBoletos mpBoletos;
	//
	private static final String PATH_TO_KEYSTORE_1OF = "certificado1of.pfx";
	private static final String PATH_TO_KEYSTORE_2OF = "certificado2of.pfx";
	private static final String PATH_TO_KEYSTORE_3OF = "certificado3of.pfx";
	private static final String PATH_TO_KEYSTORE_4OF = "certificado4ofx.pfx";
	
	private static final String KEYSTORE_PASSWORD = "mpxdsa1";
	
	private static final String SIGNATUREALGO = "Sha256WithRSA";
	private static final String URI_REGISTRO = 
			"https://cobranca.bradesconetempresa.b.br/ibpjregistrotitulows/registrotitulohomologacao";

	private String numeroIntimacao;
    private Date dataIntimacao;

	@PostConstruct
	public void postConstruct() {
		//
		System.out.println("MpRegistraBoletoBean.postConstruct()");
	}

	public void registrarBoleto() throws Exception {
		//
		System.out.println("MpRegistraBoletoBean.registrarBoleto() - 000");
		
//		this.xxxxxxx();
//
//		System.out.println("MpRegistraBoletoBean.registrarBoleto() - 001");
		
		// Convert Objeto em JSON ! 
		// ========================
		ObjectMapper objectMapper = new ObjectMapper();
		
		//convert Object to json string
//		MpBoletoRegistro mpBoletoRegistro = new MpBoletoRegistro(
//				"123456789","0001","39","2","0","0","0","09","123400000001234567","237","0","1","0","0",
//				"123456","25.05.2017","20.06.2017","0","100","04","0","0","","","0","0","0","0","0","0",
//				"0","0","0","","0","0","","0","0","","0","0","0","","0","0","Cliente Teste","rua Teste","90",
//				"","12345","500","bairro Teste","Teste","SP","1","12345648901234","","","","0","","0","0",
//				"","","","0","0","");
		List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByNumeroProtocoloList("1", this.numeroIntimacao);
		if (mpBoletoList.isEmpty()) {
			MpFacesUtil.addErrorMessage("Não EXiste BOLETO !");
			return;
		}
		MpBoletoRegistro mpBoletoRegistro = null;
		for (MpBoleto mpBoleto : mpBoletoList) {
			if (this.getDataIntimacao().equals(mpBoleto.getDataDocumento())) {
				//
				mpBoletoRegistro = setaMpBoletoRegistro(mpBoleto);
				break;
			}
		}
		if (null ==  mpBoletoRegistro) {
			MpFacesUtil.addErrorMessage("Não EXiste BOLETO (NULL) !");
			return;
		}
		
		//configure Object mapper for pretty print
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		//writing to console, can write to any output stream such as file
		StringWriter stringBoleto = new StringWriter();
		objectMapper.writeValue(stringBoleto, mpBoletoRegistro);
		
		System.out.println("MpRegistraBoletoBean.registrarBoleto() (stringBoleto = " + stringBoleto);		
		
		// ========================

		// PKCS7Signer signer = new PKCS7Signer();
		KeyStore keyStore = this.loadKeyStore();
		CMSSignedDataGenerator signatureGenerator = this.setUpProvider(keyStore);
		
		// Json de teste...
//		String json = "{\"nuCPFCNPJ\": \"123456789\",\"filialCPFCNPJ\": \"0001\",\"ctrlCPFCNPJ\": \"39\",\"cdTipoAcesso\": \"2\",\"clubBanco\": \"0\",\"cdTipoContrato\": \"0\",\"nuSequenciaContrato\": \"0\",\"idProduto\": \"09\",\"nuNegociacao\": \"123400000001234567\",\"cdBanco\": \"237\",\"eNuSequenciaContrato\": \"0\",\"tpRegistro\": \"1\",\"cdProduto\": \"0\",\"nuTitulo\": \"0\",\"nuCliente\": \"123456\",\"dtEmissaoTitulo\": \"25.05.2017\",\"dtVencimentoTitulo\": \"20.06.2017\",\"tpVencimento\": \"0\",\"vlNominalTitulo\": \"100\",\"cdEspecieTitulo\": \"04\",\"tpProtestoAutomaticoNegativacao\": \"0\",\"prazoProtestoAutomaticoNegativacao\": \"0\",\"controleParticipante\": \"\",\"cdPagamentoParcial\": \"\",\"qtdePagamentoParcial\": \"0\",\"percentualJuros\": \"0\",\"vlJuros\": \"0\",\"qtdeDiasJuros\": \"0\",\"percentualMulta\": \"0\",\"vlMulta\": \"0\",\"qtdeDiasMulta\": \"0\",\"percentualDesconto1\": \"0\",\"vlDesconto1\": \"0\",\"dataLimiteDesconto1\": \"\",\"percentualDesconto2\": \"0\",\"vlDesconto2\": \"0\",\"dataLimiteDesconto2\": \"\",\"percentualDesconto3\": \"0\",\"vlDesconto3\": \"0\",\"dataLimiteDesconto3\": \"\",\"prazoBonificacao\": \"0\",\"percentualBonificacao\": \"0\",\"vlBonificacao\": \"0\",\"dtLimiteBonificacao\": \"\",\"vlAbatimento\": \"0\",\"vlIOF\": \"0\",\"nomePagador\": \"Cliente Teste\",\"logradouroPagador\": \"rua Teste\",\"nuLogradouroPagador\": \"90\",\"complementoLogradouroPagador\": \"\",\"cepPagador\": \"12345\",\"complementoCepPagador\": \"500\",\"bairroPagador\": \"bairro Teste\",\"municipioPagador\": \"Teste\",\"ufPagador\": \"SP\",\"cdIndCpfcnpjPagador\": \"1\",\"nuCpfcnpjPagador\": \"12345648901234\",\"endEletronicoPagador\": \"\",\"nomeSacadorAvalista\": \"\",\"logradouroSacadorAvalista\": \"\",\"nuLogradouroSacadorAvalista\": \"0\",\"complementoLogradouroSacadorAvalista\": \"\",\"cepSacadorAvalista\": \"0\",\"complementoCepSacadorAvalista\": \"0\",\"bairroSacadorAvalista\": \"\",\"municipioSacadorAvalista\": \"\",\"ufSacadorAvalista\": \"\",\"cdIndCpfcnpjSacadorAvalista\": \"0\",\"nuCpfcnpjSacadorAvalista\": \"0\",\"endEletronicoSacadorAvalista\": \"\"}";
		String json = stringBoleto.toString();

		byte[] signedBytes = this.signPkcs7(json.getBytes("UTF-8"), signatureGenerator);
//		System.out.println("Signed Encoded Bytes: " + new String(Base64.encode(signedBytes)));

		HttpEntity entity = new StringEntity(new String(Base64.encode(signedBytes)), Charset.forName("UTF-8"));
//		System.out.println("XXX004 = " + entity);
		
		HttpPost post = new HttpPost(URI_REGISTRO);
//		System.out.println("XXX005 = " + post);
		
		post.setEntity(entity);
//		System.out.println("XXX006 = POST");

		HttpClientBuilder builder = HttpClientBuilder.create();
//		System.out.println("XXX007 = " + builder);
		
		// ??? Erro PKIX path building failed -> keytool -insert ... 2 certificados vindo do BRADESCO ...
		// Baixei e usei o utilitário ... KeyStore Explorer 5.3.2 !
		HttpResponse response = builder.build().execute(post); 
//		System.out.println("XXX008 = " + response.getStatusLine());
		
		String boletoRetorno = EntityUtils.toString(response.getEntity(), "UTF-8");		
		System.out.println("XXX009 = " + boletoRetorno);
		
		Integer posI = boletoRetorno.indexOf("{");
		Integer posF = boletoRetorno.indexOf("}");
		System.out.println("XXX009-1 = " + posI + " / " + posF + " / " + boletoRetorno.length());
		
		String boletoRetornoJson = boletoRetorno.substring(posI, posF + 1);
		System.out.println("XXX0010 = " + boletoRetornoJson);
				
		// Convert JSON Retorno para Objeto ! 
		// ========================
		ObjectMapper objectMapperRet = new ObjectMapper();
		//JSON from String to Object
		MpBoletoRegistroRetorno mpBoletoRegistroRetorno = objectMapperRet.readValue(boletoRetornoJson,
																			MpBoletoRegistroRetorno.class);
		System.out.println("XXX011 / cdErro = " + mpBoletoRegistroRetorno.getCdErro() + 
													"/ msg.erro = " + mpBoletoRegistroRetorno.getMsgErro());
	}

	private KeyStore loadKeyStore() throws Exception {
		//
		KeyStore keystore = KeyStore.getInstance("JKS");

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		String numOf = "4";
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
		
		return keystore;
	}

	private CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception {
		//
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

		Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(aliaz);

		final List<Certificate> certlist = new ArrayList<Certificate>();

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
		return generator;
	}

	private byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {
		//
		CMSTypedData cmsdata = new CMSProcessableByteArray(content);
		CMSSignedData signeddata = generator.generate(cmsdata, true);
		//
		return signeddata.getEncoded();
	}
	
	private MpBoletoRegistro setaMpBoletoRegistro(MpBoleto mpBoleto) {
		//
		MpBoletoRegistro mpBoletoRegistro = new MpBoletoRegistro();
		//
		mpBoletoRegistro.setNuCPFCNPJ("27586775");
		mpBoletoRegistro.setFilialCPFCNPJ("0001");
		mpBoletoRegistro.setCtrlCPFCNPJ("47");
		mpBoletoRegistro.setCdTipoAcesso("02");
		mpBoletoRegistro.setClubBanco("2269651");
		mpBoletoRegistro.setCdTipoContrato("48");
		mpBoletoRegistro.setNuSequenciaContrato("0");
		mpBoletoRegistro.setIdProduto(mpBoleto.getCarteira()); // "09"
		
		// Número da Negociação Formato: Agencia: 4 posições (Sem digito)
		// Zeros: 7 posições Conta: 7 posições (Sem digito)
		String agencia = "0000";
		String zeros7 = "0000000";
		String conta7 = "0000000";
		mpBoletoRegistro.setNuNegociacao(agencia + zeros7 + conta7); 

		mpBoletoRegistro.setCdBanco("237");
		
//		mpBoletoRegistro.setENuSequenciaContrato(mpBoleto.geteNuSequenciaContrato());
//		mpBoletoRegistro.setTpRegistro(mpBoleto.gettpRegistro());
//		mpBoletoRegistro.setCdProduto(mpBoleto.getcdProduto());
//		mpBoletoRegistro.setNuTitulo(mpBoleto.getnuTitulo());
//		mpBoletoRegistro.setNuCliente(mpBoleto.getnuCliente());
//		mpBoletoRegistro.setDtEmissaoTitulo(mpBoleto.getdtEmissaoTitulo());
//		mpBoletoRegistro.setDtVencimentoTitulo(mpBoleto.getdtVencimentoTitulo());
//		mpBoletoRegistro.setTpVencimento(mpBoleto.gettpVencimento());
//		mpBoletoRegistro.setVlNominalTitulo(mpBoleto.getvlNominalTitulo());
//		mpBoletoRegistro.setCdEspecieTitulo(mpBoleto.getcdEspecieTitulo());
//		mpBoletoRegistro.setTpProtestoAutomaticoNegativacao(mpBoleto.gettpProtestoAutomaticoNegativacao());
//		mpBoletoRegistro.setPrazoProtestoAutomaticoNegativacao(mpBoleto.getprazoProtestoAutomaticoNegativacao());
//		mpBoletoRegistro.setControleParticipante(mpBoleto.getcontroleParticipante());
//		mpBoletoRegistro.setCdPagamentoParcial(mpBoleto.getcdPagamentoParcial());
//		mpBoletoRegistro.setQtdePagamentoParcial(mpBoleto.getqtdePagamentoParcial());
//		mpBoletoRegistro.setPercentualJuros(mpBoleto.getpercentualJuros());
//		mpBoletoRegistro.setVlJuros(mpBoleto.getvlJuros());
//		mpBoletoRegistro.setQtdeDiasJuros(mpBoleto.getqtdeDiasJuros());
//		mpBoletoRegistro.setPercentualMulta(mpBoleto.getpercentualMulta());
//		mpBoletoRegistro.setVlMulta(mpBoleto.getvlMulta());
//		mpBoletoRegistro.setQtdeDiasMulta(mpBoleto.getqtdeDiasMulta());
//		mpBoletoRegistro.setPercentualDesconto1(mpBoleto.getpercentualDesconto1());
//		mpBoletoRegistro.setVlDesconto1(mpBoleto.getvlDesconto1());
//		mpBoletoRegistro.setDataLimiteDesconto1(mpBoleto.getdataLimiteDesconto1());
//		mpBoletoRegistro.setPercentualDesconto2(mpBoleto.getpercentualDesconto2());
//		mpBoletoRegistro.setVlDesconto2(mpBoleto.getvlDesconto2());
//		mpBoletoRegistro.setDataLimiteDesconto2(mpBoleto.getdataLimiteDesconto2());
//		mpBoletoRegistro.setPercentualDesconto3(mpBoleto.getpercentualDesconto3());
//		mpBoletoRegistro.setVlDesconto3(mpBoleto.getvlDesconto3());
//		mpBoletoRegistro.setDataLimiteDesconto3(mpBoleto.getdataLimiteDesconto3());
//		mpBoletoRegistro.setPrazoBonificacao(mpBoleto.getprazoBonificacao());
//		mpBoletoRegistro.setPercentualBonificacao(mpBoleto.getpercentualBonificacao());
//		mpBoletoRegistro.setVlBonificacao(mpBoleto.getvlBonificacao());
//		mpBoletoRegistro.setDtLimiteBonificacao(mpBoleto.getdtLimiteBonificacao());
//		mpBoletoRegistro.setVlAbatimento(mpBoleto.getvlAbatimento());
//		mpBoletoRegistro.setVlIOF(mpBoleto.getvlIOF());
//		mpBoletoRegistro.setNomePagador(mpBoleto.getnomePagador());
//		mpBoletoRegistro.setLogradouroPagador(mpBoleto.getlogradouroPagador());
//		mpBoletoRegistro.setNuLogradouroPagador(mpBoleto.getnuLogradouroPagador());
//		mpBoletoRegistro.setComplementoLogradouroPagador(mpBoleto.getcomplementoLogradouroPagador());
//		mpBoletoRegistro.setCepPagador(mpBoleto.getcepPagador());
//		mpBoletoRegistro.setComplementoCepPagador(mpBoleto.getcomplementoCepPagador());
//		mpBoletoRegistro.setBairroPagador(mpBoleto.getbairroPagador());
//		mpBoletoRegistro.setMunicipioPagador(mpBoleto.getmunicipioPagador());
//		mpBoletoRegistro.setUfPagador(mpBoleto.getufPagador());
//		mpBoletoRegistro.setCdIndCpfcnpjPagador(mpBoleto.getcdIndCpfcnpjPagador());
//		mpBoletoRegistro.setNuCpfcnpjPagador(mpBoleto.getnuCpfcnpjPagador());
//		mpBoletoRegistro.setEndEletronicoPagador(mpBoleto.getendEletronicoPagador());
//		mpBoletoRegistro.setNomeSacadorAvalista(mpBoleto.getnomeSacadorAvalista());
//		mpBoletoRegistro.setLogradouroSacadorAvalista(mpBoleto.getlogradouroSacadorAvalista());
//		mpBoletoRegistro.setNuLogradouroSacadorAvalista(mpBoleto.getnuLogradouroSacadorAvalista());
//		mpBoletoRegistro.setComplementoLogradouroSacadorAvalista(mpBoleto.getcomplementoLogradouroSacadorAvalista());
//		mpBoletoRegistro.setCepSacadorAvalista(mpBoleto.getcepSacadorAvalista());
//		mpBoletoRegistro.setComplementoCepSacadorAvalista(mpBoleto.getcomplementoCepSacadorAvalista());
//		mpBoletoRegistro.setBairroSacadorAvalista(mpBoleto.getbairroSacadorAvalista());
//		mpBoletoRegistro.setMunicipioSacadorAvalista(mpBoleto.getmunicipioSacadorAvalista());
//		mpBoletoRegistro.setUfSacadorAvalista(mpBoleto.getufSacadorAvalista());
//		mpBoletoRegistro.setCdIndCpfcnpjSacadorAvalista(mpBoleto.getcdIndCpfcnpjSacadorAvalista());
//		mpBoletoRegistro.setNuCpfcnpjSacadorAvalista(mpBoleto.getnuCpfcnpjSacadorAvalista());
//		mpBoletoRegistro.setEndEletronicoSacadorAvalista(mpBoleto.getendEletronicoSacadorAvalista());
		//
		return mpBoletoRegistro;
	}

//	private void xxxxxxx() {
//		//
//		InputStream is = null;
//		try {
//			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//
//	        File file = new File(extContext.getRealPath(File.separator + "resources" + 
//					File.separator + "opt" + File.separator + PATH_TO_KEYSTORE_1OF));
//	        
//	        is = new FileInputStream(file);
//	        
//	        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//	        String password = "mpxdsa1";
//	        
//	        keystore.load(is, password.toCharArray());
//
//
//	        Enumeration enumeration = keystore.aliases();
//	        
//	        while(enumeration.hasMoreElements()) {
//	            String alias = (String)enumeration.nextElement();
//	            System.out.println("alias name: " + alias);
//	            Certificate certificate = keystore.getCertificate(alias);
//	            System.out.println(certificate.toString());
//
//	        }
//
//	    } catch (java.security.cert.CertificateException e) {
//	        e.printStackTrace();
//	    } catch (NoSuchAlgorithmException e) {
//	        e.printStackTrace();
//	    } catch (FileNotFoundException e) {
//	        e.printStackTrace();
//	    } catch (KeyStoreException e) {
//	        e.printStackTrace();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }finally {
//	        if(null != is)
//	            try {
//	                is.close();
//	            } catch (IOException e) {
//	                // TODO Auto-generated catch block
//	                e.printStackTrace();
//	            }
//	    }		
//	}
	
    
	public String getNumeroIntimacao() { return numeroIntimacao; }
	public void setNumeroIntimacao(String numeroIntimacao) { this.numeroIntimacao = numeroIntimacao; }
    
	public Date getDataIntimacao() { return dataIntimacao; }
	public void setDataIntimacao(Date dataIntimacao) { this.dataIntimacao = dataIntimacao; }
	
}