package com.mpxds.mpbasic.rest.service.mysql;

import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.repository.MpAlertaSites;
import com.mpxds.mpbasic.rest.model.mysql.MpAlertaSiteMySqlDTO;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
 
@Path("/alertaSiteMySql")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MpAlertaSiteMySqlJsonRestService {
	//
	private MpAlertaSites mpAlertaSites = MpCDIServiceLocator.getBean(MpAlertaSites.class);
	
	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");

	// ---

	@GET
	@Path("/{id}")
	public Response getMpAlertaSitesMySql(@PathParam("id") String iX) {
		//
		String jsonX = "[ ";
		
		for (MpAlertaSite mpAlertaSite : this.mpAlertaSites.listAll()) {
			//
			if (null == mpAlertaSite.getTamanhoMsg())
				mpAlertaSite.setTamanhoMsg(100);
			
			if (!iX.isEmpty()) {
				//
				if (!iX.equals("*"))
					if (Long.parseLong(iX) == mpAlertaSite.getId())
						assert(true); // nop
					else
						continue;
			} 
			//
			MpAlertaSiteMySqlDTO mpAlertaSiteMySqlDTOX = new MpAlertaSiteMySqlDTO(
												mpAlertaSite.getId(),
												mpAlertaSite.getTamanhoMsg().toString(),
												mpAlertaSite.getAlertaMsg(),
												"", // mpAlertaSite.getAlertaTxt(),
												this.sdfDMY.format(mpAlertaSite.getDataAlertaFim()),
												this.sdfDMY.format(mpAlertaSite.getDataAlertaIni()),
												mpAlertaSite.getNumeroOficio());
			
			jsonX = jsonX + mpAlertaSiteMySqlDTOX.getJson(mpAlertaSiteMySqlDTOX) + " , ";
		}
		//
		if (jsonX.length() > 5)
			jsonX = jsonX +  " ] ";
	    //
		return Response.status(200).entity( jsonX ).build();
	}	
	
}