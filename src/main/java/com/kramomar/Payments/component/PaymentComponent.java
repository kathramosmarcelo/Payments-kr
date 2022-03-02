package com.kramomar.Payments.component;
import org.springframework.stereotype.Component;

import com.kramomar.Payments.entity.Payment;
import com.kramomar.Payments.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PaymentComponent {
    private final PaymentRepository paymentRepository;

    public Mono<Payment> create(Payment payment) {
        return paymentRepository.create(payment);
    }

    public Flux<Payment> read() {
        return paymentRepository.read();
    }
}
