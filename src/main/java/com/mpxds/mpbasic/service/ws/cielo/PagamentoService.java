package com.mpxds.mpbasic.service.ws.cielo;

import com.mpxds.mpbasic.model.cielo.Payment;
import com.mpxds.mpbasic.model.cielo.PaymentRequest;
import com.mpxds.mpbasic.model.cielo.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PagamentoService {
	//
    public PaymentResponse solicitarPagamento(PaymentRequest paymentRequest) throws Exception {
    	//
        //simula o tempo de resposta da API
        Thread.sleep(1000);

        Payment payment = new Payment();
        payment.setServiceTaxAmount(0);
        payment.setInstallments(1);
        payment.setCapture(false);
        payment.setAuthenticate(false);
        payment.setCreditCard(paymentRequest.getPayment().getCreditCard());
        payment.setProofOfSale("123456");
        payment.setTid("123456987654");
        payment.setAuthorizationCode("123456");
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setType(paymentRequest.getPayment().getType());
        payment.setAmount(paymentRequest.getPayment().getAmount());
        payment.setCurrency(paymentRequest.getPayment().getCurrency());
        payment.setCountry(paymentRequest.getPayment().getCountry());
        payment.setStatus(1);
        payment.setReturnCode("4");
        payment.setReturnMessage("Operation Successful");

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setMerchantOrderId(paymentRequest.getMerchantOrderId());
        paymentResponse.setPayment(payment);

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        
        System.out.println("paymentRequest: \n" + mapper.writeValueAsString(paymentRequest));
        System.out.println("paymentResponse: \n" + mapper.writeValueAsString(paymentResponse));
        //
        return paymentResponse;
    }
}
