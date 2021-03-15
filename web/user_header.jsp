<%-- 
    Document   : user_hearder
    Created on : Jul 11, 2020, 10:11:04 PM
    Author     : saost
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <title>Winku Social Network Toolkit</title>
    <link rel="icon" href="images/fav.png" type="image/png" sizes="16x16">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <link rel="stylesheet" href="css/main.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/color.css">
    <link rel="stylesheet" href="css/responsive.css">

</head>
<c:set var="authUser" value="${sessionScope.AUTH_USER}"></c:set>
<body>
<%--    </div><!-- responsive header -->--%>
    <div class="theme-layout">
        <div class="topbar stick">
            <div class="logo">
                <a title="" href="MainController?btnAction=View home page"><img src="images/logo.png" alt=""></a>
            </div>

            <div class="top-area">
                <ul class="setting-area">
                    <li>
                        <a href="#" title="Home" data-ripple=""><i class="ti-search"></i></a>
                        <div class="searched">
                            <form method="post" id="search_form" action="MainController?btnAction=View home page" class="form-search">
                                    <input type="text" name="searchKeyword" placeholder="Search article"
                                    <c:if test="${not empty requestScope.SEARCH_KEYWORD}">
                                           value="${requestScope.SEARCH_KEYWORD}"
                                    </c:if>
                                    >
                                <button type="submit" onclick="submitForm()"><i class="ti-search"></i></button>
                                <script>
                                    function submitForm() {
                                        document.getElementById("search_form").submit();
                                    }
                                </script>
                            </form>
                        </div>
                    </li>

                    <li><a onclick="window.location.href='MainController?btnAction=View home page'"><i class="ti-home"></i></a></li>
                    <li>
                        <a onclick="window.location.href='MainController?btnAction=View notification'" title="Notification" data-ripple="">
                            <i class="ti-bell"></i><span>${requestScope.NUMBER_OF_UNSEEN_NOTIFICATION}</span>
                        </a>
                    </li>

                </ul>
                <div class="user-img">
<%--                    <img src="images/resources/admin.jpg" alt="">--%>
                    <p>${sessionScope.AUTH_USER.fullName}</p>
                    <span class="status f-online"></span>
                    <div class="user-setting">
                        <a onclick="window.location.href='MainController?btnAction=Logout'" title=""><i class="ti-power-off"></i>log out</a>
                    </div>
                </div>
            </div>
        </div><!-- topbar -->

    </div>


    <script data-cfasync="false" src="../../cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script src="js/main.min.js"></script>
    <script src="js/script.js"></script>
<%--    <div class="topbar stick ">--%>
<%--        <div class="logo">--%>
<%--            <a title="" href="MainController?btnAction=View home page"><img src="images/logo.png" alt=""></a>--%>
<%--        </div>--%>

<%--        <div class="top-area">--%>

<%--            <ul >--%>
<%--                <li>--%>
<%--                    <a href="#" title="Home" data-ripple=""><i class="ti-search"></i><span class="ripple"><span class="ink" style="height: 20px; width: 20px; background-color: rgb(217, 217, 217); top: -0.366907px; left: -5.83453px;"></span></span></a>--%>
<%--                    <div class="searched active">--%>
<%--                        <form method="post" class="form-search">--%>
<%--                            <input type="text" placeholder="Search Friend">--%>
<%--                            <button data-ripple=""><i class="ti-search"></i><span class="ripple"><span class="ink" style="height: 33px; width: 33px; background-color: rgb(217, 217, 217); top: -5.98561px; left: -3.35254px;"></span></span></button>--%>
<%--                        </form>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <li>--%>
<%--                    <i class="ti-clipboard"></i>--%>
<%--                    <a href="MainController?btnAction=View home page" title="">News feed</a>--%>
<%--                </li>--%>

<%--                <li>--%>
<%--                    <a href="MainController?btnAction=View notification" title=""><i class="ti-bell"></i>--%>
<%--                        <span>${requestScope.NUMBER_OF_UNSEEN_NOTIFICATION}</span></a>--%>
<%--                        <a href="MainController?btnAction=View notification" title="">Notifications</a>--%>
<%--                </li>--%>

<%--                <li>--%>
<%--                    <a href="#" title="Search article" data-ripple=""><i class="ti-search"></i></a>--%>
<%--                    <div class="searched">--%>
<%--                        <form method="post" class="form-search">--%>
<%--                            <input type="text" placeholder="Search article">--%>
<%--                            <button data-ripple><i class="ti-search"></i></button>--%>
<%--                        </form>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <li>--%>
<%--                    <b>${authUser.fullName}</b>--%>
<%--                </li>--%>
<%--            </ul>--%>

<%--            <ul>--%>

<%--            </ul>--%>

<%--            <div class="user-img">--%>
<%--                <img src="images/resources/admin.jpg" alt="">--%>
<%--                <span class="status f-online"></span>--%>
<%--                <div class="user-setting">--%>
<%--                    <a onclick="window.location.href='MainController?btnAction=Logout'" title=""><i class="ti-power-off"></i>log out</a>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div><!-- topbar -->--%>

<%--    <script data-cfasync="false" src="../../cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script src="js/main.min.js"></script>--%>
<%--    <script src="js/script.js"></script>--%>
</body>


</html>
