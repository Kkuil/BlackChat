CREATE DATABASE IF NOT EXISTS init_base;

USE init_base;

-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT                                   NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    `username`    VARCHAR(256) DEFAULT 'Kkuil'             NOT NULL COMMENT '用户名',
    `password`    VARCHAR(256) DEFAULT '123456'            NOT NULL COMMENT '密码',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT      DEFAULT 0                   NOT NULL COMMENT '是否删除(0-未删, 1-已删)'
) COMMENT '用户表';
