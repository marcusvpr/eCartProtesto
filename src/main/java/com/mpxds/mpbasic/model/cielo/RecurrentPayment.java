package com.mpxds.mpbasic.model.cielo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("RecurrentPayment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecurrentPayment {
    @JsonProperty("RecurrentPaymentId")
    private String recurrentPaymentId;

    @JsonProperty("AuthorizeNow")
    private boolean authorizeNow;

    @JsonProperty("EndDate")
    private String endDate;

    @JsonProperty("Interval")
    private String interval;

    @JsonProperty("ReasonCode")
    private int reasonCode;
    
    // 
    
	public String getRecurrentPaymentId() {
		return recurrentPaymentId;
	}

	public void setRecurrentPaymentId(String recurrentPaymentId) {
		this.recurrentPaymentId = recurrentPaymentId;
	}

	public boolean isAuthorizeNow() {
		return authorizeNow;
	}

	public void setAuthorizeNow(boolean authorizeNow) {
		this.authorizeNow = authorizeNow;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}
    
}
