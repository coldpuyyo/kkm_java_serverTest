package com.psy7758.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet2 extends HttpServlet{
   @Override
   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter printWriter = response.getWriter();

      printWriter.printf("Oracle Driver Source : %s\n", getServletContext().getInitParameter("oracleDriverSrc"));
      printWriter.printf("MySQL Driver Source : %s\n", getServletContext().getInitParameter("mysqlDriverSrc"));
      printWriter.printf("Maria Driver Source : %s", getServletContext().getInitParameter("MariaDriverSrc"));
   }
}