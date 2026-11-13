package com.wang.controller;


import com.wang.common.Result;
import com.wang.entity.Student;
import com.wang.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /*增：POST /students */
    @PostMapping
    public Result<Void> save(@RequestBody Student student) {
        studentService.create(student);
        return Result.ok();
    }

    /*查一个：GET /students/1 */
    @GetMapping("/{id}")
    public Result<Student> get(@PathVariable Long id) {
        return Result.ok(studentService.getById(id));
    }

    /*查全部：GET /students */
    @GetMapping
    public Result<List<Student>> list() {
        return Result.ok(studentService.list());
    }

    /*改：PUT /students/1*/
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Student student) {
        studentService.update(id, student);
        return Result.ok();
    }

    /*删：DELETE /students/1*/
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.ok();
    }
}
