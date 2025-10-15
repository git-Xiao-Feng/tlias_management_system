package com.itheima.mapper;


import com.itheima.pojo.EmpGenderData;
import com.itheima.pojo.EmpJobData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    @Select("SELECT if(gender=1,'男性员工','女性员工') AS name,COUNT(*) AS value FROM emp GROUP BY gender")
    List<EmpGenderData> getEmpGenderDate();

    @Select("SELECT CASE job WHEN '1' THEN '班主任'" +
            "WHEN '2' THEN '讲师'" +
            "WHEN '3' THEN '学工主管'" +
            "WHEN '4' THEN '教研主管'" +
            "WHEN '5' THEN '咨询师'" +
            "ELSE '其他'" +
            "END AS job,COUNT(*) AS count FROM emp GROUP BY job ORDER BY count DESC")
    List<Map<String, Object>> getEmpJobDate();


    @Select("SELECT CASE degree WHEN '1' THEN '初中'" +
            "WHEN '2' THEN '高中'" +
            "WHEN '3' THEN '专科'" +
            "WHEN '4' THEN '本科'" +
            "WHEN '5' THEN '硕士'" +
            "WHEN '6' THEN '博士'" +
            "END AS name,COUNT(*) AS value FROM student GROUP BY degree")
    List<EmpGenderData> getStudentDegreeData();

    @Select("SELECT c.name AS class,COUNT(*) AS count FROM student s LEFT JOIN clazz c ON s.clazz_id = c.id GROUP BY c.name")
    List<Map<String, Object>> getStudentCountData();
}
