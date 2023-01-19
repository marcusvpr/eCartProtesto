package com.mpxds.mpbasic.util.mail;

import java.util.Date;
//
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.mpxds.mpbasic.util.MpAppUtil;

public class MpSendMailLOCAWEB {
	//    
	public MpSendMailLOCAWEB(String recipients[], String subject,
            					String message, String from) throws MessagingException {
		//
	    MpAppUtil.PrintarLn("MpSendMailLOCAWEB - 000");
	    
        boolean debug = false;
        //Setando o host
        Properties props = new Properties();
//      props.put("mail.smtp.host", "smtp.protestorjcapital.com.br");
        props.put("mail.smtp.host", "email-ssl.com.br");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new MpSMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);

        session.setDebug(debug);

        // Criando a mensagem
        Message msg = new MimeMessage(session);

        // Setando o endereco
        InternetAddress addressFrom = new InternetAddress(from);
        
        msg.setFrom(addressFrom);
        //
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
        	if (null==recipients[i] || recipients[i].isEmpty()) continue; 
        	//
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        //Conteudo
        msg.setSubject(subject);
        msg.setContent(message, "text/html; charset=UTF-8");
        
        Transport.send(msg);
    }
	
	public MpSendMailLOCAWEB(String recipients[], String subject, String message, String from, String pathFileX)
																						throws MessagingException {
		//
		MpAppUtil.PrintarLn("MpSendMailLOCAWEB file Anexo - 000 ( " + pathFileX);

		boolean debug = false;
		// Setando o host
		Properties props = new Properties();
		// props.put("mail.smtp.host", "smtp.protestorjcapital.com.br");
		props.put("mail.smtp.host", "email-ssl.com.br");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new MpSMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		// Criando a mensagem
		Message msg = new MimeMessage(session);

		// Setando o endereco
		InternetAddress addressFrom = new InternetAddress(from);
		
		msg.setFrom(addressFrom);
		//
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			if (null == recipients[i] || recipients[i].isEmpty())
				continue;
			//
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Conteudo
		msg.setSubject(subject);
		msg.setContent(message, "text/html; charset=UTF-8");

		//
		// create and fill the first message part
		 
		MimeBodyPart messageBodyPart1 = new MimeBodyPart();
		 
//		messageBodyPart1.setText(message);
		messageBodyPart1.setContent(message, "text/html; charset=UTF-8");
		 
		// create the second message part
		 
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		 
		// attach the file to the message
		 
		FileDataSource fdatasource = new FileDataSource(pathFileX);
		 
		messageBodyPart2.setDataHandler(new DataHandler(fdatasource));
		 
		messageBodyPart2.setFileName(fdatasource.getName());
		 
		// create the Multipart and add its parts to it
		 
		Multipart mpart = new MimeMultipart();
		 
		mpart.addBodyPart(messageBodyPart1);
		 
		mpart.addBodyPart(messageBodyPart2);
		 
		// add the Multipart to the message
		 
		msg.setContent(mpart);
		 
		// set the Date: header
		 
		msg.setSentDate(new Date());
		 
		//
	    
		Transport.send(msg);
	}	
	
}