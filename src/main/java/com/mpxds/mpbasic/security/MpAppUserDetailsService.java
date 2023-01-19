package com.mpxds.mpbasic.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mpxds.mpbasic.model.MpGrupo;
import com.mpxds.mpbasic.model.MpUser;
import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.repository.MpUsers;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;

public class MpAppUserDetailsService implements UserDetailsService {
	//
	private MpUsuarios mpUsuarios;
	private MpUsuario mpUsuario;
	
	@Override
	public UserDetails loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
		//
		this.mpUsuarios = MpCDIServiceLocator.getBean(MpUsuarios.class);
		this.mpUsuario = this.mpUsuarios.porLoginEmail(loginEmail);

		MpUsers mpUsers = MpCDIServiceLocator.getBean(MpUsers.class);
		MpUser mpUser = mpUsers.porLoginEmail(loginEmail);
		//
		MpUsuarioSistema user = null;
		
//		System.out.println(" MpAppUserDetailsService.loadUserByUsername() - 000 ( " + loginEmail);		
		
		if (this.mpUsuario != null) {
			user = new MpUsuarioSistema(mpUsuario, this.getMpGrupos(mpUsuario));
		} else {
			//
			if (mpUser != null) {
				//
				user = new MpUsuarioSistema(mpUser, this.getMpGrupos(mpUser));
			} else {
				//
//				System.out.println(" MpAppUserDetailsService.loadUserByUsername() - UsuarioNãoEncontrado! ( " + loginEmail);
				//
				throw new UsernameNotFoundException("Usuário não encontrado.");
			}
		}
		//
//		System.out.println(" MpAppUserDetailsService.loadUserByUsername() - 001 ( ");		
		//
		return user;
	}

	private Collection<? extends GrantedAuthority> getMpGrupos(MpUsuario mpUsuario) {
		//
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (MpGrupo mpGrupo : mpUsuario.getMpGrupos()) {
			//
			authorities.add(new SimpleGrantedAuthority("ROLE_" + mpGrupo.getNome().toUpperCase()));
		}
		//
		return authorities;
	}

	private Collection<? extends GrantedAuthority> getMpGrupos(MpUser mpUser) {
		//
		if (null == this.mpUsuario) { // Localiza usuário básico !
			//
			this.mpUsuario = this.mpUsuarios.porLoginEmail("mpUsuarioBasico");
			if (null == this.mpUsuario)
				throw new UsernameNotFoundException("Erro-3103 (Usuário não encontrado. Contactar o Suporte)");			
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (MpGrupo mpGrupo : this.mpUsuario.getMpGrupos()) {
			//
			authorities.add(new SimpleGrantedAuthority("ROLE_" + mpGrupo.getNome().toUpperCase()));
		}
		//
		return authorities;
	}

}
