<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>다중 파일 업로드</title>

    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script defer src="/static/js/admin/notice/board/reg.js"></script>
    <link rel="stylesheet" href="/static/css/notice_board_reg.css">
</head>

<body>
   <div 
      class="hidden"
      data-pageNum="${pageNum}"
      data-searchField="${searchField}"
      data-searchWord="${searchWord}"></div>
       
    <h1 id="logo"><a href="/">PSYLAB</a></h1>
    
    <div id="body">
        <main>
            <h2>공지사항 등록</h2>
            <div class="limitMsg"></div>

            <form name="regNoticeBoard" class="noticeForm">
                <div>
                    <h3 class="hidden">공지사항 입력</h3>
                    
                    <div class="table">
                        <label for="title">제목</label>
                        <input type="text" id="title"  name="title" />
                        
                        <!--
                              서블릿에서 Collection 형태로 수신하기 위한 편의성을 위해 name 식별자를 동일하게 설정. 
                        -->
                          <label for="file1">첨부파일 1</label>
                        <input type="file" id="file1"  name="files" />
                        
                          <label for="file2">첨부파일 2</label>
                        <input type="file" id="file2"  name="files" />
                        
                          <label for="file1">첨부파일 2</label>
                        <input type="file" id="file2"  name="files" />
                        
                        <label for="content">내용</label>
                        <textarea class="content" id="content" name="content"></textarea>
                        
                       <input type="checkbox" id="pub" name="pub" value="true" checked>
                       <label for="pub" for="pub" >바로공개</label> 
                    </div>
                </div>
                
                <div class="btn">
               <button type="button" name="regBtn" >등록</button>
               <button type="button" name="cancel" >취소</button>
            </div>
            </form>
        </main>
    </div>
</body>

</html>