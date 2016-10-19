/*
Source Server         : MySQL-Local
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : tickets-tracker-db

Target Server Type    : MYSQL
*/

CREATE DATABASE IF NOT EXISTS `tickets-tracker-db`;

USE `tickets-tracker-db`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ticket_master`
-- ----------------------------
DROP TABLE IF EXISTS `ticket_master`;
CREATE TABLE `ticket_master` (
  `TICKET_ID` varchar(10) NOT NULL,
  `TICKET_TYPE` varchar(15) NOT NULL,
  `TICKET_OWNER` varchar(30) NOT NULL,
  `ASSIGNED_TO_TEAM` varchar(20) NOT NULL,
  `ASSIGNEE` varchar(30) NOT NULL,
  `CREATED_DATE` varchar(10) NOT NULL,
  `RESOLVED_DATE` varchar(10) DEFAULT NULL,
  `TICKET_STATUS` varchar(15) NOT NULL,
  PRIMARY KEY (`TICKET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of ticket_master
-- ----------------------------
INSERT INTO `ticket_master` VALUES ('MYT-1137', 'Issue', 'Andy Thomas', 'SL', 'Arun Kulkarni', '2016-10-18', null, 'In Progress');
INSERT INTO `ticket_master` VALUES ('MYT-1188', 'Change Request', 'Andy Thomas', 'SL', 'Arun Kulkarni', '2016-10-11', '2016-10-13', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1198', 'Change Request', 'Jon Estes', 'SL', 'Niharika Sharma', '2016-10-10', null, 'Testing');
INSERT INTO `ticket_master` VALUES ('MYT-1211', 'Issue', 'Priscilla Wilde', 'DB', 'Vinay Patil', '2016-10-06', '2016-10-17', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1220', 'Development', 'Andy Thomas', 'SL', 'Sameer Joshi', '2016-10-11', '2016-10-18', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1221', 'Issue', 'Brandon Anderson', 'UI', 'Arvind Joshi', '2016-10-03', '2016-10-06', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1232', 'Development', 'JC Thomas', 'UI', 'Neha Gaikwad', '2016-10-14', '2016-10-19', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1301', 'Development', 'Chuck Beck', 'SL', 'Arun Kulkarni', '2016-10-01', '2016-10-10', 'Resolved');
INSERT INTO `ticket_master` VALUES ('MYT-1364', 'Development', 'AJ Child', 'SL', 'Mehul Kukreja', '2016-10-10', '2016-10-12', 'Resolved');
