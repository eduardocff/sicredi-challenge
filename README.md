# Sicredi Challenge - Eduardo Cardoso
Sicredi challenge for the position of back-end developer.

### Project Dependencies
- Java 11
- Maven

### Application
- To run: ``mvn clean spring-boot:run``

### Documentation
For project documentation I used ``Swagger``.

- SWAGGER: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
- SWAGGER-CLOUD: [https://sicredi-challenge-cloud.herokuapp.com/swagger-ui/index.html](https://sicredi-challenge-cloud.herokuapp.com/swagger-ui/index.html)

### Unit Tests
- ``WebMvcTest``: Controller tests;
- ``Mockito``: Service tests;

### Spring Boot and infrastructure
- ``Spring Boot``: Automatically configures, integrations with several libs, easy to use and also because I already have familiarity
- Database - ``h2 database``: I opted for a memory managed database as I didn't want any extra project dependencies (like servers etc). even though it is based on memory, a configuration was made so that data would not be lost on application restart.
- Messaging Management - ``ActiveMQ``: I thought of some options like Kafka and RabbitMQ, but for the same reason I chose the database, the ActiveMQ used in the project allows me to create queues without having to configure a server apart from the application. So, it became easier and I learned a new technology :D.
- Logging - ``Slf4j``: Provided by ``Lombok`` and easy to use. 
- ``Lombok``

### Important informations
- The database starts empty, so it is necessary to create the associates through cpf and name.
- The voting has 3 statuses: Created, Opened and Closed.
- Voting is 1 minute by default.
- At the end of each voting, the final result is published in a queue.
- For each vote of a Associate, the CPF is validated through the external url previously available.
