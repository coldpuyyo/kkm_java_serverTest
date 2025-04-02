package com.psy7758.dao.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.psy7758.context.ServletContextHolder;
import com.psy7758.dao.CommonModule;
import com.psy7758.dto.Client;

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
   public ArrayList<Client> getClient(String searchField, String searchWord, boolean pub) throws SQLException {
      String selectSql = String.format("SELECT @rowNum := @rowNum + 1 num, client.* "
            + "FROM client, ( SELECT @rowNUm := 0 ) rn "
            + "WHERE %s LIKE ? %s "
            + "ORDER BY regDate", searchField, pub ? "" : "AND pub = 1");
      
      return getClientData(selectSql, searchWord);
   }
   
   @Override
   public int setClientPubTrue(String id) throws SQLException {
      String updateSql = String.format("UPDATE client set pub = 1 WHERE id = ?");
      
      return setPub(updateSql, id);
   }
}