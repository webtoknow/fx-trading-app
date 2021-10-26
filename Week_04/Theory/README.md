# Week 4 - Architecture
## Architecture, architecting, architects
What is [Architecture, architecting, architects](https://docs.google.com/presentation/d/1JRixGmGy8YTbxeMdWNeGx1bSle4PlmX5/edit?usp=sharing&ouid=112452627469857234057&rtpof=true&sd=true)?

[System Design Architecture](https://docs.google.com/presentation/d/12orbmuOojmRNdVnkvvvetLITSDVDzWA9/edit?usp=sharing&ouid=112452627469857234057&rtpof=true&sd=true)

## What is a Single Page Application?

- is an app that works inside a browser
- does not require page reloading during use
- most resources (HTML + CSS + scripts) are only loaded throughout the lifespan of application
- only data is transmitted back and forth
- no need to write code to render pages on the server
- easy to debug with Chrome - you can monitor network operations, investigate page elements and data associated with it
- can use Browser's local storage
- we are using this type of applications every day
- *e.g. : Gmail, Google Maps, GitHub*

## Angular

### About

- is a comprehensive JavaScript framework, used by developers to build web applications
- is built entirely in **TypeScript** which contains the ES6 JavaScript features, has tooling and supports type safety
- TypeScript must be 'transpiled' into JavaScript using the *tsc* compiler
- enable to write SPA (Single Page Applications) with ease
- comes with features like data-binding, change-detection, forms, router&navigation and http implementation

### ES6 and TypeScript

- ES6 is the 6th version of the ECMA Script Programming Language, released in 2011
- adds many features intended to make large-scale software developement easier, including:
    - use of **const** and **let** instead of var
    - block-scoped variables and functions
    - arrow functions
    - default function parameters
    - class definition syntax
- TypeScript was created to statically identify constructs that are likely to be errors -> allow to make safe assumptions about state during execution

### Angular CLI

- is used by the new generation of angular projects
- every time we start a new project, there is a bunch of files to be created -> Angular CLI can resolve that issue by generating all that files for us
- when it is done, we have a basic Angular project, runnable and containing all we need to continue
- after starting the application, a new port is opened, where a live server continues to run using **Webpack's dev server**

### Webpack

- is a popular module bundler
- bundles application source code in convenient chunks
- loads that code from a server to browser
- takes in various assets (such as JavaScript, CSS and HTML) and then transforms these assets into a format that's convenient to consume through a browser
- based on a configure-driven approach and takes all assets in the project as dependencies -> treats the complete project as a dependency graph
- we can separate the source code into multiple files and import those files into the application to use the functionality contained in them
- module bundlers were built to bring this capability in a couple forms: 
    - by asynchronously loading modules and running them when they have finished loading
    - by combining all of the necessary files into a single JavaScript file that would be loaded via a **script** tag in the HTML.
    
## Spring

### Spring Framework
Spring is a framework for the Java platform comprising many modules. Among them are Spring Core, Spring Security and Spring Data.  

- Central to the framework is Spring Core which comes with the IoC container.  
IoC stands for Inversion of Control and is a software design principle. A related design pattern is Dependency Injection.  
With the help of the IoC container, applications using Spring can be decoupled from object dependencies(**beans**) instantiation.  
Application code will declare and use beans and Spring Core will take care of their instantiation - with the help of xml or java(annotations or code) configuration.  
- uses Aspect Oriented Programming  
- provides bean life cycle and scopes

Example of benefits:   
-  allows for more modular and loosely coupled code
-  allows for easier testing
-  when we need to replace a concrete implementation with another one, our application code should not need to change.

### Spring Boot

Spring Boot is a project which helps with faster development speed.  
It favors convention over configuration: you specify which Spring dependencies to use in the project and Spring Boot gives reasonable configuration defaults for them.

## PostgreSQL
- object relational database
- open source
- supports many data types including json, geometrical and arrays
- supports user defined data types
- high SQL standard compliance

