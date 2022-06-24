CREATE DATABASE  IF NOT EXISTS `musicstreaming` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `musicstreaming`;
-- MariaDB dump 10.17  Distrib 10.5.6-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: musicstreaming
-- ------------------------------------------------------
-- Server version	10.5.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `play`
--

DROP TABLE IF EXISTS `play`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play` (
  `play_음원관리번호` int(11) NOT NULL,
  `play_구독자주민번호` char(13) NOT NULL,
  `p_count` int(11) DEFAULT 0,
  PRIMARY KEY (`play_음원관리번호`,`play_구독자주민번호`),
  KEY `play_FK` (`play_구독자주민번호`),
  CONSTRAINT `play_FK` FOREIGN KEY (`play_구독자주민번호`) REFERENCES `스트리밍구독자` (`구독자주민번호`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `play_FK_1` FOREIGN KEY (`play_음원관리번호`) REFERENCES `음원` (`음원관리번호`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play`
--

LOCK TABLES `play` WRITE;
/*!40000 ALTER TABLE `play` DISABLE KEYS */;
INSERT INTO `play` VALUES (1,'9501121111111',1),(3,'9504291111111',12),(4,'9501121111111',13),(5,'9501121111111',14),(6,'9501121111111',3),(28,'9501121111111',1),(37,'9501121111111',13),(41,'9501121111111',1);
/*!40000 ALTER TABLE `play` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `스트리밍구독자`
--

DROP TABLE IF EXISTS `스트리밍구독자`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `스트리밍구독자` (
  `성별` varchar(5) DEFAULT NULL,
  `주소` varchar(60) DEFAULT NULL,
  `핸드폰번호` varchar(11) DEFAULT NULL,
  `구독자주민번호` char(13) NOT NULL,
  `구독_관리자주민번호` char(13) DEFAULT NULL,
  `구독개월수` int(11) DEFAULT 0,
  `구독시작날짜` date DEFAULT NULL,
  `passwd` varchar(80) NOT NULL,
  `성명` varchar(40) NOT NULL,
  `ID` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`구독자주민번호`),
  UNIQUE KEY `ID` (`ID`),
  KEY `스트리밍구독자_FK` (`구독_관리자주민번호`),
  CONSTRAINT `스트리밍구독자_FK` FOREIGN KEY (`구독_관리자주민번호`) REFERENCES `음원관리자` (`관리자주민번호`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `스트리밍구독자`
--

LOCK TABLES `스트리밍구독자` WRITE;
/*!40000 ALTER TABLE `스트리밍구독자` DISABLE KEYS */;
INSERT INTO `스트리밍구독자` VALUES ('M','경기도 고양시 호수로','01038275421','0011071111111','9605171111111',3,'2020-11-27','0027fbbf74da9448e528797e7962f693725d182f74e8183e3025a4ebe0278dd5','최원석','cwon1107'),('M','고양시 백석동','01072259999','9501121111111','9605171111111',3,'2020-10-13','a8e91c8984b870455be33e19524069dbf19c667b777d66e14dff2f51832c068e','최진영','captain9981'),('F','고양시 호수로','01030229999','9504291111111','9605171111111',6,'2020-11-25','b35307e7e44f3219a1cb97cccce67478252a7a48271fcbaf2caeb39390280793','김현신','khs0313'),('M','경기도 광주','01039069999','9702201111111','9605171111111',12,'2020-06-10','e448f0dd6db3ea632cde2b24a0c249cda697f88ee8cc63d76d6963c9c18e8e29','하원석','rmdwjd0220'),('M','부산 해운대로','01090459999','9704291111111','9605171111111',12,'2019-12-25','44d0013bba3afe37f6d5401a6a12cad4296a8188de5ab565ad20d22bbf1d12c7','조희제','gmlwp0110');
/*!40000 ALTER TABLE `스트리밍구독자` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `음원`
--

DROP TABLE IF EXISTS `음원`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `음원` (
  `음악이름` varchar(70) NOT NULL,
  `가수` varchar(20) NOT NULL,
  `장르` varchar(20) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  `앨범이름` varchar(50) DEFAULT NULL,
  `음원관리번호` int(11) NOT NULL,
  `음원관리자주민번호` char(13) NOT NULL,
  PRIMARY KEY (`음원관리번호`),
  KEY `음원_FK` (`음원관리자주민번호`),
  CONSTRAINT `음원_FK` FOREIGN KEY (`음원관리자주민번호`) REFERENCES `음원관리자` (`관리자주민번호`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `음원`
--

LOCK TABLES `음원` WRITE;
/*!40000 ALTER TABLE `음원` DISABLE KEYS */;
INSERT INTO `음원` VALUES ('비와 당신','럼블피쉬','포크',21,'Memory For You',1,'9605171111111'),('너를 보내고','윤도현','발라드',31,'가을 우체국 앞에서',2,'9605171111111'),('잠이 오질 않네요','장범준','발라드',151,'잠이 오질 않네요',3,'9605171111111'),('희재','성시경','발라드',81,'Try To Remember',4,'9605171111111'),('나랑 같이 걸을래','적재','발라드',158,'나랑 같이 걸을래 ',5,'9605171111111'),('내 마음이 움찔했던 순간','규현','발라드',176,'내 마음이 움찔했던 순간',6,'9605171111111'),('너의 모든 순간','성시경','발라드',117,'별에서 온 그대 OST Part 7',7,'9605171111111'),('Feel Special','TWICE','댄스',288,'Feel Special',8,'9605171111111'),('빨간 맛','레드벨벳','댄스',44,'The Red Summer - Summer Mini Album',9,'9605171111111'),('FANCY YOU','TWICE','댄스',77,'FANCY YOU',10,'9605171111111'),('흔들리는 꽃들 속에서 네 샴푸향이 느껴진거야','장범준','OST',36,'멜로가 체질 OST Part 3',11,'9605171111111'),('idontwannabeyouanymore','Billie Eilish','POP',434,'dont smile at me',12,'9605171111111'),('wish you were gay','Billie Eilish','POP',762,'wish you were gay',13,'9605171111111'),('bad guy','Billie Eilish','POP',200,'WHEN WE ALL FALL ASLEEP, WHERE DO WE GO?',14,'9605171111111'),('사계','태연 (TAEYEON)','발라드',700,'사계 (Four Seasons)',15,'9605171111111'),('그대라는 시','태연 (TAEYEON)','발라드',321,'호텔 델루나 OST part 3',16,'9605171111111'),('불티','태연 (TAEYEON)','댄스',400,'Purpose - The 2nd Album',17,'9605171111111'),('11:11','태연 (TAEYEON)','발라드',444,'11:11',18,'9605171111111'),('Rain','태연 (TAEYEON)','발라드',200,'Rain - SM STATION',19,'9605171111111'),('FINE','태연 (TAEYEON)','발라드',333,'My Voice - The 1st Album',20,'9605171111111'),('비밀번호 486','윤하','발라드',486,'고백하기 좋은 날',21,'9605171111111'),('미치GO (GO)','G-DRAGON','힙합',223,'쿠데타 (COUP D\'ETAT)',22,'9605171111111'),('삐딱하게 (Crooked)','G-DRAGON','힙합',272,'쿠데타 (COUP D\'ETAT)',23,'9605171111111'),('염라 (Karma)','달의하루','인디',75,'염라(Karma)',24,'9605171111111'),('THE BADDEST','K/DA','댄스',135,'THE BADDEST',25,'9605171111111'),('POP/STARS','K/DA','댄스',124,'POP/STARS',26,'9605171111111'),('덤디덤디','(여자) 아이들','댄스',765,'덤디덤디 (DUMDi DUMDi)',27,'9605171111111'),('LATATA','(여자) 아이들','댄스',252,'i am',28,'9605171111111'),('싫다고 말해','(여자) 아이들','댄스',421,'I made',29,'9605171111111'),('LION','(여자) 아이들','댄스',321,'I trust',30,'9605171111111'),('한','(여자) 아이들','댄스',147,'한',31,'9605171111111'),('말해줘요','걸스데이','댄스',132,'Girl\'s Day Party #6',32,'9605171111111'),('여자 대통령','걸스데이','댄스',285,'여자 대통령',33,'9605171111111'),('기대해','걸스데이','댄스',465,'여자 대통령',34,'9605171111111'),('한번만 안아줘','걸스데이','댄스',636,'여자 대통령',35,'9605171111111'),('METEOR','창모(CHANGMO)','힙합',231,'Boyhood',36,'9605171111111'),('Beautiful','크러쉬(CRUSH)','발라드',539,'도깨비 OST Part 4',37,'9605171111111'),('가라사대','비와이(BewhY)','힙합',147,'The Movie Star',38,'9605171111111'),('Jesus In LA','Alec Benjamin','POP',385,'Jesus In LA',39,'9605171111111'),('Mad at Disney','salem ilese','POP',113,'Mad at Disney',40,'9605171111111'),('팔레트','아이유','발라드',478,'Palette',41,'9605171111111');
/*!40000 ALTER TABLE `음원` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `음원관리자`
--

DROP TABLE IF EXISTS `음원관리자`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `음원관리자` (
  `주소` varchar(60) DEFAULT NULL,
  `FName_M` varchar(15) NOT NULL,
  `LName_M` varchar(15) NOT NULL,
  `관리자주민번호` char(13) NOT NULL,
  PRIMARY KEY (`관리자주민번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `음원관리자`
--

LOCK TABLES `음원관리자` WRITE;
/*!40000 ALTER TABLE `음원관리자` DISABLE KEYS */;
INSERT INTO `음원관리자` VALUES ('고양시 일산동구 호수로','윤석','최','9605171111111');
/*!40000 ALTER TABLE `음원관리자` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `추가`
--

DROP TABLE IF EXISTS `추가`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `추가` (
  `a_음원관리번호` int(11) NOT NULL,
  `a_구독자주민번호` char(13) NOT NULL,
  `a_플레이리스트명` varchar(20) NOT NULL,
  PRIMARY KEY (`a_음원관리번호`,`a_구독자주민번호`,`a_플레이리스트명`),
  KEY `추가_FK_1` (`a_구독자주민번호`,`a_플레이리스트명`),
  CONSTRAINT `추가_FK` FOREIGN KEY (`a_음원관리번호`) REFERENCES `음원` (`음원관리번호`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `추가_FK_1` FOREIGN KEY (`a_구독자주민번호`, `a_플레이리스트명`) REFERENCES `플레이리스트` (`플레이리스트_구독자주민번호`, `플레이리스트명`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `추가`
--

LOCK TABLES `추가` WRITE;
/*!40000 ALTER TABLE `추가` DISABLE KEYS */;
INSERT INTO `추가` VALUES (6,'9501121111111','balad'),(8,'9501121111111','dance'),(9,'9501121111111','dance'),(15,'9702201111111','teayeon'),(16,'9702201111111','teayeon'),(17,'9702201111111','teayeon'),(18,'9702201111111','teayeon'),(19,'9702201111111','teayeon'),(20,'9702201111111','teayeon'),(25,'9501121111111','dance'),(27,'9501121111111','idle'),(28,'9501121111111','idle'),(29,'9501121111111','idle'),(30,'9501121111111','idle'),(31,'9501121111111','idle'),(41,'9501121111111','balad');
/*!40000 ALTER TABLE `추가` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `플레이리스트`
--

DROP TABLE IF EXISTS `플레이리스트`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `플레이리스트` (
  `플레이리스트_구독자주민번호` char(13) NOT NULL,
  `플레이리스트명` varchar(20) NOT NULL,
  PRIMARY KEY (`플레이리스트_구독자주민번호`,`플레이리스트명`),
  CONSTRAINT `플레이리스트_FK` FOREIGN KEY (`플레이리스트_구독자주민번호`) REFERENCES `스트리밍구독자` (`구독자주민번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `플레이리스트`
--

LOCK TABLES `플레이리스트` WRITE;
/*!40000 ALTER TABLE `플레이리스트` DISABLE KEYS */;
INSERT INTO `플레이리스트` VALUES ('9501121111111','balad'),('9501121111111','dance'),('9501121111111','idle'),('9702201111111','teayeon');
/*!40000 ALTER TABLE `플레이리스트` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-05 11:57:36
