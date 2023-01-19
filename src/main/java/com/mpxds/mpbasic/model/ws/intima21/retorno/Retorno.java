package com.mpxds.mpbasic.model.ws.intima21.retorno;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "relatorio")
public class Retorno {
	//
	@XmlElement(name = "intimacao")
	private List<Intimacao> intimacaoList = new ArrayList<Intimacao>();

	// Getter Methods

	public List<Intimacao> getIntimacaos() {
		return intimacaoList;
	}

	// Setter Methods

	public void setIntimacaos(List<Intimacao> intimacaoList) {
		this.intimacaoList = intimacaoList;
	}
}
