package com.mpxds.mpbasic.rest.service.mysql;

//import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mpxds.mpbasic.model.MpUsuario;
import com.mpxds.mpbasic.repository.MpUsuarios;
import com.mpxds.mpbasic.rest.model.mysql.MpUsuarioMySqlDTO;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
 
@Path("/usuarioMySql")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MpUsuarioMySqlJsonRestService {
	//
	private MpUsuarios mpUsuarios = MpCDIServiceLocator.getBean(MpUsuarios.class);
	
//	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");

	// ---
	
	@GET
	@Path("/{id}")
	public Response getMpUsuariosMySql(@PathParam("id") String iX) {
		//
		String jsonX = "[ ";
		
		for (MpUsuario mpUsuario : this.mpUsuarios.listAll()) {
			//
			if (!iX.isEmpty()) {
				//
				if (!iX.equals("*"))
					if (Long.parseLong(iX) == mpUsuario.getId())
						assert(true); // nop
					else
						continue;
			} 
			//
			MpUsuarioMySqlDTO mpUsuarioMySqlDTOX = new MpUsuarioMySqlDTO(
												mpUsuario.getId(),
												mpUsuario.getLogin(),
												mpUsuario.getNome(),
												mpUsuario.getEmail(),
												mpUsuario.getSenha(),
												mpUsuario.getCpfCnpj(),
												mpUsuario.getTelefone(),
												mpUsuario.getCelular(),
												mpUsuario.getDataNascimento(), 
												mpUsuario.getObservacao(), 
												mpUsuario.getIndUserWeb(), 
												mpUsuario.getIndAtivacao(),
												mpUsuario.getCodigoAtivacao(),
												mpUsuario.getIndBoleto(), 
												mpUsuario.getIndTitulo(),
												mpUsuario.getNumIpUltimoLogin(),
												mpUsuario.getDataUltimoLogin(),
												mpUsuario.getDataUltimoLoginAnt(), 
												mpUsuario.getMpStatus().name(),
												mpUsuario.getMpSexo().name());			
			//
			jsonX = jsonX + mpUsuarioMySqlDTOX.getJson(mpUsuarioMySqlDTOX) + " , ";
		}
		//
		if (jsonX.length() > 5)
			jsonX = jsonX +  " ] ";
	    //
		return Response.status(200).entity( jsonX ).build();
	}	
	
}