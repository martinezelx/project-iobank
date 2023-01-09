# Spring Boot Rest API that simulates a small bank

## Tech Stack
- IDE: IntelliJ IDEA Community Edition (2022.3.1)
- Java 19: [OpenJDK JDK 19.0.1](https://jdk.java.net/19/)
- Spring Boot: 3.0.1
- MapStruct: 1.5.3-Final
- Lombok: 1.18.24
- SpringDoc OpenApi v2: 2.0.2
- Maven: 3.10.1
- H2 Database: 2.1.214
- Testing: Mockito 4.8.1 / JUnit 5.9.1

## Architecture
I have based the project on this tutorial to make the architecture: [Hexagonal Architecture](https://www.baeldung.com/hexagonal-architecture-ddd-spring)

## Prerequisites to test/run the application
- Java 19.0.1
- Maven 3.10.1
- To check API status can use [this actuator](http://localhost:8090/actuator/health)

## Updated Requirements
While working on the project, I thought of several improvements that could be made:
1. To apply a correct use of the DDD methodology, 
everything related to users should go into another microservice 
and leave the current one only for accounts and transactions.
2. Perform more tests for the different exceptions.
3. Several improvements in the code to make it more readable and maintainable.
4. Some change in the nomenclature of the method names may be necessary to better adapt it to the SOLID principle.

## Run tests
You can use your favorite IDE to run the tests or use the following command:
```shell
mvn test
```

## Run the application
1. Clone the repository
2. In the cloned folder repository, run the following command:
```shell
mvn spring-boot:run
```

## Available endpoints
Since the project uses SpringDoc OpenApi v2, we can see the available endpoints at the following link:
[http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)

In [postman](postman) folder you can find the collection of endpoints in JSON.

#### Create user: 
- [localhost:8090/api/v1/iobank/users/create](localhost:8090/api/v1/iobank/users/create)
#### Create account: 
- [localhost:8090/api/v1/iobank/accounts/create](localhost:8090/api/v1/iobank/accounts/create)
#### Create transaction:
- [localhost:8090/api/v1/iobank/transactions/create](localhost:8090/api/v1/iobank/transactions/create)
#### Get user (also by Id):
- [localhost:8090/api/v1/iobank/users/search](localhost:8090/api/v1/iobank/users/search)
#### Get account (also by Id):
- [localhost:8090/api/v1/iobank/accounts/search](localhost:8090/api/v1/iobank/accounts/search)
#### Get transaction (also by Id):
- [localhost:8090/api/v1/iobank/transactions/search](localhost:8090/api/v1/iobank/transactions/search)
#### Create deposit:
- [localhost:8090/api/v1/iobank/accounts/deposit/{id}](localhost:8090/api/v1/iobank/accounts/search)

### Examples to call the endpoints
You can use the HTTP client of your choice to call the endpoints. In the next examples I will use Postman to perform the requests.

Using the [import feature of Postman](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/) you can import the OpenAPI definition from the following link: [http://localhost:8090/v3/api-docs](http://localhost:8090/v3/api-docs)