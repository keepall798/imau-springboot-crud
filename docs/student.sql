-- ============================================================
-- 第 7 站练习用：学生表
-- 用法（在项目根目录的 IDEA Terminal 里执行）：
-- & "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" -uroot -p123456 --default-character-set=utf8 -e "source E:/项目/imau-springboot-crud/docs/student.sql"
-- ============================================================

USE `imau_learn`;

DROP TABLE IF EXISTS `t_student`;

CREATE TABLE `t_student` (
    `id`   bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '学生ID，自增',
    `name` varchar(20) NOT NULL COMMENT '姓名',
    `age`  int(11)     NOT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '学生表（练习用）';

-- 演示数据（可执行也可不执行，后面用接口自己加）
INSERT INTO `t_student` (`name`, `age`)
VALUES ('张三', 20),
       ('李四', 21);

SELECT * FROM `t_student`;
