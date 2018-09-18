-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: db_person
-- ------------------------------------------------------
-- Server version	8.0.12

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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT 'unkown',
  `description` varchar(150) DEFAULT NULL,
  `attentionCount` int(11) DEFAULT '0',
  `browseCount` int(11) DEFAULT '0',
  `discountPrice` float DEFAULT '0',
  `freezeStore` int(11) DEFAULT '0',
  `logo` varchar(20) DEFAULT 'unkown',
  `logoUrl` varchar(45) DEFAULT 'unkown',
  `marketPrice` float DEFAULT '0',
  `metaKeywords` varchar(45) DEFAULT 'unkown',
  `productType` varchar(30) DEFAULT 'unkown',
  `store` int(11) DEFAULT '0',
  `imageCount` int(11) DEFAULT '0',
  `isMarketable` int(11) DEFAULT '1',
  `isNew` int(11) NOT NULL DEFAULT '0',
  `hotSales` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'1001','小米背包1','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,1,0),(2,'1002','小米背包2','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2),(3,'1003','小米背包3','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,1,0),(4,'1004','小米背包4','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2),(5,'1005','小米背包5','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,1,0),(6,'1006','小米背包6','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2),(7,'1007','小米背包7','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,1,0),(8,'1008','小米背包8','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2),(9,'1009','小米背包9','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,1,0),(10,'10010','小米背包10','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2),(11,'10011','小米背包11','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,0),(12,'10012','小米背包12','单肩背包',0,0,8.8,0,NULL,NULL,10,'包，服饰','',100,3,1,0,2);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `score` int(11) DEFAULT '0',
  `age` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'13603021940','风清扬','999999',0,0),(3,'13603021941','东方不败','123456',0,0),(5,'13603021942','向问天','123456',0,0),(2,'13603021943','任我行','123456',0,0),(4,'13603021944','令狐冲','123456',0,0),(6,'13603021941',NULL,'520',0,0),(7,'13602031941',NULL,'520123',0,0),(8,'13603021945',NULL,'123456',0,0);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-18 19:55:30
