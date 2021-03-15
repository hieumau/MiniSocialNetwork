<%-- 
    Document   : create_article_page
    Created on : Sep 23, 2020, 11:04:14 PM
    Author     : saost
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <title>Mini Social Network</title>
    <link rel="icon" href="images/fav.png" type="image/png" sizes="16x16">
    <link rel="stylesheet" href="css/main.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/color.css">
    <link rel="stylesheet" href="css/responsive.css">

</head>
    <body>

<%--    import js--%>

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


    <c:set var="message" value="${requestScope.MESSAGE}"></c:set>
    <div id="snackbar">${message}</div>

<%--    toast message end--%>

    <div class="div-middle">
        <div class="central-meta">
            <div class="new-postbox">
                <div class="newpst-input">
                    <form method="post" action="MainController?btnAction=Post article"
                    enctype="multipart/form-data" >
                        <textarea type="text" rows="1" name="title" placeholder="Title" required></textarea>
                        <textarea type="text" rows="4" name="description" placeholder="Description"></textarea>

                        <img  id="imageUpload" -webkit-filter: drop-shadow(5px 5px 5px #222); filter: drop-shadow(5px 5px 5px #222); />

                        <div class="attachments">
                            <ul>
                                <li>
                                    <i class="fa fa-image"></i>
                                    <label class="fileContainer">
                                        <script>
                                            function readImage(input) {
                                            if (input.files && input.files[0]) {
                                                var reader = new FileReader();
                                                reader.onload = function (e) {
                                                    $('#imageUpload')
                                                        .attr('src', e.target.result)
                                                        .width(innerWidth)
                                                        .height(init_autosize());
                                                };

                                                reader.readAsDataURL(input.files[0]);
                                            }
                                        }</script>
                                        <input type="file" name="imageUpload" value=""  onchange="readImage(this) ">
                                    </label>
                                </li>
                                <li>
                                    <button type="submit" >Post</button>
                                </li>
                            </ul>
                        </div>
                    </form>
                </div>
            </div>
        </div><!-- add post new box -->
    </div>

<%--    show toast message--%>
    <c:choose>
        <c:when test="${not empty message}">
            <script>showToastMessage()</script>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>

    </body>
</html>
