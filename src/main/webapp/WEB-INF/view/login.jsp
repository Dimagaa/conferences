<%--
  User: Dmytro Martyshchuk
  Date: 11/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<section class="vh-100">
    <div class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card bg-dark bg-gradient text-white shadow-lg" style="border-radius: 2rem">
                    <div class="cart-body p-5 text-center">
                        <h2 class="fw-bold bb-2 text-uppercase">Login</h2>
                        <p class="text-white-50 mb-5 fs-5">Please enter your login and password!</p>
                        <form class="needs-validation log" button-target="login-btn" novalidate>
                            <div class="input-group col-12 mb-4">
                                <input type="email" name="login" class="form-control form-control-lg validation" for="login-btn"
                                       id="typeEmailX" placeholder="Email">
                                <label class="visually-hidden" for="typeEmailX">Email address</label>
                                <div class="invalid-feedback fs-5">You have entered an invalid email</div>
                            </div>

                            <div class="col-12 mb-4">
                                <input type="password" name="password" class="form-control form-control-lg light-validation validation"
                                       for="login-btn" id="typePasswordX" placeholder="Password">
                                <label class="visually-hidden" for="typePasswordX">Password</label>
                                <div class="invalid-feedback fs-5">This field cannot be empty</div>
                            </div>

                            <p class="small pb-lg-2"><a class="text-white-50 fs-6" href="#">Forgot
                                password?</a></p>
                            <div class="d-none"  id="loginError">
                                <div class=" d-flex alert fs-5 border-1 border-danger align-items-center">
                                    <span class="bi bi-exclamation-circle text-danger fs-3"></span>
                                    <span>
                                        The email address or password is incorrect. Please retry...
                                    </span>
                                </div>
                            </div>
                            <button type="button" class="btn btn-outline-light btn-lg px-5 mt-5" id="login-btn" onclick="doLogin()" disabled>
                                Login
                            </button>
                        </form>
                    </div>
                    <div class="text-white text-center mb-5">
                        <p class="mb-0 fs-6">Don't have an account? <a href="${pageContext.request.contextPath}/signup" class="text-white-50 fw-bold">Sign
                            Up</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>