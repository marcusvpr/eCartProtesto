package com.mpxds.mpbasic.rest.service;

import com.mpxds.mpbasic.rest.model.MpPerson;
import com.mpxds.mpbasic.rest.model.MpResponse;

public interface MpPersonService {
	//
	public MpResponse addMpPerson(MpPerson p);
	
	public MpResponse deleteMpPerson(int id);
	
	public MpPerson getMpPerson(int id);
	
	public MpPerson[] getAllMpPersons();

}