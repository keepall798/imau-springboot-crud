package com.wang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
