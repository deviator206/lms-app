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
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `ORIGINATING_BU` varchar(120) DEFAULT NULL,
  `SALES_REP_ID` int(11) DEFAULT NULL,
  `ATTACHMENT` varchar(4000) DEFAULT NULL,
  `INDUSTRY` varchar(528) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mi`
--

DROP TABLE IF EXISTS `mi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `mi` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(120) DEFAULT NULL,
  `STATUS` varchar(120) DEFAULT NULL,
  `NAME` varchar(1024) DEFAULT NULL,
  `DESCRIPTION` text,
  `LEAD_ID` int(11) DEFAULT NULL,
  `INVESTMENT` decimal(12,4) DEFAULT NULL,
  `CREATION_DATE` date DEFAULT NULL,
  `CREATOR_ID` int(11) DEFAULT NULL,
  `UPDATOR_ID` int(11) DEFAULT NULL,
  `UPDATE_DATE` date DEFAULT NULL,
  `ATTACHMENT` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mi_info`
--

DROP TABLE IF EXISTS `mi_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `mi_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MI_ID` int(11) DEFAULT NULL,
  `INFO` text,
  `CREATION_DATE` date DEFAULT NULL,
  `CREATOR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `notification` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` int(11) NOT NULL,
  `NOTIFICATION_KEY` text,
  `ENABLED` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `SOURCE_INFO` text,
  `ATTACHMENT` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(60) NOT NULL,
  `USER_ROLE` varchar(60) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `USER_DISPLAY_NAME` varchar(1024) DEFAULT NULL,
  `BU` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-13 13:51:34
