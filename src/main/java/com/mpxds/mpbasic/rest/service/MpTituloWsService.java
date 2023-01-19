package com.mpxds.mpbasic.rest.service;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.rest.model.MpResponse;
import com.mpxds.mpbasic.rest.model.MpTituloWs;

public interface MpTituloWsService {
	//
	public MpResponse addMpTituloWs(MpTituloWs t);
	
	public MpResponse deleteMpTituloWs(int id);
	
	public MpTituloWs getMpTituloWs(int id);
	
	public MpTituloWs[] getAllMpTituloWss();
	
	public MpTituloWs getMpTituloWs(MpTitulo mpTitulo);
}