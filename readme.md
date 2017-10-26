# WalletHub Assigment

This project is a parser in Java that parses web server access log file, loads the log to MySQL and checks if a given IP makes more than a certain number of requests for the given duration.

It was built using Uncle Bob Clean Archtecture design, and made to keep low use of memory, no matter the file size.

### Project Archtecture
* SpringBoot
* Spring-Data JPA
* Lombok
* Apache Commons CLI 
* MySql

### How to run
As requested in this challenge, a jar is already generated and ready in the root folder. To run it, simple run the command:

```sh
$ java -jar assigment.jar --accesslog=access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 
```

##### Important!
This application require an instance of MySQL running in port 3306 at localhost. If you want, you can instantiate a version of it running the docker-compose file inside de ``/dependencies`` folder:
```sh
$ cd dependencies && docker-compose up -d
```

### To regenerate the JAR
A minified version of maven is already included in the root. To generate a new executable jar file, execute the command:
```sh
$ ./mvnw clean package
```
The new file will be under ``/target``

### SQL
The Sql's requested are inside the ``/SQL`` folder

### Desired improvments
* Add a external configuration file
* Add a control table using file MD5 to evict file parsing on every run