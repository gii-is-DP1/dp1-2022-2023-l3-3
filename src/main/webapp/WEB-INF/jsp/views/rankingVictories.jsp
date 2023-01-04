<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 pageName="rankingVictories">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Ranking por victorias</h2>

        <table class="table table-striped">
			<tr>   
                <th>Posición</th>
				<th>Nombre</th>
				<th>Número de victorias</th>
			</tr>
			<c:forEach items="${rankings}" var="ranking">
				<tr>
                    <td>${rankings.indexOf(ranking)+1}</td>
                    <td>
                        <img src="/resources/images/avatars/${ranking.getFirst().avatar}" height="40" width="40">
                        <c:out value="${ranking.getFirst().nickname}"/>
                    </td>
                    <td><c:out value="${ranking.getSecond()}"/></td>
                </tr>
			</c:forEach>
		</table>
    </body>
</sevenislands:layout2>