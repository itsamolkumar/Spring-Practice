package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/*
=====================================================================================
========------>4 Ways to Resolve Circular Dependency in Spring Boot<-------==========
=====================================================================================
1. @Lazy: Delays bean initialization by injecting a proxy. The actual bean is created when first accessed.

2. Setter Injection: Injects dependencies through setter methods after object creation, allowing Spring to resolve certain circular dependencies.

3. Field Injection (@Autowired): Injects dependencies directly into fields after object creation using Spring's early singleton reference mechanism.

4. ApplicationContext: Injects ApplicationContext and uses getBean() to retrieve the required bean when needed, avoiding direct constructor dependency.

Note: Constructor injection cannot resolve direct circular dependencies. Refactoring the architecture to eliminate circular dependencies is the best practice.
*
* */
public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");
        ApplicationContext context= new AnnotationConfigApplicationContext(AppConfig.class);
        OrderService order=context.getBean(OrderService.class);
        order.ordered();


    }
}