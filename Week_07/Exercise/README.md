# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [Exercise 1 - Create project](#exercise-1---create-project)
- [Exercise 2 - Database Setup](#exercise-2---database-setup)
- [Exercise 3 - Read data to controller](#exercise-3---read-data-to-controller)
- [Exercise 4 - Read all users](#exercise-4---read-all-users)
- [Exercise 5 - User Registration](#exercise-5---user-registration)
- [Exercise 6 - User Authentication](#exercise-6---user-authentication)
- [Exercise 7 - User Autherization](#exercise-7---user-autherization)
 
 
 
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
 
Now lets test with PostMan.

 ## Exercise 4 - Read all users
 
 Do this exercise alone this time :).
 

 ## Exercise 5 - User Registration
 
 Create a new value object class for the request.
 
 
```Java
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

Update user-service with the register method.

```Java

public void registerNewUser(UserRequestVo userRequestVo) {
        User user = new User();
        user.setUserName(userRequestVo.getUsername());
        user.setPassword(userRequestVo.getPassword());
        user.setEmail(userRequestVo.getEmail());

        userRepository.save(user);
    }


```

Update user-controller with the request part.


```Java
@PostMapping("/user/register")
    @CrossOrigin
    public void registerNewUser(@RequestBody UserRequestVo userRequestVo) {
        userService.registerNewUser(userRequestVo);
    }
```

Now lets test with PostMan.


 ## Exercise 6 - User Authentication
 
 Create a new class for the token response: UserTokenResponseVo.java
 
 
 ```Java
 package com.project.user.administration.vo;

public class UserTokenResponseVo {

    private String username;
    private String token;

    public UserTokenResponseVo(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
 ```
 
 Update user repository class with the method for retrieving the user.
 
  ```Java
  
  @Query("SELECT u FROM User u WHERE u.userName = ?1 and u.password = ?2")
  public User findUserByStatusAndName(String userName, String password);
   
 ```
 
 
 Create user-login Jpa class and repository class
 
 ```Java
 
 package com.project.user.administration.model;

import javax.persistence.*;

@Entity
@Table(name = "user_login")
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_id", updatable = false, nullable = false)
    private Long userLoginId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "token")
    private String token;

    @Column(columnDefinition = "token_expire_time")
    private String tokenExpireTime;

    public UserLogin(){
    }

    public UserLogin(User user, String token, String tokenExpireTime){
        this.user=user;
        this.token=token;
        this.tokenExpireTime= tokenExpireTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(Long userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(String tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
```

```Java
package com.project.user.administration.repository;

import com.project.user.administration.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    @Query("SELECT u FROM UserLogin u WHERE u.user.userName = ?1  and u.token = ?2")
    public UserLogin findByUserAndToken(String userName, String token);

}
 
 ```
  
 Update user-service class with validation, user-login save and token generation logick.
 
 
 ```Java
 
  public String getCurrentTimeStamp() {
        Date newDate = DateUtils.addHours(new Date(), 3);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
  }
    
  public static String createJsonWebToken(String username){
        String jwt = JWT.create()
                    .withSubject(username)
                    .withIssuer("auth0")
                    .withExpiresAt(DateUtils.addHours(new Date(), 3))
                    .sign(Algorithm.HMAC256(System.getProperty("aplication-secret")));
        return jwt;
  }
    
    
 public UserTokenResponseVo validateUserCredentialsAndGenerateToken(UserRequestVo userRequestVo) {

        User user = userRepository.findUserByStatusAndName(userRequestVo.getUsername(), userRequestVo.getPassword());

        if( user != null) {

            //String token=  RandomStringUtils.random(25, true, true);
            String token = createJsonWebToken(userRequestVo.getUsername());

            UserLogin userLogin= new UserLogin(user, token, getCurrentTimeStamp());
            userLoginRepository.save(userLogin);

            UserTokenResponseVo userTokenResponseVo = new UserTokenResponseVo();
            userTokenResponseVo.setToken(token);
            userTokenResponseVo.setUsername(userRequestVo.getUsername());

            return userTokenResponseVo;
        } else {
            throw new RuntimeException("User not found");
        }
    }
 
  ```
  
  Update user-controller for the authenticate http method.
  
 ```Java
   @PostMapping("/user/authenticate")
   @CrossOrigin
    public UserTokenResponseVo login(@RequestBody UserRequestVo userRequestVo) {
        return userService.validateUserCredentialsAndGenerateToken(userRequestVo);
    }
```

Test it with post man.


## Exercise 7 - User Autherization

Create the TokenResponse class.

 ```Java
 
 package com.project.user.administration.vo;

public class UserTokenResponseVo {

    private String username;
    private String token;

    public UserTokenResponseVo(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

 ```
 
 Update User Service class with the autherization logick.
 
 
 
  ```Java
  
  public UserAuthorizeResponseVo authorizeV1(UserRequestVo userRequestVo) throws ParseException {
        UserLogin userLogin = userLoginRepository.findByUserAndToken(userRequestVo.getUsername(), userRequestVo.getToken());
	
	// check user-login in database
        if(userLogin != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(userLogin.getTokenExpireTime());

	    // check if token expired	
            if(new Date().compareTo(date) <1){
                return new UserAuthorizeResponseVo(userRequestVo.getUsername(), true);
            } else {
                return new UserAuthorizeResponseVo(userRequestVo.getUsername(), false);
            }
        }
        return new UserAuthorizeResponseVo(userRequestVo.getUsername(), false);
    }

    public UserAuthorizeResponseVo authorizeV2(UserRequestVo userRequestVo) throws ParseException {
        String userName = extractUserNameFromToken(userRequestVo.getToken());
        UserLogin userLogin = userLoginRepository.findByUserAndToken(userName, userRequestVo.getToken());
	
	// check user-login in database
        if(userLogin != null){
            return new UserAuthorizeResponseVo(userRequestVo.getUsername(),  verifyToken(userRequestVo.getUsername(),     userRequestVo.getToken()));
        }
        return new UserAuthorizeResponseVo(userRequestVo.getUsername(), false);
    }


    public static String extractUserNameFromToken( String token) throws JWTVerificationException{

            Algorithm algorithm = Algorithm.HMAC256(System.getProperty("aplication-secret"));
            JWTVerifier verifier =  JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            return  jwt.getSubject();

    }

    public static boolean verifyToken(String user, String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(System.getProperty("aplication-secret"));
            JWTVerifier verifier =  JWT.require(algorithm)
                                        .withIssuer("auth0")
                                        .build();

            DecodedJWT jwt = verifier.verify(token);
            String subject = jwt.getSubject();

            Date dateTheTokenWillExpire = jwt.getExpiresAt();
            if(new Date().compareTo(dateTheTokenWillExpire) <1){
                return true;
            } else {
                return false;
            }

        } catch(JWTVerificationException exception){
            return false;
        }

    }
}
```

Update controller for the authorize request

 ```Java
 @PostMapping("/user/authorize")
    @CrossOrigin
    public UserAuthorizeResponseVo authorize(@RequestBody UserRequestVo userRequestVo) throws ParseException {
        return userService.authorizeV2(userRequestVo);
    }
 ```
 
 
 Test it with post man.
