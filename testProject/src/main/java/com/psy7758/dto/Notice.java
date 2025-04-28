package com.psy7758.dto;

import java.time.LocalDateTime;

public class Notice {
   private int id;
   private String title;
   private String writer_id;
   private String content;
   private LocalDateTime regDate;
   private int hit;
   private String files;
   
   /*
    * 공지사항 등록시 DB 에 부분적인 데이터 저장만 필요하므로, 이를 위해 별도의 오버로딩 생성자를
    * 추가하지 않고 디폴트 생성자를 통해 객체를 생성한후 setter 메서드를 통해 해당 객체의 필드를
    * 부분적으로 채우는 방식의 기법 적용.
    * 하지만 실제 오버로딩 생성자를 새로 추가하는 것이 실제 공지사항 등록시, 변경사항 처리를 최소화
    * 가능하지만 유연한 대처를 위해서는 디폴트 생성자 필요.
    */
   private boolean pub;   // 공개 여부 정보도 관리자 계시판 목록에 표시하기 위해 pub 필드 추가.
   
   public Notice() {}

   public Notice(int id, String title, String writer_id, String content, LocalDateTime regDate, int hit, String files,   boolean pub) {
      this.id = id;
      this.title = title;
      this.writer_id = writer_id;
      this.content = content;
      this.regDate = regDate;
      this.hit = hit;
      this.files = files;
      this.pub = pub;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getWriter_id() {
      return writer_id;
   }

   public void setWriter_id(String writer_id) {
      this.writer_id = writer_id;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public LocalDateTime getRegDate() {
      return regDate;
   }

   public void setRegDate(LocalDateTime regDate) {
      this.regDate = regDate;
   }

   public int getHit() {
      return hit;
   }

   public void setHit(int hit) {
      this.hit = hit;
   }

   public String getFiles() {
      return files;
   }

   public void setFiles(String files) {
      this.files = files;
   }
   
   public boolean getPub() {
      return pub;
   }

   public void setPub(boolean pub) {
      this.pub = pub;
   }

   @Override
   public String toString() {
      return "Notice [id=" + id + ", title=" + title + ", writer_id=" + writer_id + ", content=" + content
            + ", regDate=" + regDate + ", hit=" + hit + ", files=" + files + ", pub=" + pub + ", getId()=" + getId()
            + ", getTitle()=" + getTitle() + ", getWriter_id()=" + getWriter_id() + ", getContent()=" + getContent()
            + ", getRegDate()=" + getRegDate() + ", getHit()=" + getHit() + ", getFiles()=" + getFiles()
            + ", getPub()=" + getPub() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
            + ", toString()=" + super.toString() + "]";
   }
}