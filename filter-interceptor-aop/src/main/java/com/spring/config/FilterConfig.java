package com.spring.config;

import com.spring.filter.CorsFilter;
import com.spring.filter.CustomRequestFilter;
import com.spring.filter.CustomResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new CorsFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*")); // 필터 적용 url
        registrationBean.setOrder(1); // 필터 적용 순서

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomRequestFilter> customRequestFilter() {
        FilterRegistrationBean<CustomRequestFilter> registrationBean = new FilterRegistrationBean<>(new CustomRequestFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*")); // 필터 적용 url
        registrationBean.setOrder(2); // 필터 적용 순서

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomResponseFilter> customResponseFilter() {
        FilterRegistrationBean<CustomResponseFilter> registrationBean = new FilterRegistrationBean<>(new CustomResponseFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*")); // 필터 적용 url
        registrationBean.setOrder(3); // 필터 적용 순서

        return registrationBean;
    }
}
