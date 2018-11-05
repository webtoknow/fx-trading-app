# Week 7 - Development : software security
### Table of contents

- [Security in today context] (#security-in-today-context)
	+ [Digital security] (#digital-security)
	+ [Application security] (#application-security)
	+ [Software security] (#software-security)
- [Spring Security] (#spring-security)
	+ What is it
	+ Main features
	+ Authentication
	+ Authorizaiton
	+ Building blocks
- [JWT approach] (#jwt_approach)
	- ??	

- [References] (#references)

## Security in today context

> One interesting thing in today world is that every commercial or non-commercial undertaken is running towards being a software developing entity. Almost every firm is now to some extent a software development company, pretty much all universities are now creating software for everyday activities. 

> If you consider that an increasing part of today code is running in human free environment you can start imagine the criticality of small things like your washing machine working against you. What about someone hacking into an electrical power plant and messing it up for a whole city?

> A good thing is that today almost everyone is investing in anti-virus and firewall solutions. The catch is that these two types of software do not really protect applications.

### Digital security

> And, what is digital security?

> *The right tools.*
An increasing number of devops and developers are familiar with security but still a good percentage needs to operate with tools that they do not know inside-out. So the right tools need to be at hand, easy to use even for unfamiliar users.

> *The right feedback.*
Earliest feedback is most of the time the best feedback. You need to know as soon as possible if you are doing something wrong. While you are operate systems or develop system add tools that give you the right feedback, the immediate one. Lack of security is like any other error in the application. Is not so much different comparing to a StackOverflow or ArrayOutOfBound exceptions. Use: Slack, Hipchat, Jira, Maven, Jenkins, SIEM, PagerDuty. TODO: filter these samples;

> *The right detection*
There are multiple detection approaches these days. Some are good but they lack automation. Some are good and allow automation but are not used. How any of you have heard about SonarQube? How many used it?

> *The right protection* 
If a good protection is to hire some guys to guard your city a better protection is to invest time in learning everyone how to protect and work together with the formers. Same is for applications and is called 'runtime application self-protection' (RASP).  It uses the advantages of having acccess to application input and logic to block the attacks from inside by terminating user session or even shutdown the application.

> The right defense
Scanning the code for the smelling parts is a good step ahead in adding a good level of application security. A more positive approach would be to use tools the enforce security patterns while you are writing it. TODO: add samples of security patterns and tools helping this enforcement. 

### Application security

> But, what is application security? Is anything and anyone that is trying to protect an application **after** it was created. See below a few approaches.

> *Static application security testing.* 
It is called white-box/white-hat testing. Is about searching for known patterns of vulnerabilities and defects in the source code and log the warnings. For these activities the researcher has *prior" knowledge about the application. Here you can choose tools like [SonarQube](https://hub.docker.com/r/owasp/sonarqube/) from OWASP. 
 
> *Dynamic application security testing.* 
It is also called black-box/black-hat security testing. It is about searching for vulnerabilities on a running code, on a live application. Any suspicious behavior of the application is logged. Hence for the SAST and DAST there is a pretty high volume of false positives reported. Tools that can be used are [ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project) or [Arachni](http://www.arachni-scanner.com/).

> *Interactive application security testing.* 
This is a mix of other testing techniques, including SAST and DAST. It uses the advantages of knowing the application data flows and business flows to create advance attack scenarios. The use of recursive dynamic analysis is usually combined with machine learning so the tool will get smarter while testing your application. One of its important aims is to reduce the number of false positive has the wasted time on wrong investigations. It is designed to work in Agile environments with DevOps support.

### Software security

> Software security is about creating software that is secure but itself and continue to stay like this during its entire life from development to production. 

> As a software developer you are desiging your software to be secure from scratch. 
> As a software developer you allways validate your user's input and use the right encoding for the input.
> As a software developer you handle user authentication and authorization.
> As a software developer you handle correctly user sessions.
> As a software developer you use the strongest available cryptography to secure data at rest and in transit.
> As a software developer you ensure that all third party components are validated agaings your entity/company policies. 
> As a software developer you challenge any flaws in software desgin or architecture. 
> As a software developer you have and improve recurrently the 'Secure coding guidelines' for developers. The entire internal development community is participating here.
> As a software developer you helpp your colleagues in DevOvs and Release Management to secure the configuration procedures and to securely deploy your application.

> One the most unknown field of today software development is security desgin patterns. Let's add a few here:
>> * Single access point for login in. 
>> * Clear distinction between authentication and authorization, also known as access control.
>> * Alwasy use sessions to isolate information in muti-user environment.
>> * Limit the view using the principle 'access is allowed per need'. Default user has very limited access, usually at login page, general policy guide, user rights page and alike.
>> * Work with mylti layered security. Firewalls, anti-viruses, self-protection, etc.
>> * Sanitaze your data by removing expired, duplicate or unnecessary data.
>> * Design to fail in a secure manener.

## Spring Security

### What is it
> Spring Security initially started as a continuation of Acegi Security. A good tool with a bit too complicated configuration via XML. Since Sprint 2.0 it was embeded into Spring Framework and continuously improved. Nowadays the security configuration can be done via annotation directly in Java classes.

### Main features
> Spring Security helps with the two major areas of access control: authenticationa and authorization.

### Authentication
> Authentication is about assurring that the entity/principal called in Spring accessing your protected resources is allowed to access it. First step is to identify the principal. Simplest form of identification is via username and password. Others, more sophisticated ways, are via LDAP (Lightweight Directory Access Protocal) or via CAS (Central Authentication Service) which is a single sign-on protocol.
> Authentications supported:
>> HTTP Basic
>> Form-based authenticataion
>> LDAP 
>> Java Authentication and Authorization Service
>> Kerberos

### Authorization
> If the authenticaton is successful the principal goes to the second step. Now that we know who our user is what are the actions that is allowed to perform? Which are the areas where is allowed to navigate? If you are an admin you are probably allowed to do whatever you want, if you are an user you probably has much more limited access. 
> This access control is called user role authorization. Usually this is done via URL controll but is not the tightest security in place. Spring introduced method-level security which means that you have to be authorized to execute that Java method. You can hack the URL and try to execute again but method level authorization will block you.

### Building blocks
> 

## JWT approach


### References

[cgisecurity.com]
[contrastsecurity.com]
[developer.com]
[jwt.io]
[martinfowler.com]
[owasp.org]
[spring.io]
[synopsys.com]
