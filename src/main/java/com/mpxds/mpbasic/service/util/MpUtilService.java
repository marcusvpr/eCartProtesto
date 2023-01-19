package com.mpxds.mpbasic.service.util;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.enums.MpTipoCampo;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;

//import io.minio.MinioClient;
//import io.minio.UploadObjectArgs;

@Service
public class MpUtilService implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
	
	// ---
	
	public MpSistemaConfig buscaMpConsig(String paramX) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramX);
		//
		return mpSistemaConfig;
	}

	public Object buscaMpConsigValor(String paramX, MpTipoCampo mpTipoCampo) {
		//
		Object objX = null;
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramX);
		if (null == mpSistemaConfig)
			return objX;		
		//
		return mpSistemaConfig.obterValor(mpTipoCampo);
	}
	
	// --- Intima21 !

	public Boolean buscaAmbienteProducaoIntima21() {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaIntima21");
		
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			String[] parms = mpSistemaConfig.getValorT().split(";");
			
			for (String parm : parms) {
				//
				if (parm.indexOf("ambienteProducao=true") >= 0)
					return true;
			}
		}
		//
		return false;
	}
	
	public String buscaUrlIntima21(Boolean indProducao) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaIntima21");
		
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			String[] parms = mpSistemaConfig.getValorT().split(";");
			
			for (String parm : parms) {
				//
				if (indProducao) {
					//                01234567
					if (parm.indexOf("urlPRD=") >= 0)
						return parm.substring(7);
				} else
					if (parm.indexOf("urlDSV=") >= 0)
						return parm.substring(7);
			}
		}
		//
		return "";
	}
	
	public String buscaUserPswIntima21(String numeroOficio) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaIntima21");
		
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			String[] parms = mpSistemaConfig.getValorT().split(";");
			
			for (String parm : parms) { // OFX=XXXXX/YYYYYY onde XXXXX=Usuário e YYYYY=Senha !
				//                                            01234				
				if ( (numeroOficio.equals("1") && parm.indexOf("OF1=") >= 0)
			    ||   (numeroOficio.equals("2") && parm.indexOf("OF2=") >= 0)
			    ||   (numeroOficio.equals("3") && parm.indexOf("OF3=") >= 0)
			    ||   (numeroOficio.equals("4") && parm.indexOf("OF4=") >= 0) )
					return parm.substring(4);
			}
		}
		//
		return "";
	}

	// Trata Mensagens Registro Boleto Intimação !
	
	public String buscaMensagemRegistroBoleto(String numeroOficio) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + numeroOficio + 
																						"_mensagemRegistroBoleto");	
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			return mpSistemaConfig.getValorT().trim();
		}
		//
		return "";
	}

	public void gravaMensagemRegistroBoleto(String numeroOficio, String mensagem) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + numeroOficio + 
																						"_mensagemRegistroBoleto");	
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			// Trunca Mensagem de Erro !
			if (mensagem.length() > 1001)
				mensagem = mensagem.substring(0, 1000);
			//
			mpSistemaConfig.setValorT(mensagem);

			mpSistemaConfigs.guardar(mpSistemaConfig);
		}
		//
	}
	
	public String buscaAndamentoRegistroBoleto(String numeroOficio) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + numeroOficio + 
																						"_andamentoRegistroBoleto");	
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			//
			return mpSistemaConfig.getValorT().trim();
		}
		//
		return "";
	}

	public void gravaAndamentoRegistroBoleto(String numeroOficio, String mensagem) {
		//
		MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + numeroOficio + 
																						"_andamentoRegistroBoleto");	
		if (null == mpSistemaConfig) 
			assert(true); //nop
		else {
			// Trunca Mensagem de Erro !
			if (mensagem.length() > 1001)
				mensagem = mensagem.substring(0, 1000);
			//
			mpSistemaConfig.setValorT(mensagem);

			mpSistemaConfigs.guardar(mpSistemaConfig);
		}
		//
	}
		
//    public String fileUploader(String pathX, String arquivoX) {
//    	//
//		if (pathX.isEmpty()) pathX = "C:\\MPXDS\\s3-minio"; 			
//		if (arquivoX.isEmpty()) arquivoX = "0756_20200716_CE_202007160931022752.PDF";
//		
//		String msgX = "";    	
//		
//		System.out.println("FileUploader pathX = " + pathX + " / arquivoX = " + arquivoX);    	
//    	//
//        try {
//        	//
//            MinioClient minioClient = MinioClient.builder()
//	            				.endpoint("http://200.150.196.119")
//								.credentials("PRBWG6g5T2", "G0Rk7Ls5c6")
//								.build();
////            // Check if the bucket already exists.
////            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
////            					.bucket("public-protrjcapital")
////            					.build());
////            if (isExist) 
////              System.out.println("Bucket already exists.");
////            else {
////              // Make a new bucket called asiatrip to hold a zip file of photos.
////              minioClient.makeBucket(MakeBucketArgs.builder()
////			            		.bucket("public-protrjcapital")
////			            		.build());
////            }
//            //
//            // Upload an JSON file.
//            minioClient.uploadObject(UploadObjectArgs.builder()
//                    .bucket("public-protrjcapital")
//                    .object(arquivoX)
//                    .filename(pathX + arquivoX).build());            
//            
//            System.out.println("'" + pathX + arquivoX + "' is successfully uploaded " + 
//            								" as 'of1' to 'public-protrjcapital' ... bucket.");
//            //
//        } catch(Exception e) {
//        	  //
//        	  System.out.println("Error2 occurred: " + e);
//        }
//		//
//		return msgX;
//	} 		
	
}