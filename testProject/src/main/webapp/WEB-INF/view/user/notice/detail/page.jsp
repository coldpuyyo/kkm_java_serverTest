<%@page import="com.psy7758.service.imp.AdminService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.psy7758.dto.Notice"%>
<!-- ================================================================================================== -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- ================================================================================================== -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html>

<head>
   <meta charset="UTF-8">
   <title>detail.jsp 페이지의 첨부 파일에 대한 목록 분리와 링크 설정</title>
   
   <link rel="stylesheet" href="/static/css/notice_detail_page.css">
</head>

<body>
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
               
               <!-- ====================================================================================================================== -->      
               
               <div>첨부파일</div>
               <div class="attachedfilesList">
                  <c:forTokens var="file" items="${noticeModel.files}" delims="," varStatus="status">
                          <a href="${file}">
                              <c:choose>
                                  <c:when test="${fn:endsWith(file, 'gif')}">${fn:toUpperCase(file)}</c:when>
                                  <c:otherwise>${file}</c:otherwise>
                              </c:choose>
      
                              <%-- ${fn:endsWith(file, 'gif' ) ? fn:toUpperCase(file) : file} --%>
                          </a>
                          ${status.last ? '' : '/ '}
                       </c:forTokens>
               </div>
               
               <!-- ====================================================================================================================== -->      
               
               <div>${noticeModel.content}</div>
            </div>
         </div>
         
         <div class="pageNavigation">
            <div>
               <a href="/notice/list">목록</a>
            </div>

            <div>이전글</div>
            <div><a href="">인터넷 보안의 핵심 원리</a></div>
            
            <div>다음글</div>
            <div>다음글이 없습니다.</div>
         </div>
      </main>
   </div>
</body>

</html>