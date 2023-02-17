package com.github.maojx0630.mahjong.spring.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new HomeLogInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/view/**");
//    registry
//        .addInterceptor(new LoginInterceptor())
//        .addPathPatterns("/**")
//        .excludePathPatterns("/login/**")
//        .excludePathPatterns("/view/**");
  }
}
