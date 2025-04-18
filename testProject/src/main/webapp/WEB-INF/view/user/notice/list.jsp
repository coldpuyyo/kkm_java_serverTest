<%@page import="com.psy7758.service.imp.AdminService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.psy7758.dto.Notice"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>이전 페이지와 다음 페이지 링크 버튼에 실행에 따른 페이지 네이션 표시와 범위 제한</title>

<link rel="stylesheet" href="/static/css/notice_list.css">

<script type="text/javascript">
      function changePageScope(page) {
          location.href = "?pageNum=" + page;
          
          /*
             JSP 에서 내부 스크립트를 이용하여 직접 아래와같이 템플릿 리터럴의 표현식블럭을
             적용시에는 JSP 의 EL 로 해석하는 문제점 발생.
             따라서 JSP 에 내부 스크립트 적용시에는 JS 의 템플릿 리터럴의 표현식 블럭
             사용 금지.
             
             ※ JSP 내에 주석 적용시에도 EL 의 표현식 블럭을 주석 내에 표현시, 파싱 과정에서
               주석내의 EL 표현식 블럭을 그대로 해석하여 에러 발생의 요인이 됨에 주의. 
          */
//          location.href = `?pageNum=${page}`;
      }
   </script>
</head>

<body>
	<div id="body">
		<main class="main">
			<h2>공지사항</h2>

			<div>
				<form method="get" action="list">
					<fieldset>
						<legend class="hidden">공지사항 검색 필드</legend>

						<label>검색분류 
						<select name="searchField">
							<option value="title"
									${param.searchField == 'title' ? 'selected' : ''}>제목</option>
							<option value="writer_Id"
									${param.searchField == 'writer_Id' ? 'selected' : ''}>작성자</option>
						</select>
						</label> 
						<label>검색어 
						<input type="text" name="searchWord"
							value="${param.searchWord}" />
						</label> 
						<input type="submit" value="검색" />
					</fieldset>
				</form>
			</div>
			<hr>

			<div>
				<h3 class="hidden">공지사항 목록</h3>
				<div class="table">
					<div class="w60">번호</div>
					<div class="expand">제목</div>
					<div class="w100">작성자</div>
					<div class="w100">작성일</div>
					<div class="w60">조회수</div>

					<c:forEach var="notice" items="${noticesModel}">
						<div>${notice.id}</div>
						<div>
							<a href="detail/page?id=${notice.id}">${notice.title}</a>
						</div>
						<div>${notice.writer_id}</div>
						<div>${notice.regDate}</div>
						<div class="colorRed">${notice.hit}</div>
					</c:forEach>
				</div>
			</div>
			<hr>

			<div class="pageNationPart">
				<div>
					<h3 class="hidden">현재 페이지</h3>
					<div>
						<span>1</span> / 1 pages
					</div>
				</div>

				<div>
					<c:set var="pageNum"
						value="${ empty param.pageNum ? 1 : param.pageNum }" />

					<!-- 링크 클릭에 의해 로드된 DB 페이지에 해당하는 페이지 네이션 시작 번호  -->
					<c:set var="pageNationStartNum"
						value="${ pageNum - ( pageNum - 1 ) % 5 }" />
					<!--
                  하나의 페이지에 표시되는 레코드수가 10개로 가정하고 전체 레코드수가 321 ~ 330 범위로 가정할 때
                  페이지 네이션의 마지막 페이지 번호를 33번 이라 가정.
                  ( 전체 레코드 수가 아님에 주의. )
               -->
					<c:set var="pageNationLastNum" value="${33}" />

					<ul>
						<c:forEach var="i" begin="0" end="4">
							<c:set var="printPageNum" value="${ pageNationStartNum + i }" />

							<c:if test="${printPageNum <= pageNationLastNum }">
								<li><a href="?pageNum=${printPageNum}&searchField=${param.searchField}&searchWord=${param.searchWord}">${printPageNum}</a></li>
							</c:if>
						</c:forEach>
					</ul>

					<div class="btn">
						<!--
                     페이지 네이션 시작 번호가 1 보다 크다는 것은 6 이상이 되는 6, 11, 15, ... 등과 같은 시작 페이지
                     네이션을 의미하므로, "이전" 버튼을 클릭했을 때의 페이지 스코프 적용이 되도록 하되, 현재 페이지 네이션
                     시작 번호에서 1 을 뺀 페이지 네이션 번호가 "이전" 버튼을 클릭했을 때의 마지막 페이지 네이션 번호로 결정.
                     하지만 페이지 네이션 시작 번호가 1 이하이면 "이전" 버튼 실행에 대한 페이지가 넘어가지 않도록 제한.
                  -->
						<c:if test="${pageNationStartNum > 1 }">
							<button class="btn-prev"
								onclick="changePageScope(${pageNationStartNum - 1})">이전</button>
						</c:if>

						<c:if test="${pageNationStartNum <= 1 }">
							<button class="btn-prev" onclick="alert('이전 페이지가 없습니다.');">이전</button>
						</c:if>

						<!-- ================================================================================ -->

						<%--    <c:choose>
                     <c:when test="${pageNationStartNum > 1}">
                        <button class="btn-prev" onclick = "changePageScope(${pageNationStartNum - 1})">이전</button>
                     </c:when>
                     <c:otherwise>
                        <button class="btn-prev" onclick="alert('이전 페이지가 없습니다.');">이전</button>
                     </c:otherwise>
                  </c:choose> --%>

						<!-- ===================================================================================================================== -->

						<!-- 다음 페이지 스코프 네이션 시작번호 -->
						<c:set var="nextPageNationStartNum"
							value="${ pageNationStartNum + 5 }" />

						<!--
                     다음 페이지 네이션 시작번호가 마지막 페이지 네이션의 번호 이하이면 다음 페이지로 넘기되,
                     다음 페이지 네이션 시작번호가 마지막 페이지 네이션의 번호보다 크다면, 마지막 페이지 네이션의 번호가
                     현재 페이지 네이션의 범위에 포함되는 번호이므로 다음 페이지로 넘어가지 못하도록 설정.
                  -->
						<c:if test="${nextPageNationStartNum <= pageNationLastNum }">
							<button class="btn-next"
								onclick="changePageScope(${nextPageNationStartNum})">다음</button>
						</c:if>

						<c:if test="${nextPageNationStartNum > pageNationLastNum }">
							<button class="btn-next" onclick="alert('다음 페이지가 없습니다.');">다음</button>
						</c:if>

						<!-- ================================================================================ -->

						<%--    <c:choose>
                     <c:when test="${nextPageNationStartNum <= pageNationLastNum }">
                        <button class="btn-next" onclick="changePageScope(${nextPageNationStartNum})">다음</button>
                     </c:when>
                     <c:otherwise>
                        <button class="btn-next" onclick="alert('다음 페이지가 없습니다.');">다음</button>
                     </c:otherwise>
                  </c:choose> --%>
					</div>
				</div>
			</div>
		</main>
	</div>
</body>

</html>