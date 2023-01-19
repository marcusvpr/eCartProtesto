package com.mpxds.mpbasic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "wp_users")
public class MpUser implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
    private Long id;
	private String user_login;
	private String user_pass;
	private String user_nicename;
	private String user_email;
	private String user_url;
	private String user_registered;
	private String user_activation_key;
	private String user_status;
	private String display_name;
	
	private List<MpGrupo> mpGrupos = new ArrayList<>();
	
	// ---

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getUser_login() { return user_login; }
	public void setUser_login(String user_login) { this.user_login = user_login; }

	public String getUser_pass() { return user_pass; }
	public void setUser_pass(String user_pass) { this.user_pass = user_pass; }

	public String getUser_nicename() { return user_nicename; }
	public void setUser_nicename(String user_nicename) { this.user_nicename = user_nicename; }

	public String getUser_email() { return user_email; }
	public void setUser_email(String user_email) { this.user_email = user_email; }

	public String getUser_url() { return user_url; }
	public void setUser_url(String user_url) { this.user_url = user_url; }

	public String getUser_registered() { return user_registered; }
	public void setUser_registered(String user_registered) { this.user_registered = user_registered; }
	
	public String getUser_activation_key() { return user_activation_key; }
	public void setUser_activation_key(String user_activation_key) { 
															this.user_activation_key = user_activation_key; }
	
	public String getUser_status() { return user_status; }
	public void setUser_status(String user_status) { this.user_status = user_status; }
	
	public String getDisplay_name() { return display_name; }
	public void setDisplay_name(String display_name) { this.display_name = display_name; }
		
}