<%@ page session="false" trimDirectiveWhitespaces="true" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix="sevenislands" tagdir="/WEB-INF/tags" %>

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
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button class="btn btn-default" type="submit">Sign up</button>
        </div>
      </div>
    </form:form>
  </jsp:body>
</sevenislands:layout2>
