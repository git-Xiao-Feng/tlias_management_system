package com.itheima.service;

import com.itheima.pojo.EmpGenderData;
import com.itheima.pojo.EmpJobData;

import java.util.List;

public interface ReportService {
     List<EmpGenderData> getEmpGenderDate();

     EmpJobData getEmpJobDate();

     List<EmpGenderData> getStudentDegreeData();

     EmpJobData getStudentCountData();
}
