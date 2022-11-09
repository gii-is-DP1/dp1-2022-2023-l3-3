<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
  <%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 pageName="home">
  	<h1>7 Islas</h1>

  	<sec:authorize access="hasAuthority('player')">
		<a class="btn btn-default" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Crear Partida</a>
	</sec:authorize>

  	<sec:authorize access="hasAuthority('player')">
		<a class="btn btn-default" href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
	</sec:authorize>

	<sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/controlPanel" htmlEscape="true"/>'>Panel de Control</a>
	</sec:authorize>

	<a class="btn btn-default" href='<spring:url value="/logout" htmlEscape="true"/>'>Cerrar Sesión</a>
</sevenislands:layout2>
