/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1-3306-123456
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : 127.0.0.1:3306
 Source Schema         : snake_admin

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 01/08/2024 15:39:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门ID',
  `parent_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上级部门ID',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门负责人电话',
  `principal` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门负责人',
  `email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门负责人邮箱',
  `type` smallint(3) DEFAULT NULL COMMENT '部门类型',
  `status` smallint(3) DEFAULT NULL COMMENT '部门状态（1-正常，0-禁用）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统管理-部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1818877165706498050', '0', ' 总部', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:11:24', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877274557075458', '1818877165706498050', ' 杭州分公司', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:11:50', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877346485194753', '1818877165706498050', ' 西安分公司', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:07', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877368098443266', '1818877165706498050', ' 南京分公司', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:12', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877398989492225', '1818877165706498050', ' 郑州分公司', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:20', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877465389518849', '1818877274557075458', ' 研发部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:36', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877491125768193', '1818877274557075458', ' 市场部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877517306613762', '1818877274557075458', ' 财务部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:12:48', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877580120510466', '1818877346485194753', ' 财务部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:13:03', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877608528531458', '1818877346485194753', ' 研发部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:13:10', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877648907096066', '1818877368098443266', ' 研发部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:13:19', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1818877684583845889', '1818877398989492225', ' 研发部门', ' 18516908635', ' 张三', ' 370696614@qq.com', NULL, 1, '备注', 0, '2024-08-01 13:13:28', NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单ID',
  `parent_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上级菜单ID',
  `menu_type` smallint(3) DEFAULT NULL COMMENT '菜单类型（`0`代表菜单、`1`代表`iframe`、`2`代表外链、`3`代表按钮）',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的`locales`文件夹下对应添加）',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由名称（必须唯一并且和当前路由`component`字段对应的页面里用`defineOptions`包起来的`name`保持一致） ',
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径（传`component`组件路径，那么`path`可以随便写，如果不传，`component`组件路径会跟`path`保持一致）',
  `rank` bigint(20) DEFAULT NULL COMMENT '菜单排序（平台规定只有`home`路由的`rank`才能为`0`，所以后端在返回`rank`的时候需要从非`0`开始',
  `redirect` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由重定向',
  `icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `extra_icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '右侧图标',
  `enter_transition` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '进场动画（页面加载动画）',
  `leave_transition` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '离场动画（页面加载动画）',
  `active_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单激活（将某个菜单激活，主要用于通过`query`或`params`传参的路由，当它们通过配置`showLink: false`后不在菜单中显示，就不会有任何菜单高亮，而通过设置`activePath`指定激活菜单即可获得高亮，`activePath`为指定激活菜单的`path`）',
  `auths` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识（按钮级别权限设置）  ',
  `frame_src` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '链接地址（需要内嵌的`iframe`链接地址） ',
  `frame_loading` tinyint(1) DEFAULT NULL COMMENT '加载动画（0-false;1-true,内嵌的`iframe`页面是否开启首次加载动画）',
  `keep_alive` tinyint(1) DEFAULT NULL COMMENT '缓存页面（0-false;1-true,是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）',
  `hidden_tag` tinyint(1) DEFAULT NULL COMMENT '标签页（0-false;1-true,当前菜单名称或自定义信息禁止添加到标签页）',
  `fixed_tag` tinyint(1) DEFAULT NULL COMMENT '固定标签页（0-false;1-true,当前菜单名称是否固定显示在标签页且不可关闭）',
  `show_link` tinyint(1) DEFAULT NULL COMMENT '菜单（0-false;1-true,是否显示该菜单）',
  `show_parent` tinyint(1) DEFAULT NULL COMMENT '父级菜单（0-false;1-true,是否显示父级菜单)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统管理-菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色编码',
  `status` smallint(3) DEFAULT NULL COMMENT '角色状态（1-正常，0-禁用）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统管理-角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1818826169089343490', '系统管理员', 'admin', 1, '系统管理员', '2024-08-01 09:48:46', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES ('1818826913309868034', '研发人员', ' develop', 0, '研发人员', '2024-08-01 09:51:43', NULL, NULL, '2024-08-01 09:54:20', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `menu_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营平台-角色菜单关联';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `status` smallint(3) DEFAULT NULL COMMENT '角色状态（1-正常，0-禁用）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `sex` smallint(3) DEFAULT NULL COMMENT '性别(1-男,0-女)',
  `dept_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门 ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统管理-用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1818902514041393154', 'admin', '8tj657VNMn!', '超级管理员', '18516908635', '370696614@qq.com', 'https://avatars.githubusercontent.com/u/4476132', 1, '超级管理员', 1, NULL, '2024-08-01 14:52:08', NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营平台-用户角色关联';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
