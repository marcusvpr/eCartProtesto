package com.mpxds.mpbasic.scheduler;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
 //import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;

import java.net.URL;
import java.net.URLDecoder;
//import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.util.MpAppUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
//import com.mpxds.mpbasic.util.mail.MpSendMail;
//import com.outjected.email.api.MailMessage;
//import com.outjected.email.impl.templating.velocity.VelocityTemplate;

//import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MpSchedulerJob implements Job {
	//
    private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private MpBoletos mpBoletos;	

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

//	private MpMailer mpMailer;
    
    public static final String PROP_SMTP_HOST = "smtp.gmail.com";
    public static final String PROP_SMTP_SERVER = "smtp.gmail.com";
    public static final String PROP_SMTP_PORT = "465";
    public static final String PROP_SMTP_SSL = "true";
    public static final String PROP_SMTP_AUTH = "true";
    public static final String PROP_SMTP_USERNAME = "marcusvinciuspinheirorodrigues@gmail.com";
    public static final String PROP_SMTP_PASSWORD = "brunna18";
     
    public static final String PROP_RECIPIENT = "marcus_vpr@hotmail.com";
    public static final String PROP_CC_RECIPIENT = "renato_rjx@hotmail.com";

    public static final String PROP_SENDER = "marcusvinciuspinheirorodrigues@gmail.com";

    public static final String PROP_REPLY_TO = "marcusvinciuspinheirorodrigues@gmail.com";

    // --- Email ...
    
    public static final String PROP_SUBJECT = "MPXDS Quartz - teste send email !";

    public static final String PROP_MESSAGE = "Isso é um teste ... MPXDS Quartz - teste send email !";

    public static final String PROP_CONTENT_TYPE = "text/html";

    // ---
    
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String diaSemJob;
	
	// -----------------------
	    
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//
		Date dataHoraJob = new Date();

		this.diaSemJob = MpAppUtil.diaSemana(dataHoraJob);

		MpAppUtil.PrintarLn("MpSchedulerJobForm.execute() = " + sdf.format(dataHoraJob) + " 12 " + diaSemJob);
		
		MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("indAtivaJob");
		if (null == mpSistemaConfig) {
			MpAppUtil.PrintarLn("MpSchedulerJobForm.execute() - 001 (mpSistemaConfig = NULL");
			return; // Não executa !
		} else { 
			if (!mpSistemaConfig.getIndValor()) {
				MpAppUtil.PrintarLn("MpSchedulerJobForm.execute() - 002 " + 
							"(mpSistemaConfig.IndValor = " + mpSistemaConfig.getIndValor());
				return; // Não executa !
			}
		}
		
		MpAppUtil.PrintarLn("MpSchedulerJobForm.execute() = Entrou 000");
		
		// Verificar Envio de Email co Boletos Impressos no DIA !
		try {
			//
			trataEmailBoletoImpresso("1");
			//
		} catch (Exception e) {
			//
        	JobExecutionException e2 =
            		new JobExecutionException(e);
            	// this job will refire immediately
            	e2.refireImmediately();
            	throw e2;
        }

		//
//		JobDataMap data = context.getMergedJobDataMap();
//
//        MailInfo mailInfo = populateMailInfo(data, createMailInfo());
//        
//        getLog().info("Sending message " + mailInfo);
//
//        try {
//            MimeMessage mimeMessage = prepareMimeMessage(mailInfo);
//            
//            Transport.send(mimeMessage);
//            //
//        } catch (MessagingException e) {
//        	//
//            throw new JobExecutionException("Unable to send mail: " + mailInfo,
//                    e, false);
//        }
        //
    }
	
    protected Logger getLog() { return log; }

    protected MimeMessage prepareMimeMessage(MailInfo mailInfo)  throws MessagingException {
    	//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.prepareMimeMessage() - 000 ");

		Session session = getMailSession(mailInfo);

        MimeMessage mimeMessage = new MimeMessage(session);

        Address[] toAddresses = InternetAddress.parse(mailInfo.getTo());
        
        mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);

        if (mailInfo.getCc() != null) {
        	//
            Address[] ccAddresses = InternetAddress.parse(mailInfo.getCc());
            
            mimeMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
        }

        mimeMessage.setFrom(new InternetAddress(mailInfo.getFrom()));
        
        if (mailInfo.getReplyTo() != null) {
            mimeMessage.setReplyTo(new InternetAddress[]{new InternetAddress(mailInfo.getReplyTo())});
        }
        
        mimeMessage.setSubject(mailInfo.getSubject());
        
        mimeMessage.setSentDate(new Date());

        setMimeMessageContent(mimeMessage, mailInfo);

        return mimeMessage;
    }
    
    protected void setMimeMessageContent(MimeMessage mimeMessage, MailInfo mailInfo) throws MessagingException {
    	//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.setMimeMessageContent() - 000 ");

		if (mailInfo.getContentType() == null) {
            mimeMessage.setText(mailInfo.getMessage());
        } else {
            mimeMessage.setContent(mailInfo.getMessage(), mailInfo.getContentType());
        }
    }

    protected Session getMailSession(MailInfo mailInfo) throws MessagingException {
    	//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.getMailSession() - 000 ");

		Properties properties = new Properties();
        
        properties.put("mail.smtp.host", mailInfo.getSmtpHost());
        properties.put("mail.smtp.server", mailInfo.getSmtpServer());
        properties.put("mail.server.port", mailInfo.getSmtpPort());
        properties.put("mail.enable.ssl", mailInfo.getSmtpSsl());
        properties.put("mail.auth", mailInfo.getSmtpAuth());
        properties.put("mail.username", mailInfo.getSmtpUsername());
        properties.put("mail.password", mailInfo.getSmtpPassword());
        
        return Session.getDefaultInstance(properties, null);
    }
    
    protected MailInfo createMailInfo() {
    	//
        return new MailInfo();
    }
    
    protected MailInfo populateMailInfo(JobDataMap data, MailInfo mailInfo) {
    	//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.populateMailInfo() - 000 ");

		// Required parameters
        mailInfo.setSmtpHost(getRequiredParm(data, PROP_SMTP_HOST, "PROP_SMTP_HOST"));
        mailInfo.setSmtpServer(getRequiredParm(data, PROP_SMTP_SERVER, "PROP_SMTP_SERVER"));
        mailInfo.setSmtpPort(getRequiredParm(data, PROP_SMTP_PORT, "PROP_SMTP_PORT"));
        mailInfo.setSmtpSsl(getRequiredParm(data, PROP_SMTP_SSL, "PROP_SMTP_SSL"));
        mailInfo.setSmtpAuth(getRequiredParm(data, PROP_SMTP_AUTH, "PROP_SMTP_AUTH"));
        mailInfo.setSmtpUsername(getRequiredParm(data, PROP_SMTP_USERNAME, "PROP_SMTP_USERNAME"));
        mailInfo.setSmtpPassword(getRequiredParm(data, PROP_SMTP_PASSWORD, "PROP_SMTP_PASSWORD"));
        
        mailInfo.setTo(getRequiredParm(data, PROP_RECIPIENT, "PROP_RECIPIENT"));
        mailInfo.setFrom(getRequiredParm(data, PROP_SENDER, "PROP_SENDER"));
        mailInfo.setSubject(getRequiredParm(data, PROP_SUBJECT, "PROP_SUBJECT"));
        mailInfo.setMessage(getRequiredParm(data, PROP_MESSAGE, "PROP_MESSAGE"));
        
        // Optional parameters
        mailInfo.setReplyTo(getOptionalParm(data, PROP_REPLY_TO));
        mailInfo.setCc(getOptionalParm(data, PROP_CC_RECIPIENT));
        mailInfo.setContentType(getOptionalParm(data, PROP_CONTENT_TYPE));
        
        return mailInfo;
    }
    
    
    protected String getRequiredParm(JobDataMap data, String property, String constantName) {
    	//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.getRequiredParm() - 000 ");
    	
        String value = getOptionalParm(data, property);
        
        if (value == null) {
            throw new IllegalArgumentException(constantName + " not specified.");
        }
        //
        return value;
    }
    
    protected String getOptionalParm(JobDataMap data, String property) {
    	//
        String value = data.getString(property);
        
        if ((value != null) && (value.trim().length() == 0)) {
            return null;
        }
        
        return value;
    }
    
    protected static class MailInfo {
    	//
		private String smtpHost;
        private String smtpServer;
        private String smtpPort;
        private String smtpSsl;
        private String smtpAuth;
        private String smtpUsername;
        private String smtpPassword;
        
        private String to;
        private String from;
        private String subject;
        private String message;
        private String replyTo;
        private String cc;
        private String contentType;

        @Override
        public String toString() {
            return "'" + getSubject() + "' to: " + getTo();
        }
        
        public String getCc() { return cc; }
        public void setCc(String cc) { this.cc = cc; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }

        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getReplyTo() { return replyTo; }
        public void setReplyTo(String replyTo) { this.replyTo = replyTo; }

        public String getSmtpHost() { return smtpHost; }
        public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }

        public String getSmtpServer() { return smtpServer; }
        public void setSmtpServer(String smtpServer) { this.smtpServer = smtpServer; }

        public String getSmtpPort() { return smtpPort; }
        public void setSmtpPort(String smtpPort) { this.smtpPort = smtpPort; }

        public String getSmtpSsl() { return smtpSsl; }
        public void setSmtpSsl(String smtpSsl) { this.smtpSsl = smtpSsl; }

        public String getSmtpAuth() { return smtpAuth; }
        public void setSmtpAuth(String smtpAuth) { this.smtpAuth = smtpAuth; }
        
        public String getSmtpUsername() { return smtpUsername; }
        public void setSmtpUsername(String smtpUsername) { this.smtpUsername = smtpUsername; }

        public String getSmtpPassword() { return smtpPassword; }
        public void setSmtpPassword(String smtpPassword) { this.smtpPassword = smtpPassword; }

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }

        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        //
    }
    
    protected void trataEmailBoletoImpresso(String numOficio) throws Exception {
		//
		MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() - 000 ( " + numOficio);
    		
    	MpCartorioOficio mpCartorioOficio = MpCartorioOficio.valueOf("Of"+ numOficio);
    	if (null ==mpCartorioOficio) {
    		//
        	MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() - Erro ( Of" + numOficio +
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
	   		dtBoleto = sdfHHMM.parse(mpCartorioOficio.getHorarioGeracaoBoleto().substring(0, 5));
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
		MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() - 001 ( At = " + sdf.format(dtAtual) +
					" / Bol = " + sdf.format(dtBoleto) + " / Bol+1 = " + sdf.format(dtAtualF));
		//
	    if ((dtBoleto.after(dtAtual) && dtBoleto.before(dtAtualF))) {
	    	// Trata envio Email com Boletos Impressos !
			MpSistemaConfig mpSistemaConfigX = mpSistemaConfigs.porParametro("Of" + numOficio + "_EmailBoleto");
	    	if (null == mpSistemaConfigX) {
		    	//
    			MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() ! " + 
    												"Parâmetro não existe ! ( Of" + numOficio + "_EmailBoleto");
    			return;
		   	} else {
				//
		   		Integer contBoleto = 0;
			        
		   		List<MpBoleto> mpBoletoList = mpBoletos.mpBoletoByImpressaoList();
				    
		   		String boletoJSON = "";
		   		String boletoTXT = "";
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
				    MpAppUtil.PrintarLn("MpSchedulerJob = FOR ( " + boletoJSON);
		   		}		
		   		//
		   		if (contBoleto == 0) {
		   			//
				    MpAppUtil.PrintarLn("Nenhum Boleto Impresso... encontrado ! ( Of. = " + numOficio); 
		   		} else {
		   			//
				    MpAppUtil.PrintarLn("MpSchedulerJob = FOR ( Entrou Aqui 000-000");

//				    trataGravacaoArquivo(boletoJSON, "arquivos/mpBoletoImpresso_of" + numOficio + ".json");

		   			trataGravacaoArquivo(boletoTXT, "arquivos/mpBoletoImpresso_of" + numOficio + ".txt");
		   		
			    //
//			    MpSendMail mpSendMail = new MpSendMail("marcusvinciuspinheirorodrigues@gmail.com", 
//			    										mpSistemaConfigX.getValorT(),
//			    										"MPXDS Robot.Job - Emissao de BOLETOs Impressos ...",
//			    										"Email Automático com Boletos Impressos em ANEXO!");
//			    mpSendMail.sendGmail();
			    //
//			    MailMessage message = mpMailer.novaMensagem();
//				//
//				String[] mailList = mpSistemaConfigX.getValorT().split(",");
//
//				InternetAddress[] recipientAddress = new InternetAddress[mailList.length];
//				//
//				int counter = 0;
//						
//				String mailTo = "";
//				String mailCc = "";
//				//
//				for (String recipient : mailList) {
//					//
//					recipientAddress[counter] = new InternetAddress(recipient.trim());
//					//
//					if (counter == 0)
//						mailTo = recipient;
//					else
//						if (counter == 1)
//							mailCc = recipient;
//						else
//							mailCc = mailCc + " , " + recipient;
//					//
//					counter++;
//				}
				//
//				message.to(mailTo)
//						.cc(mailCc)
//						.subject("MPXDS Robot.Job - Emissao de BOLETOs Impressos ...")
//						.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
//																		"/emails/mpBoletoImpresso.template")))
//						.put("locale", new Locale("pt", "BR"))
//						.charset("utf-8")
//						.addAttachment("boleto_impresso.json", "application/json",
//										ContentDisposition.ATTACHMENT, getClass().getResourceAsStream(
//																			targetFile.getAbsolutePath()))
//						.send();
		   			//
		   			MpAppUtil.PrintarLn("MpSchedulerJob = Email 000 ( " + mpSistemaConfigX.getValorT());
		   		}
		   		//
		   	}
		    //
		    MpAppUtil.PrintarLn("MpSchedulerJob = Passou AQUI - 000 "); 
		    //
	   	}
	    //
    }
    
    protected void trataGravacaoArquivo(String arquivo, String nomeArquivo) {
    	//
	    MpAppUtil.PrintarLn("MpSchedulerJob.trataGravacaoArquivo = Passou AQUI - 000 ( " + 
	    																	arquivo + " / " + nomeArquivo);
		//
	    URL resource = Thread.currentThread().getContextClassLoader().getResource(nomeArquivo);
	    // Get the absolute path.
	    @SuppressWarnings("deprecation")
		String pathArquivo = (resource == null) ? nomeArquivo : URLDecoder.decode(resource.getFile()); ;
	    
		MpAppUtil.PrintarLn("MpSchedulerJob = File 000 ( pathArquivo = " + pathArquivo);

		if (pathArquivo.substring(0,1).contentEquals("/")) pathArquivo = pathArquivo.substring(1);
		
		MpAppUtil.PrintarLn("MpSchedulerJob = File 000 ( pathArquivo = " + pathArquivo);

//	    Properties prop = new Properties(); 	    	
//	    prop.load(ec.getResourceAsStream("/" + nomeArquivo));    	
		//
//	    Path path = null;
	    	
		try {
//			path = Files.write( Paths.get(pathArquivo), arquivo.getBytes(), StandardOpenOption.CREATE);
				
//			InputStream stream = new ByteArrayInputStream(arquivo.getBytes());        	
//				
//			byte[] buffer;			        
//			//
//			buffer = new byte[stream.available()];
//							
//			stream.read(buffer);
//			//						
//			File targetFile = new File(pathArquivo);				        
//			//
//			OutputStream outStream = new FileOutputStream(targetFile);
//					        
//			outStream.write(buffer);
//	
//			outStream.close();
//			
//			stream.close();
			
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
//			}
			
//			FileUtils.writeStringToFile(new File(pathArquivo), arquivo);
			
			// Files.newBufferedWriter() uses UTF-8 encoding by default
	        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(pathArquivo))) {
	            writer.write(arquivo);
	        } 			
			//
			MpAppUtil.PrintarLn("MpSchedulerJob = File 000.2 ( ENTROU !");
			//
		} catch (Exception e) {
			//
			MpAppUtil.PrintarLn("MpSchedulerJob.trataGravacaoArquivo ( Exception = " + e); 
		}
		//
		MpAppUtil.PrintarLn("MpSchedulerJob = File 000.3 ( ENTROU !");
		//
    }
    
}
