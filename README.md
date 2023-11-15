# TODO Management Application

##### Todo App (REST-API)

## Tech Stack Used
* Java 17
* Spring Boot
* Spring Security (JWT Based Authentication and Authorization)
* Spring Data JPA/Hibernate
* Spring Boot Validator
* PostgreSQL PL/pgSQL
* Open-API
* Lombok

## Security Flow
* On Successful validation of login credentials, a JWT will be returned representing the user **(decode the below sample JWT on jwt.io for reference)**

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXJkaWsuYmVobDc0NDRAZ21haWwuY29tIiwiYWNjb3VudF9jcmVhdGlvbl90aW1lc3RhbXAiOiIyMDIxLTA1LTI5VDIzOjU1OjM0LjgyMzQ1MCIsInVzZXJfaWQiOiJmODA1OWZlMC04ODE5LTQ1M2UtYTc2NC01ZDlkMzg3NjJiY2EiLCJzY29wZSI6InVzZXIiLCJuYW1lIjoiSGFyZGlrIFNpbmdoIEJlaGwiLCJleHAiOjE2MjIzNDg3NDYsImlhdCI6MTYyMjMxMjc0Nn0.jCirtPsrAlSeIeNuZshh-7PuCoStShBYacHFyFyiBmM
```
* The received JWT should be included in the headers when calling a protected API
* Authentication Bearer format to be used **(Header key should be 'Authentication' and value should start with Bearer followed with a single blank space and recieved JWT)**

```
Authentication : Bearer <JWT>
```

## Setup

* Install Java 17
* Install Maven

Recommended way is to use [sdkman](https://sdkman.io/) for installing both maven and java

Run mvn clean install in the core

```
mvn clean install
```

Run docker commands

```
sudo docker-compose up
```

Service port is 8080 and Postgres Port is 5432. They both can be changed in the [docker-compose.yml](docker-compose.yml) file


To stop the container run

```
sudo docker-compose stop
```

## To Run Locally Without Docker

Create postgres user (superuser) with name and password as dammey

```
CREATE USER loki WITH PASSWORD 'dammey' SUPERUSER;
```
Create Database with name 'todo' and assign the above created user to the database with preferable CLI or GUI tool

```
create database todo;
```

Run mvn clean install in the core

```
mvn clean install
```

Run Tests

```
mvn test
```

Run Application

```
run as -> spring boot application
```

API Documentation can be viewed by visiting the below link (can be altered in application.properties)

```
http://localhost:8083/swagger-ui/index.html
```