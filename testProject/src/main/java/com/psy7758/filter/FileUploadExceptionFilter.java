package com.psy7758.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class FileUploadExceptionFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
         throws IOException, ServletException {
      
      /*
       * NoticeRegController 서블릿에서의 @MultipartConfig 설정에 의한 파일 업로드 초과시 IllegalStateException 이
       * 발생되면서 FileSizeLimitExceededException 이 발생되는데, FileSizeLimitExceededException 예외는 요청이
       * Servlet 에 도달하기도 전에 Tomcat 이 예외를 던지면서 요청을 차단.
       * 따라서 해당 서블릿에서는 예외처리 자체가 불가함에 따라 아래와같이 서블릿 필터에서 예외처리.
       */
      try {
         filterChain.doFilter(request, response);
      } catch (IllegalStateException e) {
         
         /*
          * 예외가 발생되면 응답객체의 sendError 메서드를 통해 js 에서의 Axios 통신에 대한 응답을
          * 강제로 예외를 발생시킴으로써, 기존과 같이 js 에서 상위 공지목록 페이지로 디스패칭하지 않고
          * 예외 발생에 따른 메시지가 생성되도록 로직 처리. 
          */
         ((HttpServletResponse)response).sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      }
   }
}