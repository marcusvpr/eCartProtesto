package com.mpxds.mpbasic.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mpxds.mpbasic.model.MpUser;
import com.mpxds.mpbasic.model.MpUsuario;

public class MpUsuarioSistema extends User {
	//
	private static final long serialVersionUID = 1L;
	
	private MpUser mpUser;
	private MpUsuario mpUsuario;
	
	// ---
	
	public MpUsuarioSistema(MpUser mpUser, Collection<? extends GrantedAuthority> authorities) {
		//
		super(mpUser.getUser_email(), mpUser.getUser_pass(), authorities);
		this.mpUser = mpUser;
	}
	
	public MpUsuarioSistema(MpUsuario mpUsuario, Collection<? extends GrantedAuthority> authorities) {
		//
		super(mpUsuario.getEmail(), mpUsuario.getSenha(), authorities);
		this.mpUsuario = mpUsuario;
	}
	
	// ---

	public MpUser getMpUser() { return mpUser; }
	public MpUsuario getMpUsuario() { return mpUsuario; }

}
