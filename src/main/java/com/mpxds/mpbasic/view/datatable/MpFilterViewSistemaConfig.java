package com.mpxds.mpbasic.view.datatable;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;
import com.mpxds.mpbasic.service.MpSistemaConfigService;
import com.mpxds.mpbasic.util.MpAppUtil;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewSistemaConfig implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	@Inject
	private MpSistemaConfigService mpSistemaConfigService;
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpSistemaConfig> mpSistemaConfigList = new ArrayList<MpSistemaConfig>();
     
    private List<MpSistemaConfig> filteredMpSistemaConfigList = new ArrayList<MpSistemaConfig>();

    private MpSistemaConfig mpSistemaConfigSel;

//    private LazyDataModel<MpSistemaConfig> model;
    
    // ---
 
    @PostConstruct
    public void init() {
    	//
    	this.mpSistemaConfigList = this.mpSistemaConfigs.listAll();
    }
    
	public void novoMpSistemaConfig() { 
		//
		this.mpSistemaConfigSel = new MpSistemaConfig();
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;
		
		MpAppUtil.PrintarLn("MpFilterViewSistemaConfig.novoMpSistemaConfig()");
	}
	
	public void apagaMpSistemaConfig() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		
		MpAppUtil.PrintarLn("MpFilterViewSistemaConfig.apagaMpSistemaConfig()");
	}
	
	public void alteraMpSistemaConfig() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;
		
		MpAppUtil.PrintarLn("MpFilterViewSistemaConfig.alteraMpSistemaConfig()");
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
			this.mpSistemaConfigSel = mpSistemaConfigService.salvar(this.mpSistemaConfigSel);

			this.mpSistemaConfigList.add(this.mpSistemaConfigSel);
			//
			MpFacesUtil.addInfoMessage("Sistema Configuração... INCLUÍDO com sucesso! ( " + 
																			this.mpSistemaConfigSel.getParametro());
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				this.mpSistemaConfigSel = mpSistemaConfigService.salvar(this.mpSistemaConfigSel);
				//
				MpFacesUtil.addInfoMessage("Sistema Configuração... ALTERADO com sucesso! ( " + 
																			this.mpSistemaConfigSel.getParametro());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpSistemaConfigs.remover(mpSistemaConfigSel);

					this.mpSistemaConfigList.remove(this.mpSistemaConfigSel);
					//
					MpFacesUtil.addInfoMessage("Sistema Configuração... EXCLUIDO com sucesso! ( " + 
																			this.mpSistemaConfigSel.getParametro());
				}
		//
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
    
	public MpSistemaConfig getMpSistemaConfigSel() { return mpSistemaConfigSel; }
	public void setMpSistemaConfigSel(MpSistemaConfig mpSistemaConfigSel) { 
																this.mpSistemaConfigSel = mpSistemaConfigSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpSistemaConfig> getMpSistemaConfigList() { return mpSistemaConfigList; }
	public void setMpSistemaConfigList(List<MpSistemaConfig> mpSistemaConfigList) {
															this.mpSistemaConfigList = mpSistemaConfigList; }

    public List<MpSistemaConfig> getFilteredMpSistemaConfigList() { return filteredMpSistemaConfigList; }
    public void setFilteredMpSistemaConfigList(List<MpSistemaConfig> filteredMpSistemaConfigList) {
        											this.filteredMpSistemaConfigList = filteredMpSistemaConfigList; }
    
}