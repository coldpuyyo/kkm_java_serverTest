package com.psy7758.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/*
 * 기존과같이 CharSetServletFilter 에 setContentType("text/html;charset=utf-8") 를 그대로
 * 지정하면, 매 요청시 마다 응답에 컨텐트 타입을 무조건 "text/html" 로 강제함으로써, JSP 에 지정한 외부
 * CSS 파일도 "text/html" 형식으로 서빙되어, 외부 CSS 가 적용되지 않는 문제점 발생.
 */
@WebFilter("/*")
public class CharSetServletFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
         throws IOException, ServletException {
      request.setCharacterEncoding("utf-8");
      
      /*
       *       < getServletPath >
       * 
       * - 현재 요청의 컨텍스트 루트를 제외한 요청 맵핑 또는 포워딩 경로 반환.
       */
        String path = ((HttpServletRequest)request).getServletPath();
        
        System.out.println("필터 실행 : " + path);
        
        /*
         * 요청에 대한 맵핑 경로 또는 포워딩 경로를 추출하여 CSS 문서가 아닌 경우에만, 응답에
         * "text/htm" 컨텐트 타입을 지정.
         * 
         * ※ 필터는 서블릿 뿐만 아니라, 웹 애플리케이션의 정적 리소스
         *   (HTML, CSS, JavaScript, 이미지 파일 등)를 포함하는 모든 요청전 선실행.
         * 
         * ==============================================================================
         * 
         *       < 실행 흐름 >
         * 
         * 1> NoticeListController 에서 최초 실행.
         * 
         * 2> NoticeListController 서블릿 실행전, CharSetServletFilter 실행. 
         *    - 최초 실행된 NoticeListController 의 맵핑 경로가 "/notice/list" 이므로 아래 컨텐트
         *      지정 메서드가 실행되고, NoticeListController 로 필터 체이닝.
         *    
         * 3> NoticeListController 서블릿이 실행되어 list.jsp 로 포워딩.
         * 
         * 4> list.jsp 도 서블릿이므로 list.jsp 로 포워딩전, 다시 CharSetServletFilter 실행.
         *      - list.jsp 에 대한 포워딩 경로가 "/WEB-INF/view/notice/list.jsp" 이지만, WEB-INF
         *      내부 파일은 직접 요청이 불가하므로 실제 CharSetServletFilter 는 실행되지 않음.
         *      
         * 5> 포워딩된 list.jsp 가 실행되면서, CSS 파일("/static/css/notice_list.css")이 링크로
         *    포함되어 있어, 브라우저가 HTML 을 해석하면서 다시 "/static/css/notice_list.css" 파일을 요청.
         *    
         * 6> "/static/css/notice_list.css" 파일 요청에 의해 요청전, 다시 CharSetServletFilter 실행.
         *    - 요청 경로가 "/static/css/notice_list.css" 이므로 아래 "text/htm" 컨텐트 지정 메서드는
         *      미실행되고 이후, CSS 파일을 정상적인 본래 컨텐트 타입("text/css")으로 설정된 상태로 브라우저로 서빙.
         */
        if (!path.endsWith(".css")) {
           System.out.println("text/html\n");
           
            response.setContentType("text/html;charset=utf-8");
        }
      
      filterChain.doFilter(request, response);
   }
}