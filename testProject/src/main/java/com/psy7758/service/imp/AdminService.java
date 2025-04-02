package com.psy7758.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dao.Dao;
import com.psy7758.dao.imp.MariaDao;
import com.psy7758.dao.imp.MysqlDao;
import com.psy7758.dto.Client;
import com.psy7758.service.Service;

public class AdminService implements Service{
   private Dao dao = new MysqlDao();
//   private Dao dao = new MariaDao();

   @Override
   public ArrayList<Client> getClient() throws SQLException {
      return getClient("id", "");
   }

   @Override
   public ArrayList<Client> getClient(String searchField, String searchWord) throws SQLException {
      return dao.getClient(searchField, searchWord, true);
   }
   
   // 관리자에게만 제공되는 기능 - 관리자에만 적용되는 확장 메서드. 
   public int setClientPubTrue(String id) throws SQLException {
      return dao.setClientPubTrue(id);
   }
}