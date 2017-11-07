/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50717
 Source Host           : localhost
 Source Database       : testvideos

 Target Server Version : 50717
 File Encoding         : utf-8

 Date: 11/07/2017 11:01:18 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `video_job`
-- ----------------------------
DROP TABLE IF EXISTS `video_job`;
CREATE TABLE `video_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `filename` varchar(500) NOT NULL DEFAULT '' COMMENT '文件名',
  `command` varchar(1000) NOT NULL DEFAULT '' COMMENT '生成的ffmpeg命令的字符串',
  `srcpath` varchar(1000) DEFAULT '' COMMENT '源视频地址(绝对路径)',
  `despath` varchar(1000) DEFAULT '' COMMENT '转码视频地址(绝对路径)',
  `desurl` varchar(500) DEFAULT '' COMMENT '视频转码成功后地址(相对路径)',
  `srcurl` varchar(500) DEFAULT '' COMMENT '视频源上传地址(相对路径)',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '视频状态 0:等待转码中 1:转码进行中 2:转码成功 3:转码失败 4:转码成功回调失败',
  `counts` int(11) NOT NULL DEFAULT '0' COMMENT '转码次数 最多转3次',
  `msg` varchar(1000) DEFAULT '' COMMENT '转码错误信息，如果有错误信息就写入',
  `callmsg` varchar(1000) DEFAULT '' COMMENT '回调错误信息，返回接收到回调接口的信息',
  `videokey` varchar(100) NOT NULL DEFAULT '' COMMENT '视频唯一值,uuid 格式,用来转码成功后告知服务器更新哪个视频',
  `inputtime` bigint(20) NOT NULL DEFAULT '0' COMMENT '视频插入时间',
  `updatetime` bigint(20) NOT NULL DEFAULT '0' COMMENT '视频更新时间（开始转码，转码成功或转码失败）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
