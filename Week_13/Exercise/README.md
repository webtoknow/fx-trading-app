# Week 13 - Continous deployment exercises

## Table of contents

- [Exercise 1 - Infrastructure as code with Terraform Introduction](#exercise-1---infrastructure-as-code-with-terraform-introduction)
- [Exercise 2 - Terraform Variables and Dependencies](#exercise-2---terraform-variables-and-dependencies)
- [Exercise 3 - Ansible Automation](#exercise-3---ansible-automation)
- [Exercise 4 - Jenkins Automation](#exercise-4---jenkins-automation)

<br/>

## Exercise 1 - Infrastructure as code with Terraform Introduction

### Overview
In this exercise, you will use Terraform to create, update, and destroy Google Cloud resources. You will start by defining Google Cloud as the provider.
You will then create a VM instance without mentioning the network to see how terraform parses the configuration code. You will then edit the code to add network and create a VM instance on Google Cloud.
You will explore how to update the VM instance. You will edit the existing configuration to add tags and then edit the machine type. You will then execute terraform commands to destroy the resources created.

### Objectives
In this lab you will learn how to perform the following tasks:
- Verify Terraform installation
- Define Google Cloud as the provider
- Create, change, and destroy Google Cloud resources by using Terraform

### Check Terraform Installation
1.	On the Google Cloud menu, click Activate Cloud Shell. If a dialog box appears, click Continue.
2.	If prompted, click Continue.
3.	Confirm that Terraform is installed by running the following command:
```
terraform --version
```

Note: The available downloads for the latest version of Terraform can be found on the Terraform website. Terraform is distributed as a binary package for all supported platforms and architectures and Cloud Shell uses Linux 64-bit.
The output should look like this (do not copy; this is example output):
```
Terraform v1.2.2
Terraform comes pre-installed in Cloud Shell. With Terraform already installed, you can directly create some infrastructure.
```

### Add Google Cloud provider
1.	Create a directory called compute:
```
mkdir compute && cd $_
```
2.	Create the main.tf file:
```
touch main.tf
```
3.	Click Open Editor on the toolbar of Cloud Shell. Click Open in a new window to leave the Editor open in a separate tab.
4.	Copy the following code in the main.tf file.
```
terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
    }
  }
}
provider "google" {
  region  = "us-central1"
  zone    = "us-central1-c"
}
```
5.	Click File > Save.
6.	Switch to the Cloud Shell and run the terraform init command.
```
terraform init
```
The output should look like this (do not copy; this is example output):
```
Initializing the backend...
Initializing provider plugins...
- Finding hashicorp/google versions matching "4.15.0"...
- Installing hashicorp/google v4.15.0...
- Installed hashicorp/google v4.15.0 (signed by HashiCorp)
Terraform has created a lock file .terraform.lock.hcl to record the provider
selections it made above. Include this file in your version control repository
so that Terraform can guarantee to make the same selections by default when
you run "terraform init" in the future.
Terraform has been successfully initialized!
```

### Build the infrastructure
Let us try creating a compute instance without specifying the network parameter and see how terraform processes such configuration.
1.	Switch to the editor window. Within the main.tf file, enter the following code block.
```
resource "google_compute_instance" "terraform" {
  name         = "terraform"
  machine_type = "n1-standard-2"
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
}
```
2.	Save the main.tf file by clicking File > Save.
3.	Now run the following command to preview if the compute engine will be created.
```
terraform plan
```
4.	The configuration fails with the following error. This is because you cannot configure a compute engine without a network.
```
│ Error: Insufficient network_interface blocks
│
│ on main.tf line 15, in resource "google_compute_instance" "terraform":
│ 15: resource "google_compute_instance" "terraform" {
│
│ At least 1 "network_interface" blocks are required.
```
5.	Now add the network by including the following code segment to the google_compute_instance block.
```
network_interface {
    network = "default"
    access_config {
    }
}
```
The final code in main.tf file will look like this:
```
terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
    }
  }
}
provider "google" {
  region  = "us-central1"
  zone    = "us-central1-c"
}
resource "google_compute_instance" "terraform" {
  name         = "terraform"
  machine_type = "n1-standard-2"
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}
```
6.	Save the main.tf file by clicking File > Save.
7.	Now run the terraform plan command to preview if the compute engine will be created.
```
terraform plan
```
Click Authorize when prompted.
The output should look like this (do not copy; this is example output):
```
Terraform used the selected providers to generate the following execution plan. Resource actions are indicated with the following symbols:
  + create
Terraform will perform the following actions:
  # google_compute_instance.terraform will be created
  + resource "google_compute_instance" "terraform" {
      + can_ip_forward       = false
      + cpu_platform         = (known after apply)
      + current_status       = (known after apply)
      + deletion_protection  = false
      ...
Plan: 1 to add, 0 to change, 0 to destroy.
```
8.	Apply the desired changes by running the following command.
```
terraform apply
```
9.	Confirm the planned actions by typing yes.
The output should look like this (do not copy; this is example output):
```
...
Apply complete! Resources: 1 added, 0 changed, 0 destroyed.
Note: If you get an error, revisit the previous steps to ensure that you have the correct code entered in the main.tf file.
```

### Verify on Cloud Console
In the Cloud Console, verify that the resources were created.
1.	In the Cloud Console, on the Navigation menu, click Compute Engine > VM instances.
2.	View the terraform instance created. 

### Change the infrastructure
In this task, we will be performing 2 types of changes to the infrastructure:
- Adding network tags
- Editing the machine-type
Adding tags to the compute resource
In addition to creating resources, Terraform can also make changes to those resources.
1.	Add a tags argument to the instance we just created so that it looks like this:
```
resource "google_compute_instance" "terraform" {
  name         = "terraform"
  machine_type = "n1-standard-2"
  tags         = ["web", "dev"]
  # ...
}
```
2.	Run terraform plan
```
terraform plan
```
3.	Run terraform apply to update the instance.
```
terraform apply
```
The output should look like this (do not copy; this is example output):
```
Terraform used the selected providers to generate the following execution plan. Resource actions are indicated with the following symbols:
  ~ update in-place
Terraform will perform the following actions:
  # google_compute_instance.terraform will be updated in-place
  ~ resource "google_compute_instance" "terraform" {
        id                   = "projects/example-project-id/zones/us-central1-c/instances/terraform"
        name                 = "terraform"
      ~ tags                 = [
          + "dev",
          + "web",
        ]
        # (17 unchanged attributes hidden)
        # (4 unchanged blocks hidden)
    }
Plan: 0 to add, 1 to change, 0 to destroy.
```
The prefix ~ means that Terraform will update the resource in-place.

4.	Respond yes when promoted, and Terraform will add the tags to your instance.

### Editing the machine type without stopping the VM
Machine type of a VM cannot be changed on a running VM. Let us see how terraform processes the change in machine type for a running VM.
1.	Navigate to main.tf and edit the machine_type argument of terraform instance from n1-standard-2 to n1-standard-1 so that it looks like this:
```
resource "google_compute_instance" "terraform" {
  name         = "terraform"
  machine_type = "n1-standard-1"
  tags         = ["web", "dev"]
  # ...
}
```
2.	Run terraform plan
```
terraform plan
```
3.	Run terraform apply again to update the instance.
```
terraform apply
```
The terraform apply fails with a warning as shown below. (do not copy; this is example output)
```
╷
│ Error: Changing the machine_type, min_cpu_platform, service_account, enable_display, shielded_instance_config, scheduling.node_affinities or network_interface.[#d].(network/subnetwork/subnetwork_project) or advanced_machine_features on a started instance requires stopping it. To acknowledge this, please set allow_stopping_for_update = true in your config. You can also stop it by setting desired_status = "TERMINATED", but the instance will not be restarted after the update.
│
│   with google_compute_instance.terraform,
│   on main.tf line 31, in resource "google_compute_instance" "terraform":
│   31: resource "google_compute_instance" "terraform" {
```
4.	The machine-type cannot be changed on a running VM. To ensure the VM stops before updating the machine_type, set allow_stopping_for_update argument to true so that the code looks like this:
```
resource "google_compute_instance" "terraform" {
  name         = "terraform"
  machine_type = "n1-standard-1"
  tags         = ["web", "dev"]
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
  allow_stopping_for_update = true
}
```
5.	Run terraform plan
```
terraform plan
```
6.	Run terraform apply again to update the instance.
```
terraform apply
```
7.	Respond yes when promoted.
8.	Verify the change in machine-type and the tags added by navigating to the VM Instances on the Cloud Console and clicking the terraform instance created. 

### Destroy the infrastructure
You have now seen how to build and change infrastructure. Before moving on to creating multiple resources and showing resource dependencies, you will see how to completely destroy your Terraform-managed infrastructure.
1.	Execute the following command. Answer yes to execute this plan and destroy the infrastructure:
```
terraform destroy
```
2.	Verify that the instance terraform no longer exists by navigating to the VM Instances on the Cloud Console.

<br />

## Exercise 2 - Terraform Variables and Dependencies

### Overview
In this exercise, you will create two VMs in the default network. We will use variables to define the VM's attributes at runtime and use output values to print a few resource attributes.
We will then add a static IP address to the first VM to examine how terraform handles implicit dependencies. We will then create a GCS bucket by mentioning explicit dependency to the VM to examine how terraform handles explicit dependency.

### Objectives
In this lab, you learn how to perform the following tasks:
- Use variables and output values
- Observe implicit dependency
- Create explicit resource dependency

### Initialize Terraform
Let us initialize Terraform by setting Google as the provider.
1.	Open Cloud Shell and execute the following command to verify that terraform is installed.
```
terraform -version
```
The output should look like this (do not copy; this is example output):
```
Terraform v1.2.2
Terraform comes pre-installed in Cloud Shell. With Terraform already installed, you can directly create infrastructure resources.
```
2.	Create a directory for your Terraform configuration and navigate to it by running the following command:
```
mkdir tfinfra && cd $_
```
3.	In Cloud Shell, click Open Editor to open Cloud Shell Editor.
4.	Click Open in a new window button to leave the Editor open in a separate tab..
5.	To create a new file in the tfinfra folder, right-click on tfinfra folder and click New File.
6.	Name the new file provider.tf, and then click OK.
7.	Add the following code into provider.tf:
```
  provider "google" {
  project = "<REPLACE_WITH_PROJECT_ID>"
  region  = "us-east1"
  zone    = "us-east1-b"
}
```
You can find your own project id by running the following command in Cloud Shell:
```
gcloud config list --format 'value(core.project)'
```
8.	To save provider.tf, click File > Save.
9.	Initialize Terraform by running the following commands:
```
terraform init
```
The output should look like this (do not copy; this is example output):
```
Initializing the backend...
Initializing provider plugins...
- Finding hashicorp/google versions matching "4.15.0"...
- Installing hashicorp/google v4.15.0...
- Installed hashicorp/google v4.15.0 (signed by HashiCorp)
Terraform has created a lock file .terraform.lock.hcl to record the provider
selections it made above. Include this file in your version control repository
so that Terraform can guarantee to make the same selections by default when
you run "terraform init" in the future.
Terraform has been successfully initialized!
Terraform has now installed the necessary plug-ins to interact with the Google Cloud API.
```
Authentication is not required for API. The Cloud Shell credentials give access to the project and APIs.

### View Implicit Resource Dependency
To demonstrate how Terraform infers an implicit dependency, we assign a static IP address to the VM instance.

#### Create a VM instance
Let us create a VM instance and parameterize its configuration by defining variables:
1.	To create a new file, right-click on tfinfra folder and click New File.
2.	Name the new file instance.tf, and then open it.
3.	Copy the following code into instance.tf:
```
resource google_compute_instance "vm_instance" {
name         = "${var.instance_name}"
zone         = "${var.instance_zone}"
machine_type = "${var.instance_type}"
boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      }
  }
 network_interface {
    network = "default"
    access_config {
      # Allocate a one-to-one NAT IP to the instance
    }
  }
}
```
4.	To save instance.tf, click File > Save.

#### Create variables
1.	Right-click on tfinfra folder and click New File to create a new file for variables.
2.	Name the new file variables.tf, and then click OK.
3.	Add the following properties to variables.tf:
```
variable "instance_name" {
  type        = string
  description = "Name for the Google Compute instance"
}
variable "instance_zone" {
  type        = string
  description = "Zone for the Google Compute instance"
}
variable "instance_type" {
  type        = string
  description = "Type of the Google Compute instance"
  default     = "n1-standard-1"
  }
```
By giving instance_type a default value, you make the variable optional. The instance_name, and instance_zone are required, and you will define them at run time.
4.	To save variable.tf, click File > Save.

#### Create output values
1.	Right-click on tfinfra folder and click New File to create a new file for outputs.
2.	Name the new file outputs.tf, and then click OK.
3.	Add the following properties to outputs.tf:
```
output "network_IP" {
  value = google_compute_instance.vm_instance.instance_id
  description = "The internal ip address of the instance"
}
output "instance_link" {
  value = google_compute_instance.vm_instance.self_link
  description = "The URI of the created resource."
}
```
4.	To save outputs.tf, click File > Save.

#### Assign a static IP
1.	Now add to your configuration by assigning a static IP to the VM instance in instance.tf
```
resource "google_compute_address" "vm_static_ip" {
  name = "terraform-static-ip"
}
```
This should look familiar from the earlier example of adding a VM instance resource, except this time you're creating a google_compute_address resource type. This resource type allocates a reserved IP address to your project.
2.	Update the network_interface configuration for your instance like so:
```
 network_interface {
    network = "default"
    access_config {
      # Allocate a one-to-one NAT IP to the instance
      nat_ip = google_compute_address.vm_static_ip.address
    }
  }
```
The final code is as show below.
```
 resource "google_compute_address" "vm_static_ip" {
  name = "terraform-static-ip"
}
 resource google_compute_instance "vm_instance" {
name         = "${var.instance_name}"
zone         = "${var.instance_zone}"
machine_type = "${var.instance_type}"
boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      }
  }
 network_interface {
    network = "default"
    access_config {
      # Allocate a one-to-one NAT IP to the instance
      nat_ip = google_compute_address.vm_static_ip.address
    }
  }
}
```
3.	Initialize terraform by running the following command.
```
terraform init
```
4.	Run the following command to preview the resources created.
```
terraform plan
```
5.	If prompted, enter the details for the instance creation as shown below:
- var.instance_name: myinstance
- var.instance_zone: us-east1-b
6.	Run the following command to view the order of resource creation.
```
terraform apply
```
7.	If prompted, enter the details for the instance creation as shown below:
- var.instance_name: myinstance
- var.instance_zone: us-east1-b
8.	Confirm the planned actions by typing yes.
Note: Observe that Terraform handles implicit dependency automatically by creating a static IP address before the instance.
```
google_compute_address.vm_static_ip: Creating...
google_compute_address.vm_static_ip: Creation complete after 2s [id=projects/example-project-id/regions/us-east1/addresses/terraform-static-ip]
google_compute_instance.vm_instance: Creating...
google_compute_instance.vm_instance: Still creating... [10s elapsed]
google_compute_instance.vm_instance: Creation complete after 15s [id=projects/example-project-id/zones/us-east1-b/instances/myinstance]
Apply complete! Resources: 2 added, 0 changed, 0 destroyed.
```

#### Verify on Cloud Console
In the Cloud Console, verify that the resources were created.
1.	In the Cloud Console, on the Navigation menu (three horizontal lines on top left), click Compute Engine > VM instances.
2.	View that an instance named `myinstance` is created.  
3.	Verify the static IP address. On the Navigation menu, click VPC networks > IP addresses > External IP addresses.  

### Create Explicit Dependency
Explicit dependencies are used to inform dependencies between resources that are not visible to Terraform. In this example, consider that you will run on your instance that expects to use a specific Cloud Storage bucket, but that dependency is configured inside the application code and thus not visible to Terraform. In that case, you can use depends_on to explicitly declare the dependency.
1.	To create a new file, right-click on tfinfra folder and click New File.
2.	Name the new file `exp.tf`, and then click OK.
3.	Add a Cloud Storage bucket and an instance with an explicit dependency on the bucket by adding the following base code into exp.tf:
```
# Create a new instance that uses the bucket
resource "google_compute_instance" "another_instance" {
  name         = "terraform-instance-2"
  machine_type = "f1-micro"
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
  # Tells Terraform that this VM instance must be created only after the
  # storage bucket has been created.
  depends_on = [google_storage_bucket.example_bucket]
}
```
4.	Add the following code to create a bucket.
```
# New resource for the storage bucket our application will use.
resource "google_storage_bucket" "example_bucket" {
  name     = "<UNIQUE-BUCKET-NAME>"
  location = "US"
  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}
```
Note: Storage buckets must be globally unique. Because of this, you will need to replace UNIQUE-BUCKET-NAME with a unique, valid name for a bucket. Using the project name and the date is usually a good way to create a unique bucket name.
Notice that in our code the VM instance configuration is added before the GCS bucket. When executing Terraform apply you will notice that the order that resources are defined in a terraform configuration file has no effect on how Terraform applies your changes.
The final code is as show below:
```
resource "google_compute_instance" "another_instance" {
  name         = "terraform-instance-2"
  machine_type = "f1-micro"
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
  # Tells Terraform that this VM instance must be created only after the
  # storage bucket has been created.
  depends_on = [google_storage_bucket.example_bucket]
}
resource "google_storage_bucket" "example_bucket" {
  name     = "<UNIQUE-BUCKET-NAME>"
  location = "US"
  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}
```
5.	To save exp.tf, click File > Save.
6.	Run the following command to preview the resources created.
```
terraform plan
```
If prompted, enter the details for the instance creation as shown below:
- var.instance_name: myinstance
- var.instance_zone: us-east1-b
7.	Run the following command to view the order of resource creation.
```
terraform apply
```
If prompted, enter the details for the instance creation as shown below:
- var.instance_name: myinstance
- var.instance_zone: us-east1-b
8.	Confirm the planned actions by typing `yes`.
9.	Observe that due explicit dependency, the compute instance is created after the creation of the Cloud Storage Bucket.
```
Enter a value: yes
google_storage_bucket.example_bucket: Creating...
google_storage_bucket.example_bucket: Creation complete after 1s [id=example-project-id]
google_compute_instance.another_instance: Creating...
google_compute_instance.another_instance: Still creating... [10s elapsed]
google_compute_instance.another_instance: Creation complete after 14s [id=projects/example-project-id/zones/us-east1-b/instances/terraform-instance-2]
Apply complete! Resources: 2 added, 0 changed, 0 destroyed.
```

#### Verify on Cloud Console
In the Cloud Console, verify that the resources were created.
1.	In the Cloud Console, on the Navigation menu, click Compute Engine > VM instances.
2.	View the terraform-instance-2 instance created.
3.	Verify the Cloud Storage bucket created. On the Navigation menu, click Cloud Storage.

### View Dependency Graph
1.	To view resource dependency graph of the resource created, execute the following command
```
terraform graph | dot -Tsvg > graph.svg
```
2.	Switch to the editor and notice a file called graph.svg created. Click the file to view the dependency graph.

### Review
In this exercise, you created a VM instance with a static IP address to view how implicit resource dependencies are handled with Terraform. You then created an explicit dependency by adding the depend_on argument so that you can create a GCS bucket before creating a VM instance. You also viewed the dependency graph that terraform uses to trace the order of resource creation.

Before moving on, make sure all remaining resources created with terraform during this exercise are removed, so we can work on a clean environment in the next step.
1.	Execute the following command. Answer yes to execute this plan and destroy the infrastructure:
```
terraform destroy
```
2.	Verify that the instances, static IPs and storage buckets created no longer exists by navigating to their respective areas on the Cloud Console.

<br/>

## Exercise 3 - Ansible Automation

### Setup required infrastructure using Terraform

For the next exercise, we need to setup the lab infrastructure for running Ansible playbooks. This will be comprised of an ansible-controller host and 3 web-server VMs for serving our application. Again, all the Terraform commands will be started from the Cloud Shell VM.

Make sure all lab infrastrucure from previous exercises is cleaned up (`terraform destroy` in corresponding paths) and create a new folder:
```
mkdir ansible-tf && cd $_
```

Create a new main.tf file which defines all 4 VMs (one ansible controller and 3 app hosts/web servers):

```
terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
    }
  }
}

provider "google" {
  region  = "europe-central2"
  zone    = "europe-central2-a"
}

resource "google_compute_instance" "ansible-controller" {
  name         = "ansible-controller"
  machine_type = "e2-medium"
  tags         = ["jenkins", "ansible-controller"]
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}

resource "google_compute_instance" "app-server-1" {
  name         = "app-server-1"
  machine_type = "e2-medium"
  tags         = ["app-server"]
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}

resource "google_compute_instance" "app-server-2" {
  name         = "app-server-2"
  machine_type = "e2-medium"
  tags         = ["app-server"]
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}

resource "google_compute_instance" "app-server-3" {
  name         = "app-server-3"
  machine_type = "e2-medium"
  tags         = ["app-server"]
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}

# define firewall rules
resource "google_compute_firewall" "jenkins" {
  name        = "jenkins"
  network     = "default"
  description = "Creates firewall rule targeting jenkins server"

  allow {
    protocol  = "tcp"
    ports     = ["8080"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags = ["jenkins"]
}

resource "google_compute_firewall" "app-server" {
  name        = "app-server"
  network     = "default"
  description = "Creates firewall rule for app-server hosts"

  allow {
    protocol  = "tcp"
    ports     = ["8220"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags = ["app-server"]
}
```

Deploy the Terraform infrastrucuture in the same way as you did for the previous exercises:
```
terraform init

terraform plan

terraform apply
```

### Configure Ansible Server

SSH to the ansible-controller VM to start the installation process.

Make sure Python `pip3` module is installed on host (does not come by default with Debian 11):
```
sudo apt install python3-pip
```

Go ahead with ansible installation using pip3:
```
sudo pip3 install ansible
```

Make sure ansible is installed correctly and available to non-root user:
```
ansible --version
```

As mentioned in the theory part, Ansible uses ssh for connecting the controller to the managed nodes and running modules or ad-hoc commands. This requires the ssh keys to be configured correctly at the OS level to allow ssh access.
```
ssh-keygen
```
Press `enter` on all prompts to accept default values and not use any passphrase.

Once the ssh public-private key pair is generated on the ansible-controller, you need to add the public key to each of the app-server VMs. This step is mandatory, otherwise ssh connections will be rejected - Ansible itself will not work.

Copy the contents of the public key on the ansible-controller VM in your clipboard:
```
cat ~/.ssh/id_rsa.pub
```

On `each` of the app-servers you need to add the ansible-controller's public key (the contents of your clipboard) to the authorized_keys file:
```
mkdir -p ~/.ssh
echo ANSIBLE_CONTROLLER_PUBLIC_KEY >> ~/.ssh/authorized_keys
```
In the above command, substitute the `ANSIBLE_CONTROLLER_PUBLIC_KEY` with the output from the cat ~/.ssh/id_rsa.pub command that you executed on your local system. It should start with ssh-rsa AAAA... or similar.

Ansible is able to also run commands on remote hosts as superuser (root). Because we don't want to be prompted for password when this happens, we need to configure the remote hosts OS to allow switching to root, when using our GCP global user. Run the following on each of the app-servers as well, after configuring the authorized_hosts file:
```
echo "$(whoami) ALL=(ALL) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/ansible_user
``` 


If everything worked correctly you are now done with the app-server VMs setup. From now on, all the following commands will be executed on `ansible-controller`.

To test the ssh connection, you can try running a remote command on one or each of the app-servers. 
```
ssh app-server-1 hostname
```
The `hostname` command is executed remotely and should display the correct host - app-server-1 in our example.

Now that we are done with ssh setup, we can go ahead with the Ansible configuration itself.

Create a new folder where you will place the local inventory file (otherwise ansible will try to use the default /etc/ansible/hosts) and ansible playbooks:
```
mkdir ~/ansible && cd $_
```

Create an inventory file and add the hostnames or IP addresses of the remote machines. If you use IPs, use the `internal` addresses, not the public ones - but in most cases the plain hostnames should work (GCP has a builtin DNS for the default network):
```
cat <<EOF > hosts
[webservers]
app-server-1
app-server-2
app-server-3
EOF
```

You can make sure that the hosts are accessible from the ansible server by running the `ping` module on all hosts defined in the local inventory file:
```
ansible -i hosts all -m ping
```
You should get back a similar outoput:
```
app-server-1 | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3"
    },
    "changed": false,
    "ping": "pong"
}
app-server-2 | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3"
    },
    "changed": false,
    "ping": "pong"
}
app-server-3 | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3"
    },
    "changed": false,
    "ping": "pong"
}
```

Now that we can confirm everything works correctly, we can go ahead with setting up the automation. This will be similar with the first exercise from `Week_12` - deploying the `quote-service` Spring Boot application to each of the app-server hosts natively (using local Java, not running inside a container).

The firewall rules were already configured as part of the Terraform setup, so we should be able to access the application using the default port defined in github (8220).

Create the following playbook.yaml file in the same ansible folder:
```
---
- name: Configure application hosts
  hosts: webservers
  become: true
  vars:
      - destdir: /apps/java
  tasks:

      - name : Install OpenJDK
        apt:
          name: openjdk-11-jdk
          state: present

      - name : Install git
        apt:
          name: git
          state: present

      - name: Download the source files from github
        become: yes
        git:
          repo: 'https://github.com/WebToLearn/fx-trading-app.git'
          dest: "{{ destdir }}"

      - name: Build the jar package
        shell:
          "chmod 755 mvnw && ./mvnw package -Pprod -DskipTests"
        args:
          chdir: "{{ destdir }}/App/quote-service"
        register: mvninstlout

      - name: Debug java build command
        debug: msg='{{mvninstlout.stdout_lines}}'

      - name: Start the App
        async: 10
        poll: 0
        shell:
          "(java -jar target/quote-service-0.0.1-SNAPSHOT.jar > quote-service.log 2>&1 &)"
        args:
          chdir: "{{ destdir }}/App/quote-service"
        register: appstart

      - name: Validating the port is open
        tags: javavalidate
        wait_for:
          host: "localhost"
          port: 8220
          delay: 10
          timeout: 30
          state: started
          msg: "quote-service application started"
```

Run the playbook, passing the local hosts configuration file:
```
ansible-playbook -i hosts playbook.yaml
```

The playbook should be pretty self-explaining, but as a summary it does the following, on `all` app-server hosts:
- sets up `destdir` variable to a predefined path where we will download and deploy the application from
- using apt ansible module makes sure openjdk 11 is installed (it will use the OS level apt package manager to installe it if not found)
- in a similar maner, it installs git on host
- fetches the java source files from github
- makes mvn wrapper executable and builds the application, packaging it as a jar file to the default path
- displays the build log to ansible console
- starts the application, sending all output to quote-service.log file, created in the application root path
- confirms that the application is started and listening on default port (8220)

You can see if the application is running correctly by either running curl from the ansible controller, or use the external IP and try to reach it from your local browser. As the firewall rules were configured during the Terraform step, it should work correctly.
<br />

## Exercise 4 - Jenkins Automation

### Install Jenkins on ansible-controller VM

#### Step 1. Update the System
Since we have a fresh installation of Debian 11, we need to update the package repository to its latest versions available:
```
sudo apt update
```

#### Step 2. Install Java
Jenkins is written in Java, and that is why we need the Java installed on our system along with some dependencies:
```
sudo apt install openjdk-11-jre -y
```
To check whether Java is installed execute the following command:
```
java -version
```
You should receive a similar output:
```
root@vps:~# java -version
openjdk version "11.0.14" 2022-01-18
OpenJDK Runtime Environment (build 11.0.14+9-post-Debian-1deb11u1)
OpenJDK 64-Bit Server VM (build 11.0.14+9-post-Debian-1deb11u1, mixed mode, sharing)
```

#### Step 3. Add Jenkins GPG key and PPA
By default the repository of Debian 11, does not contain Jenkins, so we need to add manually the key and the PPA.
```
curl -fsSL https://pkg.jenkins.io/debian/jenkins.io.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
```

Update the repository before you install Jenkins:
```
sudo apt update
```
Once, the system is updated with the latest packages, install Jenkins.

#### Step 4. Install Jenkins
```
sudo apt install jenkins -y
```
After the installation, start and enable the Jenkins service, in order for the service to start automatically after system reboot.
```
sudo systemctl start jenkins && sudo systemctl enable jenkins
```

To check the status of the service execute the following command:
```
sudo systemctl status jenkins
```

You should receive the following output:
```
● jenkins.service - Jenkins Continuous Integration Server
     Loaded: loaded (/lib/systemd/system/jenkins.service; enabled; vendor preset: enabled)
     Active: active (running) since Fri 2023-01-06 10:19:58 UTC; 36s ago
   Main PID: 11709 (java)
      Tasks: 47 (limit: 4693)
     Memory: 1.2G
        CPU: 59.256s
     CGroup: /system.slice/jenkins.service
             └─11709 /usr/bin/java -Djava.awt.headless=true -jar /usr/share/java/jenkins.war --webroot=/var/ca>

Jan 06 10:19:35 ansible-controller jenkins[11709]: 6b044d342f81407aa15980d2093379a0
Jan 06 10:19:35 ansible-controller jenkins[11709]: This may also be found at: /var/lib/jenkins/secrets/initial>
Jan 06 10:19:35 ansible-controller jenkins[11709]: ***********************************************************>
Jan 06 10:19:35 ansible-controller jenkins[11709]: ***********************************************************>
Jan 06 10:19:35 ansible-controller jenkins[11709]: ***********************************************************>
Jan 06 10:19:58 ansible-controller jenkins[11709]: 2023-01-06 10:19:58.402+0000 [id=30]        INFO        jen>
Jan 06 10:19:58 ansible-controller jenkins[11709]: 2023-01-06 10:19:58.439+0000 [id=22]        INFO        hud>
Jan 06 10:19:58 ansible-controller systemd[1]: Started Jenkins Continuous Integration Server.
Jan 06 10:19:59 ansible-controller jenkins[11709]: 2023-01-06 10:19:59.102+0000 [id=46]        INFO        h.m>
Jan 06 10:19:59 ansible-controller jenkins[11709]: 2023-01-06 10:19:59.104+0000 [id=46]        INFO        hud>
```

Another way to check if Jenkins, is active is checking the running processes:
```
ps auxwww | grep jenkins
```

You should receive the following output:
```
jenkins     5914 68.9 29.8 3647404 1201024 ?     Ssl  19:19   0:56 /usr/bin/java -Djava.awt.headless=true -jar /usr/share/java/jenkins.war --webroot=/var/cache/jenkins/war --httpPort=8080
```

#### Step 5. Finish Jenkins Installation
After successful installation we can finish the installation by accessing the Jenkins Web Interface:

http://YourServerIPaddress:8080


The Jenkins home page is asking the administrator in the order to be unlocked. To find the administrator password execute the following command:
```
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
The output of the administrator password will be as described below:
```
e5bcfa4486dd412f988a4762a8535aa3
```

Copy the administrator password, paste in the input of the Jenkins interface and click on the “Continue” button.

Go through the Jenkins install wizard with default values until you get to the ```Jenkins is ready!``` screen.

### Create your first Jenkins job

Although Jenkins is usually used to deploy and/or build applications on remote hosts - either using Jenkins agents, or other third party tools (ie Ansible), for today's example we will build and deploy straight on the same host where the Controller is running. This was decided both to save time and because authentication in GCP is usually handled using service accounts instead of plain ssh keys, thus complicating things outside the scope of this exercise.

Jenkins does not run as superuser, so we need to install the prerequisites on host beforehand. Because we will fetch the source files from github, we need to have the `git` binary available on host, so go ahead and install that first:
```
sudo apt install git -y
```
Also a JDK is needed to build the actual application:
```
sudo apt install openjdk-11-jdk -y
```

Go back to the Jenkins web page and create a new job:

- Click on `+ New Item` to create a new job
- Decide on a Job name and select Freestyle Project
- Under `Source Code Management`select Git and enter the fx-trading-app github URL: https://github.com/WebToLearn/fx-trading-app.git
- There are no credentials needed, as this is a public repository
- Check the available Build Triggers (questian mark icon next to each one), but don't select any of them - we will trigger the build manually 
- Under Build Steps select `Execute Shell`

The execute shell allows us to run a full bash script. In today's example we will simply use the same logic as before - build and deploy the quotes-service application.

Enter the following as the automation script:
```
# go to app root folder
cd App/quote-service

# remove any previous artifacts, if existing
rm -fr target/

# make mvn wrapper executable
chmod 755 mvnw

# run the maven build 
./mvnw package -Pprod -DskipTests

# start the application in the background, redirecting the console output to quote-service.log
java -jar target/quote-service-0.0.1-SNAPSHOT.jar  > quote-service.log 2>&1 &

# wait a few seconds for the application to start
sleep 10

# check the log to see if the application is started correctly
cat quote-service.log
```

As you might notice, we didn't need to run git commands, as the Job itself is configured to fetch the source repository on it's own.

### Cleanup

Make sure you run `terraform destroy` in your Cloud Shell instance, after you are done with the lab.
