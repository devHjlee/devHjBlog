package com.com.spring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.HashMap;
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
        if (requestURI.equals("/getData")) {
            // 컨트롤러 실행 전에 응답을 가로채기 위해 CustomResponseWrapper 생성
            CustomResponseWrapper customResponseWrapper = new CustomResponseWrapper(response);

            // 필터 체인 실행 (컨트롤러로 진입)
            filterChain.doFilter(request, customResponseWrapper);

            // 컨트롤러가 생성한 응답값 가져오기
            String originalResponse = customResponseWrapper.getResponseAsString();

            // JSON 형식으로 변환하여 응답값 수정
            Map<String, Object> modifiedResponse = new HashMap<>();
            modifiedResponse.put("name", "a");
            modifiedResponse.put("age", 1);
            modifiedResponse.put("use", "Y");

            // 수정된 응답값을 JSON 형식으로 변환
            String modifiedResponseJson = new ObjectMapper().writeValueAsString(modifiedResponse);

            // 수정된 응답값을 클라이언트로 전송
            response.setContentType("application/json");
            response.getWriter().write(modifiedResponseJson);
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

    class CustomResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream buffer;
        private final ServletOutputStream outputStream;
        private final PrintWriter writer;

        public CustomResponseWrapper(HttpServletResponse response) throws IOException {
            super(response);

            buffer = new ByteArrayOutputStream();
            outputStream = new CustomOutputStream(buffer);
            writer = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()));
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return outputStream;
        }

        @Override
        public PrintWriter getWriter() {
            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            }
            if (outputStream != null) {
                outputStream.flush();
            }
        }

        public byte[] getResponseAsByteArray() throws IOException {
            flushBuffer();
            return buffer.toByteArray();
        }

        public String getResponseAsString() throws IOException {
            flushBuffer();
            return buffer.toString(getCharacterEncoding());
        }

        private class CustomOutputStream extends ServletOutputStream {
            private final ByteArrayOutputStream outputStream;

            public CustomOutputStream(ByteArrayOutputStream outputStream) {
                this.outputStream = outputStream;
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                outputStream.write(b, off, len);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        }
    }
}

