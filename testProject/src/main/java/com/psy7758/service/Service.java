package com.psy7758.service;

import java.util.ArrayList;

import com.psy7758.dto.Notice;
import com.psy7758.dto.view.notice.NoticeView;

public interface Service {
   /*
    * getNotices 메서드에 대한 반환 타입을 ArrayList<Notice> 에서 ArrayList<NoticeView> 로 변경.
    */
   
   //페이지 네이션 클릭시의 페이지 조회를 위한 오버로딩 메서드.
   ArrayList<NoticeView> getNotices(int pageNum);
   
   // 검색 필드와 검색어 입력된 상태에서의 페이지 네이션 클릭시의 조회를 위한 오버로딩 메서드.
   ArrayList<NoticeView> getNotices(int pageNum, String searchField, String searchWord);
   
   // 공지사항 전체 레코드수 조회를 위한 메서드.
   int getNoticeCnt();
   
   // 공지사항에서 검색 필드와 검색어가 입력된 상태에서의 검색된 레코드수 조회를 위한 메서드.
   int getNoticeCnt(String searchField, String searchWord);
   
   // list.jsp 페이지에서 목록을 클릭했을 때, 해당 목록에 대응되는 ID 를 통해 검색된 데이터를 detail/page.jsp 페이지에 로드하기 위한 메서드.
   Notice getCurrentNotice(int id);

   // 공지사항 상세 페이지 조회시의 이전 페이지 조회를 위한 메서드(전체 목록 조회시).   
   Notice getPrevNotice(int id);
   
   // 공지사항 상세 페이지 조회시의 이전 페이지 조회를 위한 메서드(검색 목록 조회시).
   Notice getPrevNotice(int id, String searchField, String searchWord);
   
   // 공지사항 상세 페이지 조회시의 다음 페이지 조회를 위한 메서드(전체 목록 조회시).
   Notice getNextNotice(int id);
   
   // 공지사항 상세 페이지 조회시의 다음 페이지 조회를 위한 메서드(검색 목록 조회시).
   Notice getNextNotice(int id, String searchField, String searchWord);
}