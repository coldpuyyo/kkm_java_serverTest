package com.psy7758.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dao.Dao;
import com.psy7758.dao.imp.MariaDao;
import com.psy7758.dao.imp.MysqlDao;
//import com.psy7758.dao.imp.OracleDao;
import com.psy7758.dto.Notice;
import com.psy7758.service.Service;

public class AdminService implements Service {
//   private Dao dao = new OracleDao();
	private Dao dao = new MysqlDao();
//   private Dao dao = new MariaDao();

	@Override
	public ArrayList<Notice> getNotices(int pageNum) {
		return getNotices(pageNum, "id", "");
	}

	@Override
	public ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord) {
		ArrayList<Notice> notices = null;
		try {
			notices = dao.getNotices(pageNum, searchField, searchWord, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notices;
	}

	@Override
	public Notice getCurrentNotice(int id) { // 기존 getNotice 메서드를 getCurrentNotice 로 메서드명 변경.
		Notice notice = null;

		try {
			notice = dao.getCurrentNotice(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;
	}

	// 관리자에게만 제공되는 기능 - 관리자에만 적용되는 확장 메서드.
	public int setPub(String id) {
		int result = 0;

		try {
			result = dao.setPub(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}