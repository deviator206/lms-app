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

