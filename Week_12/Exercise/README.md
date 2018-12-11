# DevOps exercise 
## Continous deployment

1. Login to Amazon (https://eu-central-1.console.aws.amazon.com/ec2/v2/home):
EC2 Dashboard -> Launch Instance, select Amazon Linux 2 AMI (HVM), Next: Configure Instance Details
Set number of instances to 3, Next: Add Storage, Next: Add tags, Next: Configure Security Group
Add Custom TCP Rule for port 8080, Source: Anywhere, Review and Launch, Launch
Select Create a new key pair and save the key, Launch Instances.
View Instances and add tags on Name column: master_node, dev_node and sit_node.

Install Putty: https://the.earth.li/~sgtatham/putty/latest/w64/putty-64bit-0.70-installer.msi
Open Puttygen
File->Load private key and select the key provided by Amazon.
Save private key as .ppk

Open Putty and create connections to all 3 machines (Use Public IPs and go to Connection->SSH->Auth and browse to .ppk key)

- Run below commands to set a simpler hostname on all 3 hosts:

```bash
sudo -i
vi /etc/hostname
replace hostname (e.g. ip-172-31-44-160.eu-central-1.compute.internal -> master-node.eu-central-1.compute.internal)
hostname -F /etc/hostname
logout
sudo -i
```

- Get the IP of each host by running the command: ifconfig eth0 | grep -w 'inet' | awk '{print $2}'
and update /etc/hosts file on master-node, as in below example:

127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost6 localhost6.localdomain6
172.31.44.160 master-node
172.31.45.114 dev-node
172.31.42.203 sit-node

- Generate ssh keys on master, as ec2-user
su - ec2-user
cd ~/.ssh
ssh-keygen -t rsa
cat id_rsa.pub >> authorized_keys

- Create a new key file with the content of .ppk key provided by Amazon and name it as amazon.pem (in ~root/.ssh)
vi amazon.pem
chmod 400 amazon.pem

- Run below commands on master to copy the keys on the remaining hosts
scp -i amazon.pem -p * dev-node:~/.ssh
scp -i amazon.pem -p * sit-node:~/.ssh
ssh master-node "sudo cp .ssh/* ~root/.ssh"
ssh dev-node "sudo cp .ssh/* ~root/.ssh"
ssh sit-node "sudo cp .ssh/* ~root/.ssh"
sudo -i
scp /etc/hosts dev-node:/etc
scp /etc/hosts sit-node:/etc

Passwordless ssh is set

----------------------------------------------------------------------------------------------------------------
Run below commands to install the required softaware on all node (java, tomcat, jenkins)
sudo -i
sudo amazon-linux-extras install -y ansible2
sed -i 's|#inventory      = /etc/ansible/hosts|inventory      = /root/ansible/hosts|' /etc/ansible/ansible.cfg
mkdir ansible
vi ~root/ansible/hosts and add:
[master]
master-node
[nodes]
dev-node
sit-node

- vi install_node.sh and copy below content:
echo "Installing jdk"
yum install -y java-1.8.0-openjdk

echo "Installing tomcat"
wget https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35.tar.gz;
sleep 3
tar xvf apache-tomcat-8.5.35.tar.gz

echo '<Context privileged="true" antiResourceLocking="false"
         docBase="${catalina.home}/webapps/manager">
    <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="^.*$" />
</Context>' > manager.xml
cp manager.xml /root/apache-tomcat-8.5.35/conf/Catalina/localhost/

sed -i '/<\/tomcat-users>/i <user username="deploy" password="deploy" roles="manager-script"/>' /root/apache-tomcat-8.5.35/conf/tomcat-users.xml;
cp manager.xml /root/apache-tomcat-8.5.35/conf/Catalina/localhost/
sleep 3
/root/apache-tomcat-8.5.35/bin/startup.sh

- vi install_master.sh and copy below content:

echo "Installing jdk"
yum install -y java-1.8.0-openjdk

echo "installing Git"
yum install -y git

echo "installing Jenkins"
sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat-stable/jenkins.repo
sudo rpm --import https://jenkins-ci.org/redhat/jenkins-ci.org.key
sudo yum install -y  jenkins

mkdir /jenkins
chmod 777 /jenkins
export JENKINS_HOME=/jenkins >> ~/.bash_profile
. ~/.bash_profile
sed -i 's|/var/lib/jenkins|/jenkins|' /etc/passwd
sed -i 's|JENKINS_HOME="/var/lib/jenkins"|JENKINS_HOME="/jenkins"|' /etc/sysconfig/jenkins

service jenkins restart

- vi install_nodes.sh and copy below content:

echo "Installing jdk"
yum install -y java-1.8.0-openjdk

echo "Installing tomcat"
wget https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35.tar.gz;
sleep 3
tar xvf apache-tomcat-8.5.35.tar.gz

echo '<Context privileged="true" antiResourceLocking="false"
         docBase="${catalina.home}/webapps/manager">
    <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="^.*$" />
</Context>' > manager.xml
mkdir -p apache-tomcat-8.5.35/conf/Catalina/localhost/
cp manager.xml /root/apache-tomcat-8.5.35/conf/Catalina/localhost/

sed -i '/<\/tomcat-users>/i <user username="deploy" password="deploy" roles="manager-script"/>' /root/apache-tomcat-8.5.35/conf/tomcat-users.xml;
sleep 3
/root/apache-tomcat-8.5.35/bin/startup.sh

Run below commands on master
chmod 744 install_master.sh install_nodes.sh
ansible nodes -m shell -a "mkdir ansible;scp -p master-node:~/ansible/*.sh ~/ansible"
ansible master -m shell -a "~/ansible/install_master.sh"
ansible nodes -m shell -a "~/ansible/install_nodes.sh"

------------------------------------------------------------------------------------
Verify tomcat status on dev-node and sit-node:
http://<dev-node-public-ip>:8080
http://<sit-node-public-ip>:8080
  
  
------------------------------------------------------------------------------------
Access Jenkins url:
http://<master-node-public-ip>:8080
  
Login as admin and get the password from /jenkins/secrets/initialAdminPassword file

Select "Select plugins to install"
Select none and Install
Continue as admin
Save and finish
Start using Jenkins
  
Click Manage Jenkins -> Manage Plugins -> Available tab
Select "Deploy to container" and Download
Repeat for "Delivery Pipeline" and "Copy data to workspace" plugin

Download GIt plugin:
https://updates.jenkins.io/download/plugins/git/3.9.1/git.hpi
Click Manage Jenkins -> Manage Plugins -> Advanced tab and upload git.hpi file

Restart Jenkins

--------------------------------------------------------------------------------------

Click new Item, set name as Build, Freestyle project, OK
Click new Item, set name as DeployDEV, Freestyle project, OK
Click new Item, set name as Test, Freestyle project, OK
Click new Item, set name as DeploySIT, Freestyle project, OK

1. Edit Build
Source Code Management: Set GitHub repo URL
Build triggers: Poll SCM and Scheduler H/5 * * * *
Post build actions: Add post-build action->Build other projects and select DeployDEV

2. Edit DeployDEV
Check Copy data to workspace and set path to: /jenkins/workspace/Build/
Post build actions: Deploy ear/war to container
 EAR/WAR files: **/*.ear
Context path: <application context>
Tomcat URL: http://<dev-node-public-ip>:8080
Set credentials: deploy/deploy
Post build actions: Add post-build action->Build other projects and select Test
  
3. Edit Test
Build->Execute shell and add below text:
date
echo Test
curl http://<dev-node-public-ip>:8080/HWS/hi | grep Hello
Post build actions: Add post-build action->Build other projects and select DeploySIT

3. Edit DeploySIT
Check Copy data to workspace and set path to: /jenkins/workspace/Build/
Post build actions: Deploy ear/war to container
 EAR/WAR files: **/*.ear
Context path: <application context>
Tomcat URL: http://<sit-node-public-ip>:8080
Set credentials: deploy/deploy

Click on "+" near All, set view name: DeliveryPipeline and select DeliveryPipelineView, OK
On the bottom side, Pipelines: Add Components and select the first job (Build)

-----------------------------------
Run Build job using Build Now button
