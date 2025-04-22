package com.psy7758.controller.user.notice.detail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.dto.Notice;
import com.psy7758.service.Service;
import com.psy7758.service.imp.UserService;

@WebServlet("/user/notice/detail/page")
public class DetailListController extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int id = Integer.parseInt(request.getParameter("id"));
      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
      Service service = new UserService();
      Notice prevNotice = null, nextNotice = null;
      
      if ( searchWord.equals("") ) {
         prevNotice = service.getPrevNotice(id);
         nextNotice = service.getNextNotice(id);
      } else {
         prevNotice = service.getPrevNotice(id, searchField, searchWord);
         nextNotice = service.getNextNotice(id, searchField, searchWord);
      }
      
      request.setAttribute("noticeModel", service.getCurrentNotice(id));
      request.setAttribute("prevNotice", prevNotice);
      request.setAttribute("nextNotice", nextNotice);
      request.setAttribute("pageNum", pageNum);
      request.setAttribute("searchField", searchField);
      request.setAttribute("searchWord", searchWord);
      
      request.getRequestDispatcher("/WEB-INF/view/user/notice/detail/page.jsp").forward(request, response);
   }
}