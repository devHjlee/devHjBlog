package com.com.spring.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CustomRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화할 때 실행
        log.info("Custom Request init START");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // GET 방식 요청 중 '/filterData' 경로에 대해서만 파라미터 변경
        if (req.getMethod().equals("GET") && req.getRequestURI().equals("/filterData")) {
            // 요청을 위한 커스텀 래퍼(wrapper) 생성
            CustomRequestWrapper requestWrapper = new CustomRequestWrapper(req);

            // 원하는 대로 파라미터 수정
            requestWrapper.setParameter("name", req.getParameter("name"));
            requestWrapper.setParameter("age", req.getParameter("age"));
            requestWrapper.setParameter("user", "1");

            // 수정된 요청으로 계속 진행
            filterChain.doFilter(requestWrapper, response);
        } else {
            // 다른 요청에 대해서는 기존 요청 그대로 전달
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 종료될 때 실행
        log.info("Custom Request init Destory");
    }
}
