package com.mpxds.mpbasic.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MpTitulos")
public class MpTitulosDTO {
	//
	List<MpTituloDTO> mpTituloDTOList = new ArrayList<MpTituloDTO>();

	public List<MpTituloDTO> getMpTituloDTOList() { return mpTituloDTOList; }
	public void setMpTituloDTOList(List<MpTituloDTO> mpTituloDTOList) { this.mpTituloDTOList = mpTituloDTOList; }
	
}