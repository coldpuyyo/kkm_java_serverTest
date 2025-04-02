package com.psy7758.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javax.servlet.ServletContext;

import com.psy7758.dto.Client;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class CommonModule implements Dao {
   private static final HikariConfig config = new HikariConfig();
   private static HikariDataSource dataSource;
    private static boolean firstAccess = true;
    
    // 생성자에 동기화 메서드 적용은 불가함에 주의.
   public CommonModule(ServletContext context, String driver, String url, String user_name, String psw) {
      synchronized (CommonModule.class) {
         if(firstAccess) {
            /*
             * 실제 서버에 HikariCP 를 적용키 위해서는 HikariConfig 설정에 JDBC 드라이버
             * 클래스 설정도 반드시 적용되어야함에 주의.
             * 또한 HikariConfig 에 대한 모든 초기 설정이 완료된 이후에야만, HikariConfig
             * 설정을 HikariDataSource 에 전달하여 HikariCP 생성이 가능하므로 순서에 주의.
             */
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(user_name);
            config.setPassword(psw);
            dataSource = new HikariDataSource(config);
            
            /*
             * HikariCP 를 애플리케이션 종료 시점에 해제하기 위해서는 ServletContextListener 의 contextDestroyed
             * 메서드에서 ServletContextEvent 객체를 통해 ServletContext 참조를 얻어 HikariDataSource 의 연결을
             * 해제해야 하므로 아래와같이 HikariDataSource 의 참조를 ServletContext 를 통해 전달.
             */
            context.setAttribute("dataSource", dataSource);
            
            /*
             * ServletContext 의 setInitParameter 메서드는 컨텍스트 초기화 전, 즉 ServletContextListener 시점
             * 에서만 호출 가능하여 이후 시점에서는 호출 불가.
             * 따라서 setInitParameter 메서드는 web.xml(context-param, init-param)이나 ServletContextListener
             * 를 통해 설정해야함에 주의.
             * 단, setAttribute 메서드는 컨텍스트 초기화 이후에도 동적으로 추가 가능.
             */
            context.setAttribute("closedJdbcUrl", url);
            
            firstAccess = false;
         }
      }
   }
   
   public ArrayList<Client> getClientData(String selectSql, String searchWord) throws SQLException {
      try(
            // HikariCP 객체를 통해 Connection 객체 반환.
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement  = connection.prepareStatement(selectSql)
      ){                  
         preparedStatement.setString(1, "%" + searchWord + "%");
         
         ArrayList<Client> clients = new ArrayList<Client>();
         try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
               Client client = new Client();
               
               client.setNum(resultSet.getInt("num"));
               client.setId(resultSet.getString("id"));
               client.setPwd(resultSet.getString("pwd"));
               client.setName(resultSet.getString("name"));
               client.setPhoneNum(resultSet.getString("phoneNum"));
               client.setBirthDate(Optional.ofNullable(resultSet.getDate("birthDate"))
                     .map(java.sql.Date::toLocalDate)
                     .orElse(null));
               client.setTotPoint(resultSet.getInt("totPoint"));
               client.setRegDate(Optional.ofNullable(resultSet.getTimestamp("regDate"))
                     .map(java.sql.Timestamp::toLocalDateTime)
                     .orElse(null));
               client.setPub(resultSet.getBoolean("pub"));
               
               clients.add(client);
            }
         }
         
         return clients;
      }
   }
   
   public int setPub(String updateSql, String id) throws SQLException {
      try(Scanner sc = new Scanner(System.in)){
         try(
               Connection connection = dataSource.getConnection();
               PreparedStatement preparedStatement  = connection.prepareStatement(updateSql)
         ){
            preparedStatement.setQueryTimeout(2);
            connection.setAutoCommit(false);
            
            preparedStatement.setString(1, id);
            int row = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("정상 업데이트!!");
            
            return row;
         } catch (SQLTimeoutException e) {
            System.out.println("접속 대기중...............");
            System.out.print("재시도('y') : ");
            
            if(!sc.next().equalsIgnoreCase("y")) {
               System.out.println("접속을 종료합니다!!");
               return 0;
            }
            
            System.out.println("재접속중...............");
            
            return setPub(updateSql, id);
         }
      }
   }
}