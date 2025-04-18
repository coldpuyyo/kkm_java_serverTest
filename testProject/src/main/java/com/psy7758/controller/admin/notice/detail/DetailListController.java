package com.psy7758.controller.admin.notice.detail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.service.imp.AdminService;

@WebServlet("/admin/notice/detail/page")
public class DetailListController extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int id = Integer.parseInt(request.getParameter("id"));
      
         request.setAttribute("noticeModel", new AdminService().getNotices(id));
      
      request.getRequestDispatcher("/WEB-INF/view/notice/detail/page.jsp").forward(request, response);
   }
}