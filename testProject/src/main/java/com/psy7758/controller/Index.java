package com.psy7758.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @WebServlet("/")로 매핑할 경우, 모든 요청(정적 리소스 포함)을 해당 서블릿이 가로챌 수 있음.
 * 따라서 이러한 문제를 피하기 위해 web.xml의 welcome-file 설정을 통해 루트(/) 요청에 대해서만
 * 해당 서블릿 또는 JSP 가 실행되도록 유도하는 것이 안정적임.
 */
@WebServlet("/index")
public class Index extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
   }
}