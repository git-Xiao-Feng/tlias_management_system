package com.itheima.controller;



import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.Emps;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpsController {
    private static final String UPLOAD_DIR = "C:\\Users\\Public\\Pictures\\javaupload\\";

    @Autowired
    private EmpsService empsService ;


    // 根据条件获取员工列表
    @GetMapping
    public Result queryEmpList(EmpQueryParam empQueryParam){
        log.info("查询员工列表，参数：{}",empQueryParam);
        PageResult pageResult = empsService.queryEmpsList(empQueryParam);
        return Result.success(pageResult);
    }


    // 根据ID查询单个员工和员工的工作经历
    @GetMapping("/{id}")
    public Result getEmp(@PathVariable("id") Integer id){
        log.info("查询员工，id：{}",id);
        Emps emp = empsService.getById(id);
        return Result.success(emp);
    }


    // 更新员工信息
    @PutMapping
    public Result updateEmp(@RequestBody Emps emp){
        log.info("更新员工，参数：{}",emp);
        empsService.updateEmp(emp);
        return Result.success();
    }

    // 新增员工
    @PostMapping
    public Result addEmp(@RequestBody Emps emp){
        log.info("新增员工，参数：{}",emp);
        empsService.addEmp(emp);
        return Result.success();
    }


    // 头像图片上传
    @PostMapping("/upload")
    public Result uploadPhoto(MultipartFile file) throws IOException {
        log.info("上传文件，参数：{}",file);
        if(file.isEmpty()){
            return Result.error("文件不能为空");
        }
        String filename = file.getOriginalFilename();
        filename = filename.substring(filename.lastIndexOf("."));
        String ufilename = UUID.randomUUID().toString().replace("-", "") + filename;
        File targetFile = new File(UPLOAD_DIR + ufilename);
        // 如果目标目录不存在，则创建它
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        // 保存文件
        file.transferTo(targetFile);



        return Result.success("http://localhost:8080/files/" + ufilename);
    }


    // 删除员工

    @DeleteMapping
    public Result  delEmp(@RequestParam List<Integer> ids){
        log.info("删除员工，id：{}",ids);
        empsService.deleteById(ids);
        return Result.success();
    }

}
