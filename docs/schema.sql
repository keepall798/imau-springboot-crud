-- ============================================================
-- 本项目的数据库脚本（与你的 imau_learn.user 表结构完全一致）
-- 说明：如果库里已经有这张表，跳过本文件即可；这里只是给"换台电脑"时用。
-- 执行：在 IDEA 的 Database 控制台或 MySQL 命令行里跑一遍
-- ============================================================

CREATE DATABASE IF NOT EXISTS `imau_learn`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE `imau_learn`;

CREATE TABLE IF NOT EXISTS `user` (
    `username` varchar(32) NOT NULL COMMENT '用户名（主键，由用户输入，不是自增 ID）',
    `password` varchar(32) NOT NULL COMMENT '密码（教学项目明文存储；生产环境必须用 BCrypt 等加盐哈希）',
    PRIMARY KEY (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '用户表';

-- 演示数据（INSERT IGNORE：已存在就跳过，可重复执行）
INSERT IGNORE INTO `user` (`username`, `password`)
VALUES ('丁真', '123456'),
       ('丁义', '123456');

-- 查看结果
SELECT * FROM `user`;
