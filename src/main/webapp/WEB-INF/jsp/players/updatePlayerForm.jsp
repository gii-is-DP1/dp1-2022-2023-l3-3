<%@ page session="false" trimDirectiveWhitespaces="true" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix="sevenislands" tagdir="/WEB-INF/tags" %>

<style>
  body {
    background-image: url("resources/images/grafics/piratasEnIsla.jpg");
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center center;
    background-attachment: fixed;
  }
  @media (max-width: 1500px) {
    body {
      background-size: auto 100%;
    }
  }
</style>

<sevenislands:layout2 pageName="settings">
  <jsp:attribute name="customScript">
    <script>
      $(function () {
        $("#birthDate").datepicker({ dateFormat: "yy/mm/dd" });
      });
    </script>
  </jsp:attribute>
  <jsp:body>
    <h2>Edit Profile</h2>
    <form:form
      modelAttribute="user"
      class="form-horizontal"
      id="edit-player-form"
    >
      <input type="hidden" name="avatar" value="${player.avatar}" />
      <div class="form-group has-feedback">
        <sevenislands:inputField label="Nickname" name="nickname" />
        <sevenislands:inputField
          label="Password"
          type="password"
          name="password"
        />
        <sevenislands:inputField label="First Name" name="firstName" />
        <sevenislands:inputField label="Last Name" name="lastName" />
        <sevenislands:inputField label="Birth Date" name="birthDate" />
        <sevenislands:inputField label="Email" name="email" />
      </div>

      <div class="pull-right">
        <a
          class="btn btn-default"
          href='<spring:url value="/" htmlEscape="true"/>'
        >
          Volver
        </a>
        <button class="btn btn-default mr-3" type="submit">Update</button>
      </div>
    </form:form>
  </jsp:body>
</sevenislands:layout2>
