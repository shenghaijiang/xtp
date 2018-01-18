/*
Navicat MySQL Data Transfer

Source Server         : root@139.196.125.197@!QQ1234567890-=q
Source Server Version : 50720
Source Host           : 139.196.125.197:3306
Source Database       : git-xtp

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-01-18 16:34:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(50) DEFAULT NULL COMMENT '编码-code',
  `Name` varchar(50) DEFAULT NULL,
  `Token` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES ('1', 'xtp', 'xtp权限管理', 'CJzdWIiOiJ7XCJ1c2VySDkwMTUeyJhbGciOiJI');

-- ----------------------------
-- Table structure for appfavorite
-- ----------------------------
DROP TABLE IF EXISTS `appfavorite`;
CREATE TABLE `appfavorite` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AppId` int(11) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of appfavorite
-- ----------------------------

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `Type` varchar(50) NOT NULL DEFAULT '' COMMENT '类型',
  `Key` varchar(50) NOT NULL DEFAULT '' COMMENT '键',
  `Value` varchar(100) NOT NULL DEFAULT '' COMMENT '值',
  `CreateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据字典';

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES ('1', 'datarule_symbol', '=', '=', '2016-03-01 22:42:05');
INSERT INTO `dict` VALUES ('2', 'datarule_symbol', '>', '>', '2016-03-01 22:42:21');
INSERT INTO `dict` VALUES ('3', 'datarule_symbol', '<', '<', '2016-03-01 22:42:28');
INSERT INTO `dict` VALUES ('6', 'db_source', 'zcurd_base', '基本库', '2016-09-18 17:25:49');
INSERT INTO `dict` VALUES ('7', 'db_source', 'zcurd_busi', '业务库', '2016-09-18 17:25:54');

-- ----------------------------
-- Table structure for ext
-- ----------------------------
DROP TABLE IF EXISTS `ext`;
CREATE TABLE `ext` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tableName` int(11) DEFAULT NULL,
  `columnName` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ext
-- ----------------------------

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `Id` int(50) NOT NULL AUTO_INCREMENT,
  `Type` varchar(50) DEFAULT NULL,
  `Centent` varchar(50) DEFAULT NULL,
  `UserId` int(50) DEFAULT NULL,
  `UserName` varchar(50) DEFAULT NULL,
  `CreateDate` varchar(50) DEFAULT NULL,
  `IP` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AppId` int(11) DEFAULT NULL,
  `Code` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `URL` varchar(50) DEFAULT NULL,
  `ParentId` int(11) DEFAULT NULL,
  `Icon` varchar(50) DEFAULT NULL,
  `ParentFlag` bit(1) DEFAULT NULL,
  `DisplayFlag` bit(1) DEFAULT NULL,
  `Sort` int(11) DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  `Type` int(6) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '1', 'app', '应用管理', '/app', '0', 'fa fa-address-book-o', '', '', '1', '\0', null);
INSERT INTO `menu` VALUES ('2', '1', 'userinfo', '个人信息设置', '/userinfo', '0', 'fa fa-trophy', '', '', '0', '\0', null);
INSERT INTO `menu` VALUES ('3', '1', 'menu', '功能管理', '/menu', '0', 'fa fa-object-ungroup', '', '', '2', '\0', null);
INSERT INTO `menu` VALUES ('4', '1', 'user', '用户管理', '/user', '0', 'fa fa-bank', '', '', '4', '\0', null);
INSERT INTO `menu` VALUES ('5', '1', 'role', '角色管理', '/role', '0', 'fa fa-arrows', '', '', '3', '\0', null);
INSERT INTO `menu` VALUES ('6', '1', 'menuoperation', '功能权限', '/newmenuoperation', '0', 'fa fa-arrows', '', '', '7', '\0', null);
INSERT INTO `menu` VALUES ('7', '1', 'rolemenuoperation', '功能权限(角色)', '/roledatarule', '0', 'fa fa-arrows', '', '', '8', '\0', null);
INSERT INTO `menu` VALUES ('8', '1', 'usermenu', '用户功能管理', '/usermenu2', '0', 'fa fa-arrows', '', '', '6', '\0', null);
INSERT INTO `menu` VALUES ('9', '1', 'usermenuoperation', '功能权限(用户)', '/userbuttonrule', '0', 'fa fa-arrows', '', '', '9', '\0', null);
INSERT INTO `menu` VALUES ('10', '1', 'xtp二级菜单', 'xtp二级菜单', 'xtp二级菜单', '1', 'fa fa-tag', '\0', '', '1', '', null);
INSERT INTO `menu` VALUES ('11', '1', 'xtp三级菜单', 'xtp三级菜单', 'xtp三级菜单', '10', 'fa fa-tag', '\0', '', '0', '', null);
INSERT INTO `menu` VALUES ('12', '1', 'menuoperator', '通用权限', '/menuoperator', '0', 'fa fa-suitcase', '', '', '5', '\0', null);

-- ----------------------------
-- Table structure for menuoperation
-- ----------------------------
DROP TABLE IF EXISTS `menuoperation`;
CREATE TABLE `menuoperation` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  `Icon` varchar(50) DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menuoperation
-- ----------------------------
INSERT INTO `menuoperation` VALUES ('1', 'view', '查询', '0', 'fa fa-adjust', '\0');
INSERT INTO `menuoperation` VALUES ('2', 'insert', '添加', '0', 'fa fa-edit', '\0');
INSERT INTO `menuoperation` VALUES ('3', 'add', '新增', '0', 'fa fa-address-book', '\0');
INSERT INTO `menuoperation` VALUES ('4', 'update', '修改', '0', 'fa fa-thermometer-quarter', '\0');

-- ----------------------------
-- Table structure for organize
-- ----------------------------
DROP TABLE IF EXISTS `organize`;
CREATE TABLE `organize` (
  `Id` int(11) NOT NULL,
  `AppId` int(11) DEFAULT NULL,
  `Code` varchar(20) DEFAULT NULL,
  `ParentId` varchar(20) DEFAULT NULL,
  `FullName` varchar(30) DEFAULT NULL,
  `Category` varchar(20) DEFAULT NULL,
  `OuterPhone` varchar(100) DEFAULT NULL,
  `InnerPhone` varchar(100) DEFAULT NULL,
  `Fax` varchar(20) DEFAULT NULL,
  `Postalcode` varchar(20) DEFAULT NULL,
  `Address` varchar(20) DEFAULT NULL,
  `Web` varchar(20) DEFAULT NULL,
  `Manager` varchar(20) DEFAULT NULL,
  `AssistantManager` varchar(20) DEFAULT NULL,
  `InnerOrganizeFlag` bit(1) DEFAULT NULL,
  `Sort` int(11) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `EnableFlag` bit(1) DEFAULT NULL,
  `CreateUserID` varchar(20) DEFAULT NULL,
  `ModifyDate` datetime DEFAULT NULL,
  `AccountBook` varchar(255) DEFAULT NULL COMMENT '账套',
  `Level` int(11) DEFAULT NULL COMMENT '组织级别',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of organize
-- ----------------------------
INSERT INTO `organize` VALUES ('10000223', '1', '001.02', '100000008', '办公室', 'Department', '', '', '', null, null, null, '', '', '', '10000460', '', '', 'Administrator', '2009-02-20 18:49:00', null, null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  `CreateDate` datetime DEFAULT NULL,
  `ModifyDate` datetime DEFAULT NULL,
  `AppId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员', '管理员', '\0', '2017-03-14 16:29:59', '2017-06-30 16:20:11', '1');

-- ----------------------------
-- Table structure for roledatarule
-- ----------------------------
DROP TABLE IF EXISTS `roledatarule`;
CREATE TABLE `roledatarule` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `RoleId` int(11) NOT NULL COMMENT '角色',
  `Value` varchar(50) NOT NULL COMMENT 'sql条件值',
  `CreateDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `Symbol` varchar(255) DEFAULT NULL COMMENT '符号',
  `MenuId` int(11) DEFAULT NULL COMMENT '菜单',
  `FieldName` varchar(50) DEFAULT NULL COMMENT '字段名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色数据规则';

-- ----------------------------
-- Records of roledatarule
-- ----------------------------

-- ----------------------------
-- Table structure for rolemenu
-- ----------------------------
DROP TABLE IF EXISTS `rolemenu`;
CREATE TABLE `rolemenu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `RoleId` int(11) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rolemenu
-- ----------------------------
INSERT INTO `rolemenu` VALUES ('27', '1', '2');
INSERT INTO `rolemenu` VALUES ('28', '1', '1');
INSERT INTO `rolemenu` VALUES ('29', '1', '10');
INSERT INTO `rolemenu` VALUES ('30', '1', '11');
INSERT INTO `rolemenu` VALUES ('31', '1', '3');
INSERT INTO `rolemenu` VALUES ('32', '1', '5');
INSERT INTO `rolemenu` VALUES ('33', '1', '4');
INSERT INTO `rolemenu` VALUES ('34', '1', '12');
INSERT INTO `rolemenu` VALUES ('35', '1', '6');
INSERT INTO `rolemenu` VALUES ('36', '1', '8');
INSERT INTO `rolemenu` VALUES ('37', '1', '7');
INSERT INTO `rolemenu` VALUES ('38', '1', '9');

-- ----------------------------
-- Table structure for rolemenuoperation
-- ----------------------------
DROP TABLE IF EXISTS `rolemenuoperation`;
CREATE TABLE `rolemenuoperation` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `RoleId` int(11) DEFAULT NULL,
  `MenuOperationId` int(11) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  `Code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rolemenuoperation
-- ----------------------------

-- ----------------------------
-- Table structure for roleuser
-- ----------------------------
DROP TABLE IF EXISTS `roleuser`;
CREATE TABLE `roleuser` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roleuser
-- ----------------------------
INSERT INTO `roleuser` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AppUserId` int(11) DEFAULT NULL COMMENT '在具体应用User表中的Id',
  `AppId` int(11) DEFAULT NULL,
  `OrganizeId` int(11) DEFAULT NULL,
  `OrganizeIdName` varchar(50) DEFAULT NULL,
  `Account` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Mail` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
  `QQ` varchar(50) DEFAULT NULL,
  `CreateDate` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ModifyDate` datetime DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `Birth` date DEFAULT NULL COMMENT '出生年月',
  `EducationId` int(11) DEFAULT NULL COMMENT '学历',
  `DepartmentId` int(11) DEFAULT NULL COMMENT '所属部门ID',
  `PostId` int(11) DEFAULT NULL COMMENT '所属岗位ID',
  `Quality` varchar(50) DEFAULT NULL COMMENT '资质',
  `WorkTypeId` int(11) DEFAULT NULL COMMENT '工种ID',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1', '1', null, null, 'admin', '123456', '管理员', '', '', '', '2017-09-21 14:24:30', '2017-06-30 16:20:04', '\0', '38816', '2017-06-06', '1', '1', '1', '资质', '1');

-- ----------------------------
-- Table structure for userext
-- ----------------------------
DROP TABLE IF EXISTS `userext`;
CREATE TABLE `userext` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AppUserId` int(11) DEFAULT NULL COMMENT '在具体应用User表中的Id',
  `AppId` int(11) DEFAULT NULL,
  `OrganizeId` int(11) DEFAULT NULL,
  `OrganizeIdName` varchar(50) DEFAULT NULL,
  `Account` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Mail` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
  `QQ` varchar(50) DEFAULT NULL,
  `CreateDate` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ModifyDate` datetime DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `Birth` date DEFAULT NULL COMMENT '出生年月',
  `EducationId` int(11) DEFAULT NULL COMMENT '学历',
  `DepartmentId` int(11) DEFAULT NULL COMMENT '所属部门ID',
  `PostId` int(11) DEFAULT NULL COMMENT '所属岗位ID',
  `Quality` varchar(50) DEFAULT NULL COMMENT '资质',
  `WorkTypeId` int(11) DEFAULT NULL COMMENT '工种ID',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userext
-- ----------------------------

-- ----------------------------
-- Table structure for userfavorite
-- ----------------------------
DROP TABLE IF EXISTS `userfavorite`;
CREATE TABLE `userfavorite` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` int(11) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  `Type` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userfavorite
-- ----------------------------

-- ----------------------------
-- Table structure for usermenu
-- ----------------------------
DROP TABLE IF EXISTS `usermenu`;
CREATE TABLE `usermenu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` int(11) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  `Type` int(6) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usermenu
-- ----------------------------

-- ----------------------------
-- Table structure for usermenuoperation
-- ----------------------------
DROP TABLE IF EXISTS `usermenuoperation`;
CREATE TABLE `usermenuoperation` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` int(11) DEFAULT NULL,
  `MenuOperationId` int(11) DEFAULT NULL,
  `Code` varchar(50) DEFAULT NULL,
  `Type` int(6) DEFAULT NULL,
  `MenuId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usermenuoperation
-- ----------------------------
