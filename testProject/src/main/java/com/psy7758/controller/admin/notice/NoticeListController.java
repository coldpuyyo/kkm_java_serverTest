package com.psy7758.controller.admin.notice;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psy7758.dto.view.notice.NoticeView;
import com.psy7758.service.imp.AdminService;

@WebServlet("/admin/notice/list")
public class NoticeListController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   
   /*
    * AdminService 를 doGet, doPost 에서 공유 사용을 위해 필드로 선언.
    * - 차후 의존성 주입을 위한 확장을 위해서도 권장.
    */
   private final AdminService service = new AdminService();
   
   /*
    * Axios 를 통해 전송된, JSON 데이터를 담기 위한 내부 정적 클래스 선언.
    */
   public static class PubDelData {
      private int[] pubTrueId_;
      private int[] pubFalseId_;
      private int[] delNotice;      // 이후 삭제할 공지 목록을 담기 위한 필드도 미리 선언.
      private String pudDelBtn;
      
      public PubDelData() {}

      public PubDelData(int[] pubTrueId_, int[] pubFalseId_, int[] delNotice, String pudDelBtn) {
         this.pubTrueId_ = pubTrueId_;
         this.pubFalseId_ = pubFalseId_;
         this.delNotice = delNotice;
         this.pudDelBtn = pudDelBtn;
      }

      public int[] getPubTrueId_() {
         return pubTrueId_;
      }

      public void setPubTrueId_(int[] pubTrueId_) {
         this.pubTrueId_ = pubTrueId_;
      }

      public int[] getPubFalseId_() {
         return pubFalseId_;
      }

      public void setPubFalseId_(int[] pubFalseId_) {
         this.pubFalseId_ = pubFalseId_;
      }

      public int[] getDelNotice() {
         return delNotice;
      }

      public void setDelNotice(int[] delNotice) {
         this.delNotice = delNotice;
      }

      public String getPudDelBtn() {
         return pudDelBtn;
      }

      public void setPudDelBtn(String pudDelBtn) {
         this.pudDelBtn = pudDelBtn;
      }
   }
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int defaultPageNum = 1;
      
      String pageNum = request.getParameter("pageNum");
      String searchField = request.getParameter("searchField");
      String searchWord = request.getParameter("searchWord");
      
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
      request.setAttribute("noticeViews", notices);
      request.setAttribute("noticeCnt", noticeCnt);
      request.setAttribute("pageNum", pageNum);
      request.setAttribute("searchField", searchField);
      request.setAttribute("searchWord", searchWord);
      
      request.getRequestDispatcher("/WEB-INF/view/admin/notice/list.jsp").forward(request, response);
   }
   
   /*
    * 프론트단에서 post 요청시의 doPost 메서드 추가 선언.
    */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PubDelData pubData = new ObjectMapper().readValue(request.getInputStream(), PubDelData.class);
      
      if( pubData.getPudDelBtn().equals("batchPubBtn") ) {
         service.setPub(pubData.getPubTrueId_(), pubData.getPubFalseId_());
      } else {
         service.delNotice(pubData.getDelNotice());
      }
   }
}