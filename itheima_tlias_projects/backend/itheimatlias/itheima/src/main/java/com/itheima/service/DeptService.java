package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {


    List<Dept> findAll();

    Integer delete(Integer id);

    void add(Dept dept);

    Integer update(Dept dept);

    Dept getById(Integer id);
}
