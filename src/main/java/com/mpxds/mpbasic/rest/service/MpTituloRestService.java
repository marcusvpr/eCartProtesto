package com.mpxds.mpbasic.rest.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.model.MpTituloLog;
import com.mpxds.mpbasic.repository.MpTitulos;
import com.mpxds.mpbasic.repository.MpTituloLogs;
import com.mpxds.mpbasic.rest.model.MpTituloWs;

import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;

@Path("/mpTitulo")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class MpTituloRestService {
	//	
	private MpTitulos mpTitulos;

	private MpTituloLogs mpTituloLogs;

	@GET
	@Path("/{oficio}/{numeroProtocolo}/{dataProtocolo}/get")
	public List<MpTituloWs> getMpTituloWs(@PathParam("oficio") String oficio,
										@PathParam("numeroProtocolo") String numeroProtocolo,
										@PathParam("dataProtocolo") String dataProtocolo) {
		//
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
		
		// Inject !
		this.mpTitulos = MpCDIServiceLocator.getBean(MpTitulos.class);
		this.mpTituloLogs = MpCDIServiceLocator.getBean(MpTituloLogs.class);

		List<MpTituloWs> mpTituloWsList = new ArrayList<MpTituloWs>();
			
		List<MpTitulo> mpTituloList;
		//
		try {
			//
			mpTituloList = this.mpTitulos.mpTituloByNumeroDataProtocoloList(oficio,
																			numeroProtocolo,
																		    sdfYMD.parse(dataProtocolo));
		} catch (ParseException e) {
			//
			System.out.println("MpTituloService.getMpTituloWs()  - e = " + e);
			
			return new ArrayList<MpTituloWs>();
		}
		//
		for (MpTitulo mpTitulo : mpTituloList) {
			//
//	    	// MVPR_20180904 ...
//			if (mpTitulo.getStatus().trim().toUpperCase().equals("EXCLUIDO DEPOSITO EMITIDO"))
//				mpTitulo.setStatus("EM ANDAMENTO");
//			//
//			if (!mpTitulo.getComplemento().isEmpty())
//				mpTitulo.setStatus(mpTitulo.getStatus() + " ( " + mpTitulo.getComplemento()) ;
//			//
			Boolean indTituloProtestadoOrdinario = false;
////			if (this.statusTitulo.indexOf("Protestado Ordinario") >= 0)
//			if (mpTitulo.getClasseTitulo().indexOf("ORDINARIO") >= 0)
//				indTituloProtestadoOrdinario = true;
//
			Boolean indTituloProtestadoCustodiado = false;
////			if (this.statusTitulo.indexOf("Protestado Custodiado") >= 0)
//			if (mpTitulo.getClasseTitulo().indexOf("CUSTODIADO") >= 0) // TJD??
//				indTituloProtestadoCustodiado = true;
			//
			MpTituloWs mpTituloWs = new MpTituloWs(mpTitulo.getId().intValue(), 
													mpTitulo.getCodigoOficio(),
													mpTitulo.getNumeroProtocolo(), 
													sdfYMD.format(mpTitulo.getDataProtocolo()), 
													mpTitulo.getComplemento(),
													mpTitulo.getClasseTitulo(),
													mpTitulo.getStatus(),
													indTituloProtestadoOrdinario,
													indTituloProtestadoCustodiado);
			
			mpTituloWsList.add(mpTituloWs);
			//
	    	// Grava Titulo Log...
	        // ===================
	        MpTituloLog mpTituloLog = new MpTituloLog();
	        
	        mpTituloLog.setDataGeracao(new Date());
	     // Usuário não logado ... página serviços pública !
	        mpTituloLog.setUsuarioNome("API.REST = MpTituloService.getMpTituloWs()"); 
	        //
	        mpTituloLog.setNumeroOficio(oficio);
	        mpTituloLog.setNumeroProtocolo(numeroProtocolo);
	        
	        this.mpTituloLogs.guardar(mpTituloLog);        
	        //
			break;
		}
		//		
		return mpTituloWsList;
	}
	
}