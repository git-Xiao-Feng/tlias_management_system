package com.itheima.controller;


import com.itheima.pojo.EmpGenderData;
import com.itheima.pojo.EmpJobData;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;


    // 员工性别统计
    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        List<EmpGenderData> egd = reportService.getEmpGenderDate();
        return Result.success(egd);
    }


    // 员工职位统计
    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        EmpJobData ejd = reportService.getEmpJobDate();
        return Result.success(ejd);
    }


    // 学员学历统计
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        // 复用EmpGenderData来存储学生学历统计数据
        List<EmpGenderData> sdd =  reportService.getStudentDegreeData();
        return Result.success(sdd);
    }


    // 每个班级的学员人数统计
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        // 复用EmpJobData来存储每个班级的学生人数统计数据
        EmpJobData scd =  reportService.getStudentCountData();
        return Result.success(scd);
    }
}
