<%--
  User: Dmytro Martyshchuk
  Date: 11/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

  <title>Sign Up</title>

</head>
<body class="bg-body-dark ">
<section class="vh-100">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-white" style="border-radius: 2rem">
          <div class="cart-body p-5 text-center">
            <h2 class="fw-bold bb-2 text-uppercase mb-4">Sign Up</h2>
            <p class="mb-5 fst-italic fs-5">Get started with your account</p>

            <form class="needs-validation" action="${pageContext.request.contextPath}/signup" method="post" button-target="sign-up-btn" novalidate>
              <div class="col-12 mb-3">
                <label class="form-label fs-5 fw-semibold" for="typeEmailX" >Email*</label>
                <input type="email" name="email" class="form-control form-control-lg validation" for="sign-up-btn" id="typeEmailX" placeholder="Email">
                <div class="invalid-feedback">You have entered an invalid email!</div>
              </div>
              <div class="col-12 mb-3">
                <label class="form-label fs-5 fw-semibold" for="FirstName">First Name*</label>
                <input type="text" name="first_name" class="form-control form-control-lg validation" for="sign-up-btn" id="FirstName" placeholder="First name">
                <div class="invalid-feedback">First Name must be at least 2 characters</div>
              </div>
              <div class="col-12 mb-3">
                <label class="form-label fs-5 fw-semibold f" for="LastName">Last Name*</label>
                <input type="text" name="last_name" class="form-control form-control-lg validation" for="sign-up-btn" id="LastName" placeholder="Last name">
                <div class="invalid-feedback">Last Name must be at least 2 characters</div>
              </div>
              <div class="col-12 mb-5">
                <label class="form-label fs-5 fw-semibold" for="typePasswordX">Password*</label>
                <div class="input-group">
                  <input type="password" name="password" class="form-control form-control-lg password border-end-0 validation" for="sign-up-btn"  id="typePasswordX" placeholder="Password">
                  <div class="input-group-text password-toggle bg-transparent border-start-0" id="togglePassword" onclick="togglePassword()">
                    <i class="bi bi-eye-fill toggleEye" id="togglePasswordIcon"></i>
                  </div>
                  <div class="invalid-feedback">Password must be at least 8 characters and contain letters, numbers</div>
                </div>


              </div>

              <button class="btn btn-outline-dark btn-lg px-5" id="sign-up-btn" type="submit" disabled>Sign Up</button>
            </form>
          </div>

        </div>
      </div>
    </div>
  </div>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
