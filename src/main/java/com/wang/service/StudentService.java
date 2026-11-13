package com.wang.service;

import com.wang.entity.Student;

import java.util.List;

public interface StudentService {

    /*新增学生*/
    void create(Student student);

    /*按id查一个学生*/
    Student getById(Long id);

    /*查全部学生*/
    List<Student> list();

    /*按id修改学生*/
    void update(Long id, Student student);

    /*按id删除学生*/
    void delete(Long id);
}
