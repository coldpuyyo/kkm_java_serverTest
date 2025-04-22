package com.psy7758.controller.admin.notice.detail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.dto.Notice;
import com.psy7758.service.imp.AdminService;

@WebServlet("/admin/notice/detail/page")
public class DetailListController extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      /*
       * list.jsp 에서 쿼리스트링을 통해 id 는 키와 값 모두 전달되고, searchField 와 searchWord 는
       * 키의 경우 무조건 전달되도록 하였으므로 별도의 임시변수나 방어 코드 불필요.
       */
      int id = Integer.parseInt(request.getParameter("id"));
      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
      AdminService service = new AdminService();
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
      
      request.getRequestDispatcher("/WEB-INF/view/admin/notice/detail/page.jsp").forward(request, response);
   }
}