package com.wang.service.impl;


import com.wang.entity.Student;
import com.wang.exception.BizException;
import com.wang.mapper.StudentMapper;
import com.wang.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public void create(Student student) {
//简单校验：姓名不为空。正规项目用DTO + @NotBlank,这里先手写，体验一下校验在哪层
        if (student.getName() == null || student.getName().isBlank()) {
            throw new BizException(400, "姓名不能为空");
        }
        //id不用管：Idtype.AUTO，插入数据自动生成并回填到student.id
        studentMapper.insert(student);
    }

    @Override
    public Student getById(Long id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new BizException(404, "学生不存在：" + id);
        }
        return student;
    }

    @Override
    public List<Student> list() {
        return studentMapper.selectList(null);
    }

    @Override
    @Transactional
    public void update(Long id, Student student) {
        student.setId(id);      // 以路径里的id为准
        int rows = studentMapper.updateById(student);
        if (rows == 0) {
            throw new BizException(404, "学生不存在：" + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int rows = studentMapper.deleteById(id);
        if (rows == 0) {
            throw new BizException(404, "学生不存在：" + id);
        }
    }
}
