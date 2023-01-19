package com.mpxds.mpbasic.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mpxds.mpbasic.rest.model.MpTrackDTO;

@Path("/json/metallica")
public class MpJSONService {

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public MpTrackDTO getTrackInJSON() {
		//
		MpTrackDTO track = new MpTrackDTO();
		
		track.setTitle("Enter Sandman");
		track.setSinger("Metallica");
		//
		return track;
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(MpTrackDTO track) {
		//
		String result = "Track saved : " + track;

		return Response.status(201).entity(result).build();
	}
	
}