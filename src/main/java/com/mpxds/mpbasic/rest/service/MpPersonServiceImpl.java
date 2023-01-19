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

import com.mpxds.mpbasic.rest.model.MpPerson;
import com.mpxds.mpbasic.rest.model.MpResponse;

@Path("/mpPerson")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class MpPersonServiceImpl implements MpPersonService {
	//
	private static Map<Integer, MpPerson> mpPersons = new HashMap<Integer, MpPerson>();
	
	@Override
	@POST
    @Path("/add")
	public MpResponse addMpPerson(MpPerson p) {
		//
		MpResponse mpResponse = new MpResponse();

		if (mpPersons.get(p.getId()) != null) {
			//
			mpResponse.setStatus(false);
			mpResponse.setMessage("Person Already Exists");
			return mpResponse;
		}
		//
		mpPersons.put(p.getId(), p);
		
		mpResponse.setStatus(true);
		mpResponse.setMessage("Person created successfully");
		//
		return mpResponse;
	}

	@Override
	@GET
    @Path("/{id}/delete")
	public MpResponse deleteMpPerson(@PathParam("id") int id) {
		//
		MpResponse mpResponse = new MpResponse();
		
		if (mpPersons.get(id) == null) {
			//
			mpResponse.setStatus(false);
			mpResponse.setMessage("Person Doesn't Exists");
			return mpResponse;
		}
		//
		mpPersons.remove(id);
		
		mpResponse.setStatus(true);
		mpResponse.setMessage("Person deleted successfully");
		//
		return mpResponse;
	}

	@Override
	@GET
	@Path("/{id}/get")
	public MpPerson getMpPerson(@PathParam("id") int id) {
		//
		return mpPersons.get(id);
	}
	
	@GET
	@Path("/{id}/getDummy")
	public MpPerson getDummyMpPerson(@PathParam("id") int id) {
		//
		MpPerson p = new MpPerson();
		
		p.setAge(99);
		p.setName("Dummy");
		p.setId(id);
		//
		return p;
	}

	@Override
	@GET
	@Path("/getAll")
	public MpPerson[] getAllMpPersons() {
		//
		Set<Integer> ids = mpPersons.keySet();
		
		MpPerson[] p = new MpPerson[ids.size()];
		
		int i=0;
		for(Integer id : ids) {
			//
			p[i] = mpPersons.get(id);
			i++;
		}
		//
		return p;
	}

}