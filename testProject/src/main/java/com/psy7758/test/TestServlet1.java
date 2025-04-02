package com.psy7758.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.service.imp.AdminService;

public class TestServlet1 extends HttpServlet {
   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter printWriter = response.getWriter();

      /*
       * service 메서드는 추상 메서드 원형에 throws 가
       * 정의되어 있지 않아, 메서드 내부에서 직접 예외처리를 해야함에 주의.
       */
      try {
         printWriter.println(new AdminService().getClient());
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
   }
}
