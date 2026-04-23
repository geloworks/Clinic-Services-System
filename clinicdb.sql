CREATE DATABASE  IF NOT EXISTS `clinicdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `clinicdb`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: clinicdb
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `adminaccount`
--

DROP TABLE IF EXISTS `adminaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adminaccount` (
  `AdminID` varchar(25) NOT NULL,
  `Username` varchar(25) NOT NULL,
  `Password` varchar(60) NOT NULL,
  PRIMARY KEY (`AdminID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminaccount`
--

LOCK TABLES `adminaccount` WRITE;
/*!40000 ALTER TABLE `adminaccount` DISABLE KEYS */;
INSERT INTO `adminaccount` VALUES ('Admin','admin','$2a$10$g0G.c73lJPAgociX.sqb4eLvuV1JiMO4T6/NooNi2Z4bbWd0RqGmm');
/*!40000 ALTER TABLE `adminaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dashboard`
--

DROP TABLE IF EXISTS `dashboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dashboard` (
  `PatientRegistered` int NOT NULL,
  `SuppliesSold` int NOT NULL,
  `Earnings` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dashboard`
--

LOCK TABLES `dashboard` WRITE;
/*!40000 ALTER TABLE `dashboard` DISABLE KEYS */;
INSERT INTO `dashboard` VALUES (1,30,9288.94);
/*!40000 ALTER TABLE `dashboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `PatientID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `Sex` varchar(10) NOT NULL,
  `Age` int NOT NULL,
  `Weight` double DEFAULT NULL,
  `Height` double DEFAULT NULL,
  `ContactNumber` varchar(20) NOT NULL,
  PRIMARY KEY (`PatientID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'Ageo','Acejo','Male',20,60.5,170,'09123456789'),(2,'Leigh','Ednilao','Male',20,62,172,'09123456789'),(3,'Angelo','Javier','Male',20,65.2,168,'09123456789'),(4,'Jet','Sia','Male',20,70,175,'09123456789'),(5,'Matheu','Culaton','Male',20,60,170,'09123456789'),(6,'Denmarx','Canoza','Male',20,80,167,'09123456789'),(7,'Stephen','Cruz','Male',20,60,166,'09123456789'),(8,'Ronn','Viquiera','Male',20,75,176,'09123456789'),(9,'Marco','Ramos','Male',20,65,173,'09123456789'),(10,'Clarence','Marcos','Male',20,85.2,165,'09123456789'),(11,'Ryan','Ragos','Male',30,52,155,'09196317625');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patientrecords`
--

DROP TABLE IF EXISTS `patientrecords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patientrecords` (
  `RecordID` int NOT NULL AUTO_INCREMENT,
  `PatientID` int NOT NULL,
  `Service` varchar(100) NOT NULL,
  `Doctor` varchar(100) DEFAULT NULL,
  `Diagnosis` text,
  `Prescription` text,
  `Date` date NOT NULL,
  PRIMARY KEY (`RecordID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patientrecords`
--

LOCK TABLES `patientrecords` WRITE;
/*!40000 ALTER TABLE `patientrecords` DISABLE KEYS */;
INSERT INTO `patientrecords` VALUES (1,1,'Circumcision','Dr.Ageo','Phimosis','Antibiotics & Painkillers','2026-01-10'),(2,2,'Consultation','Dr.Angelo','Acute Bronchitis','Cough Syrup & Rest','2026-01-12'),(3,3,'Dental Checkup','Dr.Leigh','Dental Caries','Oral Prophylaxis','2026-01-15'),(4,4,'Minor Surgery','Dr.Jet','Sebaceous Cyst','Excision & Mupirocin','2026-02-01'),(5,5,'Physical Exam','Dr.Ageo','Normal Findings','Daily Multivitamins','2026-02-05'),(6,6,'Follow-up','Dr.Angelo','Recovering Well','Continue Medication','2026-02-10'),(7,7,'Emergency','Dr.Leigh','Laceration','Suturing & Tetanus Shot','2026-03-01'),(8,8,'Consultation','Dr.Jet','Seasonal Allergies','Loratadine 10mg','2026-03-05'),(9,9,'Circumcision','Dr.Ageo','Post-Op Check','Wound Cleaning','2026-03-12'),(10,10,'General Checkup','Dr.Angelo','Mild Dehydration','Oral Rehydration Salts','2026-04-02'),(11,9,'General Consultation','Dr. Angelo','Dead','E-Zinc Zinc Sulfate (x1)','2026-04-12'),(12,10,'Child Growth Monitoring Visit','Dr. Jet','hello','Generic Zinc Zinc + Vitamins (x2), Nutrilin Multivitamins (x2)','2026-04-12'),(13,8,'Vaccination','Dr. Jet','z','Nutrilin Multivitamins (x5)','2026-04-12'),(14,6,'Hello','Dr. Angelo','dsa','Generic Zinc Zinc + Vitamins (x5)','2026-04-12'),(15,4,'Vaccination','Dr. Angelo','qeqweqwe','E-Zinc Zinc Sulfate (x2), Nutrilin Multivitamins (x1)','2026-04-14'),(16,11,'Basic Skin Check','Dr. Leigh','Sample','Hydrite Oral Rehydration Salts (x5), Human Nature SPF 30 Sunscreen (x5), E-Zinc Zinc Sulfate (x1)','2026-04-15'),(17,10,'General Consultation','Dr. Angelo','S','E-Zinc Zinc Sulfate (x1)','2026-04-15');
/*!40000 ALTER TABLE `patientrecords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `queueboard`
--

DROP TABLE IF EXISTS `queueboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `queueboard` (
  `QueueID` int NOT NULL AUTO_INCREMENT,
  `QueueNumber` int NOT NULL,
  `PatientID` int NOT NULL,
  `PatientName` varchar(50) NOT NULL,
  `Service` varchar(50) NOT NULL,
  `Doctor` varchar(50) NOT NULL,
  `Status` varchar(20) NOT NULL,
  PRIMARY KEY (`QueueID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `queueboard`
--

LOCK TABLES `queueboard` WRITE;
/*!40000 ALTER TABLE `queueboard` DISABLE KEYS */;
/*!40000 ALTER TABLE `queueboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipts`
--

DROP TABLE IF EXISTS `receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipts` (
  `BillID` int NOT NULL AUTO_INCREMENT,
  `PatientID` int NOT NULL,
  `PatientName` varchar(255) DEFAULT NULL,
  `PatientAge` int DEFAULT NULL,
  `Service` varchar(100) DEFAULT NULL,
  `ServiceCost` decimal(10,2) DEFAULT NULL,
  `SupplyCost` decimal(10,2) DEFAULT NULL,
  `BaseCost` decimal(10,2) DEFAULT NULL,
  `Discount` decimal(10,2) DEFAULT NULL,
  `SubTotal` decimal(10,2) DEFAULT NULL,
  `TotalAmountDue` decimal(10,2) DEFAULT NULL,
  `PhilHealthID` varchar(50) DEFAULT NULL,
  `PhilHealthAmount` decimal(10,2) DEFAULT '0.00',
  `CashPaid` decimal(10,2) DEFAULT '0.00',
  `Changes` decimal(10,2) DEFAULT '0.00',
  `TransactionDate` date DEFAULT NULL,
  PRIMARY KEY (`BillID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipts`
--

LOCK TABLES `receipts` WRITE;
/*!40000 ALTER TABLE `receipts` DISABLE KEYS */;
INSERT INTO `receipts` VALUES (1,9,'Marco Ramos',20,'General Consultation',550.00,99.80,649.80,36.39,727.78,691.39,'dasdasdasdasdasd123123',5203.00,0.00,4511.61,'2026-04-12'),(2,10,'Clarence Marcos',20,'Child Growth Monitoring Visit',800.00,553.50,1353.50,75.80,1515.92,1440.12,NULL,0.00,1500.00,59.88,'2026-04-12'),(3,8,'Ronn Viquiera',20,'Vaccination',500.00,833.75,1333.75,74.69,1493.80,1419.11,NULL,0.00,1500.00,80.89,'2026-04-12'),(4,6,'Denmarx Canoza',20,'Hello',123.00,550.00,673.00,37.69,753.76,716.07,NULL,0.00,1000.00,283.93,'2026-04-12'),(5,4,'Jet Sia',20,'Vaccination',500.00,366.35,866.35,48.52,970.31,921.80,NULL,0.00,1000.00,78.20,'2026-04-14'),(6,11,'Ryan Ragos',30,'Basic Skin Check',1700.00,1469.80,3169.80,177.51,3550.18,3372.67,NULL,0.00,3500.00,127.33,'2026-04-15'),(7,10,'Clarence Marcos',20,'General Consultation',550.00,99.80,649.80,0.00,727.78,727.78,'dasdad713ss',6536.00,0.00,5808.22,'2026-04-15');
/*!40000 ALTER TABLE `receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `UserID` varchar(100) NOT NULL,
  `ActionType` varchar(100) NOT NULL,
  `Details` varchar(1000) NOT NULL,
  `DateAndTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` VALUES ('Admin','Add Service','Added Service: Hello\nPrice: 123','2026-04-12 13:52:41'),('Admin','Add Supply','Added Supply: he lo\nQuantity: 123\nUnit Cost: 321','2026-04-12 13:53:12'),('Admin','Add Staff','Added Staff: gelo','2026-04-12 13:57:08'),('Staff','Add Patient to Queue','Patient P-009added to the queue','2026-04-12 14:03:29'),('Staff','Consultation','Consultation Complete for Patient P-009','2026-04-12 14:04:22'),('Staff','Billing','Patient 9 paid P 691.39','2026-04-12 14:05:34'),('Staff','Add Patient to Queue','Patient P-010added to the queue','2026-04-12 14:08:22'),('Staff','Consultation','Consultation Complete for Patient P-010','2026-04-12 14:09:06'),('Staff','Billing','Patient 10 paid P 1,440.12','2026-04-12 14:09:15'),('Staff','Add Patient to Queue','Patient P-008added to the queue','2026-04-12 14:11:41'),('Staff','Consultation','Consultation Complete for Patient P-008','2026-04-12 14:11:50'),('Staff','Billing','Patient 8 paid P 1,419.11','2026-04-12 14:11:55'),('Staff','Add Patient to Queue','Patient P-006added to the queue','2026-04-12 14:12:39'),('Staff','Consultation','Consultation Complete for Patient P-006','2026-04-12 14:12:54'),('Staff','Billing','Patient 6 paid P 716.07','2026-04-12 14:13:51'),('Admin','Update Supply','Updated Supply: Nutrilin Multivitamins\nQuantity: 49\nCost: 166.75','2026-04-14 22:58:01'),('Admin','Update Supply','Updated Supply: Generic Zinc Zinc + Vitamins\nQuantity: 50\nCost: 110.0','2026-04-14 22:58:05'),('Admin','Update Supply','Updated Supply: Allerta Loratadine\nQuantity: 51\nCost: 23.5','2026-04-14 22:58:10'),('Admin','Update Staff','Updated Staff: gelo','2026-04-14 22:59:15'),('Admin','Delete Staff','Deleted Staff: gelo','2026-04-14 22:59:57'),('Admin','Add Staff','Added Staff: user','2026-04-14 23:00:09'),('S-002','Add Patient to Queue','Patient P-004added to the queue','2026-04-14 23:01:01'),('S-002','Consultation','Consultation Complete for Patient P-004','2026-04-14 23:01:43'),('S-002','Billing','Patient 4 paid P 921.80','2026-04-14 23:02:14'),('Admin','Update Staff','Updated Staff: user','2026-04-14 23:03:38'),('Admin','Add Service','Added Service: Cryopractor\nPrice: 5000','2026-04-15 12:13:44'),('Admin','Update Service','Updated Service: Cryopractor123\nPrice: 5500','2026-04-15 12:14:01'),('Admin','Delete Service','Deleted Service: Cryopractor123\nPrice: 5500','2026-04-15 12:14:40'),('Admin','Update Supply','Updated Supply: hello lolo\nQuantity: 123\nCost: 321.0','2026-04-15 12:16:04'),('Admin','Update Supply','Updated Supply: hello lolo\nQuantity: 123\nCost: 321.0','2026-04-15 12:17:02'),('Admin','Add Supply','Added Supply: hello lolo\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:21:39'),('Admin','Delete Supply','Deleted Supply: hello lolo\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:21:50'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:24:26'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:24:55'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:25:13'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:25:28'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:25:34'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:25:47'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:26:28'),('Admin','Update Supply','Updated Supply: hello world\nQuantity: 123\nCost: 321.0','2026-04-15 12:26:36'),('Admin','Add Supply','Added Supply: hello world\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:26:39'),('Admin','Delete Supply','Deleted Supply: hello world\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:26:48'),('Admin','Add Supply','Added Supply: hello world\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:27:02'),('Admin','Delete Supply','Deleted Supply: hello world\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 12:28:19'),('Admin','Add Service','Added Service: Sample\nPrice: 1000','2026-04-15 13:21:25'),('Admin','Update Service','Updated Service: Sample\nPrice: 1000','2026-04-15 13:21:36'),('Admin','Update Service','Updated Service: Sample2\nPrice: 10000','2026-04-15 13:21:51'),('Admin','Update Service','Updated Service: Vaccination\nPrice: 1500','2026-04-15 13:22:32'),('Admin','Delete Service','Deleted Service: Sample2\nPrice: 10000','2026-04-15 13:22:47'),('Admin','Add Supply','Added Supply: Cetaphil Gentle Cleanser\nQuantity: 331\nUnit Cost: 484.0','2026-04-15 13:24:00'),('Admin','Update Supply','Updated Supply: Sample Supply\nQuantity: 123\nCost: 321.0','2026-04-15 13:24:28'),('Admin','Delete Supply','Deleted Supply: Sample Supply\nQuantity: 123\nUnit Cost: 321.0','2026-04-15 13:24:39'),('Admin','Add Staff','Added Staff: hash','2026-04-15 13:27:46'),('Admin','Update Staff','Updated Staff: hash','2026-04-15 13:28:32'),('S-003','Add Patient','Added Patient: RyanRagos','2026-04-15 13:31:54'),('S-003','Add Patient to Queue','Patient P-011added to the queue','2026-04-15 13:34:23'),('S-003','Consultation','Consultation Complete for Patient P-011','2026-04-15 13:40:48'),('S-003','Add Patient to Queue','Patient P-010added to the queue','2026-04-15 13:42:39'),('S-003','Billing','Patient 11 paid P 3,372.67','2026-04-15 13:44:57'),('S-003','Consultation','Consultation Complete for Patient P-010','2026-04-15 13:45:24'),('S-003','Billing','Patient 10 paid P 727.78','2026-04-15 13:46:42');
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `ServiceID` int NOT NULL AUTO_INCREMENT,
  `ServiceName` varchar(100) NOT NULL,
  `Price` double NOT NULL,
  PRIMARY KEY (`ServiceID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (1,'General Consultation',550),(2,'Vaccination',1500),(3,'Chronic Disease Check',450),(4,'Health Check-up',1000),(5,'Wound Cleaning & Dressing',700),(6,'Pediatric Consultation',200),(7,'Child Growth Monitoring Visit',800),(8,'Dermatology Consultation',1250),(9,'Basic Skin Check',1700),(10,'Skin Allergy Testing',4500),(11,'Acne Evaluation',2250),(12,'Skin Tag Removal',2500),(13,'Chemical Peel',2750),(14,'Pico Laser Treatment',5500),(15,'Deep-Cleansing Facial',425),(16,'Hello',123);
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staffaccount`
--

DROP TABLE IF EXISTS `staffaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staffaccount` (
  `StaffID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) DEFAULT NULL,
  `FullName` varchar(50) NOT NULL,
  `Password` varchar(60) NOT NULL,
  PRIMARY KEY (`StaffID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staffaccount`
--

LOCK TABLES `staffaccount` WRITE;
/*!40000 ALTER TABLE `staffaccount` DISABLE KEYS */;
INSERT INTO `staffaccount` VALUES (2,'user','ads asd','$2a$10$2FnmW.nha3EiW7bGmI20ieu2NQBoUHaZ3Z/0OCqT0PB.DkT7WCgtm'),(3,'hash','sample test','$2a$10$TbFomF1hwbhXS45AFEemnu.99iG3yMAB111iAWzSOBuBVB9o.J/NS');
/*!40000 ALTER TABLE `staffaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supply`
--

DROP TABLE IF EXISTS `supply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supply` (
  `SupplyID` int NOT NULL AUTO_INCREMENT,
  `BrandName` varchar(100) DEFAULT NULL,
  `GenericName` varchar(100) DEFAULT NULL,
  `Measurement` varchar(50) DEFAULT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Quantity` int NOT NULL,
  `UnitCost` double NOT NULL,
  PRIMARY KEY (`SupplyID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supply`
--

LOCK TABLES `supply` WRITE;
/*!40000 ALTER TABLE `supply` DISABLE KEYS */;
INSERT INTO `supply` VALUES (1,'E-Zinc','Zinc Sulfate','60 mL','Syrup',94,99.8),(2,'Nutrilin','Multivitamins','120 mL','Syrup',48,166.75),(3,'Generic Zinc','Zinc + Vitamins','60 mL','Syrup',50,110),(4,'Allerta','Loratadine','10 mg','Tablet',51,23.5),(5,'Temprazin','Cetirizine HCI','60 mL','Syrup',854,201),(6,'Biogesic','Paracetamol','500 mg','Tablet',2554,2.75),(7,'Generic','Amoxicillin','500 mg','Capsule',1771,7.75),(8,'Cetaphil','Gentle Cleanser','236 mL','Skincare',331,484),(9,'Human Nature','SPF 30 Sunscreen','50 mL','Skincare',579,199),(10,'Neozep','Phenylephrine + Paracetamol','500 mg','Tablet',1987,3.25),(11,'Solmux','Carbocisteine','60 mL','Syrup',743,145.5),(12,'Ventolin','Salbutamol','100 mcg','Inhaler',325,289.75),(13,'Fern-C','Ascorbic Acid','500 mg','Capsule',1200,9.5),(14,'Ceelin Plus','Vitamin C + Zinc','120 mL','Syrup',642,185.25),(15,'Diatabs','Loperamide','2 mg','Capsule',978,4.5),(16,'Hydrite','Oral Rehydration Salts','200 mL','Solution',426,75),(17,'Betadine','Povidone-Iodine','120 mL','Antiseptic',289,210),(18,'Calpol','Paracetamol','120 mL','Syrup',512,132.5),(19,'Lagundi','Vitex Negundo','60 mL','Herbal Syrup',377,99),(20,'Alaxan','Ibuprofen + Paracetamol','200 mg + 325 mg','Tablet',1567,8.25),(25,'Cetaphil','Gentle Cleanser','236 mL mL','Skincare',331,484);
/*!40000 ALTER TABLE `supply` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-23 20:49:35
