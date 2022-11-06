<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<sevenislands:layout2>
    <h2><fmt:message key="welcome"/></h2>

    <a class="btn btn-default" href='<spring:url value="/login" htmlEscape="true"/>'>Iniciar sesión</a>
    <a class="btn btn-default" href='<spring:url value="/signup" htmlEscape="true"/>'>Registrarse</a>
</sevenislands:layout2>
