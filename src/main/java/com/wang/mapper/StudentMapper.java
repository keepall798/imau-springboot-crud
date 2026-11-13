package com.wang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
