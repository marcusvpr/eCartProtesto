package com.mpxds.mpbasic.repository.filter;

import java.io.Serializable;

public class MpContatoSiteFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String email;
	
	// ---

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
}