package com.mpxds.mpbasic.model.cielo;

import com.mpxds.mpbasic.model.cielo.enums.CardType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Payment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {
	//
    @JsonProperty("Type")
    private CardType type;
    @JsonProperty("Amount")
    private Long amount;
    @JsonProperty("Installments")
    private Integer installments;
    @JsonProperty("SoftDescriptor")
    private String softDescriptor;
    @JsonProperty("CreditCard")
    private CreditCard creditCard;
    @JsonProperty("DebitCard")
    private DebitCard debitCard;
    @JsonProperty("RecurrentPayment")
    private RecurrentPayment recurrentPayment;

    @JsonProperty("ServiceTaxAmount")
    private Integer serviceTaxAmount;
    @JsonProperty("Interest")
    private String interest;
    @JsonProperty("Capture")
    private Boolean capture;
    @JsonProperty("Authenticate")
    private Boolean authenticate;
    @JsonProperty("ProofOfSale")
    private String proofOfSale;
    @JsonProperty("Tid")
    private String tid;
    @JsonProperty("AuthorizationCode")
    private String authorizationCode;
    @JsonProperty("PaymentId")
    private String paymentId;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("ReturnCode")
    private String returnCode;
    @JsonProperty("ReturnMessage")
    private String returnMessage;
    @JsonProperty("ReturnUrl")
    private String returnUrl;
    @JsonProperty("IsCryptoCurrencyNegotiation")
    private Boolean isCryptoCurrencyNegotiation;
    
    //
    
	public CardType getType() {
		return type;
	}
	public void setType(CardType type) {
		this.type = type;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getInstallments() {
		return installments;
	}
	public void setInstallments(Integer installments) {
		this.installments = installments;
	}
	public String getSoftDescriptor() {
		return softDescriptor;
	}
	public void setSoftDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public DebitCard getDebitCard() {
		return debitCard;
	}
	public void setDebitCard(DebitCard debitCard) {
		this.debitCard = debitCard;
	}
	public RecurrentPayment getRecurrentPayment() {
		return recurrentPayment;
	}
	public void setRecurrentPayment(RecurrentPayment recurrentPayment) {
		this.recurrentPayment = recurrentPayment;
	}
	public Integer getServiceTaxAmount() {
		return serviceTaxAmount;
	}
	public void setServiceTaxAmount(Integer serviceTaxAmount) {
		this.serviceTaxAmount = serviceTaxAmount;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public Boolean getCapture() {
		return capture;
	}
	public void setCapture(Boolean capture) {
		this.capture = capture;
	}
	public Boolean getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(Boolean authenticate) {
		this.authenticate = authenticate;
	}
	public String getProofOfSale() {
		return proofOfSale;
	}
	public void setProofOfSale(String proofOfSale) {
		this.proofOfSale = proofOfSale;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	public Boolean getIsCryptoCurrencyNegotiation() {
		return isCryptoCurrencyNegotiation;
	}
	public void setIsCryptoCurrencyNegotiation(Boolean isCryptoCurrencyNegotiation) {
		this.isCryptoCurrencyNegotiation = isCryptoCurrencyNegotiation;
	}
   
}
