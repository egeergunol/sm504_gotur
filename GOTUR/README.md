# GOTUR
An online grocery shopping application implemented using SpringBoot, JPA, MySQL, Spring Security, Hibernate and Thymeleaf.

MESE Company, 2022, All Rights Reserved.


## Deploying to AWS EC2

Project can be run on AWS EC2 as described in this section. Assuming that you are using Ubuntu 20.04 image.

**Clone Repository to server instance:**

    git clone https://github.com/egeergunol/sm504_gotur.git
    cd sm504_gotur/GOTUR

### Dependency Installation

**Install MySQL Server:**

	sudo apt-get update
	sudo apt-get install mysql-server


**Install java8 and maven:**

    sudo apt-get install maven
    sudo apt install openjdk-8-jdk openjdk-8-jre

**If you are planning to use root user with empty password, first connect to mysql console and alter root user to allow connection with empty password:**

    mysql -u root -p
	ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '';

>This requires updating the database credentials in `src/main/resources/application.properties`

**On the server machine import the db with prepopulated data:**

	sudo mysql GOTUR_db < GOTUR_db.sql

You can check if the db is imported correctly

	mysql -u root -p
	use GOTUR_db
	show tables

You should see the correct tables are created.


### Running Code
You can simply run the code for testing with spring-boot plugin:

    mvn spring-boot:run

Running outside the ssh session:

	sudo apt-get install tmux
	tmux new -s session_name

Run your command and detach from tmux session with

    ctrl+b d

> Please note that inorder to be able to access your instance from the public internet, you need to give required ingress 
> and security group access to the instance on your VCP settings. 