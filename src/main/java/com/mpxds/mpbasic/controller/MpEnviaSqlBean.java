package com.mpxds.mpbasic.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.mpxds.mpbasic.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpEnviaSqlBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

    private String comandoSQL = "";
    private String resultSQL;
    private Boolean indResultSQL = true ;
    //
    private String msgErro = "OK";
    private String result = "";
    

	// -------------------------------- Inicio ------------------------------------

	public MpEnviaSqlBean() {
		//
		System.out.println("MpEnviaSqlBean() - Entrou.000"); 
		//
	}
	
    public void executaSql() {
		//
		System.out.println("MpEnviaSqlBean.executaSql() - Entrou.000"); 
        //
    	if (null == this.comandoSQL || this.comandoSQL.isEmpty()) {
    		//
    		MpFacesUtil.addInfoMessage("Informar comando SQL !");
    		//
    		return;
    	}
    	//
    	if (comandoSQL.toLowerCase().indexOf("alter table") >= 0 ||
    		comandoSQL.toLowerCase().indexOf("create table") >= 0 ||
    		comandoSQL.toLowerCase().indexOf("drop table") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("update") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("delete from") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("insert into") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("grant") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("revoke") >= 0 ||
    	    comandoSQL.toLowerCase().indexOf("select") >= 0 ||
    		comandoSQL.toLowerCase().indexOf("set table") >= 0) {
    		this.result = "";
    	} else {
    		//
    		MpFacesUtil.addInfoMessage("Informar um comando SQL válido ( " +
    							"ALTER CREATE DROP UPDATE INSERT DELETE SET SELECT) !");
    		return;
    	}
    	//
        try {
			this.manager.getTransaction().begin();
        	
        	this.manager.createNativeQuery(comandoSQL).executeUpdate();
        	//
        	String mensagem = "MpEnviaSqlBean.Executado - msgErro = ( " + msgErro + " )\n" + 
				  	 								" ( comandoSQL = " + comandoSQL + " )\n" + 
				  	 								" ( result = " + result + " )";
        	//
    		MpFacesUtil.addInfoMessage(mensagem);
   	      	//
		} catch (Exception e) {
			//
			this.manager.getTransaction().rollback();
			
    		MpFacesUtil.addInfoMessage("SQL Exception ( e " + e);
        } finally {
        	//
            if (this.manager.getTransaction().isActive())
            	this.manager.getTransaction().commit();
        }	
    }

    public void onShowExemploSql() {
		//
		System.out.println("MpEnviaSqlBean.onShowExemploSql() - Entrou.000"); 

    	//
        // Alter Table commands
    	//
    	MpFacesUtil.addInfoMessage("Exemplos SQL: \n\n" + 
        		"ALTER TABLE tabela RENAME TO tabelaRenamed \n" +  
        		"ALTER TABLE tabela ADD CONSTRAINT con1 CHECK (i6 > 4) \n" +
        		"ALTER TABLE tabela ADD COLUMN vco1 VARCHAR(NN) \n" +
        		"ALTER TABLE tabela DROP COLUMN vco1 \n" +
        		"ALTER TABLE tabela ADD COLUMN vco1 VARCHAR(NN) \n" +
        		"ALTER TABLE tabela ALTER COLUMN vco1 RENAME TO j1 \n" +
        		"ALTER TABLE tabela DROP CONSTRAINT con1 \n" + 
        		"ALTER TABLE tabela DROP CONSTRAINT tstfk \n" +
        		"ALTER TABLE tabela ADD CONSTRAINT tstfk FOREIGN KEY(i7) REFERENCES primarytbl (i8) \n" +
        		"ALTER TABLE tabela ADD CONSTRAINT ucons9 UNIQUE (i9) \n\n" +
        		// Drop table command
        		"DROP TABLE tabela \n\n" +
        		// Set table readonly command
        		"SET TABLE tabela READONLY true \n" +
        		"SET TABLE tabela READONLY false \n\n" +
        		// Create table commands
        		"CREATE TABLE tabela (i INT, vc VARCHAR) \n" +
        		"CREATE CACHED TABLE tabela (i INT, vc VARCHAR) \n" +
        		"CREATE TABLE tabela (i6 INT, vc6 VARCHAR, CONSTRAINT uconsz UNIQUE(i6)) \n" +
        		"CREATE TABLE tabela (i7 INT, vc7 VARCHAR, "
        				+ "CONSTRAINT tstfkz FOREIGN KEY (i7) REFERENCES primarytbl (i8)) \n\n" +
        		// Update command
        		"UPDATE tabela SET vc = 'eleven' WHERE i = 1 \n\n" +
        		// delete
        		"DELETE FROM tabela WHERE i = 1 \n\n" +
        		// grant, revoke
        		"GRANT ALL ON tabela TO tstuser \n" +
        		"REVOKE ALL ON tabela FROM tstuser");
        //
    }
    
    // -------------------------------------
	   
    public String getComandoSQL() { return this.comandoSQL; }
    public void setComandoSQL(String newComandoSQL) { this.comandoSQL = newComandoSQL; }

    public String getResultSQL() { return this.resultSQL; }
    public void setResultSQL(String newResultSQL) { this.resultSQL = newResultSQL; }

    public Boolean getIndResultSQL() { return this.indResultSQL; }
    public void setIndResultSQL(Boolean newIndResultSQL) { this.indResultSQL = newIndResultSQL; }

}