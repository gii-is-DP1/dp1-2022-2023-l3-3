<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
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

<sevenislands:layout2 pageName="gamesAsPlayer">
	<body>
		<div class="controlPanel-menu">
			<div>
				<a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
			</div>
		</div>
			<h2><br/>Partidas jugadas</h2>
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
				<th>Id</th>
				<th>Fecha de creacion</th>
				<th>Fecha de finalizacion</th>
				<th>Creador</th>
				<th>Ganador</th>
			</tr>
			<c:forEach items="${games}" var="game">
				<tr>
					<td><c:out value="${game.getFirst().id}"/></td>
					<td><c:out value="${game.getFirst().creationDate}"/></td>
					<td><c:out value="${game.getFirst().endingDate}"/></td>	
					<td><c:out value="${game.getSecond().nickname}"></c:out></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</sevenislands:layout2>