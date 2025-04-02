package com.psy7758.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dao.Dao;
import com.psy7758.dao.imp.MariaDao;
import com.psy7758.dao.imp.MysqlDao;
import com.psy7758.dto.Client;
import com.psy7758.service.Service;

public class UserService implements Service{
   private Dao dao = new MysqlDao();
//   private Dao dao = new MariaDao();

   @Override
   public ArrayList<Client> getClient() throws SQLException {
      return getClient("id", "");
   }

   @Override
   public ArrayList<Client> getClient(String searchField, String searchWord) throws SQLException {
      return dao.getClient(searchField, searchWord, false);
   }
   
}