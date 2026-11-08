# Banking Software Design

During this course, we will learn about the whole process and all concepts involved in building an enterprise application from scratch. Starting with an idea and some wireframes, together we will analyze, develop, test and publish our application.

Each week will be an opportunity to understand theoretical concepts and apply them in punctual exercises. In the end, we will obtain a functional Forex trading application with capabilities to buy and sell currencies, and view all transactions made by other users.

## Course Structure

- Introduction
- Business Analysis
  - [Theory](01_Business_Analysis/Theory/README.md)
  - [Exercise](01_Business_Analysis/Exercise/README.md)
- UI/UX Design
  - [Theory](02_UI_UX_Design/Theory/README.md)
  - [Exercise](02_UI_UX_Design/Exercise/README.md)
- Architecture
  - [Theory](03_Architecture/Theory/README.md)
  - [Exercise](03_Architecture/Exercise/README.md)
- Quote and FX Trading Microservices with Spring
  - [Theory](04_Backend_Development/Theory/README.md)
  - [Exercise](04_Backend_Development/Exercise/README.md)
- User-Administration Microservice with Spring
  - [Theory](05_Backend_Development/Theory/README.md)
  - [Exercise](05_Backend_Development/Exercise/README.md)
- Login and Register Pages with Angular
  - [Theory](06_Frontend_Development/Theory/README.md)
  - [Exercise](06_Frontend_Development/Exercise/README.md)
- Dashboard Page with Angular
  - [Theory](07_Frontend_Development/Theory/README.md)
  - [Exercise](07_Frontend_Development/Exercise/README.md)
- Testing Introduction
  - [Theory](08_Software_Testing/Theory/README.md)
  - [Exercise](08_Software_Testing/Exercise/README.md)
- Testing Automation
  - [Theory](09_Software_Testing/Theory/README.md)
  - [Exercise](09_Software_Testing/Exercise/README.md)
- DevOps Introduction
  - [Theory](10_DevOps/Theory/README.md)
  - [Exercise](10_DevOps/Exercise/README.md)
- DevOps Automation
  - [Theory](11_DevOps/Theory/README.md)
  - [Exercise](11_DevOps/Exercise/README.md)


## How to use this repository

1. Clone the repository: `git clone https://github.com/webtoknow/fx-trading-app.git`. The paths in the laboratories assume the folder is named `fx-trading-app`, so do not rename it.
2. Do the laboratories in order, from 01 to 11.
3. Each laboratory has a `Theory` folder (read it first) and an `Exercise` folder (do it during the session).
4. Laboratories 04, 05, 06 and 07 build one application step by step:
   - 04 creates the quote and FX trading microservices;
   - 05 edits the 04 project to add the user-administration microservice;
   - 06 creates the Angular login and register pages;
   - 07 continues from 06 with the dashboard page.
5. `App/` is the complete application, used by laboratories 08-11. Run it from the `App/` folder with `docker compose up --build` (needs Docker), then open http://localhost/login.
6. Inside an `Exercise` folder, `Code` is the starting point of the laboratory and `Solution` is its finished state. Compare with a `Solution` only after you have tried the exercise yourself.

For laboratories 06 and 07, see [Local frontend setup](FRONTEND_LABS.md) for running the solutions with mocks or the Java backend, automated checks, and the final browser checklist.

## Technical Requirements

| Software/Tools                                                                                                        | Version          | Usage                          | Requirement for session                                            |
|-----------------------------------------------------------------------------------------------------------------------|------------------|--------------------------------|--------------------------------------------------------------------|
| [PostgreSQL](https://www.postgresql.org/download/)                                                                    | 17 or latest     | Relational Database            | Laboratories [04](04_Backend_Development/Exercise/README.md), [05](05_Backend_Development/Exercise/README.md), [09](09_Software_Testing/Exercise/README.md) |
| [pgAdmin](https://www.pgadmin.org/download/)                                                                                                             | 4 or latest      | Database Administration        | [Backend Development](04_Backend_Development/Exercise/README.md)   |
| [Git](https://git-scm.com/download/win)                                                                               | 2 or latest      | Version Control System         | [Backend Development](04_Backend_Development/Exercise/README.md)   |
| [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) or any other IDE (Visual Studio, Eclipse, etc.)             | 2025.2 or latest | Backend & Frontend Development | [Backend Development](04_Backend_Development/Exercise/README.md)   |
| [Maven](https://maven.apache.org/install.html)                                                                        | 3 or latest      | Backend Dependency Management  | [Backend Development](04_Backend_Development/Exercise/README.md)   |
| [JDK](https://www.oracle.com/ro/java/technologies/downloads/#java21) or [OpenJDK](https://jdk.java.net/java-se-ri/21) | 21               | Backend Development            | Laboratories [04](04_Backend_Development/Exercise/README.md), [05](05_Backend_Development/Exercise/README.md), [09](09_Software_Testing/Exercise/README.md) |
| [Postman](https://www.getpostman.com/apps)                                                                            | 9 or latest      | Backend APIs Testing           | [Backend Development](04_Backend_Development/Exercise/README.md)   |
| [NodeJS](https://nodejs.org/en/)                                                                                      | 22.12+ or 24     | Frontend Runtime Environment   | [Frontend Development](06_Frontend_Development/Exercise/README.md) |
| [Angular CLI](https://github.com/angular/angular-cli)                                                                 | 21               | Frontend Development           | [Frontend Development](06_Frontend_Development/Exercise/README.md) |
| [Google Cloud Account](https://cloud.google.com/edu/students?hl=ro)                                                   | n/a              | Backend & Frontend Deployment  | [DevOps Introduction](10_DevOps/Exercise/README.md)                |
| [Docker Desktop](https://www.docker.com/products/docker-desktop/) | latest | Run the complete application | Laboratories [08](08_Software_Testing/Exercise/README.md) to [11](11_DevOps/Exercise/README.md) |
| [Google Chrome](https://www.google.com/chrome/) | latest | Browser for test automation | Laboratory [09](09_Software_Testing/Exercise/README.md) |
