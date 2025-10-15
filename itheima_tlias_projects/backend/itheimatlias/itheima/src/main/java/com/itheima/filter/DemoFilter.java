package com.itheima.filter;

import com.itheima.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


// @WebFilter(urlPatterns = "/*")
//public class DemoFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("DemoFilter 执行中");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        if(request.getRequestURI().contains("/login")){
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        String token = request.getHeader("token");
//        if(token == null || token.isEmpty()){
//            ((HttpServletResponse)servletResponse).sendError(401,"未登录");
//            return;
//        }
//        try {
//            JwtUtils.parseJwt(token);
//
//
//        } catch (Exception e) {
//            ((HttpServletResponse)servletResponse).sendError(401,"未登录");
//            return;
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//        // System.out.println("DemoFilter 执行后");
//    }
//}
