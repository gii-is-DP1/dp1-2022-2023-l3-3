<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="myAchievements">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Mis Logros</h2>
        <h3>Nombre de Usuario: <c:out value="${user.nickname}"/></h3>

        <table class="table table-striped">
			<tr>
				<th>Nombre</th>
				<th>Fecha de obtención</th>
				<th>Descripción</th>
			</tr>
			<c:forEach items="${achievements}" var="achievement">
				<tr>
                    <td>
                        <img src="/resources/images/grafics/${achievement.getFirst().badgeImage}" height="40" width="40">
                        <c:out value="${achievement.getFirst().name}"/>
                    </td>
					<td><c:out value="${achievement.getSecond()}"/></td>
					<td><c:out value="${achievement.getFirst().description.replaceAll('LIMIT', achievement.getFirst().getThreshold())}"/></td>	
				</tr>
			</c:forEach>
		</table>
    </body>
</sevenislands:layout3>