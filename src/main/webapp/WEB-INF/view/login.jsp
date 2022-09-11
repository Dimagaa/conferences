<%--
  User: Dmytro Martyshchuk
  Date: 10/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <div class="form">

        <h1> Login </h1>
        <form method="post" action="">

            <label>
                <input type="text" required placeholder="login" name="login">
            </label><br>
            <label>
                <input type="password" required placeholder="password" name="password">
            </label><br><br>
            <input class="button" type="submit" value="Enter">

        </form>
    </div>
</body>
</html>
