package com.com.spring.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerNameFilter implements Filter {

    private String newServerName; // 변경할 서버 이름

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 시 호출되는 메서드
        newServerName = "New Server Name"; // 변경할 서버 이름 초기화
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 필터가 적용될 때 호출되는 메서드
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 서버 이름 변경
        httpRequest.getServerName();
        httpResponse.setHeader("Server", newServerName);

        // 다음 필터 또는 서블릿으로 요청과 응답 전달
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 필터가 소멸될 때 호출되는 메서드
    }
}
