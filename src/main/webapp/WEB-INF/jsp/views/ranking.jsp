<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="ranking">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Ranking</h2>

        <table class="table table-striped">
			<tr>
				<th>Nombre</th>
				<th>Puntuación total</th>
			</tr>
			<c:forEach items="${rankings}" var="ranking">
				<tr>
                    <td>
                        <img src="/resources/images/avatars/${ranking.getFirst().avatar}" height="40" width="40">
                        <c:out value="${ranking.getFirst().nickname}"/>
                    </td>
                    <td><c:out value="${ranking.getSecond()}"/></td>
                </tr>
			</c:forEach>
		</table>
    </body>
</sevenislands:layout3>