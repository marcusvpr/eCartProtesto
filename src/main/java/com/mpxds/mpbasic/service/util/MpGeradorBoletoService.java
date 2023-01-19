package com.mpxds.mpbasic.service.util;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.swing.ImageIcon;

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
import org.springframework.stereotype.Service;

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
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.enums.MpEspecieTituloBradesco;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.model.intima21.MpBoletoIntimacao;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistro;
import com.mpxds.mpbasic.rest.model.MpBoletoRegistroRetorno;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpBoletoLogService;
import com.mpxds.mpbasic.service.MpSistemaConfigService;
import com.mpxds.mpbasic.util.MpAppUtil;

@Service
public class MpGeradorBoletoService implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	@Inject
	private MpSistemaConfigService mpSistemaConfigService;
	
	@Inject
	private MpBoletos mpBoletos;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpBoletoLogService mpBoletoLogService;
		
	@Inject
	private MpUtilService mpUtilService;
	
	// ---
	
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
	
	//
		
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDMYp = new SimpleDateFormat("dd.MM.yyyy");
	private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");

	private Integer numeroGuiaGerado;
//	private Integer numeroGuiaGeradoAnt;

	private String codigoErroRegistro;
	private String mensagemLog;
	
    private String msgErro = "";
    private String boletoRegistro = "";
    private String boletoRegistroRetorno = "";
	
	// ---
	
	public BoletoViewer boletoCriar( Object objeto, MpCartorioOficio mpCartorioOficioSel, Boolean indApos16h,
										Boolean indRegistroX, String ambienteBradesco) {
		//
		MpBoleto mpBoletoSelX = null;
		MpBoletoIntimacao mpBoletoIntimacaoSelX = null;
		
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			mpBoletoIntimacaoSelX = (MpBoletoIntimacao) objeto;
			//
		} else {
			//
			mpBoletoSelX = (MpBoleto) objeto;
		}
		//
//		System.out.println("MpGeradorBoletoService() - 000 .................... ( IndRegistro = " + indRegistroX);

		String nomeCedente = "";
		Integer agenciaCodigoCedente = 0;
		Integer agenciaContaCedente = 0;

		// if (null == this.mpCartorioOficioSel) {
		// //
		// nomeCedente = this.mpCartorioComarcaSel.getNomeCedente();
		// agenciaCodigoCedente = this.mpCartorioComarcaSel.getAgenciaCodigoCedente();
		// agenciaContaCedente = this.mpCartorioComarcaSel.getAgenciaContaCedente();
		// } else {
		//
		nomeCedente = mpCartorioOficioSel.getNomeCedente();
		agenciaCodigoCedente = mpCartorioOficioSel.getAgenciaCodigoCedente();
		agenciaContaCedente = mpCartorioOficioSel.getAgenciaContaCedente();
		// }

		// Cedente
		Cedente cedente;
		// if (null == this.mpCartorioOficioSel)
		// cedente = new Cedente(nomeCedente, this.mpCartorioComarcaSel.getCnpj());
		// else
		cedente = new Cedente(nomeCedente, mpCartorioOficioSel.getCnpj());
		// Sacado
		// Endereço do sacado
		Endereco endereco = new Endereco();

		Sacado sacado = null;
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			sacado = new Sacado(mpBoletoIntimacaoSelX.getNomeSacado());
			endereco.setBairro(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getBairro());
			//
		} else {
			//
			sacado = new Sacado(mpBoletoSelX.getNomeSacado());
			endereco.setBairro(mpBoletoSelX.getMpEnderecoLocal().getBairro());
		}
		
		// endereco.setUF(UnidadeFederativa.MG);//
		// this.mpBoletoSel.getMpEnderecoLocal().getMpEstadoUF().getCodigo() ;
		// endereco.setLocalidade(this.mpBoletoSel.getMpEnderecoLocal().getCidade());
		// endereco.setCep(new CEP(this.mpBoletoSel.getMpEnderecoLocal().getCep()));
		// endereco.setLogradouro(this.mpBoletoSel.getMpEnderecoLocal().getLogradouro());
		// endereco.setNumero(this.mpBoletoSel.getMpEnderecoLocal().getNumero() + " / "
		// +
		// this.mpBoletoSel.getMpEnderecoLocal().getComplemento());

		sacado.addEndereco(endereco);

		// Criando o título
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());

		// if (null == this.mpCartorioOficioSel) {
		// //
		// contaBancaria.setAgencia(new Agencia(agenciaCodigoCedente,
		// this.mpCartorioComarcaSel.getAgenciaCodigoCedenteDig()));
		// contaBancaria.setNumeroDaConta(new NumeroDaConta(agenciaContaCedente,
		// this.mpCartorioComarcaSel.getAgenciaContaCedenteDig()));
		// contaBancaria.setCarteira(new
		// Carteira(this.mpCartorioComarcaSel.getCarteira()));
		// } else {
		//
		contaBancaria.setAgencia(new Agencia(agenciaCodigoCedente, mpCartorioOficioSel.
																			getAgenciaCodigoCedenteDig()));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(agenciaContaCedente, mpCartorioOficioSel.
																			getAgenciaContaCedenteDig()));
		contaBancaria.setCarteira(new Carteira(mpCartorioOficioSel.getCarteira()));
		// }

		//
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		
		String nossoNumero = "";
		
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			titulo.setNumeroDoDocumento(mpBoletoIntimacaoSelX.getNumeroDocumento());
			//
			nossoNumero = mpBoletoIntimacaoSelX.getNossoNumero().replaceAll("/", "");
		} else {
			//
			titulo.setNumeroDoDocumento(mpBoletoSelX.getNumeroDocumento());
			//
			nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		}

		nossoNumero = nossoNumero.replaceAll("-", "");
		if (nossoNumero.isEmpty())
			nossoNumero = "00000000000"; // ???????

		if (nossoNumero.length() < 12)
			titulo.setNossoNumero(nossoNumero);
		else
			titulo.setNossoNumero(nossoNumero.substring(0, 11)); // Numérico e Tam.max=11?
		//
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			titulo.setDigitoDoNossoNumero(mpBoletoIntimacaoSelX.getNossoNumeroDig());
			// mpGeradorDigitoVerificador.gerarDigito(contaBancaria.getCarteira().getCodigo(),
			// titulo.getNossoNumero()));
			//
			titulo.setValor(mpBoletoIntimacaoSelX.getValorDocumento());
			titulo.setValorCobrado(mpBoletoIntimacaoSelX.getValorCobrado());
			//
			titulo.setAcrecimo(mpBoletoIntimacaoSelX.getValorAcrescimo());
			//
			titulo.setDesconto(mpBoletoIntimacaoSelX.getValorTarifa());
			titulo.setDeducao(mpBoletoIntimacaoSelX.getValorCPMF());
			titulo.setMora(mpBoletoIntimacaoSelX.getValorLeis());
			// titulo.setAcrecimo(mpBoletoSelX.getValorIss());

			// ParametrosBancariosMap parametrosBancariosMap = new ParametrosBancariosMap();

			titulo.setDataDoDocumento(mpBoletoIntimacaoSelX.getDataDocumento());

//			// Trata dados após 16h00m ...
//			// ---------------------------
//			if (indApos16h)
				titulo.setDataDoVencimento(mpBoletoIntimacaoSelX.getDataVencimento1());
//			else
//				titulo.setDataDoVencimento(mpBoletoSelX.getDataVencimento());
			// ---------------------------
			//
		} else {
			//
			titulo.setDigitoDoNossoNumero(mpBoletoSelX.getNossoNumeroDig());
			// mpGeradorDigitoVerificador.gerarDigito(contaBancaria.getCarteira().getCodigo(),
			// titulo.getNossoNumero()));
			//
			titulo.setValor(mpBoletoSelX.getValorDocumento());
			titulo.setValorCobrado(mpBoletoSelX.getValorCobrado());
			//
			titulo.setAcrecimo(mpBoletoSelX.getValorAcrescimo());
			//
			titulo.setDesconto(mpBoletoSelX.getValorTarifa());
			titulo.setDeducao(mpBoletoSelX.getValorCPMF());
			titulo.setMora(mpBoletoSelX.getValorLeis());
			// titulo.setAcrecimo(mpBoletoSelX.getValorIss());

			// ParametrosBancariosMap parametrosBancariosMap = new ParametrosBancariosMap();

			titulo.setDataDoDocumento(mpBoletoSelX.getDataDocumento());

			// Trata dados após 16h00m ...
			// ---------------------------
			if (indApos16h)
				titulo.setDataDoVencimento(mpBoletoSelX.getDataVencimento1());
			else
				titulo.setDataDoVencimento(mpBoletoSelX.getDataVencimento());
			// ---------------------------
		}

		titulo.setTipoDeDocumento(TipoDeTitulo.DSI_DUPLICATA_DE_SERVICO_PARA_INDICACAO);
		titulo.setAceite(Aceite.N);

		// Dados do boleto (Defaut) ...
		Boleto boleto = new Boleto(titulo);

		/*
		 * A área do boleto destinada ao "Nosso Número" deverá exibir a informação
		 * "Código da Carteira/Nosso Número". Ex: 109/1234567-8.
		 */
		// String nossoNumeroParaExibicao = String.format("%d/%s-%s",
		// titulo.getContaBancaria().getCarteira().getCodigo(),
		// mpBoletoSelX.getNossoNumero(),
		// titulo.getDigitoDoNossoNumero());
		// boleto.addTextosExtras("txtFcNossoNumero", nossoNumeroParaExibicao);
		// boleto.addTextosExtras("txtRsNossoNumero", nossoNumeroParaExibicao);

		
		boleto.addTextosExtras("txtFcEspecie", "R$");
		boleto.addTextosExtras("txtRsEspecie", "R$");
		
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			boleto.addTextosExtras("txtFcNossoNumero", mpBoletoIntimacaoSelX.getNossoNumero());
			boleto.addTextosExtras("txtRsNossoNumero", mpBoletoIntimacaoSelX.getNossoNumero());
			boleto.addTextosExtras("txtFcNumeroDocumento", mpBoletoIntimacaoSelX.getNumeroDocumento());
			boleto.addTextosExtras("txtRsNumeroDocumento", mpBoletoIntimacaoSelX.getNumeroDocumento());

			// MVPR-21062019 NÂO SAI VALOR COBRADO 20.OFicio -> BrunoReportou! ...
			String valCobradoX = (mpBoletoIntimacaoSelX.getValorCobrado() + "").replace(".", ",");
			// System.out.println("MpBoletoBean.000 ................................... (
			// valCobradoX = " + valCobradoX);
			boleto.addTextosExtras("txtRsValorCobrado", valCobradoX);
			//
			boleto.addTextosExtras("txtFcEspecieDocumento", mpBoletoIntimacaoSelX.getEspecieDocumento());

//			// Trata dados após 16h00m ...
//			// ---------------------------
//			if (indApos16h) {
//				boleto.addTextosExtras("txtFcLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel1());
//				boleto.addTextosExtras("txtRsLinhaDigitavel", mpBoletoSelX.getLinhaDigitavel1());
//				boleto.addTextosExtras("txtFcAgenciaCodigoCedente", mpBoletoSelX.getAgenciaCodigoCedente1());
//				boleto.addTextosExtras("txtRsAgenciaCodigoCedente", mpBoletoSelX.getAgenciaCodigoCedente1());
//				//
//				Image img1 = CodigoDeBarras.valueOf(mpBoletoSelX.getCodigoBarras()).toImage();
//				boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img1).getImage());
//			} else {
				boleto.addTextosExtras("txtFcLinhaDigitavel", mpBoletoIntimacaoSelX.getLinhaDigitavel());
				boleto.addTextosExtras("txtRsLinhaDigitavel", mpBoletoIntimacaoSelX.getLinhaDigitavel());
				boleto.addTextosExtras("txtFcAgenciaCodigoCedente", mpBoletoIntimacaoSelX.
																				getAgenciaCodigoCedente());
				//
				Image img = CodigoDeBarras.valueOf(mpBoletoIntimacaoSelX.getCodigoBarras()).toImage();
				boleto.addImagensExtras("txtFcCodigoBarra", new ImageIcon(img).getImage());
//			}
			//
			boleto.addTextosExtras("txtFcSacadoL3", mpBoletoIntimacaoSelX.getCpfCnpj());
			//
		} else {
			//
			boleto.addTextosExtras("txtFcNossoNumero", mpBoletoSelX.getNossoNumero());
			boleto.addTextosExtras("txtRsNossoNumero", mpBoletoSelX.getNossoNumero());
			boleto.addTextosExtras("txtFcNumeroDocumento", mpBoletoSelX.getNumeroDocumento());
			boleto.addTextosExtras("txtRsNumeroDocumento", mpBoletoSelX.getNumeroDocumento());

			// MVPR-21062019 NÂO SAI VALOR COBRADO 20.OFicio -> BrunoReportou! ...
			String valCobradoX = (mpBoletoSelX.getValorCobrado() + "").replace(".", ",");
			// System.out.println("MpBoletoBean.000 ................................... (
			// valCobradoX = " + valCobradoX);
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
			//
			boleto.addTextosExtras("txtFcSacadoL3", mpBoletoSelX.getCpfCnpj());
		}

		// ---

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
		if (null != mpSistemaConfig)
			boleto.setLocalPagamento(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucaoAoSacado");
		if (null != mpSistemaConfig)
			boleto.setInstrucaoAoSacado(mpSistemaConfig.getDescricao());

		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao1");
		if (null != mpSistemaConfig)
			boleto.setInstrucao1(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao2");
		if (null != mpSistemaConfig)
			boleto.setInstrucao2(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao3");
		if (null != mpSistemaConfig)
			boleto.setInstrucao3(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao4");
		if (null != mpSistemaConfig)
			boleto.setInstrucao4(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao5");
		if (null != mpSistemaConfig)
			boleto.setInstrucao5(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao6");
		if (null != mpSistemaConfig)
			boleto.setInstrucao6(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao7");
		if (null != mpSistemaConfig)
			boleto.setInstrucao7(mpSistemaConfig.getDescricao());
		mpSistemaConfig = mpSistemaConfigs.porParametro("BoletoInstrucao8");
		if (null != mpSistemaConfig)
			boleto.setInstrucao8(mpSistemaConfig.getDescricao());

		// =================================================

		if (objeto.getClass() == MpBoletoIntimacao.class) {
			// Trata hora impressão boleto !
			String horaHMS = sdfHMS.format(new Date());

			// Trata registro Boleto (WebServices) ... BRADESCO !
			if (indRegistroX) { // Vide SistConfig !
				if (null == mpBoletoIntimacaoSelX.getDataHoraRegistro())
					this.boletoRegistrar(mpBoletoIntimacaoSelX, mpCartorioOficioSel, indApos16h);
				//
			} else {
				// Sem REGISTRO no BRADESCO gera número da guia = 0 !
				mpBoletoIntimacaoSelX.setNumeroGuiaGerado(0);
				this.numeroGuiaGerado = 0;
			}
			//
			if (null == this.numeroGuiaGerado)
				this.numeroGuiaGerado = mpBoletoIntimacaoSelX.getNumeroGuiaGerado();
			//
			if (null != mpBoletoIntimacaoSelX.getBoletoInstrucao8())
				if (ambienteBradesco.toUpperCase().equals("HOMOLOGACAO"))
					boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado
							+ "     HORA   " + horaHMS + "  ( Ambiente: HOMOLOCACAO )");
				else
					boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado 
							+ "     HORA   " + horaHMS);
			//
		} else {
			//
			// Trata hora impressão boleto !
			String horaHMS = sdfHMS.format(new Date());

			// Trata registro Boleto (WebServices) ... BRADESCO !
			if (indRegistroX) { // Vide SistConfig !
				if (null == mpBoletoSelX.getDataHoraRegistro())
					this.boletoRegistrar(mpBoletoSelX, mpCartorioOficioSel, indApos16h);
				else {
					// Trata registro novamente para boletos reimpressos após às 16:00:00.00 (16H)
					// ...
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
						if (mpBoletoSelX.getDataHoraRegistro().before(dataHora16H))
							this.boletoRegistrar(mpBoletoSelX, mpCartorioOficioSel, indApos16h);
					}
				}
			} else {
				// Sem REGISTRO no BRADESCO gera número da guia = 0 !
				mpBoletoSelX.setNumeroGuiaGerado(0);
				this.numeroGuiaGerado = 0;
			}
			//
			if (null == this.numeroGuiaGerado)
				this.numeroGuiaGerado = mpBoletoSelX.getNumeroGuiaGerado();
			//
			if (null != mpBoletoSelX.getBoletoInstrucao8())
				if (ambienteBradesco.toUpperCase().equals("HOMOLOGACAO"))
					boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado
							+ "     HORA   " + horaHMS + "  ( Ambiente: HOMOLOCACAO )");
				else
					boleto.setInstrucao8("                                GUIA Nº  " + this.numeroGuiaGerado 
							+ "     HORA   " + horaHMS);
		}
		
		// Usa template Gerado no Writer.OpenOffice .odt) ...
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		String pathX = extContext.getRealPath("//resources//pdfs//");

		File mpTemplate = new File(pathX + "MpBoletoIntimacaoTemplateSemSacadorAvalista_" + 
															mpCartorioOficioSel.getNumero().trim() + "of.pdf");

		BoletoViewer boletoViewer = new BoletoViewer(boleto, mpTemplate);
		//
		return boletoViewer;
	}

	private void boletoRegistrar(Object objeto, MpCartorioOficio mpCartorioOficioSel, Boolean indApos16h) {
		//
		MpBoleto mpBoletoSelX = null;
		MpBoletoIntimacao mpBoletoIntimacaoSelX = null;
		
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			mpBoletoIntimacaoSelX = (MpBoletoIntimacao) objeto;
			//
		} else {
			//
			mpBoletoSelX = (MpBoleto) objeto;
		}
		

		// Trata Sequência número GUIA do Boleto ...
		// =================================================
		this.numeroGuiaGerado = 0;
//		this.numeroGuiaGeradoAnt = 0;
		//
		this.codigoErroRegistro = "";
		//
		String ofCoNumX = "";
		// if (null == this.mpCartorioOficioSel)
		// ofCoNumX = "Co" + this.mpCartorioComarcaSel.getNumero();
		// else
		ofCoNumX = "Of" + mpCartorioOficioSel.getNumero();
		//
		MpSistemaConfig mpSistemaConfigX = this.mpSistemaConfigs.porParametro(ofCoNumX + "_BoletoNumeroGuia");
		if (null == mpSistemaConfigX) {
			this.mensagemLog = this.mensagemLog + "( " + "Error! Controle GUIA... não existe! Contactar o SUPORTE. = "
																		+ ofCoNumX + "_BoletoNumeroGuia" + " ) ";
			//
		} else {
			if (mpSistemaConfigX.getValorN() == 0) {
				//
				String[] wordsNumeroGuia = null;
				if (objeto.getClass() == MpBoletoIntimacao.class)
					wordsNumeroGuia = mpBoletoIntimacaoSelX.getBoletoInstrucao8().split(" ");
				else
					wordsNumeroGuia = mpBoletoSelX.getBoletoInstrucao8().split(" ");

				// MpAppUtil.PrintarLn(this.mpBoletoSelX.getBoletoInstrucao8() + " / " +
				// wordsNumeroGuia.length);
				// numeroGuia =
				// Integer.parseInt(this.mpBoletoSelX.getBoletoInstrucao8().substring(8,
				// 16).trim());

				try {
					this.numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);
//					this.numeroGuiaGeradoAnt = this.numeroGuiaGerado;

					if (objeto.getClass() == MpBoletoIntimacao.class) 
						assert(true); // nop
					else {
						//
						// Atualiza numeroGuia BD !!!
						mpSistemaConfigX.setValorN(this.numeroGuiaGerado);

						this.mpSistemaConfigService.salvar(mpSistemaConfigX);
						//
						// MpAppUtil.PrintarLn("Controle GUIA(0)... Atualizar SistemaConfig (
						// numeroGuiaGerado = " +
						// this.numeroGuiaGerado);
					}
					//
				} catch (Exception e) {
					//
					this.mensagemLog = this.mensagemLog + "( "
													+ "Error! Controle GUIA/Instrução8 ! Contactar SUPORTE = ";

					if (objeto.getClass() == MpBoletoIntimacao.class)
						this.mensagemLog = this.mensagemLog + mpBoletoIntimacaoSelX.getBoletoInstrucao8() + 
																							" / e = " + e + " )";
					else
						this.mensagemLog = this.mensagemLog + mpBoletoSelX.getBoletoInstrucao8() + 
																							" / e = " + e + " )";
				}
			} else {
				this.numeroGuiaGerado = mpSistemaConfigX.getValorN();
//				this.numeroGuiaGeradoAnt = this.numeroGuiaGerado;

				// ------------------------------------//
				// Trata Incremento Número da Guia ... //
				// ------------------------------------//
				if (objeto.getClass() == MpBoletoIntimacao.class) 
					this.numeroGuiaGerado = 0;
				else
					this.numeroGuiaGerado++;
				// ------------------------------------//
				
				// --------------------------------- //
				// Verificar numero Guia Duplicado?  //
				// --------------------------------- //
				String numOfCoX = "";
				// if (null == this.mpCartorioOficioSel)
				// numOfCoX = this.mpCartorioComarcaSel.getNumero();
				// else
				numOfCoX = mpCartorioOficioSel.getNumero();

				List<MpBoleto> mpBoletoGuias = mpBoletos.mpBoletoByNumeroGuiaGeradoList(numOfCoX,
																							 this.numeroGuiaGerado);
				if (mpBoletoGuias.size() > 0) {
					//
					if (objeto.getClass() == MpBoletoIntimacao.class) 
						this.numeroGuiaGerado = 0;
					else
						this.numeroGuiaGerado++;
				}
				//
				if (objeto.getClass() == MpBoletoIntimacao.class) 
					assert(true); // nop
				else {
					//
					// Atualiza numeroGuia BD !!!
					mpSistemaConfigX.setValorN(this.numeroGuiaGerado);

					this.mpSistemaConfigService.salvar(mpSistemaConfigX);
					//
					// MpAppUtil.PrintarLn("Controle GUIA(1)... Atualizar SistemaConfig (
					// numeroGuiaGerado = " +
					// this.numeroGuiaGerado);
				}
			}
		}

//		System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 000");

		// ================================================== //
		// Trata registro Boleto (WebServices) ... BRADESCO ! //
		// ================================================== //
		//
		MpBoletoRegistro mpBoletoRegistro = new MpBoletoRegistro("123456789", "0001", "39", "2", "0", "0", "0", "09",
			"123400000001234567", "237", "0", "1", "0", "0", "123456", "25.05.2017", "20.06.2017", "0", "100", "04",
			"0", "0", "", "", "0", "0", "0", "0", "0", "0", "0", "0", "0", "", "0", "0", "", "0", "0", "", "0", "0",
			"0", "", "0", "0", "Cliente?", "Rua?", "90", "", "12345", "500", "Bairro?", "Teste", "??", "1",
			"12345648901234", "", "", "", "0", "", "0", "0", "", "", "", "0", "0", "");
		//
		// if (null == mpCartorioOficioSel) {
		// //
		// mpBoletoRegistro.setNuCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(0,
		// 10).replace(".", ""));
		// mpBoletoRegistro.setFilialCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(11,
		// 15));
		// mpBoletoRegistro.setCtrlCPFCNPJ(mpCartorioComarcaSel.getCnpj().substring(16,
		// 18));
		// } else {
		//
		mpBoletoRegistro.setNuCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(0, 10).replace(".", ""));
		mpBoletoRegistro.setFilialCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(11, 15));
		mpBoletoRegistro.setCtrlCPFCNPJ(mpCartorioOficioSel.getCnpj().substring(16, 18));
		// }
		//
		mpBoletoRegistro.setCdTipoAcesso("2");
		mpBoletoRegistro.setClubBanco("2269651");
		mpBoletoRegistro.setCdTipoContrato("48");
		mpBoletoRegistro.setNuSequenciaContrato("0");

		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			mpBoletoRegistro.setIdProduto(mpBoletoIntimacaoSelX.getCarteira()); // "09"

			// Número da Negociação Formato: Agencia: 4 posições (Sem digito) ->
			// 0123456789012345
			// Zeros: 7 posições Conta: 7 posições (Sem digito) -> 3119-4/0002820-7
			// String agencia = String.format("%04d",
			// this.mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4));
			if (indApos16h) {
				mpBoletoRegistro.setNuNegociacao(mpBoletoIntimacaoSelX.getAgenciaCodigoCedente1().substring(0, 4)
					+ String.format("%07d", 0) + mpBoletoIntimacaoSelX.getAgenciaCodigoCedente1().substring(7, 14));
			} else {
				mpBoletoRegistro.setNuNegociacao(mpBoletoIntimacaoSelX.getAgenciaCodigoCedente().substring(0, 4)
					+ String.format("%07d", 0) + mpBoletoIntimacaoSelX.getAgenciaCodigoCedente().substring(7, 14));
			}
			//
		} else {
			//
			mpBoletoRegistro.setIdProduto(mpBoletoSelX.getCarteira()); // "09"

			// Número da Negociação Formato: Agencia: 4 posições (Sem digito) ->
			// 0123456789012345
			// Zeros: 7 posições Conta: 7 posições (Sem digito) -> 3119-4/0002820-7
			// String agencia = String.format("%04d",
			// this.mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4));
			if (indApos16h) {
				mpBoletoRegistro.setNuNegociacao(mpBoletoSelX.getAgenciaCodigoCedente1().substring(0, 4)
						+ String.format("%07d", 0) + mpBoletoSelX.getAgenciaCodigoCedente1().substring(7, 14));
			} else {
				mpBoletoRegistro.setNuNegociacao(mpBoletoSelX.getAgenciaCodigoCedente().substring(0, 4)
						+ String.format("%07d", 0) + mpBoletoSelX.getAgenciaCodigoCedente().substring(7, 14));
			}
		}
		
		//
		mpBoletoRegistro.setCdBanco("237");
		mpBoletoRegistro.seteNuSequenciaContrato("0");
		mpBoletoRegistro.setTpRegistro("1");
		mpBoletoRegistro.setCdProduto("0");

		// // MVPR(20180724)-AjusteErroCipProdução !
		// String nossoNumero = this.mpBoletoSelX.getNossoNumero().replaceAll("/", "");
		// nossoNumero = nossoNumero.replaceAll("-", "");
		// if (nossoNumero.isEmpty()) nossoNumero = ""; // ???????
		// if (nossoNumero.length() > 10)
		// nossoNumero = nossoNumero.substring(0, 11);
		// mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO ->
		// Error: 69 !


		if (objeto.getClass() == MpBoletoIntimacao.class) {
			// 01234567890123456
			// MVPR-20180807 09/02018041124-4 = 09020180411244
			String nossoNumero = mpBoletoIntimacaoSelX.getNossoNumero().replaceAll("/", "");
			nossoNumero = nossoNumero.replaceAll("-", "");
			if (nossoNumero.isEmpty())
				nossoNumero = ""; // ???????
			if (nossoNumero.length() > 13)
				nossoNumero = nossoNumero.substring(2, 13);
			mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !
			// MVPR-20180807

			//
			mpBoletoRegistro.setNuCliente(mpBoletoIntimacaoSelX.getNumeroDocumento().replace("/", ""));
			mpBoletoRegistro.setDtEmissaoTitulo(this.sdfDMYp.format(mpBoletoIntimacaoSelX.getDataDocumento()));

//			if (indApos16h)
//				mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(mpBoletoIntimacaoSelX.getDataVencimento1()));
//			else
				mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(mpBoletoIntimacaoSelX.getDataVencimento()));
			//
			mpBoletoRegistro.setTpVencimento("0");

			// MVPR(20180502)-Ajuste Linha Digitavel !
			// String valorX =
			// this.mpBoletoSelX.getValorDocumento().toString().replace(".","");
			String valorX = mpBoletoIntimacaoSelX.getValorCobrado().toString().replace(".", "");
			mpBoletoRegistro.setVlNominalTitulo(valorX);
			//
			try {
				MpEspecieTituloBradesco mpEspecieTituloBradesco = MpEspecieTituloBradesco
													.valueOf(mpBoletoIntimacaoSelX.getEspecieDocumento().trim());
				if (null == mpEspecieTituloBradesco) {
					//
					this.mensagemLog = this.mensagemLog
							+ "( Registro/Bradesco. Não existe Espécie Título (Null/Assumiu=99) / sigla = "
							+ mpBoletoIntimacaoSelX.getEspecieDocumento() + " ) ";
					//
					mpBoletoRegistro.setCdEspecieTitulo("99");
				} else
					mpBoletoRegistro.setCdEspecieTitulo(mpEspecieTituloBradesco.getSigla());
				//
			} catch (Exception e) {
				this.mensagemLog = this.mensagemLog
						+ "( Registro/Bradesco. Não existe Espécie Título (Exception/Assumiu=99) / sigla = "
						+ mpBoletoIntimacaoSelX.getEspecieDocumento() + " ) ";
				//
				mpBoletoRegistro.setCdEspecieTitulo("99");
			}
			//
		} else {
			//
			// 01234567890123456
			// MVPR-20180807 09/02018041124-4 = 09020180411244
			String nossoNumero = mpBoletoSelX.getNossoNumero().replaceAll("/", "");
			nossoNumero = nossoNumero.replaceAll("-", "");
			if (nossoNumero.isEmpty())
				nossoNumero = ""; // ???????
			if (nossoNumero.length() > 13)
				nossoNumero = nossoNumero.substring(2, 13);
			mpBoletoRegistro.setNuTitulo(nossoNumero); // 11dig. ("0") Deve ser ÚNICO -> Error: 69 !
			// MVPR-20180807

			//
			mpBoletoRegistro.setNuCliente(mpBoletoSelX.getNumeroDocumento().replace("/", ""));
			mpBoletoRegistro.setDtEmissaoTitulo(this.sdfDMYp.format(mpBoletoSelX.getDataDocumento()));

			if (indApos16h)
				mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(mpBoletoSelX.getDataVencimento1()));
			else
				mpBoletoRegistro.setDtVencimentoTitulo(this.sdfDMYp.format(mpBoletoSelX.getDataVencimento()));
			//
			mpBoletoRegistro.setTpVencimento("0");

			// MVPR(20180502)-Ajuste Linha Digitavel !
			// String valorX =
			// this.mpBoletoSelX.getValorDocumento().toString().replace(".","");
			String valorX = mpBoletoSelX.getValorCobrado().toString().replace(".", "");
			mpBoletoRegistro.setVlNominalTitulo(valorX);
			//
			try {
				MpEspecieTituloBradesco mpEspecieTituloBradesco = MpEspecieTituloBradesco
																.valueOf(mpBoletoSelX.getEspecieDocumento().trim());
				if (null == mpEspecieTituloBradesco) {
					//
					this.mensagemLog = this.mensagemLog
							+ "( Registro/Bradesco. Não existe Espécie Título (Null/Assumiu=99) / sigla = "
							+ mpBoletoSelX.getEspecieDocumento() + " ) ";
					//
					mpBoletoRegistro.setCdEspecieTitulo("99");
				} else
					mpBoletoRegistro.setCdEspecieTitulo(mpEspecieTituloBradesco.getSigla());
				//
			} catch (Exception e) {
				this.mensagemLog = this.mensagemLog
						+ "( Registro/Bradesco. Não existe Espécie Título (Exception/Assumiu=99) / sigla = "
						+ mpBoletoSelX.getEspecieDocumento() + " ) ";
				//
				mpBoletoRegistro.setCdEspecieTitulo("99");
			}
		}
		
//		System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 001");

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

		if (objeto.getClass() == MpBoletoIntimacao.class) {
			// MVPR-30072018 !
			if (mpBoletoIntimacaoSelX.getNomeSacado().length() > 69)
				mpBoletoRegistro.setNomePagador(mpBoletoIntimacaoSelX.getNomeSacado().substring(0, 69));
			else
				mpBoletoRegistro.setNomePagador(mpBoletoIntimacaoSelX.getNomeSacado());
			//
			if (mpBoletoIntimacaoSelX.getMpEnderecoLocal().getLogradouro().length() > 39)
				mpBoletoRegistro
						.setLogradouroPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getLogradouro().
																								substring(0, 39));
			else
				mpBoletoRegistro.setLogradouroPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getLogradouro());
			//
			mpBoletoRegistro.setNuLogradouroPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getNumero());

			if (null == mpBoletoIntimacaoSelX.getMpEnderecoLocal().getComplemento())
				mpBoletoRegistro.setComplementoLogradouroPagador("");
			else
				mpBoletoRegistro.setComplementoLogradouroPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().
																								getComplemento());

			mpBoletoRegistro.setCepPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getCep().substring(0, 5));
			mpBoletoRegistro.setComplementoCepPagador("0");
			mpBoletoRegistro.setBairroPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getBairro());

			if (null == mpBoletoIntimacaoSelX.getMpEnderecoLocal()
			||  null == mpBoletoIntimacaoSelX.getMpEnderecoLocal().getMpEstadoUF()) {
				//
				mpBoletoRegistro.setMunicipioPagador("RioJaneiro?");
				mpBoletoRegistro.setUfPagador("RJ");
			} else {
				mpBoletoRegistro.setMunicipioPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getMpEstadoUF().
																									getDescricao());
				mpBoletoRegistro.setUfPagador(mpBoletoIntimacaoSelX.getMpEnderecoLocal().getMpEstadoUF().name());
			}
			//
			// System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0001 = " +
			// this.mpBoletoSelX.getCpfCnpj());

			mpBoletoRegistro.setCdIndCpfcnpjPagador("2"); // CNPJ
			mpBoletoRegistro.setNuCpfcnpjPagador(mpBoletoIntimacaoSelX.getCpfCnpj());
			if (mpBoletoIntimacaoSelX.getCpfCnpj().trim().length() < 12) {
				mpBoletoRegistro.setCdIndCpfcnpjPagador("1"); // CPF
				if (mpBoletoIntimacaoSelX.getCpfCnpj().trim().length() == 11)
					mpBoletoRegistro.setNuCpfcnpjPagador("000" + mpBoletoIntimacaoSelX.getCpfCnpj().trim());
				else if (mpBoletoIntimacaoSelX.getCpfCnpj().trim().length() == 10)
					mpBoletoRegistro.setNuCpfcnpjPagador("0000" + mpBoletoIntimacaoSelX.getCpfCnpj().trim());
				else if (mpBoletoIntimacaoSelX.getCpfCnpj().trim().length() == 9)
					mpBoletoRegistro.setNuCpfcnpjPagador("00000" + mpBoletoIntimacaoSelX.getCpfCnpj().trim());
			}
			//
		} else {
			// MVPR-30072018 !
			if (mpBoletoSelX.getNomeSacado().length() > 69)
				mpBoletoRegistro.setNomePagador(mpBoletoSelX.getNomeSacado().substring(0, 69));
			else
				mpBoletoRegistro.setNomePagador(mpBoletoSelX.getNomeSacado());
			//
			if (mpBoletoSelX.getMpEnderecoLocal().getLogradouro().length() > 39)
				mpBoletoRegistro
						.setLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getLogradouro().substring(0, 39));
			else
				mpBoletoRegistro.setLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getLogradouro());
			//
			mpBoletoRegistro.setNuLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getNumero());

			if (null == mpBoletoSelX.getMpEnderecoLocal().getComplemento())
				mpBoletoRegistro.setComplementoLogradouroPagador("");
			else
				mpBoletoRegistro.setComplementoLogradouroPagador(mpBoletoSelX.getMpEnderecoLocal().getComplemento());

			mpBoletoRegistro.setCepPagador(mpBoletoSelX.getMpEnderecoLocal().getCep().substring(0, 5));
			mpBoletoRegistro.setComplementoCepPagador("0");
			mpBoletoRegistro.setBairroPagador(mpBoletoSelX.getMpEnderecoLocal().getBairro());

			if (null == mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF()) {
				mpBoletoRegistro.setMunicipioPagador("RioJaneiro?");
				mpBoletoRegistro.setUfPagador("RJ");
			} else {
				mpBoletoRegistro.setMunicipioPagador(mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().getDescricao());
				mpBoletoRegistro.setUfPagador(mpBoletoSelX.getMpEnderecoLocal().getMpEstadoUF().name());
			}
			//
			// System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0001 = " +
			// this.mpBoletoSelX.getCpfCnpj());

			mpBoletoRegistro.setCdIndCpfcnpjPagador("2"); // CNPJ
			mpBoletoRegistro.setNuCpfcnpjPagador(mpBoletoSelX.getCpfCnpj());
			if (mpBoletoSelX.getCpfCnpj().trim().length() < 12) {
				mpBoletoRegistro.setCdIndCpfcnpjPagador("1"); // CPF
				if (mpBoletoSelX.getCpfCnpj().trim().length() == 11)
					mpBoletoRegistro.setNuCpfcnpjPagador("000" + mpBoletoSelX.getCpfCnpj().trim());
				else if (mpBoletoSelX.getCpfCnpj().trim().length() == 10)
					mpBoletoRegistro.setNuCpfcnpjPagador("0000" + mpBoletoSelX.getCpfCnpj().trim());
				else if (mpBoletoSelX.getCpfCnpj().trim().length() == 9)
					mpBoletoRegistro.setNuCpfcnpjPagador("00000" + mpBoletoSelX.getCpfCnpj().trim());
			}
			//
		}
		//
//		System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 004");

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
			// System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0002");

			// Convert Objeto em JSON !
			// ========================
			ObjectMapper objectMapper = new ObjectMapper();

			// configure Object mapper for pretty print
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

			// writing to console, can write to any output stream such as file
			StringWriter stringBoleto = new StringWriter();
			objectMapper.writeValue(stringBoleto, mpBoletoRegistro);
			//
			this.boletoRegistro = stringBoleto.toString();
			this.boletoRegistro = this.boletoRegistro.replaceAll(", ", ",");
			this.boletoRegistro = this.boletoRegistro.replaceAll(" : ", ":");

			// System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0003 ( " +
			// this.boletoRegistro);

			// PKCS7Signer signer = new PKCS7Signer();
			KeyStore keyStore = this.loadKeyStore();
			CMSSignedDataGenerator signatureGenerator = this.setUpProvider(keyStore);

			// Json de teste...
			// String json = "{\"nuCPFCNPJ\": \"123456789\",\"filialCPFCNPJ\":
			// \"0001\",\"ctrlCPFCNPJ\": \"39\",\"cdTipoAcesso\": \"2\",\"clubBanco\":
			// \"0\",\"cdTipoContrato\": \"0\",\"nuSequenciaContrato\": \"0\",\"idProduto\":
			// \"09\",\"nuNegociacao\": \"123400000001234567\",\"cdBanco\":
			// \"237\",\"eNuSequenciaContrato\": \"0\",\"tpRegistro\": \"1\",\"cdProduto\":
			// \"0\",\"nuTitulo\": \"0\",\"nuCliente\": \"123456\",\"dtEmissaoTitulo\":
			// \"25.05.2017\",\"dtVencimentoTitulo\": \"20.06.2017\",\"tpVencimento\":
			// \"0\",\"vlNominalTitulo\": \"100\",\"cdEspecieTitulo\":
			// \"04\",\"tpProtestoAutomaticoNegativacao\":
			// \"0\",\"prazoProtestoAutomaticoNegativacao\": \"0\",\"controleParticipante\":
			// \"\",\"cdPagamentoParcial\": \"\",\"qtdePagamentoParcial\":
			// \"0\",\"percentualJuros\": \"0\",\"vlJuros\": \"0\",\"qtdeDiasJuros\":
			// \"0\",\"percentualMulta\": \"0\",\"vlMulta\": \"0\",\"qtdeDiasMulta\":
			// \"0\",\"percentualDesconto1\": \"0\",\"vlDesconto1\":
			// \"0\",\"dataLimiteDesconto1\": \"\",\"percentualDesconto2\":
			// \"0\",\"vlDesconto2\": \"0\",\"dataLimiteDesconto2\":
			// \"\",\"percentualDesconto3\": \"0\",\"vlDesconto3\":
			// \"0\",\"dataLimiteDesconto3\": \"\",\"prazoBonificacao\":
			// \"0\",\"percentualBonificacao\": \"0\",\"vlBonificacao\":
			// \"0\",\"dtLimiteBonificacao\": \"\",\"vlAbatimento\": \"0\",\"vlIOF\":
			// \"0\",\"nomePagador\": \"Cliente Teste\",\"logradouroPagador\": \"rua
			// Teste\",\"nuLogradouroPagador\": \"90\",\"complementoLogradouroPagador\":
			// \"\",\"cepPagador\": \"12345\",\"complementoCepPagador\":
			// \"500\",\"bairroPagador\": \"bairro Teste\",\"municipioPagador\":
			// \"Teste\",\"ufPagador\": \"SP\",\"cdIndCpfcnpjPagador\":
			// \"1\",\"nuCpfcnpjPagador\": \"12345648901234\",\"endEletronicoPagador\":
			// \"\",\"nomeSacadorAvalista\": \"\",\"logradouroSacadorAvalista\":
			// \"\",\"nuLogradouroSacadorAvalista\":
			// \"0\",\"complementoLogradouroSacadorAvalista\": \"\",\"cepSacadorAvalista\":
			// \"0\",\"complementoCepSacadorAvalista\": \"0\",\"bairroSacadorAvalista\":
			// \"\",\"municipioSacadorAvalista\": \"\",\"ufSacadorAvalista\":
			// \"\",\"cdIndCpfcnpjSacadorAvalista\": \"0\",\"nuCpfcnpjSacadorAvalista\":
			// \"0\",\"endEletronicoSacadorAvalista\": \"\"}";

			// System.out.println("MpBoletoBean.boletoRegistrar() / ZZZ0004 ( json = " +
			// this.boletoRegistro);

			byte[] signedBytes = this.signPkcs7(this.boletoRegistro.getBytes("UTF-8"), signatureGenerator);
			// System.out.println("Signed Encoded Bytes: " + new
			// String(Base64.encode(signedBytes)));

			HttpEntity entity = new StringEntity(new String(Base64.encode(signedBytes)), Charset.forName("UTF-8"));
			// System.out.println("XXX004 = " + entity);

			// Trata Ambiente de Produção x Homologação ...
			// MpSistemaConfig mpSistemaConfig =
			// mpSistemaConfigs.porParametro("Bradeco_Registro_URI");

//			System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 005 ( " + ofCoNumX);

			MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofCoNumX + "_Bradesco_Registro_URI");
			if (null == mpSistemaConfig) {
				this.msgErro = " ( Contactar o Suporte... Não foi encontrado parâmetro do sistema = '" + ofCoNumX
						+ "_Bradesco_Registro_URI'";
				this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
				//
				String numOfCoX = "";
				// if (null == this.mpCartorioOficioSel)
				// numOfCoX = this.mpCartorioComarcaSel.getNumero();
				// else
				numOfCoX = mpCartorioOficioSel.getNumero();

				this.mpUtilService.gravaMensagemRegistroBoleto(numOfCoX, this.mensagemLog);
				//
				return;
			}

//			System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 006");			
			//
			HttpPost post = new HttpPost(URI_REGISTRO_HOMOLOGACAO);
			if (mpSistemaConfig.getValorT().trim().toUpperCase().equals("PRODUCAO"))
				post = new HttpPost(URI_REGISTRO_PRODUCAO);
			else if (mpSistemaConfig.getValorT().trim().toUpperCase().equals("HOMOLOGACAO"))
				assert (true); // nop
			else {
				this.msgErro = " ( Contactar o Suporte...  BRAD.0001 - Inválido parâmetro do sistema = ( "
						+ mpSistemaConfig.getValorT().trim() + " ) ...assumido PRODUCAO !";
				this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
				//
				post = new HttpPost(URI_REGISTRO_PRODUCAO);
				// return;
			}
			// System.out.println("XXX005 = " + post);
//			System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 007 ( " + post);			

			post.setEntity(entity);
			// System.out.println("XXX006 = POST");

			HttpClientBuilder builder = HttpClientBuilder.create();
			// System.out.println("XXX007 = " + builder);

			// ??? Erro PKIX path building failed -> keytool -insert ... 2 certificados
			// vindo do BRADESCO ...
			// Baixei e usei o utilitário ... KeyStore Explorer 5.3.2 !
			HttpResponse response = builder.build().execute(post);
			// System.out.println("XXX008 = " + response.getStatusLine());

			String boletoRetorno = EntityUtils.toString(response.getEntity(), "UTF-8");
			// System.out.println("XXX009 = " + boletoRetorno);

			Integer posI = boletoRetorno.indexOf("{");
			Integer posF = boletoRetorno.indexOf("}");
			// System.out.println("XXX009-1 = " + posI + " / " + posF + " / " +
			// boletoRetorno.length());

			this.boletoRegistroRetorno = boletoRetorno.substring(posI, posF + 1);
			// System.out.println("XXX0010 = " + this.boletoRegistroRetorno);

			// Convert JSON Retorno para Objeto !
			// ========================
			ObjectMapper objectMapperRet = new ObjectMapper();

			// JSON from String to Object
			MpBoletoRegistroRetorno mpBoletoRegistroRetorno = objectMapperRet.readValue(this.boletoRegistroRetorno,
																				MpBoletoRegistroRetorno.class);
			if (mpBoletoRegistroRetorno.getCdErro().equals("0") || mpBoletoRegistroRetorno.getCdErro().equals("00")) {
				//
				if (objeto.getClass() == MpBoletoIntimacao.class) 
					mpBoletoIntimacaoSelX.setDataHoraRegistro(new Date());
				else
					mpBoletoSelX.setDataHoraRegistro(new Date());
				//
			} else if (mpBoletoRegistroRetorno.getCdErro().equals("69")) { // Titulo Cadastrado ! Avisar Erro !
				this.msgErro = " ( Cod.Erro/Bradesco) = " + mpBoletoRegistroRetorno.getCdErro() + "/ msg.erro = "
						+ mpBoletoRegistroRetorno.getMsgErro() + " / Contactar o Suporte ";
				this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
			} else {
				this.msgErro = " ( Cod.Erro/Bradesco = " + mpBoletoRegistroRetorno.getCdErro() + "/ msg.erro = "
						+ mpBoletoRegistroRetorno.getMsgErro();
				this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
			}
			//
			this.codigoErroRegistro = mpBoletoRegistroRetorno.getCdErro();
			
			System.out.println("MpBoletoBean.boletoRegistrar() - Entrou 008 ( " + this.mensagemLog);			

			String numOfCoX = "";
			// if (null == this.mpCartorioOficioSel)
			// numOfCoX = this.mpCartorioComarcaSel.getNumero();
			// else
			numOfCoX = mpCartorioOficioSel.getNumero();

			if (this.codigoErroRegistro.equals("0") || this.codigoErroRegistro.equals("00"))
				assert(true); // Nop
			else
				this.mpUtilService.gravaMensagemRegistroBoleto(numOfCoX, this.mensagemLog + " / " +
																			mpBoletoRegistro.getNomePagador());
			//
			// System.out.println("XXX011 / Cod.Erro = " +
			// mpBoletoRegistroRetorno.getCdErro() +
			// " / Erro: " + this.msgErro);
			//
		} catch (Exception e) {
			//
			this.msgErro = " ( Exceção = " + e;
			this.mensagemLog = this.mensagemLog + this.msgErro + " ) ";
			// ==========================================================
			String numOfCoX = "";
			// if (null == this.mpCartorioOficioSel)
			// numOfCoX = this.mpCartorioComarcaSel.getNumero();
			// else
			numOfCoX = mpCartorioOficioSel.getNumero();

			System.out.println("MpGeradorBoletoService.boletoRegistrar() - Entrou 009 ( " + this.msgErro + " / " +
							this.mensagemLog + " / " + mpBoletoRegistro.getNomePagador() + " / " + numOfCoX);

			// ==========================================================
			// Salva para Exibir na Tela de Carga da Intimacao ! 
			this.mpUtilService.gravaMensagemRegistroBoleto(numOfCoX, this.mensagemLog + " / " +
																				mpBoletoRegistro.getNomePagador());
			// ==========================================================
		}
		
		// Se o Boleto já foi registrado (CodErro=69)... Não incrementa o Número da GUIA
		// ! ??? MVPR-10042019 !
		if (this.codigoErroRegistro.equals("69")) {
			//
			mpSistemaConfigX = this.mpSistemaConfigs.porParametro(ofCoNumX + "_BoletoNumeroGuia");
			if (null == mpSistemaConfigX) {
				this.mensagemLog = this.mensagemLog + "( "
						+ "Error ! Controle GUIA... não existe ! Contactar o SUPORTE. = " + ofCoNumX
						+ "_BoletoNumeroGuia" + " ) ";
				//
				String numOfCoX = "";
				// if (null == this.mpCartorioOficioSel)
				// numOfCoX = this.mpCartorioComarcaSel.getNumero();
				// else
				numOfCoX = mpCartorioOficioSel.getNumero();

				this.mpUtilService.gravaMensagemRegistroBoleto(numOfCoX, this.mensagemLog);
			} else {
				if (mpSistemaConfigX.getValorN() == 0)
					assert (true);
				else {
					// Atualiza numeroGuia BD !!!
					if (objeto.getClass() == MpBoletoIntimacao.class) 
						assert(true); // nop
					else {
						//
//						mpSistemaConfigX.setValorN(this.numeroGuiaGeradoAnt);
//
//						this.mpSistemaConfigService.salvar(mpSistemaConfigX);
					}
				}
			}
		}
		//
	}
	
	private KeyStore loadKeyStore() throws Exception {
		//
		KeyStore keystore = KeyStore.getInstance("JKS");

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		String numOf = "1";
		String pathOf = PATH_TO_KEYSTORE_1OF;
		if (numOf.equals("2"))
			pathOf = PATH_TO_KEYSTORE_2OF;
		if (numOf.equals("3"))
			pathOf = PATH_TO_KEYSTORE_3OF;
		if (numOf.equals("4"))
			pathOf = PATH_TO_KEYSTORE_4OF;

		InputStream is = new FileInputStream(extContext
				.getRealPath(File.separator + "resources" + File.separator + "opt" + File.separator + pathOf));
		// System.out.println("XXX001 = " + is);

		// InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
		// System.out.println("XXX002 = " + keystore);
		//
		return keystore;
	}

	private CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception {
		//
		// System.out.println("YYY001 = " + keystore);

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
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
						.build(signer, (X509Certificate) cert));

		generator.addCertificates(certstore);
		//
		// System.out.println("YYY002 = " + generator);
		//
		return generator;
	}

	private byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {
		//
		// System.out.println("YYY003 = " + content);

		CMSTypedData cmsdata = new CMSProcessableByteArray(content);
		CMSSignedData signeddata = generator.generate(cmsdata, true);
		//
		return signeddata.getEncoded();
	}
	
	public void gravaBoletoLog(Object objeto, MpCartorioOficio mpCartorioOficioSel, String ambienteBradesco,
																	Date dataIntimacao, String numeroIntimacao) {
		//
		MpBoleto mpBoletoSelX = null;
		MpBoletoIntimacao mpBoletoIntimacaoSelX = null;

		MpBoletoLog mpBoletoLog = new MpBoletoLog();
        
		//
		if (objeto.getClass() == MpBoletoIntimacao.class) {
			//
			mpBoletoIntimacaoSelX = (MpBoletoIntimacao) objeto;
			//
//	        if (null == this.mpCartorioOficioSel)
//        		mpBoletoLog.setNumeroOficio(this.mpCartorioComarcaSel.getNumero());
//        	else
        		mpBoletoLog.setNumeroOficio(mpCartorioOficioSel.getNumero());
        
	        mpBoletoLog.setNumeroDocumento(mpBoletoIntimacaoSelX.getNumeroDocumento());
	        mpBoletoLog.setValorDocumento(mpBoletoIntimacaoSelX.getValorDocumento());
	        mpBoletoLog.setValorCPMF(mpBoletoIntimacaoSelX.getValorCPMF());
	        mpBoletoLog.setValorCobrado(mpBoletoIntimacaoSelX.getValorCobrado());
	        mpBoletoLog.setValorTarifa(mpBoletoIntimacaoSelX.getValorTarifa());
	        mpBoletoLog.setValorLeis(mpBoletoIntimacaoSelX.getValorLeis());
			//
		} else {
			//
			mpBoletoSelX = (MpBoleto) objeto;
			//
//	        if (null == this.mpCartorioOficioSel)
//	        	mpBoletoLog.setNumeroOficio(this.mpCartorioComarcaSel.getNumero());
//	        else
	        	mpBoletoLog.setNumeroOficio(mpCartorioOficioSel.getNumero());
	        
	        mpBoletoLog.setNumeroDocumento(mpBoletoSelX.getNumeroDocumento());
	        mpBoletoLog.setValorDocumento(mpBoletoSelX.getValorDocumento());
	        mpBoletoLog.setValorCPMF(mpBoletoSelX.getValorCPMF());
	        mpBoletoLog.setValorCobrado(mpBoletoSelX.getValorCobrado());
	        mpBoletoLog.setValorTarifa(mpBoletoSelX.getValorTarifa());
	        mpBoletoLog.setValorLeis(mpBoletoSelX.getValorLeis());
		}
		//
        mpBoletoLog.setDataGeracao(new Date());
        mpBoletoLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
        mpBoletoLog.setUsuarioEmail(mpSeguranca.getEmailUsuario()); // MR-07112019 !
        mpBoletoLog.setIndUserWeb(mpSeguranca.getIndUserWebUsuario()); // MR-07112019 !

        mpBoletoLog.setBoletoRegistro(boletoRegistro);
        mpBoletoLog.setBoletoRegistroRetorno(boletoRegistroRetorno);
        mpBoletoLog.setAmbienteBradesco(ambienteBradesco);
        mpBoletoLog.setMensagem(mensagemLog);
        // MR-26032019 (Solicitação 2Of. Rel.BoletosImpressos !
		mpBoletoLog.setProtocolo(sdfDMY.format(dataIntimacao) + "-" + numeroIntimacao);
		//
        mpBoletoLog.setNumeroGuia(numeroGuiaGerado + "");
        mpBoletoLog.setIndCancelamento(false);

        //
        mpBoletoLog = this.mpBoletoLogService.salvar(mpBoletoLog);
        //
        // this.enviaEmailLog(mpBoletoLog);
	}
	
	public void atualizaPdf(File fileX, Object objeto, MpCartorioOficio mpCartorioOficioSel) {
		//
		MpBoleto mpBoletoSelX = null;
		MpBoletoIntimacao mpBoletoIntimacaoSelX = null;

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
		    BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, 
		    																		BaseFont.NOT_EMBEDDED);
		    BaseFont baseFontBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, 
																					BaseFont.NOT_EMBEDDED);
		    //Get the number of pages in pdf.
		    int pages = pdfReader.getNumberOfPages(); 
 
		    //Iterate the pdf through pages.
		    for (int i=1; i<=pages; i++) { 
				//Contain the pdf data.
				PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
	 
				pageContentByte.beginText();
				
				//Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 9);
								 
				//Write text
		        DecimalFormat df = new DecimalFormat();
		        df.applyPattern("R$ ##,###,##0.00");
				
		        String valCobradoX = "?";

		        if (objeto.getClass() == MpBoletoIntimacao.class) {
					//
					mpBoletoIntimacaoSelX = (MpBoletoIntimacao) objeto;

					// ---------------------------------------------//
					// Trata Montagem Dados do Texto da Intimação ! //
					// ---------------------------------------------//
					//
					String nomeSacadoDocX = "";
					String dtProtocoloDocX = "";
					
	    	        Integer posX = mpBoletoIntimacaoSelX.getNomeSacado().indexOf("Protocolo:");
	    	        if (posX >=0) {
	    	        	// Ex.:
	    	        	// SILVIO BARBOSA Protocolo: 14/12/2017-071131
	    	        	// 01234567890123456789012345678901234567890123456789
	    	        	nomeSacadoDocX = mpBoletoIntimacaoSelX.getNomeSacado().substring(0, posX - 1);
	    	        	dtProtocoloDocX = mpBoletoIntimacaoSelX.getNomeSacado().substring(posX + 11, 
	    	        																			posX + 21);
	    	        }
	    	        //
					String itEnderecoCartorio = mpCartorioOficioSel.getEndereco() + " - " +
												mpCartorioOficioSel.getBairroCidadeUF() + " - CEP: " +
												mpCartorioOficioSel.getCep();
					String itTelefoneCartorio = "Tel: " + mpCartorioOficioSel.getTelefone();
					String itCelularCartorio = "WhatsApp: " + mpCartorioOficioSel.getCelular();
					String itEmailCartorio = "E-mail: " + mpCartorioOficioSel.getEmail();
					
					// Pegar SistConfig?
					String itHorarioFuncionamentoCartorio = "Horário de Atendimento: das: " + 
															mpCartorioOficioSel.getHorarioFuncionamento();
					//
					String itNumeroProtocolo = "Nº do Protocolo: " + dtProtocoloDocX  + "–" +
															mpBoletoIntimacaoSelX.getNumeroIntimacao();
					String itCodigoSeguranca = "Código de Segurança: " + 
															mpBoletoIntimacaoSelX.getNumeroIntimacaoCode();
					
					
					Integer itLinX = 750; // Linha 
					Integer itColX = 007; // Coluna
					Integer itEspX = 010; // Espaçamento

					String[] itTextoLin = {
						itLinX + "/" + itColX + "/" + itEspX,
						"Horário de Atendimento: das 10:00 h até 16:00 h",
						"A este Ofício de Protesto de Títulos foi solicitada a apresentação e a intimação de V.Sª. para pagamento do título/documento de dívida, com as",
						"características abaixo, sendo consequente a lavratura do instrumento de protesto, caso não ocorra seu pagamento, neste Tabelionato, até 03 dias",
						"úteis da data da assinatura da intimação. Por este instrumento que expedi e remeti na forma do art. 1º do Provimento 97 do CNJ (Conselho",
						"Nacional de Justiça), INTIMO-O(A) a efetuar o aludido pagamento, sob pena de protesto de cujo instrumento, a ser entregue ao apresentante,",
						"constará a resposta eventualmente oferecida, por escrito no mesmo prazo.",						
					};

					// Trata Captura 'txtPDFLayoutIntima21' dados Layout PDF Intimação ! 
					String valorT = (String) this.mpUtilService.buscaMpConsigValor("txtPDFLayoutIntima21", 
			    																	MpTipoCampo.TEXTO);
			    	if (null == valorT)
			    		assert(true); // nop
			    	else {
			    		//
			    		itTextoLin = valorT.split("§");
			    		//  012345678901
						// "999/999/999",
//			    		System.out.println("MpGeradorBoletoService.atualizaPdf() ( " + itTextoLin.length 
//										    		+ " / '" + itTextoLin[0] + "' / ValorT = ( " + valorT);
			    		//
			    		itLinX = Integer.parseInt(itTextoLin[0].substring(0, 3));  // 010;
			    		itColX = Integer.parseInt(itTextoLin[0].substring(4, 7));  // 600;
			    		itEspX = Integer.parseInt(itTextoLin[0].substring(8, 11)); // 020;
			    		
			    		// Captura Horário Funcionamento !
			    		itHorarioFuncionamentoCartorio = itTextoLin[1];
			    	}

			    	//
					String itDevedorNome = "Ilmo(a) Sr(a) " + nomeSacadoDocX;
					
					String formatCpfCnpj = "";
					if (mpBoletoIntimacaoSelX.getCpfCnpj().trim().length() > 11)
						formatCpfCnpj = MpAppUtil.formatarString(mpBoletoIntimacaoSelX.getCpfCnpj(),
																				"##.###.###/####-##");
					else
						formatCpfCnpj = MpAppUtil.formatarString(mpBoletoIntimacaoSelX.getCpfCnpj(),
																					"###.###.###-##");
					//
					String itDevedorDocumento = "CNPJ/CPF: " + formatCpfCnpj;

					String itEspecieTitulo = "Espécie De Título: " + 
																mpBoletoIntimacaoSelX.getEspecieDocumento();
					String itEndosso = "Endosso: " + mpBoletoIntimacaoSelX.getEndosso();
					String itNumeroTitulo = "Número do Título: " + mpBoletoIntimacaoSelX.getNumeroTitulo();

					String itEmissaoTitulo = "Emissão: " + 
											sdfDMY.format(mpBoletoIntimacaoSelX.getDataEmissao());
					String itVencimentoTitulo = "Vencimento: " + 
											sdfDMY.format(mpBoletoIntimacaoSelX.getDataVencimentoTitulo());
					String itMotProtestoTitulo = "Motivo do protesto: " + 
																mpBoletoIntimacaoSelX.getMotivoProtesto();

					String valSaldoX = df.format(mpBoletoIntimacaoSelX.getValorSaldo());

					valSaldoX = valSaldoX.replace(",", "X");
					valSaldoX = valSaldoX.replace(".", ",");
					valSaldoX = valSaldoX.replace("X", ".");
										
					String itValorSaldo = "Valor do Saldo:: " + valSaldoX;

					String itApresentanteNome = "Apresentante: " + 
																mpBoletoIntimacaoSelX.getNomeApresentante();
					String itApresentanteDocumento = ""; // "CNPJ/CPF: " + 
														 //	mpBoletoIntimacaoSelX.getCpfCnpj(); // ???

					String itCedenteNome = "Cedente: " + mpBoletoIntimacaoSelX.getNomeCedente();
					String itSacadorNome = "Sacador: " + mpBoletoIntimacaoSelX.getNomeSacador();
					String itSacadorDocumento = ""; // "CNPJ/CPF: ?????????";
//																+ mpBoletoIntimacaoSelX.getDocumentoSacador()
				
					//
					String itCustasTitulo = "Custas: " + mpBoletoIntimacaoSelX.getCustas();					
					// Acerta texto DistribuiÃ§Ã£o: -> Distribuição: ..
					itCustasTitulo = itCustasTitulo.replace("DistribuiÃ§Ã£o", "Distribuição");
					
					//
					valCobradoX = df.format(mpBoletoIntimacaoSelX.getValorCobrado());

					valCobradoX = valCobradoX.replace(",", "X");
			        valCobradoX = valCobradoX.replace(".", ",");
			        valCobradoX = valCobradoX.replace("X", ".");
			        
					// Texto Forma Pagamento... 
					String[] itTextoLin1 = {
						"FORMA DE PAGAMENTO: Através deste boleto bancário no valor de " + 
						valCobradoX + ", ( valor original da dívida + emolumentos + acréscimos legais + tarifas",
						"bancárias). A quitação somente será fornecida após a confirmação do recebimento pelo cartório. PRAZO DE PAGAMENTO: O pagamento somente poderá ser feito",
						"até a data prevista neste boleto. Tentativa de pagamento posterior ou por meios diferentes do aqui previsto, não impedirá a lavratura do protesto e eventual",
						"responsabilização por fraude. Para atualizar o vencimento do boleto acesse o link http://www.protestorjcapital.com.br:8080/apps/ServicoSite?tab=0 e siga as",
						"orientações."						
					};

					// 
					valorT = (String) this.mpUtilService.buscaMpConsigValor("txtPDFLayout1Intima21", 
			    																	MpTipoCampo.TEXTO);
			    	if (null == valorT)
			    		assert(true); // nop
			    	else {
			    		//
			    		itTextoLin1 = valorT.split("§");
			    		//
			    	}

					// Texto Especie do Titulo ..
					String[] itTextoLin2 = {
						"Legenda de Espécie de Títulos DM = Duplicata Mercantil; DS = Duplicata de Prestação de Serviço; DMI = Duplicata de Venda Mercantil por Indicação; DSI = Duplicata de Prestaçãode Serviço por",
						"Indicação; NP = Nota Promissória; CH = Cheque; CC = Contrato de Câmbio; LC = Letra de Câmbio; DD = Documento de Dívida; TM = Triplicata Mercantil; TS =Triplicata de Serviço; CDA = Certidão da",
						"Dívida Ativa; CF = Confissão de Dívida; TJD = Título Judicial Definitivo; CDT = Certidões de Débito do Tribunal; NPR = NotaPromissória Rural; RA = Recibo de Aluguel; EC = Encargos Condominiais"						
					};

					// 
					valorT = (String) this.mpUtilService.buscaMpConsigValor("txtPDFLayout2Intima21", 
			    																			MpTipoCampo.TEXTO);
			    	if (null == valorT)
			    		assert(true); // nop
			    	else {
			    		//
			    		itTextoLin2 = valorT.split("§");
			    		//
			    	}
					
					// ------------------------------------------------- //
					// Trata Preenchimeto Dados do Texto da Intimação !  //
					// ------------------------------------------------- //
					pageContentByte.setTextMatrix(itColX + 30, itLinX);
					pageContentByte.showText(itEnderecoCartorio + " " + itTelefoneCartorio + " " + 
																						itCelularCartorio);
					itLinX = itLinX - itEspX;
					pageContentByte.setTextMatrix(itColX + 30, itLinX);
					pageContentByte.showText(itEmailCartorio+ " " + itHorarioFuncionamentoCartorio);

					// Numero Protocol & Código Segurança ...
					//Set text font and size.
					pageContentByte.setFontAndSize(baseFontBold, 9);

					itLinX = itLinX - itEspX - 5;
					pageContentByte.setTextMatrix(itColX + 80, itLinX);
					pageContentByte.showText(itNumeroProtocolo + " " + itCodigoSeguranca);
					itLinX = itLinX - 5;
					
					// Texto Inicial Intimação ...
					pageContentByte.setFontAndSize(baseFont, 9);

					// Cuidado Pos.0 = L/C/S e Pos.1 = Horario Funcionamento ...
					for (int linX = 2; linX < itTextoLin.length; linX++) {
						//
						itLinX = itLinX - itEspX;
						
						pageContentByte.setTextMatrix(itColX, itLinX);
						pageContentByte.showText(itTextoLin[linX]);
					}
					//
					itLinX = itLinX - itEspX - 5;

					//Set text font and size.
					pageContentByte.setFontAndSize(baseFontBold, 9);

					pageContentByte.setTextMatrix(itColX, itLinX);
					pageContentByte.showText(itDevedorNome + " " + itDevedorDocumento);
					itLinX = itLinX - itEspX;

					// Especie Titulo + Endosso + NumeroTitulo ...
					pageContentByte.setFontAndSize(baseFont, 7);

					pageContentByte.setTextMatrix(itColX, itLinX);
					pageContentByte.showText(itEspecieTitulo + " " + itEndosso  + " " + itNumeroTitulo +
									itEmissaoTitulo + " " + itVencimentoTitulo  + " " + itMotProtestoTitulo);
					itLinX = itLinX - itEspX;

					pageContentByte.setTextMatrix(itColX, itLinX);
					pageContentByte.showText(itValorSaldo + " " + itApresentanteNome + " - " +
																					itApresentanteDocumento);
					itLinX = itLinX - itEspX;

					// Cedente ...
					//Set text font and size.
					pageContentByte.setFontAndSize(baseFont, 6);
					
					pageContentByte.setTextMatrix(itColX, itLinX);
					pageContentByte.showText(itCedenteNome + " " + itSacadorNome + " - " +
																						itSacadorDocumento);
					itLinX = itLinX - itEspX - 3;

					// Custas Titulo ...
					
					pageContentByte.setTextMatrix(itColX, itLinX);
					pageContentByte.showText(itCustasTitulo);

					itLinX = itLinX - 5;
					
					// Texto Forma Pagamento ...
					//Set text font and size.
					pageContentByte.setFontAndSize(baseFont, 8);

					for (int linX1 = 0; linX1 < itTextoLin1.length; linX1++) {
						//
						itLinX = itLinX - itEspX;
						
						pageContentByte.setTextMatrix(itColX, itLinX);
						
						// Trata Valor Pagamento ...
						String linhaP = itTextoLin1[linX1].replace("R$ ??.???,??", valCobradoX);
						
						pageContentByte.showText(linhaP);
					}
					itLinX = itLinX - 5;
					
					// Texto Legenda Titulo...
					pageContentByte.setFontAndSize(baseFont, 6);

					for (int linX2 = 0; linX2 < itTextoLin2.length; linX2++) {
						//
						itLinX = itLinX - itEspX;
						
						pageContentByte.setTextMatrix(itColX, itLinX);
						pageContentByte.showText(itTextoLin2[linX2]);
					}
					//
					itLinX = itLinX - itEspX;

					// Reseta Fonte Valor Normal ...
					pageContentByte.setFontAndSize(baseFont, 9);
					
					// ------------------------------------------------- //
					//
				} else {
					//
					mpBoletoSelX = (MpBoleto) objeto;
					
			        valCobradoX = df.format(mpBoletoSelX.getValorCobrado());

			        valCobradoX = valCobradoX.replace(",", "X");
			        valCobradoX = valCobradoX.replace(".", ",");
			        valCobradoX = valCobradoX.replace("X", ".");
				}
				//
				pageContentByte.setTextMatrix(430, 400);

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
			
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			
			String pathX = extContext.getRealPath(File.separator + capturaPathResourcePdf(mpCartorioOficioSel) +
																							File.separator);
			//		
    		File fileOutWeb = new File(pathX + File.separator + fileX.getName());
			
    		pdfReader = new PdfReader(fileX.getAbsolutePath());
    		
    		pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(fileOutWeb.getAbsolutePath()));

//			System.out.println("AtualizaPdf(00) ......................................... ( pdfStamperWeb = " + 
//																						fileOutWeb.getAbsolutePath());
		    //
			pdfStamper.close();
			pdfReader.close();

			//			this.arquivoPdf = fileOutX;
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
	
	private String capturaPathResourcePdf(MpCartorioOficio mpCartorioOficioSel) {
		;
		//
		String pathFileX = File.separator + "resources" + File.separator + "pdfs" + File.separator;

//		if (null == this.mpCartorioOficioSel)
//			pathFileX = pathFileX + "co" + this.mpCartorioComarcaSel.getNumero();
//		else
			pathFileX = pathFileX + "of" + mpCartorioOficioSel.getNumero();
		//
		return pathFileX;
	}

}
