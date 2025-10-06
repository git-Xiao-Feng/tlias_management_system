package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/depts")
public class DeptController {


    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result list(){

        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);

    }

    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Integer id){
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @DeleteMapping
    public Result delete(@RequestParam(value = "id",required = false) Integer id){
        try {
            Integer deleteCount = deptService.delete(id);
            if(deleteCount == 0){
                return Result.error("删除失败");
            }else {
                return Result.success();
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {   //捕获数据库外键约束异常
            return Result.error("删除失败，该部门下有员工");
        }

    }


    @PostMapping
    public Result add(@RequestBody Dept dept){
        deptService.add(dept);
        return Result.success();
    }
    @PutMapping
    public Result update(@RequestBody Dept dept){
        //输出到控制台

        System.out.println(dept.getUpdateTime());
        Integer updateCount = deptService.update(dept);
        if(updateCount == 0){
            return Result.error("更新失败");
        }
        return Result.success();
    }
}
