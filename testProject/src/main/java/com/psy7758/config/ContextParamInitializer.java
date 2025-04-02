package com.psy7758.config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.psy7758.context.ServletContextHolder;
import com.zaxxer.hikari.HikariDataSource;

@WebListener
public class ContextParamInitializer implements ServletContextListener {
   @Override
   public void contextInitialized(ServletContextEvent sce) {
      ServletContext context = sce.getServletContext();
      
      /*
       * 서블릿이 아닌 클래스에서도 ServletContext 를 사용하여 초기 파라미터 설정이 가능하도록
       * ServletContextHolder 를 이용하여 ServletContext 를 저장.
       */
      ServletContextHolder.setServletContext(context);
      
      /*
       * 개별 DBMS 들의 JDBC 설정 정보를 개별 DAO 에서 별도로 저장하는 것이 아닌, ServletContextListener
       * 에서 ServletContext 를 이용하여 초기화함으로써, 개별 DAO 에서는 ServletContextHolder 를 이용
       * ServletContext 객체의 참조를 통해 해당 개별 JDBC 에 해당하는 설정값을 얻어 DBCP(HikariCP)의 설정을
       * 초기화.
       */
      context.setInitParameter("mysql_driver", "com.mysql.cj.jdbc.Driver");
      context.setInitParameter("maria_driver", "org.mariadb.jdbc.Driver");
      context.setInitParameter("mysql_url", "jdbc:mysql://localhost:3306/test_db");
      context.setInitParameter("maria_url", "jdbc:mariadb://localhost:3307/maria_db");
      context.setInitParameter("oracle_userName", "PSY");
      context.setInitParameter("mysql_userName", "kkm");
      context.setInitParameter("maria_userName", "kkm");
      context.setInitParameter("oracle_psw", "p_28717342");
      context.setInitParameter("mysql_psw", "qntleh378@");
      context.setInitParameter("maria_psw", "qntleh378@");
   }
   
   /*
    * ServletContextListener 에 정의된 contextDestroyed 메서드는 추상 메서드 원형에 throws 가
    * 정의되어 있지 않아, 메서드 내부에서 직접 예외처리를 해야함에 주의.
    */
   @Override
   public void contextDestroyed(ServletContextEvent sce) {
      /*
       * 내부적으로 데이터베이스 연결 풀을 관리하는 HikariDataSource 는 애플리케이션이 종료될 때 명시적으로
       * 연결 풀을 닫지 않으면 데이터베이스 연결이 해제되지 않아 운영체제나 데이터베이스 서버의 리소스가 불필요하게
       * 점유되고, 데이터베이스 서버가 연결을 처리할 수 없게 되어 새로운 요청을 받을 수 없는 상태가 될 수 있으므로
       * 반드시 종료(close)해야함.
       * 따라서 contextDestroyed 메서드를 통해 애플리케이션이 종료되는 시점에서 HikariDataSource 연결을
       * 종료하도록 설정.
       * 
       * DAO 의 공통 모듈이되는 CommonModule 에서 실제 HikariDataSource 의 연결을 설정하고 생성하므로
       * 이를 애플리케이션 종료 시점에 해제하기 위해서는 HikariDataSource 의 참조가 필요하므로, CommonModule
       * 에서 ServletContext 를 통해 HikariDataSource 의 참조를 저장함으로써, contextDestroyed 메서드의
       * ServletContextEvent 객체를 통해 해당 참조를 얻어 HikariDataSource 의 연결을 해제.
       * 
       * ※ ServletContext 의 getInitParameter 와 setInitParameter 메서드는 문자열 정보만 반환 및 전달
       *   가능한 반면, getAttribute 와 setAttribute 메서드는 문자열뿐만 아니라 다른 참조형 데이터도 반환 및
       *   전달 가능.
       *   단, getAttribute 와 setAttribute 메서드는 Object 타입을 반환 및 전달 가능함에 주의.
       */
      ((HikariDataSource)sce.getServletContext().getAttribute("dataSource")).close();
      
      /*
       * MySQL JDBC 드라이버(mysql-connector-java)는  AbandonedConnectionCleanupThread 라는 백그라운드
       * 쓰레드를 자동으로 실행하여, 사용하지 않는 연결이나 자원을 정리.
       * 웹 애플리케이션이 종료될 때, 해당 쓰레드가 정상적으로 종료되지 않으면 Tomcat 은 이를 감지하여 메모리 누수 발생에
       * 따른 예외를 발생. 따라서 아래와같이 checkedShutdown 메서드를 통해 해당 백그라운드 스레드를 종료해야함에 주의.
       * 단, 다른 데이터베이스 드라이버(Oracle, MariaDB 등)는 이런 방식의 백그라운드 쓰레드를 사용하지 않기 때문에
       * 해당 문제가 발생치 않음.
       */
      AbandonedConnectionCleanupThread.checkedShutdown();
      
      /*
       * 애플리케이션 서버(Tomcat 등)에서 웹 애플리케이션을 다시 로드하거나 종료할 때, JDBC 드라이버가 DriverManager 에
       * 등록된 상태로 남아 있다면, 해당 드라이버는 클래스 로더에 의해 다시 참조.
       * 따라서 이로 인한 드라이버 클래스가 메모리에서 해제되지 않아 메모리 누수가 발생되므로 deregisterDriver 메서드를
       * 통해 반드시 해제해야함에 주의.
       * 
       * ============================================================================================
       * 
       *       < getDriver >
       * 
       * - DriverManager 클래스의 정적메서드로써, JDBC 접속 URL 을 인자로 전달하면 해당 JDBC 드라이버의 참조를 반환.
       * 
       * ============================================================================================
       * 
       *      < deregisterDriver >
       *
       * -  DriverManager 클래스의 정적메서드로써, DriverManager 에서 등록 해제하려는 대상 JDBC 드라이버의 참조를
       *    전달하여 등록을 해제.
       */
      try {
         Driver driver = DriverManager.getDriver((String)(sce.getServletContext().getAttribute("closedJdbcUrl")));
         DriverManager.deregisterDriver(driver);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}