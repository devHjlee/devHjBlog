package com.com.spring.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Slf4j
public class ParamFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화할 때 실행
        log.info("init START");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter START");
        chain.doFilter(new RequestWrapper((HttpServletRequest)request), response);
    }

    private static class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String values[] = super.getParameterValues(parameter); // 전달받은 parameter 불러오기

            if(values == null) {
                return null;
            }

            for(int i=0; i<values.length; i++) {
                if(values[i] != null) {
                    try {
                        values[i] = values[i]+i; // parameter 복호화
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return values;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter); // 전달받은 parameter 불러오기

            if(value == null) {
                return null;
            }

            try {
                value = value; // parameter 복호화
            } catch(Exception e) {
                e.printStackTrace();
            }

            return value;
        }
    }

    @Override
    public void destroy() {
        // 종료될 때 실행
        log.info("destroy START");
    }
}
