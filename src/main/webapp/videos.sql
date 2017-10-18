/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50717
 Source Host           : localhost
 Source Database       : videos

 Target Server Version : 50717
 File Encoding         : utf-8

 Date: 10/16/2017 15:43:29 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `video_job`
-- ----------------------------
DROP TABLE IF EXISTS `video_job`;
CREATE TABLE `video_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `srcpath` varchar(500) NOT NULL,
  `despath` varchar(500) DEFAULT '',
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `video_job`
-- ----------------------------
BEGIN;
INSERT INTO `video_job` VALUES ('32', '/Users/jieli/Documents/workfile/videotest/upload/1508138063230.mp4', '/Users/jieli/Documents/workfile/videotest/out/1508138063460.mp4', '1'), ('33', '/Users/jieli/Documents/workfile/videotest/upload/1508138206624.mp4', '/Users/jieli/Documents/workfile/videotest/out/1508138206866.mp4', '1'), ('34', '/Users/jieli/Documents/workfile/videotest/upload/1508139599559.mp4', '/Users/jieli/Documents/workfile/videotest/out/1508139599983.mp4', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
