-- ============================================================
-- 图书表
-- Get-Content "E:\项目\imau-springboot-crud\docs\book.sql" -Raw | & "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" -uroot -p123456 --default-character-set=utf8
-- ============================================================

USE `imau_learn`;

DROP TABLE IF EXISTS `t_book`;

CREATE TABLE `t_book` (
    `id`     bigint      NOT NULL AUTO_INCREMENT,
    `title`  varchar(50) NOT NULL COMMENT '书名',
    `author` varchar(30) NOT NULL COMMENT '作者',
    `price`  decimal(8,2) NOT NULL COMMENT '价格',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '图书表（毕业考用）';

INSERT INTO `t_book` (`title`, `author`, `price`)
VALUES ('三体', '刘慈欣', 39.90);

SELECT * FROM `t_book`;
