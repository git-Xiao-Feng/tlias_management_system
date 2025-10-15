package com.itheima.controller;


import com.itheima.pojo.Clazz;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {


    @Autowired
    private ClazzService clazzService;

    // 查询班级列表
    @GetMapping
    public Result findClazzAll(EmpQueryParam empQueryParam){     // 复用实体类
        log.info("查询班级列表，参数：{}",empQueryParam);
        PageResult pageResult = clazzService.findClazzAll(empQueryParam);
        return Result.success(pageResult);

    }


    // 删除班级
    @DeleteMapping("/{id}")
    public Result deleteClazz(@PathVariable("id") Integer id){
        try {
            Integer deleteCount = clazzService.deleteClazz(id);
            if(deleteCount == 0){
                return Result.error("删除失败");
            }else {
                return Result.success();
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {   //捕获数据库外键约束异常
            return Result.error("删除失败，该班级下有学生");
        }
    }


    // 新增班级
    @PostMapping
    public Result addClazz(@RequestBody Clazz clazz){
        log.info("新增班级，参数：{}",clazz);
        Integer i = clazzService.addClazz(clazz);
        if(i == 0){
            return Result.error("新增失败");
        }else {
            return Result.success();
        }
    }


    // 根据ID查询单个班级
    @GetMapping("/{id}")
    public Result findClazzById(@PathVariable("id") Integer id){
        log.info("根据ID查询单个班级，参数：{}",id);
        Clazz clazz = clazzService.findClazzById(id);
        if(clazz == null){
            return Result.error("查询失败");
        }else {
            return Result.success(clazz);
        }
    }

    // 修改班级
    @PutMapping
    public Result updateClazz(@RequestBody Clazz clazz){
        log.info("修改班级，参数：{}",clazz);
        Integer i = clazzService.updateClazz(clazz);
        if(i == 0){
            return Result.error("修改失败");
        }else {
            return Result.success();
        }
    }


}
