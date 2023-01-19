package com.mpxds.mpbasic.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;
//import java.util.Locale;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
//import com.mpxds.mpbasic.util.mail.MpSendMail;
//import com.outjected.email.api.MailMessage;
//import com.outjected.email.impl.templating.velocity.VelocityTemplate;

// ---

public class MpMyJob implements Job {
	//
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

//	@Inject
//	private MpMailer mpMailer;
	
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		//		
		MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 000");		

		//	    MpSendMail mpSendMail = new MpSendMail("marcusvinciuspinheirorodrigues@gmail.com", 
//		"marcus_vpr@hotmail.com",
//		"MPXDS Robot.Job - Emissao de BOLETOs Impressos ...",
//		"Email Automático com Boletos Impressos em ANEXO!");
//
//	    mpSendMail.sendGmail();

		String numOficio = "1";
		
		List<MpSistemaConfig> mpSistemaConfigList = mpSistemaConfigs.porParametros("Of" + numOficio + "_EmailBoleto");
		
//		MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 000.1  ( Size.List = " + mpSistemaConfigList.size());		
		
    	if (mpSistemaConfigList.size() == 0) {
	    	//
			MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() ! " + 
												"Parâmetro não existe ! ( Of" + numOficio + "_EmailBoleto");
	   	} else {
		
//		    MailMessage message = mpMailer.novaMensagem();
			//
		    String mailListSC = "";
		    Integer iX = 0;
		    
		    for (MpSistemaConfig mpSistemaConfigX : mpSistemaConfigList) {
		    	//
		    	if (iX == 0)
		    		mailListSC = mpSistemaConfigX.getValorT();
		    	else
		    		mailListSC = "," + mpSistemaConfigX.getValorT();

		    	//
		    	iX++;
		    }
		    //
//			MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 000.2  ( mailListSC = " + mailListSC);		

			String[] mailList = mailListSC.split(",");
	
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
					//
				} catch (AddressException e) {
					MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 000.3  ( AddressException = " + e);		
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
//			MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 000.4  ( mailCc = " + mailCc);		

//			message.to(mailTo)
//					.cc(mailCc)
//					.subject("MPXDS Robot.Job - Emissao de BOLETOs Impressos ...")
//					.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
//																	"/emails/mpBoletoImpresso.template")))
//					.put("locale", new Locale("pt", "BR"))
//					.charset("utf-8")
	//				.addAttachment("boleto_impresso.json", "application/json",
	//								ContentDisposition.ATTACHMENT, getClass().getResourceAsStream(
	//																	targetFile.getAbsolutePath()))
//					.send();
			//
			try {
				sendEmailAnexo("1", mailTo, mailCc);
				//
			} catch (IOException e) {
				MpAppUtil.PrintarLn("MpMyJob = Email IOException ( e = " + e);
			}
			//
//			MpAppUtil.PrintarLn("MpMyJob = Email 000 ( mailListSC = " + mailListSC);
	   	}
		//
		MpAppUtil.PrintarLn("MpMyJob.execute() - Entrou 001");		
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
		
}