package com.mpxds.mpbasic.controller.intima21;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.mpxds.mpbasic.model.MpUsuarioContato;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.model.ws.intima21.retorno.Acesso;
import com.mpxds.mpbasic.model.ws.intima21.retorno.Intimacao;
import com.mpxds.mpbasic.model.ws.intima21.retorno.Notificacao;
import com.mpxds.mpbasic.model.ws.intima21.retorno.Retorno;
import com.mpxds.mpbasic.repository.MpUsuarioContatos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.util.MpUtilService;
//import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

import br.com.cra21.crarj.ConsultaIntimacaoDocument;
import br.com.cra21.crarj.ConsultaIntimacaoResponseDocument;
import br.com.cra21.crarj.impl.ConsultaIntimacaoDocumentImpl;
import samples.quickstart.service.xmlbeans.ServerCraStub;
 
@Named
@ViewScoped
public class MpBaixaIntima21Bean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpSeguranca mpSeguranca;
	
	@Inject
	private MpUtilService mpUtilService;
	
	@Inject
	private MpUsuarioContatos mpUsuarioContatos;
	
	private ServerCraStub stubX;

	// 
	
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;
	private MpCartorioOficio mpCartorioOficioX;
	
	private Date dataDe;
	private Date dataAte;
	
	private StreamedContent file;	
     
	private SimpleDateFormat sdfDMYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");

	private String msgIntima21 = "";	
	
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
			msg = msg + " (Data De maior Data Até) = " + sdfDMY.format(this.dataDe) + " - " + 
																					sdfDMY.format(this.dataAte);
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
		this.baixaArquivoRetornoIntima21();

		MpFacesUtil.addErrorMessage(this.msgIntima21);
		//
		return this.file;  
    }
	
    public void baixaArquivoRetornoIntima21() {
    	//
		try {
			//
			Boolean indProducaoIntima21 = mpUtilService.buscaAmbienteProducaoIntima21(); // Produçao/Teste(Sandbox) !
			
			String urlIntima21 = mpUtilService.buscaUrlIntima21(indProducaoIntima21);
			if (urlIntima21.isEmpty()) {
				//
    			MpFacesUtil.addErrorMessage("Error captura URL da API 21!");
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
	    	
//	    	System.out.println("MpCarregaBoletoIntimacaoBean.enviaArquivoIntima21()................. ( Url = " +
//														urlIntima21 + " / IndProd =  " + indProducaoIntima21 +
//														" / User = " + usuarioIntima21 + " / Psw = " + senhaIntima21);
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
			this.geraBaixaIntimacaoRetorno();
			//
		} catch (Throwable e) {
			//
			System.out.println(".................................................erro inesperado");
			e.printStackTrace();
		}
    }    
    
	public void criaStubIntima21(String endereco) throws AxisFault {
		//
		this.stubX = new ServerCraStub(endereco);
	}
	
	private void geraBaixaIntimacaoRetorno() throws InstantiationException, IllegalAccessException, IOException {
		//
		XmlOptions options = this.stubX._getXmlOptions();

		ConsultaIntimacaoDocument consultaIntimacaoDocument = ConsultaIntimacaoDocument.Factory.newInstance(options);

		ConsultaIntimacaoDocumentImpl.ConsultaIntimacao consultaIntimacao18 = 
										ConsultaIntimacaoDocumentImpl.ConsultaIntimacao.Factory.newInstance(options);

		// Site Documentação ... https://p21sistemas.github.io/intima21/ ! 
//		<consulta>
//		    <data_geracao_inicial>01/01/2020</data_geracao_inicial>
//		    <data_geracao_final>30/01/2020</data_geracao_final>
//		    <data_notificacao_inicial>01/01/2020</data_notificacao_inicial>
//		    <data_notificacao_final>30/01/2020</data_notificacao_final>
//		    <data_validade_inicial>01/01/2020</data_validade_inicial>
//		    <data_validade_final>30/01/2020</data_validade_final>
//	    </consulta>		
		
		XmlString userDados = XmlString.Factory.newInstance();
		userDados.setStringValue("<consulta><data_geracao_inicial>" + sdfDMY.format(this.dataDe) + 
					"</data_geracao_inicial><data_geracao_final>" + sdfDMY.format(this.dataAte) + 
					"</data_geracao_final></consulta>");
		
		consultaIntimacao18.xsetUserDados(userDados);

		consultaIntimacaoDocument.setConsultaIntimacao(consultaIntimacao18);
		//
		ConsultaIntimacaoResponseDocument resposta = ConsultaIntimacaoResponseDocument.Factory.newInstance(options);

//		ConsultaIntimacaoResponseDocumentImpl.ConsultaIntimacaoResponse consultaIntimacaoResponse =
//																		resposta.addNewConsultaIntimacaoResponse();

		resposta = this.stubX.consultaIntimacao(consultaIntimacaoDocument);

//		System.out.println("............ Intimacao = " + resposta.getConsultaIntimacaoResponse().getReturn());
		
//		XmlObject xmlObject = resposta.getConsultaIntimacaoResponse().copy();

//		String xmlRetorno = "<relatorio><intimacao><protocolo>0123456789</protocolo><data_protocolo>04/05/2020</data_protocolo><especie>DMI</especie><numero>01234567891</numero><data_validade>10/05/2020</data_validade><fins_falimentares>1</fins_falimentares><data_geracao>07/05/2020</data_geracao><nome_devedor>MARCELO</nome_devedor><documento_devedor>01234567890</documento_devedor><notificacoes><notificacao><nome_devedor_notificado>MARCELO</nome_devedor_notificado><documento_devedor_notificado>01234567890</documento_devedor_notificado><tipo_devedor_notificado>1</tipo_devedor_notificado><celular_devedor_notificado>21999999999</celular_devedor_notificado><emails_devedor_notificado><email>email@email.com.br</email><email>email2@email.com.com.br</email></emails_devedor_notificado></notificacao></notificacoes><acessos><acesso><origem>emaiL@email.com</origem><ip>192.168.0.0</ip><navegador>Chrome-81.0</navegador><sistema_operacional>Windows</sistema_operacional><data>14/05/2020 - 11:45</data></acesso></acessos></intimacao><intimacao><protocolo>0123456788</protocolo><data_protocolo>03/05/2020</data_protocolo><especie>CBI</especie><numero>01234567892</numero><data_validade>10/05/2020</data_validade><fins_falimentares>0</fins_falimentares><data_geracao>07/05/2020</data_geracao><nome_devedor>JERFESON</nome_devedor><documento_devedor>04065456185</documento_devedor><notificacoes><notificacao><nome_devedor_notificado>JERFESON</nome_devedor_notificado><documento_devedor_notificado>04065456185</documento_devedor_notificado><tipo_devedor_notificado>1</tipo_devedor_notificado><celular_devedor_notificado>21999999999</celular_devedor_notificado><emails_devedor_notificado><email>email@email.com.br</email><email>email2@email.com.com.br</email></emails_devedor_notificado></notificacao></notificacoes><acessos><acesso><origem>emaiL@email.com</origem><ip>192.168.0.0</ip><navegador>Chrome-81.0</navegador><sistema_operacional>Windows</sistema_operacional><data>14/05/2020 - 11:45</data></acesso></acessos></intimacao><intimacao><protocolo>027181</protocolo><data_protocolo>07/05/2020</data_protocolo><especie>DMI</especie><numero>57391-D</numero><data_validade>12/05/2020</data_validade><fins_falimentares>0</fins_falimentares><data_geracao>11/05/2020</data_geracao><nome_devedor>BRASILCRAFT COM DE ARTEF DE COURO L</nome_devedor><documento_devedor>06088958000162</documento_devedor><notificacoes><notificacao><nome_devedor_notificado>BRASILCRAFT COM DE ARTEF DE COURO L</nome_devedor_notificado><documento_devedor_notificado>06088958000162</documento_devedor_notificado><tipo_devedor_notificado>1</tipo_devedor_notificado><celular_devedor_notificado>21999999999</celular_devedor_notificado><emails_devedor_notificado><email>email@email.com.br</email><email>email2@email.com.com.br</email></emails_devedor_notificado></notificacao></notificacoes><acessos><acesso><origem>emaiL@email.com</origem><ip>192.168.0.0</ip><navegador>Chrome-81.0</navegador><sistema_operacional>Windows</sistema_operacional><data>14/05/2020 - 11:45</data></acesso></acessos></intimacao><intimacao><protocolo>027607</protocolo><data_protocolo>11/05/2020</data_protocolo><especie>DMI</especie><numero>110</numero><data_validade>14/05/2020</data_validade><fins_falimentares>0</fins_falimentares><data_geracao>13/05/2020</data_geracao><nome_devedor>JG BAZAR E FERRAGENS LTDA</nome_devedor><documento_devedor>31873345000183</documento_devedor><notificacoes><notificacao><nome_devedor_notificado>JG BAZAR E FERRAGENS LTDA</nome_devedor_notificado><documento_devedor_notificado>31873345000183</documento_devedor_notificado><tipo_devedor_notificado>1</tipo_devedor_notificado><celular_devedor_notificado>21999999999</celular_devedor_notificado><emails_devedor_notificado><email>email@email.com.br</email><email>email2@email.com.com.br</email></emails_devedor_notificado></notificacao></notificacoes><acessos><acesso><origem>emaiL@email.com</origem><ip>192.168.0.0</ip><navegador>Chrome-81.0</navegador><sistema_operacional>Windows</sistema_operacional><data>14/05/2020 - 11:45</data></acesso></acessos></intimacao><intimacao><protocolo>026609</protocolo><data_protocolo>05/05/2020</data_protocolo><especie>DMI</especie><numero>320150-2</numero><data_validade>08/05/2020</data_validade><fins_falimentares>0</fins_falimentares><data_geracao>13/05/2020</data_geracao><nome_devedor>FILL TRANSPORTE DE CARGAS E SERVICO DE LOGIST</nome_devedor><documento_devedor>31751580000182</documento_devedor><notificacoes><notificacao><nome_devedor_notificado>FILL TRANSPORTE DE CARGAS E SERVICO DE LOGIST</nome_devedor_notificado><documento_devedor_notificado>31751580000182</documento_devedor_notificado><tipo_devedor_notificado>1</tipo_devedor_notificado><celular_devedor_notificado>21999999999</celular_devedor_notificado><emails_devedor_notificado><email>email@email.com.br</email><email>email2@email.com.com.br</email></emails_devedor_notificado></notificacao></notificacoes><acessos><acesso><origem>emaiL@email.com</origem><ip>192.168.0.0</ip><navegador>Chrome-81.0</navegador><sistema_operacional>Windows</sistema_operacional><data>14/05/2020 - 11:45</data></acesso></acessos></intimacao></relatorio>";

		String xmlRetorno = resposta.getConsultaIntimacaoResponse().getReturn();
		// Remove HEADER !
		xmlRetorno = xmlRetorno.replace("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>", "");
		
//		File file = new File("relatorio.xml");  
		//
		JAXBContext jaxbContext;
		
		try {
			//
			jaxbContext = JAXBContext.newInstance(Retorno.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	        
			StringReader reader = new StringReader(xmlRetorno);
			
//	        Relatorio.Retorno retorno = (Relatorio.Retorno) jaxbUnmarshaller.unmarshal(file);
	        Retorno retorno = (Retorno) jaxbUnmarshaller.unmarshal(reader);

	        // Trata Gravação arquivo RETORNO ! 
			Integer totalRegistros = retorno.getIntimacaos().size();
			
			String newLineX = "\r\n"; // System.getProperty("line.separator").toString();		    
		    String boletoTXT = "";
		    
		    String boletoALL = "[HEADER, Data = " + this.sdfDMYHMS.format(new Date()) + ", Total Registros = " + 
		    																	totalRegistros + " ]" + newLineX;
		    //
	        for (Intimacao intimacao : retorno.getIntimacaos()) {
	        	//
	        	// Trata captura dos TELEFONES(CELULARES) e E-MAILS ! 
		        String telefoneX = "";
		        String emailX = "";

//		        System.out.println("............ / N.Intimações = " + intimacao.getNotificacaos().size());

		        for (Notificacao notificacao : intimacao.getNotificacaos()) {
		        	//
//		        	System.out.println("............ Telefone = " + telefoneX + " / " + 
//		        								notificacao.getCelular_devedor_notificado() +
//		        								" / N.Emails = " + notificacao.getEmails().size());
		        	if (telefoneX.isEmpty())
		        		telefoneX = notificacao.getCelular_devedor_notificado();
		        	else
		        		telefoneX = telefoneX + " ; " + notificacao.getCelular_devedor_notificado();
		        	//
		        	for (String email : notificacao.getEmails()) {
		        		//
//			        	System.out.println("............ Email = " + email); 

			        	if (emailX.isEmpty())
			        		emailX = email;
			        	else
			        		emailX = emailX + " ; " + email;
		        	}
		        }

	        	//
//	        	System.out.println("............ / N.Acessos = " + intimacao.getAcessos().size());
	        	String emailAcessoX = "";

	        	for (Acesso acesso : intimacao.getAcessos()) {
		        	//
//		        	System.out.println("............ Acesso = " + acesso.getOrigem() + " / " + emailAcessoX); 

		        	if (emailAcessoX.isEmpty())
		        		emailAcessoX = acesso.getOrigem();
		        	else
		        		emailAcessoX = emailAcessoX + " ; " + acesso.getOrigem();
		        }
		        
	    		//
	        	boletoTXT = "[\"" + intimacao.getData_protocolo() + "\", " +
	    				 "\"" + intimacao.getProtocolo() + "\", " +
	    			 	 "\"" + telefoneX + "\", " +
	    				 "\"" + emailX + "\", " +
	    				 "\"" + emailAcessoX + "\", " +
	    				 "\"" + intimacao.getDocumento_devedor() + "\"]" + newLineX;
	        	//
	        	boletoALL = boletoALL + boletoTXT;

	        	// Salva Dados Usuario Contato ...
	        	MpUsuarioContato mpUsuarioContato = mpUsuarioContatos.porCpfCnpj(intimacao.getDocumento_devedor());
	        			
	        	if (null == mpUsuarioContato) 
	        		mpUsuarioContato = new MpUsuarioContato();
	        	//
	        	mpUsuarioContato.setCpfCnpj(intimacao.getDocumento_devedor());
	        	mpUsuarioContato.setNome(intimacao.getNome_devedor());
	        	mpUsuarioContato.setEmail(emailX);
	        	mpUsuarioContato.setTelefone(telefoneX);
	        	
	        	mpUsuarioContatos.guardar(mpUsuarioContato);
	        	//
        	
//	        	System.out.println("............ Intimacao = " + boletoTXT);
	        }
	        //
		    //
		    boletoALL = boletoALL + "[TRAILLER, Data = " + this.sdfDMYHMS.format(new Date()) + ", Total Registros = " 
		    																	+ totalRegistros + " ]" + newLineX;
		    
			// 1Oficio_aaaammdd_intimacao_eletronica.txt (Prisco Ajutse MVPR-30052018 ...
			String nomeArquivo =  this.mpCartorioOficioSel.getNumero() + "Oficio_" + this.sdfYMD.format(new Date()) +
																						"_intimacao_eletronica.txt";

	        // Salva arquivo na pasta relacionada ao cartório em questão ! Ex.: ../Ofx/..
	        String pathX = System.getProperty("user.home") + File.separator + "Of" + 
	        										this.mpCartorioOficioSel.getNumero().trim() + File.separator;

			File targetFile = new File(pathX + nomeArquivo);
			
			// 
		    com.google.common.io.Files.write(boletoALL, targetFile, Charset.forName("UTF-8"));

//			System.out.println("MpBaixaIntima21.geraBaixaIntimacaoRetorno() - 000 ( Path = " + 
//										pathX + " / Arq = " + nomeArquivo + " / " + targetFile.getAbsolutePath());
		    //
			if (pathX.indexOf("C:") >= 0) // Windows
				assert(true); // nope
			else { // Linux
				//
				Path path = Paths.get(pathX + nomeArquivo);
			    
		        Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
		        
		        perms.add(PosixFilePermission.OWNER_WRITE);
		        perms.add(PosixFilePermission.OWNER_READ);
		        perms.add(PosixFilePermission.OWNER_EXECUTE);
		        perms.add(PosixFilePermission.GROUP_WRITE);
		        perms.add(PosixFilePermission.GROUP_READ);
		        perms.add(PosixFilePermission.GROUP_EXECUTE);
		        perms.add(PosixFilePermission.OTHERS_WRITE);
		        perms.add(PosixFilePermission.OTHERS_READ);
		        perms.add(PosixFilePermission.OTHERS_EXECUTE);
		        
		        Files.setPosixFilePermissions(path, perms);
		        //
			}
	        //
	        String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(
        																			targetFile.getAbsolutePath());
	        
	        this.file = new DefaultStreamedContent(new FileInputStream(targetFile.getAbsolutePath()), contentType,
        																							nomeArquivo);
	        
//			System.out.println("MpBaixaIntima21.geraBaixaIntimacaoRetorno() - 001 (.......... " + contentType + 
//					" / " + resposta.getConsultaIntimacaoResponse().getReturn() + " / " + userDados +
//					" / " +	resposta.getConsultaIntimacaoResponse().toString());
	        //
		} catch (JAXBException e) {
			System.out.println(".................................................erro inesperado");
			e.printStackTrace();
		}  
   			
    	// ------------------------------------------------
    	
		this.msgIntima21 = resposta.getConsultaIntimacaoResponse().getReturn();
		//
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
    
}