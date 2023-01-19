package com.mpxds.mpbasic.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
 
public class MpCustomPasswordEncoder implements PasswordEncoder {
	//
	private int workload = 12;
	
    @Override
    public String encode(CharSequence rawPassword) {
    	//
    	String salt = BCrypt.gensalt(this.workload);
    	
        String hashed = BCrypt.hashpw(rawPassword.toString(), salt);
 
        return hashed;
    }
 
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	//
    	return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

}