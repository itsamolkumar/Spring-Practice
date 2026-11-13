package org.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.Payment.PaymentService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class OrderService implements BeanNameAware, ApplicationContextAware {
    private PaymentService paymentService;
    public OrderService(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name is "+name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Application Context is:-  "+applicationContext);
        System.out.println(""+applicationContext.getId());
        System.out.println(""+applicationContext.getApplicationName());
        System.out.println(""+applicationContext.getDisplayName());
        System.out.println(""+ applicationContext.getParent());
        System.out.println(""+ applicationContext.getStartupDate());
    }

    @PostConstruct
    public void init(){
        System.out.println("Bean Initialized");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("Bean is distroying");
    }
}
