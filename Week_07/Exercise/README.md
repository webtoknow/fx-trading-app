# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [Exercise 1 - Create project](#exercise-1---create-project)
- [Exercise 2 - Database Setup](#exercise-2---database-setup)
- [Exercise 3 - Read data to controller](#exercise-3---read-data-to-controller)
- [Exercise 4 - Read all users](#exercise-4---read-all-users)
- [Exercise 5 - User Registration](#exercise-5---user-registration)
- [Exercise 6 - User Authentication](#exercise-6---user-authentication)
- [Exercise 7 - User Authorization](#exercise-7---user-authorization)

 ## Pre-requisites
    - Install Java 11
    - Install maven
    - Install PostgreSQL
    - Install Postman
 
 
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
        <version>2.1.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.10</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.2</version>
        </dependency>
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
</project>
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
 
 Create the following property file under the "resources" package: application.properties and add the following lines to it
 
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
 Run the following sql commands to create the prerequisites.
 
 
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


Now use the Grant Wizzard to grant to your db-user access to your db , or just run the following scripts directly:

```SQL 
CREATE ROLE new_user LOGIN PASSWORD 'new_user_password';

REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT CONNECT on DATABASE users  TO new_user;

GRANT USAGE ON SCHEMA public TO new_user;

GRANT ALL PRIVILEGES ON DATABASE users TO new_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO new_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO new_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO new_user;
```

 ## Exercise 3 - Read data to controller
 
 Create the following packages under: com.project.user.administration
 1) controller
 2) exception
 3) model
 4) repository
 5) services
 6) vo
 
 Create the first entity 
 
 ```Java
package com.project.user.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
```
 
 
Create the repository for the User entity in order for it to be able to read an user.
 
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

 Create the user value object to be passed to the front-end.
 
 ```Java
package com.project.user.administration.vo;

 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class UserRequestVo {

     private String username;
     private String email;
     private String password;
     private String token;

 }
 ```
 
Create the user service and add method for finding by user id
 
```Java
 
package com.project.user.administration.services;

import com.project.user.administration.model.User;
import com.project.user.administration.repository.UserRepository;
import com.project.user.administration.vo.UserRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRequestVo findByUserId(Long userId) {

        User user = userRepository.findByUserId(userId);

        return UserRequestVo.builder()
                .username(user.getUserName())
                .email(user.getEmail())
                .build();
    }
}
 
```
 
 Create the user controller.
 
 
 ```Java
 
package com.project.user.administration.controller;

import com.project.user.administration.services.UserService;
import com.project.user.administration.vo.UserRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    @CrossOrigin
    public UserRequestVo getUserById(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }
}
 ```
 
Use Postman to test the endpoint.

Example: GET - localhost:8200/user/1  =>  should return the user "razvan"

 ## Exercise 4 - Read all users
 Do this exercise alone this time :)

 ### 4.a. - GET all users
    Example: GET localhost:8200/user/all  =>  should return all users
    Hint: Use the map() methods from Java stream to transform the List<User> to List<UserRequestVo>

 ### 4.b. [Optional] - GET all users whose username contains a given sequence of letters
    Example: GET - localhost:8200/user?name=andr  =>  should return the user whose username is "andrei"
    Hints: - The name should be passed to the request as a @RequestParam
           - Use the filter() and map() methods from Java stream

 ## Exercise 5 - User Registration

Add the register method to UserService.

```Java

public void registerNewUser(UserRequestVo userRequestVo) {
    User user = User.builder()
        .userName(userRequestVo.getUsername())
        .password(userRequestVo.getPassword())
        .email(userRequestVo.getEmail())
        .build();

    userRepository.save(user);
}

```

Add registerNewUser method to the UserController

```Java
@PostMapping("/user/register")
@CrossOrigin
public void registerNewUser(@RequestBody UserRequestVo userRequestVo) {
    userService.registerNewUser(userRequestVo);
}
```

Use Postman to test the new endpoint.


 ## Exercise 6 - User Authentication
 
 Create a new class for the token response: UserTokenResponseVo.java

 ```Java
package com.project.user.administration.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenResponseVo {
    private String username;
    private String token;
}
 ```
 
 Update the UserRepository by adding a method for retrieving the user.
 
```Java
  
@Query("SELECT u FROM User u WHERE u.userName = ?1 and u.password = ?2")
public User findUser(String userName, String password);
   
```
 
 
 Create the UserLogin entity
 
 ```Java
package com.project.user.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
```

Update the User entity and add a new attribute:
```Java
@OneToMany(mappedBy = "user")
private List<UserLogin> logins = new ArrayList<>();
```

Create the repository for the UserLogin class
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

 Inject the UserLoginRepository in the UserService
 ```Java
 @Autowired
 UserLoginRepository userLoginRepository;
 ```

  
 Update the UserService class with validation, user-login save and token generation logic.
 
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
                    .sign(Algorithm.HMAC256("secret"));
        return jwt;
  }
    

 public UserTokenResponseVo validateUserCredentialsAndGenerateToken(UserRequestVo userRequestVo) {

        User user = userRepository.findUser(userRequestVo.getUsername(), userRequestVo.getPassword());

        if( user != null) {

            //String token=  RandomStringUtils.random(25, true, true);
            String token = createJsonWebToken(userRequestVo.getUsername());

             UserLogin userLogin = UserLogin.builder()
                    .user(user)
                    .token(token)
                    .tokenExpireTime(getCurrentTimeStamp())
                    .build();
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
  
  Add the authentication method to the UserController.
  
 ```Java
   @PostMapping("/user/authenticate")
   @CrossOrigin
    public UserTokenResponseVo login(@RequestBody UserRequestVo userRequestVo) {
        return userService.validateUserCredentialsAndGenerateToken(userRequestVo);
    }
```

Test this endpoint with Postman.


## Exercise 7 - User Authorization

Create the UserAuthorizeResponseVo class.

 ```Java
 package com.project.user.administration.vo;

 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class UserAuthorizeResponseVo {
     private String username;
     private boolean isValid;
 }
 ```
 
 Update UserService class with the authorization logic.

 
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

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier =  JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            return  jwt.getSubject();

    }

    public static boolean verifyToken(String user, String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256("secret");
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

```

Update the UserController and add the authorize request

 ```Java
    @PostMapping("/user/authorize")
    @CrossOrigin
    public UserAuthorizeResponseVo authorize(@RequestBody UserRequestVo userRequestVo) throws ParseException {
        return userService.authorizeV2(userRequestVo);
    }
 ```
 
 Test it with Postman.
