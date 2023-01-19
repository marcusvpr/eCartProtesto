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

import com.mpxds.mpbasic.model.MpNoticiaSite;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpNoticiaSites;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpNoticiaSiteService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewNoticiaSite implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpNoticiaSites mpNoticiaSites;

	@Inject
	private MpNoticiaSiteService mpNoticiaSiteService;

	@Inject
	private MpSeguranca mpSeguranca;

	private String mpCartorioOficioSel;

	//
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpNoticiaSite> mpNoticiaSiteList = new ArrayList<MpNoticiaSite>();
     
    private List<MpNoticiaSite> filteredMpNoticiaSiteList = new ArrayList<MpNoticiaSite>();

    private MpNoticiaSite mpNoticiaSiteSel = new MpNoticiaSite();
    
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
        if (this.mpCartorioOficioSel.equals("X"))
        	this.mpNoticiaSiteList = this.mpNoticiaSites.listAll();
        else
        	this.mpNoticiaSiteList = this.mpNoticiaSites.listAllOficio(this.mpCartorioOficioSel);
		//
    }
    
	public void novoMpNoticiaSite() { 
		//
		this.mpNoticiaSiteSel = new MpNoticiaSite();
		
		this.mpNoticiaSiteSel.setNumeroOficio(this.mpCartorioOficioSel);
		this.mpNoticiaSiteSel.setDataNoticia(new Date());
		this.mpNoticiaSiteSel.setAssunto("");		
		this.mpNoticiaSiteSel.setDescricao("");		
		this.mpNoticiaSiteSel.setUrl("");		
		this.mpNoticiaSiteSel.setIndAtivo(true);		
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;
		//
//		MpAppUtil.PrintarLn("MpFilterViewNoticiaSite.novoMpNoticiaSite()");
	}
	
	public void apagaMpNoticiaSite() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		//		
//		MpAppUtil.PrintarLn("MpFilterViewNoticiaSite.apagaMpNoticiaSite()");
	}
	
	public void alteraMpNoticiaSite() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;		
		//
//		MpAppUtil.PrintarLn("MpFilterViewNoticiaSite.alteraMpNoticiaSite()");
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
			this.mpNoticiaSiteSel = mpNoticiaSiteService.salvar(this.mpNoticiaSiteSel);

			this.mpNoticiaSiteList.add(this.mpNoticiaSiteSel);
			//
			MpFacesUtil.addInfoMessage("Noticia Site... INCLUÍDO com sucesso! ( " + 
											this.mpNoticiaSiteSel.getNumeroOficio() + " / " +
											this.mpNoticiaSiteSel.getDataNoticiaSDF());
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				MpNoticiaSite mpNoticiaSiteSelAnt = this.mpNoticiaSiteSel;
				
				this.mpNoticiaSiteSel = mpNoticiaSiteService.salvar(this.mpNoticiaSiteSel);
				
				this.mpNoticiaSiteList.remove(mpNoticiaSiteSelAnt);
				this.mpNoticiaSiteList.add(this.mpNoticiaSiteSel);
				//
				MpFacesUtil.addInfoMessage("Noticia Site... ALTERADO com sucesso! ( Oficio = " + 
						this.mpNoticiaSiteSel.getNumeroOficio() + " / " +
						this.mpNoticiaSiteSel.getDataNoticiaSDF());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpNoticiaSites.remover(mpNoticiaSiteSel);

					this.mpNoticiaSiteList.remove(this.mpNoticiaSiteSel);
					//
					MpFacesUtil.addInfoMessage("Noticia Site... EXCLUIDO com sucesso! ( " + 
							this.mpNoticiaSiteSel.getNumeroOficio() + " / " +
							this.mpNoticiaSiteSel.getDataNoticiaSDF());
				}
		//
//		System.out.println("MpFilterViewNoticiaSite.salvar().............. ( " + 
//				this.txtModoTela + " / " +
//				this.mpNoticiaSiteSel.getNumeroOficio() + " / " +
//				this.mpNoticiaSiteSel.getDataNoticiaIniSDF());
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
    
	public MpNoticiaSite getMpNoticiaSiteSel() { return mpNoticiaSiteSel; }
	public void setMpNoticiaSiteSel(MpNoticiaSite mpNoticiaSiteSel) {this.mpNoticiaSiteSel = mpNoticiaSiteSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpNoticiaSite> getMpNoticiaSiteList() { return mpNoticiaSiteList; }
	public void setMpNoticiaSiteList(List<MpNoticiaSite> mpNoticiaSiteList) {
															this.mpNoticiaSiteList = mpNoticiaSiteList; }

    public List<MpNoticiaSite> getFilteredMpNoticiaSiteList() { return filteredMpNoticiaSiteList; }
    public void setFilteredMpNoticiaSiteList(List<MpNoticiaSite> filteredMpNoticiaSiteList) {
        											this.filteredMpNoticiaSiteList = filteredMpNoticiaSiteList; }    
}