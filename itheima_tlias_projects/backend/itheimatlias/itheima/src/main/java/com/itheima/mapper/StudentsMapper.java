package com.itheima.mapper;


import com.itheima.pojo.Student;
import com.itheima.pojo.StudentPage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentsMapper {


    List<Student> findStuAll(StudentPage studentPage);

    Integer deleteStu(List<Integer> ids);



    Integer addStu(Student student);


    @Select("select * from student where id = #{id}")
    Student findStudentById(Integer id);



    Integer updateStudent(Student student);


    @Update("update student set violation_count = violation_count + 1, violation_score = violation_score + #{score} where id = #{id}")
    Integer violation(Integer id, Integer score);
}
