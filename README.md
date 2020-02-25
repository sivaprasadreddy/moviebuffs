# MovieBuffs

## Backend Tech Stack
* Java
* SpringBoot 2.x
* H2(Dev) / Postgres (Prod)
* Spring Data JPA
* Spring Security JWT Authentication
* Jasypt
* Swagger2
* Zalando problem-spring-web
* Flyway
* SonarQube
* Jacoco
* Maven
* JUnit 5, Mockito, Testcontainers

## Frontend Tech Stack
* ReactJS
* Redux, React Router
* Axios
* Font-awesome

## How to run?

### Run Backend tests

`moviebuffs/moviebuffs-api> ./mvnw clean verify`

### Run application locally

`moviebuffs/moviebuffs-api> ./mvnw clean package -Pci & java -jar target/bookmarker-0.0.1-SNAPSHOT.jar`

* Application: http://localhost:8080/

### Running using Docker

To start application and Postgres

`moviebuffs> ./run.sh start`

To start application and all dependent services like ELK, grafana, prometheus

`moviebuffs> ./run.sh start_all`

* Application: http://localhost:18080/
* SwaggerUI: http://localhost:18080/swagger-ui.html
* Prometheus: http://localhost:9090/
* Grafana: http://localhost:3000/ (admin/admin)
* Kibana: http://localhost:5601/ 

### Run Performance Tests

`moviebuffs/moviebuffs-gatling-tests> ./mvnw gatling:test`

### Run SonarQube analysis

```
> ./run.sh sonar
> ./mvnw clean verify -Psonar -Dsonar.login=$SONAR_LOGIN_TOKEN
```
