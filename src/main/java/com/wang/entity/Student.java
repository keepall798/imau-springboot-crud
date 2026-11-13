package com.wang.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_student")
public class Student {

    /*自增主键：插入时不传，数据库自动＋1 生成*/
    @TableId(type = IdType.AUTO)
    private Long id;

    /*姓名*/
    private String name;

    /*年龄*/
    private Integer age;
}
