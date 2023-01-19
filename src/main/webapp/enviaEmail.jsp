<%@page import="java.io.*" %> 
<%@page import="java.util.*" %> 
<%@page import="javax.mail.internet.*" %> 
<%@page import="javax.mail.*" %> 
 
<%!
 
     private static final String SMTP_HOST_NAME = "smtp.protestorjcapital.com.br";
     private static final String SMTP_AUTH_USER = "suporte@protestorjcapital.com.br";
     private static final String SMTP_AUTH_PWD = "@SuporteMpxds2018";
 
     private static final String emailMsgTxt = "Controle de Mensagens via JSP.";
     private static final String emailSubjectTxt = "Subject da Mensagem";
     private static final String emailFromAddress = "suporte@protestorjcapital.com.br";
 
     // Inserir aqui os enderecos onde a mensagem sera entregue
 
     private static final String[] emailList = {"marcus_vpr@hotmail.com", "renato_rjx@hotmail.com"};
 
     public void postMail(String recipients[], String subject,
             String message, String from) throws MessagingException {
         boolean debug = true;
 
         //Setando o host
         Properties props = new Properties();
         props.put("mail.smtp.host", SMTP_HOST_NAME);
         props.put("mail.smtp.auth", "true");
 
         Authenticator auth = new SMTPAuthenticator();
         Session session = Session.getDefaultInstance(props, auth);
 
         session.setDebug(debug);
 
         // Criando a mensagem
         Message msg = new MimeMessage(session);
 
         // Setando o endereco
         InternetAddress addressFrom = new InternetAddress(from);
         msg.setFrom(addressFrom);
 
         InternetAddress[] addressTo = new InternetAddress[recipients.length];
         for (int i = 0; i < recipients.length; i++) {
             addressTo[i] = new InternetAddress(recipients[i]);
         }
         msg.setRecipients(Message.RecipientType.TO, addressTo);
 
 
         //Conteudo
         msg.setSubject(subject);
         msg.setContent(message, "text/plain");
         Transport.send(msg);
     }
 
     private class SMTPAuthenticator extends javax.mail.Authenticator {
 
         public PasswordAuthentication getPasswordAuthentication() {
             String username = SMTP_AUTH_USER;
             String password = SMTP_AUTH_PWD;
             return new PasswordAuthentication(username, password);
         }
     }
 
%>
 
<br/>
<%
postMail(emailList,emailSubjectTxt,emailMsgTxt,emailFromAddress );
session.invalidate();
%>