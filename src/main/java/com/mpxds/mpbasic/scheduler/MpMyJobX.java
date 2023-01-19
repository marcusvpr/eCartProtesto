package com.mpxds.mpbasic.scheduler;

import java.util.Date;
import java.util.EmptyStackException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
//import java.security.cert.Certificate;
import java.io.*;

import javax.inject.Inject;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLPeerUnverifiedException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.util.MpAppUtil;

// ---

public class MpMyJobX implements Job {
	//
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String https_url;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//
	    MpAppUtil.PrintarLn("MpMyJobX.execute - 000 ( Data = " + sdf.format(new Date()));

//	    String fileName = "arquivos/mpBoletoImpresso_of1.json";
//	    
//      URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
//        
//     	// Get the absolute path.
//      String filePath = (resource == null) ? fileName : URLDecoder.decode(resource.getFile()); ;
        //	    
//	    MpAppUtil.PrintarLn("MpMyJobX.execute = Passou AQUI - 001 ( filePath = " + filePath);

		MpSistemaConfig mpSistemaConfig = this.mpSistemaConfigs.porParametro("urlBoletoImpressoJob");
		if (null == mpSistemaConfig) {
			//
			MpAppUtil.PrintarLn("MpMyJobX.execute() - 001 (mpSistemaConfig = NULL urlBoletoImpressoJob");			
			throw new EmptyStackException();
		}
		//
//		MpAppUtil.PrintarLn("MpMyJobX.execute() - 002 " + 
//							"(mpSistemaConfig.valort = " + mpSistemaConfig.getValorT());	    
	    //
		https_url = mpSistemaConfig.getValorT(); // HttpsURLConnection!!! = "https://www.google.com/";
	    
	    this.chamaURL();
	    //
	}
	    
	private void chamaURL() {
		//		        
		URL url;
		//
		try {
			//
			url = new URL(https_url);

//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//			MpAppUtil.PrintarLn("MpMyJobX.testIt() - Entrou.000 ");

			//dump all the content
//			this.print_content(con);
			//
		} catch (MalformedURLException e) {
			MpAppUtil.PrintarLn("MpMyJobX.testIt() - MalformedURLException = " + e);
		} catch (IOException e) {
			MpAppUtil.PrintarLn("MpMyJobX.testIt() - IOException = " + e);
		}
		//
	}
		     	
	private void print_content(HttpURLConnection con) {
		//
		if (con != null) {
			//
		  	try {
		  	   System.out.println("****** Content of the URL ********");

		  	   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
		  	   String input;
		  	   //
		  	   while ((input = br.readLine()) != null) {
		  		   //
		  	      System.out.println(input);
		  	   }
		  	   br.close();
		  	   //
		  	} catch (IOException e) {
				MpAppUtil.PrintarLn("MpMyJobX.testIt() - IOException = " + e);
		  	}
		  	//
		}
	}
	     
}