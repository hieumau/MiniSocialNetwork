<%-- 
    Document   : notification
    Created on : Sep 27, 2020, 6:22:40 PM
    Author     : saost
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="user_header.jsp"%>
<html>
    <body>
    <c:set var="notificationList" value="${requestScope.NOTIFICATION_LIST}"></c:set>
    <div class="central-meta">
        <div class="editing-interest">
            <h5 class="f-title"><i class="ti-bell"></i>All Notifications </h5>
            <div class="notification-box">

            </div>
        </div>
        <ul class="nearby-contct">
            <ul class="nearby-contct" >
                <c:if test="${not empty notificationList}">
                    <c:forEach var="notification" items="${notificationList}">
                        <c:if test="${notification.isSeen}">
                            <li>
                                <a href="MainController?btnAction=View article detail&articleId=${notification.articleId.articleId}&notificationId=${notification.notificationId}">
                                    <div class="notification-box">
                                        <p> <b>${notification.interactUserName}</b> ${notification} <b>${notification.articleTitle}</b></p>
                                        <span><p6>${notification.timeAgo}</p6></span>
                                    </div>
                                </a>
                            </li>
                        </c:if>

                        <c:if test="${!notification.isSeen}">
                            <li class="seen">
                                <a href="MainController?btnAction=View article detail&articleId=${notification.articleId.articleId}&notificationId=${notification.notificationId}">
                                    <div class="notification-box">
                                        <p> <b>${notification.interactUserName}</b> ${notification} <b>${notification.articleTitle}</b></p>
                                        <span><p6>${notification.timeAgo}</p6></span>
                                    </div>
                                </a>
                            </li>
                        </c:if>

                    </c:forEach>
                </c:if>
            </ul>

        </ul>
    </div>


    <script src="js/main.min.js"></script>
    <script src="js/script.js"></script>
    </body>
</html>
