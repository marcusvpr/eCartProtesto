package com.mpxds.mpbasic.model.ws.intima21;

import java.util.ArrayList;
import java.util.List;

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "intimacoes")
public class Intimacoes {
	//
	private List<Intimacao> intimacaoList = new ArrayList<Intimacao>();

	//

	public List<Intimacao> getIntimacao() { return intimacaoList; }
	public void setIntimacao(List<Intimacao> intimacaoList) { this.intimacaoList = intimacaoList; }

}