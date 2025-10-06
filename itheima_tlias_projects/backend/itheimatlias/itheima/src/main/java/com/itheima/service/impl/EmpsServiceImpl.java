package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpsMapper;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.Emps;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class EmpsServiceImpl implements EmpsService {

    @Autowired
    private EmpsMapper empsMapper;
    @Override
    public PageResult queryEmpsList(EmpQueryParam empQueryParam) {

        // 调用分页插件的分页方法
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        // 执行查询
        List<Emps> empsList = empsMapper.queryEmpsList(empQueryParam);

        Page<Emps> empsPage = (Page<Emps>) empsList;
        // 返回分页结果
        return new PageResult(empsPage.getTotal(), empsPage.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEmp(Emps emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empsMapper.addEmp(emp);
        // 此时emp.getId()已经包含了数据库生成的ID
        // 可以将ID设置给工作经历列表中的每一条记录

        Integer empId = emp.getId();
        System.out.println("员工ID：" + empId);
        List<EmpExpr> exprList = emp.getExprList();
        System.out.println("工作经历列表：" + exprList);
        if (exprList != null && !exprList.isEmpty()) {
            for (EmpExpr expr : exprList) {
                expr.setEmpId(empId); // 将员工ID设置给每条工作经历记录
            }
            System.out.println("设置后的工作经历列表：" + exprList);
        empsMapper.addEmpExpr(exprList);
        }
    }

    @Override
    public Emps getById(Integer id) {
        Emps emp = empsMapper.getEmpById(id);
        emp.setExprList(empsMapper.getEmpExprById(id));
        return emp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(List<Integer> ids) {
        empsMapper.deleteById(ids);
        empsMapper.deleteEmpExpr(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmp(Emps emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empsMapper.updateEmp(emp);
        empsMapper.deleteEmpExpr(List.of(emp.getId()));
        empsMapper.addEmpExpr(emp.getExprList());
    }
}
