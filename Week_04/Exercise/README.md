# Week 4 - Architecture and setup

## Setup Angular

Install [Angular CLI](https://cli.angular.io/)

````bash
npm install -g @angular/cli
````

Got to *Week_04\Exercise\Code*

````bash
cd fx-trading-app\Week_04\Exercise\Code
````

Let's generate a new Angular project using CLI

````bash
ng new ui
````

Start the project

````bash
cd ui
ng serve
````

## Setup Spring Boot App and database

I. Generate a Spring Boot starter project with Maven as build tool  

Open https://start.spring.io/ in the browser.  

Select: 1) Generate a Maven Project 2) with Java 3) and Spring Boot 2.0.5(or newer and stable if this one becomes unavailable)  
4) Complete Group section. Example: <em>com.banking.sofware.design</em>   
5) For Artifact choose a name like <em>fxtrading</em>  
6) Click on the link having text 'Switch to the full version.'  
7) Select Java version. Example: 10  
8) Select the following modules: Security, Web, JPA, PostgreSQL  
9) Generate project  



II. Create database  

In pgAdmin console:  

1. Create a database with name <em>fxtrading</em>  
2. Open a Query Tool in the newly created database and  
a) create a user that the application will use to connect  
b) give the newly created user access rights  
Example:  
```
CREATE USER fxowner WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE fxtrading TO fxowner;
```

III. Import project in IDE and set properties:  

In <em>src/main/resources/application.properties</em> configure database connection.   
Use database and user created at previous step  
```
spring.datasource.url=jdbc:postgresql://localhost:5432/fxtrading  
spring.datasource.username=fxowner  
spring.datasource.password=password 
```

After doing this, the application should start when running the main class: ```<ArtifactName>Application```

