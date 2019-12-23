# Week 12 - Devops

## Table of contents

- [DevOps Automation – Time to market by commit with open source tools](#devops-automation-–-time-to-market-by-commit-with-open-source-tools)
- [Overview of Jenkins](#overview-of-jenkins)
- [Overview of Ansible](#overview-of-ansible)
- [What is DevOps?](#what-is-devops?)

## DevOps Automation – Time to market by commit with open source tools

### DevOps Automation – Benefits and Concepts

- DevOps Automation is broadly divided into – Continuous Integration, Continuous Delivery, (CI/CD) and Continuous Deployment

- The CI/CD approach largely offers the following benefits to businesses:
  - Faster software builds
  - Time to market – the deadline deployment to PROD will be achieved easier
  - Improve the code quality
  - Efficient Developers

## Overview of Jenkins

- Open source automation tool
- Jenkins is used to integrate all DevOps stages with the help of plugins.
- Jenkins has well over 1000 plugins: Git, Ansible, Amazon EC2, Maven 2 project, HTML publisher etc.
- Multi-technology
- Multi-platform
- Extensible
- Pipeline supports building Continuous Delivery (CDel) pipelines through either a Web UI or a scripted Jenkinsfile.

### Jenkins User Interface

![jenkins_ui](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/jenkins_ui.PNG)

## Overview of Ansible

- Open source automation platform
- Agentless
- Desired end state
- Idempotency
- Human-readable automation

> Playbook -> Play -> Task -> Module

### Orientation to the Classroom Environment

![classroom](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/classroom.PNG)

### Ansible Inventory

- Static inventory (groups, :children, implicit localhost) 
- Default groups: all (without the default: localhost), ungrouped

### Ansible Configuration Files

- Three locations: /etc/ansible/ansible.cfg, ~/.ansible.cfg, ./ansible.cfg (the recommended location for testing)
- Mostly used sections: [defaults], [privilege_escalation], [ssh_connection]

### Variables in playbooks

- vars:
![vars](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/vars.png)

- vars_files:
![vars_files](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/vars_files.png)

### Gathering facts

- Facts = variables that are automatically discovered by Ansible on a managed host.

Example of facts gathered from a managed host: the host name, the IP addresses, the version of the operating system, the available disk space, memory

### Implementing Task Control

- Loops: with_items, with_nested (loops inside of loops), with_fileglob
> Note: with_X deprecated, use loop instead
- Running tasks conditionally: when
- Implementing tags: --tags and --skip-tags

### Handlers

- Tasks that respond to a notification triggered by other tasks (using notify statement)
- Globally-unique name
- Ansible notifies handlers only if the task acquires the CHANGED status
- If no task notifies the handler by name -> it will not run
- If one or more tasks notify the handler -> it will run ONCE, AFTER all the tasks in the play are completed ( unless - meta: flush_handlers) 

### Ansible blocks and Error Handling

Blocks= clauses that logically group tasks

- block : main tasks to run
- rescue: tasks that will be run if the tasks in the block clause fails
- always: tasks that will run independently of the result of the other clauses

![block](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/block.png)

### Role structure

![role](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/role.PNG)

### Implementing roles

Create roles:

- Using ansible-galaxy command line tool
  - ansible-galaxy  init //will create the role structure
  - ansible-galaxy install -r //will install role

Use roles in playbooks

- roles/requirements.yml

![requirements](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/requirements.png)

### Order of execution

- default : the order specified in the playbook
- pre_tasks: executed before any roles are applied
- post_tasks: executed after all roles are applied

## What is DevOps?

Let's watch on Youtube [What is DevOps? - In Simple English](https://www.youtube.com/watch?v=_I94-tJlovg).
