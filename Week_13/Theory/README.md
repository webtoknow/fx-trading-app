# Week 13 - Devops Automation

## Table of contents

- [Infrastructure as code - Terraform](#infrastructure-as-code---terraform)
- [Overview of Ansible](#overview-of-ansible)
- [Terraform vs Ansible](#terraform-vs-ansible)
- [Overview of Jenkins](#overview-of-jenkins)

## Infrastructure as code - Terraform

Infrastructure as code (IaC) tools allow you to manage infrastructure with configuration files rather than through a graphical user interface. IaC allows you to build, change, and manage your infrastructure in a safe, consistent, and repeatable way by defining resource configurations that you can version, reuse, and share.

Terraform is HashiCorp's infrastructure as code tool. It lets you define resources and infrastructure in human-readable, declarative configuration files, and manages your infrastructure's lifecycle. Using Terraform has several advantages over manually managing your infrastructure:

- Terraform can manage infrastructure on multiple cloud platforms.
- The human-readable configuration language helps you write infrastructure code quickly.
- Terraform's state allows you to track resource changes throughout your deployments.
- You can commit your configurations to version control to safely collaborate on infrastructure.

### Manage any infrastructure
Terraform plugins called providers let Terraform interact with cloud platforms and other services via their application programming interfaces (APIs). HashiCorp and the Terraform community have written over 1,000 providers to manage resources on Amazon Web Services (AWS), Azure, Google Cloud Platform (GCP), Kubernetes, Helm, GitHub, Splunk, and DataDog, just to name a few. Find providers for many of the platforms and services you already use in the Terraform Registry. If you don't find the provider you're looking for, you can write your own.

### Standardize your deployment workflow
Providers define individual units of infrastructure, for example compute instances or private networks, as resources. You can compose resources from different providers into reusable Terraform configurations called modules, and manage them with a consistent language and workflow.

Terraform's configuration language is declarative, meaning that it describes the desired end-state for your infrastructure, in contrast to procedural programming languages that require step-by-step instructions to perform tasks. Terraform providers automatically calculate dependencies between resources to create or destroy them in the correct order.

![terraform-deployment-workflow](https://content.hashicorp.com/api/assets?product=tutorials&version=main&asset=public%2Fimg%2Fterraform%2Fterraform-iac.png)

To deploy infrastructure with Terraform:

- Scope - Identify the infrastructure for your project.
- Author - Write the configuration for your infrastructure.
- Initialize - Install the plugins Terraform needs to manage the infrastructure.
- Plan - Preview the changes Terraform will make to match your configuration.
- Apply - Make the planned changes.

### Track your infrastructure
Terraform keeps track of your real infrastructure in a state file, which acts as a source of truth for your environment. Terraform uses the state file to determine the changes to make to your infrastructure so that it will match your configuration.

### Collaborate
Terraform allows you to collaborate on your infrastructure with its remote state backends. When you use Terraform Cloud, you can securely share your state with your teammates, provide a stable environment for Terraform to run in, and prevent race conditions when multiple people make configuration changes at once.

You can also connect Terraform Cloud to version control systems (VCSs) like GitHub, GitLab, and others, allowing it to automatically propose infrastructure changes when you commit configuration changes to VCS. This lets you manage changes to your infrastructure through version control, as you would with application code.

### About the Terraform Language
The main purpose of the Terraform language is declaring resources, which represent infrastructure objects. All other language features exist only to make the definition of resources more flexible and convenient.

A Terraform configuration is a complete document in the Terraform language that tells Terraform how to manage a given collection of infrastructure. A configuration can consist of multiple files and directories.

The syntax of the Terraform language consists of only a few basic elements:
```
resource "aws_vpc" "main" {
  cidr_block = var.base_cidr_block
}

<BLOCK TYPE> "<BLOCK LABEL>" "<BLOCK LABEL>" {
  # Block body
  <IDENTIFIER> = <EXPRESSION> # Argument
}
```

- Blocks are containers for other content and usually represent the configuration of some kind of object, like a resource. Blocks have a block type, can have zero or more labels, and have a body that contains any number of arguments and nested blocks. Most of Terraform's features are controlled by top-level blocks in a configuration file.
- Arguments assign a value to a name. They appear within blocks.
- Expressions represent a value, either literally or by referencing and combining other values. They appear as values for arguments, or within other expressions.

The Terraform language is declarative, describing an intended goal rather than the steps to reach that goal. The ordering of blocks and the files they are organized into are generally not significant; Terraform only considers implicit and explicit relationships between resources when determining an order of operations.

### Example
The following example describes a simple network topology for Amazon Web Services, just to give a sense of the overall structure and syntax of the Terraform language. Similar configurations can be created for other virtual network services, using resource types defined by other providers, and a practical network configuration will often contain additional elements not shown here.

```
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 1.0.4"
    }
  }
}

variable "aws_region" {}

variable "base_cidr_block" {
  description = "A /16 CIDR range definition, such as 10.1.0.0/16, that the VPC will use"
  default = "10.1.0.0/16"
}

variable "availability_zones" {
  description = "A list of availability zones in which to create subnets"
  type = list(string)
}

provider "aws" {
  region = var.aws_region
}

resource "aws_vpc" "main" {
  # Referencing the base_cidr_block variable allows the network address
  # to be changed without modifying the configuration.
  cidr_block = var.base_cidr_block
}

resource "aws_subnet" "az" {
  # Create one subnet for each given availability zone.
  count = length(var.availability_zones)

  # For each subnet, use one of the specified availability zones.
  availability_zone = var.availability_zones[count.index]

  # By referencing the aws_vpc.main object, Terraform knows that the subnet
  # must be created only after the VPC is created.
  vpc_id = aws_vpc.main.id

  # Built-in functions and operators can be used for simple transformations of
  # values, such as computing a subnet address. Here we create a /20 prefix for
  # each subnet, using consecutive addresses for each availability zone,
  # such as 10.1.16.0/20 .
  cidr_block = cidrsubnet(aws_vpc.main.cidr_block, 4, count.index+1)
}
```

## Overview of Ansible

### What is Ansible?
Ansible is an open source automation and orchestration tool for software provisioning, configuration management, and software deployment. Ansible can easily run and configure Unix-like systems as well as Windows systems to provide infrastructure as code. It contains its own declarative programming language for system configuration and management.

Ansible is popular for its simplicity of installation, ease of use in what concerns the connectivity to clients, its lack of agent for Ansible clients and the multitude of skills. It functions by connecting via SSH to the clients, so it doesn’t need a special agent on the client-side, and by pushing modules to the clients, the modules are then executed locally on the client-side and the output is pushed back to the Ansible server.

Since it uses SSH, it can very easily connect to clients using SSH-Keys, simplifying though the whole process. Client details, like hostnames or IP addresses and SSH ports, are stored in files called inventory files. Once you have created an inventory file and populated it, ansible can use it.

### How does Ansible work?
Ansible uses the concepts of control and managed nodes. It connects from the control node, any machine with Ansible installed, to the managed nodes sending commands and instructions to them.

The units of code that Ansible executes on the managed nodes are called modules. Each module is invoked by a task, and an ordered list of tasks together forms a playbook. Users write playbooks with tasks and modules to define the desired state of the system.

The managed machines are represented in a simplistic inventory file that groups all the nodes into different categories.

Ansible leverages a very simple language, YAML, to define playbooks in a human-readable data format that is really easy to understand from day one. Even more, Ansible doesn’t require the installation of any extra agents on the managed nodes so it’s simple to start using it.

Typically, the only thing a user needs is a terminal to execute Ansible commands and a text editor to define the configuration files.

### Basic Concepts & Terms

- Ansible Server (controller): The machine where Ansible is installed and from which all tasks and playbooks will be ran.

- Host: A remote machine managed by Ansible.

- Group: Several hosts grouped together that share a common attribute.

- Inventory: A collection of all the hosts and groups that Ansible manages. Could be a static file in the simple cases or we can pull the inventory from remote sources, such as cloud providers.

- Modules: Units of code that Ansible sends to the remote nodes for execution. Basically, a module is a command or set of similar Ansible commands meant to be executed on the client-side.

- Tasks: Units of action that combine a module and its arguments along with some other parameters.

- Plays: An ordered list of tasks along with its necessary parameters that define a recipe to configure a system.

- Playbooks: yaml file containing one ore more plays 

- Roles: Redistributable units of organization that allow users to share automation code easier. Roles enable us to reuse and share our Ansible code efficiently. They provide a well-defined framework and structure for setting your tasks, variables, handlers, metadata, templates, and other files. This way, we can reference and call them in our playbooks with just a few lines of code while we can reuse the same roles over many projects without the need to duplicate our code.

### Ansible Playbooks

Playbooks are the simplest way in Ansible to automate repeating tasks in the form of reusable and consistent configuration files. Playbooks are defined in YAML files and contain any ordered set of steps to be executed on our managed nodes.

As mentioned, tasks in a playbook are executed from top to bottom. At a minimum, a playbook should define the managed nodes to target and some tasks to run against them.

In playbooks, data elements at the same level must share the same indentation while items that are children of other items must be indented more than their parents. 

Let’s look at a simple playbook to get an idea of how that looks in practice. 

```
---
  # Play1 - WebServer related tasks
  - name: Play Web - Create apache directories and username in web servers
    hosts: webservers
    become: yes
    become_user: root
    tasks:
      - name: create username apacheadm
        user:
          name: apacheadm
          group: users,admin
          shell: /bin/bash
          home: /home/weblogic

      - name: install httpd
        yum:
          name: httpd
          state: installed
        
  # Play2 - Application Server related tasks
  - name: Play app - Create tomcat directories and username in app servers
    hosts: appservers
    become: yes
    become_user: root
    tasks:
      - name: Create a username for tomcat
        user:
          name: tomcatadm
          group: users
          shell: /bin/bash
          home: /home/tomcat

      - name: create a directory for apache tomcat
        file:
          path: /opt/oracle
          owner: tomcatadm
          group: users
          state: present
          mode: 0755
```

To run the playbook, using a local hosts inventory, the shell command is:
```
ansible-playbook sampleplaybook.yml -i ansible_hosts
```

### Ansible alternatives

Although Ansible is the most common configuration management tool at the moment, there are two other major alternatives. These two are `Puppet` and `Chef`. 

They are mentioned here just for information purposes. One of the major difference between Ansible and the other two is that the first one is agentless. What this means is that you do not need to install any software on the remote nodes, before attempting to run automation tasks. All that Ansible requires is that the remote host has a correct version of python installed.

<!-- ![ansible-chef-puppet](https://www.veritis.com/wp-content/uploads/Infographics/chef-vs-puppet-vs-ansible-what-are-the-differences-it-infographic.png) -->

![ansible-puppet-chef](https://blog.aspiresys.com/wp-content/uploads/2022/03/Chef-vs-Puppet-vs-Ansible-%E2%80%93-The-Salient-Differences.jpg)


### Orientation to the Lab Environment

![classroom](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/classroom.PNG)



## Terraform vs Ansible

### Configuration Management vs Provisioning
Chef, Puppet, and Ansible are all configuration management tools, which means they are designed to install and manage software on existing servers. CloudFormation, Heat, Pulumi, and Terraform are provisioning tools, which means they are designed to provision the servers themselves (as well as the rest of your infrastructure, like load balancers, databases, networking configuration, etc), leaving the job of configuring those servers to other tools. Although the distinction is not entirely clear cut, given that configuration management tools can typically do some degree of provisioning (e.g., you can deploy a server with Ansible) and that provisioning tools can typically do some degree of configuration (e.g., you can run configuration scripts on each server you provision with Terraform), you typically want to pick the tool that’s the best fit for your use case.

In particular, we’ve found that if you use Docker, the vast majority of your configuration management needs are already taken care of. With Docker, you can create images (such as containers or virtual machine images) that have all the software your server needs already installed and configured. Once you have such an image, all you need is a server to run it. And if all you need to do is provision a bunch of servers, then a provisioning tool like Terraform is typically going to be a better fit than a configuration management tool (here’s an example of how to use Terraform to deploy Docker on AWS).

That said, if you’re not using server templating tools, a good alternative is to use a configuration management and provisioning tool together. For example, a popular combination is to use Terraform to provision your servers and Ansible to configure each one.

![terraform-ansible](https://www.whizlabs.com/blog/wp-content/uploads/2019/12/Ansible_vs_Terraform.png)

### Mutable Infrastructure vs Immutable Infrastructure
Configuration management tools such as Chef, Puppet, and Ansible typically default to a mutable infrastructure paradigm. For example, if you instruct Chef to install a new version of OpenSSL, it will run the software update on your existing servers, and the changes will happen in place. Over time, as you apply more and more updates, each server builds up a unique history of changes. As a result, each server becomes slightly different than all the others, leading to subtle configuration bugs that are difficult to diagnose and reproduce (configuration drift). Even with automated tests, these bugs are difficult to catch; a configuration management change might work just fine on a test server, but that same change might behave differently on a production server because the production server has accumulated months of changes that aren’t reflected in the test environment.

If you’re using a provisioning tool such as Terraform to deploy machine images created by Docker or Packer, most “changes” are actually deployments of a completely new server. For example, to deploy a new version of OpenSSL, you would use Packer to create a new image with the new version of OpenSSL, deploy that image across a set of new servers, and then terminate the old servers. Because every deployment uses immutable images on fresh servers, this approach reduces the likelihood of configuration drift bugs, makes it easier to know exactly what software is running on each server, and allows you to easily deploy any previous version of the software (any previous image) at any time. It also makes your automated testing more effective, because an immutable image that passes your tests in the test environment is likely to behave exactly the same way in the production environment.

Of course, it’s possible to force configuration management tools to do immutable deployments, too, but it’s not the idiomatic approach for those tools, whereas it’s a natural way to use provisioning tools. It’s also worth mentioning that the immutable approach has downsides of its own. For example, rebuilding an image from a server template and redeploying all your servers for a trivial change can take a long time. Moreover, immutability lasts only until you actually run the image. After a server is up and running, it will begin making changes on the hard drive and experiencing some degree of configuration drift (although this is mitigated if you deploy frequently).

### Procedural vs Declarative
Chef and Ansible encourage a procedural style where you write code that specifies, step-by-step, how to to achieve some desired end state. Terraform, CloudFormation, Pulumi, Heat, and Puppet all encourage a more declarative style where you write code that specifies your desired end state, and the IAC tool itself is responsible for figuring out how to achieve that state.

## Overview of Jenkins

Jenkins is an open source continuous integration/continuous delivery and deployment (CI/CD) automation software DevOps tool written in the Java programming language. It is used to implement CI/CD workflows, called pipelines.

Pipelines automate testing and reporting on isolated changes in a larger code base in real time and facilitates the integration of disparate branches of the code into a main branch. They also rapidly detect defects in a code base, build the software, automate testing of their builds, prepare the code base for deployment (delivery), and ultimately deploy code to containers and virtual machines, as well as bare metal and cloud servers. 

![jenkins-ci-cd](https://cdn.hostadvice.com/2018/03/devopsjenkins.png)

Jenkins runs as a server on a variety of platforms including Windows, MacOS, Unix variants and especially, Linux. It requires a Java 8 VM and above and can be run on the Oracle JRE or OpenJDK. Usually, Jenkins runs as a Java servlet within a Jetty application server. It can be run on other Java application servers such as Apache Tomcat. More recently, Jenkins has been adapted to run in a Docker container. There are read-only Jenkins images available in the Docker Hub online repository.

### Plugins
A plugin is an enhancement to the Jenkins system. They help extend Jenkins capabilities and integrated Jenkins with other software. Plugins can be downloaded from the online Jenkins Plugin repository and loaded using the Jenkins Web UI or CLI. Currently, the Jenkins community claims over 1500 plugins available for a wide range of uses.

Plugins help to integrate other developer tools into the Jenkins environment, add new user interface elements to the Jenkins Web UI, help with administration of Jenkins, and enhance Jenkins for build and source code management. One of the more common uses of plugins is to provide integration points for CI/CD sources and destinations. These include software version control systems (SVCs) such as Git and Atlassian BitBucket, container runtime systems -- especially Docker, virtual machine hypervisors such as VMware vSphere, public cloud instances including Google Cloud Platform and Amazon AWS, and private cloud systems such as OpenStack. There are also plugins that assist in communicating with operating systems over FTP, CIFS, and SSH.

A plugin is written in Java. Plugins use their own set of Java Annotations and design patterns that define how the plugin is instantiated, extension points, the function of the plugin and the UI representation in the Jenkins Web UI. Plugin development also makes use of Maven deployment to Jenkins.

### Summary 

- Open source automation tool
- Jenkins is used to integrate all DevOps stages with the help of plugins.
- Jenkins has well over 1000 plugins: Git, Ansible, Amazon EC2, Maven 2 project, HTML publisher etc.
- Multi-technology
- Multi-platform
- Extensible
- Pipeline supports building Continuous Delivery (CDel) pipelines through either a Web UI or a scripted Jenkinsfile.


### Jenkins User Interface

![jenkins_ui](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/jenkins_ui.PNG)

