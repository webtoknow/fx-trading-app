
# Week 7 - Create User-Administration Microservice With Spring

## Table of contents

- [SpringBoot](#spring-boot)
  - [About](#about)
  - [Annotations](#annotations)
  - [Code Example](#code-example)
- [Jpa](#Jpa)
  - [Jpa Introduction](#jpa-introduction)
  - [Jpa Entity](#jpa-entity)
  - [Spring Data](#spring-data)



## About

Spring Boot is an open source Java-based framework used to create a Micro Services.
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

This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.(primeste request si mapeaza la clasele potrivite)


3) @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.


The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.


There is also a CommandLineRunner method marked as a @Bean and this runs on start up. 
It retrieves all the beans that were created either by your app or were automatically added thanks to Spring Boot. 



## Code Example


```Java
package hello;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);

            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}
```


```Java
package hello.controllers;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```


## Jpa Introduction

### What?
Java Persistence API is a collection of classes and methods to persistently store the vast amounts of data into a database which is provided by the Oracle Corporation.

### Why?
To reduce the burden of writing codes for relational object management, a programmer follows the ‘JPA Provider’ framework, which allows easy interaction with database instance. Here the required framework is taken over by JPA.

### Who?
JPA is an open source API, therefore various enterprise vendors such as Oracle, Redhat, Eclipse, etc. provide new products by adding the JPA persistence flavor in them. Some of these products include:
Hibernate, Eclipselink, Toplink, Spring Data JPA, etc.


### Jpa Entity


```Java

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = -3009157732242241606L;
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "age")
	private int age;

	protected Employee() {
	}

	public Employee(String firstName, String lastName,int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLast_Name(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}

```

### Spring Data

This spring-module provides spring data repository interfaces which are implemented to create JPA repositories.

#### No More DAO Implementations

Spring data provides a base class JpaRepository that helps us from writting the following methods:

- findAll
- count, delete, deleteAll, deleteAll, deleteById, existsById, findById, save
- exists, findOne
- deleteAllInBatch()
- deleteInBatch(Iterable<T> entities)
  
  
The repository interface is used for extending the CRUD interface. This interface adds the layer of a repository in the program. Spring Data JPA provides two major ways of creating queries. These queries are then used in the repository interface to fetch the data from the database.



```Java

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	List findByLastName(String lastName);
	
@Query("SELECT e FROM Employee e WHERE e.age = :age")
    public List findByAge(@Param("age") int age);
}


```
