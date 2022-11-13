<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

<sevenislands:layout2 pageName="lobbyPlayers">
	<body>
		<a class="btn btn-default" href='<spring:url value="/lobby" htmlEscape="true"/>'>Volver</a>
		<h2><br/>Jugadores</h2>
		<div class="container">
			<br />
			<c:if test="${message != null}">
			<div class="alert alert-${messageType}">
				<c:out value="${message}"></c:out>
				<a href="#" class="close" data-dismiss="alert" aria-label="close"></a>
			</div>
			</c:if>
		</div>
		<table class="table table-striped">
			<tr>
				<th>Avatar</th>			
				<th>Nickname</th>
				<th>Expulsar</th>
			</tr>
			<c:forEach items="${players}" var="player">
				<tr>
					<td><img src="/resources/images/avatars/${player.avatar}" height="25" width="25"></td>
					<td><c:out value="${player.nickname}"/></td>		
					<td><a href="/lobby/players/delete/${player.id}"><span class="glyphicon glyphicon-trash alert" aria-hidden="true"></a></td>		
				</tr>
			</c:forEach>
		</table>
	</body>
</sevenislands:layout2>