# Week 9 - Development : software security
### Table of contents

- [Spring Boot](#spring-boot)
  	- [About](#about)
  	- [Spring Data](#springData)
  	- [Entities](#entities)
  	- [Repository](#repository)
  	- [Service](#service)
  	- [Controller](#controller)
- [Security in today context](#security-in-today-context)
	+ [Digital security](#digital-security)
	+ [Application security](#application-security)
	+ [Software security](#software-security)
- [Spring Security](#spring-security)
	+ [What is it](#what-is-it)
	+ [Main features](#main-features)
	+ [Authentication](#authentication)
	+ [Authorization](#authorization)
	+ [Building blocks](#building-blocks-for-java)
- [JWT approach](#jwt-approach)
	- [Overview](#overview)	
	- [Creation and usage](#creation-and-usage)
- [References](#references)

## About

Spring Boot is an open source Java-based framework used to create Micro Services.
It is easy to create a stand-alone and production ready spring application.



The Spring Boot Maven plugin provides many convenient features:

1) It collects all the jars on the classpath and builds a single, runnable jar, which makes it more convenient to execute and transport your service.
2) searches for the public static void main() method to flag as a runnable class.
3) It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. 
You can override any version you wish, but it will default to Boot’s chosen set of versions.


## Spring Data

Spring Data JPA is the part of the application that connects our Spring data Java application to the database.

The communication between our spring/java application with the database for storing and retrieving data is always done through this layer.



## Entities
Entities are part of the Spring Data and they represent a mapping of the java Classes to the tables in the databases.
We can see that every entity class has a corresponding table in the database. Every member of the class is mapped to a column in the database. 
Usually the entities can be found in a package named 'model' or 'entities'.
All entities are marked with the @Entity


```Java
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

    @Column(name = "token")
    private String token;

    @Column(name = "token_expire_time")
    private String tokenExpireTime;

}
```

```Java
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

    @Column(name = "token")
    private String token;

    @Column(name = "token_expire_time")
    private String tokenExpireTime;

}
```

## Repository
Repositories are part of Spring Data and they represent a Class that contains the actions/interations that are performed on the database. Actions can be either adding new data in the database(register a new user) or retrieving data from the database(finding and existing user). 
Usually the repositories can be found in a package named 'repository'.

Spring data provides a base class JpaRepository that helps us from writting the following methods:

- findAll
- count, delete, deleteAll, deleteAll, deleteById, existsById, findById, save
- exists, findOne
- deleteAllInBatch()
- deleteInBatch(Iterable<T> entities)
  
  
The repository interface is used for extending the CRUD interface. This interface adds the layer of a repository in the program. Spring Data JPA provides two major ways of creating queries. These queries are then used in the repository interface to fetch the data from the database.

Repositories are usually marked with @Repository annotation.


```Java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUserId(Long userId);

    @Query("SELECT u FROM User u WHERE u.userName = ?1 and u.password = ?2")
    public User findUserByStatusAndName(String userName, String password);
}
```
## Service
The Service Classes contain only the business related logic( a certain functionality for an operation). The service classes interact directly with the repository classes in order to perform an operation, usually the service class needs information from the database or  changes information into the database.
The service classes can be found in the service package.

## Controller
The Controller classes contain the endpoints(the access points) from which an outside system can interact with our application. Basically the communication with the outside system is done via the endpoints specified in the Controller class. The endpoint receives the data from the outside system and passes it to the service class in order to execute the business logic of the operation that the outside system demanded.

Outside System ------> [Controller -> Service -> Repository] ------> Database

## Code Example

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
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    @CrossOrigin
    public UserRequestVo getAnswersByQuestionId(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PostMapping("/user/register")
    @CrossOrigin
    public void registerNewUser(@RequestBody UserRequestVo userRequestVo) {
        userService.registerNewUser(userRequestVo);
    }

    @PostMapping("/user/authenticate")
    @CrossOrigin
    public UserTokenResponseVo login(@RequestBody UserRequestVo userRequestVo) {
        return userService.validateUserCredentialsAndGenerateToken(userRequestVo);
    }

    @PostMapping("/user/authorize")
    @CrossOrigin
    public UserAuthorizeResponseVo authorize(@RequestBody UserRequestVo userRequestVo) throws ParseException {
        return userService.authorizeV2(userRequestVo);
    }

}
```

## Security in today context

> One interesting thing in today world is that every commercial or non-commercial undertaken is running towards being a software developing entity. Almost every firm is now to some extent a software development company, pretty much all universities are now creating software for everyday activities.

> If you consider that an increasing part of today code is running in human free environment you can start imagine the criticality of small things like your washing machine working against you. What about someone hacking into an electrical power plant and messing it up for a whole city?

> A good thing is that today almost everyone is investing in anti-virus and firewall solutions. The catch is that these two types of software do not really protect applications.

### Digital security

> And, what is digital security?

> *The right tools.*
An increasing number of DevOps and developers are familiar with security but still a good percentage needs to operate with tools that they do not know inside-out. So the right tools need to be at hand, easy to use even for unfamiliar users.

> *The right feedback.*
Earliest feedback is most of the time the best feedback. You need to know as soon as possible if you are doing something wrong. While you are operate systems or develop system add tools that give you the right feedback, the immediate one. Lack of security is like any other error in the application. Is not so much different comparing to a StackOverflow or ArrayOutOfBound exceptions. Use: Slack, HipChat, JIRA, Maven, Jenkins, SIEM, PagerDuty. TODO: filter these samples;

> *The right detection*
There are multiple detection approaches these days. Some are good but they lack automation. Some are good and allow automation but are not used. How any of you have heard about SonarQube? How many used it?

> *The right protection* 
If a good protection is to hire some guys to guard your city a better protection is to invest time in learning everyone how to protect and work together with the formers. Same is for applications and is called 'runtime application self-protection' (RASP).  It uses the advantages of having access to application input and logic to block the attacks from inside by terminating user session or even shutdown the application.

> The right defense
Scanning the code for the smelling parts is a good step ahead in adding a good level of application security. A more positive approach would be to use tools the enforce security patterns while you are writing it. TODO: add samples of security patterns and tools helping this enforcement.

### Application security

> But, what is application security? Is anything and anyone that is trying to protect an application **after** it was created. See below a few approaches.

> *Static application security testing.* 
It is called white-box/white-hat testing. Is about searching for known patterns of vulnerabilities and defects in the source code and log the warnings. For these activities the researcher has *prior" knowledge about the application. Here you can choose tools like [SonarQube](https://hub.docker.com/r/owasp/sonarqube/) from OWASP.
 
> *Dynamic application security testing.*
It is also called black-box/black-hat security testing. It is about searching for vulnerabilities on a running code, on a live application. Any suspicious behavior of the application is logged. Hence for the SAST and DAST there is a pretty high volume of false positives reported. Tools that can be used are [ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project) or [Arachni](http://www.arachni-scanner.com/).

> *Interactive application security testing.* 
This is a mix of other testing techniques, including SAST and DAST. It uses the advantages of knowing the application data flows and business flows to create advance attack scenarios. The use of recursive dynamic analysis is usually combined with machine learning so the tool will get smarter while testing your application. One of its important aims is to reduce the number of false positive has the wasted time on wrong investigations. It is designed to work in Agile environments with DevOps support.

### Software security

> Software security is about creating software that is secure but itself and continue to stay like this during its entire life from development to production.

> As a software developer you are designing your software to be secure from scratch.
> As a software developer you always validate your user's input and use the right encoding for the input.

> As a software developer you handle user authentication and authorization.

> As a software developer you handle correctly user sessions.

> As a software developer you use the strongest available cryptography to secure data at rest and in transit.

> As a software developer you ensure that all third party components are validated against your entity/company policies.

> As a software developer you challenge any flaws in software design or architecture.

> As a software developer you have and improve recurrently the 'Secure coding guidelines' for developers. The entire internal development community is participating here.

> As a software developer you help your colleagues in DevOvs and Release Management to secure the configuration procedures and to securely deploy your application.

> One the most unknown field of today software development is security design patterns. Let's add a few here:
* Single access point for login in.
* Clear distinction between authentication and authorization, also known as access control.
* Always use sessions to isolate information in multi-user environment.
* Limit the view using the principle 'access is allowed per need'. Default user has very limited access, usually at login page, general policy guide, user rights page and alike.
* Work with multi-layered security. Firewalls, anti-viruses, self-protection, etc.
* Sanitize your data by removing expired, duplicate or unnecessary data.
* Design to fail in a secure manner.

## Spring Security

### What it is
> Spring Security initially started as a continuation of Acegi Security. A good tool with a bit too complicated configuration via XML. Since Sprint 2.0 it was embedded into Spring Framework and continuously improved. Nowadays the security configuration can be done via annotation directly in Java classes.

### Main features
> Spring Security helps with the two major areas of access control: authenticationa and authorization.

### Authentication
> Authentication is about ensuring that the entity/principal called in Spring accessing your protected resources is allowed to access it. First step is to identify the principal. Simplest form of identification is via username and password. Others, more sophisticated ways, are via LDAP (Lightweight Directory Access Protocol) or via CAS (Central Authentication Service) which is a single sign-on protocol.
> Authentications supported:
* HTTP Basic;
* Form-based authentication
* LDAP 
* Java Authentication and Authorization Service
* Kerberos

### Authorization
> If the authentication is successful the principal goes to the second step. Now that we know who our user is what are the actions that is allowed to perform? Which are the areas where is allowed to navigate? If you are an admin you are probably allowed to do whatever you want, if you are an user you probably has much more limited access.

> This access control is called user role authorization. Usually this is done via URL control but is not the tightest security in place. Spring introduced method-level security which means that you have to be authorized to execute that Java method. You can hack the URL and try to execute again but method level authorization will block you.



### Building blocks for Java
> Sprint Security is quite developer friendly, to start using it there is little what a developer needs to do.

>*Step A* Add the necessary libraries to your project. Using Spring Boot you just need to add **sprint-boot-starter-security** to you Maven configuration. That will take care of the related dependencies.
```xml
<dependencies>
	<!-- ... other dependency elements ... -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-security</artifactId>
	</dependency>
</dependencies>
```
>*Step B* Add the minimum configuration
```java
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        return manager;
    }
}
```
>Some important takeaways from the code above:
* Require authentication for every URL in the application.
* Generates a basic login form.
* Allow 'user' with 'password' to authenticate via form base authentication.
* Allow user to logout.
* Help with other ten plus features (cache control, prevents well know attacks).
>How Spring knows that every URL needs to be authenticated? Or how does it know to support form based authentication? All is based on the default configuration of the *WebSecurityConfigurerAdapter* via *configure* method:
```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .and()
        .httpBasic();
}
```
> The above basically says that all requests require user to be authenticated, it allows form based login and also allow basic Http authentication. The *and()* method is a helper method that basically close the section above. E.g. *fromLogin* ends with the second *and()*. Is like the closing tag of a XML tag.

>*Step C* Configure more detailed Http Security
>What if you want a different login page? It can be configured like this:
```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login") 1
            .permitAll();        2
}
``` 
>All user are authenticated via */login* page, whose access is permitted for all.

>*Step D* Configure authorization
>So for we've dealt only with authentication. Only one role was created, USER. But what if some resources are freely accessible and some other should be really kept away. This approach requires multiple roles to be created and configured for different resource types using multiple children of *http.authorizeRequests()*:
```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .antMatchers("/resources/**", "/signup", "/about").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
            .anyRequest().authenticated()
            .and()
        // ...
        .formLogin();
}
```
>Using the code above:
* All URLs under */resources* and URLs equal with */signup* and equal to */about* are free to access.
* All URLs under */admin* require the user to have an *ADMIN* role.

* All URLs under */db* require the user to have both *ADMIN* and *DBA* roles.

* All others require user to be authenticated via form login.


>*Step E* Deal with custom logout
> A few important things happen once the user is logged out. These are default actions built in Spring.
* Invalidate the HTTP session.
* Clear the SecurityContextHolder.
* Clean up RememberMe authentication is it was configured.
* Redirect to /login?logout.

>If custom changes are needed they can be added, in the same method as above, like this:
```java
    http
        .logout()
            .logoutUrl("/my/logout")
            .logoutSuccessUrl("/my/index")
            .logoutSuccessHandler(logoutSuccessHandler)
            .addLogoutHandler(logoutHandler)
            .deleteCookies(cookieNamesToClear)
            .and()
        ...
}
```
>These changes add, starting with *logoutUrl* line above: 
* a custom logout page, 
* a custom logout redirect page, 
* a new logout handler on successful logout, 
* a custom general logout handler,
* deletes user named cookies. 


## JWT approach

### Overview
>A JSON Web Token (JWT) is a JSON object used to exchange information between parties. The exchange is done in a save way and it contains:
* Header:
	```json
	{
		"typ": "JWT",
		"alg": "HS256"
	}
	```
* Payload:
	```json
	{
		"userId": "b08f86af-35da-48f2-8fab-cef3904660bd"
	}
	```
* Signature:
	```json
	-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM
	```

### Creation and usage
>*How to create a ready-to-transport header*
>> The header contains information about how JWT signature should be computed. The "alg" key in our example says that HMAC-SHA256 algorithm should be used to create the signature. Before being transported header is Base64Url encoded.

>*How to create a ready-to-transport payload*
>> The data included in payload is also known as 'claims' of the JWT. There are a few standard but to mandatory like 'iss', 'sub' and 'exp'. Before being transported the payload is Base64Url encoded.

>*How to create a ready-to-transport signature*
>>To create the signature take the encoded header and the encoded payload and concatenate them via a dot. Then take the secret and sign the former concatenation using the algorithm specified in the header. Encode the result from the algorithm also using Base64Url encoding and here it is your signature.

>*What is sent across?*
>>All above are concatenated in the form of *header.payload.signature*:
```json
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiMDhmODZhZi0zNWRhLTQ4ZjItOGZhYi1jZWYzOTA0NjYwYmQifQ.-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM
```
>>You may notice that the header and the payload are concatenated to create the signature but are sent as distinct entities.


## SpringBoot

Spring Boot is an open source Java-based framework used to create Micro Services.
It is easy to create a stand-alone and production ready spring application.


The Spring Boot Maven plugin provides many convenient features:

1) It collects all the jars on the classpath and builds a single, runnable jar, which makes it more convenient to execute and transport your service.
2) searches for the public static void main() method to flag as a runnable class.
3) It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. 
You can override any version you wish, but it will default to Boot’s chosen set of versions.


## Annotations



@SpringBootApplication:

1) @Configuration tags the class as a source of bean definitions for the application context.

2) @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.

Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. 

This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.(receives requests and maps to the coresponding classes)


3) @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.


The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.






### References

* [cgisecurity.com](cgisecurity.com)
* [contrastsecurity.com](contrastsecurity.com)
* [developer.com](developer.com)
* [ietf.org](https://tools.ietf.org/html/rfc7519)
* [jwt.io](jwt.io)
* [martinfowler.com](martinfowler.com)
* [owasp.org](wasp.org)
* [spring.io](spring.io)
* [stackoverflow.com](https://stackoverflow.com/questions/201479/what-is-base-64-encoding-used-for/201510#201510)
* [synopsys.com](synopsys.com)
* [wikipedia.org](wikipedia.org)
