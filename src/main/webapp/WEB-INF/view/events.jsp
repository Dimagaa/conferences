<%--
  User: Dmytro Martyshchuk
  Date: 08/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <title>Profile</title>
</head>
<body class="bg-body-dark">

<!-- Navbar -->
<c:import url="modules/navbar.jsp"/>

<div class="container">

  <!-- Search -->
  <div class="row justify-content-center">
    <div class="col mt-4 mb-4">
      <form class="form-control">
        <input class="form-control" type="text" placeholder="Search" aria-label="Search">
      </form>
    </div>
  </div>

  <!-- page content -->

  <div class="row bg-dark vh-100" style="border-radius: 2rem">
    <div class="col">


      <!-- page header -->

      <c:choose>
        <c:when test="${sessionScope.user.role == 'USER'}">
          <jsp:include page="modules/user/user-pageheader.jsp"/>
        </c:when>
        <c:when test="${sessionScope.user.role == 'SPEAKER'}">
          <jsp:include page="modules/speaker/speaker-pageheader.jsp"/>
        </c:when>
        <c:when test="${sessionScope.user.role == 'MODERATOR'}">
          <jsp:include page="modules/moderator/moderator-pageheader.jsp"/>
        </c:when>
      </c:choose>

      <div class="row mt-5">

        <!-- filter panel -->
        <div class="col-3 d-flex justify-content-center bg-dark bg-gradient">
          <div class="text-white">
            <div class="text-center text-uppercase fw-bolder mt-3 fs-5">filters</div>

            <div class="mt-3 fs-6">
              <div class="text-uppercase">Status</div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="isNewChecked">
                <label class="form-check-label" for="isNewChecked">New</label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="isExpiredChecked">
                <label class="form-check-label" for="isExpiredChecked">Expired</label>
              </div>
            </div>
            <div class="mt-3 fs-6">
              <label class="form-label text-uppercase" for="filterBySpeaker">Speaker</label>
              <input class="form-control" list="listSpeakers" id="filterBySpeaker" placeholder="Type to search...">
              <datalist id="listSpeakers">
                <option value="Dimaga Bumaga">
              </datalist>
            </div>
            <div class="mt-3 mb-4 fs-6">
              <label class="form-label text-uppercase" for="filterByPlace">Place</label>
              <input class="form-control" list="listPlaces" id="filterByPlace" placeholder="Type to search...">
              <datalist id="listPlaces">
                <option value="Odesa">
              </datalist>
            </div>
          </div>
        </div>

        <div class="col">
          <!-- Insert events here-->
          <div class="row mb-4">
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row mb-4">
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row mb-4">
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row mb-4">
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card">
                <h5 class="card-header text-center fw-bold border-0 text-black">Java frameworks</h5>
                <div class="card-body">

                  <div class="event-scroll mb-4 " style="height: 180px">
                    <div class="list-group">

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">Spring Boot Performance</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>

                      <div class="list-group-item border-0">
                        <div class="d-flex w-100 justify-content-between">
                          <h6 class="mb-0">ORM Frameworks: Hibernate - how to use?</h6>
                        </div>
                        <p class="mb-2 text-black-50 fst-italic">Dmytro Martyshchuk</p>
                      </div>
                    </div>
                  </div>

                  <div class="border-top fst-italic">
                    <div class="row text-center mt-2">
                      <div class="col">
                        <p> Start at 23.06.2022 14:00</p>
                      </div>
                      <div class="col text-center">
                        <p class="text-black-50">2 h</p>
                      </div>
                    </div>
                    <div class="row col-3 text-center">
                      <p>Odessa</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

</body>
</html>