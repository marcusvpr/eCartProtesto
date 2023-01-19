package com.mpxds.mpbasic.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.model.MpEnderecoLocal;
import com.mpxds.mpbasic.repository.MpBoletos;
import com.mpxds.mpbasic.rest.model.MpBoletoDTO;
import com.mpxds.mpbasic.rest.model.MpBoletosDTO;
import com.mpxds.mpbasic.util.cdi.MpCDIServiceLocator;
 
@Path("/mpBoletoJsonWs")
public class MpBoletoJsonRestService {
	//
	@Context 
	ServletContext servletContext;

	private MpBoletos mpBoletos;
	
	// ---

	@GET
	@Path("/{oficio}/{numeroProtocolo}/{dataProtocolo}/getJson")
    @Produces(MediaType.APPLICATION_JSON)
	public MpBoletosDTO getMpBoletoDTO(@PathParam("oficio") String oficio,
											@PathParam("numeroProtocolo") String numeroProtocolo,
											@PathParam("dataProtocolo") String dataProtocolo) {
		// Inject !
		this.mpBoletos = MpCDIServiceLocator.getBean(MpBoletos.class);
		
		MpBoletosDTO mpBoletosDTO = new MpBoletosDTO();
			
		List<MpBoleto> mpBoletoList;
		//
		try {
			//
			mpBoletoList = this.mpBoletos.mpBoletoByNumeroProtocoloList(oficio, numeroProtocolo);
			//
		} catch (Exception e) {
			//
			System.out.println("MpBoletoRestJsonService.getMpBoletoDTO()  - e = " + e);
			
			return new MpBoletosDTO();
		}
		//
		for (MpBoleto mpBoleto : mpBoletoList) {
			//
			MpEnderecoLocal mpEnderecoLocal = new MpEnderecoLocal();
			
			mpEnderecoLocal.setBairro(mpBoleto.getMpEnderecoLocal().getBairro());
			mpEnderecoLocal.setCep(mpBoleto.getMpEnderecoLocal().getCep());
			mpEnderecoLocal.setCidade(mpBoleto.getMpEnderecoLocal().getCidade());
			mpEnderecoLocal.setLogradouro(mpBoleto.getMpEnderecoLocal().getLogradouro());
			mpEnderecoLocal.setNumero(mpBoleto.getMpEnderecoLocal().getNumero());
			
			MpBoletoDTO mpBoletoDTO = new MpBoletoDTO(mpBoleto.getCodigoOficio(), mpBoleto.getNumeroIntimacao(), 
												mpBoleto.getNumeroIntimacaoCode(), mpBoleto.getCpfCnpj(),
												mpBoleto.getNomeSacado(), mpBoleto.getMpEnderecoLocal(), 
												mpBoleto.getNumeroDocumento(), mpBoleto.getEspecieDocumento(),
												mpBoleto.getNossoNumero(), mpBoleto.getNossoNumeroDig(), 
												mpBoleto.getValorDocumento(), mpBoleto.getValorAcrescimo(), 
												mpBoleto.getValorCobrado(), mpBoleto.getValorTarifa(), 
												mpBoleto.getValorCPMF(), mpBoleto.getValorLeis(), 
												mpBoleto.getDataDocumento(), mpBoleto.getDataVencimento(), 
												mpBoleto.getBoletoInstrucao8(), mpBoleto.getCarteira(), 
												mpBoleto.getCodigoBarras(), mpBoleto.getAgenciaCodigoCedente(), 
												mpBoleto.getLinhaDigitavel(),
												mpBoleto.getDataVencimento1(), mpBoleto.getCodigoBarras1(), 
												mpBoleto.getAgenciaCodigoCedente1(), mpBoleto.getLinhaDigitavel1(),
												mpBoleto.getValorIss());
			
			
			mpBoletosDTO.getMpBoletoDTOList().add(mpBoletoDTO);
			//
			break;
		}
		//		
		return mpBoletosDTO;
	}
	
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	public Response getMpBoletos() {
		//
	    List< MpBoletoDTO > matched;
	    GenericEntity< List< MpBoletoDTO > > entity;
	    //
		// Inject !
		this.mpBoletos = MpCDIServiceLocator.getBean(MpBoletos.class);
		
		MpBoletosDTO mpBoletosDTO = new MpBoletosDTO();
			
		List<MpBoleto> mpBoletoList = new ArrayList<MpBoleto>();;
		//
		try {
			//
			mpBoletoList = this.mpBoletos.mpBoletoByNumeroProtocoloList("1", "014733");
			//
		} catch (Exception e) {
			//
			System.out.println("MpBoletoRestJsonService.getMpBoletoDTO()  - e = " + e);
			
		    entity  = new GenericEntity< List< MpBoletoDTO > >(new ArrayList<MpBoletoDTO>() ) { };

		    return Response.ok( entity ).build();
		}
		//
		for (MpBoleto mpBoleto : mpBoletoList) {
			//
			MpEnderecoLocal mpEnderecoLocal = new MpEnderecoLocal();
			
			mpEnderecoLocal.setBairro(mpBoleto.getMpEnderecoLocal().getBairro());
			mpEnderecoLocal.setCep(mpBoleto.getMpEnderecoLocal().getCep());
			mpEnderecoLocal.setCidade(mpBoleto.getMpEnderecoLocal().getCidade());
			mpEnderecoLocal.setLogradouro(mpBoleto.getMpEnderecoLocal().getLogradouro());
			mpEnderecoLocal.setNumero(mpBoleto.getMpEnderecoLocal().getNumero());
			
			MpBoletoDTO mpBoletoDTO = new MpBoletoDTO(mpBoleto.getCodigoOficio(), mpBoleto.getNumeroIntimacao(), 
												mpBoleto.getNumeroIntimacaoCode(), mpBoleto.getCpfCnpj(),
												mpBoleto.getNomeSacado(), mpBoleto.getMpEnderecoLocal(), 
												mpBoleto.getNumeroDocumento(), mpBoleto.getEspecieDocumento(),
												mpBoleto.getNossoNumero(), mpBoleto.getNossoNumeroDig(), 
												mpBoleto.getValorDocumento(), mpBoleto.getValorAcrescimo(), 
												mpBoleto.getValorCobrado(), mpBoleto.getValorTarifa(), 
												mpBoleto.getValorCPMF(), mpBoleto.getValorLeis(), 
												mpBoleto.getDataDocumento(), mpBoleto.getDataVencimento(), 
												mpBoleto.getBoletoInstrucao8(), mpBoleto.getCarteira(), 
												mpBoleto.getCodigoBarras(), mpBoleto.getAgenciaCodigoCedente(), 
												mpBoleto.getLinhaDigitavel(),
												mpBoleto.getDataVencimento1(), mpBoleto.getCodigoBarras1(), 
												mpBoleto.getAgenciaCodigoCedente1(), mpBoleto.getLinhaDigitavel1(),
												mpBoleto.getValorIss());
			
			
			mpBoletosDTO.getMpBoletoDTOList().add(mpBoletoDTO);
			//
			break;
		}
	    //

	    matched = mpBoletosDTO.getMpBoletoDTOList();
	    entity  = new GenericEntity< List< MpBoletoDTO > >( matched ) { };

	    return Response.ok( entity ).build();
	}	
	
	
}