
    Document   : creat_user_account
    Created on : Jul 9, 2020, 10:51:47 AM
    Author     : saost
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="main.js"></script>
    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
            background-color: black;
        }

        * {
            box-sizing: border-box;
        }

        /* Add padding to containers */
        .container {
            padding: 16px;
            background-color: white;
        }

        /* Full-width input fields */
        input[type=text], input[type=password], select, input[type=number] {
            width: 100%;
            padding: 15px;
            margin: 5px 0 22px 0;
            display: inline-block;
            border: none;
            background: #f1f1f1;
        }

        input[type=text]:focus, input[type=password]:focus, select, input[type=number] {
            background-color: #ddd;
            outline: none;
        }

        /* Overwrite default styles of hr */
        hr {
            border: 1px solid #f1f1f1;
            margin-bottom: 25px;
        }

        /* Set a style for the submit button */
        .registerbtn {
            background-color: #4CAF50;
            color: white;
            padding: 16px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
            opacity: 0.9;
        }

        .registerbtn:hover {
            opacity: 1;
        }

        /* Add a blue text color to links */
        a {
            color: dodgerblue;
        }

        /* Set a grey background color and center the text of the "sign in" section */
        .signin {
            background-color: #f1f1f1;
            text-align: center;
        }
    </style>
    <title>User Register</title>
</head>
<body>

<form action="MainController">
    <div class="container">
        <h1>User Register</h1>
        <p>Please fill in this form to create an account.</p>
        <c:set var="userError" value="${requestScope.USER_ERROR}"></c:set>

        <hr>
        <label for="id"><b>Email</b></label>
        <input type="email" placeholder="Enter your email" name="id"
               value="${param.get("id")}" required>
        <p>${userError.userIdError}</p><br>


        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" id="psw" required>
        <p>${userError.passwordError}</p><br>

        <label for="passwordRepeat"><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="passwordRepeat" id="psw-repeat" required>
        <p>${userError.passwordRepeatError}</p><br>

        <label for="fullName"><b>Full Name</b></label>
        <input type="text" placeholder="Full Name" name="fullName"
               value="${param.get("fullName")}" required>
        <p>${userError.fullNameError}</p><br>
        <hr>


        <button type="submit" name="btnAction" value="Create user account" class="registerbtn">Register</button>
    </div>

    <div class="container signin">
        <p>Already have an account? <a href="login.jsp">Sign in</a>.</p>
    </div>
</form>

</body>
</html>
