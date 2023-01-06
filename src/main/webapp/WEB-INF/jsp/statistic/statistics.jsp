<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="statistics">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Estadísticas Globales</h2>

        <table class="table table-striped">
			<tr>
                <th>Nombre</th>
				<th>Total</th>
				<th>Promedio</th>
				<th>Máximo</th>
                <th>Mínimo</th>
			</tr>
			<tr>
                <th>Nº de Partidas Jugadas</th>
                <td><c:out value="${total_games}"/></td>
                <td><c:out value="${average_games}"/></td>
                <td><c:out value="${max_games}"/></td>
                <td><c:out value="${min_games}"/></td>
            </tr>
            <tr>
                <th>Tiempo Total Jugado</th>
                <td><c:out value="${total_time}"/> min</td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
            <tr>
                <th>Puntos Totales</th>
                <td><c:out value="${total_points}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${max_points}"/></td>
                <td><c:out value="${min_points}"/></td>
            </tr>
            <tr>
                <th>Turnos Totales</th>
                <td><c:out value="${total_turns}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
		</table>
    </body>
</sevenislands:layout3>