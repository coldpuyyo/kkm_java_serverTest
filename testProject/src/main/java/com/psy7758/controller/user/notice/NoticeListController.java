package com.psy7758.controller.user.notice;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.dto.view.notice.NoticeView;
import com.psy7758.service.Service;
import com.psy7758.service.imp.UserService;

@WebServlet("/user/notice/list")
public class NoticeListController extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int defaultPageNum = 1;

      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
      Service service = new UserService();
      List<NoticeView> notices = null;
      int noticeCnt = 0;
      
      if( searchWord == null ) {
         notices = service.getNotices(defaultPageNum);
         noticeCnt = service.getNoticeCnt();
         
         pageNum = String.valueOf(defaultPageNum);
      } else if ( searchWord.equals("") ) {
         notices = service.getNotices(Integer.parseInt(pageNum));
         noticeCnt = service.getNoticeCnt();
      } else {
         notices = service.getNotices(Integer.parseInt(pageNum), searchField, searchWord);
         noticeCnt = service.getNoticeCnt(searchField, searchWord);
      }
      
      request.setAttribute("pagingSizeValue", getServletContext().getInitParameter("pagingSizeValue"));
      request.setAttribute("pagenationSet", getServletContext().getInitParameter("pagenationSet"));
      request.setAttribute("noticeViews", notices);   // 속성명 변경.
      request.setAttribute("noticeCnt", noticeCnt);
      request.setAttribute("pageNum", pageNum);
      request.setAttribute("searchField", searchField);
      request.setAttribute("searchWord", searchWord);
      
      request.getRequestDispatcher("/WEB-INF/view/user/notice/list.jsp").forward(request, response);
   }
}