# Restful Mongo Application

Just a simple application that exposes Mono database through Restful APIs.

  - Uses Spring Boot for the Restful API
  - Containerzied Mongodb is used.
  - The applications is designed to be released as Docker container.
  - Docker-compose is used to bootstrap the application.

# Design!

  - Simple web application design. The application code is broken in traditional controller, repository and domain layers.
  - I deliberately skipped the services layer, reason being, I wanted to keep the code very compact.
  - The Unit tests provide decent coverage but not overall, with more time I would like to add more unit test.
  - The tests provide both unit and integration coverage.
  - Integration tests of repositories utilize embeded Mongodb instance.
  - The integration tests for controllers utilize mockito for mocking repositories.
  - Application is written with the motivation to keeping the code as generic as possible. Repositories are very thin and delegate all of the interesting bits to the BaseRepository.
 
### Tech

This project uses a number of open source projects to work properly:

* [Java 8] - Well, duh?
* [Spring Boot] - for Restful APIs!
* [Bean Validation API] - for validating the filters used for searching
* [Embeded in-memory Mongodb] - for integration tests for repositories.
* [Mongodb] - to hold application documents
* [Docker] - to package the Restful API. 
* [Docker-Compose] - to setup Mongodb and setup seed data for users and events. Also used to package the full stack.
* [Gradle] - to hold everything together
* [Git] - No explanation needed.

### How to Run

You only need Java 8 and Docker installed on your machine. On my Linux laptop I am running following application versions,

openjdk version "1.8.0_222"
OpenJDK Runtime Environment (build 1.8.0_222-8u222-b10-1ubuntu1~19.04.1-b10)
OpenJDK 64-Bit Server VM (build 25.222-b10, mixed mode)

Docker version 18.09.7, build 2d0083d

docker-compose version 1.21.0, build unknown

To build the application .

```sh
$ cd restful-mongo
$ docker-compose -f docker-compose/docker-compose.yml up
$ ./gradlew clean check bootRun
```
This would run the Mongodb in docker container and initialize it with seed data for both users and events. The last statement will run all the unit tests and then kick-start the Spring Boot application.

For production environments...

```sh
$ cd restful-mongo
$ ./gradlew clean check buildDockerImage
$ docker-compose -f docker-compose/docker-compose-fullstack.yml up -d
```
This would run all the tests and then build a docker image of the restful application. The last statement then bring up the whole application stack.

### Todos

 - Write MORE Tests
 - For some reason I can't get the MongoTemplate.getById work against the production instance. Funny thing is the getById API works from embeded instance running for integration tests. ¯\_(ツ)_/¯
 - The search doesn't work from the integration tests, but works against production instance. Really quirky behavior.
 - Both integrations tests UserRepositoryTest and EventRepositoryTest have Embeded Mongodb initialization built-in. Need to get it out of these test and put it in some generic base class.
 - Make the MongoTemplate in UserRepositoryTest and EventRepositoryTest a Spring managed bean that could be injected.

### Tested Uris
When passing the filter data in Json format in Get request Uri, we need to encode the the Json data so that the server can safely parse and construct filter object on the server side. 

I used com.faziz.exercise.tradeledger.FilterJsonEncoder class to construct the encoded Json for the search filter. This class uses Jackson library to serialize filter object and then encode the serialized Json data which I then pass on to the curl command for URI testing.

For looking up users and events by id,
curl http://localhost:6868/users/507f191e810c19729de860e0
curl http://localhost:6868/users/507f191e810c19729de860e2
curl http://localhost:6868/events/507f191e810c19729de8aae0
curl http://localhost:6868/events/507f191e810c19729de8aae3

For looking up users with ids less-than-equal to the given id,
{"attribute":"id","operator":"lte","value":"507f191e810c19729de860e2"}
http://localhost:6868/users/search?filter=%7B%22attribute%22%3A%22id%22%2C%22operator%22%3A%22lte%22%2C%22value%22%3A%22507f191e810c19729de860e2%22%7D

For searching for users using filters and range,
Json Representation
{"attribute":"id","operator":"eq","range":{"from":"507f191e810c19729de8aae0","to":"507f191e810c19729de8aae2"}}
UTF-8 Encoded filter value.
curl http://localhost:6868/events/search?filter=%7B%22attribute%22%3A%22id%22%2C%22operator%22%3A%22eq%22%2C%22range%22%3A%7B%22from%22%3A%22507f191e810c19729de8aae0%22%2C%22to%22%3A%22507f191e810c19729de8aae2%22%7D%7D

Following link shows use of multiple filters. 
curl http://localhost:6868/events/search?filter=%7B%22attribute%22%3A%22id%22%2C%22operator%22%3A%22eq%22%2C%22range%22%3A%7B%22from%22%3A%22507f191e810c19729de8aae0%22%2C%22to%22%3A%22507f191e810c19729de8aae2%22%7D%7D&filter=%7B%22attribute%22%3A%22id%22%2C%22operator%22%3A%22eq%22%2C%22value%22%3A%22507f191e810c19729de860e1%22%7D



