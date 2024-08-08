/*
 Navicat Premium Data Transfer

 Source Server         : 101.126.69.31
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 101.126.69.31:13306
 Source Schema         : snake_admin

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 08/08/2024 20:45:52
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
INSERT INTO `sys_dept` VALUES ('1819394579972079617', '0', '陕西蓝东科技', '18512341234', '张三', '370696614@qq.com', NULL, 1, '公司日哥', 0, '2024-08-02 23:27:25', NULL, NULL, '2024-08-05 23:02:43', NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820476856873492481', '1819394579972079617', '研发部', '18512341234', '张三', '18512341234@163.com', NULL, 1, '研发部', 1, '2024-08-05 23:08:00', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820476958392426498', '1820476856873492481', '研发一组', '', '李小鹏', '', NULL, 1, '', 1, '2024-08-05 23:08:24', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820477107168583682', '1819394579972079617', '营销部', '', '吕一', '', NULL, 1, '', 0, '2024-08-05 23:09:00', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820477169416249345', '1819394579972079617', '财务部', '', '张大千', '', NULL, 1, '', 0, '2024-08-05 23:09:15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820477370180804609', '1820477107168583682', '营销特战队', '', '', '', NULL, 1, '', 0, '2024-08-05 23:10:03', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1820477411180126209', '1820477169416249345', '账务核查', '', '', '', NULL, 1, '', 0, '2024-08-05 23:10:12', NULL, NULL, NULL, NULL, NULL);
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
INSERT INTO `sys_menu` VALUES ('1819614103262461953', '0', 99, '系统管理', 'PureSystem', '/system', '', 1, '', 'ri:settings-3-line', '', 'bounce', 'bounce', '', '', '', 0, 0, 0, 0, 0, 0, '2024-08-03 13:59:44', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819614103262461954', '0', 99, '接口文档', NULL, '/snake-admin-doc', NULL, 100, '/snake-admin/pure-admin-doc', 'ri:terminal-window-line', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819614103262461955', '1819614103262461954', 2, '运营平台接口文档', 'http://101.126.69.31:41000/doc.html', '/external', NULL, 102, NULL, 'fa-solid:ad', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819614693686534146', '1819614103262461953', 0, '用户管理', 'SystemUser', '/system/user/index', '', 10, '', 'ri:admin-line', '', 'bounce', 'bounce', '', '', '', 0, 0, 0, 0, 0, 0, '2024-08-03 14:02:04', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819614933785272321', '1819614103262461953', 0, '角色管理', 'SystemRole', '/system/role/index', '', 11, '', 'ri:admin-fill', '', 'bounce', 'bounce', '', '', '', 0, 0, 0, 0, 0, 0, '2024-08-03 14:03:02', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819615102815723521', '1819614103262461953', 0, '菜单管理', 'SystemMenu', '/system/menu/index', '', 12, '', 'ep:menu', '', 'bounce', 'bounce', '', '', '', 0, 0, 0, 0, 0, 0, '2024-08-03 14:03:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1819615242922254337', '1819614103262461953', 0, '部门管理', 'SystemDept', '/system/dept/index', '', 13, '', 'ri:git-branch-line', '', 'bounce', 'bounce', '', '', '', 0, 0, 0, 0, 0, 0, '2024-08-03 14:04:15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1820854703098048513', '1819614103262461954', 1, '前端开源框架文档', 'SnakeAdminDoc', '/snake-admin/pure-admin-doc', 'IFrame', 100, '', 'ri:terminal-window-line', '', '', '', '', '', 'https://pure-admin.github.io/pure-admin-doc/pages/introduction/', 1, 0, 0, 0, 1, 1, '2024-08-07 00:09:26', NULL, NULL, NULL, NULL, NULL);
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
INSERT INTO `sys_role` VALUES ('1819395864901201922', '超级管理员', 'admin', 1, '超级管理员', '2024-08-02 23:32:32', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES ('1820629320230727681', '普通角色', 'common', 1, '普通角色', '2024-08-06 09:13:50', NULL, NULL, '2024-08-06 09:14:38', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色ID',
  `menu_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_index` (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('1821210764745347074', '1820629320230727681', '1819614103262461954', '2024-08-07 23:44:17', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_menu` VALUES ('1821210764745347075', '1820629320230727681', '1819614103262461955', '2024-08-07 23:44:17', NULL, NULL, NULL, NULL, NULL);
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
  `avatar` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `status` smallint(3) DEFAULT NULL COMMENT '角色状态（1-正常，0-禁用）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `sex` smallint(3) DEFAULT NULL COMMENT '性别(1-男,0-女)',
  `dept_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `deleted` smallint(3) DEFAULT NULL COMMENT '删除标识(0-正常，1-删除)',
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
INSERT INTO `sys_user` VALUES ('1819395060203110402', 'admin', '6f620dae6deb84a7ad7480441ccc4d0b35f3507af4a3c257902983bd9ebe7128', '超级管理员', '18512341234', '370696614@qq.com', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Ff0100d63-59d2-87d2-ac5c-eef40c134026%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1725387907&t=433cfd651e6521a8344a7239c08e7ae1', 1, '超级管理员', 1, '1819394579972079617', 0, '2024-08-02 23:29:20', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES ('1821502037142151169', 'zhangsan', '3f49aa1748b257d21ca178ac40dd1ae7fc8c115584227b0044538181b2a6f930', '张三', '18516908635', '18516908635@163.com', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Ff0100d63-59d2-87d2-ac5c-eef40c134026%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1725387907&t=433cfd651e6521a8344a7239c08e7ae1', 1, 'Admin@123', 0, '1819394579972079617', 1, '2024-08-08 19:01:42', NULL, NULL, '2024-08-08 19:37:12', NULL, NULL);
INSERT INTO `sys_user` VALUES ('1821506058967982081', 'lisi', '62c67c3db377ee703af28e6ca0e12bfcb76ec92fa4b9acc4cb11dfb19f73007e', '李四', '18523412345', '', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Ff0100d63-59d2-87d2-ac5c-eef40c134026%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1725387907&t=433cfd651e6521a8344a7239c08e7ae1', 1, '', NULL, '1819394579972079617', 1, '2024-08-08 19:17:41', NULL, NULL, '2024-08-08 19:36:08', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `role_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_index` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1819403867666776066', '1819395060203110402', '1819395864901201922', '2024-08-03 00:04:20', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES ('1821517248481976322', '1821502037142151169', '1820629320230727681', '2024-08-08 20:02:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES ('1821517350260957186', '1821506058967982081', '1820629320230727681', '2024-08-08 20:02:33', NULL, NULL, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
