


## 1. Quick Recap: DI, IoC & Beans

- DI (Dependency Injection): Spring provides dependencies instead of classes creating them manually.
- IoC (Inversion of Control): Spring container controls object creation and dependency management.
- Bean: An object created and managed by the Spring IoC container.
- `@Component`, `@Service`, `@Repository`, `@Controller` and `@Configuration` register Spring-managed beans.
- `@Configuration` is also internally a `@Component`.

## 2. Bean Creation Order

- Spring creates beans based on their dependencies, not randomly.
- A dependency is created before the bean that requires it.
- Example: `PaymentGateway → PaymentService → OrderService → OrderController`.

## 3. Circular Dependency

Definition: Occurs when two or more classes depend on each other directly or indirectly.

Example: A needs B, and B needs A.

- Spring cannot determine which bean to create first.
- Constructor-based circular dependency causes `BeanCurrentlyInCreationException`.
- In normal Java, recursive object creation can result in `StackOverflowError`.
- Constructor injection fails because all dependencies must be available before object creation.

### Ways to Handle Circular Dependency

1. @Lazy: Injects a proxy instead of the actual bean. The real dependency is resolved when first accessed.
2. Setter Injection: Creates objects first and injects dependencies later through setter methods.
3. Field Injection (@Autowired): Creates objects first and injects dependencies directly into fields.
4. Refactoring: Moves common logic into a third service, separates responsibilities or introduces interfaces/events to remove circular dependencies.

Early Reference Mechanism: Spring can expose a reference to a bean before it is fully initialized, allowing certain setter/field-based circular dependencies to be resolved.

Important:

- Constructor injection is still preferred because it makes dependencies clear and mandatory.
- Spring Boot 2.6+ disables circular references by default.
- Property: `spring.main.allow-circular-references=false`.
- Enabling it with `true` is possible but not recommended.
- Circular dependency is a design smell; refactoring is the best solution.

## 4. Bean Scope

Definition: Bean scope determines how many objects Spring creates for a bean definition and how long they live.

### Types of Bean Scope

| Scope       | Meaning                                      |
| ----------- | -------------------------------------------- |
| Singleton   | One object per bean definition per container |
| Prototype   | New object every time requested              |
| Request     | One object per HTTP request                  |
| Session     | One object per user session                  |
| Application | One object per web application               |
| WebSocket   | One object per WebSocket session             |

## 5. Singleton Scope

- Default scope in Spring.
- Spring creates one object per bean definition inside the container.
- Every request for that bean returns the same object.
- `@Scope("singleton")` is optional.

Important:

- Singleton does not mean one object per class in the entire JVM.
- Multiple bean definitions of the same class can create multiple singleton objects.
- Manually created objects using `new` are not Spring-managed beans.
- Generally suitable for stateless services.

## 6. Prototype Scope

- Creates a new object every time the bean is requested from Spring.
- Configured using `@Scope("prototype")`.
- Suitable for objects that maintain changing state.

Important:

- Prototype is similar to creating a new object using `new`.
- Spring manages creation and dependency injection but does not manage prototype destruction in the same way as singleton beans.
- If a prototype bean is injected into a singleton bean, only one prototype instance is injected during singleton creation. It does not create a new instance on every method call.

## 7. Web-Specific Scopes

- Request: One bean instance per HTTP request.
- Session: One bean instance per user session.
- Application: One bean instance per ServletContext / web application.
- WebSocket: One bean instance per WebSocket session.

These scopes require a web-aware Spring ApplicationContext.

## 8. Bean Initialization

Bean initialization determines when Spring creates a bean.

| Type                 | Meaning                                 |
| -------------------- | --------------------------------------- |
| Eager Initialization | Bean created during application startup |
| Lazy Initialization  | Bean created only when required         |

Default behaviour:

- Singleton beans → Eager initialization.
- Prototype beans → Created when requested.

## 9. Eager Initialization

- Spring creates the bean when the application context starts.
- Default for singleton beans.
- Helps detect configuration errors and missing dependencies during startup.
- Provides early error detection and fail-fast behaviour.

## 10. Lazy Initialization (@Lazy)

- Delays bean creation until it is actually needed.
- Spring initially keeps the bean definition rather than creating the actual object.
- Bean is created when requested through the container.

### Two Ways to Use @Lazy

1\. At Bean Level

```
@Component
@Lazy
public class ReportService {
}
```

The bean is not created during startup unless required by another eager bean.

2\. At Injection Point

```
public UserService(@Lazy EmailService emailService) {
    this.emailService = emailService;
}
```

Spring injects a proxy and resolves the actual dependency when its method is called.

Difference:

| Bean-Level @Lazy            | Injection-Point @Lazy                |
| --------------------------- | ------------------------------------ |
| Delays bean creation        | Injects a proxy                      |
| Bean created when requested | Actual dependency resolved when used |

## 11. Global Lazy Initialization

Enable lazy initialization for the entire Spring Boot application.

application.properties

```
spring.main.lazy-initialization=true
```

application.yml

```
spring:
  main:
    lazy-initialization: true
```

- Reduces application startup time.
- Errors may appear later when a bean is actually initialized.
- Default value is `false`.
- Use `@Lazy(false)` to force a particular bean to initialize eagerly.

## 12. Common Lazy Initialization Scenarios

| Scenario                                                  | Behaviour                               |
| --------------------------------------------------------- | --------------------------------------- |
| Eager bean → Lazy bean                                    | Lazy bean may be created during startup |
| Eager bean → Lazy dependency with injection-point `@Lazy` | Proxy is injected                       |
| Lazy bean → Eager bean                                    | Eager dependency is already created     |
| Lazy bean → Lazy bean                                     | Both are created when required          |

## 13. Bean Lifecycle

Definition: Bean lifecycle represents the complete journey of a bean from its discovery and creation to initialization, usage and destruction.

1\. Bean Definition

2\. Object Creation

3\. Dependency Injection

4\. Initialization

5\. Ready to Use

6\. Destruction

### Lifecycle Steps

1. Bean Definition: Spring discovers the bean through annotations, `@Bean` methods or XML configuration and stores its metadata.
2. Object Creation: Spring creates the actual object using its constructor.
3. Dependency Injection: Spring injects the required dependencies.
4. Initialization: Spring executes initialization logic, such as configuration validation, resource loading and setup.
5. Ready to Use: Bean becomes available for other beans and application operations.
6. Destruction: Spring performs cleanup when the container shuts down.

### Lifecycle Methods

| Mechanism             | Purpose                          |
| --------------------- | -------------------------------- |
| `@PostConstruct`      | Executes initialization logic    |
| `InitializingBean`    | Provides initialization callback |
| Custom init method    | Executes custom initialization   |
| `@PreDestroy`         | Executes cleanup logic           |
| `DisposableBean`      | Provides destruction callback    |
| Custom destroy method | Executes custom cleanup          |

Important: Prototype beans receive creation and dependency injection management, but Spring does not manage their destruction lifecycle like singleton beans.

## Final Quick Revision

| Concept              | Remember                              |
| -------------------- | ------------------------------------- |
| DI                   | Inject dependencies                   |
| IoC                  | Spring controls object creation       |
| Bean                 | Spring-managed object                 |
| Circular Dependency  | Beans depend on each other            |
| Singleton            | One object per bean definition        |
| Prototype            | New object per request to container   |
| Request Scope        | One object per HTTP request           |
| Session Scope        | One object per user session           |
| Application Scope    | One object per web application        |
| Eager Initialization | Created at startup                    |
| Lazy Initialization  | Created when needed                   |
| `@Lazy`              | Delays initialization / injects proxy |
| `@PostConstruct`     | Initialization callback               |
| `@PreDestroy`        | Destruction callback                  |

Source: Your uploaded Lecture 6 PDF, pages 1–38.&#x20;

Notes (5).pdf



This version covers all 50 numbered sections and the major concepts, examples, configurations and important points in your PDF, condensed for revision.