-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: 172.30.225.230    Database: community_pvt_system
-- ------------------------------------------------------
-- Server version	5.7.34-log

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
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(20) NOT NULL COMMENT '区域代码',
  `name` varchar(50) DEFAULT NULL COMMENT '区域名',
  `level` tinyint(2) DEFAULT NULL COMMENT '等级',
  `parent_code` varchar(20) DEFAULT NULL COMMENT '父级代码',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`) USING BTREE,
  KEY `idx_parent_code` (`parent_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=662915 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'110000000000','北京市',1,'0','2021-12-30 09:17:41','',0),(2,'110100000000','市辖区',2,'110000000000','2021-12-30 09:17:41','',0),(3,'110101000000','东城区',3,'110100000000','2021-12-30 09:17:41','',0),(4,'110101001000','东华门街道',4,'110101000000','2021-12-30 09:17:41','',0),(5,'110101001001','多福巷社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(6,'110101001002','银闸社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(7,'110101001005','东厂社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(8,'110101001006','智德社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(9,'110101001007','南池子社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(10,'110101001008','黄图岗社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(11,'110101001009','灯市口社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(12,'110101001010','正义路社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(13,'110101001011','甘雨社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(14,'110101001013','台基厂社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(15,'110101001014','韶九社区居委会',5,'110101001000','2021-12-30 09:17:41','',0),(7628,'120000000000','天津市',1,'0','2021-12-30 09:18:14','',0),(13467,'130000000000','河北省',1,'0','2021-12-30 09:18:25','',0),(69893,'140000000000','山西省',1,'0','2021-12-30 09:28:51','',0),(94994,'150000000000','内蒙古自治区',1,'0','2021-12-30 09:33:27','',0),(110782,'210000000000','辽宁省',1,'0','2021-12-30 09:37:09','',0),(128693,'220000000000','吉林省',1,'0','2021-12-30 09:41:19','',0),(141555,'230000000000','黑龙江省',1,'0','2021-12-30 09:44:18','',0),(157314,'310000000000','上海市',1,'0','2021-12-30 09:47:40','',0),(163754,'320000000000','江苏省',1,'0','2021-12-30 09:48:51','',0),(187588,'330000000000','浙江省',1,'0','2021-12-30 09:54:02','',0),(214613,'340000000000','安徽省',1,'0','2021-12-30 09:56:31','',0),(234733,'350000000000','福建省',1,'0','2021-12-30 09:57:11','',0),(253291,'360000000000','江西省',1,'0','2021-12-30 09:57:47','',0),(276801,'370000000000','山东省',1,'0','2021-12-30 09:58:33','',0),(353940,'410000000000','河南省',1,'0','2021-12-30 10:01:43','',0),(409070,'420000000000','湖北省',1,'0','2021-12-30 10:03:33','',0),(439065,'430000000000','湖南省',1,'0','2021-12-30 10:04:34','',0),(470715,'440000000000','广东省',1,'0','2021-12-30 10:05:38','',0),(499274,'450000000000','广西壮族自治区',1,'0','2021-12-30 10:06:35','',0),(517243,'460000000000','海南省',1,'0','2021-12-30 10:07:12','',0),(520836,'500000000000','重庆市',1,'0','2021-12-30 10:07:19','',0),(533130,'510000000000','四川省',1,'0','2021-12-30 10:07:48','',0),(572021,'520000000000','贵州省',1,'0','2021-12-30 10:11:29','',0),(591492,'530000000000','云南省',1,'0','2021-12-30 10:12:08','',0),(607739,'540000000000','西藏自治区',1,'0','2021-12-30 10:12:57','',0),(614041,'610000000000','陕西省',1,'0','2021-12-30 10:13:10','',0),(635418,'620000000000','甘肃省',1,'0','2021-12-30 10:17:27','',0),(654591,'630000000000','青海省',1,'0','2021-12-30 10:20:38','',0),(659774,'640000000000','宁夏回族自治区',1,'0','2021-12-30 10:21:49','',0),(662914,'650000000000','新疆维吾尔自治区',1,'0','2021-12-30 10:22:31','',0);
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `d_group` tinyint(4) unsigned NOT NULL COMMENT '组织分组(1.系统,2.租户,3.小区,4.楼栋,5.房屋,6.区域,7.楼层)',
  `department_name` varchar(100) NOT NULL COMMENT '组织名',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `pk_org_id` bigint(20) NOT NULL COMMENT '租户主键id',
  `code` varchar(20) DEFAULT NULL COMMENT '编码',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `is_init` tinyint(1) unsigned DEFAULT '0' COMMENT '是否系统预置数据(0:否,1:是)',
  `level` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '组织当前层级(所属org下)',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`),
  KEY `idx_d_group` (`d_group`) USING BTREE,
  KEY `idx_department_name` (`department_name`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_path_d_group` (`path`,`d_group`) USING BTREE,
  KEY `idx_pk_org_id` (`pk_org_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,1,'联通物联网',NULL,1,'00','00',NULL,0,1,0,'2021-11-24 16:13:02','SYSTEM'),(10,1,'北京市',1,1,'BJ','00-BEIJING',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(12,1,'上海市',1,1,'SH','00-SHANGHAI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(14,1,'河北省',1,1,'HE','00-HEBEI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(20,1,'浙江省',1,1,'ZJ','00-ZHEJIANG',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(21,1,'安徽省',1,1,'AH','00-ANHUI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(23,1,'江西省',1,1,'JX','00-JIANGXI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(24,1,'山东省',1,1,'SD','00-SHANDONG',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(25,1,'河南省',1,1,'HA','00-HENAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(26,1,'湖北省',1,1,'HB','00-HUBEI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(27,1,'湖南省',1,1,'BN','00-HUNAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(28,1,'广东省',1,1,'GD','00-GUANGDONG',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(29,1,'海南省',1,1,'HI','00-HAINAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(30,1,'四川省',1,1,'SC','00-SICHUAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(31,1,'贵州省',1,1,'GZ','00-GUIZHOU',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(32,1,'云南省',1,1,'YN','00-YUNNAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(34,1,'甘肃省',1,1,'GS','00-GANSU',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(35,1,'青海省',1,1,'QH','00-QINGHAI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(36,1,'台湾省',1,1,'TW','00-TAIWAN',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(37,1,'内蒙古',1,1,'NM','00-NEIMENGGU',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(38,1,'广西省',1,1,'GX','00-GUANGXI',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(39,1,'西藏省',1,1,'XZ','00-XIZANG',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM'),(40,1,'宁夏',1,1,'NX','00-XINING',NULL,0,1,1,'2021-11-24 16:13:02','SYSTEM');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_property`
--

DROP TABLE IF EXISTS `department_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `property_key` varchar(50) DEFAULT NULL COMMENT '编码',
  `val` varchar(255) DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_property`
--

LOCK TABLES `department_property` WRITE;
/*!40000 ALTER TABLE `department_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `department_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pk_org_id` bigint(20) NOT NULL COMMENT '当前租户id',
  `self_dept_id` bigint(20) unsigned NOT NULL COMMENT '组织id(自身)',
  `community_id` bigint(20) unsigned NOT NULL COMMENT '小区id',
  `region_id` bigint(20) unsigned DEFAULT NULL COMMENT '区域id',
  `building_id` bigint(20) unsigned NOT NULL COMMENT '楼栋id',
  `floor_id` bigint(20) unsigned DEFAULT NULL COMMENT '楼层id',
  `unit` int(10) unsigned DEFAULT '1' COMMENT '单元号',
  `name` varchar(191) NOT NULL COMMENT '房号',
  `house_attribute` varchar(10) DEFAULT NULL COMMENT '房屋属性（物业侧：自住，出租）',
  `room` tinyint(3) unsigned DEFAULT NULL COMMENT '室',
  `hall` tinyint(3) unsigned DEFAULT NULL COMMENT '厅',
  `bathroom` tinyint(3) unsigned DEFAULT NULL COMMENT '卫',
  `house_area` varchar(30) NOT NULL COMMENT '房屋面积',
  `used_area` varchar(30) NOT NULL DEFAULT '' COMMENT '使用面积',
  `public_area` varchar(30) NOT NULL DEFAULT '' COMMENT '公摊面积',
  `storage_no` varchar(64) DEFAULT '' COMMENT '储藏号',
  `parking_no` varchar(64) NOT NULL DEFAULT '' COMMENT '车位编号',
  `orientation` varchar(191) DEFAULT NULL COMMENT '房屋朝向',
  `property_years` tinyint(4) DEFAULT NULL COMMENT '产权年限(70、40、20、10、临时)',
  `floor_num` int(4) DEFAULT NULL COMMENT '楼层',
  `house_type` tinyint(4) DEFAULT NULL COMMENT '房屋类型，1住宅 2商铺 3办公',
  `house_status` tinyint(4) DEFAULT NULL COMMENT '房屋状态，1常住户 2非常住户 3装修户 4未交房 5未售',
  `used_status` tinyint(4) DEFAULT '7' COMMENT '使用状态，1自用2已租 3空置 4待租 5待售 6已售 7其它',
  `delivery_time` datetime DEFAULT NULL COMMENT '交房时间',
  `description` varchar(256) DEFAULT '' COMMENT '描述',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建者id',
  `updated_on` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新者id',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除 1：已删除）',
  `deleted_on` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted_by` varchar(64) DEFAULT NULL COMMENT '删除人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_self_dept_id` (`self_dept_id`),
  UNIQUE KEY `uk_community_building_unit_name` (`community_id`,`building_id`,`unit`,`name`,`deleted_on`),
  KEY `house_org_id_foregin` (`pk_org_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='房屋';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house`
--

LOCK TABLES `house` WRITE;
/*!40000 ALTER TABLE `house` DISABLE KEYS */;
/*!40000 ALTER TABLE `house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indicator_card`
--

DROP TABLE IF EXISTS `indicator_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indicator_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(32) NOT NULL COMMENT '指标卡名称',
  `label_type_id` bigint(20) NOT NULL COMMENT '所属账户标签id',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL COMMENT '创建者',
  `deleted` tinyint(1) NOT NULL COMMENT '删除状态(0：未删除；1：已删除)',
  `delete_time` datetime(3) NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `label_type_id_deleted` (`label_type_id`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='指标卡预置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indicator_card`
--

LOCK TABLES `indicator_card` WRITE;
/*!40000 ALTER TABLE `indicator_card` DISABLE KEYS */;
INSERT INTO `indicator_card` VALUES (14,'park',9,'2023-01-17 11:45:19.000','zjb',0,'1970-01-01 00:00:00.000'),(15,'region',9,'2023-01-17 11:45:19.000','zjb',0,'1970-01-01 00:00:00.000'),(16,'building',9,'2023-01-17 11:45:19.000','zjb',0,'1970-01-01 00:00:00.000'),(17,'floor',9,'2023-01-17 11:45:19.000','zjb',0,'1970-01-01 00:00:00.000'),(18,'house',9,'2023-01-17 11:45:19.000','zjb',0,'1970-01-01 00:00:00.000');
/*!40000 ALTER TABLE `indicator_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label_type`
--

DROP TABLE IF EXISTS `label_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label_type` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `label_name` varchar(32) NOT NULL COMMENT '标签名称',
  `label_type` tinyint(1) DEFAULT NULL COMMENT '标签类型（0：用户标签；1：账户标签）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label_type`
--

LOCK TABLES `label_type` WRITE;
/*!40000 ALTER TABLE `label_type` DISABLE KEYS */;
INSERT INTO `label_type` VALUES (2,'厂园区',0),(6,'联通管理方',0),(9,'厂园区',1),(13,'联通管理方',1);
/*!40000 ALTER TABLE `label_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '自增ID',
  `menu_id` varchar(32) NOT NULL COMMENT '菜单id',
  `menu_name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(255) DEFAULT NULL,
  `component_uri` varchar(255) DEFAULT NULL COMMENT '菜单组件',
  `api_url` varchar(255) DEFAULT NULL COMMENT '对应api url',
  `module_type` tinyint(1) DEFAULT NULL COMMENT '菜单分类（1：超级管理员菜单、2：普通用户菜单）',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标，待产品需求',
  `icon_active` varchar(128) DEFAULT NULL COMMENT '图标激活',
  `menu_type` tinyint(2) DEFAULT NULL COMMENT '菜单类型 1菜单组 2菜单项 3查看操作',
  `permission_code` varchar(128) DEFAULT NULL COMMENT '权限代码(menu_type为3时必填)',
  `parent_menu_id` varchar(32) DEFAULT NULL COMMENT '父id， 顶级存储0',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `hidden` tinyint(1) DEFAULT NULL COMMENT '1:true:隐藏，0:false:显示',
  `catalog` varchar(32) DEFAULT NULL COMMENT '目录，区分不同saas应用',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '菜单创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型【1：SYSTEM; 2: Portal；3：API】',
  `updated_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '角色修改时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '修改者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  `updated_by_type` tinyint(1) DEFAULT NULL COMMENT '修改者类型【1：SYSTEM; 2: Portal；3：API】',
  `org_type_id` int(11) DEFAULT NULL COMMENT '组织类型id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'100',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basics:control:all','0',NULL,NULL,NULL,'基础功能','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(2,'10001',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basic:dashboard:console','100',NULL,NULL,NULL,'控制台','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(3,'10002',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basics:system:control','100',NULL,NULL,NULL,'系统管理','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(4,'1000201',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:role:control','10002',NULL,NULL,NULL,'角色管理','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(5,'100020101',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:role:detail','1000201',NULL,NULL,NULL,'角色查看','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(6,'100020102',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:role:add','1000201',NULL,NULL,NULL,'新增角色','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(7,'100020103',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:role:edit','1000201',NULL,NULL,NULL,'编辑角色','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(8,'1000202',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:org:control','10002',NULL,NULL,NULL,'组织管理','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(9,'100020201',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:org:add','1000202',NULL,NULL,NULL,'新增组织','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(10,'100020202',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:org:edit','1000202',NULL,NULL,NULL,'编辑组织','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(11,'100020203',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:org:delete','1000202',NULL,NULL,NULL,'删除组织','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(12,'1000203',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:account:control','10002',NULL,NULL,NULL,'账户管理','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(13,'100020301',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:account:detail','1000203',NULL,NULL,NULL,'账户详情','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(14,'100020302',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:account:add','1000203',NULL,NULL,NULL,'新增账户','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(15,'100020303',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:account:edit','1000203',NULL,NULL,NULL,'编辑账户','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(16,'100020304',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:account:opt','1000203',NULL,NULL,NULL,'启用/禁用账户','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(17,'1000204',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:user:control','10002',NULL,NULL,NULL,'用户管理','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(18,'100020401',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:user:detail','1000204',NULL,NULL,NULL,'用户详情','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(19,'100020402',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:user:add','1000204',NULL,NULL,NULL,'新增用户','2022-01-14 16:57:47',NULL,NULL,'2022-01-14 16:57:47',NULL,NULL,NULL),(20,'100020403',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:user:edit','1000204',NULL,NULL,NULL,'编辑用户','2022-01-14 16:57:48',NULL,NULL,'2022-01-14 16:57:48',NULL,NULL,NULL),(21,'100020404',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:user:delete','1000204',NULL,NULL,NULL,'删除用户','2022-01-14 16:57:48',NULL,NULL,'2022-01-14 16:57:48',NULL,NULL,NULL),(22,'1000205',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:system:setting','10002',NULL,NULL,NULL,'系统设置','2022-01-14 16:57:48',NULL,NULL,'2022-01-14 16:57:48',NULL,NULL,NULL),(82,'10007',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basics:log:operation','100',NULL,NULL,NULL,'操作日志','2022-01-14 16:57:50',NULL,NULL,'2022-01-14 16:57:50',NULL,NULL,NULL),(83,'1000701',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basics:log:list','10007',NULL,NULL,NULL,'日志列表','2022-01-14 16:57:50',NULL,NULL,'2022-01-14 16:57:50',NULL,NULL,NULL),(109,'100020104',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:role:delete','1000201',NULL,NULL,NULL,'删除角色','2022-01-19 15:04:52',NULL,NULL,'2022-01-19 15:04:52',NULL,NULL,NULL),(206,'1000206',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'system:notice:control','10002',NULL,NULL,NULL,'公告管理','2022-08-25 16:57:50',NULL,NULL,'2022-08-25 16:57:50',NULL,NULL,NULL),(374,'10005',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'basics:factoryPark:control','100',NULL,NULL,NULL,'厂园区','2023-01-15 16:38:16',NULL,NULL,'2023-01-15 16:38:16',NULL,NULL,NULL),(375,'1000501',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:control','10005',NULL,NULL,NULL,'空间管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(376,'100050101',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:park','1000501',NULL,NULL,NULL,'园区管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(377,'10005010101',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:park:add','100050101',NULL,NULL,NULL,'添加园区','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(378,'10005010102',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:park:edit','100050101',NULL,NULL,NULL,'编辑园区','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(379,'10005010103',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:park:detail','100050101',NULL,NULL,NULL,'园区详情','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(380,'10005010104',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:park:delete','100050101',NULL,NULL,NULL,'删除园区','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(381,'100050102',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:region','1000501',NULL,NULL,NULL,'区域管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(382,'10005010201',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:region:add','100050102',NULL,NULL,NULL,'添加区域','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(384,'10005010203',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:region:edit','100050102',NULL,NULL,NULL,'编辑区域','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(385,'10005010204',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:region:delete','100050102',NULL,NULL,NULL,'删除区域','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(386,'100050103',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:building','1000501',NULL,NULL,NULL,'楼栋管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(387,'10005010301',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:building:add','100050103',NULL,NULL,NULL,'添加楼栋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(389,'10005010303',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:building:edit','100050103',NULL,NULL,NULL,'编辑楼栋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(390,'10005010304',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:building:delete','100050103',NULL,NULL,NULL,'删除楼栋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(391,'10005010305',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:building:batchDelete','100050103',NULL,NULL,NULL,'批量删除楼栋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(392,'100050104',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:Storey','1000501',NULL,NULL,NULL,'楼层管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(393,'10005010401',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:Storey:add','100050104',NULL,NULL,NULL,'添加楼层','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(395,'10005010404',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:Storey:edit','100050104',NULL,NULL,NULL,'编辑楼层','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(396,'10005010405',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:Storey:delete','100050104',NULL,NULL,NULL,'删除楼层','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(397,'10005010406',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:Storey:batchDelete','100050104',NULL,NULL,NULL,'批量删除楼层','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(398,'100050105',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:space:home','1000501',NULL,NULL,NULL,'房屋管理','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(399,'10005010501',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:home:add','100050105',NULL,NULL,NULL,'添加房屋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(401,'10005010503',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:home:edit','100050105',NULL,NULL,NULL,'编辑房屋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(402,'10005010504',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:home:detail','100050105',NULL,NULL,NULL,'查看房屋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL),(403,'10005010505',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'factoryPark:home:delete','100050105',NULL,NULL,NULL,'删除房屋','2023-01-15 16:57:48',NULL,NULL,'2023-01-15 16:57:48',NULL,NULL,NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_role`
--

DROP TABLE IF EXISTS `menu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_role` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
  `pk_menu_id` bigint(20) NOT NULL COMMENT '菜单主键id',
  `pk_role_id` bigint(20) NOT NULL COMMENT '角色主键ID',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型。（1：system、2：用户、3：open api）',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_role`
--

LOCK TABLES `menu_role` WRITE;
/*!40000 ALTER TABLE `menu_role` DISABLE KEYS */;
INSERT INTO `menu_role` VALUES (13412,2,1,'2023-02-15 10:28:22',2,'2'),(13453,1,1,'2023-02-15 10:28:22',2,'2'),(18289,1,2,'2023-06-27 16:47:26',2,'2'),(18290,2,2,'2023-06-27 16:47:26',2,'2'),(18291,3,2,'2023-06-27 16:47:26',2,'2'),(18292,4,2,'2023-06-27 16:47:26',2,'2'),(18293,5,2,'2023-06-27 16:47:26',2,'2'),(18294,6,2,'2023-06-27 16:47:26',2,'2'),(18295,7,2,'2023-06-27 16:47:26',2,'2'),(18296,8,2,'2023-06-27 16:47:26',2,'2'),(18297,9,2,'2023-06-27 16:47:26',2,'2'),(18298,10,2,'2023-06-27 16:47:26',2,'2'),(18299,11,2,'2023-06-27 16:47:26',2,'2'),(18300,12,2,'2023-06-27 16:47:26',2,'2'),(18301,13,2,'2023-06-27 16:47:26',2,'2'),(18302,14,2,'2023-06-27 16:47:26',2,'2'),(18303,15,2,'2023-06-27 16:47:26',2,'2'),(18304,16,2,'2023-06-27 16:47:26',2,'2'),(18305,17,2,'2023-06-27 16:47:26',2,'2'),(18306,18,2,'2023-06-27 16:47:26',2,'2'),(18307,19,2,'2023-06-27 16:47:26',2,'2'),(18308,20,2,'2023-06-27 16:47:26',2,'2'),(18309,21,2,'2023-06-27 16:47:26',2,'2'),(18310,22,2,'2023-06-27 16:47:26',2,'2'),(18334,82,2,'2023-06-27 16:47:26',2,'2'),(18335,83,2,'2023-06-27 16:47:26',2,'2'),(18346,109,2,'2023-06-27 16:47:26',2,'2'),(18376,206,2,'2023-06-27 16:47:26',2,'2'),(18436,374,2,'2023-06-27 16:47:26',2,'2'),(18437,375,2,'2023-06-27 16:47:26',2,'2'),(18438,376,2,'2023-06-27 16:47:26',2,'2'),(18439,379,2,'2023-06-27 16:47:26',2,'2'),(18440,381,2,'2023-06-27 16:47:26',2,'2'),(18441,386,2,'2023-06-27 16:47:26',2,'2'),(18442,392,2,'2023-06-27 16:47:26',2,'2'),(18443,398,2,'2023-06-27 16:47:26',2,'2'),(18444,402,2,'2023-06-27 16:47:26',2,'2');
/*!40000 ALTER TABLE `menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_management`
--

DROP TABLE IF EXISTS `notice_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `org_id` bigint(20) NOT NULL COMMENT '租户id',
  `dept_id` bigint(20) NOT NULL COMMENT '组织id',
  `dept_tree_path` varchar(255) NOT NULL COMMENT '所在组织的层级关系树',
  `notice_content` varchar(512) DEFAULT NULL COMMENT '公告内容',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '公告状态(0:未发布，1:发布)',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态（0：未删除；1：已删除）',
  PRIMARY KEY (`id`),
  KEY `notice_management_org_id_IDX` (`org_id`,`dept_id`,`dept_tree_path`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公告管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_management`
--

LOCK TABLES `notice_management` WRITE;
/*!40000 ALTER TABLE `notice_management` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_user_access`
--

DROP TABLE IF EXISTS `notice_user_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice_user_access` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `notice_id` bigint(20) NOT NULL COMMENT '公告id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志(0：未删除，1：已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='公告用户中间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_user_access`
--

LOCK TABLES `notice_user_access` WRITE;
/*!40000 ALTER TABLE `notice_user_access` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice_user_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_info`
--

DROP TABLE IF EXISTS `org_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `org_key` varchar(32) DEFAULT NULL COMMENT '账户key',
  `org_id` varchar(32) NOT NULL COMMENT '账户id',
  `org_name` varchar(64) DEFAULT NULL COMMENT '账户名称',
  `org_type_id` bigint(20) NOT NULL COMMENT '租户类型id;organization_type.id',
  `status` tinyint(1) DEFAULT NULL COMMENT '账户状态（0：禁用、1：正常）',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级账户id',
  `org_owner` bigint(20) DEFAULT NULL COMMENT '账户所有者',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `social_credit_code` varchar(100) DEFAULT NULL COMMENT '统一社会信用代码',
  `company_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `description` varchar(128) DEFAULT NULL COMMENT '备注',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型（1：system、2：用户、3：open api）',
  `updated_on` datetime DEFAULT NULL COMMENT '修改时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `updated_by_type` tinyint(1) DEFAULT NULL COMMENT '修改者类型（1：system、2：用户、3：open api）',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除 1：已删除）',
  `deleted_on` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `max_dept_high` int(11) DEFAULT NULL COMMENT '组织最大层级',
  `source` tinyint(2) DEFAULT '0' COMMENT '来源 0（楼宇）1（物联应用平台)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_organization_social_credit_code` (`social_credit_code`,`deleted_flag`,`deleted_on`),
  KEY `idx_org_name` (`org_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='租户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_info`
--

LOCK TABLES `org_info` WRITE;
/*!40000 ALTER TABLE `org_info` DISABLE KEYS */;
INSERT INTO `org_info` VALUES (1,'REGISTER','666666','联通物联网有限责任公司',5,1,-1,2,NULL,NULL,NULL,NULL,'2021-11-24 17:11:37','SYSTEM',1,NULL,NULL,NULL,0,'1970-01-01 00:00:00',NULL,3,0);
/*!40000 ALTER TABLE `org_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_label`
--

DROP TABLE IF EXISTS `org_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_label` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `org_id` bigint(20) NOT NULL COMMENT '账户id',
  `label_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `label_name` varchar(32) DEFAULT NULL COMMENT '标签名称',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改者',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COMMENT='账户标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_label`
--

LOCK TABLES `org_label` WRITE;
/*!40000 ALTER TABLE `org_label` DISABLE KEYS */;
INSERT INTO `org_label` VALUES (160,1,13,NULL,NULL,'2023-02-07 17:13:22.000',NULL,NULL);
/*!40000 ALTER TABLE `org_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_menu`
--

DROP TABLE IF EXISTS `org_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `org_id` bigint(20) NOT NULL COMMENT '账户id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_org_id_menu_id` (`org_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=565 DEFAULT CHARSET=utf8 COMMENT='账户菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_menu`
--

LOCK TABLES `org_menu` WRITE;
/*!40000 ALTER TABLE `org_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_role`
--

DROP TABLE IF EXISTS `org_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pk_org_id` bigint(20) NOT NULL COMMENT '租户主键id',
  `pk_role_id` bigint(20) NOT NULL COMMENT '角色主键ID',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  PRIMARY KEY (`id`),
  KEY `pk_org_id` (`pk_org_id`) USING BTREE,
  KEY `pk_role_id` (`pk_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=481 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_role`
--

LOCK TABLES `org_role` WRITE;
/*!40000 ALTER TABLE `org_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_type_menu_root`
--

DROP TABLE IF EXISTS `org_type_menu_root`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_type_menu_root` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `org_type_id` bigint(20) NOT NULL COMMENT '账户类型id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_org_type_id_menu_id` (`org_type_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户类型菜单根节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_type_menu_root`
--

LOCK TABLES `org_type_menu_root` WRITE;
/*!40000 ALTER TABLE `org_type_menu_root` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_type_menu_root` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `org_key` varchar(32) DEFAULT NULL COMMENT '账户key',
  `org_id` varchar(32) NOT NULL COMMENT '账户id',
  `org_name` varchar(64) DEFAULT NULL COMMENT '账户名称',
  `org_type_id` bigint(20) NOT NULL COMMENT '租户类型id;organization_type.id',
  `status` tinyint(1) DEFAULT NULL COMMENT '账户状态（0：禁用、1：正常）',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级账户id',
  `org_owner` bigint(20) DEFAULT NULL COMMENT '账户所有者',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `social_credit_code` varchar(100) DEFAULT NULL COMMENT '统一社会信用代码',
  `company_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `description` varchar(128) DEFAULT NULL COMMENT '备注',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型（1：system、2：用户、3：open api）',
  `updated_on` datetime DEFAULT NULL COMMENT '修改时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `updated_by_type` tinyint(1) DEFAULT NULL COMMENT '修改者类型（1：system、2：用户、3：open api）',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除 1：已删除）',
  `deleted_on` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `max_dept_high` int(11) DEFAULT NULL COMMENT '组织最大层级',
  `source` tinyint(2) DEFAULT '0' COMMENT '来源 0（楼宇）1（物联应用平台)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_organization_social_credit_code` (`social_credit_code`,`deleted_flag`,`deleted_on`),
  KEY `idx_org_name` (`org_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='租户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (1,'REGISTER','22222222','联通物联网有限责任公司',5,1,-1,2,NULL,NULL,NULL,NULL,'2021-11-24 17:11:37','SYSTEM',1,NULL,NULL,NULL,0,'1970-01-01 00:00:00',NULL,3,0);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_type`
--

DROP TABLE IF EXISTS `organization_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` tinyint(1) NOT NULL COMMENT '租户类型',
  `name` varchar(64) NOT NULL COMMENT '租户类型名称',
  `created_on` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者用户id',
  `updated_on` datetime(3) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者用户id',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:未删除 1:已删除)',
  `deleted_on` datetime(3) DEFAULT NULL COMMENT '删除时间',
  `is_show` tinyint(1) DEFAULT '0' COMMENT '前台是否展示（0：不展示； 1：展示）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='租户类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_type`
--

LOCK TABLES `organization_type` WRITE;
/*!40000 ALTER TABLE `organization_type` DISABLE KEYS */;
INSERT INTO `organization_type` VALUES (5,5,'联通账户','2021-11-24 17:05:23.000',1,NULL,NULL,0,NULL,0),(11,11,'物业账户','2022-01-01 17:05:23.000',1,NULL,NULL,0,NULL,1),(12,12,'通用账户','2022-01-01 17:05:23.000',1,NULL,NULL,0,NULL,1);
/*!40000 ALTER TABLE `organization_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `role_id` varchar(32) NOT NULL COMMENT '角色id',
  `role_name` varchar(32) NOT NULL COMMENT '角色名称',
  `role_key` varchar(32) NOT NULL COMMENT '角色key',
  `permit` tinyint(4) NOT NULL COMMENT '角色权限  [0: ADMIN、1: VIEW_ONLY、2：自定义、3：SA、4：SO、5：省SA、6：省SO]',
  `role_type` tinyint(1) DEFAULT NULL COMMENT '角色类型(1:默认角色、2：自定义角色)',
  `description` varchar(128) DEFAULT NULL COMMENT '备注',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
  `created_by` varchar(64) NOT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型【1：SYSTEM; 2: Portal；3：API】',
  `updated_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '角色修改时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '修改者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  `updated_by_type` tinyint(1) DEFAULT NULL COMMENT '修改者类型【1：SYSTEM; 2: Portal；3：API】',
  `delete_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除 1：已删除）',
  `delete_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '删除时间',
  `org_id` bigint(20) NOT NULL COMMENT '租户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_id_role_name` (`org_id`,`role_name`,`delete_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2784 DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'798641702200934402','超级管理员','SUPER_ADMIN',6,0,'超级管理员','2020-10-19 19:41:46','SYSTEM',1,'2021-01-12 20:02:36','2',2,0,'1970-01-01 00:00:00',1),(2,'798641702200934403','系统高级管理员','SUPER_OPERATOR',6,0,'物联网总部管理员','2020-10-19 19:46:50','SYSTEM',1,'2021-01-12 20:02:38','2',2,0,'1970-01-01 00:00:00',1),(2779,'1063390201579044864','厂园区管理员','FACTORY_PARK_ADMIN',6,0,'厂园区管理员','2023-01-13 09:26:00','SYSTEM',1,'2021-01-12 20:02:38','2',2,0,'1970-01-01 00:00:00',1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensitivity`
--

DROP TABLE IF EXISTS `sensitivity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensitivity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '敏感词id',
  `sensitive_word` varchar(100) NOT NULL COMMENT '敏感词',
  `type` tinyint(1) DEFAULT NULL COMMENT '0：补充数据；1：暴恐词；2：反动词；3：广告次；4：民生词；5：敏感词；6：色情词；7：涉枪涉爆违法信息关键词；8：网址；9：政治类；',
  PRIMARY KEY (`id`),
  KEY `sensitiveWord` (`sensitive_word`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensitivity`
--

LOCK TABLES `sensitivity` WRITE;
/*!40000 ALTER TABLE `sensitivity` DISABLE KEYS */;
/*!40000 ALTER TABLE `sensitivity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_param`
--

DROP TABLE IF EXISTS `sys_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `org_id` bigint(20) NOT NULL COMMENT '账户id',
  `title` varchar(50) DEFAULT NULL COMMENT '系统title',
  `logo_id` int(11) DEFAULT NULL COMMENT '系统logo（1：通用； 2：预置）',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `updated_on` datetime DEFAULT NULL COMMENT '修改时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `dept_tree_path` varchar(255) NOT NULL COMMENT '组织层级树',
  `updater_path` varchar(255) DEFAULT NULL COMMENT '编辑者所在组织',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_tree_path` (`dept_tree_path`)
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_param`
--

LOCK TABLES `sys_param` WRITE;
/*!40000 ALTER TABLE `sys_param` DISABLE KEYS */;
INSERT INTO `sys_param` VALUES (195,1,'XXXX',2,'2024-03-21 16:55:52','2',NULL,NULL,'00','00');
/*!40000 ALTER TABLE `sys_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT 'hash密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(100) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(50) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `last_online_ip` varchar(64) DEFAULT NULL COMMENT '用户最近登录ip',
  `last_online_address` varchar(200) DEFAULT NULL COMMENT '用户最近登录地址',
  `last_online_on` datetime DEFAULT NULL COMMENT '用户最后上线时间',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。',
  `created_by_type` tinyint(1) DEFAULT NULL COMMENT '创建者类型（1：system、2：用户、3：open api）',
  `updated_on` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。',
  `updated_by_type` tinyint(1) DEFAULT NULL COMMENT '更新者类型（1：system、2：用户、3：open api）',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除 1：已删除）',
  `deleted_on` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted_by` varchar(64) DEFAULT NULL COMMENT '删除人id',
  `user_type` tinyint(1) DEFAULT NULL COMMENT '用户类型（1：实体用户、2：业主用户,3：门禁人员、4：门禁访客、5：个人用户 ）',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_address` varchar(255) DEFAULT NULL COMMENT '联系人地址',
  `activate_sms_reminder_flag` tinyint(1) DEFAULT NULL COMMENT '0：开通 1：不开通',
  `long_time_login` tinyint(1) DEFAULT '0' COMMENT '0：关闭，1：开启',
  `delete_by_type` tinyint(1) DEFAULT NULL COMMENT '删除者类型（1：system、2：用户、3：open api）',
  PRIMARY KEY (`id`),
  KEY `idx_user_type_deleted_flag` (`user_type`,`deleted_flag`) USING BTREE,
  KEY `idx_username_phone_number` (`username`,`phone_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'00000000','root','BCF942BD54D8F4CE49434A1C7691B2131B2139E8306EA090C430E9D7402C9C5E',NULL,NULL,NULL,0,'172.30.108.6',NULL,'2024-03-15 09:59:47','2021-11-15 11:02:17',NULL,NULL,'2024-03-15 09:59:47','system',1,0,'2022-01-06 08:54:13',NULL,1,NULL,NULL,NULL,0,NULL),(2,'1','admin','fjBsOLqzRrV0oa5260+Q2oFEQ15Q4zc+kTo1TPJcaWI4lrxi2aPY/ke/CLWUKTOu',NULL,NULL,NULL,0,'127.0.0.1',NULL,'2024-03-26 17:02:34','2021-11-24 17:19:58',NULL,NULL,'2024-03-26 17:02:41','system',1,0,NULL,NULL,1,NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_grant`
--

DROP TABLE IF EXISTS `user_grant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_grant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `org_id` varchar(32) NOT NULL COMMENT '账户id',
  `privileges` varchar(100) DEFAULT NULL COMMENT '权限',
  `to_org_id` bigint(20) NOT NULL COMMENT '目标租户',
  `to_dept_id` bigint(20) DEFAULT NULL COMMENT '目标组织',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '目标用户',
  PRIMARY KEY (`id`),
  KEY `idx_org_id` (`org_id`) USING BTREE,
  KEY `idx_to_dept_id` (`to_org_id`,`to_dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='租户授权表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_grant`
--

LOCK TABLES `user_grant` WRITE;
/*!40000 ALTER TABLE `user_grant` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_grant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_label`
--

DROP TABLE IF EXISTS `user_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_label` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `label_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `label_name` varchar(32) DEFAULT NULL COMMENT '标签名称',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改者',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='用户标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_label`
--

LOCK TABLES `user_label` WRITE;
/*!40000 ALTER TABLE `user_label` DISABLE KEYS */;
INSERT INTO `user_label` VALUES (1,'1',6,NULL,NULL,'2022-11-25 09:34:53.334',NULL,NULL),(2,'2',6,NULL,NULL,'2022-11-25 09:34:53.334',NULL,NULL);
/*!40000 ALTER TABLE `user_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_operate_log`
--

DROP TABLE IF EXISTS `user_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '账户pkid',
  `request_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  `request_ip` varchar(128) NOT NULL COMMENT '请求者ip',
  `operation_code` varchar(128) NOT NULL COMMENT '操作code',
  `operation_name` varchar(128) NOT NULL COMMENT '操作名称',
  `operation_by_id` varchar(30) DEFAULT NULL COMMENT '操作人',
  `operation_by_name` varchar(128) DEFAULT NULL COMMENT '操作人',
  `operation_target` varchar(128) DEFAULT NULL COMMENT '操作对象',
  `service_type` int(10) DEFAULT NULL COMMENT '服务类别',
  `log_level` varchar(30) NOT NULL COMMENT '日志等级',
  `file` longblob COMMENT '二进制文件',
  `status_code` varchar(30) NOT NULL COMMENT '状态码',
  `status_msg` varchar(128) NOT NULL COMMENT '状态描述',
  `request_params` varchar(2048) DEFAULT NULL COMMENT '请求参数',
  `response_params` varchar(2048) DEFAULT NULL COMMENT '响应参数',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=304 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_operate_log`
--

LOCK TABLES `user_operate_log` WRITE;
/*!40000 ALTER TABLE `user_operate_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_operate_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_org`
--

DROP TABLE IF EXISTS `user_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pk_user_id` bigint(20) NOT NULL COMMENT '用户主键id',
  `pk_org_id` bigint(20) NOT NULL COMMENT '账户主键id',
  `pk_dept_id` bigint(20) DEFAULT NULL COMMENT '租户下的组织id,department_id',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  PRIMARY KEY (`id`),
  KEY `pk_org_id` (`pk_org_id`) USING BTREE,
  KEY `pk_user_id` (`pk_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1 COMMENT='用户组织关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_org`
--

LOCK TABLES `user_org` WRITE;
/*!40000 ALTER TABLE `user_org` DISABLE KEYS */;
INSERT INTO `user_org` VALUES (1,1,1,1,'2021-08-10 09:14:14',NULL),(2,2,1,1,'2021-11-24 17:26:44',NULL);
/*!40000 ALTER TABLE `user_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `pk_user_id` bigint(20) DEFAULT NULL COMMENT '用户自增ID',
  `pk_role_id` bigint(20) NOT NULL COMMENT '角色自增ID',
  `pk_org_id` bigint(20) NOT NULL COMMENT '租户自增id',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) NOT NULL COMMENT '创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的',
  PRIMARY KEY (`id`),
  KEY `pk_role_id` (`pk_role_id`) USING BTREE,
  KEY `pk_user_id` (`pk_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1,1,'2021-09-02 10:34:51','system'),(2,2,2,1,'2021-11-24 17:26:12','system');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'community_pvt_system'
--
/*!50003 DROP FUNCTION IF EXISTS `get_full_space_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`boardserver`@`%` FUNCTION `get_full_space_name`(dept_path_tree varchar(256)) RETURNS varchar(4096) CHARSET utf8
begin
    declare path_name varchar(512);
    declare temp_name varchar(256);
    declare temp_parent_id varchar(256);
    declare temp_current_id varchar(256);
    declare temp_group_id tinyint(4);
    select d.id, d.department_name, d.parent_id,d.d_group into temp_current_id,temp_name, temp_parent_id, temp_group_id from department d where d.path = dept_path_tree;
    if temp_group_id = 5 then
        select
            h.name
        into path_name
        from house h left join organization o on h.pk_org_id = o.id left join org_label ol on ol.org_id = o.id
        where h.self_dept_id = temp_current_id;
    else
        set path_name = CONCAT_WS('/', temp_name, path_name);
    end if;

    while temp_parent_id is not null and temp_group_id > 2 do
            select d.department_name, d.parent_id, d.d_group into temp_name, temp_parent_id, temp_group_id from department d where d.id = temp_parent_id;
            if temp_group_id > 2 then
                set path_name = CONCAT_WS('/', temp_name, path_name);
            end if;
        end while;
    return path_name;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-26 17:59:34
