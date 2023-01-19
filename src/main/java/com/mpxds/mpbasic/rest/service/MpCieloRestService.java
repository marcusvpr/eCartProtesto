package com.mpxds.mpbasic.rest.service;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestBody;

import com.mpxds.mpbasic.model.cielo.PaymentRequest;
import com.mpxds.mpbasic.model.cielo.PaymentResponse;
import com.mpxds.mpbasic.service.ws.cielo.PagamentoService;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
 
@Path("/mpCieloWs")
public class MpCieloRestService {
	//
	@Context 
	ServletContext servletContext;

    private PagamentoService pagamentoService;	
	// ---

	@POST
	@Path("/1/sales/")
    @Produces(MediaType.APPLICATION_JSON)
    PaymentResponse solicitarPagamento(@RequestBody PaymentRequest paymentRequest) throws Exception {
    	//
		// Inject !
		this.pagamentoService = MpCDIServiceLocator.getBean(PagamentoService.class);
		
        return pagamentoService.solicitarPagamento(paymentRequest);
    }	
	
}