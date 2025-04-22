package com.psy7758.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dao.Dao;
import com.psy7758.dao.imp.MariaDao;
import com.psy7758.dao.imp.MysqlDao;
//import com.psy7758.dao.imp.OracleDao;
import com.psy7758.dto.Notice;
import com.psy7758.service.Service;

public class AdminService implements Service{
//   private Dao dao = new OracleDao();
   private Dao dao = new MysqlDao();
//   private Dao dao = new MariaDao();

   @Override
   public ArrayList<Notice> getNotices(int pageNum) {
      return getNotices(pageNum, "id", "");
   }
   
   @Override
   public ArrayList<Notice> getNotices(int pageNum, String searchField, String searchWord) {
      ArrayList<Notice> notices = null;
      try {
         notices = dao.getNotices(pageNum, searchField, searchWord, true);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return notices;
   }
   
   @Override
   public int getNoticeCnt() {
      return getNoticeCnt("id", "");
   }
   
   @Override
   public int getNoticeCnt(String searchField, String searchWord) {
      int noticeCnt = 0;
      
      try {
         noticeCnt = dao.getNoticeCnt(searchField, searchWord, true);
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return noticeCnt;
   }
   
   @Override
   public Notice getCurrentNotice(int id) {
      Notice notice = null;
      
      try {
         notice = dao.getCurrentNotice(id);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return notice;
   }
   
   @Override
   public Notice getPrevNotice(int id) {
      return getPrevNotice(id, "id", "");
   }

   @Override
   public Notice getPrevNotice(int id, String searchField, String searchWord) {
      Notice notice = null;
      
      try {
         notice = dao.getPrevNotice(id, searchField, searchWord, true);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return notice;
   }

   @Override
   public Notice getNextNotice(int id) {
      return getNextNotice(id, "id", "");
   }

   @Override
   public Notice getNextNotice(int id, String searchField, String searchWord) {
      Notice notice = null;
      
      try {
         notice = dao.getNextNotice(id, searchField, searchWord, true);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return notice;
   }

   // 관리자에게만 제공되는 기능 - 관리자에만 적용되는 확장 메서드. 
   public int setPub(String id) {
      int result = 0;
      
      try {
         result = dao.setPub(id);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return result;
   }
}