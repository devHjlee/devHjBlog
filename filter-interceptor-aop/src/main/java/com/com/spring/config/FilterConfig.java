package com.com.spring.config;

import com.com.spring.filter.CorsFilter;
import com.com.spring.filter.ParamFilter;
import com.com.spring.filter.ServerNameFilter;
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
    public FilterRegistrationBean<ParamFilter> paramFilter() {
        FilterRegistrationBean<ParamFilter> registrationBean = new FilterRegistrationBean<>(new ParamFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*")); // 필터 적용 url
        registrationBean.setOrder(2); // 필터 적용 순서

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ServerNameFilter> serverNameFilter() {
        FilterRegistrationBean<ServerNameFilter> registrationBean = new FilterRegistrationBean<>(new ServerNameFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*")); // 필터 적용 url
        registrationBean.setOrder(3); // 필터 적용 순서

        return registrationBean;
    }
}
