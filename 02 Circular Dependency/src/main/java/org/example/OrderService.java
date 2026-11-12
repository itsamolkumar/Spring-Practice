package org.example;

import org.example.Payment.PaymentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class OrderService {
//    private PaymentService paymentService;
//    public OrderService(PaymentService paymentService){
//        this.paymentService=paymentService;
//    }
//
//    public void ordered(){
//        paymentService.pay();
//        System.out.println("Order succesfull");
//    }

    //Circular Dependency
    private CartService cartService;
    public OrderService(CartService cartService){
        System.out.println("Entered in OrderService Constructor");
        this.cartService=cartService;
//        cartService.placeOrder();
    }
    public void ordered(){
        System.out.println("Item Ordered...");
        cartService.placeOrder();
    }
}
