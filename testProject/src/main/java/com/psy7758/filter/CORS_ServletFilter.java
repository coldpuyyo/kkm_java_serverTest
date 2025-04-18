package com.psy7758.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.context.ServletContextHolder;

@WebFilter("/*")
public class CORS_ServletFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
         throws IOException, ServletException {
      HttpServletRequest request_ = (HttpServletRequest)request;
      HttpServletResponse response_ = (HttpServletResponse)response;
      
      if (ServletContextHolder.getServletContext().getInitParameter("react_env").equals("development")) {
         response_.setHeader("Access-Control-Allow-Origin", request_.getHeader("Origin"));
         response_.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
         response_.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
         
         /*
          * 프론트엔드(React 등)에서 axios 요청을 보낼 때, 세션 쿠키나 인증 정보를 포함할 수 있도록
          * 허용하는 정보를 헤더에 설정.
          * 즉, CORS 요청에서 withCredentials: true 를 사용한 요청을 허용하는 역할. 
          */
         response_.setHeader("Access-Control-Allow-Credentials", "true");
         
         /*
          *       < OPTIONS 요청 >
          * 
          * - OPTIONS 요청(Preflight 요청)은 CORS(Cross-Origin Resource Sharing)환경에서 보안상의
          *   이유로 브라우저가 서버에게 먼저 허용 여부를 확인하는 과정으로써, "Preflight 요청" 이라고도 지칭.
          *   
          * ==============================================================================
          * 
          *       < OPTIONS 요청이 발생하는 요건 >
          * 
          * 1> 요청 메서드가 GET, HEAD, POST 가 아닌, PUT, DELETE, PATCH 인 경우.
          * 2> POST 요청이지만 Content-Type 이 application/x-www-form-urlencoded,
          *    multipart/form-data, text/plain 이 아닌, application/json 을 사용할 경우.
          * 3> 요청에 Authorization, X-Custom-Header 등 비표준 헤더가 포함된 경우.
          * 4> withCredentials: true 옵션을 사용하여 쿠키 또는 인증 정보를 포함하는 경우.
          * 
          * ==============================================================================
          * 
          * ※ 당 예시에서는 상기와 같은 이유로 하기 방어 코드가 설정되지 않은 경우, POST 요청전 브라우저가 서버
          *   에게 먼저 허용 여부를 확인하는 과정인 OPTIONS 요청이 먼저 발생되어, CORS_ServletFilter 가
          *   한번더 실행되는 결과 확인 가능.
          *   단, POST 요청보다 선행되는 OPTIONS 요청에서는 필터 체인(doFilter)에 의한 서블릿 전이는 되지
          *   않지만, 불필요한 doFilter 메서드의 호출로 인해 발생되는 불필요한 연산 처리를 방지하기 위한 아래와
          *   같은 방어 코드 필요.
          */
         if ("OPTIONS".equalsIgnoreCase(request_.getMethod())) {
            
            /*
             * 이 코드는 서버의 HTTP 응답 상태 코드를 200(OK)로 설정하는 역할.
             * 즉, 서버가 요청을 정상적으로 처리했음을 클라이언트(브라우저)에게 알리는 것으로써, OPTIONS 요청이
             * 들어왔을 때 별도의 처리 없이 200 응답을 즉시 반환하면, 브라우저는 서버가 해당 요청을 허용한다고
             * 판단하고 실제 요청을 처리.
             * 단, 상기 CORS 설정이 헤더에 포함된 상태에서 반환해야만, 브라우저가 CORS 정책 위반으로 차단하지
             * 않으므로 당 방어코드 설정이 상기 CORS 설정 보다 우선 처리되는 것은 불가.
             * 
             * ※ HTTP 응답 상태 코드의 기본값은 200 OK 이므로, 만약 서버가 따로 응답 코드를 설정하지 않으면,
             *   대부분의 경우 기본적으로 200 OK 상태 코드가 반환되므로 아래 코드는 생략 가능.
             *   하지만, 일부 서블릿 컨테이너등에서는 기본 응답 코드가 200 이 아닌 경우가 존재할 수 있고 코드
             *   명시성 확보를 위해 설정하는 것을 권장. 
             */
            response_.setStatus(HttpServletResponse.SC_OK);
              return;
           }

         System.out.println("개발 환경 CORS 활성화!!");
      }
      
      filterChain.doFilter(request, response);
   }
}