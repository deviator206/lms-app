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

Test at - http://localhost:8080/greeting

For Leads Feature --------
Create relevant tables


CREATE TABLE LEAD_CONTACT (
ID INT(11) NOT NULL AUTO_INCREMENT,
NAME VARCHAR(120) NOT NULL,
EMAIL VARCHAR(60),
PHONE VARCHAR(60),
COUNTRY VARCHAR(60),
STATE VARCHAR(60), 
PRIMARY KEY (ID));


CREATE TABLE ROOT_LEAD (
ID INT NOT NULL AUTO_INCREMENT,
SOURCE VARCHAR(60) DEFAULT NULL,
CUST_NAME VARCHAR(128) DEFAULT NULL,
DESCRIPTION VARCHAR(512) DEFAULT NULL,
CONTACT_ID INT,
DELETED TINYINT(4) NOT NULL DEFAULT 0,
CREATION_DATE DATE,
CREATOR_ID INT(11),
PRIMARY KEY (ID));

CREATE TABLE LEADS (
ID INT(11) NOT NULL AUTO_INCREMENT,
BU VARCHAR(45) NOT NULL,
STATUS VARCHAR(45) NOT NULL,
ROOT_ID INT(11) NOT NULL,
DELETED TINYINT(4) NOT NULL DEFAULT 0,
CREATION_DATE DATE NOT NULL,
UPDATE_DATE DATE NOT NULL,
CREATOR_ID INT(11) NOT NULL,
UPDATOR_ID INT(11) NOT NULL,
PRIMARY KEY (ID))

To create root lead

POST http://localhost:8080/rootlead

payload -

{
  "id": 1,
  "source": "Marketing",
  "custName": "shicv",
  "description": "dingDong",
  "leadContact": {
    "name": "dingdong",
    "email": "a@b.com",
    "phoneNumber": "9764007637",
    "country": "India",
    "state": "MH"
  },
  "leadsSummaryRes": {
    "businessUnits": [
      "marketing",
      "sales"
    ],
    "salesRep": "shivanshu",
    "industry": "it"
  },
  "deleted": false,
  "creatorId": "123",
  "creationDate": "2019-06-04"
}

To get root lead

GET http://localhost:8080/rootlead/{lead-id}

To update Lead

PUT http://localhost:8080/lead/4

payload

{
"id": 4,
"businessUnit": "marketing",
"status": "DRAFT",
"rootLeadId": 7,
"salesRep": "shivanshu",
"industry": null,
"deleted": false,
"creationDate": "2019-06-04",
"creatorId": 123,
"updateDate": "2019-06-04",
"updatorId": 123
}

-----------------------------------------------------------------

Reference data Feature

---------------------------------------------

Create tables

CREATE  TABLE REF_DATA (
CODE VARCHAR(45) NOT NULL UNIQUE,
NAME VARCHAR(120),
TYPE VARCHAR(45),
PRIMARY KEY (CODE));

Create ref data

POST http://localhost:8080/refdata

Payload

{
  "code": "DRAFT",
  "name": "DRAFT",
  "type": "LEAD_STATUS"
}

Get ref data

GET http://localhost:8080/refdata?type=LEAD_STATUS




