<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout pageName="login">
    <h1>Iniciar sesión</h1>

    <sec:authorize access="hasAuthority('player')">
		<a class="btn btn-default" href='<spring:url value="/lobby" htmlEscape="true"/>'>Crear Partida</a>
	</sec:authorize>
</sevenislands:layout>