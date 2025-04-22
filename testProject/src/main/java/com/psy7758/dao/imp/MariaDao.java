package com.psy7758.dao.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dao.CommonModule;
import com.psy7758.dto.Notice;

public class MariaDao extends CommonModule {
   private static ServletContext context = ServletContextHolder.getServletContext();
   
   public MariaDao() {
      super(
            context,
            context.getInitParameter("maria_driver"),
            context.getInitParameter("maria_url"),
            context.getInitParameter("maria_userName"),
            context.getInitParameter("maria_psw")
      );
   }
   
   @Override
   public ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT * FROM NOTICE "
            + "WHERE %s LIKE ? %s "
            + "ORDER BY REGDATE DESC "
            + "LIMIT %d, %d;",
            searchField, pub ? "" : "AND pub = 1", ( pageNum - 1 ) * getPagingSizeValue(), getPagingSizeValue()
      );
      
      return getNoticesDb(selectSql, searchWord);
   }
   
   @Override
   public int getNoticeCnt(String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT COUNT(ID) CNT FROM NOTICE WHERE %s LIKE ? %s",
            searchField, pub ? "" : "AND pub = 1");
      
      return getNoticeCntDb(selectSql, searchWord);
   }
   
   @Override
   public Notice getCurrentNotice(int id) throws SQLException {
      return getCurrentNoticeDb(id);
   }
   
   @Override
   public Notice getPrevNotice(int id, String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT * FROM NOTICE "
            + "WHERE %s %s LIKE ? "
            + "AND REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID = ?)"
            + "ORDER BY REGDATE DESC "
            + "LIMIT 1",
            pub ? "" : "pub = 1 AND", searchField);
      
      return getPrevNoticeDb(selectSql, id, searchWord);
   }

   @Override
   public Notice getNextNotice(int id, String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT * FROM NOTICE "
            + "WHERE %s %s  LIKE ? "
            + "AND REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID = ?)"
            + "LIMIT 1",
            pub ? "" : "pub = 1 AND", searchField);
      
      return getNextNoticeDb(selectSql, id, searchWord);
   }

   @Override
   public int setPub(String id) throws SQLException {
      String updateSql = String.format("UPDATE client set pub = 1 WHERE id = ?");
      
      return setPubDb(updateSql, id);
   }
}