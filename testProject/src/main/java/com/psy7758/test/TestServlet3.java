package com.psy7758.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet3 extends HttpServlet {
   protected void service(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      
      PrintWriter printWriter = response.getWriter();
      
      /*
       *       < getInitParameter >
       * 
       * - 서블릿 초기화 파라미터 값을 추출.
       *   ServletConfig 인터페이스로 구현된, GenericServlet 에 구현되어 있는 메서드로써,
       *   HttpServlet 이 GenericServlet 를 상속받으므로 바로 호출 가능.
       *   ( HttpServlet 클래스를 계층도 보기{Ctrl + t}로 바로 확인 가능 )
       * 
       */
      printWriter.printf("Oracle Driver Source : %s\n", getInitParameter("oracleDriverSrc"));
      printWriter.printf("MySQL Driver Source : %s\n", getInitParameter("mysqlDriverSrc"));
      printWriter.printf("Maria Driver Source : %s", getInitParameter("MariaDriverSrc"));
   }
}