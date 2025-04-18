package com.psy7758.dao.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dao.CommonModule;
import com.psy7758.dto.Notice;

public class MysqlDao extends CommonModule {
	private static ServletContext context = ServletContextHolder.getServletContext();

	public MysqlDao() {
		super(context, context.getInitParameter("mysql_driver"), context.getInitParameter("mysql_url"),
				context.getInitParameter("mysql_userName"), context.getInitParameter("mysql_psw"));
	}

	@Override
	public ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord, boolean pub)
			throws SQLException {
		String selectSql = String.format(
				"SELECT * FROM NOTICE " + "WHERE %s LIKE ? %s " + "ORDER BY REGDATE DESC " + "LIMIT %d, %d;",
				searchField, pub ? "" : "AND pub = 1", (pageNum - 1) * getPagingSizeValue(), getPagingSizeValue());

		return getNoticesDb(selectSql, searchWord);
	}

	@Override
	public Notice getCurrentNotice(int id) throws SQLException {
		return getCurrentNoticeDb(id);
	}

	@Override
	public int setPub(String id) throws SQLException {
		String updateSql = String.format("UPDATE client set pub = 1 WHERE id = ?");

		return setPubDb(updateSql, id);
	}
}