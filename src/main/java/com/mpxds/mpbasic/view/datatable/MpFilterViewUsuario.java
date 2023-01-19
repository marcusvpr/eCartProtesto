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

import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.model.MpGrupo;
import com.mpxds.mpbasic.repository.MpGruposX;
import com.mpxds.mpbasic.service.MpUsuarioService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewUsuario implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpUsuarios mpUsuarios;

	@Inject
	private MpUsuarioService mpUsuarioService;

	@Inject
	private MpGruposX mpGruposX;

	//
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpUsuario> mpUsuarioList = new ArrayList<MpUsuario>();
     
    private List<MpUsuario> filteredMpUsuarioList = new ArrayList<MpUsuario>();

    private MpUsuario mpUsuarioSel;

	private Boolean indAdmin = false;
	private Boolean indCart = false;
	private Boolean indComarca = false;
	private Boolean indUser = false;

//    private LazyDataModel<MpUsuario> model;
    
    // ---
 
    @PostConstruct
    public void init() {
    	//
    	this.mpUsuarioList = this.mpUsuarios.listAll();

    	this.indAdmin = false;
		this.indCart = false;
		this.indComarca = false;
		this.indUser = false;
    }
    
	public void novoMpUsuario() { 
		//
		this.mpUsuarioSel = new MpUsuario();
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;

		this.indAdmin = false;
		this.indCart = false;
		this.indComarca = false;
		this.indUser = false;
		
//		MpAppUtil.PrintarLn("MpFilterViewUsuario.novoMpUsuario()");
	}
	
	public void apagaMpUsuario() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		//
		this.trataGrupo();
		
//		MpAppUtil.PrintarLn("MpFilterViewUsuario.apagaMpUsuario()");
	}
	
	public void alteraMpUsuario() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;		
		//
		this.trataGrupo();

//		MpAppUtil.PrintarLn("MpFilterViewUsuario.alteraMpUsuario()");
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
			this.mpUsuarioSel = mpUsuarioService.salvar(this.mpUsuarioSel);

			this.mpUsuarioList.add(this.mpUsuarioSel);
			//
			MpFacesUtil.addInfoMessage("Usuário... INCLUÍDO com sucesso! ( " + this.mpUsuarioSel.getEmail());
			//
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				if (!this.trataAtualizacaoGrupo()) return;

				MpUsuario mpUsuarioSelAnt = this.mpUsuarioSel;
				
				this.mpUsuarioSel = mpUsuarioService.salvar(this.mpUsuarioSel);
				
				this.mpUsuarioList.remove(mpUsuarioSelAnt);
				this.mpUsuarioList.add(this.mpUsuarioSel);
				//
				MpFacesUtil.addInfoMessage("Usuário... ALTERADO com sucesso! ( email = " + 
											this.mpUsuarioSel.getEmail() + " / Login = " + this.mpUsuarioSel.getLogin());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpUsuarioSel.setMpGrupos(null);

					this.mpUsuarios.remover(mpUsuarioSel);

					this.mpUsuarioList.remove(this.mpUsuarioSel);
					//
					MpFacesUtil.addInfoMessage("Usuário... EXCLUIDO com sucesso! ( " + 
																			this.mpUsuarioSel.getEmail());
				}
		//
	}
	
	public void trataGrupo() {
		//
		this.indAdmin = false;
		this.indCart = false;
		this.indComarca = false;
		this.indUser = false;

		for (MpGrupo mpGrupo : this.mpUsuarioSel.getMpGrupos()) {
			//
			if (mpGrupo.getDescricao().equals("ADMINISTRADORES")) this.indAdmin = true;
			if (mpGrupo.getDescricao().equals("CARTORIOS")) this.indCart = true;
			if (mpGrupo.getDescricao().equals("COMARCAS")) this.indComarca = true;
			if (mpGrupo.getDescricao().equals("USUARIOS")) this.indUser= true;
		}
	}

	public Boolean trataAtualizacaoGrupo() {		
		//
		if (this.indAdmin == false && this.indCart == false  && this.indComarca == false && this.indUser == false) {
			//
			MpFacesUtil.addInfoMessage("Selecionar Grupo Usuário !");
			return false;
		}

		this.mpUsuarioSel.getMpGrupos().clear();
		
		if (this.indAdmin == true) {
			MpGrupo mpGrupo = mpGruposX.porNome("ADMINISTRADORES");
			if (null == mpGrupo) {
				//
				MpFacesUtil.addInfoMessage("Grupo Usuário (Error... ! ( ADMINISTRADORES");
				return false;
			}
			this.mpUsuarioSel.getMpGrupos().add(mpGrupo);
		}
		if (this.indCart == true) {
			MpGrupo mpGrupo = mpGruposX.porNome("CARTORIOS");
			if (null == mpGrupo) {
				//
				MpFacesUtil.addInfoMessage("Grupo Usuário (Error... ! ( CARTORIOS");
				return false;
			}
			this.mpUsuarioSel.getMpGrupos().add(mpGrupo);
		}
		if (this.indComarca == true) {
			MpGrupo mpGrupo = mpGruposX.porNome("COMARCAS");
			if (null == mpGrupo) {
				//
				MpFacesUtil.addInfoMessage("Grupo Usuário (Error... ! ( COMARCAS");
				return false;
			}
			this.mpUsuarioSel.getMpGrupos().add(mpGrupo);
		}
		if (this.indUser == true) {
			MpGrupo mpGrupo = mpGruposX.porNome("USUARIOS");
			if (null == mpGrupo) {
				//
				MpFacesUtil.addInfoMessage("Grupo Usuário (Error... ! ( USUARIOS");
				return false;
			}
			this.mpUsuarioSel.getMpGrupos().add(mpGrupo);
		}
		//
		return true;
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
    
	public MpUsuario getMpUsuarioSel() { return mpUsuarioSel; }
	public void setMpUsuarioSel(MpUsuario mpUsuarioSel) {this.mpUsuarioSel = mpUsuarioSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpUsuario> getMpUsuarioList() { return mpUsuarioList; }
	public void setMpUsuarioList(List<MpUsuario> mpUsuarioList) {
															this.mpUsuarioList = mpUsuarioList; }

    public List<MpUsuario> getFilteredMpUsuarioList() { return filteredMpUsuarioList; }
    public void setFilteredMpUsuarioList(List<MpUsuario> filteredMpUsuarioList) {
        											this.filteredMpUsuarioList = filteredMpUsuarioList; }
    
//	public Boolean isIndAdmin() { return indAdmin; }
	public Boolean getIndAdmin() { return indAdmin; }
	public void setIndAdmin(Boolean indAdmin) { this.indAdmin = indAdmin; }
	
//	public Boolean isIndCart() { return indCart; }
	public Boolean getIndCart() { return indCart; }
	public void setIndCart(Boolean indCart) { this.indCart = indCart; }
	
//	public Boolean isIndComarca() { return indComarca; }
	public Boolean getIndComarca() { return indComarca; }
	public void setIndComarca(Boolean indComarca) { this.indComarca = indComarca; }
	
//	public Boolean isIndUser() { return indUser; }
	public Boolean getIndUser() { return indUser; }
	public void setIndUser(Boolean indUser) { this.indUser = indUser; }
    
}