package com.mpxds.mpbasic.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

//import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.repository.MpBoletoLogs;
import com.mpxds.mpbasic.security.MpUsuarioLogado;
import com.mpxds.mpbasic.security.MpUsuarioSistema;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped // @RequestScoped
public class MpGraficoUsuarioNewsBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");
	
	@Inject
	private MpUsuarios mpUsuarios;
	
	@Inject
	private MpBoletoLogs mpBoletoLogs;
	
	@Inject
	@MpUsuarioLogado
	private MpUsuarioSistema mpUsuarioLogado;
	
	private LineChartModel model;

	private Date dataInicial;
	private Date dataFinal;
	
	private Boolean indGeraGrafico = false;
	private Integer diasIntervalo = 25;
	
	// -----------------
	
	public void preRender() {
		//
		if (this.indGeraGrafico) return;
		//
		Calendar dataIni = Calendar.getInstance();
		
		dataIni.setTime(new Date());
		dataIni.add(Calendar.DAY_OF_MONTH, -14);
		
		this.dataInicial = dataIni.getTime();
		
		this.dataFinal = new Date();
		// 
		this.model = new LineChartModel();
		
		this.model.setTitle("Usuários Internet");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);
		
		this.model.getAxes().put(AxisType.X, new CategoryAxis());
		
		if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("1of") >= 0)
			this.adicionarSerie("1");		
		else	
			if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("2of") >= 0)
				this.adicionarSerie("2");		
			else	
				if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("3of") >= 0)
					this.adicionarSerie("3");		
				else	
					if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("4of") >= 0)
						this.adicionarSerie("4");		
					else	
						this.adicionarSerie();		
	}
	
	private void adicionarSerie(String numeroOficio) {
		//
		Map<Date, Long> valoresPorDataXOf = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo,
																					 				numeroOficio);
		
		ChartSeries seriesXOf = new ChartSeries();
		seriesXOf.setLabel(numeroOficio + "ºOficio");
		
		for (Date data : valoresPorDataXOf.keySet()) {
			seriesXOf.set(DATE_FORMAT.format(data), valoresPorDataXOf.get(data));
		}
		this.model.addSeries(seriesXOf);
		//
		Map<Date, Long> valoresPorDataWeb = this.mpUsuarios.valoresTotaisPorDataWeb(this.diasIntervalo);

		ChartSeries seriesWeb = new ChartSeries();
		seriesWeb.setLabel("Usuários");

		for (Date data : valoresPorDataWeb.keySet()) {
			seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
		}
		this.model.addSeries(seriesWeb);
	}
	
	private void adicionarSerie() {
		//
		Map<Date, Long> valoresPorData1Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "1");
		Map<Date, Long> valoresPorData2Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "2");
		Map<Date, Long> valoresPorData3Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "3");
		Map<Date, Long> valoresPorData4Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "4");
		
		Map<Date, Long> valoresPorDataWeb = this.mpUsuarios.valoresTotaisPorDataWeb(this.diasIntervalo);
		
		ChartSeries series1Of = new ChartSeries();
		series1Of.setLabel("1ºOficio");
		ChartSeries series2Of = new ChartSeries();
		series2Of.setLabel("2ºOficio");
		ChartSeries series3Of = new ChartSeries();
		series3Of.setLabel("3ºOficio");
		ChartSeries series4Of = new ChartSeries();
		series4Of.setLabel("4ºOficio");

		ChartSeries seriesUser = new ChartSeries();
		seriesUser.setLabel("Usuários");
		
		for (Date data : valoresPorData1Of.keySet()) {
			series1Of.set(DATE_FORMAT.format(data), valoresPorData1Of.get(data));
		}
		for (Date data : valoresPorData2Of.keySet()) {
			series2Of.set(DATE_FORMAT.format(data), valoresPorData2Of.get(data));
		}
		for (Date data : valoresPorData3Of.keySet()) {
			series3Of.set(DATE_FORMAT.format(data), valoresPorData3Of.get(data));
		}
		for (Date data : valoresPorData4Of.keySet()) {
			series4Of.set(DATE_FORMAT.format(data), valoresPorData4Of.get(data));
		}

		for (Date data : valoresPorDataWeb.keySet()) {
			seriesUser.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
		}
		//
		this.model.addSeries(series1Of);
		this.model.addSeries(series2Of);
		this.model.addSeries(series3Of);
		this.model.addSeries(series4Of);

		this.model.addSeries(seriesUser);
	}

	public void geraGrafico() {
		//
//		System.out.println("MpGraficoBoletoLogsCotacaoBean.geraGrafico() ( " + this.dataInicial + " / " +
//																						this.dataFinal);
		//
		this.indGeraGrafico = true;
		//
		String msg = "";
		if (null == this.dataInicial) msg = msg + "(Data Inicial)";
		if (null == this.dataFinal) msg = msg + "(Data Final)";

		if (!msg.isEmpty()) {
			MpFacesUtil.addInfoMessage(msg + "... inválida(s)");
			return;
		}
		
		if (this.dataInicial.after(dataFinal)) {
			MpFacesUtil.addInfoMessage("Data Inicial maior Data Final!");
			return;
		}
		//
		this.model = new LineChartModel();
		
		this.model.setTitle("Usuários WEB :");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);
		
		this.model.getAxes().put(AxisType.X, new CategoryAxis());
		//
		Map<Date, Long> valoresPorData1Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "1");
		Map<Date, Long> valoresPorData2Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "2");
		Map<Date, Long> valoresPorData3Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "3");
		Map<Date, Long> valoresPorData4Of = this.mpBoletoLogs.valoresTotaisPorDataWeb(this.diasIntervalo, "4");
		
		Map<Date, Long> valoresPorDataWeb = this.mpUsuarios.valoresTotaisPorDataWeb(this.diasIntervalo);
		//
		if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("1of") >= 0) {
			ChartSeries series1Of = new ChartSeries();
			series1Of.setLabel("1ºOficio");

			for (Date data : valoresPorData1Of.keySet()) {
				series1Of.set(DATE_FORMAT.format(data), valoresPorData1Of.get(data));
			}
			this.model.addSeries(series1Of);
			//
			ChartSeries seriesWeb = new ChartSeries();
			seriesWeb.setLabel("Usuários");

			for (Date data : valoresPorDataWeb.keySet()) {
				seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
			}
			this.model.addSeries(seriesWeb);
		} else
			if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("2of") >= 0) {
				ChartSeries series2Of = new ChartSeries();
				series2Of.setLabel("2ºOficio");

				for (Date data : valoresPorData2Of.keySet()) {
					series2Of.set(DATE_FORMAT.format(data), valoresPorData2Of.get(data));
				}
				this.model.addSeries(series2Of);
				//
				ChartSeries seriesWeb = new ChartSeries();
				seriesWeb.setLabel("Usuários");

				for (Date data : valoresPorDataWeb.keySet()) {
					seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
				}
				this.model.addSeries(seriesWeb);
			} else
				if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("3of") >= 0) {
					ChartSeries series3Of = new ChartSeries();
					series3Of.setLabel("3ºOficio");

					for (Date data : valoresPorData3Of.keySet()) {
						series3Of.set(DATE_FORMAT.format(data), valoresPorData3Of.get(data));
					}
					this.model.addSeries(series3Of);
					//
					ChartSeries seriesWeb = new ChartSeries();
					seriesWeb.setLabel("Usuários");

					for (Date data : valoresPorDataWeb.keySet()) {
						seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
					}
					this.model.addSeries(seriesWeb);
				} else
					if (this.mpUsuarioLogado.getMpUsuario().getLogin().indexOf("4of") >= 0) {
						ChartSeries series4Of = new ChartSeries();
						series4Of.setLabel("4ºOficio");

						for (Date data : valoresPorData4Of.keySet()) {
							series4Of.set(DATE_FORMAT.format(data), valoresPorData4Of.get(data));
						}
						this.model.addSeries(series4Of);
						//
						ChartSeries seriesWeb = new ChartSeries();
						seriesWeb.setLabel("Usuários");

						for (Date data : valoresPorDataWeb.keySet()) {
							seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
						}
						this.model.addSeries(seriesWeb);
					} 
					else {
							ChartSeries series1Of = new ChartSeries();
							series1Of.setLabel("1ºOficio");
							ChartSeries series2Of = new ChartSeries();
							series2Of.setLabel("2ºOficio");
							ChartSeries series3Of = new ChartSeries();
							series3Of.setLabel("3ºOficio");
							ChartSeries series4Of = new ChartSeries();
							series4Of.setLabel("4ºOficio");

							ChartSeries seriesWeb = new ChartSeries();
							seriesWeb.setLabel("Usuários");
							//
							for (Date data : valoresPorData1Of.keySet()) {
								series1Of.set(DATE_FORMAT.format(data), valoresPorData1Of.get(data));
							}
							for (Date data : valoresPorData2Of.keySet()) {
								series2Of.set(DATE_FORMAT.format(data), valoresPorData2Of.get(data));
							}
							for (Date data : valoresPorData3Of.keySet()) {
								series3Of.set(DATE_FORMAT.format(data), valoresPorData3Of.get(data));
							}
							for (Date data : valoresPorData4Of.keySet()) {
								series4Of.set(DATE_FORMAT.format(data), valoresPorData4Of.get(data));
							}

							for (Date data : valoresPorDataWeb.keySet()) {
								seriesWeb.set(DATE_FORMAT.format(data), valoresPorDataWeb.get(data));
							}
							//
							this.model.addSeries(series1Of);
							this.model.addSeries(series2Of);
							this.model.addSeries(series3Of);
							this.model.addSeries(series4Of);

							this.model.addSeries(seriesWeb);
						}
		//
	}
	
	// ---
	
	public LineChartModel getModel() { return model; }

  	public Date getDataInicial() { return this.dataInicial; }
  	public void setDataInicial(Date newDataInicial) { this.dataInicial = newDataInicial; }

  	public Date getDataFinal() { return this.dataFinal; }
  	public void setDataFinal(Date newDataFinal) { this.dataFinal = newDataFinal; }
	
  	public Boolean getIndGeraGrafico() { return this.indGeraGrafico; }
  	public void setIndGeraGrafico(Boolean newIndGeraGrafico) { this.indGeraGrafico = newIndGeraGrafico; }

  	public Integer getDiasIntervalo() { return this.diasIntervalo; }
  	public void setDiasIntervalo(Integer newDiasIntervalo) { this.diasIntervalo = newDiasIntervalo; }
	
}
