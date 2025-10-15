package com.itheima.mapper;


import com.itheima.pojo.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {


    @Select("select id, username, name from emp where username=#{username} and password=#{password}")
    LoginUser queryLoginUser(String username, String password);
}
