package com.mpxds.mpbasic.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MpBoletos")
public class MpBoletosDTO {
	//
	List<MpBoletoDTO> mpBoletoDTOList = new ArrayList<MpBoletoDTO>();

	public List<MpBoletoDTO> getMpBoletoDTOList() { return mpBoletoDTOList; }
	public void setMpBoletoDTOList(List<MpBoletoDTO> mpBoletoDTOList) { this.mpBoletoDTOList = mpBoletoDTOList; }
	
}