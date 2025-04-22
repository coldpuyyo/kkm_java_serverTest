package com.psy7758.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dto.Notice;
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

   public ArrayList<Notice> getNoticesDb(String selectSql, String searchWord) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
         preparedStatement.setString(1, "%" + searchWord + "%");

         ArrayList<Notice> notices = new ArrayList<Notice>();
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
               Notice notice = new Notice();

               notice.setId(resultSet.getInt("id"));
               notice.setTitle(resultSet.getString("title"));
               notice.setWriter_id(resultSet.getString("writer_id"));
               notice.setContent(resultSet.getString("content"));
               notice.setRegDate(resultSet.getTimestamp("regDate").toLocalDateTime());
               notice.setHit(resultSet.getInt("hit"));
               notice.setFiles(resultSet.getString("files"));

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
   
   /*
    * getPrevNoticeDb 와 getNextNoticeDb 의 로직 처리가 동일하므로 통합 실행을 위한 private 메서드 생성.
    * 아래 메서드를 public 메서드로 만들어 개별 DAO 에서 하나의 메서드로 일관된 호출 형식을 취할수도 있지만,
    * 내부 캡슐화와 호출 명시성을 위해 아래와같이 별도의 private 메서드로 구현.
    */
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
   
   public int setPubDb(String updateSql, String id) throws SQLException {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
         preparedStatement.setString(1, id);
         
         int row = preparedStatement.executeUpdate();

         return row;
      }
   }
}