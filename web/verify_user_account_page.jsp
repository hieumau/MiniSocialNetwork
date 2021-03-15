<%-- 
    Document   : verify_user_account_page
    Created on : Sep 23, 2020, 9:46:35 PM
    Author     : saost
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify email page</title>
    </head>
    <body>

    <script>
        const url = new URL(location);

        if (url.searchParams.get('btnAction') != null){
            url.searchParams.set('btnAction', 'View verify page')
        }
        history.replaceState(null, null, url)
    </script>

    <%@include file="user_header.jsp"%>
    <div class="col-lg-12">
        <div class="contct-info">
            <div class="contact-form">
                <div class="cnt-title">
                    <span>Verify Email</span>
                </div>
                <div class="cnt-title">
                    <a href="MainController?btnAction=Resend verify code"><i><span>Resend code  </span><img src="images/envelop.png" alt=""></i></a>
                </div>
                <div>${requestScope.MESSAGE}</div>
                <form method="post" action="MainController">

                    <div class="form-group">
                        <input type="number" name="verifyCode" required="required"/>
                        <label class="control-label" for="input">6-Digits code</label><i class="mtrl-select"></i>
                    </div>

                    <div class="submit-btns">

                            <button class="mtr-btn signin" type="submit" value="Check verify code" name="btnAction"><span>Verify</span></button>
                    </div>
                </form>
            </div>
            <div class="cntct-adres">
                <h3>Your Email</h3>
                <ul>
                    <li>
                        <i class="fa fa-envelope-o"></i>
                        <span><a href="https://wpkixx.com/cdn-cgi/l/email-protection" class="__cf_email__" data-cfemail="cba2a5ada48bb2a4beb9a6aaa2a7e5a8a4a6">${authUser.userId}</a></span>
                    </li>
                </ul>

            </div>
        </div>
    </div>

    </body>
</html>
