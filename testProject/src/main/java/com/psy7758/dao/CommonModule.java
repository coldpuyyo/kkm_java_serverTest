package com.psy7758.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dto.Notice;
import com.psy7758.dto.view.notice.NoticeView;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class CommonModule implements Dao {
   private static final HikariConfig config = new HikariConfig();
   private static HikariDataSource dataSource;
   
   private static int pagingSizeValue = Integer.parseInt(
         ServletContextHolder.getServletContext().getInitParameter("pagingSizeValue")
   );

   public CommonModule(ServletContext context, String driver, String url, String user_name, String psw) {
      synchronized (CommonModule.class) {
         if (dataSource == null) {
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(user_name);
            config.setPassword(psw);
            dataSource = new HikariDataSource(config);

            context.setAttribute("dataSource", dataSource);
            context.setAttribute("closedJdbcUrl", url);
         }
      }
   }
   
   public static int getPagingSizeValue() {
      return pagingSizeValue;
   }
   
   public ArrayList<NoticeView> getNoticesDb(String selectSql, String searchWord) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
         preparedStatement.setString(1, "%" + searchWord + "%");

         ArrayList<NoticeView> notices = new ArrayList<NoticeView>();
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
               NoticeView notice = new NoticeView();

               notice.setId(resultSet.getInt("id"));
               notice.setTitle(resultSet.getString("title"));
               notice.setWriter_id(resultSet.getString("writer_id"));
               notice.setContent(resultSet.getString("content"));
               notice.setRegDate(resultSet.getTimestamp("regDate").toLocalDateTime());
               notice.setHit(resultSet.getInt("hit"));
               notice.setFiles(resultSet.getString("files"));
               notice.setPub(resultSet.getBoolean("pub"));
               notice.setCmt_cnt(resultSet.getInt("cmt_cnt"));
               
               notices.add(notice);
            }
         }

         return notices;
      }
   }
   
   public int getNoticeCntDb(String selectSql, String searchWord) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
         preparedStatement.setString(1, "%" + searchWord + "%");
         
         int selectCnt = 0;
         
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
               resultSet.next();
               selectCnt = resultSet.getInt("cnt");
         }

         return selectCnt;
      }
   }
   
   
   public Notice getCurrentNoticeDb(int id) throws SQLException {
      String selectSql = "SELECT * FROM notice WHERE id LIKE ?";
      
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
         preparedStatement.setInt(1, id);

         Notice notice = new Notice();
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            
            notice.setId(resultSet.getInt("id"));
            notice.setTitle(resultSet.getString("title"));
            notice.setWriter_id(resultSet.getString("writer_id"));
            notice.setContent(resultSet.getString("content"));
            notice.setRegDate(resultSet.getTimestamp("regDate").toLocalDateTime());
            notice.setHit(resultSet.getInt("hit"));
            notice.setFiles(resultSet.getString("files"));
         }

         return notice;
      }
   }
   
   private Notice getPreNextNotice(String selectSql, int id, String searchWord) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
         preparedStatement.setString(1, "%" + searchWord + "%");
         preparedStatement.setInt(2, id);
         
         Notice notice = null;
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if(resultSet.next()) {
               notice = new Notice();
               
               notice.setId(resultSet.getInt("id"));
               notice.setTitle(resultSet.getString("title"));
               notice.setWriter_id(resultSet.getString("writer_id"));
               notice.setContent(resultSet.getString("content"));
               notice.setRegDate(resultSet.getTimestamp("regDate").toLocalDateTime());
               notice.setHit(resultSet.getInt("hit"));
               notice.setFiles(resultSet.getString("files"));
            }
         }
         
         return notice;
      }
   }
   
   public Notice getPrevNoticeDb(String selectSql, int id, String searchWord) throws SQLException {
      return getPreNextNotice(selectSql, id, searchWord);
   }
   
   public Notice getNextNoticeDb(String selectSql, int id, String searchWord) throws SQLException {
      return getPreNextNotice(selectSql, id, searchWord);
   }
   
   public int setPubDb(String pubSql, String nonePubSql, int[] pubTrueId_, int[] pubFalseId_) throws SQLException {
      try (Connection connection = dataSource.getConnection()) {
         int row = 0;
         
         connection.setAutoCommit(false);
         
         try {
            if( pubTrueId_.length != 0 ) {
               try(PreparedStatement preparedStatement = connection.prepareStatement(pubSql)){
                  for (int i = 0; i < pubTrueId_.length; i++) {
                     preparedStatement.setInt(i + 1, pubTrueId_[i]);
                  }
                  
                  row = preparedStatement.executeUpdate();
               }
            }
            
            if( pubFalseId_.length != 0 ) {
               try(PreparedStatement preparedStatement = connection.prepareStatement(nonePubSql)){
                  for (int i = 0; i < pubFalseId_.length; i++) {
                     preparedStatement.setInt(i + 1, pubFalseId_[i]);
                  }
                  
                  row += preparedStatement.executeUpdate();
               }
            }
            
            connection.commit();
         } catch (Exception e) {
            e.printStackTrace();
            
            connection.rollback();
         }
         
         return row;
      } 
   }
   
   public int delNoticeDb(String delSql, int[] delId) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(delSql)) {
         int row = 0;
         
         if( delId.length != 0 ) {
            for (int i = 0; i < delId.length; i++) {
               preparedStatement.setInt(i + 1, delId[i]);
            }
            
            row = preparedStatement.executeUpdate();
         }
         
         return row;
      } 
   }
   
   public int regNoticeDb(String insertSql, Notice notice) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
         int row = 0;
         
         preparedStatement.setString(1, notice.getTitle());
         preparedStatement.setString(2, notice.getWriter_id());
         preparedStatement.setString(3, notice.getFiles());
         preparedStatement.setString(4, notice.getContent());
         preparedStatement.setBoolean(5, notice.getPub());
         
         row = preparedStatement.executeUpdate();
         
         return row;
      } 
   }
}