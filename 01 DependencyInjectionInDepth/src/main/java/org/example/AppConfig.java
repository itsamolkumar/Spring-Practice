package org.example;

import org.example.payments.CardPayment;
import org.example.payments.PaymentService;
import org.example.payments.UPIPayment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {
    @Bean
    public ExternalWork externalWork(){
        return new ExternalWork();
    }
    @Bean
    public Adder adder(){
        return new Adder(5,6);
    }

    @Bean
    public PaymentService cardPayment(){
        return new CardPayment();
    }
    @Bean
    public PaymentService upiPayment(){
        return new UPIPayment();
    }
    @Bean
    public Order order(@Qualifier("upiPayment") PaymentService paymentService){
    return new Order(paymentService);
    }
}
