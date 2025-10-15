package com.itheima.controller;




import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentPage;
import com.itheima.service.StudentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/students")
public class StudentsController {


    @Autowired
    private StudentsService studentsService;

    // 查询学员列表
    @GetMapping
    public Result findStuAll(StudentPage studentPage){
        log.info("查询学员列表，参数：{}",studentPage);
        PageResult pageResult = studentsService.findStuAll(studentPage);
        return Result.success(pageResult);

    }


    // 删除学员
    @DeleteMapping("/{ids}")
    public Result deleteStu(@PathVariable("ids") List<Integer> ids){
        try {
            Integer deleteCount = studentsService.deleteStu(ids);
            if(deleteCount == 0){
                return Result.error("删除失败");
            }else {
                return Result.success();
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {   //捕获数据库外键约束异常
            return Result.error("删除失败，该学员下有成绩");
        }
    }


    // 新增班级
    @PostMapping
    public Result addStu(@RequestBody Student student){
        log.info("新增学员，参数：{}",student);
        Integer i = studentsService.addStu(student);
        if(i == 0){
            return Result.error("新增失败");
        }else {
            return Result.success();
        }
    }


    // 根据ID查询单个班级
    @GetMapping("/{id}")
    public Result findStudentById(@PathVariable("id") Integer id){
        log.info("根据ID查询单个学员，参数：{}",id);
        Student student = studentsService.findStudentById(id);
        if(student == null){
            return Result.error("查询失败");
        }else {
            return Result.success(student);
        }
    }

    // 修改班级
    @PutMapping
    public Result updateStudent(@RequestBody Student student){
        log.info("修改学员，参数：{}",student);
        Integer i = studentsService.updateStudent(student);
        if(i == 0){
            return Result.error("修改失败");
        }else {
            return Result.success();
        }
    }

    // 学员违纪扣分处理
    @PutMapping("violation/{id}/{score}")
    public Result violation(@PathVariable("id") Integer id,@PathVariable("score") Integer score){
        log.info("学员违纪扣分处理，参数：{}，{}",id,score);
        Integer i = studentsService.violation(id, score);
        if(i == 0){
            return Result.error("处理失败");
        }else {
            return Result.success();
        }
    }
}
