package com.mpxds.mpbasic.rest.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mpxds.mpbasic.model.MpBoletoLog;
import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpBoletoLogs;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
 
@Path("/mpUtilsWs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MpUtilsRestService {
	//
	private MpUsuarios mpUsuarios;
	private MpBoletoLogs mpBoletoLogs;

	@Context 
	ServletContext servletContext;

	// ---
	
	@GET
	@Path("/ajustaMpUsuario/get")
	public Response getAjustaMpUsuario() {
		//
		this.mpUsuarios = MpCDIServiceLocator.getBean(MpUsuarios.class);
		
		List<MpUsuario> mpUsuarios = this.mpUsuarios.mpUsuarioByLoginList();

		for (MpUsuario mpUsuarioX : mpUsuarios) {
			//
			if (mpUsuarioX.getLogin().indexOf("marcus") >= 0
			||  mpUsuarioX.getLogin().indexOf("prisco") >= 0
			||  mpUsuarioX.getLogin().indexOf("usuario") >= 0
			||  mpUsuarioX.getLogin().indexOf("1of") >= 0
			||  mpUsuarioX.getLogin().indexOf("2of") >= 0
			||  mpUsuarioX.getLogin().indexOf("3of") >= 0
			||  mpUsuarioX.getLogin().indexOf("4of") >= 0)
				mpUsuarioX.setIndUserWeb(false);
			else
				mpUsuarioX.setIndUserWeb(true);
			//
			this.mpUsuarios.guardar(mpUsuarioX);
		}
		//
		String output = "<total>"  + mpUsuarios.size() + "</total>";
		//
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/ajustaMpBoletoLog/get")
	public Response getAjustaMpBoletoLog() {
		//
		this.mpBoletoLogs = MpCDIServiceLocator.getBean(MpBoletoLogs.class);
		
		List<MpBoletoLog> mpBoletoLogs = this.mpBoletoLogs.listAll();

		for (MpBoletoLog mpBoletoLogX : mpBoletoLogs) {
			//
			if (mpBoletoLogX.getUsuarioNome().indexOf("marcus") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("prisco") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("usuario") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("1of") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("2of") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("3of") >= 0
			||  mpBoletoLogX.getUsuarioNome().indexOf("4of") >= 0)
				mpBoletoLogX.setIndUserWeb(false);
			else
				mpBoletoLogX.setIndUserWeb(true);
			//
			this.mpBoletoLogs.guardar(mpBoletoLogX);
		}
		//
		String output = "<total>"  + mpBoletoLogs.size() + "</total>";
		//
		return Response.status(200).entity(output).build();
	}
	
}