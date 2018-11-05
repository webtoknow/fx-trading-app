# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [Exercise 1 - Create project](#exercise-1-create-project)
- [Exercise 2 - Database Setup](#exercise-2-database-setup)
- [Exercise 3 - Read data to controller](#exercise-3-read-data-to-controller)
 
 
 
 
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


 ## Exercise 2 - Database Setup
 
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
 
 Create the following folders in the package: com.project.user.administration
 1) controller
 2) exception
 3) model
 4) repository
 5) services
 6) vo
 
 Create the first entity 
 
 ```Java
 
 package com.project.user.administration.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(columnDefinition = "user_name")
    private String userName;

    @Column(columnDefinition = "password")
    private String password;

    @Column(columnDefinition = "email")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<UserLogin> logins = new ArrayList<>();

    public User(){
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserLogin> getLogins() {
        return logins;
    }

    public void setLogins(List<UserLogin> logins) {
        this.logins = logins;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
 
```
 
 
Create the user repository for the read an user.
 
 ```Java
 
package com.project.user.administration.repository;

import com.project.user.administration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUserId(Long userId);
}

 ```
 
 ```Java
 Create the user value object to pass to the front end.
 
 package com.project.user.administration.vo;

public class UserRequestVo {

    private String username;
    private String email;
    private String password;
    private String token;

    public UserRequestVo(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
 ```
 
Create the user service for the database operations.
 
 
 ```Java
 
 package com.project.user.administration.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.user.administration.model.User;
import com.project.user.administration.model.UserLogin;
import com.project.user.administration.repository.UserLoginRepository;
import com.project.user.administration.repository.UserRepository;
import com.project.user.administration.vo.UserAuthorizeResponseVo;
import com.project.user.administration.vo.UserTokenResponseVo;
import com.project.user.administration.vo.UserRequestVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService {


@Autowired
private UserRepository userRepository;


 public UserRequestVo findByUserId(Long userId){

        User user = userRepository.findByUserId(userId);

        UserRequestVo uservo  = new UserRequestVo();
        uservo.setUsername(user.getUserName());

        return uservo;
    }


}
 
```
 
 Create the user controller.
 
 
 ```Java
 
package com.project.user.administration.controller;

import com.project.user.administration.services.UserService;
import com.project.user.administration.vo.UserAuthorizeResponseVo;
import com.project.user.administration.vo.UserTokenResponseVo;
import com.project.user.administration.vo.UserRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    @CrossOrigin
    public UserRequestVo getAnswersByQuestionId(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }
 }   
 
 ```
