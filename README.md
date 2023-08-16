# tripmgtservice
Microservice for managing trips and driver matching based on driver and customer locations. High throughput achieve using Kafka.

## Features
* Drivers' current location update.
* Trip management and trip history maintain.
* Driver matching based on geo data processing.
* Trip fee calculation based on distance and waining time.
* Highly scalable.

## Supported versions

system will support the following versions.  
Other versions might also work, but we have not tested it.

* Java 8, 11
* Spring Boot 2.7.5

## Building and running

To build and test, you can run:

```sh
$ cd cab-app-tripmgtservice
# build the application
$ mvn clean install
# run the application
$ java -jar target/tripmgtservice-0.0.1.jar
```

## Contributing

Bug reports and pull requests are welcome :)