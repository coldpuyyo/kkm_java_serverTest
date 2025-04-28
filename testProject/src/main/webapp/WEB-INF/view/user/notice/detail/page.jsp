<%@page import="com.psy7758.dto.Notice"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html>

<head>
   <meta charset="UTF-8">
   <title>page.jsp 에서의 컨텐트에 HTML 인젝션을 통한 html 문서 및 이미지 포함하기와 이를 방어하기 위한 escapeXml 함수를 통한 HTML 이스케이핑</title>
   
   <link rel="stylesheet" href="/static/css/notice_detail_page.css">
</head>

<body>
   <h1 id="logo"><a href="/">PSYLAB</a></h1>
   
   <!-- ================================================================================================================================= -->
   
   <div id="body">
      <main>
         <div>
            <h3 class="hidden">공지사항 내용</h3>
            
            <div class="table">
               <div>제목</div>
               <div>${noticeModel.title}</div>
               <div>작성일</div>
               <div>${noticeModel.regDate}</div>
               <div>작성자</div>
               <div>${noticeModel.writer_id}</div>
               <div>조회수</div>
               <div>${noticeModel.hit}</div>
               <div>첨부파일</div>
               <div class="attachedfilesList">
                  <c:forTokens var="file" items="${noticeModel.files}" delims="," varStatus="status">
                     <a href="/upload/${file}">
                        ${fn:endsWith(file, 'gif') ? fn:toUpperCase(file) : file}
                     </a>
                          ${status.last ? '' : '/ '}
                       </c:forTokens>
               </div>
               <div>${ fn:escapeXml(noticeModel.content) }</div>      <!-- escapeXml 추가 -->
            </div>
         </div>
         
         <div class="pageNavigation">
            <div>
               <a href="/user/notice/list?pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">목록</a>
            </div>

            <div>이전글</div>
            <div>
               <c:choose>
                  <c:when test="${empty prevNotice}">이전글이 없습니다.</c:when>
                  <c:otherwise>
                     <a href="?id=${prevNotice.id}&pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">
                        ${prevNotice.title}
                     </a>
                  </c:otherwise>
               </c:choose>
            </div>
            
            <div>다음글</div>
            <div>
               <c:choose>
                  <c:when test="${empty nextNotice}">다음글이 없습니다.</c:when>
                  <c:otherwise>
                     <a href="?id=${nextNotice.id}&pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">
                        ${nextNotice.title}
                     </a>
                  </c:otherwise>
               </c:choose>
            </div>
         </div>
      </main>
   </div>
</body>

</html>