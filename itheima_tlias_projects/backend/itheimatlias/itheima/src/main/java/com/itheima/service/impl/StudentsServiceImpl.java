package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.StudentsMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentPage;
import com.itheima.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class StudentsServiceImpl implements StudentsService {


    @Autowired
    private StudentsMapper studentsMapper;

    @Override
    public PageResult findStuAll(StudentPage studentPage) {
        PageHelper.startPage(studentPage.getPage(), studentPage.getPageSize());
        List<Student> studentList = studentsMapper.findStuAll(studentPage);
        Page<Student> page = (Page<Student>) studentList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Integer deleteStu(List<Integer> ids) {
        return studentsMapper.deleteStu(ids);
    }

    @Override
    public Integer addStu(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return studentsMapper.addStu(student);
    }

    @Override
    public Student findStudentById(Integer id) {
        return studentsMapper.findStudentById(id);
    }

    @Override
    public Integer updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentsMapper.updateStudent(student);
    }

    @Override
    public Integer violation(Integer id, Integer score) {
        return studentsMapper.violation(id, score);
    }


}

