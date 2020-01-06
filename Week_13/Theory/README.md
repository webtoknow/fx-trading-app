# Week 13 - Devops Part 2

## Table of contents

- [Overview of Virtual Machine](#overview-of-virtual-machine)
- [Overview of Container](#overview-of-a-container)
- [Differences between Virtual Machines and Containers](#differences-between-virtual-machines-and-containers)

## Overview of Virtual Machine

### What is a Virtual Machine?

  Virtual machines are software computers that provide the same functionality as physical computers. Like physical computers, they run applications and an operating system. However, virtual machines are computer files that run on a physical computer and behave like a physical computer. 
  In other words, virtual machines behave as separate computer systems.

### Usage of Virtual Machine
  
  Virtual machines are created to perform specific tasks that are risky to perform in a host environment, such as accessing virus-infected data and testing operating systems. Since the virtual machine is sandboxed from the rest of the system, the software inside the virtual machine cannot tamper with the host computer. Virtual machines can also be used for other purposes such as server virtualization.

### Advantages of Virtual Machines:

- Provides disaster recovery and application provisioning options
- Virtual machines are simply managed, maintained, and are widely available
- Multiple operating system environments can be run on a single physical computer

### Disadvantages of Virtual Machines:

- Running multiple virtual machines on one physical machine can cause unstable performance
- Virtual machines are less efficient and run slower than a physical computer

## Overview of a Container

### What is a Container

A container is a standard unit of software that packages up code and all its dependencies so the application runs quickly and reliably from one computing environment to another.

### Usage of a Container

Containers are an abstraction at the app layer that packages code and dependencies together. Multiple containers can run on the same machine and share the OS kernel with other containers, each running as isolated processes in user space. Containers take up less space than VMs (container images are typically tens of MBs in size), can handle more applications and require fewer VMs and Operating systems.

### Advantages of a Container

- Enables more efficient use of system resources
- Enables faster software delivery cycles
- Enables application portability
- Shines for microservices architecture

### Disadvantages of Container:

- Containers don't run at bare-metal speeds
- The container ecosystem is fractured. Its based on companies developments. Example: Openshift RedHat's container-as-a-service platform works only with Kubernetes container orchestrator.
- Persistent data storage is complicated
- Graphical applications don't work well
- Not all applications benefit from containers

## Differences between Virtual Machines and Containers

![devops](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_13/Theory/images/devops.PNG)

## What is DevOps?

Let's watch on Youtube [What is DevOps? - In Simple English](https://www.youtube.com/watch?v=_I94-tJlovg).
