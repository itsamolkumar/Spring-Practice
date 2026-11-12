# Spring Framework – Quick Revision Notes


#  Dependency Injection, IoC & Loosely Coupled Design

### 1. Why Spring Core?

Spring Core helps create objects, manage dependencies and connect objects in a clean, scalable way.

### 2. Dependency

A dependency is an object or service that a class needs to perform its work.

Example: `OrderService` needs `EmailService` to send order notifications.

### 3. Tight vs Loose Coupling

- Tight Coupling: A class directly depends on a specific concrete class; difficult to change, test and reuse.
- Loose Coupling: A class depends on an abstraction (interface), making implementations replaceable.

### 4. Interface and Tight Coupling

- Using an interface such as `NotificationService` allows multiple implementations (`EmailService`, `SmsService`).
- But using `new EmailService()` inside `OrderService` still creates tight coupling.
- Object creation should happen outside the business class.
- This relates to SRP (Single Responsibility Principle) and OCP (Open-Closed Principle).

### 5. Dependency Injection (DI)

DI means providing dependencies to a class from outside instead of allowing the class to create them itself.

Remember: Don't create your dependency; receive your dependency.

DI is a general design principle, not exclusive to Spring. Spring automates it.

### 6. Benefits of DI

1. Easy to change implementations.
2. Easier testing using fake implementations.
3. More reusable code.
4. Loosely coupled design.

### 7. Types of Dependency Injection

| Type        | Working                                 | Use                                           |
| ----------- | --------------------------------------- | --------------------------------------------- |
| Constructor | Dependency through constructor          | Mandatory dependencies; preferred             |
| Setter      | Dependency through setter method        | Optional/changeable dependencies              |
| Field       | Directly into fields using `@Autowired` | Supported by Spring but generally discouraged |

### 8. Inversion of Control (IoC)

IoC means transferring control of object creation and dependency management from the class to an external system.

- Earlier: `OrderService` creates `EmailService`.
- After DI: An external system creates `EmailService` and provides it to `OrderService`.

### 9. Relationship Between IoC and DI

- IoC = Principle
- DI = Technique to achieve IoC

### 10. Spring IoC Container

Spring provides an external container responsible for:

- Creating objects.
- Managing objects.
- Injecting dependencies.
- Connecting classes.
- Managing object lifecycles.

### 11. Spring Core

Its three main responsibilities are:

1. Object creation.
2. Object management.
3. Connecting objects together.

### 12. Spring Bean

A Spring Bean is an object managed by the Spring IoC container.

Every Spring Bean is an object, but every Java object is not necessarily a Spring Bean.




# Spring IoC Container, Beans & Annotation-Based Configuration

### 1. Core Concept

Classes should focus on their own responsibilities, not create their own dependencies. Spring handles object creation, dependency wiring and lifecycle management.

### 2. First-Principles Understanding

Without Spring, `main()` creates and connects objects manually. With Spring, the IoC container handles these responsibilities.

### 3. Project Setup

- Create a Maven project.
- Add `spring-context` dependency.

It provides `ApplicationContext`, annotation configuration, component scanning, bean creation and DI.

### 4. Ways to Configure Spring

| Configuration    | Meaning                                                |
| ---------------- | ------------------------------------------------------ |
| Annotation-based | Uses annotations; modern and commonly used             |
| XML-based        | Uses XML bean configuration; common in legacy projects |

### 5. Reflection – `Student.class`

- `Student.class` does not create a Student object.
- It provides a `Class` object containing class metadata.
- Metadata includes class name, fields, methods, constructors and annotations.
- Spring uses class metadata internally to understand configuration.

### 6. Important Annotations

| Annotation       | Purpose                                            |
| ---------------- | -------------------------------------------------- |
| `@Component`     | Marks a class as eligible for bean registration    |
| `@Configuration` | Marks a configuration class                        |
| `@ComponentScan` | Specifies where Spring searches for components     |
| `@Autowired`     | Injects dependencies                               |
| `@Bean`          | Manually registers an object returned by a method  |
| `@Primary`       | Sets a default bean when multiple candidates exist |
| `@Qualifier`     | Selects a specific bean                            |

### 7. ApplicationContext

- Represents the Spring IoC container.
- Responsible for reading configuration, creating beans, resolving dependencies, managing lifecycle and providing beans.
- It is an interface, so we use its implementation.

### 8. AnnotationConfigApplicationContext

- An implementation of `ApplicationContext`.
- Starts a Spring container using Java-based annotation configuration.

```
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

### 9. AppConfig and @Configuration

`AppConfig` tells Spring how to configure the application.

```
@Configuration
@ComponentScan("com.coderarmy")
public class AppConfig {
}
```

- `@Configuration` marks the class as a configuration source.
- It can contain `@ComponentScan` and `@Bean` methods.

### 10. @Component vs @ComponentScan

- `@Component`: Marks a class as eligible to become a bean.
- `@ComponentScan`: Tells Spring where to search for such classes.
- Spring scans the specified package and its sub-packages.
- Without a package name, it scans the package containing `AppConfig` and its sub-packages.

### 11. getBean()

Used to retrieve a bean from the Spring container.

```
OrderService service =
    context.getBean(OrderService.class);
```

If the bean is not registered or not scanned, Spring throws an error.

Common reasons:

- Missing `@Component`.
- Package not included in `@ComponentScan`.
- Missing `@Bean` registration.

### 12. Types of DI in Spring

Constructor Injection

- Dependency is passed through the constructor.
- Preferred for mandatory dependencies.
- If there is only one constructor, `@Autowired` is optional.
- Supports `final` fields and easy testing without Spring.

Field Injection

- Uses `@Autowired` directly on fields.
- Spring uses reflection to inject dependencies.
- Not preferred because dependencies are hidden, testing is harder, fields cannot be `final`, and objects may remain incomplete before injection.

Setter Injection

- Spring creates the object first, then calls the setter.
- Useful for optional or changeable dependencies.

### 13. How Spring Starts Internally

1. Creates the `ApplicationContext`.
2. Reads `AppConfig.class`.
3. Processes `@Configuration`.
4. Processes `@ComponentScan`.
5. Scans packages and sub-packages.
6. Finds `@Component`, `@Service`, `@Repository`, `@Controller` classes.
7. Creates `BeanDefinition` metadata.
8. Resolves dependencies.
9. Creates actual bean objects.
10. Injects dependencies.
11. Stores beans inside the container.
12. Returns beans through `getBean()`.

### 14. BeanDefinition

A `BeanDefinition` is metadata about how Spring should create and manage a bean; it is not the actual object.

It contains:

- Bean name.
- Class name.
- Scope.
- Dependencies.
- Creation strategy.
- Lifecycle information.

Spring creates BeanDefinitions first, then actual bean objects.

### 15. Dependency Resolution

When creating a bean, Spring checks its constructor or injection point, finds the required bean in the container and injects it.

If no matching bean exists, an error occurs:

`No qualifying bean of type available`

Possible reasons:

- Required class is not registered.
- Component scanning is incorrect.
- No `@Bean` method exists.
- Required dependency is missing.

### 16. Multiple Beans of the Same Type

Suppose two classes implement `PaymentService`:

- `UPIPaymentService`
- `CardPaymentService`

If `OrderService` requests `PaymentService`, Spring finds two candidates and cannot decide which one to inject. This is called Bean Ambiguity.

Two solutions:

@Primary

- Marks one implementation as the default choice.
- Spring selects it when multiple candidates exist.

@Qualifier

- Explicitly selects the required bean by name.
- Can be used with constructor, field and setter injection.

### 17. Bean Naming

- Default component bean name is generally the class name with its first letter lowercase.
- Example: `CardPaymentService` → `cardPaymentService`.
- Custom names can be provided:

```
@Component("card")
```

Then inject using:

```
@Qualifier("card")
```

### 18. @Primary vs @Qualifier

| @Primary                        | @Qualifier                                  |
| ------------------------------- | ------------------------------------------- |
| Default preference              | Specific selection                          |
| Used for default implementation | Used to select a named bean                 |
| Lower priority                  | Takes priority over @Primary when specified |

### 19. Why Do We Need @Bean?

Used when:

- The class belongs to an external library and cannot be modified with `@Component`.
- Object creation requires custom logic or constructor arguments.

### 20. @Bean Annotation

- Used on methods inside a configuration class.
- Spring calls the method, registers the returned object and manages it as a bean.
- Default bean name is the method name.
- Custom bean names are also supported.

```
@Bean("myPaymentService")
public PaymentService paymentService() {
    return new PaymentService();
}
```

### 21. @Bean with Dependencies

Spring can automatically inject dependencies into `@Bean` method parameters.

```
@Bean
public PaymentService paymentService() {
    return new PaymentService();
}

@Bean
public OrderService orderService(
        PaymentService paymentService) {
    return new OrderService(paymentService);
}
```

Spring creates `PaymentService` and passes it to `orderService()`.

Calling another `@Bean` method directly is also possible in a full `@Configuration` class; Spring maintains singleton behaviour. Method parameters are generally cleaner.

### 22. @Component vs @Bean

| Point              | @Component          | @Bean                                   |
| ------------------ | ------------------- | --------------------------------------- |
| Applied on         | Class               | Method                                  |
| Registration       | Automatic detection | Manual registration                     |
| Best for           | Our own classes     | External classes/custom creation        |
| Component scanning | Required            | Not required if configuration is loaded |
| Default bean name  | Class-based         | Method name                             |

Remember:

- `@Component` = Spring finds the class automatically.
- `@Bean` = We tell Spring how to create the object.

### 23. Avoid Registering the Same Bean Twice

Do not register the same class using both `@Component` and `@Bean` unless intentionally creating multiple beans.

It may cause:

- Multiple beans of the same type.
- Dependency ambiguity.
- Bean overriding issues if names conflict.

### 24. Dependency Resolution with @Bean

`@Primary` and `@Qualifier` also work with beans registered using `@Bean` methods.

### 25. Why Not Put Everything in main()?

`main()` should only start the application, not manage all object creation and wiring.

Keep configuration in `AppConfig`; let Spring manage objects and dependencies.

### 26. BeanFactory vs ApplicationContext

| BeanFactory                 | ApplicationContext                                                 |
| --------------------------- | ------------------------------------------------------------------ |
| Basic container interface   | Advanced container built on BeanFactory                            |
| Basic bean management       | Complete application-level features                                |
| Basic dependency management | DI, lifecycle, events, internationalization and Spring integration |

`ApplicationContext` is commonly used in normal Spring applications.

### 27. Final Spring Startup Flow

1\. ApplicationContext starts

2\. Reads AppConfig

3\. Processes annotations

4\. Scans components

5\. Creates BeanDefinitions

6\. Resolves dependencies

7\. Creates bean objects

8\. Injects dependencies

9\. Stores beans in container

10\. getBean() returns requested bean

### Final One-Line Revision

Spring IoC Container creates objects, manages their lifecycle, resolves and injects dependencies, and provides ready-to-use beans through `ApplicationContext`.




