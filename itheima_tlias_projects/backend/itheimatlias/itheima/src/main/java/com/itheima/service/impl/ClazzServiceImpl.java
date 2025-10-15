package com.itheima.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ClazzMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {


    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public PageResult findClazzAll(EmpQueryParam empQueryParam) {
        // 调用分页插件的分页方法
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        // 执行查询
        List<Clazz> clazzList = clazzMapper.findClazzAll(empQueryParam);
        LocalDate now = LocalDate.now(); // 当前日期
        clazzList.forEach(clazz -> {
            LocalDate beginDate = clazz.getBeginDate();
            LocalDate endDate = clazz.getEndDate();
            if(beginDate != null && endDate != null){
                if (now.isBefore(beginDate)) {
                    clazz.setStatus("未开班");

                } else if ( (now.isAfter(beginDate) || now.isEqual(beginDate)) && (now.isBefore(endDate) || now.isEqual(endDate)) ) {
                    clazz.setStatus("进行中");
                } else if (now.isAfter(endDate)) {
                    clazz.setStatus("已结课");
                }
            }else{
                clazz.setStatus("未知状态");
            }
        });
        // 获取分页结果
        Page<Clazz> clazzPage = (Page<Clazz>) clazzList;
        // 返回分页结果
        return new PageResult(clazzPage.getTotal(),clazzPage.getResult());
    }

    @Override
    public Integer deleteClazz(Integer id) {
        return clazzMapper.deleteClazz(id);
    }

    @Override
    public Integer addClazz(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        return clazzMapper.addClazz(clazz);
    }

    @Override
    public Clazz findClazzById(Integer id) {
        return clazzMapper.findClazzById(id);
    }

    @Override
    public Integer updateClazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        return clazzMapper.updateClazz(clazz);
    }
}
