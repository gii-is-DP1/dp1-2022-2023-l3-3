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
				<a class="btn btn-default" href='<spring:url value="/controlPanel/add" htmlEscape="true"/>'>Añadir Usuario</a>
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
				<th>Avatar</th>
				<th>Nickname</th>
				<th>Nombre</th>
				<th>Apellidos</th>
				<th>Email</th>
				<th>Fecha de Creación</th>
				<th>Tipo</th>
				<th>Fecha de Nacimiento</th>
				<th>Activo</th>
				<th>Eliminar</th>
			</tr>
			<c:forEach items="${users}" var="user">
				<tr>
					<td><a href="/controlPanel/edit/${user.id}"><img src="/resources/images/icons/editIcon.png" height="30" width="30"></a></td>
					<td><img src="/resources/images/avatars/${user.avatar}" height="25" width="25"></td>
					<td><c:out value="${user.nickname}"/></td>
					<td><c:out value="${user.firstName}"/></td>
					<td><c:out value="${user.lastName}"/></td>
					<td><c:out value="${user.email}"/></td>
					<td><c:out value="${user.creationDate}"/></td>
					<td><c:out value="${user.userType}"/></td>
					<td><c:out value="${user.birthDate}"/></td>
					<td><a href="/controlPanel/enable/${user.id}"><img src="/resources/images/icons/${user.enabled}.png" height="25" width="25"></a></td>
					<td><a href="/controlPanel/delete/${user.id}"><img src="/resources/images/icons/deleteIcon.png" height="30" width="30"></a></td>	
				</tr>
			</c:forEach>
		</table>
		<nav aria-label="Page navigation example">
			<ul class="pagination">
				<c:if test="${valores-1>=0}">
				<a class="btn btn-default" href="/controlPanel?valor=${valores - 1}"  >ANTERIOR</a>
			</c:if><c:if test="${valores+1<=paginas}">
			  	<a class="btn btn-default" href="/controlPanel?valor=${valores + 1}" >SIGUIENTE</a>
			</c:if>
			</ul>
		</nav>
	</body>
</sevenislands:layout2>