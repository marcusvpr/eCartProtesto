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

import com.mpxds.mpbasic.model.MpCargaControle;
import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.repository.MpCargaControles;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.service.MpCargaControleService;
import com.mpxds.mpbasic.util.jsf.MpFacesUtil;
 
@Named
@SessionScoped
public class MpFilterViewCargaControle implements Serializable {
	//
	private static final long serialVersionUID = 1L;
 
	@Inject
	private MpCargaControles mpCargaControles;

	@Inject
	private MpCargaControleService mpCargaControleService;

	@Inject
	private MpSeguranca mpSeguranca;

	private String mpCartorioOficioSel;

	//
	
	private String txtModoTela = "";
	private Boolean indEditavel = false;

	private List<MpCargaControle> mpCargaControleList = new ArrayList<MpCargaControle>();
     
    private List<MpCargaControle> filteredMpCargaControleList = new ArrayList<MpCargaControle>();

    private MpCargaControle mpCargaControleSel = new MpCargaControle();
    
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
//		System.out.println("MpFilterViewCargaControle.init() ( " + this.mpCartorioOficioSel);

		if (this.mpCartorioOficioSel.equals("X"))
        	this.mpCargaControleList = this.mpCargaControles.listAll();
        else
        	this.mpCargaControleList = this.mpCargaControles.listAllOficio(this.mpCartorioOficioSel);
		//
    }
    
	public void novoMpCargaControle() { 
		//
		this.mpCargaControleSel = new MpCargaControle();
		
		this.mpCargaControleSel.setNumeroOficio(this.mpCartorioOficioSel);
		this.mpCargaControleSel.setDataHoraIni(new Date());
		this.mpCargaControleSel.setDataHoraFim(new Date());
		this.mpCargaControleSel.setMensagem("");
		//
		this.txtModoTela = "( Novo )";
		this.indEditavel = false;
		//
//		MpAppUtil.PrintarLn("MpFilterViewCargaControle.novoMpCargaControle()");
	}
	
	public void apagaMpCargaControle() { 
		//
		this.txtModoTela = "( Exclusão )";
		this.indEditavel = true;
		//		
//		MpAppUtil.PrintarLn("MpFilterViewCargaControle.apagaMpCargaControle()");
	}
	
	public void alteraMpCargaControle() { 
		//
		this.txtModoTela = "( Edição )";
		this.indEditavel = false;		
		//
//		MpAppUtil.PrintarLn("MpFilterViewCargaControle.alteraMpCargaControle()");
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
		if (this.txtModoTela.equals("( Novo )")
		||  this.txtModoTela.equals("( Edição )")) {
			//
			String msgX = this.valida();
			if (!msgX.isEmpty()) {
				//
				MpFacesUtil.addInfoMessage("Verificar! ( " + msgX);
				return;
			}
		}
		//
		if (this.txtModoTela.equals("( Novo )")) {
			//
			this.mpCargaControleSel = mpCargaControleService.salvar(this.mpCargaControleSel);

			this.mpCargaControleList.add(this.mpCargaControleSel);
			//
			MpFacesUtil.addInfoMessage("Hora Site... INCLUÍDO com sucesso! ( " + 
											this.mpCargaControleSel.getNumeroOficio() + " / " +
											this.mpCargaControleSel.getDataHoraIniSDF());
		} else
			if (this.txtModoTela.equals("( Edição )")) {
				//
				MpCargaControle mpCargaControleSelAnt = this.mpCargaControleSel;
				
				this.mpCargaControleSel = mpCargaControleService.salvar(this.mpCargaControleSel);
				
				this.mpCargaControleList.remove(mpCargaControleSelAnt);
				this.mpCargaControleList.add(this.mpCargaControleSel);
				//
				MpFacesUtil.addInfoMessage("Hora Site... ALTERADO com sucesso! ( Oficio = " + 
						this.mpCargaControleSel.getNumeroOficio() + " / " +
						this.mpCargaControleSel.getDataHoraIniSDF());
			} else
				if (this.txtModoTela.equals("( Exclusão )")) {
					//
					this.mpCargaControles.remover(mpCargaControleSel);

					this.mpCargaControleList.remove(this.mpCargaControleSel);
					//
					MpFacesUtil.addInfoMessage("Hora Site... EXCLUIDO com sucesso! ( " + 
							this.mpCargaControleSel.getNumeroOficio() + " / " +
							this.mpCargaControleSel.getDataHoraIniSDF());
				}
		//
//		System.out.println("MpFilterViewCargaControle.salvar().............. ( " + 
//				this.txtModoTela + " / " +
//				this.mpCargaControleSel.getNumeroOficio() + " / " +
//				this.mpCargaControleSel.getDataHoraIniSDF());
	}
	
	public String valida() {
		//
		String msgX = "";
		
		if (null == this.mpCargaControleSel.getDataHoraIni())
			msgX = msgX + " ( Data Inicial ) ";
		if (null == this.mpCargaControleSel.getDataHoraFim())
			msgX = msgX + " ( Data Final ) ";
		if (null == this.mpCargaControleSel.getMensagem() 
		||  this.mpCargaControleSel.getMensagem().isEmpty())
			msgX = msgX + " (  Mensagem ) ";
		if (null == this.mpCargaControleSel.getIndRecorrente()) 
			msgX = msgX + " (  Recorrente ) ";
		if (null == this.mpCargaControleSel.getDataHoraIni()
		||  null == this.mpCargaControleSel.getDataHoraFim())
			assert(true); // nop
		else {
			//
			if (this.mpCargaControleSel.getDataHoraIni().after(this.mpCargaControleSel.getDataHoraFim()))
				msgX = msgX + " (  Data Inicial maior que Final ) ";
			else {
				//
		        List<MpCargaControle> mpCargaControleListX = this.mpCargaControles.porDataIniFim(
		        															this.mpCargaControleSel.getDataHoraIni());
	
		        for (MpCargaControle mpCargaControleX : mpCargaControleListX) {
		        	//
		        	if (mpCargaControleX.getNumeroOficio().contentEquals(this.mpCartorioOficioSel))
		        		assert(true);
		        	else {
		        		//
		            	msgX = msgX + "Data Incial sobrepondo ( Oficio = " + 
		            			mpCargaControleX.getNumeroOficio() + "\n/ " +
		            			mpCargaControleX.getDataHoraIniSDF() + "/ " +  
		            			mpCargaControleX.getDataHoraFimSDF() + "\n/ " +
		            			mpCargaControleX.getMensagem();
		            	//
		        	}
		        }
				//
		        mpCargaControleListX = this.mpCargaControles.porDataIniFim(this.mpCargaControleSel.getDataHoraFim());
	
		        for (MpCargaControle mpCargaControleX : mpCargaControleListX) {
		        	//
		        	if (mpCargaControleX.getNumeroOficio().contentEquals(this.mpCartorioOficioSel))
		        		assert(true);
		        	else {
		        		//
		            	msgX = msgX + "Data Final sobrepondo ( Oficio = " + 
		            			mpCargaControleX.getNumeroOficio() + "\n/ " +
		            			mpCargaControleX.getDataHoraIniSDF() + "/ " +  
		            			mpCargaControleX.getDataHoraFimSDF() + "\n/ " +
		            			mpCargaControleX.getMensagem();
		            	//
		        	}
		        }
			}
		}
		//
		return msgX;
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
    
	public MpCargaControle getMpCargaControleSel() { return mpCargaControleSel; }
	public void setMpCargaControleSel(MpCargaControle mpCargaControleSel) {this.mpCargaControleSel = mpCargaControleSel; }
	
	public String getTxtModoTela() { return txtModoTela; }
	public void setTxtModoTela(String txtModoTela) { this.txtModoTela = txtModoTela; }

	public boolean getIndEditavel() { return indEditavel; }
	public void setIndEditavel(Boolean indEditavel) { this.indEditavel = indEditavel; }
          
    public List<MpCargaControle> getMpCargaControleList() { return mpCargaControleList; }
	public void setMpCargaControleList(List<MpCargaControle> mpCargaControleList) {
															this.mpCargaControleList = mpCargaControleList; }

    public List<MpCargaControle> getFilteredMpCargaControleList() { return filteredMpCargaControleList; }
    public void setFilteredMpCargaControleList(List<MpCargaControle> filteredMpCargaControleList) {
        											this.filteredMpCargaControleList = filteredMpCargaControleList; }    
}