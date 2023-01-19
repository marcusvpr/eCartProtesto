package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
import com.mpxds.mpbasic.util.report.MpExecutorRelatorio;

@Named
@RequestScoped
public class MpRelatorioBoletoImpressosBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	@Inject
	private MpSeguranca mpSeguranca;
	
	// 
	
	private Date dataInicio;
	private Date dataFim;

	private MpCartorioOficio mpCartorioOficioSel = MpCartorioOficio.OfX;
	
	private MpCartorioOficio mpCartorioOficioX;
	private MpCartorioOficio mpCartorioOficio1;
	private MpCartorioOficio mpCartorioOficio2;
	private MpCartorioOficio mpCartorioOficio3;
	private MpCartorioOficio mpCartorioOficio4;

	private Boolean indOrdenaDataNumeroProtocolo;
	
	// ---
	public void init() {
		//
		this.mpCartorioOficioSel = MpCartorioOficio.OfX;

		this.mpCartorioOficioX = MpCartorioOficio.OfX;
		this.mpCartorioOficio1 = MpCartorioOficio.Of1;
		this.mpCartorioOficio2 = MpCartorioOficio.Of2;
		this.mpCartorioOficio3 = MpCartorioOficio.Of3;
		this.mpCartorioOficio4 = MpCartorioOficio.Of4;		
		//
		Calendar dataX = Calendar.getInstance();
		
		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, -1);
		
		this.dataInicio = dataX.getTime();

		dataX.setTime(new Date());
		dataX.add(Calendar.DAY_OF_MONTH, +1);

		this.dataFim = dataX.getTime();
		//
		this.indOrdenaDataNumeroProtocolo = false;
	}
	
	public void emitir() {
		//
        this.mpCartorioOficioSel = MpCartorioOficio.OfX;

        if (mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1;
		else
			if (mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2;
			else
				if (mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3;
				else
					if (mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4;
		//
		String msg = "";
		if (null == this.mpCartorioOficioSel) 
			msg = msg + " (Oficio) ";
		if (null == this.dataInicio) 
			msg = msg + " (Data Inicio) ";
		if (null == this.dataFim) 
			msg = msg + " (Data Fim) ";
		if (this.dataInicio.after(this.dataFim)) 
			msg = msg + " (Data Início maior Data Fim) ";
		//
        System.out.println("MpRelatorioBoletoImpressosBean.emitir() ( " + this.mpCartorioOficioSel.getNome() + " / " + msg);
        //
		if (!msg.isEmpty()) {
			MpFacesUtil.addErrorMessage("Error : " + msg);
			return;
		}
		//
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("numero_oficio", this.mpCartorioOficioSel.getNumero());
		parametros.put("data_inicio", this.dataInicio);
		parametros.put("data_fim", this.dataFim);
		//
		SimpleDateFormat sdfYMD_HHMMSS = new SimpleDateFormat("yyyyMMdd_HHmmss");

		MpExecutorRelatorio executor = new MpExecutorRelatorio("/relatorios/mpRelatorio_boletos_impressos.jasper", 
				this.response, parametros, "MpBoletos_Impressos_" + 
				this.mpSeguranca.getLoginUsuario() + "_" + sdfYMD_HHMMSS.format(new Date()) +
				".pdf");
		//
		if (this.indOrdenaDataNumeroProtocolo)
			executor = new MpExecutorRelatorio("/relatorios/mpRelatorio_boletos_impressos1.jasper", 
					this.response, parametros, "MpBoletos_Impressos1_" + 
					this.mpSeguranca.getLoginUsuario() + "_" + sdfYMD_HHMMSS.format(new Date()) +
					".pdf");
		//
		Session session = manager.unwrap(Session.class);
		
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			MpFacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
	}

	@NotNull
	public Date getDataInicio() { return dataInicio; }
	public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

	@NotNull
	public Date getDataFim() { return dataFim; }
	public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
		
	public MpCartorioOficio getMpCartorioOficioSel() { return mpCartorioOficioSel; }
	public void setMpCartorioOficioSel(MpCartorioOficio mpCartorioOficioSel) { 
																		this.mpCartorioOficioSel = mpCartorioOficioSel; }
	public MpCartorioOficio getMpCartorioOficioX() { return mpCartorioOficioX; }
	public void setMpCartorioOficioX(MpCartorioOficio mpCartorioOficioX) { this.mpCartorioOficioX = mpCartorioOficioX; }
	public MpCartorioOficio getMpCartorioOficio1() { return mpCartorioOficio1; }
	public void setMpCartorioOficio1(MpCartorioOficio mpCartorioOficio1) { this.mpCartorioOficio1 = mpCartorioOficio1; }
	public MpCartorioOficio getMpCartorioOficio2() { return mpCartorioOficio2; }
	public void setMpCartorioOficio2(MpCartorioOficio mpCartorioOficio2) { this.mpCartorioOficio2 = mpCartorioOficio2; }
	public MpCartorioOficio getMpCartorioOficio3() { return mpCartorioOficio3; }
	public void setMpCartorioOficio3(MpCartorioOficio mpCartorioOficio3) { this.mpCartorioOficio3 = mpCartorioOficio3; }
	public MpCartorioOficio getMpCartorioOficio4() { return mpCartorioOficio4; }
	public void setMpCartorioOficio4(MpCartorioOficio mpCartorioOficio4) { this.mpCartorioOficio4 = mpCartorioOficio4; }
	
	public Boolean getIndOrdenaDataNumeroProtocolo() { return indOrdenaDataNumeroProtocolo; }
	public void setIndOrdenaDataNumeroProtocolo(Boolean indOrdenaDataNumeroProtocolo) { 
														this.indOrdenaDataNumeroProtocolo = indOrdenaDataNumeroProtocolo; }

}