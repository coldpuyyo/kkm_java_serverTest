package com.psy7758.controller.admin.notice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psy7758.service.imp.AdminService;

@WebServlet("/admin/notice/list")
public class NoticeListController extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int defaultPageNum = 1;
      
      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
      AdminService service = new AdminService();
      
      if( searchWord == null ) {
         request.setAttribute("noticesModel", service.getNotices(defaultPageNum));
         
         /*
          * 최초 로딩시에도 list.jsp 로 포워딩전, 요청 객체(request)를 통해 코드 하단부와같이
          * 디폴트 데이터로 초기화하여 pageNum 을 심어 전달함으로써, list.jsp 에서 pageNum
          * 을 통한 연산시 별도의 방어코드 불필요.
          */
         pageNum = String.valueOf(defaultPageNum);
      } else if ( searchWord.equals("") ) {
         request.setAttribute("noticesModel", service.getNotices(Integer.parseInt(pageNum)));
      } else {
         request.setAttribute("noticesModel", service.getNotices(Integer.parseInt(pageNum), searchField, searchWord));
      }
      
      request.setAttribute("pagingSizeValue", getServletContext().getInitParameter("pagingSizeValue"));
      request.setAttribute("pagenationSet", getServletContext().getInitParameter("pagenationSet"));
      
      /*
       * 최초 로딩시에는 파리미터 값이 전달되지 않은 상태이므로 포워딩된 list.jsp 에서는 EL 에서 param 을 읽을 수 없음에 주의.
       * 따라서 아래와같이 request 객체에 디폴트 pageNum, searchField, searchWord 값을 심어 전달함으로써,
       * lisp.jsp 에서 최초 로딩시의 param 을 통한 수신시 문제점 해소와 편리한 사용을 위해 param 을 통한 데이터 수신이
       * 아닌, EL 에서 요청 객체를 통해 받도록 아래와같이 요청 객체에 저장하여 포워딩하고, JSP 내에서의 데이터 전송은
       * 쿼리스트링을 통해 전달하도록 설정.
       */
      request.setAttribute("pageNum", pageNum);
      request.setAttribute("searchField", searchField);
      request.setAttribute("searchWord", searchWord);
      
      request.getRequestDispatcher("/WEB-INF/view/admin/notice/list.jsp").forward(request, response);
   }
}