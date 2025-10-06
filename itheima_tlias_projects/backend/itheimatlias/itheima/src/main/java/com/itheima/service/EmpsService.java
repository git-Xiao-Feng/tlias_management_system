package com.itheima.service;

import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.Emps;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface EmpsService {

    PageResult queryEmpsList(EmpQueryParam empQueryParam);

    void addEmp(Emps emp);


    Emps getById(Integer id);

    void deleteById(List<Integer> ids);

    void updateEmp(Emps emp);
}
