package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentPage;

import java.util.List;

public interface StudentsService {


    PageResult findStuAll(StudentPage studentPage);

    Integer deleteStu(List<Integer> ids);

    Integer addStu(Student student);



    Student findStudentById(Integer id);

    Integer updateStudent(Student student);

    Integer violation(Integer id, Integer score);
}
