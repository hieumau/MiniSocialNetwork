<%-- 
    Document   : home_page_article_page
    Created on : Sep 24, 2020, 11:10:55 AM
    Author     : saost
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="user_header.jsp"%>
<body>

<c:set var="article" value="${requestScope.ARTICLE}"></c:set>
<c:set var="commentList" value="${requestScope.COMMENT_LIST}"></c:set>
<c:set var="authUser" value="${sessionScope.AUTH_USER}"></c:set>
<c:if test="${not empty article}">
<div class="col-lg-12 align-self-center">


    <div class="central-meta item">
        <div class="user-post">
            <div class="friend-info">
<%--                <figure>--%>
<%--                    <img src="images/resources/friend-avatar10.jpg" alt="">--%>
<%--                </figure>--%>
                <div class="friend-name">
                    <ins>${article.userId.fullName}</ins>
                    <span>published: ${article.date.toString()}</span>
                </div>
                <div class="post-meta">
                    <img src="${pageContext.request.contextPath}${article.imagePath}" alt="">
                    <div class="">
                        <h1>${article.title}</h1>
<%--                        <p class="title">${article.title}</p>--%>
                        <p class="description">${article.description}</p>
                    </div>
                    <div class="we-video-info">
<%--                        reacted--%>
                        <ul>
                            <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 1}">
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1&isHomePage=no">
                                    <span class="like" data-toggle="tooltip" title="like">
                                        <i class="ti-heart"></i>
                                        <ins>${requestScope.LIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2&isHomePage=no">
                                    <span class="dislike" data-toggle="tooltip" title="dislike">
                                        <i class="ti-heart-broken"></i>
                                        <ins>${requestScope.DISLIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>

                            </c:if>
                            <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 2}">
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1&isHomePage=no">
                                    <span class="dislike" data-toggle="tooltip" title="like">
                                        <i class="ti-heart"></i>
                                        <ins>${requestScope.LIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2&isHomePage=no">
                                    <span class="like" data-toggle="tooltip" title="dislike">
                                        <i class="ti-heart-broken"></i>
                                        <ins>${requestScope.DISLIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${article.getReact(sessionScope.AUTH_USER.userId) == 0}">
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=1&isHomePage=no">
                                    <span class="dislike" data-toggle="tooltip" title="like">
                                        <i class="ti-heart"></i>
                                        <ins>${requestScope.LIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="MainController?btnAction=Press react&articleId=${article.articleId}&emotionType=2&isHomePage=no">
                                    <span class="dislike" data-toggle="tooltip" title="dislike">
                                        <i class="ti-heart-broken"></i>
                                        <ins>${requestScope.DISLIKE_EMOTION_NUMBER}</ins>
                                    </span>
                                    </a>
                                </li>
                            </c:if>
                                <li>
                                <span class="comment" data-toggle="tooltip" title="Comments">
                                    <i class="fa fa-comments-o"></i>
                                    <ins>${requestScope.COMMENT_NUMBER}</ins>
                                </span>
                                </li>
                                <%--                            if is poster this will display&ndash;%&gt;--%>
                            <c:if test="${authUser.userId.equals(article.userId.userId)}">
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
            <div class="coment-area">
                <ul class="we-comet">
<%--                        comment show--%>
                    <c:if test="${not empty commentList}">
                        <c:forEach items="${commentList}" var="comment">
                            <li>
                                <div class="we-comment g-post-classic">
                                    <div class="coment-head">
                                        <h5><a href="#" title=""> ${comment.userId.fullName} </a></h5>
                                        <span>${comment.timeAgo}</span>
            <%--                            if is poster this will display&ndash;%&gt;--%>
                                        <c:if test="${authUser.userId.equals(comment.userId.userId)}">
<%--                                            <button onclick="openConfirmCmtBox${comment.commentId}()" type="button"></button>--%>
                                            <script>
                                                function openConfirmCmtBox${comment.commentId}() {
                                                    var r = confirm("Do you want to delete comment?");
                                                    if (r == true) {
                                                        window.location.href='MainController?btnAction=Delete comment&commentId=${comment.commentId}'
                                                    }
                                                }
                                            </script>
                                            <a onclick="openConfirmCmtBox${comment.commentId}()"><span class="remove" title="delete"><i  class="fa fa-close"></i></span></a>

                                        </c:if>
                                        <p class="description">${comment.commentContent}</p>
                                    </div>
                                </div>

                            </li>
                        </c:forEach>
                    </c:if>

                        <div class="we-comment-send l-post" style="position: fixed; bottom: 10px; left: 20px; opacity: 1; cursor: pointer;">
                            <li class="post-comment" >
                                <div class="message-text-container">
                                    <form method="get" action="MainController">
                                        <input type="hidden" name="articleId" value="${article.articleId}">
                                        <textarea placeholder="Post your comment" name="content"></textarea>
                                        <button type="submit" name="btnAction" value="Post comment" title="send"><i class="fa fa-paper-plane"></i></button>
                                    </form>
                                </div>
                            </li>
                        </div>
<%--                        comment show--%>

                </ul>
            </div>
        </div>


</div><!-- centerl meta -->

</div>
    <%--    show toast message--%>
    <%--    toast message--%>
    <style>
        #snackbar {
            visibility: hidden;
            min-width: 250px;
            margin-left: -125px;
            background-color: #ffffff;
            color: #4e2121;
            text-align: center;
            border-radius: 2px;
            padding: 16px;
            position: fixed;
            z-index: 1;
            left: 50%;
            bottom: 30px;
            font-size: 17px;
        }

        #snackbar.show {
            visibility: visible;
            -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
            animation: fadein 0.5s, fadeout 0.5s 2.5s;
        }

        @-webkit-keyframes fadein {
            from {bottom: 0; opacity: 0;}
            to {bottom: 30px; opacity: 1;}
        }

        @keyframes fadein {
            from {bottom: 0; opacity: 0;}
            to {bottom: 30px; opacity: 1;}
        }

        @-webkit-keyframes fadeout {
            from {bottom: 30px; opacity: 1;}
            to {bottom: 0; opacity: 0;}
        }

        @keyframes fadeout {
            from {bottom: 30px; opacity: 1;}
            to {bottom: 0; opacity: 0;}
        }
    </style>
    <script>
        function showToastMessage() {
            var x = document.getElementById("snackbar");
            x.className = "show";
            setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
        }
    </script>

    <c:set var="message" value="${requestScope.MESSAGE}"></c:set>
    <div id="snackbar">${message}</div>

    <%--    toast message end--%>
    <c:choose>
        <c:when test="${not empty message}">
            <script>showToastMessage()</script>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>
</c:if>
</body>
</html>
