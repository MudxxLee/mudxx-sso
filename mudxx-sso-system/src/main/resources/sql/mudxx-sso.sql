DROP DATABASE IF EXISTS `mudxx-sso`;
CREATE DATABASE  `mudxx-sso` DEFAULT CHARACTER SET utf8mb4;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use `mudxx-sso`;


CREATE TABLE `sso_menu` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `name` varchar(50) NOT NULL COMMENT '名称',
                            `permission` varchar(200) DEFAULT NULL COMMENT '权限',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE TABLE `sso_role` (
                            `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `role_name` varchar(50) NOT NULL COMMENT '角色名称',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `sso_role_menu` (
                                 `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `role_id` bigint(11) DEFAULT NULL COMMENT '角色ID',
                                 `menu_id` bigint(11) DEFAULT NULL COMMENT '菜单ID',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='角色菜单关系表';

CREATE TABLE `sso_user` (
                            `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `username` varchar(50) NOT NULL COMMENT '用户名',
                            `password` varchar(100) DEFAULT NULL COMMENT '密码',
                            `status` tinyint(2) DEFAULT NULL COMMENT '状态(1:启用 0:禁用)',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='用户表';

CREATE TABLE `sso_user_role` (
                                 `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `user_id` bigint(11) DEFAULT NULL COMMENT '用户ID',
                                 `role_id` bigint(11) DEFAULT NULL COMMENT '角色ID',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='用户角色关系表';

CREATE TABLE `sso_log` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `username` varchar(50) DEFAULT NULL COMMENT '用户名',
                           `operation` varchar(50) DEFAULT NULL COMMENT '操作',
                           `method` varchar(200) DEFAULT NULL COMMENT '方法',
                           `params` varchar(5000) DEFAULT NULL COMMENT '参数',
                           `time` bigint(20) NOT NULL COMMENT '操作时间',
                           `ip` varchar(64) DEFAULT NULL COMMENT 'IP',
                           `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
                           `status` int(11) DEFAULT '1',
                           `error` varchar(500) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户操作日志表';

create table `sso_file`(
                           `id`   bigint not null auto_increment comment 'id',
                           `name` varchar(50) DEFAULT NULL COMMENT '附件名称',
                           `url`  varchar(255) comment '附件地址',
                           primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='附件表';

INSERT INTO `sso_menu` VALUES (1, 'select resource', 'sys:res:list');
INSERT INTO `sso_menu` VALUES (2, 'upload resource', 'sys:res:create');
INSERT INTO `sso_menu` VALUES (3, 'delete roles', 'sys:res:delete');

INSERT INTO `sso_role` VALUES (1, 'ADMIN');
INSERT INTO `sso_role` VALUES (2, 'USER');

INSERT INTO `sso_role_menu` VALUES (1, 1, 1);
INSERT INTO `sso_role_menu` VALUES (2, 1, 2);
INSERT INTO `sso_role_menu` VALUES (3, 1, 3);
INSERT INTO `sso_role_menu` VALUES (4, 2, 1);

INSERT INTO `sso_user` VALUES (1, 'admin', '$2a$10$5T851lZ7bc2U87zjt/9S6OkwmLW62tLeGLB2aCmq3XRZHA7OI7Dqa', '1');
INSERT INTO `sso_user` VALUES (2, 'user', '$2a$10$szHoqQ64g66PymVJkip98.Fap21Csy8w.RD8v5Dhq08BMEZ9KaSmS', '1');

INSERT INTO `sso_user_role` VALUES (1, 1, 1);
INSERT INTO `sso_user_role` VALUES (2, 2, 2);
