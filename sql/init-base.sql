SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE blackchat;

-- ----------------------------
-- Table structure for item_config
-- ----------------------------
DROP TABLE IF EXISTS `item_config`;
CREATE TABLE `item_config`
(
    `id`          bigint(20) UNSIGNED                                           NOT NULL COMMENT 'id',
    `type`        int(11)                                                       NOT NULL COMMENT '物品类型 1改名卡 2徽章',
    `img`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '物品图片',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '物品功能描述',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '功能物品配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_config
-- ----------------------------
INSERT INTO `item_config`
VALUES (1, 1, NULL, '用户可以使用改名卡，更改自己的名字。mallchat名称全局唯一，快抢订你的专属昵称吧',
        '2023-03-25 22:27:30.511', '2023-03-25 22:27:30.511');
INSERT INTO `item_config`
VALUES (2, 2, 'https://cdn-icons-png.flaticon.com/128/1533/1533913.png', '爆赞徽章，单条消息被点赞超过10次，即可获得',
        '2023-05-07 17:50:31.090', '2023-05-07 18:12:05.824');
INSERT INTO `item_config`
VALUES (3, 2, 'https://cdn-icons-png.flaticon.com/512/6198/6198527.png ', '抹茶聊天前10名注册的用户才能获得的专属徽章',
        '2023-05-07 17:50:31.100', '2023-05-07 18:12:01.448');
INSERT INTO `item_config`
VALUES (4, 2, 'https://cdn-icons-png.flaticon.com/512/10232/10232583.png',
        '抹茶聊天前100名注册的用户才能获得的专属徽章', '2023-05-07 17:50:31.109', '2023-05-07 17:56:36.059');
INSERT INTO `item_config`
VALUES (5, 2, 'https://cdn-icons-png.flaticon.com/128/2909/2909937.png', '抹茶知识星球成员的专属徽章',
        '2023-05-07 17:50:31.109', '2023-05-07 17:56:36.059');
INSERT INTO `item_config`
VALUES (6, 2, 'https://minio.mallchat.cn/mallchat/%E8%B4%A1%E7%8C%AE%E8%80%85.png', '抹茶项目contributor专属徽章',
        '2023-05-07 17:50:31.109', '2023-05-07 17:56:36.059');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`           bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `room_id`      bigint(20)          NOT NULL COMMENT '会话表id',
    `from_uid`     bigint(20)          NOT NULL COMMENT '消息发送者uid',
    `content`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
    `reply_msg_id` bigint(20)          NULL                                       DEFAULT NULL COMMENT '回复的消息内容',
    `status`       int(11)             NOT NULL COMMENT '消息状态 0正常 1删除',
    `gap_count`    int(11)             NULL                                       DEFAULT NULL COMMENT '与回复的消息间隔多少条',
    `type`         int(11)             NULL                                       DEFAULT 1 COMMENT '消息类型 1正常文本 2.撤回消息',
    `extra`        json                                                           DEFAULT NULL COMMENT '扩展信息',
    `create_time`  datetime(3)         NOT NULL                                   DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`  datetime(3)         NOT NULL                                   DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_room_id` (`room_id`) USING BTREE,
    INDEX `idx_from_uid` (`from_uid`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '消息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_mark
-- ----------------------------
DROP TABLE IF EXISTS `message_mark`;
CREATE TABLE `message_mark`
(
    `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `msg_id`      bigint(20)          NOT NULL COMMENT '消息表id',
    `uid`         bigint(20)          NOT NULL COMMENT '标记人uid',
    `type`        int(11)             NOT NULL COMMENT '标记类型 1点赞 2举报',
    `status`      int(11)             NOT NULL COMMENT '消息状态 0正常 1取消',
    `create_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_msg_id` (`msg_id`) USING BTREE,
    INDEX `idx_uid` (`uid`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '消息标记表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`
(
    `id`          bigint(20) UNSIGNED                                          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话名',
    `type`        int(11)                                                      NOT NULL COMMENT '会话类型 1大群聊 2沸点',
    `active_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后活跃时间-排序',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_active_time` (`active_time`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '会话表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room`
VALUES (1, '抹茶群聊', 1, '2023-03-25 22:30:07.328', '2023-03-25 22:30:07.328', '2023-03-25 22:30:07.328');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`            bigint(20) UNSIGNED                                           NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `name`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '用户昵称',
    `avatar`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '用户头像',
    `sex`           int(11)                                                       NULL     DEFAULT NULL COMMENT '性别 1为男性，2为女性',
    `open_id`       char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL COMMENT '微信openid用户标识',
    `last_opt_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后上下线时间',
    `ip_info`       json                                                          NULL COMMENT 'ip信息',
    `item_id`       bigint(20)                                                    NULL     DEFAULT NULL COMMENT '佩戴的徽章id',
    `status`        int(11)                                                                DEFAULT 0 COMMENT '使用状态 0.正常 1.拉黑',
    `active_status` int(11)                                                                DEFAULT 1 COMMENT '在线状态 1.在线 2.离线',
    `create_time`   datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`   datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uniq_open_id` (`open_id`) USING BTREE,
    UNIQUE INDEX `uniq_name` (`name`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;
INSERT INTO `mallchat`.`user` (`id`, `name`, `avatar`, `sex`, `open_id`, `last_opt_time`, `ip_info`, `item_id`,
                               `status`, `create_time`, `update_time`)
VALUES (10001, 'ChatGPT',
        'https://img1.baidu.com/it/u=3613958228,3522035000&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', 0, '??',
        '2023-06-29 17:03:03.357', NULL, NULL, 0, '2023-06-29 17:03:03.357', '2023-07-01 14:56:10.271');
INSERT INTO `mallchat`.`user` (`id`, `name`, `avatar`, `sex`, `open_id`, `last_opt_time`, `ip_info`, `item_id`,
                               `status`, `create_time`, `update_time`)
VALUES (10002, 'ChatGLM2',
        'http://mms1.baidu.com/it/u=1979830414,2984779047&fm=253&app=138&f=JPEG&fmt=auto&q=75?w=500&h=500', NULL, '450',
        '2023-07-01 11:58:24.605', NULL, NULL, 0, '2023-07-01 11:58:24.605', '2023-07-01 12:02:56.900');
-- ----------------------------
-- Table structure for user_backpack
-- ----------------------------
DROP TABLE IF EXISTS `user_backpack`;
CREATE TABLE `user_backpack`
(
    `id`          bigint(20) UNSIGNED                                          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`         bigint(20)                                                   NOT NULL COMMENT 'uid',
    `item_id`     int(11)                                                      NOT NULL COMMENT '物品id',
    `status`      int(11)                                                      NOT NULL COMMENT '使用状态 0.待使用 1已使用',
    `idempotent`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幂等号',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uniq_idempotent` (`idempotent`) USING BTREE,
    INDEX `idx_uid` (`uid`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户背包表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `wx_msg`;
CREATE TABLE `wx_msg`
(
    `id`          bigint(20) UNSIGNED                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `open_id`     char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '微信openid用户标识',
    `msg`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL COMMENT '用户消息',
    `create_time` datetime(3)                                               NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                                               NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_open_id` (`open_id`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '微信消息表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `black`;
CREATE TABLE `black`
(
    `id`          bigint(20) UNSIGNED                                          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        int(11)                                                      NOT NULL COMMENT '拉黑目标类型 1.ip 2uid',
    `target`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拉黑目标',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_type_target` (`type`, `target`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint(20) unsigned                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
    `create_time` datetime(3)                            NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)                            NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE,
    KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色表';
insert into role(id, `name`)
values (1, '超级管理员');
insert into role(id, `name`)
values (2, '抹茶群聊管理员');

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`         bigint(20)          NOT NULL COMMENT 'uid',
    `role_id`     bigint(20)          NOT NULL COMMENT '角色id',
    `create_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_uid` (`uid`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE,
    KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户角色关系表';

DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word`
(
    `word` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '敏感词'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='敏感词库';
INSERT INTO `sensitive_word` (`word`)
VALUES ('TMD');
INSERT INTO `sensitive_word` (`word`)
VALUES ('tmd');

DROP TABLE IF EXISTS `user_emoji`;
CREATE TABLE `user_emoji`
(
    `id`             bigint(20) unsigned                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`            bigint(20)                              NOT NULL COMMENT '用户表ID',
    `expression_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表情地址',
    `delete_status`  int(1)                                  NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
    `create_time`    datetime(3)                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`    datetime(3)                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `IDX_USER_EMOJIS_UID` (`uid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户表情包';
