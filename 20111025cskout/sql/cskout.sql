-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: Oct 25, 2011, 10:45 AM
-- 伺服器版本: 5.0.51
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- 資料庫: `cskout`
-- 

-- --------------------------------------------------------

-- 
-- 資料表格式： `flirtsbuzz`
-- 

CREATE TABLE `flirtsbuzz` (
  `flirtsNo` int(7) unsigned zerofill NOT NULL auto_increment,
  `flirtsUpDate` datetime NOT NULL default '0000-00-00 00:00:00',
  `flirtsCon` varchar(255) character set utf8 collate utf8_unicode_ci NOT NULL default '',
  `flirtsType` enum('text','photo','gift') character set utf8 collate utf8_unicode_ci NOT NULL default 'text',
  `memberEmail` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`flirtsNo`),
  KEY `memberEmail` (`memberEmail`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=31 ;

-- 
-- 列出以下資料庫的數據： `flirtsbuzz`
-- 

INSERT INTO `flirtsbuzz` VALUES (0000001, '2011-10-18 00:00:00', 'YO', 'text', '5465@45646');
INSERT INTO `flirtsbuzz` VALUES (0000002, '2011-10-16 11:18:56', 'wjj', 'text', '5465@45646');
INSERT INTO `flirtsbuzz` VALUES (0000003, '2011-10-16 11:18:57', 'wj', 'text', '5465@45646');
INSERT INTO `flirtsbuzz` VALUES (0000004, '2011-10-16 11:21:46', 'mt', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000005, '2011-10-16 11:24:06', 'mj', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000006, '2011-10-17 08:13:58', 'hello!', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000007, '2011-10-17 08:20:21', '??', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000009, '2011-10-17 08:30:49', '中文', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000010, '2011-10-17 08:36:50', '??????', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000011, '2011-10-17 08:39:26', '???????', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000012, '2011-10-17 08:40:19', '?????', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000013, '2011-10-17 08:53:46', '?', 'text', '74876@546');
INSERT INTO `flirtsbuzz` VALUES (0000014, '2011-10-17 08:54:34', '?', 'text', '74876@546');
INSERT INTO `flirtsbuzz` VALUES (0000015, '2011-10-17 08:54:57', '?', 'text', '74876@546');
INSERT INTO `flirtsbuzz` VALUES (0000016, '2011-10-17 09:00:18', '中', 'text', '74876@546');
INSERT INTO `flirtsbuzz` VALUES (0000017, '2011-10-17 09:00:44', '勿了!是不是我就在於法有據不', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000019, '2011-10-18 12:13:01', '撤回', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000024, '2011-10-18 12:59:46', 'j tjt j tj   j', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000025, '2011-10-18 02:21:36', 'u', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000026, '2011-10-18 02:21:59', '687568', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000027, '2011-10-18 08:58:53', 'jtw', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000028, '2011-10-24 11:19:49', '123', 'text', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000029, '2011-10-24 00:00:00', '123@123-111024040312.jpg', 'photo', '123@123');
INSERT INTO `flirtsbuzz` VALUES (0000030, '2011-10-24 02:36:11', 'bbb', 'text', '123@123');

-- --------------------------------------------------------

-- 
-- 資料表格式： `member`
-- 

CREATE TABLE `member` (
  `memberEmail` varchar(50) NOT NULL default '',
  `memberGender` enum('Female','Male') NOT NULL default 'Female',
  `memberName` varchar(50) NOT NULL default '',
  `memberBD` date NOT NULL default '0000-00-00',
  `memberInterested` enum('Female','Male','Both') NOT NULL default 'Both',
  `memberStatus` enum('ON','OFF') NOT NULL default 'OFF',
  `memberPWD` varchar(50) NOT NULL default '',
  `picName` varchar(255) NOT NULL default '',
  `memberLat` double NOT NULL default '0',
  `memberLon` double NOT NULL default '0',
  `memberABme` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`memberEmail`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- 列出以下資料庫的數據： `member`
-- 

INSERT INTO `member` VALUES ('123@123', 'Male', 'Honm', '1981-03-01', 'Male', 'ON', '123123', 'e2.png', 0, 0, 'Catch me!n');
INSERT INTO `member` VALUES ('5465@45646', 'Male', 'Don', '1993-10-12', 'Male', 'ON', '46546', 'sample_1.png', 0, 0, 'Come on');
INSERT INTO `member` VALUES ('5556@55', 'Female', '55', '2004-10-01', 'Both', 'OFF', '5555', 'e1.png', 0, 0, '');
INSERT INTO `member` VALUES ('74876@546', 'Female', 'HJIU', '1988-10-31', 'Male', 'OFF', '748746', 'sample_3.png', 0, 0, 'hello~');
INSERT INTO `member` VALUES ('fad@5465', 'Female', 'Tommy', '1986-10-23', 'Male', 'ON', 'dslfk', 'sample_6.png', 0, 0, 'what are you looking for??');
INSERT INTO `member` VALUES ('jweoifweh@hshfi', 'Female', 'Ui', '1991-04-19', 'Female', 'OFF', 'ishgkoip', 'sample_5.png', 0, 0, 'see?');
INSERT INTO `member` VALUES ('sdfs@asf', 'Female', 'Tut', '1987-08-18', 'Female', 'ON', '7876454', 'sample_4.png', 0, 0, 'yo~man');

-- --------------------------------------------------------

-- 
-- 資料表格式： `pic`
-- 

CREATE TABLE `pic` (
  `picName` varchar(255) NOT NULL default '',
  `picUpDate` datetime NOT NULL,
  `memberEmail` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`picName`),
  KEY `pic_ibfk_1` (`memberEmail`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- 列出以下資料庫的數據： `pic`
-- 

INSERT INTO `pic` VALUES ('e1.png', '0000-00-00 00:00:00', '5556@55');
INSERT INTO `pic` VALUES ('e2.png', '0000-00-00 00:00:00', '123@123');
INSERT INTO `pic` VALUES ('e3.png', '0000-00-00 00:00:00', 'sdfs@asf');
INSERT INTO `pic` VALUES ('e4.png', '0000-00-00 00:00:00', 'sdfs@asf');
INSERT INTO `pic` VALUES ('e5.png', '0000-00-00 00:00:00', 'sdfs@asf');
INSERT INTO `pic` VALUES ('sample_1.png', '0000-00-00 00:00:00', '5465@45646');
INSERT INTO `pic` VALUES ('sample_2.png', '0000-00-00 00:00:00', 'fad@5465');
INSERT INTO `pic` VALUES ('sample_3.png', '0000-00-00 00:00:00', '74876@546');
INSERT INTO `pic` VALUES ('sample_4.png', '0000-00-00 00:00:00', 'sdfs@asf');
INSERT INTO `pic` VALUES ('sample_5.png', '0000-00-00 00:00:00', 'jweoifweh@hshfi');
INSERT INTO `pic` VALUES ('sample_6.png', '0000-00-00 00:00:00', 'fad@5465');
INSERT INTO `pic` VALUES ('123@123-111024040312.jpg', '0000-00-00 00:00:00', '123@123');
