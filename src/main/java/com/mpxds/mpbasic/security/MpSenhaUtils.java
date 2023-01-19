package com.mpxds.mpbasic.security;

import java.io.Serializable;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder ;

public class MpSenhaUtils implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	// ---
	
	public static String gerarBCrypt ( String senha ) {
		//
		if ( senha == null ) {
			return senha ;
		}
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder ();
		//
		return bCryptEncoder . encode ( senha );
	}
	
	public static boolean senhaValida ( String senha , String senhaEncoded ) {
		//
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder ();
		
		return bCryptEncoder . matches ( senha , senhaEncoded );
	}
	
}