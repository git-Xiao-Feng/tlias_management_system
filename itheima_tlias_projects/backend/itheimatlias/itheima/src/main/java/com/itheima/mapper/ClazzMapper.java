package com.itheima.mapper;


import com.itheima.pojo.Clazz;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClazzMapper {


    List<Clazz> findClazzAll(EmpQueryParam empQueryParam);


    @Delete("delete from clazz where id = #{id}")
    Integer deleteClazz(Integer id);


    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, create_time, update_time) values(#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    Integer addClazz(Clazz clazz);


    @Select("select id, name, room, begin_date beginDate, end_date endDate, master_id masterId, subject, create_time createTime, update_time updateTime from clazz where id = #{id}")
    Clazz findClazzById(Integer id);


    @Update("update clazz set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, update_time = #{updateTime} where id = #{id}")
    Integer updateClazz(Clazz clazz);
}
