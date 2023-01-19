package com.mpxds.mpbasic.util.mail;

import javax.mail.PasswordAuthentication;

public class MpSMTPAuthenticator extends javax.mail.Authenticator {
	//
    public PasswordAuthentication getPasswordAuthentication() {
    	//
        String username = "suporte@protestorjcapital.com.br";
        String password = "@SuporteMpxds2018";
        //
        return new PasswordAuthentication(username, password);
    }
	
}
