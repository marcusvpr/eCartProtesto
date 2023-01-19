package com.mpxds.mpbasic.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.mpxds.mpbasic.model.MpTituloCargaLog;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.service.MpTituloCargaLogService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@ViewScoped
@ManagedBean
public class MpCarregaTituloBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpTitulos mpTitulos;
	
	@Inject
	private MpSeguranca mpSeguranca;

	@Inject
	private MpTituloCargaLogService mpTituloCargaLogService;
	
	@Inject
	private MpCargaControleService mpCargaControleService;
	
	// ---
	
    private UploadedFile file;
    
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;	
    
    private Integer progresso;
    private String mensagem;
    private Integer quantidadeTitulos;
    private List<String> titulos;
	
    private Boolean indFile;
    private Boolean indCancel;
    private Boolean indPBar = false;
        
	private Long numeroRegistros;
        
	private SimpleDateFormat sdfYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private ExecutorService service = Executors.newSingleThreadExecutor();    
	
    // ---
    
	@PostConstruct
	public void postConstruct() {
	  	//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;
        
        this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;

		this.indFile = false;	
		this.indCancel = false;	
		//			
    }
	
    public void uploadX() {
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
        if (null == this.mpCartorioOficioSel) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Cartório !");
	    	return;
        }
    	//
        if (null == this.file) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Arquivo !");
	    	return;
        }

        // =======================================//
    	// Trata Carga Controle ! MR-2020-05-30 ! //
        // =======================================//
        String msgX = this.mpCargaControleService.validaCargaControle(this.mpCartorioOficioSel.getNumero());
		//
        if (!msgX.isEmpty()) {
        	//
        	MpFacesUtil.addErrorMessage(msgX);
	    	return;
        }
        // =======================================//

        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		File fileX = null;
		Path folder = null;
		Path filePath = null;        
        //
		try {
			fileX = new File(extContext.getRealPath(File.separator + "resources" + File.separator + 
																	"tituloImportacao" + File.separator));
			folder = Paths.get(fileX.getAbsolutePath());
			if (!fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +
	    			extContext.getRealPath("//resources//tituloImportacao// ( e = " + e));
//	        MpAppUtil.PrintarLn("Pasta/Diretório... (path = " +
//	    			extContext.getRealPath("//resources//tituloImportacao// ( e = " + e));
	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.upload() - 001");

        try {
			//
        	File fileXX = new File(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			folder = Paths.get(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			if (!fileXX.exists())
				filePath = Files.createFile(folder);
			//
			Integer cont = MpAppUtil.countLines(fileXX);
			this.numeroRegistros = cont.longValue() ;
			
			try (InputStream input = this.file.getInputstream()) {
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			//
			this.numeroRegistros = Files.lines(filePath).count();
		}
		catch(Exception e) {
			//
//	    	MpFacesUtil.addErrorMessage("Erro-01X = Arquivo... (file = " + folder.toString() + " / e = " + e +
//	    			" / numeroRegistros = " + this.numeroRegistros);
	        MpAppUtil.PrintarLn("Erro-01TX = Arquivo... (file = " + folder.toString() + " / e = " + e +
	        		" / numeroRegistros = " + this.numeroRegistros);
//	    	return;
		}
		//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.upload() - 002 ( Cart = " + this.mpCartorioOficioSel.getNumero() + 
//        		" / FileSize = " + this.file.getSize());

        try {
        	//
            this.trataArquivoTituloX(this.file.getInputstream(), this.mpCartorioOficioSel.getNumero(),
            																			this.numeroRegistros);
            //
        } catch (IOException e) {
        	//
			MpFacesUtil.addErrorMessage("Erro_0001X - ( e = " + e.getMessage());
        }
    }
	
	
    public void upload(FileUploadEvent event) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.upload() - 000");
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
        if (null == this.mpCartorioOficioSel) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Cartório !");
	    	return;
        }
    	//
        this.file = event.getFile();
        if (null == this.file) {
        	//
        	MpFacesUtil.addErrorMessage("Selecionar Arquivo !");
	    	return;
        }
        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("hhmmss");
        
    	String pathDateT = sdf.format(new Date()) + "_" + sdfT.format(new Date()) ;  
        //
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

		File fileX = null;
		Path folder = null;
		Path filePath = null;        
        //
		try {
			fileX = new File(extContext.getRealPath(File.separator + "resources" + File.separator + 
																			"tituloImportacao" + File.separator));
			folder = Paths.get(fileX.getAbsolutePath());
			if (!fileX.exists())
				filePath = Files.createDirectory(folder);
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Pasta/Diretório... (path = " +
	    			extContext.getRealPath("//resources//tituloImportacao// ( e = " + e));
	    	return;
		}
		//
//      MpAppUtil.PrintarLn("MpCarregaTituloBean.upload() - 001");

        try {
			//
        	File fileXX = new File(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			folder = Paths.get(fileX.getAbsolutePath() + File.separator + pathDateT + "_" + 
        																				this.file.getFileName());
			if (!fileXX.exists())
				filePath = Files.createFile(folder);
			//
			try (InputStream input = this.file.getInputstream()) {
				Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			//
//			this.quantidadeTitulos = (int) Files.lines(filePath).count();			
		}
		catch(Exception e) {
			//
	    	MpFacesUtil.addErrorMessage("Erro Arquivo... (file = " + folder.toString() + " / e = " + e);
//	        MpAppUtil.PrintarLn("Erro Arquivo... (file = " + folder.toString() + " / e = " + e);
	    	return;
		}
		//
        this.indFile = true;
        
        MpFacesUtil.addInfoMessage("Upload OK ! ( Nome Arquivo = " + this.file.getFileName() + 
        		" / Tam.Arquivo = " + this.file.getSize());        
    }
    
    
    public void trataArquivoTituloX(InputStream isX, String codOficio, Long numReg) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.trataArquivoTitulo() ( codOficio = " + codOficio + " / isX = " + isX.toString());

        String rcX = this.mpTitulos.executeSQL("DELETE FROM mp_titulo WHERE codigo_oficio = '" + codOficio + "'");
    	if (null == rcX || !rcX.equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX);
//	        MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX);
	        //
			return;
    	}
    	//
    	String rcSqlX = this.mpTitulos.executeSQLs(isX, numReg);
    	if (!rcSqlX.substring(0,2).equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0007 - Erro na Carga Movimento Titulo ( rc = " + rcSqlX);
			//
			return;
    	} else
    		this.numeroRegistros = Long.parseLong(rcSqlX.substring(4).trim());
    		
    	//
    	// Grava LOG ! 
    	// ==========
    	MpTituloCargaLog mpTituloCargaLog = new MpTituloCargaLog(); 
    	
    	mpTituloCargaLog.setDataGeracao(new Date());
    	mpTituloCargaLog.setNumeroOficio(codOficio);
    	mpTituloCargaLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
		mpTituloCargaLog.setNumeroRegistros(this.numeroRegistros);
    	//    	
    	mpTituloCargaLog = this.mpTituloCargaLogService.salvar(mpTituloCargaLog);
    	
		MpFacesUtil.addInfoMessage("Upload completo!... O arquivo " + this.file.getFileName() +
												" foi processado ! ( " + mpTituloCargaLog.getNumeroRegistros());
    }
    
    
    public void trataArquivoTitulo(InputStream isX, String codOficio) {
    	//
//    	MpAppUtil.PrintarLn("MpTesteBean.trataArquivoTitulo() ( codOficio = " + codOficio);

        String rcX = this.mpTitulos.executeSQL("DELETE FROM mp_titulo WHERE codigo_oficio = '" + codOficio + "'");
    	if (null == rcX || !rcX.equals("OK")) {
    		//
			MpFacesUtil.addErrorMessage("Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX);
//	        MpAppUtil.PrintarLn("Erro_0005 - Erro Exclusão Movimento Titulo ( rc = " + rcX);
	        //
			return;
    	} else
    		this.numeroRegistros = Long.parseLong(rcX.substring(4).trim());
    	//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.trataArquivoTitulo() - 000");
    	//        
    	this.executaThread(isX);
        
    	if (indPBar)
    		this.trataProgressBar();
        
//    	this.executeSQLs(isX);
    	//
    	// Grava LOG ! 
    	// ==========
    	MpTituloCargaLog mpTituloCargaLog = new MpTituloCargaLog(); 
    	
    	mpTituloCargaLog.setDataGeracao(new Date());
    	mpTituloCargaLog.setNumeroOficio(codOficio);
    	mpTituloCargaLog.setUsuarioNome(this.mpSeguranca.getLoginUsuario());    	
		mpTituloCargaLog.setNumeroRegistros((long) this.quantidadeTitulos);
    	//
    	mpTituloCargaLog = this.mpTituloCargaLogService.salvar(mpTituloCargaLog);
    	
		MpFacesUtil.addErrorMessage("Upload completo!... O arquivo " + this.file.getFileName() +
												" foi processado !! ( " + mpTituloCargaLog.getNumeroRegistros());
    }
    
    public String executeSQLs(InputStream isX) {
		//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.executeSQLs() - 000");

        Scanner scX = new Scanner(isX);
    	//
    	this.titulos = new ArrayList<>();		

        Integer i = 0;
    		
    	while(scX.hasNextLine()) {
    		//    		
    		String sqlX = scX.nextLine();

    		// Trata ProgressBar...
    		i++;

    		this.titulos.add("titulo" + (i + 1));
    		
    		this.mensagem = " Processando titulo : " + (i + 1) + " de " + this.quantidadeTitulos;

    		this.atualizarProgresso(i);
    		
    		if (i >= this.quantidadeTitulos) break;
    		//
            try {
                Thread.sleep(200);
                //
            } catch (Exception ex) {
            	//
            }
    		//    		
    	    if (!sqlX.isEmpty()) this.mpTitulos.executeSQL(sqlX);
    	    //
    	}
	    //
	    scX.close();
		//
		return "OK";
	}

    public void trataProgressBar() {
		//
    	this.titulos = new ArrayList<>();		

        for (int i = 0; i <= this.quantidadeTitulos; i++) {
        	// 
        	
    		// Trata ProgressBar...
    		this.titulos.add("titulo" + (i + 1));
    		
    		this.mensagem = " Processando titulo : " + (i + 1) + " de " + this.quantidadeTitulos;

    		this.atualizarProgresso(i);    		
    		//
//    		MpAppUtil.PrintarLn("MpCarregaTituloBean.trataProgressBar() - 000 ( " + i);

    		try {
                Thread.sleep(20);
                
            	if (indCancel) break;
                //
            } catch (Exception ex) {
            	//
            }
    		//    		
    	}
	    //
	}

    private String executaThread(InputStream isXX) {
    	//
//        MpAppUtil.PrintarLn("MpCarregaTituloBean.executaThreadX() - 000");

//    	this.service = Executors.newSingleThreadExecutor();
    	this.service = Executors.newSingleThreadScheduledExecutor();

    	String dataYMDHM_INI = sdfYMDHM.format(new Date());

        Runnable task = new Runnable() {
        	//
            public void run() {
            	//
    	        try {
    	        	//    	        	
					Class.forName("org.hsqldb.jdbcDriver");
					
	    	        Connection conX = DriverManager.getConnection(
										"jdbc:hsqldb:file:~/db/mpProtRJCap/mpProtRJCap_DB", "sa", "");
					//
					Scanner scXX = new Scanner(isXX);
					//
					titulos = new ArrayList<>();		
					
//					MpAppUtil.PrintarLn("MpCarregaTituloBean.executaThreadX() - 001");
					//
					while(scXX.hasNextLine()) {
						// 
			        	if (indCancel) break;
						
						String sqlXX = scXX.nextLine();						
						//
						if (sqlXX.isEmpty()) continue;
						
						if (sqlXX.indexOf("insert into mp_titulo (codigo_oficio") <  0) {
							//
							System.out.println("MpTituloCargaThread.executaThread.run() - ERROR ( sqlX : " + sqlXX);
						  	continue;
						}
						//
//						MpAppUtil.PrintarLn("MpCarregaTituloBean.executaThreadX() - 002 ( " + sqlXX);

						PreparedStatement stmt = conX.prepareStatement(sqlXX);
						 
						stmt.executeUpdate();
						stmt.close();
						//
					}
					//
					conX.close();
					scXX.close();
					//
    	        } catch (ClassNotFoundException e) {
					//
					e.printStackTrace();
				} catch (SQLException e) {
					//
					e.printStackTrace();
				}
    	        //
            }
        };
        //
        this.service.execute(task);
 
        this.service.shutdown();
        //
    	String dataYMDHM_FIM = sdfYMDHM.format(new Date());

    	MpFacesUtil.addInfoMessage("Carga completada com sucesso ! (Tempo: " + dataYMDHM_INI + " / " + dataYMDHM_FIM);
		MpAppUtil.PrintarLn("MpCarregaTituloBean.executaThread()... Carga completada com sucesso ! (Tempo: " + 
																			dataYMDHM_INI + " / " + dataYMDHM_FIM);
		//
		return "OK";
	}
 
    /**
     * cria uma mensagem que sera exibida para usuario atraves do componente 
     * p:messages
     * @param texto mensagem informada
     */
    private void criarMensagem(String texto) {
        //
        FacesMessage msg = new FacesMessage(texto);
        
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    //reseta o progresso e a mensagem do progressbar
    private void resetarProgresso() {
    	//
    	this.progresso = 0;
    	this.mensagem = "";
    }

    /**
     * atualiza o progresso
     * @param i posicao da titulo na lista
     */
    private void atualizarProgresso(int i) {
    	//
        //calculo para o percentual do processo em relacao a quantidade de titulos
        this.progresso = (i * 100) / this.quantidadeTitulos;
        
        try {
            Thread.sleep(50);
            //
        } catch (Exception ex) {
        }
    }

    /**
     * processa as titulos,podendo ser adicionadas ou canceladas
     * @param acao 1 = titulos serao processadas 2 = titulos serao canceladas
     */
    public void processarTitulos() {
    	//
        	this.resetarProgresso();
            //
        	try {
        		//
        		this.trataArquivoTitulo(this.file.getInputstream(), this.mpCartorioOficioSel.getNumero());
        		//
        	} catch (IOException e) {
        		//
                MpFacesUtil.addErrorMessage("MpCarregaTituloBean Erro_0001 - ( e = " + e);
        	}
            //
        	this.criarMensagem(" Todas os " + quantidadeTitulos + "  titulos foram processados !");
            //
            this.indFile = false;
            
            this.resetarProgresso();
            //
    }

    public void cancelar() {
    	//    	
    	this.service.shutdown();
        //
        this.indFile = false;
        this.indCancel = true;

        this.resetarProgresso();

    	MpFacesUtil.addErrorMessage("Cancelamento Efetuado ! ");    	
    	MpAppUtil.PrintarLn("Cancelamento Efetuado ! ");
    }
    
    // ---
    
    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }

    public Boolean getIndFile() { return indFile; }
    public void setIndFile(Boolean indFile) { this.indFile = indFile; }

    public Boolean getIndCancel() { return indCancel; }
    public void setIndCancel(Boolean indCancel) { this.indCancel = indCancel; }

    public Boolean getIndPBar() { return indPBar; }
    public void setIndPBar(Boolean indPBar) { this.indPBar = indPBar; }

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
	
    public Integer getProgresso() {
    	if (progresso == null) progresso = 0;
    	//
    	return progresso;
    }
    public void setProgresso(Integer progresso) { this.progresso = progresso; }

    public String getMensagem() {
    	if (mensagem == null) mensagem = "";
    	//
    	return mensagem;
    }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Integer getQuantidadeTitulos() { return quantidadeTitulos; }
    public void setQuantidadeTitulos(Integer quantidadeTitulos) { this.quantidadeTitulos = quantidadeTitulos; }

    public List<String> getTitulos() { return titulos; }
    public void setTitulos(List<String> titulos) { this.titulos = titulos; }
    
}