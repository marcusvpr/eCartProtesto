package com.mpxds.mpbasic.model.cielo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardOnFile {
	//
    @JsonProperty("Usage")
    private String usage;
    @JsonProperty("Reason")
    private String reason;

    //
       
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
        
}
