package com.mpxds.mpbasic.rest.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.rest.model.MpTituloDTO;
import com.mpxds.mpbasic.rest.model.MpTitulosDTO;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;

@Path("/mpTituloJsonWs")
public class MpTituloRestJsonService {
	//	
	@Context 
	ServletContext servletContext;

	private MpTitulos mpTitulos;

	// ---
	
	@GET
	@Path("/{oficio}/{numeroProtocolo}/{dataProtocolo}/getJson")
    @Produces(MediaType.APPLICATION_JSON)
	public MpTitulosDTO getMpTituloDTO(@PathParam("oficio") String oficio,
											@PathParam("numeroProtocolo") String numeroProtocolo,
											@PathParam("dataProtocolo") String dataProtocolo) {
		//
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
		
		// Inject !
		this.mpTitulos = MpCDIServiceLocator.getBean(MpTitulos.class);

		MpTitulosDTO mpTitulosDTO = new MpTitulosDTO();
		
		List<MpTitulo> mpTituloList;
		//
		try {
			//
			mpTituloList = this.mpTitulos.mpTituloByNumeroDataProtocoloList(oficio,
																			numeroProtocolo,
																		    sdfYMD.parse(dataProtocolo));
		} catch (ParseException e) {
			//
			System.out.println("MpTituloRestJsonService.getMpTituloDTO()  - e = " + e);
			
			return new MpTitulosDTO();
		}
		//
		for (MpTitulo mpTitulo : mpTituloList) {
			//
			MpTituloDTO mpTituloDTO = new MpTituloDTO(mpTitulo.getCodigoOficio(),
													mpTitulo.getNumeroProtocolo(), 
													sdfYMD.format(mpTitulo.getDataProtocolo()), 
													mpTitulo.getComplemento(),
													mpTitulo.getClasseTitulo(),
													mpTitulo.getStatus());
			
			mpTitulosDTO.getMpTituloDTOList().add(mpTituloDTO);
			//
			break;
		}
		//		
		return mpTitulosDTO;
	}
	
}