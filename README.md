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

------------------------------------------
Login
http://localhost:8080/lms-rest-services-0.1.0/login

{
		"userName" : "shiv.orian@gmail.com",
		"password" : "password"
}


{
"accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNTY1MzYxNzkxLCJleHAiOjE1NjU5NjY1OTF9.tfFn5ArJvYXpB_DmkhaxVdnkMzQRNXRrLNSZwksLn-PNMRx_1QnKpUuysGgI-MdNLTrfIUfr2brI4NF96Bn2yw",
"tokenType": "Bearer"
}

-------------------------------------------

Create ref data

POST http://localhost:8080/refdata

Payload

{
  "code": "DRAFT",
  "name": "DRAFT",
  "type": "LEAD_STATUS"
}
-------------------------------------------------

Create Lead

POST http://localhost:8080/rootlead
{
  "source": "Marketing",
  "custName": "shicv",
  "description": "dingDong",
  "tenure": "Less than one month",
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
-------------------------------------------
Get Leads 

GET http://localhost:8080/leads

---------------------------------------------
Search Ref Data with type

http://localhost:8080/refdata?type=LEAD_STATUS,DEPT

--------------------------------------------------
Get Users
http://localhost:8080/users

------------------------------------------------
Create User

POST http://localhost:8080/user

{
"userName" : "abc",
"password" : "abc",
"email":"a@b.com"
}

-------------------------------------------------------------------
Get ref data

GET http://localhost:8080/refdata?type=LEAD_STATUS

--------------------------------------------------------------

---------------------------------------------------------------
Replace User Roles 

http://localhost:8080/user/roles

{
  "userId": 7,
  "roles": ["ADMIN"]
}

-----------------------------------------------------------------

http://localhost:8080/login
{
  "userName": "shiv.orian@gmail.com",
  "password": "password"
}

{
"accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNTY1ODkwOTExLCJleHAiOjE1NjY0OTU3MTF9.b5-Et26z4sRE6VnTCaoTUYidy0CDTjj2N2hSL3suulGAq_153-nOpnSKBRJxI1xwdCPYfpqrY_Mz5P2mLjcS0Q",
"tokenType": "Bearer",
"userInfo": {
"policies": {
"salesRepList": "DISABLE",
"userList": "SHOW"
},
"userId": 8,
"userName": "shiv.orian@gmail.com"
}
}

----------------------------------------------------------------------

------------------------------------------------------------------------------
POST http://localhost:8080/leads

{
  "status": "DRAFT",
  "custName" : "shicv2"
}

Currenty only customer name and status is supported.

-----------------------------------------------------------------------------
------------------------------------------------------------------------------
Get Market Intelligence
GET http://localhost:8080/marketIntelligence

[
  {
    "id": 1,
    "type": "project",
    "name": "new project",
    "description": null,
    "status": null,
    "investment": null,
    "rootLeadId": null,
    "rootLead": null,
    "creationDate": null,
    "miInfoList": null
  },
  {
    "id": 4,
    "type": "project6",
    "name": "new projec6t",
    "description": "cccc6",
    "status": null,
    "investment": 6,
    "rootLeadId": 4,
    "rootLead": null,
    "creationDate": "2019-08-24",
    "miInfoList": null
  }
]

-----------------------------------------------------------------------------

Create  Market Intelligence
POST http://localhost:8080/marketIntelligence

{
"type" : "project6",
 "name": "new projec6t",
  "investment" : 6,
  "description" : "cccc6"
}

-----------------------------------------------------------------------------
Update Market Intelligence
POST http://localhost:8080/marketIntelligence/{id}

{
"id" : 4,
  "miInfoList" : [{
    "info" : "info4"
  }],
  "rootLead" : {
    "custName" : "shivashu4"
  }
}

----------------------------------------------------------------------------------
Create  Market Intelligence by id with info List

GET http://localhost:8080/marketIntelligence/{id}

{
"id": 1,
"type": "project",
"name": "new project",
"description": null,
"status": null,
"investment": null,
"rootLeadId": null,
"rootLead": null,
"creationDate": null,
"miInfoList": [
  {
"id": 1,
"miId": 1,
"info": "info1"
},
  {
"id": 5,
"miId": 1,
"info": "info2"
}
],
}
---------------------------------------------------------------------------------------
---------------------------------------------------------------------
Filter Market Intelligence
POST http://localhost:8080/search/marketIntelligence
Payload -

{
"name" : "new"
}
--------------------------------------------------------------------
Forgot Password (Dummy Response)
POST http://localhost:8080/forgotpassword

Payload -

{
  "email": "shiv"
}

Response -

{
"email": "shiv",
"userName": null,
"userDisplayName": null,
"forgotPasswordUri": "https://testuri"
}

-------------------------------------------------------------------

Filter Leads 

POST http://localhost:8080/leads

Payload -

{
  "fromBu" : "marketing1"
}

--------------------------------------------------------------------
--------------------------------------------------------
Search Market Intelligence from  name and description

POST http://localhost:8080/search/marketIntelligence

Payload -

{
	"searchText" : ""
}

-------------------------------------------------------

Update Market Intelligence  with Root Lead Id

POST http://localhost:8080/marketIntelligence/{id}

Payload -

{
  "id" : 4,
  "rootLeadId" : "44544"
}

-----------------------------------------------------

Get Lead Statistics 

GET http://localhost:8080/statistics/lead

Response -

{
	"leadStatusCountMap": {
		"CLOSED": 1,
		"DRAFT": 16,
		"REJECTED": 2,
		"OPEN": 1
	}
}

-------------------------------------------------------

