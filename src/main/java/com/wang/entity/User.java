package com.wang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户实体：和数据库表 user 一一对应（一个字段 = 一列）。
 * <p>
 * 命名约定：实体类不加后缀，直接用表名对应的单数名词 —— 表 user → 类 User。
 * 只给 Service / Mapper 用，<b>不要直接返回给前端</b>（里面有密码）。
 */
@Data
@TableName("user")
public class User {

    /**
     * 主键：用户名。
     * <p>
     * type = IdType.INPUT 的含义是"主键由外部提供，我不自动生成"。
     * 为什么不用常见的 IdType.AUTO？因为 AUTO 是给数据库自增的 bigint 主键用的；
     * 为什么不用 IdType.NONE？NONE 的真实语义是"跟随全局配置"，MP 全局默认是 ASSIGN_ID（雪花算法），
     * 一旦这里传了 null，MP 会静默写入一个 19 位数字串，之后你按名字怎么都删不掉。
     * 用 INPUT 的话，传 null 会直接报错，问题暴露得越早越好。
     */
    @TableId(value = "username", type = IdType.INPUT)
    private String username;

    /** 密码（教学项目明文存储；真实项目必须加盐哈希，如 BCrypt） */
    private String password;
}
