package com.psy7758.service;

import java.util.ArrayList;

import com.psy7758.dto.Notice;

/*
 * 컨트롤러 계층에서 직접 예외처리를 하지 않고, 서비스 계층에서 예외처리를 하도록 모든 인터페이스 메서드에 대한 throws 삭제.
 * 
 * ※ DAO 계층에서는 예외를 로깅만 하여 던지고, 서비스 계층에서 비즈니스 로직과 함께 적절히 처리하도록 하는 것이 일반적.
 */
public interface Service {
   //페이지 네이션 클릭시의 페이지 조회를 위한 오버로딩 메서드.
   ArrayList<Notice> getNotices(int pageNum);
   
   // 검색 필드와 검색어 입력된 상태에서의 페이지 네이션 클릭시의 조회를 위한 오버로딩 메서드.
   ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord);
   
   // list.jsp 페이지에서 목록을 클릭했을 때, 해당 목록에 대응되는 ID 를 통해 검색된 데이터를 detail/page.jsp 페이지에 로드하기 위한 메서드.
   Notice getCurrentNotice(int id);      // 기존 getNotice 메서드를 getCurrentNotice 로 메서드명 변경.
}