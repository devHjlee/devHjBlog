package com.spring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Slf4j
public class CustomResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화할 때 실행
        log.info("Custom Reponse Filter init START");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 필터가 적용되는 요청 경로 확인
        String requestURI = request.getRequestURI();

        // '/getData' 경로에 대해서만 응답 값을 변경
        if (requestURI.equals("/filterData")) {
            // 컨트롤러 실행 전에 응답을 가로채기 위해 CustomResponseWrapper 생성
            CustomResponseWrapper customResponseWrapper = new CustomResponseWrapper(response);

            // 필터 체인 실행 (컨트롤러로 진입)
            log.info("CustomResponseFilter Start");
            filterChain.doFilter(request, customResponseWrapper);

            // 컨트롤러가 생성한 응답값 가져오기
            //String originalResponse = customResponseWrapper.getResponseAsString();
            Map<String,Object> originalResponse = customResponseWrapper.getResponseAsMap();

            // JSON 형식으로 변환하여 응답값 수정
            originalResponse.put("use","Y");

            // 수정된 응답값을 JSON 형식으로 변환
            String modifiedResponseJson = new ObjectMapper().writeValueAsString(originalResponse);

            // 수정된 응답값을 클라이언트로 전송
            response.setContentType("application/json");
            response.getWriter().write(modifiedResponseJson);
            log.info("CustomResponseFilter END");
        } else {
            // 다른 경로에 대해서는 기존 응답 그대로 전달
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 종료될 때 실행
        log.info("Custom Reponse Filter init Destory");
    }
}

