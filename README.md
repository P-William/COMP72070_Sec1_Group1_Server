# Accord

## Documentation
### [Webscocket Documentation](docs/websockets/websockets.md)



## Pre-requisites:
- Java Development Kit (JDK) 21 found [here](https://adoptium.net/temurin/releases/?os=windows)
- Maven (build tool) found [here](https://maven.apache.org/download.cgi)

## Project Setup
### Database Setup
[Setup Information](docs/postgresql-setup.md)
### Configuration Setup
[Config Information](docs/config-setup.md)

## Running the Application:
[Add run config](docs/run-config.md)


## Useful Links:
### Useful Links:
#### Quality of Life
- Lombok is great. Don't want to have to write getters and settings? Don't! Add `@Getter` and/or `@Setter` to a class and have them auto generated at compile time! Also has other very useful annotations. [docs](https://projectlombok.org/features/)
- Apache Commons Lang 3 has a bunch of useful methods that can come in handy [javadoc](https://javadoc.io/doc/org.apache.commons/commons-lang3/3.10/overview-summary.html)
#### Documentation
- [PostgreSQL](https://www.postgresql.org/docs/16/index.html)
- [Swagger Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations) Swagger is used for API documentation.
- [Spring Boot](https://docs.spring.io/spring-boot/index.html) Spring is a huge and amazing framework that helps to develop java apps easier. This is just one doc resource, there are many.
- [Spring Servlet](https://docs.spring.io/spring-boot/reference/web/servlet.html) Servlet makes crating REST APIs much easier.
- [Spring Websockets](https://www.baeldung.com/websockets-spring)
- [Spring Security](https://docs.spring.io/spring-security/reference/servlet/index.html)
#### Testing
- AssertJ is about the nicest tool you will see for creating assertions. [AssertJ](https://assertj.github.io/doc/)
- JUnit 5 is the test runner we are using. [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- Mockito is a framework that makes stubbing easy. You can mock objects and stub their return value when it's called within a method. [Mockito](https://site.mockito.org/)