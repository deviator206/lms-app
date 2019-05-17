# lms-app

Database --------------------------------------------------------

Create database in MySQL -
CREATE DATABASE leadManagement;

Create tables -
USE  leadManagement;

CREATE  TABLE USERS (
ID INT NOT NULL AUTO_INCREMENT ,
USERID VARCHAR(45) NOT NULL UNIQUE,
EMAIL VARCHAR(60),  
PASSWORD VARCHAR(128) NOT NULL ,
ENABLED TINYINT NOT NULL DEFAULT 1 ,
PRIMARY KEY (ID));

Insert values - 

INSERT INTO USERS(USERID,PASSWORD,ENABLED)
VALUES ('syadav','111', true);
INSERT INTO users(USERID,PASSWORD,ENABLED)
VALUES ('sbamane','222', true);

REST Services --------------------------------------------------

To build - 
mvn clean package

To run -
mvn spring-boot:run

To debug -
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/lms-rest-services-0.1.0.jar

Then debug remote application through eclipse at port 8000
