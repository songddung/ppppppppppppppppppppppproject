package com.example.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.project.Interceptor.SignInCheckInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private SignInCheckInterceptor signInCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInCheckInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/auth/signin","/auth/signup","/index","/map/map","/map/tourdestbaseinfo","/marker.png","/map/tourdestbaseinfoPoint","/map/map","/map/getPoint","/map/tourdestbaseinfo","/board/list","/board/write","/board/detail");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}