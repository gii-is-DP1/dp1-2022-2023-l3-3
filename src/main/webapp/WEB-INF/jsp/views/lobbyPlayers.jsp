<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

<body>
	<h2>PLayers:</h2>
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
			<th>player</th>			
			<th>nickname</th>
		</tr>
		<c:forEach items="${players}" var="player">
			<tr>
				<td><c:out value="${player.id}"/></td>
				<td><c:out value="${player.nickname}"/></td>		
				<td><a href="/lobby/players/delete/${player.id}"><span class="glyphicon glyphicon-trash alert" aria-hidden="true"></a> </td>		
				</tr>
		</c:forEach>
	</table>
</body>