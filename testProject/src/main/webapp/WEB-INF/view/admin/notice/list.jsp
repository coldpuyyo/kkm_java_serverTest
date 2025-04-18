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
<title>검색 기능 및 개별목록과 페이지 네이션 클릭시 링크 경로 수정 및 보완</title>

<link rel="stylesheet" href="/static/css/notice_list.css">

<script type="text/javascript">
      function search(){
         const optionField = document.querySelector('.optionField') 
            searchInput = document.querySelector('.searchInput');
         
         if(searchInput.value){
            location.href = '?pageNum=1&' + 'searchField=' + optionField.value + '&searchWord=' + searchInput.value; 
         } else {
            /*   검색을 한후 검색값이 없으면 검색어가 없는 최초 로딩 상태로 되돌리기.    */
            
            // 쿼리 스트링 삭제. 빈 문자열을 그대로 전달하면 상대 경로가 정삭적으로 인식되지 않음에 주의.
            location.href = '?';

            /*
                  < pathname >
            
              - 현재 URL 경로에서 쿼리스트링을 제외한 경로 부분만 추출함으로써, 상기 방식보다 깔끔한 처리.
            */
//             location.href = location.pathname;
         }
      }
      
      
      function changePageScope(page) {
          location.href = "?pageNum=" + page;
      }
   </script>
</head>

<body>
	<div id="body">
		<main class="main">
			<h2>공지사항</h2>

			<div>
				<div class="hidden">공지사항 검색 필드</div>
				<label>검색분류 <select class="optionField">

						<!--
                     검색한 경우, searchField 에 'writer_Id' 값인지 또는 'title' 값인지에 따라 option 태그의
                     selected 속성이 결정되도록 설정.
                  -->
						<option value="title"
							${searchField == 'writer_Id' ? '' : 'selected' }>제목</option>
						<!-- selected 속성에 문자열 구분자 들어가야함에 주의 -->
						<option value="writer_Id"
							${searchField == 'writer_Id' ? 'selected' : '' }>작성자</option>
				</select>
				</label> <label>검색어 <input type="text" class="searchInput"
					value="${searchWord}" />
				</label>
				<button type="button" onclick="search()">검색</button>
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
							<!--
                        차후 DetailListController 에서 기존 목록 페이지(최초 로딩 페이지가 아닌, 바로 직전 페이지)로 돌아가기
                        위해서는 기존 pageNum, searchField, searchWord 값이 필요.
                        또한 "이전글", "다음글" 에 대한 디테일 페이지로 이동하기 위해서도 searchField, searchWord 가 필요하므로
                        아래와같이 쿼리스트링을 통해 전달.
                                                
                        ※ 제목이나 작성자를 입력하여 검색 조회를 하고 NoticeListController 서블릿에서 상세 목록 조회시, 검색을
                           기준으로 조회된 목록들을 기반으로 조회된 현재 목록에 대한 이전, 다음 목록 조회를 위해 searchField,
                           searchWord 를 DetailListController 서블릿에 전달키 위해 쿼리스트링 형태로 추가.
                          목록에 대한 상세 조회후 원래 페이지의 목록 부분으로 다시 돌아와야 하는 경우에도 pageNum 필요.                        
                     -->
							<a
								href="detail/page?id=${notice.id}&pageNum=${pageNum}&searchField=${searchField}&searchWord=${searchWord}">${notice.title}</a>
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
					<!--
                  NoticeListController 에서 최초 로딩시에 대한 pageNum 도 디폹트 값으로 설정하여
                  요청 객체에 심어 전달하므로, 기존과 같이 param 을 통한 pageNum 수신이 필요없고,
                  기존과같이 파라미터를 통해 수신함으로써 발생되는, 최초 로딩시의 쿼리스트링 파라미터가 전달
                  되지 않는 경우에 대한 파라미터값 체크를 위한 방어 코드 불필요. 
               -->
					<!-- 링크 클릭에 의해 로드된 DB 페이지에 해당하는 페이지 네이션 시작 번호  -->
					<c:set var="pageNationStartNum"
						value="${ pageNum - ( pageNum - 1 ) % pagenationSet }" />
					<c:set var="pageNationLastNum" value="${33}" />

					<ul>
						<c:forEach var="i" begin="0" end="${ pagenationSet - 1 }">
							<c:set var="printPageNum" value="${ pageNationStartNum + i }" />

							<c:if test="${printPageNum <= pageNationLastNum }">

								<!--
                           검색된 상태에서 페이지 네이션을 클릭했을 때에 대한, 해당 pageNum 에 대한 데이터를 다시 가져와야 하므로,
                           NoticeListController 에서 새로운 데이터 수신을 위해 searchField 와 searchWord 도 함께 전달. 
                        -->
								<li><a
									href="?pageNum=${printPageNum}&searchField=${searchField}&searchWord=${searchWord}">${printPageNum}</a></li>
							</c:if>
						</c:forEach>
					</ul>

					<div class="btn">
						<c:choose>
							<c:when test="${pageNationStartNum > 1}">
								<button class="btn-prev"
									onclick="changePageScope(${pageNationStartNum - 1})">이전</button>
							</c:when>
							<c:otherwise>
								<button class="btn-prev" onclick="alert('이전 페이지가 없습니다.');">이전</button>
							</c:otherwise>
						</c:choose>

						<c:set var="nextPageNationStartNum"
							value="${ pageNationStartNum + 5 }" />
						<c:choose>
							<c:when test="${nextPageNationStartNum <= pageNationLastNum }">
								<button class="btn-next"
									onclick="changePageScope(${nextPageNationStartNum})">다음</button>
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