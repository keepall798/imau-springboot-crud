package com.wang.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_book")
public class Book {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String author;

    private BigDecimal price;


}
