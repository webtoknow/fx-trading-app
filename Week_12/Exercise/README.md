# DevOps exercise 
## Continous deployment

1. Write your first playbook
- Install Putty: https://the.earth.li/~sgtatham/putty/latest/w64/putty-64bit-0.70-installer.msi
- Open Putty
- Open Putty and connect to Ansible Controller
- Create your inventory file:
```
[webservers]
node1
```
- Start writing your first playbook:
```
---
- name: Install 
  become: yes
  hosts: webservers
  pre_tasks:
    - name: Stop Firewall
      service:
        name: firewalld
        state: stopped
  tasks:
    - name: Ensure latest version of apache2
      package:
        name: httpd
        state: latest
 
    - name: Ensure html page is installed
      copy:
        content: "some content here!"
        dest: /var/www/html/index.html

    - name: Enable apache2 service
      service:
        name: httpd
        state: started
        enabled: true

- name: Test
  become: no
  hosts: localhost
  tasks:
    - name: Connect to the server
      uri:
        url: "192.168.56.201:80"
        status_code: 200    
        return_content: yes
```
- Run your ansible playbook to your taget machine
```
ansible-playbook first_playbook.yml -i hosts
```

2. Deploy your webserver project
- Create the webserver role in order to install the httpd component and deploy your project using JINJA2 Template
- Create your playbook that will use the webserver role
- Run the created playbook to the target machine

3. Create a Jenkins job that will deploy your web application
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
