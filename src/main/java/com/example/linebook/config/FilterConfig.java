package com.example.linebook.config;

import com.example.linebook.filter.JwtAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
//        FilterRegistrationBean<JwtAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new JwtAuthFilter());
//        filterRegistrationBean.addUrlPatterns("/api/book/*", "/api/loans/*");
//        filterRegistrationBean.setOrder(1);
//        return filterRegistrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean<RoleAuthFilter> roleAuthFilter() {
//        FilterRegistrationBean<RoleAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new RoleAuthFilter());
//        filterRegistrationBean.addUrlPatterns("/api/books", "/api/books/*", "/api/loans/*");
//        filterRegistrationBean.setOrder(2);
//        return filterRegistrationBean;
//    }
}
