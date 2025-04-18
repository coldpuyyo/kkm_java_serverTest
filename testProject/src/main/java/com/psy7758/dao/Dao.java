package com.psy7758.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dto.Notice;

public interface Dao {
	// 검색 필드와 검색어 입력된 상태에서의 페이지 네이션 클릭시의 조회를 위한 메서드.
	ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord, boolean pub) throws SQLException;

	// list.jsp 페이지에서 목록을 클릭했을 때, 해당 목록에 대응되는 ID 를 통해 검색된 데이터를 detail/page.jsp 페이지에
	// 로드하기 위한 메서드.
	Notice getCurrentNotice(int id) throws SQLException; // ( 기존 getNotice 메서드를 getCurrentNotice 로 메서드명 변경 )

	int setPub(String id) throws SQLException;
}