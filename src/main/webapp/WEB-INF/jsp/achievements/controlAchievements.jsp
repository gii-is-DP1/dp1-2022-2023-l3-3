<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

<style>
	.controlPanel-menu {
		width: 100%;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}
</style>

<sevenislands:layout2 pageName="controlPanel">
	<body>
		<div class="controlPanel-menu">
			<div>
				<a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
			</div>
			<div>
				<a class="btn btn-default" href='<spring:url value="/controlAchievements/add" htmlEscape="true"/>'>Añadir logro</a>
			</div>
		</div>
		<h2><br/>Usuarios</h2>
		<div class="container">
			<br/>
			<c:if test="${message != null}">
			<div class="alert alert-${messageType}">
				<c:out value="${message}"></c:out>
				<a href="#" class="close" data-dismiss="alert" aria-label="close"></a>
			</div>
			</c:if>
		</div>
		<table class="table table-striped">
			<tr>
				<th>Editar</th>
				<th>Logro</th>
				<th>Description</th>
                <th>Tipo</th>
                <th>Name</th>
				
			</tr>
			<c:forEach items="${logros}" var="logro">
				<tr>
					<td><a href="/controlAchievements/edit/${logro.getFirst().id}"><img src="/resources/images/icons/editIcon.png" height="30" width="30"></a></td>
					<td><img src="/resources/images/grafics/${logro.getFirst().badgeImage}" height="25" width="25"></td>
					<td><c:out value="${logro.getFirst().description.replaceAll('LIMIT', logro.getFirst().getThreshold())}"/></td>	
					<td><c:out value="${logro.getFirst().achievementType}"/></td>
                    <td><c:out value="${logro.getFirst().name}"/></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</sevenislands:layout2>