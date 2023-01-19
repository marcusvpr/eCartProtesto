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

import com.mpxds.mpbasic.model.MpGrupo;
import com.mpxds.mpbasic.repository.MpGruposX;
import com.mpxds.mpbasic.service.MpGrupoService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewGrupo implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpGrupoService mpGrupoService;

	@Inject
	private MpGruposX mpGruposX;

	//
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpGrupo> mpGrupoList = new ArrayList<MpGrupo>();
     
    private List<MpGrupo> filteredMpGrupoList = new ArrayList<MpGrupo>();

    private MpGrupo mpGrupoSel;
    
    // ---
 
    @PostConstruct
    public void init() {
    	//
    	this.mpGrupoList = this.mpGruposX.mpGrupoList();
    }
    
	public void novoMpGrupo() { 
		//
		this.mpGrupoSel = new MpGrupo();
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;
	}
	
	public void apagaMpGrupo() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		//
	}
	
	public void alteraMpGrupo() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;		
		//
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
			this.mpGrupoSel = mpGrupoService.salvar(this.mpGrupoSel);

			this.mpGrupoList.add(this.mpGrupoSel);
			//
			MpFacesUtil.addInfoMessage("Grupo... INCLUÍDO com sucesso! ( " + this.mpGrupoSel.getNome());
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				MpGrupo mpGrupoSelAnt = this.mpGrupoSel;
				
				this.mpGrupoSel = mpGrupoService.salvar(this.mpGrupoSel);
				
				this.mpGrupoList.remove(mpGrupoSelAnt);
				this.mpGrupoList.add(this.mpGrupoSel);
				//
				MpFacesUtil.addInfoMessage("Sistema Configuração... ALTERADO com sucesso! ( " + 
																						this.mpGrupoSel.getNome());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpGruposX.remover(mpGrupoSel);

					this.mpGrupoList.remove(this.mpGrupoSel);
					//
					MpFacesUtil.addInfoMessage("Usuário... EXCLUIDO com sucesso! ( " + this.mpGrupoSel.getNome());
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
    
	public MpGrupo getMpGrupoSel() { return mpGrupoSel; }
	public void setMpGrupoSel(MpGrupo mpGrupoSel) {this.mpGrupoSel = mpGrupoSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpGrupo> getMpGrupoList() { return mpGrupoList; }
	public void setMpGrupoList(List<MpGrupo> mpGrupoList) { this.mpGrupoList = mpGrupoList; }

    public List<MpGrupo> getFilteredMpGrupoList() { return filteredMpGrupoList; }
    public void setFilteredMpGrupoList(List<MpGrupo> filteredMpGrupoList) {
        											this.filteredMpGrupoList = filteredMpGrupoList; }
    
}