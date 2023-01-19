package com.mpxds.mpbasic.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/hello")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MpHelloWorldService {
	//
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
		//
		String output = "Jersey say : " + msg;
		//
		return Response.status(200).entity(output).build();
	}
 
}