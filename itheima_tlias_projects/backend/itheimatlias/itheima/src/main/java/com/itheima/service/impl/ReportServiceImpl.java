package com.itheima.service.impl;


import com.itheima.mapper.ReportMapper;
import com.itheima.pojo.EmpGenderData;
import com.itheima.pojo.EmpJobData;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {



    @Autowired
    private ReportMapper reportMapper;


    @Override
    public List<EmpGenderData> getEmpGenderDate() {
        List<EmpGenderData> list = reportMapper.getEmpGenderDate();
        return list;
    }

    @Override
    public EmpJobData getEmpJobDate() {
        List<Map<String, Object>> mapList = reportMapper.getEmpJobDate();
        EmpJobData empJobData = new EmpJobData();
        List jobList = new ArrayList<>();
        List dataList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            jobList.add(map.get("job"));
            dataList.add(map.get("count"));
        }
        empJobData.setJobList(jobList);
        empJobData.setDataList(dataList);
        return empJobData;
    }

    @Override
    public List<EmpGenderData> getStudentDegreeData() {
        return reportMapper.getStudentDegreeData();
    }

    @Override
    public EmpJobData getStudentCountData() {
        List<Map<String, Object>> mapList = reportMapper.getStudentCountData();
        EmpJobData empJobData = new EmpJobData();
        List jobList = new ArrayList<>();
        List dataList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            jobList.add(map.get("class"));
            dataList.add(map.get("count"));
        }
        empJobData.setJobList(jobList);
        empJobData.setDataList(dataList);
        return empJobData;
    }
}
