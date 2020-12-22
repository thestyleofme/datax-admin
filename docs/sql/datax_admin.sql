/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql5.7.26
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : datax_admin

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 22/12/2020 11:04:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for datax_statistics
-- ----------------------------
DROP TABLE IF EXISTS `datax_statistics`;
CREATE TABLE `datax_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exec_id` int(11) NULL DEFAULT NULL COMMENT '执行id',
  `json_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'datax执行的json文件名',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'job名称',
  `reader_plugin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'reader插件名称',
  `writer_plugin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'writer插件名称',
  `start_time` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务启动时刻',
  `end_time` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务结束时刻',
  `total_costs` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务总计耗时，单位s',
  `byte_speed_per_second` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务平均流量',
  `record_speed_per_second` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '记录写入速度',
  `total_read_records` bigint(20) NULL DEFAULT NULL COMMENT '读出记录总数',
  `total_error_records` bigint(20) NULL DEFAULT NULL COMMENT '读写失败总数',
  `job_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'datax执行的json路径',
  `job_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'datax的json内容',
  `dirty_records` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '脏数据即未同步成功的数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for datax_sync
-- ----------------------------
DROP TABLE IF EXISTS `datax_sync`;
CREATE TABLE `datax_sync`  (
  `sync_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表ID，主键，供其他表做外键',
  `sync_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '同步名称',
  `sync_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '同步类型(OPTION:手工配置/IMPORT:脚本配置)',
  `source_datasource_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '来源数据源类型',
  `source_datasource_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '来源数据源编码',
  `source_schema` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '来源数据库',
  `source_table` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '来源表（或视图/文件路径）',
  `write_datasource_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写入数据源类型',
  `write_datasource_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写入数据源编码',
  `write_schema` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写入数据库',
  `write_table` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '写入表（文件路径）',
  `source_object_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '来源对象类型 TABLE/VIEW/SQL',
  `query_sql` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '来源sql',
  `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
  `setting_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '数据配置信息',
  `object_version_number` bigint(20) NULL DEFAULT 1 COMMENT '行版本号，用来处理锁',
  `creation_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `created_by` int(11) NOT NULL DEFAULT -1,
  `last_updated_by` int(11) NOT NULL DEFAULT -1,
  `last_update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`sync_id`) USING BTREE,
  UNIQUE INDEX `datax_sync_u1`(`sync_name`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'DATAX数据同步表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plugin
-- ----------------------------
DROP TABLE IF EXISTS `plugin`;
CREATE TABLE `plugin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件id(code)',
  `plugin_description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `plugin_version` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件版本，如1.0.0',
  `plugin_big_class` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件大类(类型,如DATASOURCE)',
  `plugin_small_class` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件小类(类型,如MYSQL)',
  `plugin_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '插件包路径',
  `object_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '插件文件名(规则: plugin_id@plugin_version.jar)',
  `plugin_fingerprint` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '插件包指纹',
  `enabled_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 1:启用 0：不启用',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
  `object_version_number` bigint(20) NOT NULL DEFAULT 1 COMMENT '行版本号，用来处理锁',
  `creation_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `created_by` int(11) NOT NULL DEFAULT -1,
  `last_updated_by` int(11) NOT NULL DEFAULT -1,
  `last_update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `plugin_driver_u1`(`plugin_id`, `plugin_version`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plugin_datasource
-- ----------------------------
DROP TABLE IF EXISTS `plugin_datasource`;
CREATE TABLE `plugin_datasource`  (
  `datasource_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datasource_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据源编码',
  `datasource_description` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `datasource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据源类型，如RDB/NOSQL/HTTP/MQ等',
  `datasource_class` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '具体的数据源类型，如MYSQL/ES/POSTGRESQL/HIVE等',
  `database_pool_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库连接池类型，如HIKARI/DRUID',
  `database_pool_setting` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '数据库连接池配置',
  `driver_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源驱动ID',
  `settings_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '数据源配置',
  `enabled_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 1:启用 0：不启用',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
  `object_version_number` bigint(20) NOT NULL DEFAULT 1 COMMENT '行版本号，用来处理锁',
  `creation_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `created_by` int(11) NOT NULL DEFAULT -1,
  `last_updated_by` int(11) NOT NULL DEFAULT -1,
  `last_update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`datasource_id`) USING BTREE,
  UNIQUE INDEX `plugin_datasource_u1`(`datasource_code`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
