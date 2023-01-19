package com.mpxds.mpbasic.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
//import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Resources;

// ---

public class MpAppUtil {
	//
	private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	
	public static String getUrlApiRest() { return "http://localhost:8080"; }
	
	// ---

	public static String pegaSacadoNome(String sacadoNome) {
		//
		String nomeX = null;
		
        Integer posX = sacadoNome.indexOf("Protocolo:");
        //
        if (posX >=0) {
        	// Ex.:
        	// SILVIO BARBOSA Protocolo: 14/12/2017-071131
        	// 01234567890123456789012345678901234567890123456789
        	nomeX = sacadoNome.substring(0, posX).trim();
        	//
        }
        //
        return nomeX;
	}
	
	public static Date pegaSacadoDataProcotolo(String sacadoNome) {
		//
		Date dataProtocolo = null;

        Integer posX = sacadoNome.indexOf("Protocolo:");
        //
        if (posX >=0) {
        	// Ex.:
        	// SILVIO BARBOSA Protocolo: 14/12/2017-071131
        	// 01234567890123456789012345678901234567890123456789
        	String dataDocX = sacadoNome.substring(posX + 11, posX + 21);
            //
        	try {
        		//
				dataProtocolo  = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocX);
				//
			} catch (ParseException e) {
				//
    			System.out.println("Error conversão Data Intimação ( SacadoNome = " + sacadoNome);
			}
        }		
		//		
		return dataProtocolo;
	}
	
	public static String pegaSacadoNumeroProcotolo(String sacadoNome) {
		//
		String numeroProtocolo = "";

        Integer posX = sacadoNome.indexOf("Protocolo:");
        //
        if (posX >=0) {
        	// Ex.:
        	// SILVIO BARBOSA Protocolo: 14/12/2017-071131
        	// 01234567890123456789012345678901234567890123456789
        	numeroProtocolo = sacadoNome.substring(posX + 22, posX + 27);
            //
        }		
		//		
		return numeroProtocolo;
	}

	public static Integer contadorOcorrencias(String s, String oc) {
		//
//		int pos = -1;
//		int contagem = 0;
//		
//		while (true) {
//			//
//		    pos = s.indexOf (oc, pos + 1); 
//		    if (pos < 0) break;
//		    contagem++;
//		}
		//
		return StringUtils.countMatches(s, oc); // ontagem;
	}
	
	public static String md5(String valor) {
		//
        MessageDigest mDigest;
        
        try { 
               //Instanciamos o nosso HASH MD5, poderíamos usar outro como
               //SHA, por exemplo, mas optamos por MD5.
              mDigest = MessageDigest.getInstance("MD5");
                     
              //Convert a String valor para um array de bytes em MD5
              byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));
              
              //Convertemos os bytes para hexadecimal, assim podemos salvar
              //no banco para posterior comparação se senhas
              StringBuffer sb = new StringBuffer();
              
              for (byte b : valorMD5){
                     sb.append(Integer.toHexString((b & 0xFF) |
                     0x100).substring(1,3));
              }
              //
              return sb.toString();
                     
        } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
              return null;
        } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
              return null;
        }
	}
	
	public static String gerarNovaSenha() {
		//
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
						   "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
						   "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
						   "u", "v", "w", "x", "y", "z",
						   "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
						   "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
						   "U", "V", "W", "X", "Y", "Z" };
		//
		String senha = "";

		for (int x = 0; x < 10; x++) {
			int j = (int) (Math.random() * carct.length);
			
			senha += carct[j];
		}
		//
		return senha;
	}

	public static Date pegaDataAtual() {
		//
		// sudo timedatectl set-timezone America/Sao_Paulo (Aplicado WebServer+DBServer MVPR-14092019) !

		return new Date();
		//
//		TimeZone tz1 = TimeZone.getTimeZone("America/Sao_Paulo");
//		
//      Calendar cal = GregorianCalendar.getInstance(tz1);
//      //
//		return cal.getTime();
	}
	
	public static long difData(Date datFinal, Date datInicial) {
		//
		Calendar dataInicial = Calendar.getInstance();
		//
		dataInicial.setTime(datInicial);
		//
		Calendar dataFinal = Calendar.getInstance();
		//
		dataFinal.setTime(datFinal);
		// Calcula a diferen�a entre a data final e da data de inicial
		long diferencaMillis = dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis();
		// Quantidade de milissegundos em um dia
		int tempoDiaMillis = 1000 * 60 * 60 * 24;
		//
		long diferencaDias = diferencaMillis / tempoDiaMillis;
		//
		return diferencaDias;
	}

	public static String diaSemana(Date dataX) {
		//
		Locale objLocale = new Locale("pt", "BR");
		//
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataX);
		//
		String diaSemana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, objLocale);
		//
		return diaSemana;
	}

	public static boolean isEmail(String email) {
		//
	    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher m = p.matcher(email);

	    boolean matchFound = m.matches();

	    if (!matchFound)
	    	return false;
	 		//
		return true;
	}

	public static boolean isNumeric(String str) {
		//
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		//
		return str.length() == pos.getIndex();
		//
		// try {
		// Integer i = Integer.parseInt(str.trim());
		// }
		// catch(NumberFormatException nfe) {
		// return false;
		// }
		// //
		// return true;
	}

	public static String randomString(int tamanho, boolean usaLetras, boolean usaNumeros) {
		//
		String randomString = RandomStringUtils.random(tamanho, usaLetras, usaNumeros);
		//
		return randomString;
	}

	public static StringBuffer ConvertUrlToString(String webPage) {
		//
		try {
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			//
			return sb;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		return null;
	}

	public static String geraHmacSHA256(String secret, String message) {
		//
		String hash = "";
		//
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

			sha256_HMAC.init(secret_key);

			hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		return hash;
		//
	}

	public static String computeSignature(String mac, String message, String secret) {
		//
		String hash = "";
		//
		try {
			// String secret = "ddc8210fb85ce70253c7fde596be46e1";
			// String message = "4a5415ecmpxds";

			// Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			Mac sha256_HMAC = Mac.getInstance(mac);

			// SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),
			// "HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), mac);

			sha256_HMAC.init(secret_key);

			hash = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes()));
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		return hash;
		//
	}

	public static String verificaCpf(String string) {
		// Retira pontos e traços do CPF !
		string = string.replace(".", "");
		string = string.replace("-", "");
		//
		String saida = "";
		Pattern pattern = Pattern.compile("[0123456789]");
		Matcher matcher = pattern.matcher(string);
		//
		while (matcher.find()) {
			saida += matcher.group();
		}
		//
		if (saida.equalsIgnoreCase("00000000191")) {
			return "false";
		} else if (saida.equalsIgnoreCase("00000000000")) {
			return "false";
		} else if (saida.equalsIgnoreCase("11111111111")) {
			return "false";
		} else if (saida.equalsIgnoreCase("22222222222")) {
			return "false";
		} else if (saida.equalsIgnoreCase("33333333333")) {
			return "false";
		} else if (saida.equalsIgnoreCase("44444444444")) {
			return "false";
		} else if (saida.equalsIgnoreCase("55555555555")) {
			return "false";
		} else if (saida.equalsIgnoreCase("66666666666")) {
			return "false";
		} else if (saida.equalsIgnoreCase("77777777777")) {
			return "false";
		} else if (saida.equalsIgnoreCase("88888888888")) {
			return "false";
		} else if (saida.equalsIgnoreCase("99999999999")) {
			return "false";
		} else {
			return saida;
		}
	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean isValidCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14))
			return false;

		Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
	}
	
	public static byte[] getFileContents(InputStream in) {
		//
		byte[] bytes = null;

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int read = 0;
			bytes = new byte[1024];
			//
			while ((read = in.read(bytes)) != -1) {
				bos.write(bytes, 0, read);
			}
			bytes = bos.toByteArray();
			//
			in.close();
			in = null;
			bos.flush();
			bos.close();
			bos = null;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return bytes;
	}

	public static String convertIS(InputStream inputStream, Charset charset) throws IOException {
		//
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}
		//
		return stringBuilder.toString();
	}

	// Método static -> não precisa instânciar a classe !
	//
	public static void PrintarLn(String texto) {
		//
		System.out.println(texto);
	}

	// ---
	
    public static File escrever(String name, byte[] contents) throws IOException {
    	//
        File file = new File(diretorioRaizParaArquivos(), name);

        OutputStream out = new FileOutputStream(file);
        
        out.write(contents);
        out.close();
        //
        return file;
    }

    public static List<File> listar() {
    	//
        File dir = diretorioRaizParaArquivos();

        return Arrays.asList(dir.listFiles());
    }

    public static java.io.File diretorioRaizParaArquivos() {
    	//
        File dir = new File(diretorioRaiz(), "arquivos");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        //
        return dir;
    }
    
    public static File diretorioRaiz() {
    	//
        // Estamos utilizando um diretório dentro da pasta temporária. 
        // No seu projeto, imagino que queira mudar isso para algo como:
        // File dir = new File(System.getProperty("java.io.tmpdir"), "mpxds");
        File dir = new File(System.getProperty("user.home"), "mpxds");	

        if (!dir.exists()) {
            dir.mkdirs();
        }
        //
        return dir;
    }
    
    public static int countLines(File input) throws IOException {
    	//
        try (InputStream is = new FileInputStream(input)) {
            int count = 1;
            for (int aChar = 0; aChar != -1;aChar = is.read())
                count += aChar == '\n' ? 1 : 0;
            //
            is.close();
            //
            if (count==1) {
            	//
            	count = 0;
            	InputStream isX = new FileInputStream(input);
            	
        		Scanner scX = new Scanner(isX);
        		while(scX.hasNextLine()) {
        			count++;
        		}
        		//
                scX.close();
                isX.close();
            }
            //
            return count;
        }
    }    
	
    public static boolean emailValidator(String email) {
    	//
		boolean isValid = false;
		
		try {
			//
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} catch (AddressException e) {
			System.out.println("MpAppUtil.emailValidator() - You are in catch block -- Exception Occurred for: " + email);
		}
		//
		return isValid;
	}
    
    public static String formataMoeda(BigDecimal valor) {
    	//    	
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        
        symbols.setGroupingSeparator(',');

        String pattern = "R$###.###.##0,###";
        
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        String bigDecimalConvertedValue = decimalFormat.format(valor);
        
//        String convertedValue = decimalFormat.format(12345.67);

//        System.out.println(bigDecimalConvertedValue);
//        System.out.println(convertedValue);    	
    	
    	return bigDecimalConvertedValue;
    }
    
    public static FileOutputStream zipaFile(File fileX) {
    	//
        FileOutputStream fos = null;

        try {
	        String sourceFile = fileX.getAbsolutePath();
	        
			fos = new FileOutputStream(fileX.getAbsolutePath() + ".zip");
			
	        ZipOutputStream zipOut = new ZipOutputStream(fos);
	        
	        File fileToZip = new File(sourceFile);
	        FileInputStream fis = new FileInputStream(fileToZip);
	        
	        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
	        zipOut.putNextEntry(zipEntry);
	        
	        byte[] bytes = new byte[1024];
	        int length;
	        //
	        while((length = fis.read(bytes)) >= 0) {
	        	//
	            zipOut.write(bytes, 0, length);
	        }
	        //
	        zipOut.close();
	        fis.close();
	        fos.close();
	        //
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
        //
    	return fos;
    }

    public static String unzipaFile(String pathX, File fileX) {
    	//
    	String fileUnzipX = "";
    	
        try {
        	//
            String fileZipX = fileX.getAbsolutePath();
            
//        	System.out.println("MpAppUtil.unzipaFile() - fileZip = " + fileZipX); 
//
//        	File destDir = new File(System.getProperty("user.home"));

            File destDir = new File(pathX);
            
            byte[] buffer = new byte[1024];
            
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZipX));
            
            ZipEntry zipEntry = zis.getNextEntry();
            //
            while (zipEntry != null) {
            	//
            	File newFile = newFileUnzip(destDir, zipEntry);
            	
            	fileUnzipX = newFile.getAbsolutePath();
            	
                FileOutputStream fos = new FileOutputStream(newFile);
                
                int len;
                //
                while ((len = zis.read(buffer)) > 0) {
                	//
                    fos.write(buffer, 0, len);
                }
                //
                fos.close();
                
                zipEntry = zis.getNextEntry();
            }
            //
            zis.closeEntry();
            zis.close();
            //
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
        //
        return fileUnzipX;
    }
    
    public static File newFileUnzip(File destinationDir, ZipEntry zipEntry) throws IOException {
    	//
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
        	//
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        //
        return destFile;
    }    
    
    public static String capturaFileNameZip(File fileZip) throws IOException {
    	//
    	String nomeFileZip = "";

        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;
        //
        try {
        	//
            fis = new FileInputStream(fileZip);
            
            zipIs = new ZipInputStream(new BufferedInputStream(fis));
            //
            while((zEntry = zipIs.getNextEntry()) != null){
            	//
                System.out.println(zEntry.getName());
                
                nomeFileZip = zEntry.getName();
            }
            //
            zipIs.close();
            //
        } catch (FileNotFoundException e) {
        	//
            e.printStackTrace();
            //
        } catch (IOException e) {
            //
            e.printStackTrace();
            //
        } 
    	//
        return nomeFileZip;
    }    
    
	public static String fileToBase64(File fileX) {
		//
		// System.out.println("MpAppUtil.fileToBase64() ( " + fileX.getAbsolutePath());
		//
        File originalFile = new File(fileX.getAbsolutePath() + ".zip");
        
        String encodedBase64 = null;
        //
        try {
        	//
            @SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            
            byte[] bytes = new byte[(int)originalFile.length()];
            
            fileInputStreamReader.read(bytes);
            
            encodedBase64 = new String(Base64.encodeBase64(bytes));
            //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
    	return encodedBase64;
    }
	
	public static String getBasicAuthenticationEncoding(String username, String password) {

        String userPassword = username + ":" + password;
        
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }

	public static String validaCpfCnpj(String cpfCnpjX) {
		//
		cpfCnpjX = formataCpfCnpj(cpfCnpjX); 
	
		if (cpfCnpjX.length() == 11 || cpfCnpjX.length() == 14)
			assert(true); // nop
		else {
			 return "CPF/CNPJ formato Inválido ! ( " + cpfCnpjX;
		}
		// Trata CPF ...
		if (cpfCnpjX.length() == 11)
			if (MpAppUtil.isValidCPF(cpfCnpjX))
				assert(true); // nop
			else {
				return "CPF Inválido ! ( " + cpfCnpjX;
			}
		// Trata CNPJ ...
		if (cpfCnpjX.length() == 14) 
			if (MpAppUtil.isValidCNPJ(cpfCnpjX))
				assert(true); // nop
			else {
				return "CNPJ Inválido ! ( " + cpfCnpjX;
			}
		//    	
		return "OK";
	}
	
	public static String formataCpfCnpj(String cpfCnpjF) {
		//
		String cpfCnpjX = cpfCnpjF.replace(".", "");
		
		cpfCnpjX = cpfCnpjX.replace(".", "");
		cpfCnpjX = cpfCnpjX.replace(".", "");
		cpfCnpjX = cpfCnpjX.replace("-", "");
		cpfCnpjX = cpfCnpjX.replace("/", "");
		//
		return cpfCnpjX;
	}
	
    public static String formatarString(String texto, String mascara) throws ParseException {
    	//
        MaskFormatter mf = new MaskFormatter(mascara);
        
        mf.setValueContainsLiteralCharacters(false);
        //
        return mf.valueToString(texto);
    }
    
    public static byte[] deflateZIP(byte[] data) throws IOException, DataFormatException {
    	//
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        
        byte[] buffer = new byte[1024];
        //
        while (!inflater.finished()) {
        	//
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        //
        outputStream.close();

        byte[] output = outputStream.toByteArray();
        //
        return output;
    }    
    	
    public static File getFileFromURL(String filenameX) {
    	//        		
        File file = null;

        try {
        	//
            URL url = Resources.getResource(filenameX);

            file = new File(url.toURI());
            //
        } catch (Exception e) {
        	//
            file = null; //  new File(url.getPath());
        }
       	//
        return file;
        //
    }    
    
	// ----

	public static FacesContext getFC() { return FacesContext.getCurrentInstance(); }

	public static ExternalContext getEC() { return FacesContext.getCurrentInstance().getExternalContext(); }

	public static HttpServletRequest getRequest() { return (HttpServletRequest) getEC().getRequest(); }

	public static HttpServletResponse getResponse() { return (HttpServletResponse) getEC().getResponse(); }

}