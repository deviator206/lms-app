# lms-app

Database --------------------------------------------------------


insert into ref_data (code, name, type) select * from REF_DATA;

 drop table LEADS;
 drop table LEAD_CONTACT;
 drop table MI;
 drop table MI_INFO;
 drop table REF_DATA;
 drop table ROOT_LEAD;
 drop table USERS;
 drop table USER_ROLE;
 drop table NOTIFICATION;
 drop table NOTIFICATION_HISTORY;

ALTER TABLE leads RENAME TO LEADS;
ALTER TABLE lead_contact RENAME TO LEAD_CONTACT;
ALTER TABLE mi RENAME TO MI;
ALTER TABLE mi_info RENAME TO MI_INFO;
ALTER TABLE ref_data RENAME TO REF_DATA;
ALTER TABLE root_lead RENAME TO ROOT_LEAD;
ALTER TABLE users RENAME TO USERS;
ALTER TABLE user_role RENAME TO USER_ROLE;
ALTER TABLE notification RENAME TO NOTIFICATION;
ALTER TABLE notification_history RENAME TO NOTIFICATION_HISTORY;


test

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

--------------------------------------------------------
For Dashboard 

POST http://localhost:8080/statistics/lead?busummary=true&userid=1111
Payload -

{
  "salesRepId" : 1111
}

-------------------------------------------------------

To get Leads

GET http://localhost:8080/leads?leadtype=generated&userid=1111

Where leadtype=generated || assigned || both || all

---------------------------------------------------------------
-----------------------------------------
Pagination -

http://localhost:8080/search/leads?start=1&pagesize=2
http://localhost:8080/leads?start=1&pagesize=2

http://localhost:8080/marketIntelligence?start=1&pagesize=3
http://localhost:8080/search/marketIntelligence?start=1&pagesize=10

Where -
	start - is start index of row(starting from 1).
	pagesize - no of rows u want
	
-------------------------------------------

Attachment upload/download 

http://localhost:8080/lead/attachment/download?name=1.pptx&leadid=2
http://localhost:8080/lead/attachment/upload?leadid=1&leadid=3&userid=1111

http://localhost:8080/marketIntelligence/attachment/upload?id=1&userid=1111
http://localhost:8080/marketIntelligence/attachment/download?name=aa.txt&miid=1

---------------------------------------------------------------------------------
-----------------------------------------
Get MI Infor with Pagination 

GET http://localhost:8080/marketIntelligence/1/marketIntelligenceInfo?start=1&pagesize=2

---------------------------------------------------------------------------------
Get MI Details with some no of MI Infor

GET http://localhost:8080/marketIntelligence/1?infosize=2

------------------------------------------------------------------------------

Swagger Documentation -

http://localhost:8080/swagger-ui.html

-----------------------------------------------------------------------

Bug Fixes 

Status Changed -

{
  "id" : 3,
  "miInfoList" : [{
    "info" : "infonew1"
  }],
  "rootLeadId" : 36,
  "updatorId" : 1
}


--------------------------------
Contact Info in Get Leads pupulated

-----------------------------------
Creator in mi info returned

---------------------------------
Filters corrcted


{
  "tenure": "LT1Y",
  "country":"IND",
  "state" : "KAR"
}

-----------------------------------------------------------------
-----------------------------------------------------
Login

POST http://localhost:8080/login
Payload -
{"userName":"syadav1","password":"password"}

Response -
{"accessToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEzIiwiaWF0IjoxNTcwNTA2ODczLCJleHAiOjE1NzExMTE2NzN9.-jM1TvtjixlV82QkGSUbw1rev9lcKGWERamJ_wxwjkrt1D2ie_nwGa5-mlCeVobf6rS-NFxA7v517hIq_Dk53A","tokenType":"Bearer","userInfo":{"policies":{},"userId":1113,"userName":"syadav1","enabled":true,"userDisplayName":null,"businessUnit":null,"roles":["ADMIN1"]}}

----------------------------------------------------
Send Authorization Token in REST -
Authorization - Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEzIiwiaWF0IjoxNTcwMzk1MzI3LCJleHAiOjE1NzEwMDAxMjd9.FhSZRVRiS22FPJMBHXG_ZlW2fR4oZtPVBOyf3kpqjfnmbUZKluLuenkAYlXv_VNKZExvlmIqciZmnLjxz-zGCg

---------------------- To send Lead Status Report on Mail ----------
http://localhost:8080/statistics/lead?sendmail=true
---------------------------------------------------------------------




