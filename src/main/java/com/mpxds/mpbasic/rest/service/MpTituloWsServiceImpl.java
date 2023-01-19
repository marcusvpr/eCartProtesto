package com.mpxds.mpbasic.rest.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.rest.model.MpResponse;
import com.mpxds.mpbasic.rest.model.MpTituloWs;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;

@Path("/mpTituloWs")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class MpTituloWsServiceImpl implements MpTituloWsService {
	//	
	private MpTitulos mpTitulos;

	private static Map<Integer, MpTituloWs> mpTituloWsMap = new HashMap<Integer, MpTituloWs>();
	
	// ---
	
	@Override
	@POST
    @Path("/add")
	public MpResponse addMpTituloWs(MpTituloWs p) {
		//
		MpResponse mpResponse = new MpResponse();

		if (mpTituloWsMap.get(p.getId()) != null) {
			//
			mpResponse.setStatus(false);
			mpResponse.setMessage("TituloWs Already Exists");
			return mpResponse;
		}
		//
		mpTituloWsMap.put(p.getId(), p);
		
		mpResponse.setStatus(true);
		mpResponse.setMessage("TituloWs created successfully");
		//
		return mpResponse;
	}

	@Override
	@GET
    @Path("/{id}/delete")
	public MpResponse deleteMpTituloWs(@PathParam("id") int id) {
		//
		MpResponse mpResponse = new MpResponse();
		
		if (mpTituloWsMap.get(id) == null) {
			//
			mpResponse.setStatus(false);
			mpResponse.setMessage("TituloWs Doesn't Exists");
			return mpResponse;
		}
		//
		mpTituloWsMap.remove(id);
		
		mpResponse.setStatus(true);
		mpResponse.setMessage("TituloWs deleted successfully");
		//
		return mpResponse;
	}

	@Override
	@GET
	@Path("/{id}/get")
	public MpTituloWs getMpTituloWs(@PathParam("id") int id) {
		//
		return mpTituloWsMap.get(id);
	}
	
	@GET
	@Path("/{id}/getDummy")
	public MpTituloWs getDummyMpTituloWs(@PathParam("id") int id) {
		//
		System.out.println("MpTituloWsServiceImpl.getDummyMpTituloWs ( id = " + id);

		Long idL = (long) (int) id;

		System.out.println("MpTituloWsServiceImpl.getDummyMpTituloWs ( idL = " + idL);

		this.mpTitulos = MpCDIServiceLocator.getBean(MpTitulos.class);
		//
		System.out.println("MpTituloWsServiceImpl.getDummyMpTituloWs ( CDI Passou !");

		MpTitulo mpTitulo = this.mpTitulos.porId(idL);
		if (null == mpTitulo) return new MpTituloWs();
		//
		return this.getMpTituloWs(mpTitulo);
	}

	@Override
	@GET
	@Path("/getAll")
	public MpTituloWs[] getAllMpTituloWss() {
		//
		Set<Integer> ids = mpTituloWsMap.keySet();
		
		MpTituloWs[] t = new MpTituloWs[ids.size()];
		
		int i=0;
		for(Integer id : ids) {
			//
			t[i] = mpTituloWsMap.get(id);
			i++;
		}
		//
		return t;
	}
		
	// ---
	
	public MpTituloWs getMpTituloWs(MpTitulo mpTitulo) {
		//
		MpTituloWs mpTituloWs = new MpTituloWs();

		mpTituloWs.setId((int) (long) mpTitulo.getId());
		mpTituloWs.setCodigoOficio( mpTitulo.getCodigoOficio());
		mpTituloWs.setNumeroProtocolo(mpTitulo.getNumeroProtocolo());
		mpTituloWs.setDataProtocolo(mpTitulo.getDataProtocolo().toString());
		mpTituloWs.setStatus(mpTitulo.getStatus());

		return mpTituloWs;
	}

}