package com.itheima.controller;


import com.itheima.pojo.Emps;
import com.itheima.pojo.LoginUser;
import com.itheima.pojo.Result;
import com.itheima.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result queryLoginUser(@RequestBody Emps emp) {
        LoginUser loginU = loginService.queryLoginUser(emp);
        if (loginU != null) {
            return Result.success(loginU);
        } else {
            return Result.error("账号或密码错误");
        }
    }
}
