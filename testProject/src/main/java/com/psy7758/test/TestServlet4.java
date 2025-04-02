package com.psy7758.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
      urlPatterns = {"/test4"},
      initParams = {
            @WebInitParam(name = "oracleDriverSrc", value = "oracle.jdbc.driver.OracleDriver"), 
            @WebInitParam(name = "mysqlDriverSrc", value = "com.mysql.cj.jdbc.Driver"), 
            @WebInitParam(name = "MariaDriverSrc", value = "org.mariadb.jdbc.Driver"), 
      }
)
public class TestServlet4 extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      PrintWriter printWriter = response.getWriter();
      
      printWriter.printf("Oracle Driver Source : %s\n", getInitParameter("oracleDriverSrc"));
      printWriter.printf("MySQL Driver Source : %s\n", getInitParameter("mysqlDriverSrc"));
      printWriter.printf("Maria Driver Source : %s", getInitParameter("MariaDriverSrc"));
   }
}