package com.mpxds.mpbasic.scheduler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.internet.InternetAddress;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

//import org.apache.deltaspike.scheduler.api.Scheduled;

import com.google.gson.Gson;
import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
//import com.mpxds.mpbasic.util.mail.MpMailer;
import com.mpxds.mpbasic.util.mail.MpSendMailLOCAWEB;
//import com.outjected.email.api.ContentDisposition;
//import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

//@Scheduled(cronExpression = "0 */30 * ? * *") 
public class MpSchedulerJobX implements Job {
	//
	@Inject
	private MpBoletos mpBoletos;	

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

//	@Inject
//	private MpMailer mpMailer;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");

	private String diaSemJob;
	
//	private String pathFileX = "/home/usuario-ftp/boletos_impressos/of";
	private String pathFileX = "C:\\temp\\ftp-protestorjcapital\\mov\\boletos_impressos\\of";
	
	// ---
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//
		Date dataHoraJob = new Date();

		this.diaSemJob = MpAppUtil.diaSemana(dataHoraJob);

		MpAppUtil.PrintarLn("MpSchedulerJobForm.execute() = " + sdf.format(dataHoraJob) + " 12 " + diaSemJob);

		// ======================================

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
		
		// Verificar Envio de Email co Boletos Impressos no DIA !
		this.trataEmailBoletoImpresso("1");
		this.trataEmailBoletoImpresso("2");
		this.trataEmailBoletoImpresso("3");
		this.trataEmailBoletoImpresso("4");
		//
	}
	
    private void trataEmailBoletoImpresso(String numOficio) {
		//
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
	    try {
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
//			MpAppUtil.PrintarLn("MpSchedulerJobForm.trataEmailBoletoImpresso() - 001 ( At = " + sdf.format(dtAtual) +
//					" / Bol = " + sdf.format(dtBoleto) + " / Bol+1 = " + sdf.format(dtAtualF));
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
				    //
					String newLineX = "\r\n"; // System.getProperty("line.separator").toString();
				    
				    String boletoJSON = "";
				    String boletoTXT = "";
				    String boletoSQL = "";
				    
				    String boletoALL = "[HEADER, Data = " + sdf.format(new Date() + ", Total Registros = " + 
																				mpBoletoList.size() + " ]" + newLineX);
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
					String msg = "";

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
				    	
				    	lineSQL = lineSQL.replace("'Dt_Protocolo'", "'" + sdf.format(
				    														mpBoletoX.getDataDocumento()) + "'");
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
				    	lineSQL = lineSQL.replace("'Dt_Sistema'", "'" + sdf.format(
																				mpBoletoX.getDataDocumento()) + "'");
				    	lineSQL = lineSQL.replace("'Usr_Deposito'", "'" + mpBoletoX.getNossoNumero() + "'");
				    	lineSQL = lineSQL.replace("'CodigoBarra'", "'" + mpBoletoX.getNossoNumero() + "'");
				    	lineSQL = lineSQL.replace("'DinChq'", "'" + mpBoletoX.getNossoNumero() + "'");
				    	lineSQL = lineSQL.replace("'InfCheque'", "'" + mpBoletoX.getNossoNumero() + "'");
				    	lineSQL = lineSQL.replace("'Dt_Vencimento'", "'" + sdf.format(
																				mpBoletoX.getDataDocumento()) + "'");
				    	//
				    	boletoSQL = boletoSQL + "\n" + lineSQL;
				    	//
			    		msg = "";
			    		
			    		String dataProtocolo = "";
			    	    String numeroProtocolo = "";
			    	    //                    0123456789012345678901234567890123456789012345678901234
			    	    // Ex.: nome_sacado = ROBERTO RIBEIRO DOS SANTOS Protocolo: 06/04/2018-020198
			    	    //                                               0123456789012345678901234567890123456789012345678901234
			    	    Integer posProtocolo = mpBoletoX.getNomeSacado().indexOf("Protocolo:");
		    	    	if (posProtocolo >= 0) {
			    	    	//
		    	    	    if (mpBoletoX.getNomeSacado().trim().length() == posProtocolo + 28) {
			    	    		dataProtocolo = mpBoletoX.getNomeSacado().substring(posProtocolo + 11, posProtocolo + 11 + 10);
			    	    		numeroProtocolo = mpBoletoX.getNomeSacado().substring(posProtocolo + 11 + 11, 
			    	    																			posProtocolo + 11 + 11 + 6);
			    	    	} else
			    				msg = "Error : Captura Data/Numero Protocolo (Tam.=28)! Favor Verificar! ( ";
		    	    	} else
		    				msg = "Error : Captura Data/Numero Protocolo (Protocolo:)! Favor Verificar ! (";    	    		
		    	    	//
		    			if (!msg.isEmpty()) {
		    				MpAppUtil.PrintarLn("MpSchedulerJob - Error : " + msg + mpBoletoX.getNomeSacado());
		    				return;
		    			}
			    		//    			
		    			String dataVencimento = sdfDMY.format(mpBoletoX.getDataVencimento());
		    			if ( mpBoletoX.getIndApos16h().equals("*"))
		        			dataVencimento = sdfDMY.format(mpBoletoX.getDataVencimento1());    				
		    			//
				    	boletoTXT = "[\"" + dataProtocolo + "\", " +
				    				 "\"" + numeroProtocolo + "\", " +
				    				 "\"" + mpBoletoX.getNumeroGuiaGerado() + "\", " +
								 	 "\"" + sdf.format(mpBoletoX.getDataImpressao()) + "\", " +
									 "\"" + dataVencimento + "\"]" + newLineX;
			    		//
			    		boletoALL = boletoALL + boletoTXT;
				    	//
				    	MpAppUtil.PrintarLn("MpSchedulerJob = FOR ( " + boletoJSON);
				    }		
				    //
				    if (contBoleto == 0) {
				    	//
				    	MpAppUtil.PrintarLn("Nenhum Boleto Impresso... encontrado ! ( Of. = " + numOficio); 
			        	return;
				    }
				    //
				    boletoALL = "[TRAILLER, Data = " + sdf.format(new Date() + ", Total Registros = " + 
																				mpBoletoList.size() + " ]" + newLineX);
				    //		
			        InputStream stream = new ByteArrayInputStream(boletoJSON.getBytes());  	
			        InputStream stream1 = new ByteArrayInputStream(boletoALL.getBytes());  	
						
			        byte[] buffer;
			        byte[] buffer1;
			        
			        File targetFile = new File("xxx");
			        File targetFile1 = new File("xxx");
			        //
					try {
						buffer = new byte[stream.available()];
						buffer1 = new byte[stream1.available()];
						
				        stream.read(buffer);
				        stream1.read(buffer1);
				        //
						ClassLoader classLoader = getClass().getClassLoader();
						
						targetFile = new File(classLoader.getResource("arquivos/mpBoletoImpresso_of" + 
																				numOficio + ".json").getFile());				        
						targetFile1 = new File(pathFileX + numOficio + ".txt");				        
						//
				        OutputStream outStream = new FileOutputStream(targetFile);
				        OutputStream outStream1 = new FileOutputStream(targetFile1);
				        
				        outStream.write(buffer);
				        outStream1.write(buffer1);

				        outStream.close();
				        outStream1.close();
				        //
					} catch (IOException e) {
						e.printStackTrace();
					}
			        //
			    	MpAppUtil.PrintarLn("MpSchedulerJob = File ( " + mpSistemaConfigX.getValorT() + " / " + 
		    				targetFile.getAbsolutePath() + " / " + targetFile.getPath());
					//
			        InputStream streamTXT = new ByteArrayInputStream(boletoTXT.getBytes());        	

					try {
						buffer = new byte[streamTXT.available()];
						
				        stream.read(buffer);
				        //
						ClassLoader classLoader = getClass().getClassLoader();
						
						targetFile = new File(classLoader.getResource("arquivos/mpBoletoImpresso_of" + 
																				numOficio + ".txt").getFile());				        
						//
				        OutputStream outStream = new FileOutputStream(targetFile);
				        
				        outStream.write(buffer);

				        outStream.close();
				        //
					} catch (IOException e) {
						e.printStackTrace();
					}
			        //
			    	MpAppUtil.PrintarLn("MpSchedulerJob = FileTXT ( " + mpSistemaConfigX.getValorT() + " / " + 
		    				targetFile.getAbsolutePath() + " / " + targetFile.getPath());
				    //
					try {
						//
//						MailMessage message = mpMailer.novaMensagem();
						//
						String[] mailList = mpSistemaConfigX.getValorT().split(",");

						InternetAddress[] recipientAddress = new InternetAddress[mailList.length];
						//
						int counter = 0;
						
						String mailTo = "";
						String mailCc = "";
						//
						for (String recipient : mailList) {
						    recipientAddress[counter] = new InternetAddress(recipient.trim());
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
						this.enviaBoletoImpressoLOCAWEB(mailList);
						
//						message.to(mailTo)
//								.cc(mailCc)
//								.subject("MPXDS - Emissao de BOLETOs Impressos...")
//								.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
//																			"/emails/mpBoletoImpresso.template")))
//								.put("locale", new Locale("pt", "BR"))
//								.charset("utf-8")
//								.addAttachment("boleto_impresso.json", "application/json",
//												ContentDisposition.ATTACHMENT, getClass().getResourceAsStream(
//																				targetFile.getAbsolutePath()))
//								.send();
						//
				    	MpAppUtil.PrintarLn("MpSchedulerJob = Email ( " + mpSistemaConfigX.getValorT() +
			    				targetFile.getAbsolutePath() + " / " + targetFile.getPath());

					} catch (Exception e) {
						//
						e.printStackTrace();

//						MpAppUtil.PrintarLn("MpSchedulerJob = ( " +
//							"Error-003 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " + e);
					}
		   		}
		    	//
		    	MpAppUtil.PrintarLn("MpSchedulerJob = Passou AQUI - 000 "); 
		    	//
	   		}
		    //
		} catch (ParseException e) {
			//
			MpAppUtil.PrintarLn("Error.001 - Tratamento Horas - " + numOficio + 
					"o. Oficio ! Contactar Suporte Técnico ! ( h = " + 
					mpCartorioOficio.getHorarioGeracaoBoleto().substring(0, 5) + " / hA = " + 
					sdf.format(dtAtual));
			return;
		}

    }

	public Boolean enviaBoletoImpressoLOCAWEB(String[] emailList) {
		//
		
		String subject = "Emissao de BOLETOs Impressos...";
		
	    Map<String,Object> mapX = new HashMap<String,Object>(); 
	    
	    mapX.put("locale", new Locale("pt-BR", "pt-BR"));

	    String message = new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpBoletoImpresso.template")).merge(mapX).toString();
		
		String from = "suporte@protestorjcapital.com.br";
		
		try {
			new MpSendMailLOCAWEB(emailList, subject, message, from);
			//
			return true;
		} catch (Exception e) {
			//
			MpFacesUtil.addErrorMessage("Error-003 (Envio E-mail). Favor contactar o Suporte Técnico ! ! ( e= " +
																										e.toString());
			System.out.println("MpSchedulerJobX() - Error-003 (Envio E-mail LOCAWEB) ( e= " + e.toString());
			//
			return false;
		}
	}
    
}