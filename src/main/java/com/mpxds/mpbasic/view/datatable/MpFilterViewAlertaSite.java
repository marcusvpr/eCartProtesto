package com.mpxds.mpbasic.view.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
//import org.primefaces.model.LazyDataModel;

import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpAlertaSites;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpAlertaSiteService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewAlertaSite implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpAlertaSites mpAlertaSites;

	@Inject
	private MpAlertaSiteService mpAlertaSiteService;

	@Inject
	private MpSeguranca mpSeguranca;

	private String mpCartorioOficioSel;

	//
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpAlertaSite> mpAlertaSiteList = new ArrayList<MpAlertaSite>();
     
    private List<MpAlertaSite> filteredMpAlertaSiteList = new ArrayList<MpAlertaSite>();

    private MpAlertaSite mpAlertaSiteSel = new MpAlertaSite();
    
    // ---
 
    @PostConstruct
    public void init() {
    	//
        this.mpCartorioOficioSel = "X";

		if ( mpSeguranca.isUsuarioOf1())
	        this.mpCartorioOficioSel = MpCartorioOficio.Of1.getNumero();
		else
			if ( mpSeguranca.isUsuarioOf2())
		        this.mpCartorioOficioSel = MpCartorioOficio.Of2.getNumero();
			else
				if ( mpSeguranca.isUsuarioOf3())
			        this.mpCartorioOficioSel = MpCartorioOficio.Of3.getNumero();
				else
					if ( mpSeguranca.isUsuarioOf4())
				        this.mpCartorioOficioSel = MpCartorioOficio.Of4.getNumero();
		//
//		System.out.println("MpFilterViewAlertaSite.init() ( " + this.mpCartorioOficioSel);

		if (this.mpCartorioOficioSel.equals("X"))
        	this.mpAlertaSiteList = this.mpAlertaSites.listAll();
        else
        	this.mpAlertaSiteList = this.mpAlertaSites.listAllOficio(this.mpCartorioOficioSel);
		//
    }
    
	public void novoMpAlertaSite() { 
		//
		this.mpAlertaSiteSel = new MpAlertaSite();
		
		this.mpAlertaSiteSel.setNumeroOficio(this.mpCartorioOficioSel);
		this.mpAlertaSiteSel.setDataAlertaIni(new Date());
		this.mpAlertaSiteSel.setDataAlertaFim(new Date());
		this.mpAlertaSiteSel.setAlertaMsg("");
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;
		//
//		MpAppUtil.PrintarLn("MpFilterViewAlertaSite.novoMpAlertaSite()");
	}
	
	public void apagaMpAlertaSite() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		//		
//		MpAppUtil.PrintarLn("MpFilterViewAlertaSite.apagaMpAlertaSite()");
	}
	
	public void alteraMpAlertaSite() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;		
		//
//		MpAppUtil.PrintarLn("MpFilterViewAlertaSite.alteraMpAlertaSite()");
	}
	
	public void mpGrava() {
		//
		try {
			this.salvar();
			//
		} catch (Exception e) {
			//
			MpFacesUtil.addInfoMessage("Erro na Gravação! ( " + e.toString());
			return;
		}
		//
		this.txtModoTela = "";
	}
	
	public void salvar() {
		//	
		if (this.txtModoTela.equals("( Novo )")) {
			//
			this.mpAlertaSiteSel = mpAlertaSiteService.salvar(this.mpAlertaSiteSel);

			this.mpAlertaSiteList.add(this.mpAlertaSiteSel);
			//
			MpFacesUtil.addInfoMessage("Alerta Site... INCLUÍDO com sucesso! ( " + 
											this.mpAlertaSiteSel.getNumeroOficio() + " / " +
											this.mpAlertaSiteSel.getDataAlertaIniSDF());
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				MpAlertaSite mpAlertaSiteSelAnt = this.mpAlertaSiteSel;
				
				this.mpAlertaSiteSel = mpAlertaSiteService.salvar(this.mpAlertaSiteSel);
				
				this.mpAlertaSiteList.remove(mpAlertaSiteSelAnt);
				this.mpAlertaSiteList.add(this.mpAlertaSiteSel);
				//
				MpFacesUtil.addInfoMessage("Alerta Site... ALTERADO com sucesso! ( Oficio = " + 
						this.mpAlertaSiteSel.getNumeroOficio() + " / " +
						this.mpAlertaSiteSel.getDataAlertaIniSDF());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpAlertaSites.remover(mpAlertaSiteSel);

					this.mpAlertaSiteList.remove(this.mpAlertaSiteSel);
					//
					MpFacesUtil.addInfoMessage("Alerta Site... EXCLUIDO com sucesso! ( " + 
							this.mpAlertaSiteSel.getNumeroOficio() + " / " +
							this.mpAlertaSiteSel.getDataAlertaIniSDF());
				}
		//
//		System.out.println("MpFilterViewAlertaSite.salvar().............. ( " + 
//				this.txtModoTela + " / " +
//				this.mpAlertaSiteSel.getNumeroOficio() + " / " +
//				this.mpAlertaSiteSel.getDataAlertaIniSDF());
	}
	
	public void posProcessarXls(Object documento) {
		//
		HSSFWorkbook planilha = (HSSFWorkbook) documento;
		HSSFSheet folha = planilha.getSheetAt(0);
		HSSFRow cabecalho = folha.getRow(0);
		HSSFCellStyle estiloCelula = planilha.createCellStyle();
		Font fonteCabecalho = planilha.createFont();
		
		fonteCabecalho.setColor(IndexedColors.WHITE.getIndex());
		fonteCabecalho.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fonteCabecalho.setFontHeightInPoints((short) 16);
		
		estiloCelula.setFont(fonteCabecalho);
		estiloCelula.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		estiloCelula.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		for (int i = 0; i < cabecalho.getPhysicalNumberOfCells(); i++) {
			cabecalho.getCell(i).setCellStyle(estiloCelula);
		}
	}
    
    // ---
    
	public MpAlertaSite getMpAlertaSiteSel() { return mpAlertaSiteSel; }
	public void setMpAlertaSiteSel(MpAlertaSite mpAlertaSiteSel) {this.mpAlertaSiteSel = mpAlertaSiteSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpAlertaSite> getMpAlertaSiteList() { return mpAlertaSiteList; }
	public void setMpAlertaSiteList(List<MpAlertaSite> mpAlertaSiteList) {
															this.mpAlertaSiteList = mpAlertaSiteList; }

    public List<MpAlertaSite> getFilteredMpAlertaSiteList() { return filteredMpAlertaSiteList; }
    public void setFilteredMpAlertaSiteList(List<MpAlertaSite> filteredMpAlertaSiteList) {
        											this.filteredMpAlertaSiteList = filteredMpAlertaSiteList; }    
}