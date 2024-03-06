# Forex Trading Application workshop

During this workshop, we will learn about the whole process and all concepts involved in building an enterprise application from scratch. Starting with an idea and some wire frames, together we will analyze, develop, test and publish our app.

Each week will be an opportunity to understand theoretical concepts and apply them in punctual exercises. In the end, we will obtain a functional Forex trading application meant to help us to buy and sell currencies and also to view all transactions made by other users.

## The agenda

The agenda has the following content:

- Week 1 – Introduction
- Week 2 – Analysis
  - [Introduction](https://drive.google.com/file/d/1ueFpjdAEXlskXxM4ao2qIN9ow5X18fVz/view?usp=sharing)

    Discovering the vital roles of analysts, their interactions with team members, and engaging activities in functional analysis.

  - [Exercise](Week_02/Exercise/README.md)

    Practice becoming a business analyst by crafting user stories for various pages on the FX-trading-app.

- Week 3 - Design
  - [Introduction](https://drive.google.com/file/d/1iNmU9-0qMfgE44g3IGIiHWLj63JHG6ep/view?usp=sharing)

    Explore key design principles like Proximity, Hierarchy, and Contrast in an interactive workshop. Enhance your design thinking skills through practical applications.

  - [Exercise](Week_03/Exercise/README.md)

    Develop your skills as a UX designer by creating mockups for different pages within the FX-trading-app.

- Week 4 – Architecture and setup
  - [Introduction](Week_04/Theory/README.md)

    Explore essential concepts in web development such as system design architecture, Single Page Applications (SPAs), Angular, ES6, and TypeScript. Dive into tools like Angular CLI, Webpack, Spring, Spring Boot, and PostgreSQL. Understand the rationale behind selecting these technologies for developing the FX trading app.

  - [Exercise](Week_04/Exercise/README.md)

    Establish the configuration for the FX trading app using Angular and Spring.

- Week 5 – Create Login and Register pages with Angular
  - [Introduction](Week_05/Theory/README.md)

    Explore Angular and delve into its core functionalities, including components, data binding, pipes, and the basics of directives. Gain insights into declaring components, utilizing data binding, leveraging built-in pipes, and crafting custom pipes through practical examples.

  - [Exercise](Week_05/Exercise/README.md)

    The exercises entail configuring a mock server and implementing login and register functionalities using Angular, alongside UI enhancements. They involve setting up a JSON Server, creating application pages, styling, and integrating additional components to improve the user experience.

- Week 6 – Create Dashboard page with Angular
  - [Introduction](Week_06/Theory/README.md)

    Continue to explore Angular, with a particular focus on Observables and Forms and Validations. Dive deeper into the concept of Observables, elucidating their benefits and demonstrating their utilization in event handling and asynchronous programming. Additionally, delve into the two types of forms in Angular: Reactive Forms and Template-driven Forms, highlighting their notable differences.

  - [Exercise](Week_06/Exercise/README.md)

    The exercises involve constructing a Dashboard page using Angular for a FX trading app, comprising two sections: FX Rates View and Blotter View. FX Rates View showcases currency rates, and Blotter View presents transaction details. Conclude by incorporating a navbar featuring a logo and Logout button onto the Dashboard page.

- Week 7 – User-Administration microservice
  - [Introduction](Week_07/Theory/README.md)

    Delve into an overview of Spring Boot development alongside software security practices, underscoring the critical need for secure software in the current digital era. The discussion encompasses pivotal topics like Spring Data facilitating database interaction, Spring Security ensuring authentication and authorization, and the employment of JSON Web Tokens (JWT) for secure data exchange. Each subject is supplemented with code illustrations and detailed elucidations, underscoring the vital role of secure coding methodologies and the seamless integration of security protocols across the software development lifecycle.

  - [Exercise](Week_07/Exercise/README.md)

    Throughout these exercises, you constructed a user administration microservice leveraging Spring Boot. You established a PostgreSQL database and outlined entities, repositories, services, and controllers to administer users. Your implementation encompassed functionalities for user registration, JWT token-based authentication, and authorization. With each exercise, you extended the microservice's capabilities, culminating in a robust solution adept at managing user-related tasks.

- Week 8 – Quote microservice
  - [Introduction](Week_08/Theory/README.md)

    Explore the principles of Inversion of Control (IoC) and Dependency Injection (DI) within the context of the Spring framework. Understand how DI enables IoC by separating dependencies, demonstrated through practical code samples. Moreover, discover the advantages of transitioning to a microservices architecture, underscoring the enhancements in cohesion and reduction in coupling it offers.

  - [Exercise](Week_08/Exercise/README.md)

    Throughout these exercises, we built a quote service microservice with Spring. We set up the project, defined currency enums and rate DTOs, and created controllers for fetching available currencies and FX rates. In the service layer, we implemented logic for generating FX rates and included integration tests. This service provides accurate FX rates for currency pairs, supporting our FX trading application.

- Week 9 – Fxtrading microservice
  - [Introduction](Week_09/Theory/README.md)

    Explore the fundamentals of HTTP and REST to understand web service interactions. HTTP methods like GET, POST, PUT, and DELETE facilitate actions on resources, while status codes convey request outcomes. REST principles align CRUD operations with HTTP methods for resource management. In Spring, REST controllers streamline API development, mapping endpoints to methods and handling responses. Embrace these concepts to build efficient and scalable web services.

  - [Exercise](Week_09/Exercise/README.md)

    Construct the final microservice for the FX trading app, accomplishing tasks such as initializing the project, configuring the database, creating REST endpoints for trade listing and creation, securing the API with OAuth2, and performing essential unit tests for key functionalities.
  
- Week 10 – Testing
  - [Introduction](https://docs.google.com/presentation/d/0B4NKACt0AT2MOEZNT0d1VEtRNWZDSHN6WUVnUTJya1BVeGUw/edit?usp=sharing&ouid=112452627469857234057&resourcekey=0-hABd5QFgst8a4iBNecvcuw&rtpof=true&sd=true)

    Explore the essence of software testing, crucial for impeccable software. It defines testing as evaluating products to meet requirements, stressing its role in error detection, client satisfaction, and quality assurance. It surveys development models like V-Model and Agile, highlighting testing approaches. Additionally, it outlines testing levels (Component, Integration, System, Acceptance) and types (Functional, Non-Functional, Structural), offering a concise understanding of testing fundamentals.

  - [Exercise](Week_10/Exercise/README.md)

    By solving these exercises, you'll delve into Equivalence Partitioning, Boundary Analysis, and Pairwise Testing techniques, addressing challenges such as ticket booking limits, password length constraints, car trading scenarios, and application interface elements.

- Week 11 – Testing
  - [Introduction](Week_11/Theory/README.md)

    Explore the fundamental aspects of automation testing, delving into its significance in handling repetitive tasks like regression and sanity testing efficiently. Understand the criteria for deciding what to automate, including programmable interactions, human-independent processes, and repetitive actions. Navigate through common challenges, such as dealing with dynamic UI elements and ensuring test robustness. Discover essential tools and integrations, like Selenium and CI/CD systems, vital for seamless automation implementation.

  - [Exercise](Week_11/Exercise/README.md)

    The exercises focus on familiarizing testers with locating web elements using different strategies such as ID, Name, TagName, ClassName, LinkText, PartialLinkText, CSS, and XPath. They involve creating test cases and inserting them into the test suite, as well as developing methods for interacting with elements on the "Register New Account" page within the framework. Additionally, testers are tasked with implementing methods to verify validation messages like "username required" or "password minimum characters" using the framework's capabilities.

- Week 12 – Devops
  - [Introduction](Week_12/Theory/README.md)

    Exploring DevOps involves understanding its principles and practices, encompassing its definition, advantages, core methodologies, Linux commands, virtual machines, containers, container orchestration, and cloud computing. Collaboration is underscored, essential practices are spotlighted, and the technological support for DevOps principles is discussed.

  - [Exercise](Week_12/Exercise/README.md)

    Learn to deploy applications on Google Cloud Platform with exercises covering VM, Docker, and serverless deployments.

- Week 13 – Devops
  - [Introduction](Week_13/Theory/README.md)

    Eplore Terraform along with other key DevOps automation tools such as Ansible and Jenkins. Discover Terraform's versatility across multiple clouds, Ansible's straightforward SSH-based operation, and Jenkins's powerful pipeline functionality. The comparison underscores Terraform's focus on provisioning and declarative nature, contrasting with Ansible's configuration management and procedural approach. Together, these tools streamline software development, deployment, and infrastructure management in contemporary DevOps methodologies.

  - [Exercise](Week_13/Exercise/README.md)

    The exercises provide a holistic introduction to using Terraform for managing Google Cloud resources effectively. Starting with basic operations like creation, updating, and deletion, users progress to advanced topics such as variable management and dependencies. Furthermore, the integration of Ansible and Jenkins enhances automation capabilities, enabling streamlined infrastructure setup and application deployment processes.

## Technical requirements

We just need:

- [Modern browser](https://browsehappy.com/)
- IDE:
  - [Idea](https://www.jetbrains.com/idea/download/)
  - [Visual Studio Code](https://code.visualstudio.com/Download)
  - [Eclipse](https://www.eclipse.org/downloads/packages/)
- [Git](https://git-scm.com/download/win)
- [NodeJS](https://nodejs.org/en/)
- [Angular CLI](https://github.com/angular/angular-cli)
- [Maven](https://maven.apache.org/install.html)
- [Postman](https://www.getpostman.com/apps)
- [Postgresql](https://www.postgresql.org/download/)
- Java 11:  use either [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [openjdk 11](https://jdk.java.net/java-se-ri/11)

## Summary
[![How can I develop a web applicaton](https://img.youtube.com/vi/_hJXIVPQhDo/0.jpg)](https://www.youtube.com/watch?v=_hJXIVPQhDo)
