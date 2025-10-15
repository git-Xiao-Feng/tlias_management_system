package com.itheima.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 示例1：本地磁盘路径映射（访问 /files/** 时，读取 D:/static-files/ 下的资源）
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:C:\\Users\\Public\\Pictures\\javaupload\\");
    }
}
