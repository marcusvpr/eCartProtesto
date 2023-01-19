package com.mpxds.mpbasic.controller;

import java.io.ByteArrayInputStream;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.PrintStream;
import java.io.Serializable;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
//import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
//import com.outjected.email.api.ContentDisposition;
//import com.outjected.email.api.MailMessage;
//import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class MpBoletoImpressoJobBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpBoletos mpBoletos;	

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

//	private MpMailer mpMailer;
	
	// ---
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String dataJob;
	private String pathArquivo;     
	// ---
    
	public void init() {
	  	//
        this.dataJob = sdf.format(new Date());        
		//
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.init() = " + this.dataJob);
		
		this.trataEmailBoletoImpresso("1");
    }

    protected void trataEmailBoletoImpresso(String numOficio) {
		//
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() - 000 ( " + numOficio);
    		
    	MpCartorioOficio mpCartorioOficio = MpCartorioOficio.valueOf("Of"+ numOficio);
    	if (null ==mpCartorioOficio) {
    		//
        	MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() - Erro ( Of" + numOficio +
        														" ) ... não existe !");
        	return;
    	}
		// Verificar Horário de Funcionamento !
	    SimpleDateFormat sdfHHMM = new SimpleDateFormat("HH:mm");
	    //
	    Date dtBoleto = null;
	    Date dtAtual = null;		
	    Date dtAtualF = null;		
    	//
	    Calendar calendarX = Calendar.getInstance();	    	
	    //
	    MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("Of" + numOficio + "_HorarioBoleto");
	    if (null == mpSistemaConfig) {
	    	//
	   		try {
				dtBoleto = sdfHHMM.parse(mpCartorioOficio.getHorarioGeracaoBoleto().substring(0, 5));
			} catch (ParseException e) {
    			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() ( e = " + e);
				return;
			}
	   	} else {
	   		//
    		if (!mpSistemaConfig.getIndValor()) {
	    		//
    			MpAppUtil.PrintarLn("Serviço Temporariamente Indisponivel ! Tente mais tarde. ( Of.= " + numOficio);
    			return;
	    	}
	    	// 17:00 17:05
    		// 012345678901
	    	calendarX.setTime(new Date());
	    	calendarX.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mpSistemaConfig.getValorT().substring(6, 8)));
	    	calendarX.set(Calendar.MINUTE, Integer.parseInt(mpSistemaConfig.getValorT().substring(9, 11)));
	    	calendarX.set(Calendar.SECOND, 0);
	    	//
	    	dtBoleto = calendarX.getTime();
	    }
	    //
	    dtAtual = new Date();	    	
		calendarX.setTime(dtAtual);
		calendarX.add(Calendar.MINUTE, 3);	    	
	    dtAtualF = calendarX.getTime();
	    //
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() - 001 ( At = " + sdf.format(dtAtual) +
					" / Bol = " + sdf.format(dtBoleto) + " / Bol+1 = " + sdf.format(dtAtualF));
		//
	    if ((dtBoleto.after(dtAtual) && dtBoleto.before(dtAtualF))) {
	    	// Trata envio Email com Boletos Impressos !
			MpSistemaConfig mpSistemaConfigX = mpSistemaConfigs.porParametro("Of" + numOficio + "_EmailBoleto");
	    	if (null == mpSistemaConfigX) {
		    	//
    			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() ! " + 
    												"Parâmetro não existe ! ( Of" + numOficio + "_EmailBoleto");
    			return;
		   	} else {
				//
		   		Integer contBoleto = 0;
			        
		   		List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByImpressaoList();
				    
		   		String boletoJSON = "";
		   		String boletoSQL = "";
				//   
		   		String paramSQL = "INSERT INTO Deposito (Dt_Protocolo, No_Protocolo, Total_Pagar, Valor_Cpmf," +
				    	"Valor_TabII6b, Valor_Tarifa, Valor_Cartorio, Total_Deposito, No_Guia, Campo1, Campo2," +
				    	"Campo3, Campo4, Campo5, Dt_Sistema, Usr_Deposito, CodigoBarra, DinChq, InfCheque," + 
				    	"Dt_Vencto, HoraEmissao, TabII, TabII6bLei3217, TabII6bLei4664, TabII6bLei111," +
				    	"TabII6bLei6281, NossoNumero, SequenciaNoTitulo, DigitoCedente, CodigoEmpresa) " + 
				    	"VALUES (CONVERT(DATETIME, 'Dt_Protocolo', 102), 'No_Protocolo', 'Total_Pagar', 'Valor_Cpmf'," + 
				    	"'Valor_TabII6b', 'Valor_Tarif'a, 'Valor_Cartorio', 'Total_Deposito', 'No_Guia', 'Campo1'," + 
				    	"'Campo2', 'Campo3, 'Campo4', 'Campo5', 'Dt_Sistema', 'Usr_Deposito', 'CodigoBarra', " + 
				    	"'DinChq', 'InfCheque', 'Dt_Vencto', 'HoraEmissao', 'TabII', 'TabII6bLei3217'," + 
				    	"'TabII6bLei4664', 'TabII6bLei111', 'TabII6bLei6281', 'NossoNumero', 'SequenciaNoTitulo'," +
				    	"'DigitoCedente', 'CodigoEmpresa')";
		   		//
		   		for (MpBoleto mpBoletoX : mpBoletoList) {
		   			//
		   			if (!numOficio.equals(mpBoletoX.getCodigoOficio()))
		   				continue;
				    //
				    contBoleto++;
				    	
				    Gson gson = new Gson();
				    		
				    boletoJSON = boletoJSON + "\n" + gson.toJson(mpBoletoX);
				    //
				    String lineSQL = paramSQL;
				    	
				    lineSQL = lineSQL.replace("'Dt_Protocolo'", "'" + sdf.format(mpBoletoX.getDataDocumento()) + "'");
				    lineSQL = lineSQL.replace("'No_Protocolo'", "'" + mpBoletoX.getNossoNumero() + "'");
				   	lineSQL = lineSQL.replace("'Total_Pagar'", mpBoletoX.getValorDocumento().toString());
				   	lineSQL = lineSQL.replace("'Valor_Cpmf'", mpBoletoX.getValorDocumento().toString());
				   	lineSQL = lineSQL.replace("'Valor_TabII6b'", mpBoletoX.getValorDocumento().toString());
			    	lineSQL = lineSQL.replace("'Valor_Tarifa'", mpBoletoX.getValorDocumento().toString());
			    	lineSQL = lineSQL.replace("'Valor_Cartorio'", mpBoletoX.getValorDocumento().toString());
				    lineSQL = lineSQL.replace("'Total_Deposito'", mpBoletoX.getValorDocumento().toString());
				    lineSQL = lineSQL.replace("'No_Guia'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Campo1'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Campo2'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Campo3'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Campo4'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Campo5'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Dt_Sistema'", "'" + sdf.format(mpBoletoX.getDataDocumento()) + "'");
				    lineSQL = lineSQL.replace("'Usr_Deposito'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'CodigoBarra'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'DinChq'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'InfCheque'", "'" + mpBoletoX.getNossoNumero() + "'");
				    lineSQL = lineSQL.replace("'Dt_Vencimento'", "'" + sdf.format(mpBoletoX.getDataDocumento()) + "'");
				    //
				    boletoSQL = boletoSQL + "\n" + lineSQL;
				    //
				    MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = FOR ( " + boletoJSON);
		   		}		
		   		//
		   		if (contBoleto == 0) {
		   			//
				    MpAppUtil.PrintarLn("Nenhum Boleto Impresso... encontrado ! ( Of. = " + numOficio); 
		   		} else {
		   			//
				    MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = FOR ( Entrou Aqui 000-000");

//				    trataGravacaoArquivo(boletoJSON, "arquivos/mpBoletoImpresso_of" + numOficio + ".json");

		   			trataGravacaoArquivo(boletoSQL, "arquivos/mpBoletoImpresso_of" + numOficio + ".txt");
		   		
				    //
//				    MailMessage message = mpMailer.novaMensagem();
					//
					String[] mailList = mpSistemaConfigX.getValorT().split(",");
	
					InternetAddress[] recipientAddress = new InternetAddress[mailList.length];
					//
					int counter = 0;
							
					String mailTo = "";
					String mailCc = "";
					//
					for (String recipient : mailList) {
						//
						try {
							recipientAddress[counter] = new InternetAddress(recipient.trim());
						} catch (AddressException e) {
			    			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataEmailBoletoImpresso() 000 ( e = " + e);
							continue;
						}
						//
						if (counter == 0)
							mailTo = recipient;
						else
							if (counter == 1)
								mailCc = recipient;
							else
								mailCc = mailCc + " , " + recipient;
						//
						counter++;
					}
					//
//					message.to(mailTo)
//							.cc(mailCc)
//							.subject("MPXDS Robot.Job - Emissao de BOLETOs Impressos ...")
//							.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
//																			"/emails/mpBoletoImpresso.template")))
//							.put("locale", new Locale("pt", "BR"))
//							.charset("utf-8")
//							.addAttachment("boleto_impresso.txt", "application/txt",
//											ContentDisposition.ATTACHMENT, getClass().getResourceAsStream(this.pathArquivo))
//							.send();
//			   			//
//			   			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = Email 000 ( " + mpSistemaConfigX.getValorT());
					//
					try {
						sendEmailAnexo("1", mailTo, mailCc);
						//
					} catch (IOException e) {
						MpAppUtil.PrintarLn("MpBoletoImpressoJobBean 001 = Email IOException ( e = " + e);
					}
			   	}
			   	//
		   	}
		    //
		    MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = Passou AQUI - 000 "); 
		    //
	   	}
	    //
    }
	
	protected void trataGravacaoArquivo(String arquivo, String nomeArquivo) {
    	//
	    MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataGravacaoArquivo = Passou AQUI - 000 ( " + 
	    																	arquivo + " / " + nomeArquivo);
		//
        ClassLoader classLoader = getClass().getClassLoader();

        File fileX = new File(classLoader.getResource(nomeArquivo).getFile());

        // Get the absolute path.
		this.pathArquivo = fileX.getPath();
	    
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = File 000 ( pathArquivo = " + pathArquivo);

		if (pathArquivo.substring(0,1).contentEquals("/")) pathArquivo = pathArquivo.substring(1);
		
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = File 000 ( pathArquivo = " + pathArquivo);

//	    Properties prop = new Properties(); 	    	
//	    prop.load(ec.getResourceAsStream("/" + nomeArquivo));    	
		//
		try {
			InputStream stream = new ByteArrayInputStream(arquivo.getBytes());        	
				
			byte[] buffer;			        
			//
			buffer = new byte[stream.available()];
							
			stream.read(buffer);
			//						
			File targetFile = new File(pathArquivo);				        
			//
			OutputStream outStream = new FileOutputStream(targetFile);
				        
			outStream.write(buffer);
	
			outStream.close();
			
			stream.close();
			//
//			BufferedWriter writer = null;
//			//
//			try {
//				//
//			    writer = new BufferedWriter( new FileWriter(pathArquivo));
//
//			    writer.write(arquivo);
//			    //
//			}
//			catch (IOException e) {
//				//
//			}
//			finally {
//				//
//			    try {
//			    	//
//			        if ( writer != null) writer.close( );
//			        //
//			    } catch ( IOException e) {
//			    	//
//			    }
//			}			
			//
//			try (PrintStream out = new PrintStream(new FileOutputStream(pathArquivo))) {
//				//
//			    out.print(arquivo);
//			} catch (IOException e) {
//				MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataGravacaoArquivo ( Exception = " + e); 
//			}
			
//			try {
//				FileUtils.writeStringToFile(new File(pathArquivo), arquivo);
//			} catch (IOException e) {
//				MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataGravacaoArquivo ( Exception = " + e); 
//			}
			
//			// Files.newBufferedWriter() uses UTF-8 encoding by default
//	        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(pathArquivo))) {
//	            writer.write(arquivo);
//	    	} catch (Exception e) {
//	    		//
//				MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataGravacaoArquivo ( Exception = " + e); 
//			}
			//
			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = File 000.2 ( ENTROU !");
			//
		} catch (Exception e) {
			//
			MpAppUtil.PrintarLn("MpBoletoImpressoJobBean.trataGravacaoArquivo ( Exception = " + e); 
		}
		//
		MpAppUtil.PrintarLn("MpBoletoImpressoJobBean = File 000.3 ( ENTROU !");
		//
    }
    
	protected void sendEmailAnexo(String numOficio, String to, String cc) throws IOException {
		//
	      // Recipient's email ID needs to be mentioned.
//	      String to = "destinationemail@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "marcusvinciuspinheirorodrigues@gmail.com";

//	      final String username = "manishaspatil";//change accordingly
//	      final String password = "******";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
//	      String host = "relay.jangosmtp.net";

	      Properties props = new Properties();
	      
	      props.load(getClass().getResourceAsStream("/mail.properties"));
	      
	      props.put("mail.smtp.host", props.getProperty("mail.server.host"));
	      props.put("mail.smtp.port", props.getProperty("mail.server.port"));
	      props.put("mail.smtp.auth", props.getProperty("mail.auth"));
	      props.put("mail.smtp.ssl.enable", props.getProperty("mail.enable.ssl"));
//	      props.put("mail.smtp.starttls.enable", "true");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(props.getProperty("mail.username"), 
	            		   								props.getProperty("mail.password"));
	            }
	         });
	      //
	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.CC,
	            InternetAddress.parse(cc));

	         // Set Subject: header field
	         message.setSubject("MPXDS Robot.Job - Emissão de BOLETOs Impressos ...");

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("Email automático - Emissao de BOLETOs Impressos -" +
	        		 														" Favor proceder à importação !");
	         	         
	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
			
	         ClassLoader classLoader = getClass().getClassLoader();
	         
	         File fileX = new File(classLoader.getResource("arquivos/mpBoletoImpresso_of" + 
																			numOficio + ".txt").getFile());
			
	         String filename = fileX.getPath();
//	         String filename = "/home/manisha/file.txt";

	         DataSource source = new FileDataSource(filename);
	         
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName("boleto_impresso.txt");
	         
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}
    	
	// ---
	
	public String getDataJob() { return dataJob; }
	public void setDataJob(String dataJob) { this.dataJob = dataJob; }
      
}