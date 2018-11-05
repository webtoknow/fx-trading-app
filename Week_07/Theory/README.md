
# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [SpringBoot](#spring-boot)
  - [About](#about)
  - [Annotations](#annotations)
  - [Example](#example)
- [Jpa](#Jpa)
  - [Jpa Introduction](#jpa-introduction)



#### About

Spring Boot is an open source Java-based framework used to create a Micro Services.
It is easy to create a stand-alone and production ready spring application.



The Spring Boot Maven plugin provides many convenient features:

1) It collects all the jars on the classpath and builds a single, runnable jar, which makes it more convenient to execute and transport your service.
2) searches for the public static void main() method to flag as a runnable class.
3) It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. 
You can override any version you wish, but it will default to Boot’s chosen set of versions.


### Annotations



@SpringBootApplication:

1) @Configuration tags the class as a source of bean definitions for the application context.

2) @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.

Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. 

This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.(primeste request si mapeaza la clasele potrivite)


3) @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.


The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.


There is also a CommandLineRunner method marked as a @Bean and this runs on start up. 
It retrieves all the beans that were created either by your app or were automatically added thanks to Spring Boot. 
