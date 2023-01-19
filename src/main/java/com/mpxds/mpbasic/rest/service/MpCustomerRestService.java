package com.mpxds.mpbasic.rest.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.mpxds.mpbasic.rest.model.MpCustomer;

import java.util.List;

@Path("/customers")
public class MpCustomerRestService {
	//
    private MpCustomerDataService dataService = MpCustomerDataService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MpCustomer> getMpCustomers() {
    	//
        return dataService.getMpCustomerList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createCustomer(MpCustomer newMpCustomer) {
    	//
        return dataService.addMpCustomer(newMpCustomer);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MpCustomer getMpCustomer(@PathParam("id") String id) {
    	//
        return dataService.getMpCustomerById(id);
    }
    
}