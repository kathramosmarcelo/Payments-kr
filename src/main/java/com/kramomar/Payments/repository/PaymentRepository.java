package com.kramomar.Payments.repository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import com.kramomar.Payments.entity.Payment;
import com.kramomar.Payments.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private  final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);
    private static final String KEY = "Payment";
    private final ReactiveRedisOperations<String, Payment> redisOperations;
    private final ReactiveHashOperations<String, String, Payment> hashOperations;


    @Autowired
    
    public PaymentRepository(ReactiveRedisOperations<String, Payment> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }

    public Mono<Payment> create(Payment payment) {
        logger.info("METHODE CREATE: ");
        if (payment.getId() != null) {
            String id = UUID.randomUUID().toString();
            payment.setId(id);
        }
        return hashOperations.put(KEY, payment.getId(), payment)
                .map(isSaved -> payment);
    }

    public Mono<Boolean> existsById(String id) {
        return hashOperations.hasKey(KEY, id);
    }

    public Flux<Payment> read() {
        return hashOperations.values(KEY);
    }
}
