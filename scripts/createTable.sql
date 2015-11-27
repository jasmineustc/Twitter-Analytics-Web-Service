-- q2 test2 create table --
DROP TABLE IF EXISTS `test2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test2` (
  `userid` BIGINT NOT NULL,
  `newDate` TIMESTAMP NOT NULL,
  `post` text
) ENGINE=MyiSAM character set utf8mb4;
-- q2 test2 create table end --

-- q3 test3 create table --
DROP TABLE IF EXISTS `test3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test3` (
  `userid` BIGINT NOT NULL,
  `value` longtext
) ENGINE=MyiSAM character set utf8mb4;
-- q3 test3 create table end --

-- q4 test4 create table --
DROP TABLE IF EXISTS `test4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test4` (
  `hashTag` VARCHAR(417) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `value` text
) ENGINE=MyiSAM character set utf8mb4;
-- q4 test4 create table end --

-- q5 test5 create table -- 
DROP TABLE IF EXISTS `test5`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test5` (
  `userid` BIGINT UNSIGNED NOT NULL,
  `count` INT NOT NULL,
  `self` INT NOT NULL
) ENGINE=MyiSAM character set utf8mb4;
-- q5 test5 create table end --
