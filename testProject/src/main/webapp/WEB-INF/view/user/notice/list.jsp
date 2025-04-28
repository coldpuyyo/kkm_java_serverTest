<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html>

<head>
   <meta charset="UTF-8">
   <title>index.jsp 및 Index.java 구현</title>
   
   <link rel="stylesheet" href="/static/css/notice_list.css">
   
   <script type="text/javascript">
      function search(){
         const optionField = document.querySelector('.optionField') 
            searchInput = document.querySelector('.searchInput');
         
         if(searchInput.value){
            location.href = '?pageNum=1&' + 'searchField=' + optionField.value + '&searchWord=' + searchInput.value; 
         } else {
            location.href = location.pathname;
         }
      }
      
      function changePageScope(page) {
         const optionField = document.querySelector('.optionField') 
         searchInput = document.querySelector('.searchInput');
         
          location.href = "?pageNum=" + page + '&searchField=' + optionField.value + '&searchWord=' + searchInput.value;
      }
   </script>
</head>

<body>
   <!-- ================================================================================================================================= -->
   <!-- 로고 링크 추가 -->
   
   <h1 id="logo"><a href="/">PSYLAB</a></h1>
   
   <!-- ================================================================================================================================= -->
   
   <div id="body">
      <main class="main">
         <h2>공지사항</h2>

         <div>
            <div class="hidden">공지사항 검색 필드</div>
            <label>검색분류
               <select class="optionField">
                  <option value="title" ${searchField == 'writer_Id' ? '' : 'selected' }>제목</option>
                  <option value="writer_Id" ${searchField == 'writer_Id' ? 'selected' : '' }>작성자</option>
               </select>
            </label>
            <label>검색어
               <input type="text" class="searchInput" value="${searchWord}" />
            </label>
            <button type="button" onclick="search()">검색</button>
         </div>
         <hr>

         <div>
            <h3 class="hidden">공지사항 목록</h3>
            <div class="table">
               <div class="thead">번호</div>
               <div class="thead">제목</div>
               <div class="thead">작성자</div>
               <div class="thead">작성일</div>
               <div class="thead">조회수</div>
               
               <c:forEach var="noticeView" items="${noticeViews}">
                  <div>${noticeView.id}</div>
                  <div>
                     <a href="detail/page?id=${noticeView.id}&pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">
                     ${noticeView.title}
                     <span class="colorRed">( ${noticeView.cmt_cnt} )</span>
                     </a>
                  </div>
                  <div>${noticeView.writer_id}</div>
                  <div>${noticeView.regDate}</div>
                  <div class="colorRed">${noticeView.hit}</div>
               </c:forEach>
            </div>
         </div>
         <hr>
            
         <c:set var="wholePage" value="${ Math.ceil( noticeCnt / ( pagingSizeValue * pagenationSet ) ) }"/>
         <c:set var="pageNationStartNum" value="${ pageNum - ( pageNum - 1 ) % pagenationSet }"/>
         <c:set var="pageNationLastNum" value="${ Math.ceil( noticeCnt / pagingSizeValue ) }"/> 
         <c:set var="currentPage" value="${ fn:substringBefore( Math.ceil( pageNationStartNum / pagenationSet ), '.' ) }"/>
         
         <div class="pageNationPart">
            <div>
               <h3 class="hidden">현재 페이지</h3>
               <div>
                  <span>${ currentPage }</span> / <span class="colorRed">${fn:substringBefore(wholePage, '.')}</span> pages
               </div>
            </div>

            <div>
               <ul>
                  <c:forEach var="i" begin="0" end="${pagenationSet - 1 }" >
                     <c:set var="printPageNum" value="${ pageNationStartNum + i }"/>
                     <c:if test="${printPageNum <= pageNationLastNum}">
                        <li ${ pageNum == printPageNum ? 'class = sellected' : '' }><a href="?pageNum=${printPageNum}&searchField=${searchField}&searchWord=${searchWord}">${printPageNum}</a></li>
                     </c:if>
                  </c:forEach>
               </ul>
               
               <div class="btn">
                  <c:choose>
                     <c:when test="${currentPage > 1}">
                        <button class="btn-prev" onclick = "changePageScope( ${pageNationStartNum - 1} )">이전</button>
                     </c:when>
                     <c:otherwise>
                        <button class="btn-prev" onclick="alert('이전 페이지가 없습니다.');">이전</button>
                     </c:otherwise>
                  </c:choose>
                  
                  <c:choose>
                     <c:when test="${ currentPage < wholePage }">
                        <button class="btn-next" onclick="changePageScope( ${pageNationStartNum + 5 } )">다음</button>
                     </c:when>
                     <c:otherwise>
                        <button class="btn-next" onclick="alert('다음 페이지가 없습니다.');">다음</button>
                     </c:otherwise>
                  </c:choose>
               </div>
            </div>
         </div>
      </main>
   </div>
</body>

</html>