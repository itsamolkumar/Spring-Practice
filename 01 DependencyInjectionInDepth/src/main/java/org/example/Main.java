package org.example;

import org.example.payments.CardPayment;
import org.example.payments.PaymentService;
import org.example.payments.UPIPayment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Hello and welcome!");
//   ******Using External Jar file by creating object manually********
//        ExternalWork externalWork=new ExternalWork();
//                externalWork.work();
//   ****** Without Spring Context Dependency*********
//        PaymentService paymentService = new CardPayment();
//        Order order=new Order(paymentService);
//        order.createOrder();
//   ****** After Spring Context Dependency***********
        Order order=context.getBean(Order.class);
        order.createOrder();
        ExternalWork externalWork=context.getBean(ExternalWork.class);
        externalWork.work();

// ******* Creating Bean with parameter(a,b)******************
        Adder adder=context.getBean(Adder.class);
        adder.print();
    }
}