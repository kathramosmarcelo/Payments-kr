package com.kramomar.Payments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kramomar.Payments.entity.Payment;
import com.kramomar.Payments.utils.Topic;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service
public class PaymentService {

	   private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	   
	@Autowired
	public KafkaTemplate<String, String> paymentTemplate;

	    private final  KafkaTemplate<String, String> kafkaTemplate;

	    public void sendCellOriginToWallet(String value) {
	        kafkaTemplate.send(Topic.NUMBER_PHONE_ORIGIN,value);
	        logger.info("Messages successfully pushed on topic: " + Topic.NUMBER_PHONE_ORIGIN);
	    }
	    public void sendCellDestinationToWallet(String value) {
	        kafkaTemplate.send(Topic.NUMBER_PHONE_ORIGIN,value);
	        logger.info("Messages successfully pushed on topic: " + Topic.NUMBER_PHONE_DESTINATION);
	    }
	
}
