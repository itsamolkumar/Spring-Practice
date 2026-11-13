# Spring Bean Lifecycle – Complete Quick Revision Notes

## 1. What is Bean Lifecycle?

Complete journey of a Spring-managed object from creation to destruction.

Spring manages:

- Bean discovery
- Object creation
- Dependency injection
- Initialization
- Usage
- Destruction

## 2. Bean Lifecycle – Complete Flow

1\. Spring Container Starts

2\. Reads Configuration / Annotations

3\. Creates BeanDefinition

4\. Instantiates Object

5\. Injects Dependencies

6\. Calls Aware Interfaces

7\. BeanPostProcessor – Before Initialization

8\. Initialization Callbacks

9\. BeanPostProcessor – After Initialization

10\. Bean Ready to Use

11\. Application Uses Bean

12\. Destruction Callbacks

13\. Bean Removed

## 3. Spring Container Starts

Using:

```
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

- Spring receives class metadata, not an AppConfig object.
- Uses Reflection to inspect annotations, configuration, component scanning and bean definitions.

## 4. BeanDefinition

Metadata describing how Spring should create and manage a bean.

Contains:

- Bean name and class.
- Scope (singleton/prototype).
- Lazy or eager creation.
- Dependencies.
- Init and destroy methods.

Important: BeanDefinition is metadata, not the actual object.

## 5. Sources of Bean Definitions

1. Annotation configuration – `@Component`, `@ComponentScan`.
2. Java configuration – `@Bean`.
3. XML configuration – `<bean>`.

## 6. Instantiation

Instantiation means creating the actual object.

```
public PaymentService() {
    System.out.println("Constructor called");
}
```

- Instantiation = Object creation.
- Initialization = Preparing the object after creation.

Both are different lifecycle phases.

## 7. Dependency Injection During Lifecycle

| Injection   | When dependency is injected            |
| ----------- | -------------------------------------- |
| Constructor | During object creation                 |
| Setter      | After object creation                  |
| Field       | After object creation using Reflection |

Constructor injection is generally preferred.

## 8. Aware Interfaces

Used when a bean needs information about its Spring container.

Common interfaces:

- `BeanNameAware` – Provides the bean name.
- `ApplicationContextAware` – Provides ApplicationContext.

Spring automatically calls their callback methods.

Direction: Spring Container → Bean.

Useful mainly for framework-level code, logging utilities, custom libraries and infrastructure components.

## 9. Initialization Phase

Runs startup logic after dependency injection and Aware callbacks.

Examples:

- Validate configuration.
- Initialize cache.
- Prepare resources.
- Populate HashMap.
- Log startup information.

### Three initialization methods

A. @PostConstruct

```
@PostConstruct
public void init() {
    System.out.println("Initialized");
}
```

- Called after dependency injection.
- Commonly preferred.
- Spring 6/7 uses `jakarta.annotation.PostConstruct`.

B. InitializingBean

```
public class PaymentService
        implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("Initialized");
    }
}
```

- Spring calls `afterPropertiesSet()`.
- Couples the class to Spring.

C. Custom initMethod

```
@Bean(initMethod = "start")
public PaymentService paymentService() {
    return new PaymentService();
}
```

```
public void start() {
    System.out.println("Initialized");
}
```

- Custom method called by Spring.
- Avoids implementing Spring interfaces.

Initialization order:

1. `@PostConstruct`
2. `afterPropertiesSet()`
3. Custom `initMethod`

## 10. Bean Ready to Use

After initialization, the bean is ready for business logic.

For singleton scope, Spring stores and reuses the same object.

## 11. Destruction Phase

Runs cleanup logic when the Spring container shuts down.

Examples:

- Close database connections.
- Stop background threads.
- Release file handles.
- Flush pending data.

### Three destruction methods

A. @PreDestroy

```
@PreDestroy
public void cleanup() {
    System.out.println("Cleaning up");
}
```

B. DisposableBean

```
public class PaymentService
        implements DisposableBean {

    @Override
    public void destroy() {
        System.out.println("Cleaning up");
    }
}
```

C. Custom destroyMethod

```
@Bean(destroyMethod = "stop")
public PaymentService paymentService() {
    return new PaymentService();
}
```

```
public void stop() {
    System.out.println("Cleaning up");
}
```

Destruction order:

1. `@PreDestroy`
2. `DisposableBean.destroy()`
3. Custom `destroyMethod`

Usually, choose one clear initialization and destruction approach.

## 12. Singleton Bean Lifecycle

Default Spring Scope

Singleton means Spring creates only one instance of a bean per container.

```
@Component
public class PaymentService {

    @PostConstruct
    public void init() {}

    @PreDestroy
    public void destroy() {}
}
```

- Created during container startup by default.
- Dependencies injected.
- Initialization performed.
- Same instance reused.
- Destroyed when container closes.

## 13. Prototype Bean Lifecycle

New Object Per Request

```
@Component
@Scope("prototype")
public class PaymentService {
}
```

Every request to Spring creates a new object.

```
PaymentService p1 =
    context.getBean(PaymentService.class);

PaymentService p2 =
    context.getBean(PaymentService.class);
```

`p1 != p2`

Lifecycle:

1. BeanDefinition registered.
2. No object usually created at startup.
3. Client requests bean.
4. New object created.
5. Dependencies injected.
6. Aware callbacks called.
7. Initialization callbacks executed.
8. Object returned to client.

Important: Spring does not automatically manage prototype destruction.

## 14. Why Prototype Destruction Doesn't Happen Automatically?

- Spring creates the prototype object and gives it to the caller.
- It does not track that instance for normal destruction.
- `@PreDestroy` generally does not execute automatically.
- `context.close()` does not destroy previously handed-out prototype instances.

Why? Tracking every prototype object could prevent garbage collection and cause memory leaks.

If prototype beans hold expensive resources:

- Clean up manually.
- Use try-with-resources where possible.
- Manage resource ownership carefully.

Garbage collection ≠ Spring destruction callback.

## 15. Singleton vs Prototype

| Singleton                    | Prototype                          |
| ---------------------------- | ---------------------------------- |
| One instance per container   | New instance per request           |
| Default scope                | Explicitly configured              |
| Usually created at startup   | Created when requested             |
| Same object reused           | Different objects                  |
| Full lifecycle managed       | Creation to initialization managed |
| Destruction callback managed | Destruction not automatic          |

## 16. Singleton Depends on Prototype

Example:

- `OrderService` = Singleton.
- `PaymentSession` = Prototype.

If Spring injects `PaymentSession` through the singleton's constructor:

- Singleton is created once.
- One prototype instance is created and injected.
- The same prototype reference remains inside `OrderService`.
- Calling singleton methods does not automatically create new prototype instances.

Remember: Prototype means new object per request from Spring, not per method call.

## 17. Getting Fresh Prototype Objects

If a singleton needs a fresh prototype each time, use:

1. `ObjectProvider`
2. `Provider`
3. Lookup method injection
4. `ApplicationContext.getBean()`

### ObjectProvider Example

```
@Component
public class OrderService {

    private final ObjectProvider<PaymentSession> provider;

    public OrderService(
            ObjectProvider<PaymentSession> provider) {
        this.provider = provider;
    }

    public void createOrder() {
        PaymentSession session = provider.getObject();
    }
}
```

Every `getObject()` request obtains a new prototype instance.

## 18. @Lazy and Bean Lifecycle

By default, singleton beans are eagerly created.

`@Lazy` delays actual object creation until the bean is needed.

```
@Component
@Lazy
public class PaymentService {
}
```

Flow:

1. Container starts.
2. BeanDefinition registered.
3. Actual object not created immediately.
4. Object created when requested.
5. Dependencies injected.
6. Initialization callbacks executed.

Remember: Lazy delays object creation, not BeanDefinition registration.

## 19. @PostConstruct and Circular Dependency

Example:

- Class A requires B through constructor.
- Class B requires A through constructor.

This creates a constructor circular dependency.

Spring cannot create A without B and B without A.

Constructor circular dependency fails.

### Using @PostConstruct

The notes demonstrate a different approach:

- Inject B into A.
- After A is constructed and dependencies are injected, `@PostConstruct` calls `b.setA(this)`.
- B receives the reference to A through its setter.

This demonstrates how initialization callbacks can establish a relationship after object creation.

## 20. BeanPostProcessor

The final lifecycle summary includes two important processing stages:

- Before Initialization: `BeanPostProcessor` processes the bean before initialization callbacks.
- After Initialization: `BeanPostProcessor` processes it again after initialization callbacks.

It can modify or wrap beans, for example using proxies.

## 21. Final Important Revision Table

| Concept             | Remember                               |
| ------------------- | -------------------------------------- |
| Bean Lifecycle      | Complete bean journey                  |
| BeanDefinition      | Bean metadata                          |
| Instantiation       | Object creation                        |
| Initialization      | Prepare object                         |
| Aware               | Get container information              |
| @PostConstruct      | After dependency injection             |
| InitializingBean    | `afterPropertiesSet()`                 |
| initMethod          | Custom startup method                  |
| @PreDestroy         | Before destruction                     |
| DisposableBean      | `destroy()`                            |
| destroyMethod       | Custom cleanup method                  |
| Singleton           | One instance per container             |
| Prototype           | New instance per request               |
| @Lazy               | Delayed object creation                |
| ObjectProvider      | Request fresh prototype                |
| Circular Dependency | Beans depend on each other             |
| BeanPostProcessor   | Before/after initialization processing |

One-line takeaway: Spring Bean Lifecycle is the complete process through which Spring discovers, creates, injects, initializes, manages and eventually destroys a bean.

