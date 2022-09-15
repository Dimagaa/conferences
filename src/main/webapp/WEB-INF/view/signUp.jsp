<%--
  User: Dmytro Martyshchuk
  Date: 11/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sig Up</title>
</head>
<body>
  <div class="form">
    <h1> Sign Up</h1>
    <form method="post" action="">

      <label>
        <input type="text" required placeholder="First name" name="firstName">
      </label><br>
      <label>
        <input type="text" required placeholder="Last name" name="lastName">
      </label><br>
      <label>
        <input type="email" required placeholder="E-mail" name="email">
      </label><br>
      <label>
        <input type="password" required placeholder="Password" name="password">
        <input type="password" required placeholder="Confirm password" name="confirmPassword">
      </label><br><br>
      <input class="button" type="submit" value="Submit">

    </form>
  </div>
</body>
</html>
