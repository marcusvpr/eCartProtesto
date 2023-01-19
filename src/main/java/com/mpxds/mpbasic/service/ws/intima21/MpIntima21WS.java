package com.mpxds.mpbasic.service.ws.intima21;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.mpxds.mpbasic.model.cielo.PaymentRequest;
import com.mpxds.mpbasic.model.cielo.PaymentResponse;
import com.mpxds.mpbasic.security.MpSeguranca;

@Named
@ViewScoped // @RequestScoped
public class MpIntima21WS implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	//
	@Inject
	private MpSeguranca mpSeguranca;

//	@Inject
//	private PagamentoService pagamentoService;	

	private static final String URL_CIELO_WS = "/Intimacao/";

	// ---

	public ClientResponse solicitarPagamento(PaymentRequest paymentRequest) throws Exception {
		//
		ClientResponse response = null;

		try {
			//
			Client client = Client.create();

			WebResource webResource = client.resource(mpSeguranca.getIntima21DevURL() + URL_CIELO_WS);
			
//			PaymentResponse paymentResponse = pagamentoService.solicitarPagamento(paymentRequest);

//			String input = paymentResponse.getJson(paymentResponse);;
//
//			response = webResource.type("application/json")
//				    .header("Content-Type", "application/json;charset=UTF-8")
//				    .header("MerchantId", mpSeguranca.getCieloMerchantId())
//				    .header("MerchantKey", mpSeguranca.getCieloMerchantKey())
//					.post(ClientResponse.class, input);

//			if (response.getStatus() != 201) {
//				//
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			}
			//
			System.out.println("Output from Server API Cielo 3.0 .................. \n");
			
			String output = response.getEntity(String.class);
			
			System.out.println(output);
			//
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		//
		return response;
		//
	}

	public ClientResponse getExemplo(PaymentRequest paymentRequest) throws Exception {
		//
		ClientResponse response = null;

		try {
			//
			Client client = Client.create();

			WebResource webResource = client.resource("http://localhost:8080/RESTfulExample/rest/json/metallica/get");

			response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				//
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		//
		return response;
		//
	}	
	

//    public MpEstadoDTO postDadosRetorno(MpEstadoDTO obj) { // Inclusão !
//		//
//		Client client = ClientBuilder.newClient();
//
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + "retorno/");
//
//		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization",
//																				mpSeguranca.getTokenJwt());
//		//
//		Response response = ib.post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//		//
//		String json = response.readEntity(String.class);
//		Gson gson = new Gson();
//		//
//		return gson.fromJson(json, new TypeToken<MpEstadoDTO>() {
//		}.getType());
//	}
	
	
//
//	public List<MpEstadoDTO> getPegaDados() {
//		//
//		Client client = ClientBuilder.newClient();
//
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS);
//
//		String json = target.request().header("Authorization", mpSeguranca.getTokenJwt()).get(String.class);
//		Gson gson = new Gson();
//		//
//		return gson.fromJson(json, new TypeToken<List<MpEstadoDTO>>() {}.getType());
//	}

//	public void apagaDados(long id) {
//		//
//		Client client = ClientBuilder.newClient();
//
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + id);
//		//
//		target.request().header("Authorization", mpSeguranca.getTokenJwt()).delete();
//	}
//	
//	public Response postDados(MpEstadoDTO obj) { // Inclusão !
//		//
//		System.out.println("MpEstadoWS.postDados() ( " + obj.getJson(obj));
//
//		Client client = ClientBuilder.newClient();
//		
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS);
//		
//		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());
//		//
//		@SuppressWarnings("unused")
//		Response response = ib.post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//		//
//		return client.target(MpAppUtil.getUrlApiRest() + URL_WS).request(MediaType.APPLICATION_JSON).
//														post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//	}	
//	
//	public Response putDados(MpEstadoDTO obj) { // Atualização ! 
//		//
//		System.out.println("MpEstadoWS.putDados() ( " + obj.getJson(obj));
//		
//		Client client = ClientBuilder.newClient();
//		
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + obj.getId());
//		
//		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());
//		//
//		@SuppressWarnings("unused")
//		Response response = ib.put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//		//
//		return client.target(MpAppUtil.getUrlApiRest() + URL_WS).request(MediaType.APPLICATION_JSON).
//															put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//	}
//	
//	// --- Atualização com Retorno !
//	
//	public MpEstadoDTO postDadosRetorno(MpEstadoDTO obj) { // Inclusão !
//		//
//		Client client = ClientBuilder.newClient();
//		
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + "retorno/");
//		
//		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());
//		//
//		Response response = ib.post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//		//
//		String json = response.readEntity(String.class);		
//		Gson gson = new Gson();
//		//
//		return gson.fromJson(json, new TypeToken<MpEstadoDTO>(){}.getType());
//	}	
//	
//	public MpEstadoDTO putDadosRetorno(MpEstadoDTO obj) { // Atualização ! 
//		//
//		Client client = ClientBuilder.newClient();
//		
//		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + "retorno/" + obj.getId());
//		
//		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());																											
//		//
//		Response response = ib.put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
//		
//		String json = response.readEntity(String.class);		
//		Gson gson = new Gson();
//		//
//		return gson.fromJson(json, new TypeToken<MpEstadoDTO>(){}.getType());
//	}	
	
}