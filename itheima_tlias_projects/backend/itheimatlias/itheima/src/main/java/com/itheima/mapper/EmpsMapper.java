package com.itheima.mapper;


import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.Emps;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpsMapper {



    List<Emps>  queryEmpsList(EmpQueryParam empQueryParam);


    void addEmp(Emps emp);

    void addEmpExpr(List<EmpExpr> exprList);

    @Select("select  id, username,  name, gender, phone, job, salary, image, entry_date entryDate, dept_id deptId from emp where id = #{id}")
    Emps getEmpById(Integer id);

    @Select("select  emp_id empId , begin, end, company, job from emp_expr where emp_id = #{id}")
    List<EmpExpr> getEmpExprById(Integer id);
    
    
    void deleteById(List<Integer> ids);

    void deleteEmpExpr(List<Integer> ids);


    void updateEmp(Emps emp);


}
