<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html>

<head>
   <meta charset="UTF-8">
   <title>View( list.jsp ) 페이지 추가 구성( 하나의 페이지 구성 단위를  pagingSizeValue X pagenationSet 묶음 단위로 구성 )과 이전, 다음 페이지 버튼 구현 - 2</title>
   
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
               <div class="thead">번호</div>     <!-- thead 로 클래스 변경 -->
               <div class="thead">제목</div>
               <div class="thead">작성자</div>
               <div class="thead">작성일</div>
               <div class="thead">조회수</div>
               
               <c:forEach var="notice" items="${noticesModel}">
                  <div>${notice.id}</div>
                  <div>
                     <a href="detail/page?id=${notice.id}&pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">${notice.title}</a></div>
                  <div>${notice.writer_id}</div>
                  <div>${notice.regDate}</div>
                  <div class="colorRed">${notice.hit}</div>
               </c:forEach>
            </div>
         </div>
         <hr>
            
         <!--
            wholePage : 하나의 페이지 구성 단위를 pagingSizeValue * pagenationSet 묶음 단위로 구성했을 때 전체 페이지 수.
         -->
         <c:set var="wholePage" value="${ Math.ceil( noticeCnt / ( pagingSizeValue * pagenationSet ) ) }"/>
         
         <!--
            pageNationStartNum : 링크 클릭에 의해 로드된 페이지(pageNum)에 해당하는 페이지 네이션 세트에서의 페이지 네이션 시작 번호
         -->
         <c:set var="pageNationStartNum" value="${ pageNum - ( pageNum - 1 ) % pagenationSet }"/>
         
         <!--
            pageNationLastNum : 레코드들을 pagingSizeValue 단위로 묶은 전체 페이지 네이션 갯수.
         -->
         <c:set var="pageNationLastNum" value="${ Math.ceil( noticeCnt / pagingSizeValue ) }"/> 
         
         <!--
            currentPage : 페이지 구성 단위를 pagingSizeValue * pagenationSet 묶음 단위로 구성했을 때의, pageNationStartNum 을 통한 현재 페이지 번호.
         -->
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
                     
                     <!--
                        아래 if 문의 페이지네이션 종료값 부분은 기존식과 동일하지만, 기존 종료값 부분에 지정했던
                        wholePage 변수에 할당했던 값과 의미가 이전과 변동되면서, pageNationLastNum 을 통해 할당.
                     -->
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