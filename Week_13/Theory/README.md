# Week 12 - Devops

## Table of contents

- [Infrastructure as code - Terraform](#infrastructure-as-code---terraform)
- [Overview of Ansible](#overview-of-ansible)
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

- Playbooks: An ordered list of tasks along with its necessary parameters that define a recipe to configure a system.

- Roles: Redistributable units of organization that allow users to share automation code easier.

### Orientation to the Lab Environment

![classroom](https://github.com/WebToLearn/fx-trading-app/blob/devops_open_source/Week_12/Theory/images/classroom.PNG)



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
