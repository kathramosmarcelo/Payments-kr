package com.kramomar.Payments.listener;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kramomar.Payments.component.PaymentComponent;
import com.kramomar.Payments.entity.Account;
import com.kramomar.Payments.entity.Payment;
import com.kramomar.Payments.service.PaymentService;
import com.kramomar.Payments.utils.JsonUtils;
import com.kramomar.Payments.utils.Topic;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
@Service
public class KafkaConsumer {

    private  final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final PaymentComponent  paymentComponent;
    private final PaymentService paymentService;
    
	@KafkaListener(topics = Topic.INSERT_PAYMENT, groupId = "group_id")
	@SendTo
	public void consume(String mesage){
		logger.info("TOPIC INSERT_PAYMENT from GATEWAY"+  mesage );
	
        var result=Mono.just(getPayment(mesage));
        result.doOnNext(payment -> {
        	paymentService.sendCellOriginToWallet(payment.getNumberPhoneOrigin());
        	paymentService.sendCellDestinationToWallet(payment.getNumberPhoneDestination());

              logger.info("SENDE MESSAGE TO WALLET -->");
              // createPayment(payment);
        }).subscribe();

	}

    Payment getPayment(String param) {
        Payment payment = null;
        try {
            payment = JsonUtils.convertFromJsonToObject(param, Payment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payment;
    }
    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_ORIGIN, groupId = "group_id")
    public void consumeResponseAccountOrigin(String param) {
        logger.info("Has been published an response account origin from service account-kr : " + param);

    }
    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_ORIGIN, groupId = "group_id")
    public void consumeResponseAccountDestination(String param) {
        logger.info("Has been published an response account origin from service account-kr : " + param);

    }
    
    Account getAccount(String paramX) {
        Account account = null;
        try {
            account = JsonUtils.convertFromJsonToObject(paramX, Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return account;
    }



    public void createPayment(String param) {

        var payment = new Payment();
        try {
            payment = JsonUtils.convertFromJsonToObject(param, Payment.class);

            var result = Mono.just(payment)
                    .map(p -> {

                        var x = getAccount(p.getNumberPhoneOrigin());
                        var y = getAccount(p.getNumberPhoneDestination());

                        // two events

                        var account1 = Mono.just(x)
                                .map(account -> {
                                    account.setAvailableBalance(account.getAvailableBalance() - p.getAmount());
                                  
                                    return account;
                                });

                        var account2 = Mono.just(y)
                                .map(account -> {
                                    account.setAvailableBalance(account.getAvailableBalance() + p.getAmount());
                                   
                                    return account;
                                });

                        return paymentComponent.create(p);

                    });

            result.doOnNext(p -> logger.info("registry success" + p))
                    .subscribe();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
}

























