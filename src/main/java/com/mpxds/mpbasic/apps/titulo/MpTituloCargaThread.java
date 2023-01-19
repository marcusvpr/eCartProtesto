package com.mpxds.mpbasic.apps.titulo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import javax.faces.bean.RequestScoped;

@RequestScoped
public class MpTituloCargaThread extends Thread {
	//			
	private Boolean indCancel = false;
	
	private InputStream isX;    

	private Thread threadTitulo;
			
	// ---
	
    public void run() {
        //
        System.out.println("MpTituloCargaThread.run() - ( Thread : " + Thread.currentThread().getName());
    	//
    	try {
	    	//
	        Class.forName("org.hsqldb.jdbcDriver");
	        
	        Connection con = DriverManager.getConnection("jdbc:hsqldb:file:~/db/mpProtRJCap/mpProtRJCap_DB", "sa", "");
	     
	        Scanner scX = new Scanner(this.isX);
		    //
		    while (scX.hasNextLine()) {
		    	//
		    	if (indCancel) break;		    	
	    		//
		    	String sqlX = scX.nextLine();
		    	//
		   	    if (sqlX.isEmpty()) continue;
		   	    
//	   	    	012345678901234567890123456789012345
//	   	    	insert into mp_titulo (codigo_oficio
		   	    if (sqlX.indexOf("insert into mp_titulo (codigo_oficio") <  0) {
			    	System.out.println("MpTituloCargaThread.run() - ERROR ( sqlX : " + sqlX);
		   	    	continue;
		   	    }
		   	    
	            PreparedStatement stmt = con.prepareStatement(sqlX);
	            //
	            stmt.executeUpdate();
			    stmt.close();

//			    System.out.println("MpTituloCargaThread.run() - ( sqlX : " + sqlX);
		    }
		    //
		    scX.close();		    
	        // stm.execute("SHUTDOWN");
	        //
	    } catch(ClassNotFoundException e) {
	    	//
	        System.out.println("Erro ao carregar o driver JDBC. ( " + e);
	        //
	    } catch(SQLException e) {
	    	//
	        System.out.println("Erro de SQL: " + e);
	        e.printStackTrace();
	    }    
    }
        
    // ---
    
    public Boolean getIndCancel() { return indCancel; }
    public void setIndCancel(Boolean indCancel) { this.indCancel = indCancel; }
        
    public InputStream getIsX() { return isX; }
    public void setIsX(InputStream isX) { this.isX = isX; }
    
    public Thread getThreadTitulo() { return threadTitulo; }
    public void setThreadTitulo(Thread threadTitulo) { this.threadTitulo = threadTitulo; }
        
}
