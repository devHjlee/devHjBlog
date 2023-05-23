package com.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.exception.AuthFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Component
public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        log.info("preHandle Start");
        // 컨트롤러 실행 전에 수행될 로직을 구현합니다.
        // 예시로 filterData 일때 인증예외를 발생시킵니다.
        if(request.getRequestURI().equals("/filterData")) {
            throw new AuthFailException("TOKEN NULL");
        }
        // true를 반환하면 컨트롤러가 실행되고, false를 반환하면 컨트롤러를 실행하지 않습니다.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 컨트롤러 실행 후에 수행될 로직을 구현합니다.
        log.info("postHandle Start");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // 뷰가 렌더링된 후에 수행될 로직을 구현합니다.
        log.info("afterCompletion Start");
    }
}
