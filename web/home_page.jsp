
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="user_header.jsp"%>
<body>

<!--<div class="se-pre-con"></div>-->
<%--<div class="theme-layout">--%>

<c:set var="userPostList" value="${requestScope.USER_POST_LIST}"></c:set>
    ${requestScope.MESSAGE}
    <div class="gap gray-bg">
        <div class="container-fluid">
            <div class="col-lg-12 justify-content-center">
                <div>
                    <%@include file="create_article_page.jsp"%>
                </div>
                <div class="div-middle">
                    <c:if test="${not empty userPostList}">
                        <c:forEach items="${userPostList}" var="userPost">
                            <c:set var="article" value="${userPost.article}"></c:set>
                            <div class="central-meta item">
                                <div class="user-post">
                                    <div class="friend-info">
                                        <div class="friend-name" onclick="window.location.href='MainController?btnAction=View article detail&articleId=${article.articleId}'">
                                            <ins>${article.userId.fullName}</ins>
                                            <span>published: ${article.timeAgo}</span>
                                            <div class="post-meta">
                                                <img src="${pageContext.request.contextPath}${article.imagePath}" alt="">
                                                <div class="description">
                                                    <div><h1>${article.title}</h1></div>
                                                    <p class="description">${article.description}</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="we-video-info">
                                            <ul>
                                                <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 1}">
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1">
                                                        <span class="like" data-toggle="tooltip" title="like">
                                                            <i class="ti-heart"></i>
                                                            <ins>${userPost.numberOfLike}</ins>
                                                        </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2">
                                                        <span class="dislike" data-toggle="tooltip" title="dislike">
                                                            <i class="ti-heart-broken"></i>
                                                            <ins>${userPost.numberOfDislike}</ins>
                                                        </span>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 2}">
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1">
                                                            <span class="dislike" data-toggle="tooltip" title="like">
                                                                <i class="ti-heart"></i>
                                                                <ins>${userPost.numberOfLike}</ins>
                                                            </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2">
                                                            <span class="like" data-toggle="tooltip" title="dislike">
                                                                <i class="ti-heart-broken"></i>
                                                                <ins>${userPost.numberOfDislike}</ins>
                                                            </span>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 0}">
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1">
                                                            <span class="dislike" data-toggle="tooltip" title="like">
                                                                <i class="ti-heart"></i>
                                                                <ins>${userPost.numberOfLike}</ins>
                                                            </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2">
                                                            <span class="dislike" data-toggle="tooltip" title="dislike">
                                                                <i class="ti-heart-broken"></i>
                                                                <ins>${userPost.numberOfDislike}</ins>
                                                            </span>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                <li>
                                                        <span class="comment" data-toggle="tooltip" title="Comments">
                                                            <i class="fa fa-comments-o"></i>
                                                            <ins>${userPost.numberOfComment}</ins>
                                                        </span>
                                                </li>
                                                    <%--                            if is poster this will display&ndash;%&gt;--%>
                                                <c:if test="${authUser.userId.equals(article.userId.userId) || authUser.roleId.roleId == 2}" >
                                                    <li>
                                                        <a onclick="openConfirmBox${article.articleId}()">
                                                            <span class="remove" title="delete"><i class="fa fa-close"></i></span></a>
                                                    </li>

                                                    <script>
                                                        function openConfirmBox${article.articleId}() {
                                                            var r = confirm("Do you want to delete post?");
                                                            if (r == true) {
                                                                window.location.href='MainController?btnAction=Delete article&articleId=${article.articleId}'
                                                            }
                                                        }
                                                    </script>
                                                </c:if>



                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div><!-- centerl meta -->
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

<!-- Start Pagination -->
<c:set var="totalPage" value="${requestScope.TOTAL_PAGE}"></c:set>
<c:set var="currentPage" value="${requestScope.CURRENT_PAGE}"></c:set>
<div class="col-12">
    <ul class="pagination justify-content-center" style="position: fixed; bottom: 0px; right: 100px">
        <c:choose>
            <c:when test="${currentPage == 1}">
                <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item"><a class="page-link" href="MainController?btnAction=View home page&pageNumber=${currentPage - 1}&searchKeyword=${requestScope.SEARCH_KEYWORD}">Previous</a></li>
            </c:otherwise>
        </c:choose>

        <c:forEach begin="1" end="${totalPage}" varStatus="page">
            <c:choose>
                <c:when test="${currentPage == page.count}">
                    <li class="page-item disabled"><a class="page-link" href="#">${page.count}</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link" href="MainController?btnAction=View home page&pageNumber=${page.count}&searchKeyword=${requestScope.SEARCH_KEYWORD}">${page.count}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${currentPage == totalPage}">
                <li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item"><a class="page-link" href="MainController?btnAction=View home page&pageNumber=${currentPage + 1}&searchKeyword=${requestScope.SEARCH_KEYWORD}">Next</a></li>
            </c:otherwise>
        </c:choose>

    </ul>
</div>
<!-- End Pagination -->




</body>

</html>