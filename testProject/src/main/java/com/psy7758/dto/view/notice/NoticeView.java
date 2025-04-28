package com.psy7758.dto.view.notice;

import java.time.LocalDateTime;

import com.psy7758.dto.Notice;

public class NoticeView extends Notice {
   private int cmt_cnt;

   public NoticeView() {}
   
   // Notice 클에스에 공개 여부 추가로 인한 생성자 수정. 
   public NoticeView(int id, String title, String writer_id, String content, LocalDateTime regDate, int hit, String files, boolean pub, int cmt_cnt) {
      super(id, title, writer_id, content, regDate, hit, files, pub);
      this.cmt_cnt = cmt_cnt;
   }

   public int getCmt_cnt() {
      return cmt_cnt;
   }

   public void setCmt_cnt(int cmt_cnt) {
      this.cmt_cnt = cmt_cnt;
   }

   // Notice 클에스에 공개 여부 추가로 인한 toString 수정. 
   @Override
   public String toString() {
      return "NoticeView [cmt_cnt=" + cmt_cnt + ", getCmt_cnt()=" + getCmt_cnt() + ", getId()=" + getId()
            + ", getTitle()=" + getTitle() + ", getWriter_id()=" + getWriter_id() + ", getContent()=" + getContent()
            + ", getRegDate()=" + getRegDate() + ", getHit()=" + getHit() + ", getFiles()=" + getFiles()
            + ", getPub()=" + getPub() + ", toString()=" + super.toString() + ", getClass()=" + getClass()
            + ", hashCode()=" + hashCode() + "]";
   }
}