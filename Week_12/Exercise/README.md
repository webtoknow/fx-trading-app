# Week 12 - Continous deployment exercises

## Table of contents

- [Exercise 1 - Create a VM in Google Cloud](#exercise-1---create-a-vm-in-google-cloud)
- [Exercise 2 - Deploy the quote-service application in VM](#exercise-2---deploy-the-quote-service-application-in-vm)
- [Exercise 3 - Install and configure Docker on a Debian 11 host](#exercise-3---install-and-configure-docker-on-a-debian-11-host)
- [Exercise 4 - Build and deploy the application in a Docker container](#exercise-4---build-and-deploy-the-application-in-a-docker-container)
- [Exercise 5 - Deploy quote-service as a serverless application](#exercise-5---deploy-quote-service-as-a-serverless-application)

<br/>

## Exercise 1 - Create a VM in Google Cloud

The most basic component in GCP, but most other cloud ecosystems as well, is the Virtual Machine. This replicates a physical server (with CPU, memory, disk, networking, etc) inside the internal network, corresponding to your GCP project.

Let's start with creating a new on so we can deploy one of the applications in fx-trading project.

Using the left hand side menu (three horizontal lines), select the following:
```
Compute Engine -> VM Instances
Create an instance -> New VM instance
```
Use the following details, leaving all else at default:
```
Name: fx-trading-host
Region: europe-west3 (Frankfurt)
Zone: europe-west3-c
Machine family: GENERAL-PURPOSE
Series: E2
Machine-type: e2-medium (2vCPU, 4 GB memory)
Do not select 'Enable display device'

Boot disk
Type: New balanced persistent disk
Size: 10 GB
Image: Debian GNU/Linux 11 (bullseye)

Enable both:
    Allow HTTP traffic
    Allow HTTPS traffic
```

Leave all other values as default and Click `CREATE`

Wait for the VM to boot up and when it is ready click on the SSH button right next to it.

<br/>

## Exercise 2 - Deploy the quote-service application in VM

First we need to install some prerequisites to build the Spring Boot application.

- Update the local apt package sources
```
sudo apt update
```

- Install openjdk, with a version matching what is defined in the pom.xml
```
sudo apt install -y openjdk-11-jdk
```

- Install git, required to fetch the application sources from github
```
sudo apt install -y git
```

- Fetch the github project to a local workspace
```
mkdir github && cd github
git clone https://github.com/WebToLearn/fx-trading-app.git
```

- Build the application and package it as jar file
```
cd fx-trading-app/App/quote-service/
chmod 755 mvnw
./mvnw package -Pprod -DskipTests
```

- Start the quote-service application
```
java -jar target/quote-service-0.0.1-SNAPSHOT.jar
```

- Open a second SSH window to the same host and try to see if the application reponds to http requests
```
curl http://localhost:8220/currencies
```

### Optional

Change the spring boot default port to 80, rebuild the application and restart it. Now you should be able to access it from your local PC, using the VM's `external ip`

<br/>

## Exercise 3 - Install and configure Docker on a Debian 11 host 

Official documentation used: https://docs.docker.com/engine/install/debian/

Connect to the VM running in Google Cloud using the 'SSH' button. This will open a new broser window to it's console. Run the following commands:

- Make sure to remove any existing previous docker installation)
```
sudo apt-get remove docker docker-engine docker.io containerd runc
```

- Update the local apt repository (default debian one)
```
sudo apt-get update
```

- Install prerequisites for adding official docker certificates and apt repo
```
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
```

- Add docker gpg key
```
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
```

- Add the official docker apt repository on local machine
```
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

- Update the package sources list and install docker
```
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

- Run the sample container to make sure all works fine

```
sudo docker run hello-world
```
Sample output:
  `Hello from Docker!`
  `This message shows that your installation appears to be working correctly.`

<br/>

## Exercise 4 - Build and deploy the application in a Docker container

- First install git:
```
sudo apt-get install git
```

- Clone application repository:
```
git clone https://github.com/WebToLearn/fx-trading-app.git
```

- Build docker image (adding an appropriate tag):
```
cd fx-trading-app/App/quote-service/
sudo docker build . --tag quote-service:0.0.1
```

- Check new image is available locally:
```
sudo docker images
```

Example output:
```
REPOSITORY      TAG                 IMAGE ID       CREATED              SIZE
quote-service   0.0.1               6b3bdd98ddee   About a minute ago   284MB
<none>          <none>              c691a1fc1ad1   About a minute ago   482MB
hello-world     latest              feb5d9fea6a5   14 months ago        13.3kB
maven           3.6.2-jdk-11-slim   f90eccee82af   3 years ago          418MB
openjdk         11.0.4-jre          fa6d3c4702db   3 years ago          267MB
```

- Start container, mapping the Spring boot defaul port to host VM:
```
sudo docker run -p 8220:8220 quote-service:0.0.1
```

- From a separate SSH session to the same VM, test that the application works correcty:
```
curl http://localhost:8220/currencies
```
Application should provide a json response, similar to `["EUR","USD","GBP","RON"]`

### Optional

You can map the container port onto a different port on the host. This would allow using default http/https ports and also match some firewall rules in the cloud, allowing external connections - for example from you local PC, via curl and even browser.

```
sudo docker run -p 80:8220 quote-service:0.0.1
```

To test the connection, copy the VMs `external IP` and use that from a network outside your GCP project. 

<br/>

## Exercise 5 - Deploy quote-service as a serverless application

This allows the user to deploy an application without setting up anything on the server. The feature is available in most Cloud ecosystems (it actually does everything behind the scenes for setting up the server) and enables a developer to have the application running with little to no system level setup. In GCP there are several flavours of serverless deployments, but the most basic and the one that is presented here is 'Cloud Run'

For a quick setup we can use the 'Cloud Shell'. This comes included with any GCP project and is basically a Debian VM with several tools already installed. It does not show up in the user configurable VM Instances and it does not cost anything to run. To start a new session click the 'Activate Cloud Shell' in the top right corner of Google Cloud Console.

Once the shell sessions is started and console is displayed in the bottom half of the screen, we can immediately start using it.

First, let's clone the application sources in a new folder:
```
mkdir github
cd github/ && git clone https://github.com/WebToLearn/fx-trading-app.git
```
Update the application to run on port 8080 (default for Cloud Run healthchecks)
```
cd fx-trading-app/App/quote-service/
sed -i 's/8220/8080/g' src/main/resources/application.properties
```

Make sure you have gcloud configured on the correct project
```
gcloud config get project
```

If not, set it to the correct one
```
gcloud config set project [REPLACE WITH OWN PROJECT_ID]
```

Deploy the application in Cloud Run
```
gcloud run deploy
```
- When you are prompted for the source code location, press Enter to deploy the current folder.
- When you are prompted for the service name, press Enter to accept the default name
- When you are prompted for region: select `europe-west3`
- When you are prompted to enable the Artifact Registry API, respond by pressing y
- You will be prompted to allow unauthenticated invocations: respond y 

Then wait a few moments until the deployment is complete. On success, the command line displays the service URL.

```
Done.
Service [quote-service] revision [quote-service-00002-wuv] has been deployed and is serving 100 percent of traffic.
Service URL: https://quote-service-hjizzxyvha-ey.a.run.app
```

