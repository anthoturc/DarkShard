## DarkShard

### Idea

DarkShard is a microservice that vends APIs for submitting code to be executed remotely.
This is similar to the core logic used by sites like LeetCode or Hackerrank.  

You can classify it as a stateless request reply service.

It will support a front-end service that is responsible for giving users a code editor in the browser.

The workflow for a request would look something like this:

```
user browser  frontend (js, auth?, ...)  darkshard
   +---+            +---+                 +---+
   |   | <--------> |   | <-------------> |   |
   +---+            +---+                 +---+
```

### Why?
I just wanted to practice using some technology/implement something end-to-end using all the best practices I have learned.
Also, I have been thinking about how the leetcode backend works. I decided to just take a stab at the following problem:
* Build a system that allows users to submit programs to be executed remotely and responds with the results of 
  that program
  
#### Requirements
* DarkShard should be highly available
* Don't need to ensure strong consistency. 
* Assuming that programs will not exceed 4 MB.
* Submitted programs will timeout after 5 minutes.
* Users can submit jobs anonymously (this may change)
* This service needs to be secure (on the server end)

### Build

```
$ mvn clean package
```

### Build Docker image

```
$ docker build -t shard . # The Dockerfile consists of a a mutli stage build so you will have left over images
$ docker container run -p 50051:50051 --rm shard # Start up the DarkShard service
```

### Run the server locally

### TODOs
* Implement CRUD on code exec jobs
* Add integration tests (in a new repo)
* Add support for any language
* Add task queue + workers (could be another service) with actual code execution logic
  * This is giving us some sense of async task execution
* Add notes on how to run the server locally
* Add Docker set up to containerize this app



### Technology
#### Tech used directly
1. [Java 11](https://www.java.com/en/)
1. [gRPC](https://grpc.io/) (for communication, service definition, client/server code gen)
1. [Dagger2](https://dagger.dev/) (for dependency injection)
1. [Immutables](https://immutables.github.io/) (lots of benefits; e.g. thread safety, protection from NPEs)
1. [JUnit5](https://junit.org/junit5/) (testing)
1. [maven](https://maven.apache.org/) (dependency management + builds)
1. [mongodb](https://www.mongodb.com/) (external data store)
1. [Docker](https://docs.docker.com/) (to containerize the service)
#### Tech used to help verify behavior
1. [BloomRPC](https://github.com/bloomrpc/bloomrpc) Testing gRPC calls against the service 
   (esp while integration tests are not ready)

