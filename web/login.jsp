<%--    Document   : login
    Created on : Jul 6, 2020, 11:14:17 AM
    Author     : saost
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <!--ten bat dau bang chu thuong-->--%>
<%--    <title>Welcome to Mini Social Network</title>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <meta name="viewport" content="width=device-width, initial-scale=1.0">--%>
<%--</head>--%>
<%--<body>--%>
<%--<form action="MainController" method="POST">--%>
<%--    <c:set var="errorMessage" value="${sessionScope.ERROR_MESSAGE}"></c:set>--%>
<%--    <p>${errorMessage}</p>--%>

<%--&lt;%&ndash;    <c:set target="errorMessage" value=""></c:set>&ndash;%&gt;--%>
<%--    Username<input type="email" name="userID" required/></br>--%>
<%--    Password<input type="password" name="password" required/></br>--%>
<%--    <input type="submit" value="Login" name="btnAction"/>--%>
<%--    <input type="reset" value="Reset"/>--%>
<%--</form>--%>
<%--<button onclick="window.location.href='creat_user_account.jsp'">Register</button>--%>


<%--</body>--%>
<%--</html>--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <title>Winku Social Network Toolkit</title>
    <link rel="icon" href="images/fav.png" type="image/png" sizes="16x16">

    <link rel="stylesheet" href="<c:url value="css/main.min.css"/> ">
    <link rel="stylesheet" href="<c:url value="css/style.css"/> ">
    <link rel="stylesheet" href="<c:url value="css/color.css"/> ">
    <link rel="stylesheet" href="<c:url value="css/responsive.css"/> ">

</head>
<body>
<!--<div class="se-pre-con"></div>-->
<div class="theme-layout">
    <div class="">
        <div class="container-fluid pdng0">
            <div class="row merged">
                <div class="login-reg-bg">
                    <c:if test="${not empty sessionScope.ERROR_MESSAGE}">
                    </c:if>
                    <div class="log-reg-area sign">
                        <h2 class="log-title">Login</h2>
                        <c:set var="errorMessage" value="${sessionScope.ERROR_MESSAGE}"></c:set>
                        <p><b>${errorMessage}</b></p>
                        <form action="MainController" method="POST">
                            <div class="form-group">
                                <input type="email" name="userID" id="input" required="required"/>
                                <label class="control-label" for="input">Username</label><i class="mtrl-select"></i>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" required="required"/>
                                <label class="control-label" for="input">Password</label><i class="mtrl-select"></i>
                            </div>

                            <div class="submit-btns">
                                <button class="mtr-btn signin" type="submit" value="Login" name="btnAction"><span>Login</span></button>
                                <button class="mtr-btn signup" type="button"><span>Register</span></button>
                            </div>
                        </form>
                    </div>
                    <div class="log-reg-area reg">
                        <h2 class="log-title">Register</h2>
                        <p>
                            Don’t use Winku Yet? <a href="#" title="">Take the tour</a> or <a href="#" title="">Join now</a>
                        </p>
                        <c:set var="userError" value="${requestScope.USER_ERROR}"></c:set>
                        <form action="MainController">
                            <div class="form-group">
                                <input type="text" name="fullName" value="${param.get("fullName")}" required="required"/><span>${userError.fullNameError}</span>
                                <label class="control-label"  for="input">First & Last Name</label><i class="mtrl-select"></i>
                            </div>
                            <div class="form-group">
                                <input type="email" name="id" value="${param.get("id")}" required="required"/>
                                <label class="control-label" for="input">Email</label><i class="mtrl-select"></i><span>${userError.userIdError}</span>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" required="required"/>
                                <label class="control-label" for="input">Password</label><i class="mtrl-select"></i><span>${userError.passwordError}</span>
                            </div>

                            <div class="form-group">
                                <input type="password" name="passwordRepeat" required="required"/>
                                <label class="control-label" for="input">Repeat password</label><i class="mtrl-select"></i><span>${userError.passwordRepeatError}</span>
                            </div>

                            <a href="#" title="" class="already-have">Already have an account</a>
                            <div class="submit-btns">

                                <button class="mtr-btn signin" value="Create user account" type="submit" name="btnAction"><span>Register</span></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script data-cfasync="false" src="../../cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script src="js/main.min.js"></script>
<script src="js/script.js"></script>

</body>

</html>
