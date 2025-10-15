package com.itheima.service.impl;


import com.itheima.mapper.LoginMapper;
import com.itheima.pojo.Emps;
import com.itheima.pojo.LoginUser;
import com.itheima.service.LoginService;
import com.itheima.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private LoginMapper loginMapper;

    @Override
    public LoginUser queryLoginUser(Emps emp) {
        LoginUser loginU = loginMapper.queryLoginUser(emp.getUsername(), emp.getPassword());
        if (loginU != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginU.getId());
            claims.put("username", loginU.getUsername());
            claims.put("name", loginU.getName());
            String token = JwtUtils.generateJwt(claims);
            loginU.setToken(token);
        }
        return loginU;
    }
}
