package com.mpxds.mpbasic.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;

@Named
@ViewScoped
public class MpServicoCancelamentoBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	private String txtServicoCancelamento = "";

	// ---
    
	public void init() {
		//
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("txtServicoCancelamento");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		this.txtServicoCancelamento = mpSistemaConfig.getValorT();
    	}
    }
	  
	public String getTxtServicoCancelamento() { return txtServicoCancelamento; }
	public void setTxtServicoCancelamento(String txtServicoCancelamento) { 
															this.txtServicoCancelamento = txtServicoCancelamento; }

}