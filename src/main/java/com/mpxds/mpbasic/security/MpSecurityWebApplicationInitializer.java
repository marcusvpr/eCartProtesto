package com.mpxds.mpbasic.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(1)
public class MpSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	//
	public MpSecurityWebApplicationInitializer() {
		//
		super(MpSecurityConfig.class);
	}
	
}