package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;

public interface ClazzService {
    PageResult findClazzAll(EmpQueryParam empQueryParam);

    Integer deleteClazz(Integer id);

    Integer addClazz(Clazz clazz);

    Clazz findClazzById(Integer id);

    Integer updateClazz(Clazz clazz);
}
