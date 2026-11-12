package org.example;

import org.example.payments.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Order {
    PaymentService paymentService;
// **** for automatic bean ceation by IOC
//    public Order(@Qualifier("upi") PaymentService paymentService){
// ******* for Bean declaration by @Bean annotaion in AppCOnfig and bean will be created by IOC
        public Order( PaymentService paymentService){
        this.paymentService=paymentService;
    }
    public void createOrder(){
        paymentService.createPayment();
        System.out.println("Order Created!");
    }
}
