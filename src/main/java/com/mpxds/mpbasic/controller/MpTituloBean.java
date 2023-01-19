package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.model.MpTituloLog;
import com.mpxds.mpbasic.model.enums.MpCartorioComarca;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpTituloLogService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpTituloBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpTitulos mpTitulos;

	@Inject
	private MpTituloLogService mpTituloLogService;
	
	@Inject
	private MpSeguranca mpSeguranca;
		
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;
    
    private String numeroProtocolo;
    private Date dataProtocolo;
    private String statusTitulo;
    
    private Boolean indTituloProtestadoOrdinario;
    private Boolean indTituloProtestadoCustodiado;
		
	private MpTitulo mpTituloSelX;		
    //
	private MpCartorioOficio mpCartorioOficioSel;
	private MpCartorioComarca mpCartorioComarcaSel;
	
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;

	private String horarioRobotOf1;
	private String horarioRobotOf2;
	private String horarioRobotOf3;
	private String horarioRobotOf4;

	private List<MpCartorioComarca> mpCartorioComarcaList = new ArrayList<MpCartorioComarca>();;

	// ---
	    
	public void init() {
		//
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;
		//
		this.indTituloProtestadoOrdinario = false;
		this.indTituloProtestadoCustodiado = false;
		//
		// Captura Data/Hora última atualização da base via ROBOT ! 
		//
    	this.horarioRobotOf1 = "Não disponivel !";
    	this.horarioRobotOf2 = "Não disponivel !";
    	this.horarioRobotOf3 = "Não disponivel !";
    	this.horarioRobotOf4 = "Não disponivel !";
    	//
    	if (mpSeguranca.isIndComarca()) {
    		//
        	for (MpCartorioComarca mpCartorioComarca :MpCartorioComarca.values()) {
        		//
            	this.mpCartorioComarcaList.add(mpCartorioComarca);
        	}
    	}
    	//
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of1_HorarioRobot");
	    if (null != mpSistemaConfig)
	    	this.horarioRobotOf1 = mpSistemaConfig.getValorT();
    	mpSistemaConfig = mpSistemaConfigs.porParametro("Of2_HorarioRobot");
	    if (null != mpSistemaConfig)
	    	this.horarioRobotOf2 = mpSistemaConfig.getValorT();
    	mpSistemaConfig = mpSistemaConfigs.porParametro("Of3_HorarioRobot");
	    if (null != mpSistemaConfig)
	    	this.horarioRobotOf3 = mpSistemaConfig.getValorT();
    	mpSistemaConfig = mpSistemaConfigs.porParametro("Of4_HorarioRobot");
	    if (null != mpSistemaConfig)
	    	this.horarioRobotOf4 = mpSistemaConfig.getValorT();
	    //
    }
    
    public void submit() {
    	//
    	MpFacesUtil.addErrorMessage("Correto");
	}
    
    public void consultarTitulo() {
    	//
		this.statusTitulo = ""; 
		String msgX = "";
    	String ofComX = "Of";
	    
	    // ------------    	
	    // Criticar ...
	    // ------------    
	    if (null == this.mpCartorioOficioSel && null == this.mpCartorioComarcaSel) {
			//
		    if (mpSeguranca.isIndComarca()) 
		    	MpFacesUtil.addErrorMessage("Informar Ofício ou Comarca");
		    else
		    	MpFacesUtil.addErrorMessage("Informar Ofício");
		    //
			return;
		}
	    //
	    if (mpSeguranca.isIndComarca()) {
	    	//
		    if (null == this.mpCartorioOficioSel || null == this.mpCartorioComarcaSel) 
		    	assert(true); // nop
		    else
		    	if (null == this.mpCartorioOficioSel) {
		    		//
			    	if (!this.mpCartorioComarcaSel.getNome().isEmpty()) {
			    		//
			    		MpFacesUtil.addErrorMessage("Informar Ofício ou Comarca !");  
			    		return;
			    	}
		    	} else {
		    		//
			    	if (!this.mpCartorioOficioSel.getNome().isEmpty()) {
			    		//
			    		MpFacesUtil.addErrorMessage("Informar Ofício ou Comarca !");  
			    		return;
			    	}
		    	}
	    }
        //
	    SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdfX = new SimpleDateFormat("dd/MM/yyyy");		

		// Verificar Horário de Funcionamento !
	    String dateX = new Date().toString();	

	    String dtInicioS = "";
	    String dtFimS = "";

	    Date dtAtual = new Date();
	    Date dtInicio = dtAtual;
	    Date dtFim = dtAtual;
	    //
	    try {
	    	// Ex.: Tue Nov 14 09:28:07 BRST 2017
	    	//      012345678901234567890123456789
	    	dtAtual = sdfHHmm.parse(dateX.substring(11, 16)); 
	    	
	    	if (null == this.mpCartorioOficioSel)
	    		ofComX = "Co";
	    	//
		    MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(ofComX + 
		    									this.mpCartorioOficioSel.getNumero() + "_HorarioTitulo");
		    if (null == mpSistemaConfig) {
		    	//
		    	dtInicioS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(0, 5);
		    	dtFimS = this.mpCartorioOficioSel.getHorarioFuncionamento().substring(6, 11);

		    	dtInicio = sdfHHmm.parse(dtInicioS);
		    	dtFim = sdfHHmm.parse(dtFimS);
		    } else {
		    	//
		    	if (!mpSistemaConfig.getIndValor()) {
		    		//
			    	if (null == this.mpCartorioOficioSel)
			    		MpFacesUtil.addErrorMessage("Serviço Temporariamente Indisponivel ! Tente mais tarde. ( Co.= "
			    															+ this.mpCartorioComarcaSel.getNumero());
			    	else
			    		MpFacesUtil.addErrorMessage("Serviço Temporariamente Indisponivel ! Tente mais tarde. ( Of.= "
								+ this.mpCartorioOficioSel.getNumero());
			    	//
			    	return;
		    	}
		    	//
		    	dtInicioS = mpSistemaConfig.getValorT().substring(0, 5);
		    	dtFimS = mpSistemaConfig.getValorT().substring(6, 11);
		    	
		    	dtInicio = sdfHHmm.parse(dtInicioS);
		    	dtFim = sdfHHmm.parse(dtFimS);
		    }		    
		    //
		    if ((dtAtual.after(dtInicio) && dtAtual.before(dtFim)))
		    	assert(true);
		    else {
		    	MpFacesUtil.addErrorMessage("Fora do horário permitido ! ( De: " + 
		    				dtInicioS + " às " + dtFimS + " ) - " + sdfHHmm.format(dtAtual));
				return;
		    }
		    //
		} catch (ParseException e) {
			//
			MpFacesUtil.addErrorMessage("Error.001 - Tratamento Horas ! Contactar Suporte Técnico ! ( hI = " + 
					dtInicioS + " / hf = " + dtFimS + " / hA = " + dtAtual.toString() + " / e = " + e);
			return;
		}
	    //
    	if (null == this.numeroProtocolo || this.numeroProtocolo.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("Informar Número Protocolo");  
			return;
		}
    	
		// MVPR-20200401 (Preencher com zeros à esquerda NumeroProtocolo (numeroIntimacao)! 
		this.numeroProtocolo = StringUtils.leftPad(this.numeroProtocolo.trim(), 6, "0");
    	
    	//
    	if (null == this.dataProtocolo) {
			//
			MpFacesUtil.addErrorMessage("Informar data Protocolo");  
			return;
		}
		//
		Calendar dataX = Calendar.getInstance(Locale.getDefault());
		dataX.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));		
		dataX.setTime(this.dataProtocolo);

		this.dataProtocolo = dataX.getTime();

		dataX.set(Calendar.HOUR_OF_DAY, 0);
		dataX.set(Calendar.MINUTE, 0);
		dataX.set(Calendar.SECOND, 0);
		
		dataX.add(Calendar.DAY_OF_MONTH, -1);
		Date dataProtocoloI = dataX.getTime();
    	
		dataX.add(Calendar.DAY_OF_MONTH, +2);
		Date dataProtocoloF = dataX.getTime();
		//
//    	List<MpTitulo> mpTituloList = mpTitulos.mpTituloByNumeroDataProtocoloList(this.mpCartorioOficioSel.getNumero(),
//    																				this.numeroProtocolo, 
//    																			 	dataProtocolo);
		List<MpTitulo> mpTituloList = new ArrayList<MpTitulo>();
    	
    	String numeroCartorioSel = "";
    	if (null == this.mpCartorioOficioSel)
    		numeroCartorioSel = this.mpCartorioComarcaSel.getNumero();
	    else
    		numeroCartorioSel = this.mpCartorioOficioSel.getNumero();
    	
		List<MpTitulo> mpTituloListX = mpTitulos.mpTituloByOficioNumeroProtocoloList(numeroCartorioSel,
																					this.numeroProtocolo);
	    //
    	if (mpTituloListX.isEmpty()) {
			//
			MpFacesUtil.addErrorMessage("Nenhuma Informação encontrada na nossa Base de Dados ( " + ofComX + "." 
					+ numeroCartorioSel + " / " +	this.numeroProtocolo + " / " + sdfX.format(this.dataProtocolo));  
			return;
		} else {
			//
			for (MpTitulo mpTitulo : mpTituloListX) {
				//
				msgX = msgX +"\n" + sdfX.format(this.dataProtocolo) + " / I = "
						+ sdfX.format(dataProtocoloI) + " / F = "
						+ sdfX.format(dataProtocoloF) + " / Base = "
						+ sdfX.format(mpTitulo.getDataProtocolo());
				System.out.println(msgX);
				//
				if (sdfX.format(mpTitulo.getDataProtocolo()).equals(sdfX.format(this.dataProtocolo))
				||  sdfX.format(mpTitulo.getDataProtocolo()).equals(sdfX.format(dataProtocoloI))
				||  sdfX.format(mpTitulo.getDataProtocolo()).equals(sdfX.format(dataProtocoloF))) {
					//
					mpTituloList.add(mpTitulo);
				}
			}
			//
	    	if (mpTituloList.isEmpty()) {
				//
	    		MpFacesUtil.addErrorMessage("Nenhuma Informação encontrada na nossa Base de Dados... ( " +
	    									this.numeroProtocolo + " / " + sdfX.format(this.dataProtocolo) +
	        				    			" / " + msgX);  
	    		return;
	    	}
		}
    	
    	// 
		this.statusTitulo = mpTituloList.get(0).getStatus();
    	// MVPR_20180904 ...
		if (this.statusTitulo.trim().toUpperCase().equals("EXCLUIDO DEPOSITO EMITIDO"))
			this.statusTitulo = "EM ANDAMENTO";
		//
		if (!mpTituloList.get(0).getComplemento().isEmpty())
			this.statusTitulo = this.statusTitulo + " ( " + mpTituloList.get(0).getComplemento() ;
		//
//		if (this.statusTitulo.indexOf("Protestado Ordinario") >= 0)
		if (this.statusTitulo.trim().toUpperCase().equals("PROTESTADO")) // PRISCO->MVPR-07112019(Viviane!)
			if (mpTituloList.get(0).getClasseTitulo().indexOf("ORDINARIO") >= 0)
				this.indTituloProtestadoOrdinario = true;
			else
				this.indTituloProtestadoOrdinario = false;

//		if (this.statusTitulo.indexOf("Protestado Custodiado") >= 0)
		if (mpTituloList.get(0).getClasseTitulo().indexOf("CUSTODIADO") >= 0) // TJD??
			this.indTituloProtestadoCustodiado = true;
		else
			this.indTituloProtestadoCustodiado = false;

		// PRISCO->MVPR-07112019(Viviane!)...
		if (this.statusTitulo.equals("EM ANDAMENTO")) {
			//
			this.indTituloProtestadoCustodiado = true;
			this.statusTitulo = "TÍTULO EM ABERTO";
		}
		
    	// Grava Titulo Log...
        // ===================
        MpTituloLog mpTituloLog = new MpTituloLog();
        
        mpTituloLog.setDataGeracao(new Date());
        // MVPR_20180906 ...
        if (null == mpSeguranca.getLoginUsuario())
        	mpTituloLog.setUsuarioNome("anonymous"); // Usuário não logado ... página serviços pública !
        else
        	mpTituloLog.setUsuarioNome(mpSeguranca.getLoginUsuario());
        //
        if (null == this.mpCartorioOficioSel)
        	mpTituloLog.setNumeroOficio(this.mpCartorioComarcaSel.getNumero());
        else
        	mpTituloLog.setNumeroOficio(this.mpCartorioOficioSel.getNumero());
        //
        mpTituloLog.setNumeroProtocolo(mpTituloList.get(0).getNumeroProtocolo());
        
        this.mpTituloLogService.salvar(mpTituloLog);        
        //
    	MpFacesUtil.addInfoMessage("Consulta Status Titulo... efetuada com sucesso ! ( " + 
    				    			this.numeroProtocolo + " / " + sdfX.format(this.dataProtocolo) +
    				    			" / " + msgX);
    }
        
	// ---

	public String getNumeroProtocolo() { return this.numeroProtocolo; }
	public void setNumeroProtocolo(String numeroProtocolo) { this.numeroProtocolo = numeroProtocolo; }

	public Date getDataProtocolo() { return this.dataProtocolo; }
	public void setDataProtocolo(Date dataProtocolo) { this.dataProtocolo = dataProtocolo; }

	public String getStatusTitulo() { return this.statusTitulo; }
	public void setStatusTitulo(String statusTitulo) { this.statusTitulo = statusTitulo; }

	public MpTitulo getMpTituloSelX() { return this.mpTituloSelX; }
	public void setMpTituloSelX(MpTitulo mpTituloSelX) { this.mpTituloSelX = mpTituloSelX; }
    		
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																	 this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioComarca getMpCartorioComarcaSel() { return mpCartorioComarcaSel; }
	public void setMpCartorioComarcaSel(MpCartorioComarca mpCartorioComarcaSel) { 
																	 this.mpCartorioComarcaSel = mpCartorioComarcaSel; }

	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }

	public Boolean getIndTituloProtestadoOrdinario() { return this.indTituloProtestadoOrdinario; }
	public void setIndTituloProtestadoOrdinario(Boolean indTituloProtestadoOrdinario) { 
												this.indTituloProtestadoOrdinario = indTituloProtestadoOrdinario; }

	public Boolean getIndTituloProtestadoCustodiado() { return this.indTituloProtestadoCustodiado; }
	public void setIndTituloProtestadoCustodiado(Boolean indTituloProtestadoCustodiado) { 
												this.indTituloProtestadoCustodiado = indTituloProtestadoCustodiado; }

	public String getHorarioRobotOf1() { return this.horarioRobotOf1; }
	public void setHorarioRobotOf1(String horarioRobotOf1) { this.horarioRobotOf1 = horarioRobotOf1; }
	public String getHorarioRobotOf2() { return this.horarioRobotOf2; }
	public void setHorarioRobotOf2(String horarioRobotOf2) { this.horarioRobotOf2 = horarioRobotOf2; }
	public String getHorarioRobotOf3() { return this.horarioRobotOf3; }
	public void setHorarioRobotOf3(String horarioRobotOf3) { this.horarioRobotOf3 = horarioRobotOf3; }
	public String getHorarioRobotOf4() { return this.horarioRobotOf4; }
	public void setHorarioRobotOf4(String horarioRobotOf4) { this.horarioRobotOf4 = horarioRobotOf4; }
	//
	public List<MpCartorioComarca> getMpCartorioComarcaList() { return mpCartorioComarcaList; }
	public void setMpCartorioComarcaSel(List<MpCartorioComarca> mpCartorioComarcaList) { 
														 		this.mpCartorioComarcaList = mpCartorioComarcaList; }
	
}