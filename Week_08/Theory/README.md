# Week 8 - Development : Dependency Injection and a microservice - quote service
### Table of contents

- [Spring](#spring)
  - [About](#about)
  - [Components and Services](#components)
  - [Inversion of Control](#inversion-of-control)
  - [Dependency Injection](#dependency-injection)
  - [Code Example](#code-example)
- [Microservices](@microservices)
- [References](#references)

## About

Basically Spring is a framework for dependency-injection which is a pattern that allows to build very decoupled systems.
Spring supports segregation of service layer, web layer and business layer, but what it really does best is "injection" of objects.
One of the entertaining things about the enterprise Java world is the huge amount of activity in building alternatives to 
the mainstream J2EE technologies, much of it happening in open source. 
A lot of this is a reaction to the heavyweight complexity in the mainstream J2EE world, but much of it is also exploring alternatives 
and coming up with creative ideas. A common issue to deal with is how to wire together different elements: 
how do you fit together this web controller architecture with that database interface backing when they were built by different 
teams with little knowledge of each other. A number of frameworks have taken a stab at this problem, 
and several are branching out to provide a general capability to assemble components from different layers. 


## Components

The topic of wiring elements together drags me almost immediately into the knotty terminology problems that surround the terms service and component. You find long and contradictory articles on the definition of these things with ease. For my purposes here are my current uses of these overloaded terms.

A components is a glob of software that's intended to be used, without change, by an application that is out of the control of the writers of the component. By 'without change' I mean that the using application doesn't change the source code of the components, although they may alter the component's behavior by extending it in ways allowed by the component writers.

A service is similar to a component in that it's used by foreign applications. The main difference is that I expect a component to be used locally (think jar file, assembly, dll, or a source import).
A service will be used remotely through some remote interface, either synchronous or asynchronous (eg web service, messaging system, RPC, or socket.)



## Inversion of Control

Inversion of Control is a principle in software engineering by which the control of objects or portions of a program is transferred to a container or framework. It’s most often used in the context of object-oriented programming.

By contrast with traditional programming, in which our custom code makes calls to a library, IoC enables a framework to take control of the flow of a program and make calls to our custom code. To enable this, frameworks use abstractions with additional behavior built in. If we want to add our own behavior, we need to extend the classes of the framework or plugin our own classes.

The advantages of this architecture are:

decoupling the execution of a task from its implementation
making it easier to switch between different implementations
greater modularity of a program
greater ease in testing a program by isolating a component or mocking its dependencies and allowing components to communicate through contracts
Inversion of Control can be achieved through various mechanisms such as: Strategy design pattern, Service Locator pattern, Factory pattern, and Dependency Injection (DI).

## Dependency Injection

Dependency injection is a pattern through which to implement IoC, where the control being inverted is the setting of object’s dependencies.

The act of connecting objects with other objects, or “injecting” objects into other objects, is done by an assembler rather than by the objects themselves.

Here’s how you would create an object dependency in traditional programming:



## Code Example


```Java
package hello;

public class Box {
    private Item item;
  
    public Box() {
        item = new ItemImpl1();    
    }
}
```

In the example above, we need to instantiate an implementation of the Item interface within the Box class itself.

By using DI, we can rewrite the example without specifying the implementation of Item that we want:


```Java
package hello;

public class Box {
    private Item item;
    public Box(Item item) {
        this.item = item;
    }
}
```

```Java
package hello;

public class Box {
    private Item item;
    
    public setItem(Item item) {
        this.item = item;
    }
}
```



In case of Field-Based DI, we can inject the dependencies by marking them with an @Autowired annotation:

```Java
package hello;


public class Box {
    @Autowired
    private Item item; 
}

```


While constructing the Box object, if there’s no constructor or setter method to inject the Item bean, the container will use reflection to inject Item into Box.

We can also achieve this using XML configuration.

-- it has a few drawbacks such as:

This method uses reflection to inject the dependencies, which is costlier than constructor-based or setter-based injection
It’s really easy to keep adding multiple dependencies using this approach. If you were using constructor injection having multiple arguments would have made us think that the class does more than one thing which can violate the Single Responsibility Principle.


#Microservices

There are many benefits to microservices. Because of their isolation and strict requirement to communicate through well-defined interfaces, microservices prevent quick and dirty solutions often found in monoliths. These hacks inside of a monolith result in a loss of cohesion and an increase in coupling --  two primary causes of complexity.


Complexity comes from low cohesion and high coupling. Microservices provide the structure to keep that at bay.

 degree to which the elements inside a module belong together
 degree of interdependence between software modules
 
 Which is which?

#References

https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring

https://martinfowler.com/articles/injection.html