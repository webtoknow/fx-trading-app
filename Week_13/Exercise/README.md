# Week 13 - Continous deployment exercises

## Table of contents

- [Exercise 1 - Write yout first Dockerfile](#exercise-1---write-your-first-dockerfile)
- [Exercise 2 - Write your first docker compose template](#exercise-2---write-your-first-docker-compose-template)
- [Exercise 3 - Write your Ansible playbook in order to deploy all 4 microservices](#exercise-3---write-your-ansible-playbook-in-order-to-deploy-the-application)
- [Exercise 4 - Create Jenkins job in order to deploy the application](#exercise-4---create-jenkins-job-in-order-to-deploy-the-application)
### Exercise 1 - Write your first Dockerfile

```
FROM maven:3.6.2-jdk-11-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM openjdk:11.0.4-jre
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/user-administration-0.0.1-SNAPSHOT.jar /user-admin.jar
CMD /usr/local/openjdk-11/bin/java -Daplication-secret=secret -jar /user-admin.jar
```

### Exercise 2 - Write your first docker compose template

```
version: "3"
services:
  ui:
    build: ./ui
    ports: 
      -  "80:80"
  users:
    build: ./user-administration
    ports: 
      -  "8200:8200"
  trading: 
    build: ./fx-trading
    ports:
      - "8210:8210"
  quote:
    build: ./quote-service
    ports:
      - "8220:8220"
  db:
    image: postgres
    environment:
        POSTGRES_PASSWORD: "admin"
    volumes:
        - postgres-db:/var/lib/postgresql/data
        - ./fx-trading/database_setup/db_setup.sql:/docker-entrypoint-initdb.d/1.sql
        - ./user-administration/database_setup/db_setup.sql:/docker-entrypoint-initdb.d/2.sql

    ports:
      - "54320:5432"
volumes:
    postgres-db:
```

### Exercise 3 - Write your Ansible playbook in order to deploy the application

```
---
- hosts: test 
  become: yes 
  tasks:
  - name: Install docker packages
    yum:
      name:
        - docker-ce
        - docker-compose
        - npm
        - maven
      state: present 
  - name: Ensure SELinux is disabled after reboot
    lineinfile:
      path: /etc/selinux/config
      regexp: '^SELINUX='
      line: SELINUX=disabled
    notify: 
    - Disable SELinux
  - name: Ensure docker is running
    service:
      name: docker
      state: started
  - name: Copy application files to target machine
    copy:
      src: "{{ workspace }}/App"
      dest: /tmp
  - name: Build & start Docker containers with Docker compose
    shell: "cd /tmp/App && docker-compose up -d"
  handlers:
    - name: Disable SELinux
      shell: "setenforce 0"

```
### Exercise 4 - Create Jenkins job in order to deploy the application

- Go to Jenkins Home Page and login with
  - user: student
  - password: student
- Click on 'New Item'
- Name your project
- Select 'Freestyle Project'
- Click Next
- On 'Build' phase click on 'Add build step'
- Select 'Invoke Ansible Playbook'
- Add the details neccesary to your playbook
- Click Save
- Run Build job using Build Now button
