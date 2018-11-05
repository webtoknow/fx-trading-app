# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [Exercise 1 - Create project](#exercise-1-create-project)
- [Exercise 2 - Database Setup](#exercise-2-database-setup)
- [Exercise 3 - Read data to controller](#exercise-3-read-data)
 
 
 
 
 ## Exercise 1 - Create project
 
 Use your favourite IDE to create a simple maven project.
 Update pom.xml with the following tags:
 
 ```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.project</groupId>
	<artifactId>user-administration</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>user-administration</name>
	<description>Spring Boot for user administration</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.1.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.10</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.7</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.4.0</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

```


Create the following package in src/main/java:
com.project.user.administration


Create under it the main class: UserAdministrationApplication.java


 ```Java
package com.project.user.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserAdministrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserAdministrationApplication.class, args);
	}
}

 ```
 
 Create the following property file: application.properties
 
```
 ## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url=jdbc:postgresql://localhost:5432/users
spring.datasource.username=new_user
spring.datasource.password=new_user_password

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

server.port=8200

```


Now run the main class.


 ## Exercise 2 - Create database
 
 Create in postgresql the database: "users"
 Use the following sql commands to create the prerequisites.
 
 
 ```Sql
 CREATE   TABLE user_table (
    user_id SERIAL PRIMARY KEY,
    user_name varchar(255),
    email varchar(255),
    password varchar(255)
);


CREATE  TABLE user_login (
    user_login_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES user_table(user_id),
    token varchar(255),
    token_expire_time varchar(255)
);


INSERT INTO user_table (user_name , email , password)
VALUES ('razvan', 'razvan@gmail.com', 'razvanPassword');

INSERT INTO user_table (user_name , email , password)
VALUES('mihai', 'mihai@gmail.com', 'mihaiPassword');


INSERT INTO user_table (user_name , email , password)
VALUES('andrei', 'andrei@gmail.com','AndreisPassword');
```

Now user Grant Wizzard to grant access of your db to your db-user.



 ## Exercise 3 - Read data to controller
