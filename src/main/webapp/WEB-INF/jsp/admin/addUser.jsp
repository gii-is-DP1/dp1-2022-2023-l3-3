<%@ page session="false" trimDirectiveWhitespaces="true" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
    #for {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  #error {
    color: black;
    text-align: center;
    background-color: rgb(255, 50, 50);
    border: 2px solid black;
    font-weight: 700;
    border-radius: 50px;
    margin: 3px;
    width: 50%;
  }
</style>


<sevenislands:layout2 pageName="addUser">
  <jsp:attribute name="customScript">
    <script>
      $(function () {
        $("#birthDate").datepicker({ dateFormat: "yy/mm/dd" });
      });
    </script>
  </jsp:attribute>
  <jsp:body>
    <a class="btn btn-default" href='<spring:url value="/controlPanel" htmlEscape="true"/>'>Volver</a>
    <h2><br/>Añadir Usuario</h2>
    <form:form
      modelAttribute="user"
      class="form-horizontal"
      id="add-user-form"
    >
      <div class="form-group has-feedback">
        <sevenislands:inputField label="Nombre de usuario" name="nickname" required="required"/>
        <sevenislands:inputField
          label="Contraseña"
          type="password"
          name="password"
          required="required"
        />
        <sevenislands:inputField label="Nombre" name="firstName" required="required"/>
        <sevenislands:inputField label="Apellidos" name="lastName" required="required"/>
        <sevenislands:inputField label="Fecha de nacimiento" name="birthDate" required="required"/>
        <sevenislands:inputField label="Email" name="email" required="required"/>
        <sevenislands:selectField label="Tipo" name="userType" names="${types}" required="required"/>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button class="btn btn-default" type="submit">Añadir</button>
        </div>
      </div>
    </form:form>
    <div id="for" >
      <c:forEach items="${errors}" var="error">
        <p id="error">${error}</p>
      </c:forEach>
    </div>
  </jsp:body>
</sevenislands:layout2>