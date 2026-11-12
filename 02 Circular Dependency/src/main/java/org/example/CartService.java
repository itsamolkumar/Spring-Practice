package org.example;

import org.example.Payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CartService {
    //using @Lazy annotaion at method level to only delay the dependency creation not whole bean(class)...
//    private OrderService orderService;
//    public CartService(@Lazy OrderService orderService){
//        System.out.println("Entered in CartService constructor");
//        this.orderService=orderService;
//    }


    //===================================================
    //  Using autowired to resolve circularDependencis
    //===================================================
//    @Autowired
//    private  OrderService orderService;
//    public CartService(){
//        System.out.println("Entered in CartService constructor...");
//    }

    //===================================================
    //  Using Setters to resolve circularDependencis
    //===================================================
//

    //=============================================================
    //  Using applicationContext to resolve circularDependencis
    //=============================================================

    private final ApplicationContext context;
    public CartService(ApplicationContext context){
        this.context=context;
    }
    public void placeOrder(){
        OrderService orderService=context.getBean(OrderService.class);
//        orderService.ordered();
        System.out.println("Cart Order placed...");
    }
}
