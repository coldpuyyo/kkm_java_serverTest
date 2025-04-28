package com.psy7758.dao.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dao.CommonModule;
import com.psy7758.dto.Notice;
import com.psy7758.dto.view.notice.NoticeView;

public class MysqlDao extends CommonModule {
   private static ServletContext context = ServletContextHolder.getServletContext();
   
   public MysqlDao() {
      super(
            context,
            context.getInitParameter("mysql_driver"),
            context.getInitParameter("mysql_url"),
            context.getInitParameter("mysql_userName"),
            context.getInitParameter("mysql_psw")
      );
   }
   
   @Override
   public ArrayList<NoticeView> getNotices(int pageNum, String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT * FROM notice_view "
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
   public int setPub(int[] pubTrueId_, int[] pubFalseId_) throws SQLException {
      String placeholders1 = String.join( ",", "?".repeat(pubTrueId_.length).split("") ),
       placeholders2 = String.join( ",", "?".repeat(pubFalseId_.length).split("") );
      
      String pubSql = String.format("UPDATE notice set pub = 1 WHERE id in(%s)", placeholders1),
      nonePubSql = String.format("UPDATE notice set pub = 0 WHERE id in(%s)", placeholders2);
      
      return setPubDb(pubSql, nonePubSql ,pubTrueId_, pubFalseId_);
   }

   @Override
   public int delNotice(int[] delId) throws SQLException {
      String placeholders = String.join( ",", "?".repeat(delId.length).split("") );
      String delSql = String.format("DELETE FROM NOTICE WHERE id in(%s)", placeholders);
      
      return delNoticeDb(delSql, delId);
   }

   @Override
   public int regNotice(Notice notice) throws SQLException {
      String insertSql = "INSERT INTO NOTICE(TITLE, WRITER_ID, FILES, CONTENT, PUB) VALUES(?, ?, ?, ?, ?)";
      
      return regNoticeDb(insertSql, notice);
   }
}