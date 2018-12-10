# Week 12 - Devops

## Release and Deployment Management - DevOps

### The goal of release and deployment management

- Release and deployment management aims to build, test and deliver services to the customers specified by service
design.

- The goal of release and deployment management is to deploy releases into operation and establish effective use of
the service in order to deliver value to the customer

- Release and deployment management also ensures handover to service operations takes place and that suitable
training and documentation exists to ensure ongoing support of the new service.
![Deployemnt](http://www.datacenterjournal.com/wp-content/uploads/2018/04/plutoradevops.jpg  "Deployemnt")


## What is High Availability ? 

With an increased demand for reliable and performant infrastructures designed to serve critical systems, the terms scalability and high availability couldn’t be more popular. While handling increased system load is a common concern, decreasing downtime and eliminating single points of failure are just as important. High availability is a quality of infrastructure design at scale that addresses these latter considerations.

In computing, the term availability is used to describe the period of time when a service is available, as well as the time required by a system to respond to a request made by a user. High availability is a quality of a system or component that assures a high level of operational performance for a given period of time.

One of the goals of high availability is to eliminate single points of failure in your infrastructure. A single point of failure is a component of your technology stack that would cause a service interruption if it became unavailable. As such, any component that is a requisite for the proper functionality of your application that does not have redundancy is considered to be a single point of failure.

https://assets.digitalocean.com/articles/high_availability/ha-diagram-animated.gif 

https://docs.aws.amazon.com/quickstart/latest/sharepoint/images/arch-traditional.png

## Deployment Strategies 

A deployment strategy is a way to change or upgrade an application. The aim is to make the change without downtime in a way that the user barely notices the improvements.

Now that we live in the DevOps era, deployments are also different in the industry. Some organizations do several deployments per day. There are some strategies to increase the frequency of deployments that help to reduce the risks of downtime without customers noticing that something changed.

### Blue / Green Deployment 

The blue/green deployment strategy consists of having two production versions of a particular system running at the same time. The catch is that only one is receiving live traffic. This means that the new version doesn’t necessarily have to be backward compatible because the blue and green deployments won’t be live at the same time. It represents a challenge for app dependencies like databases, but you can use the blue/green strategy for them too.

https://storage.googleapis.com/cdn.thenewstack.io/media/2017/11/73a2824d-blue-green.gif 

https://rollout.io/blog/blue-green-deployment/


### Rolling deployment

Rolling deployments are a pattern whereby, instead of deploying a package to all servers at once, we slowly roll out the release by deploying it to each server one-by-one. In load balanced scenarios, this allows us to reduce overall downtime.

https://storage.googleapis.com/cdn.thenewstack.io/media/2017/11/5bddc931-ramped.gif

### Canary deployment 

A canary deployment consists of gradually shifting production traffic from version A to version B. Usually the traffic is split based on weight. For example, 90 percent of the requests go to version A, 10 percent go to version B.

This technique is mostly used when the tests are lacking or not reliable or if there is little confidence about the stability of the new release on the platform.

https://storage.googleapis.com/cdn.thenewstack.io/media/2017/11/a6324354-canary.gif


### Depending on the Cloud provider or platform, the following docs can be a good start to understand deployment:

Amazon Web Services  https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-attribute-updatepolicy.html 
Docker Swarm https://docs.docker.com/engine/swarm/swarm-tutorial/rolling-update/
Kubernetes https://kubernetes.io/docs/tutorials/kubernetes-basics/update/update-intro/ 
