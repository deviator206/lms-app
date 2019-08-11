-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: leadmanagement
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `departments` (
  `CODE` varchar(45) NOT NULL,
  `NAME` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES ('MARKETING','MARKETING'),('SALES','SALES');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_contact`
--

DROP TABLE IF EXISTS `lead_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lead_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(120) NOT NULL,
  `EMAIL` varchar(60) DEFAULT NULL,
  `PHONE` varchar(60) DEFAULT NULL,
  `COUNTRY` varchar(60) DEFAULT NULL,
  `STATE` varchar(60) DEFAULT NULL,
  `DESIGNATION` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_contact`
--

LOCK TABLES `lead_contact` WRITE;
/*!40000 ALTER TABLE `lead_contact` DISABLE KEYS */;
INSERT INTO `lead_contact` VALUES (1,'dingdong','a@b.com','9764007637','India','MH',NULL),(2,'dingdong','a@b.com','9764007637','India','MH',NULL),(3,'dingdong','a@b.com','9764007637','India','MH',NULL),(4,'dingdong','a@b.com','9764007637','India','MH',NULL),(5,'dingdong','a@b.com','9764007637','India','MH',NULL),(6,'dingdong','a@b.com','9764007637','India','MH',NULL);
/*!40000 ALTER TABLE `lead_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_status`
--

DROP TABLE IF EXISTS `lead_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lead_status` (
  `CODE` varchar(45) NOT NULL,
  `NAME` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_status`
--

LOCK TABLES `lead_status` WRITE;
/*!40000 ALTER TABLE `lead_status` DISABLE KEYS */;
INSERT INTO `lead_status` VALUES ('APPROVED','APPROVED'),('DRAFT','DRAFT'),('REJECTED','REJECTED');
/*!40000 ALTER TABLE `lead_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leads`
--

DROP TABLE IF EXISTS `leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `leads` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BU` varchar(45) DEFAULT NULL,
  `SALES_REP` varchar(60) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  `ROOT_ID` int(11) DEFAULT NULL,
  `DELETED` tinyint(4) NOT NULL DEFAULT '0',
  `CREATION_DATE` date DEFAULT NULL,
  `UPDATE_DATE` date DEFAULT NULL,
  `CREATOR_ID` int(11) DEFAULT NULL,
  `UPDATOR_ID` int(11) DEFAULT NULL,
  `BUDGET` decimal(12,4) DEFAULT NULL,
  `CURRENCY` varchar(45) DEFAULT NULL,
  `MESSAGE` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leads`
--

LOCK TABLES `leads` WRITE;
/*!40000 ALTER TABLE `leads` DISABLE KEYS */;
INSERT INTO `leads` VALUES (1,'marketing','shivanshu','DRAFT',1,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(2,'sales','shivanshu','DRAFT',1,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(3,'marketing','shivanshu','DRAFT',2,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(4,'sales','shivanshu','DRAFT',2,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(5,'marketing','shivanshu','DRAFT',3,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(6,'sales','shivanshu','DRAFT',3,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(7,'marketing','shivanshu','DRAFT',4,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(8,'sales','shivanshu','DRAFT',4,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(9,'marketing','shivanshu','DRAFT',5,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(10,'sales','shivanshu','DRAFT',5,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(11,'marketing','shivanshu','DRAFT',6,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL),(12,'sales','shivanshu','DRAFT',6,0,'2019-06-04','2019-06-04',123,123,0.0000,NULL,NULL);
/*!40000 ALTER TABLE `leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ref_data`
--

DROP TABLE IF EXISTS `ref_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ref_data` (
  `CODE` varchar(45) NOT NULL,
  `NAME` varchar(120) DEFAULT NULL,
  `TYPE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ref_data`
--

LOCK TABLES `ref_data` WRITE;
/*!40000 ALTER TABLE `ref_data` DISABLE KEYS */;
INSERT INTO `ref_data` VALUES ('DRAFT','DRAFT','LEAD_STATUS'),('DRAFT1','DRAFT','LEAD_STATUS1'),('MARKETING','MARKETING','DEPT'),('SALES','SALES','DEPT');
/*!40000 ALTER TABLE `ref_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `root_lead`
--

DROP TABLE IF EXISTS `root_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `root_lead` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SOURCE` varchar(60) DEFAULT NULL,
  `CUST_NAME` varchar(128) DEFAULT NULL,
  `DESCRIPTION` varchar(512) DEFAULT NULL,
  `CONTACT_ID` int(11) DEFAULT NULL,
  `DELETED` tinyint(4) NOT NULL DEFAULT '0',
  `CREATION_DATE` date DEFAULT NULL,
  `CREATOR_ID` int(11) DEFAULT NULL,
  `BUDGET` decimal(12,4) DEFAULT NULL,
  `CURRENCY` varchar(45) DEFAULT NULL,
  `SELF_APPROVED` tinyint(4) NOT NULL DEFAULT '0',
  `TENURE` varchar(120) DEFAULT NULL,
  `SALES_REP` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `root_lead`
--

LOCK TABLES `root_lead` WRITE;
/*!40000 ALTER TABLE `root_lead` DISABLE KEYS */;
INSERT INTO `root_lead` VALUES (1,'Marketing','shicv','dingDong',1,0,'2019-06-04',123,0.0000,NULL,0,NULL,NULL),(2,'Marketing','shicv','dingDong',2,0,'2019-06-04',123,0.0000,NULL,0,NULL,NULL),(3,'Marketing','shicv','dingDong',3,0,'2019-06-04',123,0.0000,NULL,0,NULL,NULL),(4,'Marketing','shicv','dingDong',4,0,'2019-06-04',123,0.0000,NULL,0,NULL,NULL),(5,'Marketing','shicv','dingDong',5,0,'2019-06-04',123,0.0000,NULL,0,'Less than one month',NULL),(6,'Marketing','shicv','dingDong',6,0,'2019-06-04',123,0.0000,NULL,0,'Less than one month','shivanshu');
/*!40000 ALTER TABLE `root_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(45) NOT NULL,
  `EMAIL` varchar(60) DEFAULT NULL,
  `PASSWORD` varchar(128) NOT NULL,
  `ENABLED` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'sbamane',NULL,'222',1),(3,'syadav',NULL,'password',1),(5,'syadav01',NULL,'$2a$10$gchuFwWzAtpcoPXQWloRWOs/j8O1N2CJAbO3XkbTfnMPKwxNltsZO',1),(7,'syadav02',NULL,'$2a$10$hDE/a65pXJ0OxPI4ckj//uvuT3X3mOo0YmUzO/.0ZObq0jYDx8Bxy',1),(8,'shiv.orian@gmail.com',NULL,'$2a$10$uMeysPPHWxSOsZ.Y2Bs1zO8hhU5n.dPdmAjaLuNAHzBLE.rTmq7yW',1),(9,'abc','a@b.com','$2a$10$G9c7eH8uvqxhl5LvziM7rOYK/2Wz0MkhOKNK6rxgydCDNF7iq5BS6',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-11 19:08:47
