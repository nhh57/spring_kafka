# Code Conventions

This project adheres to the following code conventions:

## Java Specifics:
- Follow Oracle's Java Code Conventions.
- Use PascalCase for class names.
- Use camelCase for method names, field names, and local variables.
- Use clear, descriptive, and consistent naming.
- Utilize `CompletableFuture` for asynchronous operations.

## Project Specifics:
- Adhere to Clean Architecture principles.
- Use Spring Boot for application configuration and dependency management.
- Use Spring Data JPA for the data access layer.
- Use Spring Web for RESTful API development.
- Use Spring Validation for input validation.
- Use Spring AOP for cross-cutting concerns (e.g., logging, transactions).
- Use Spring Security (if applicable) for authentication and authorization.

## Naming Conventions:
- Class names: PascalCase
- Method/field/variable names: camelCase
- Clear, descriptive, and consistent naming should be used throughout the codebase.

## Testing:
- Unit tests are written using JUnit 5.
- Mockito is used for mocking dependencies.
- Both positive and negative test cases should be included.
- Tests are located under `src/test/java`.
- `@SpringBootTest` is used for integration tests.

## Logging:
- **Always** use SLF4J for logging instead of `System.out.printf` or `System.out.println`.
- Obtain a logger instance using `org.slf4j.LoggerFactory`.
- Example: `private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(YourClass.class);`