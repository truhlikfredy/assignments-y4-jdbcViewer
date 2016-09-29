-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.43-0+deb8u1 - (Debian)
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             9.1.0.4896
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for Database1
DROP DATABASE IF EXISTS `Database1`;
CREATE DATABASE IF NOT EXISTS `Database1` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `Database1`;


-- Dumping structure for table Database1.Department
CREATE TABLE IF NOT EXISTS `Department` (
  `Name` varchar(100) NOT NULL,
  `Number` int(11) NOT NULL DEFAULT '0',
  `Locations` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table Database1.Employee
CREATE TABLE IF NOT EXISTS `Employee` (
  `Ssn` int(11) NOT NULL AUTO_INCREMENT,
  `Bdate` date DEFAULT NULL,
  `Name` varchar(80) DEFAULT NULL,
  `Address` varchar(160) DEFAULT NULL,
  `Salary` decimal(7,0) DEFAULT NULL,
  `Sex` char(1) DEFAULT NULL,
  `Works_For` int(11) NOT NULL,
  `Manages` int(11) DEFAULT NULL,
  `Supervises` int(11) DEFAULT NULL,
  PRIMARY KEY (`Ssn`),
  KEY `Work_For` (`Works_For`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table Database1.Project
CREATE TABLE IF NOT EXISTS `Project` (
  `Name` varchar(80) NOT NULL,
  `Number` int(11) NOT NULL,
  `Location` varchar(50) DEFAULT NULL,
  `Controlled_By` int(11) DEFAULT NULL,
  PRIMARY KEY (`Name`,`Number`),
  KEY `FK_Project_Department` (`Controlled_By`),
  CONSTRAINT `FK_Project_Department` FOREIGN KEY (`Controlled_By`) REFERENCES `Department` (`Number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
