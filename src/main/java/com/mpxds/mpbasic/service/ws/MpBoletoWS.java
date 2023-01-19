package com.mpxds.mpbasic.service.ws;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mpxds.mpbasic.rest.model.MpBoletoDTO;
import com.mpxds.mpbasic.security.MpSeguranca;
import com.mpxds.mpbasic.util.MpAppUtil;

@Service
public class MpBoletoWS {
	//
	private static final String URL_WS = "/cidades/";

	@Autowired
    private MpSeguranca mpSeguranca;

	public List<MpBoletoDTO> getPegaDados() {
		//
		 Client client = ClientBuilder.newClient();
		 
		 WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS);
		 
		 String json = target.request(MediaType.APPLICATION_JSON).header("Authorization", 
				 															mpSeguranca.getTokenJwt()).get(String.class);
		 Gson gson = new Gson();
		 //
		 return gson.fromJson(json, new TypeToken<List<MpBoletoDTO>>(){}.getType());
	}

	public void apagaDados(long id) {
		//
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + id);
		//
		target.request().header("Authorization", mpSeguranca.getTokenJwt()).delete();
	}
	
	public Response postDados(MpBoletoDTO obj) { // Inclusão !
		//
		System.out.println("MpBoletoWS.postDados() ( " + obj.getJson(obj));

		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS);
		
		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());
		//
		@SuppressWarnings("unused")
		Response response = ib.post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
		//
		return client.target(MpAppUtil.getUrlApiRest() + URL_WS).request(MediaType.APPLICATION_JSON).
														post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
	}	
	
	public Response putDados(MpBoletoDTO obj) { // Atualização ! 
		//
		System.out.println("MpBoletoWS.putDados() ( " + obj.getJson(obj)); 
		
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + obj.getId());
		
		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization",
																					mpSeguranca.getTokenJwt());
		//
		@SuppressWarnings("unused")
		Response response = ib.put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
		//
		return client.target(MpAppUtil.getUrlApiRest() + URL_WS).request(MediaType.APPLICATION_JSON).
												put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
	}
	
	// --- Atualização com Retorno !
	
	public MpBoletoDTO postDadosRetorno(MpBoletoDTO obj) { // Inclusão !
		//
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + "retorno/");
		
		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", mpSeguranca.getTokenJwt());
		//
		Response response = ib.post(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
		//
		String json = response.toString(); // .readEntity(String.class);		
		Gson gson = new Gson();
		//
		return gson.fromJson(json, new TypeToken<MpBoletoDTO>(){}.getType());
	}	
	
	public MpBoletoDTO putDadosRetorno(MpBoletoDTO obj) { // Atualização ! 
		//
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(MpAppUtil.getUrlApiRest() + URL_WS + "retorno/" + obj.getId());
		
		Invocation.Builder ib = target.request(MediaType.APPLICATION_JSON).header("Authorization", 
																						mpSeguranca.getTokenJwt());																											
		//
		Response response = ib.put(Entity.entity(obj.getJson(obj), MediaType.APPLICATION_JSON));
		
		String json = response.toString(); // .readEntity(String.class);		
		Gson gson = new Gson();
		//
		return gson.fromJson(json, new TypeToken<MpBoletoDTO>(){}.getType());
	}	
	

}