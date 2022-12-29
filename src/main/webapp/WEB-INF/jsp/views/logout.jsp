<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
    body {
    background-image: url("resources/images/grafics/fondoNegroCalavera.jpg");
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center center;
    background-attachment: fixed;
  }
</style>

<sevenislands:layout2 pageName="custom-logout">
    <body> 
        <h1 style="color: white;">
            <strong>
                <center>¿Estás seguro de que deseas cerrar sesión?</center>
            </strong>
        </h1>
        <form:form>
            <center>
                <a class="btn btn-primary" href='<spring:url value="/home" htmlEscape="true"/>'>Cancelar</a>
                <button class="btn btn-primary" type="submit">Cerrar Sesión</button>
            </center>
        </form:form>
    </body>
</sevenislands:layout2>