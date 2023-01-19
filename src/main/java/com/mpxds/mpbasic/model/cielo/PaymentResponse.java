package com.mpxds.mpbasic.model.cielo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {
	//
    @JsonProperty("MerchantOrderId")
    private String merchantOrderId;
    @JsonProperty("Payment")
    private Payment payment;
    
    //

    public String getJson(PaymentResponse obj) {
		//
		String json = "";
	
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			json = mapper.writeValueAsString(obj);
			//
		} catch (JsonProcessingException e) {
			//
			e.printStackTrace();
		}
		//
		return json;
	}
    
    //
    
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
    
}
