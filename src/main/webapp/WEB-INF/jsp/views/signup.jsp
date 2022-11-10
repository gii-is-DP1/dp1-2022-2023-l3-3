<%@ page session="false" trimDirectiveWhitespaces="true" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix="sevenislands" tagdir="/WEB-INF/tags" %>

<style>
  body {
    background-image: url("resources/images/piratasEnIsla.jpg");
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center center;
    background-attachment: fixed;
  }
  @media (max-width : 1500px) {
    body {
      background-size: auto 100%;
    }
}
</style>

<sevenislands:layout2 pageName="signup">
  <jsp:attribute name="customScript">
    <script>
      $(function () {
        $("#birthDate").datepicker({ dateFormat: "yy/mm/dd" });
      });
    </script>
  </jsp:attribute>
  <jsp:body>
    <h2>Sign Up</h2>
    <form:form
      modelAttribute="player"
      class="form-horizontal"
      id="add-player-form"
    >
      <div class="form-group has-feedback">
        <sevenislands:inputField label="Nickname" name="nickname" required="required"/>
        <sevenislands:inputField
          label="Password"
          type="password"
          name="password"
          required="required"
        />
        <sevenislands:inputField label="First Name" name="firstName" required="required"/>
        <sevenislands:inputField label="Last Name" name="lastName" required="required"/>
        <sevenislands:inputField label="Birth Date" name="birthDate" required="required"/>
        <sevenislands:inputField label="Email" name="email" required="required"/>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button class="btn btn-default" type="submit">Sign up</button>
        </div>
      </div>
    </form:form>
  </jsp:body>
</sevenislands:layout2>
