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
	<h2>Users:</h2>
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
			<th>nickname</th>
            <th>first_name</th>
            <th>last_name</th>
            <th>email</th>
            <th>creation_date</th>
            <th>type</th>
            <th>birth_date</th>
            <th>avatar</th>
		</tr>
		<c:forEach items="${users}" var="user">
			<tr>
				<td><c:out value="${user.nickname}"/></td>
				<td><c:out value="${user.firstName}"/></td>
                <td><c:out value="${user.lastName}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td><c:out value="${user.creationDate}"/></td>
                <td><c:out value="${user.userType}"/></td>
                <td><c:out value="${user.birthDate}"/></td>
                <td><c:out value="${user.avatar}"/></td>
                <td><a href="/controlPanel/delete/${user.id}"><span class="glyphicon glyphicon-trash alert" aria-hidden="true"></a></td>	
			</tr>
		</c:forEach>
	</table>
</body>