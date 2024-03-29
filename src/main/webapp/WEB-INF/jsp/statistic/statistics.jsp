<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="statistics">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <a class="btn btn-default" href='<spring:url value="/dailyStatistics" htmlEscape="true"/>'>Estadísticas Globales Diarias</a>
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
                <td><c:out value="${max_games}"/>/dia</td>
                <td><c:out value="${min_games}"/>/dia</td>
            </tr>
            <tr>
                <th>Duración de las Partidas</th>
                <td><c:out value="${total_time}"/> min</td>
                <td><c:out value="${average_time}"/> min</td>
                <td><c:out value="${max_time}"/> min</td>
                <td><c:out value="${min_time}"/> min</td>
            </tr>
            <tr>
                <th>Nº de Jugadores por Partida</th>
                <td><c:out value="${total_players}"/></td>
                <td><c:out value="${average_players}"/></td>
                <td><c:out value="${max_players}"/></td>
                <td><c:out value="${min_players}"/></td>
            </tr>
            <tr>
                <th>Puntos Totales</th>
                <td><c:out value="${total_points}"/> pts</td>
                <td><c:out value="${average_points}"/> pts</td>
                <td><c:out value="${max_points}"/> pts</td>
                <td><c:out value="${min_points}"/> pts</td>
            </tr>
            <tr>
                <th>Turnos Totales</th>
                <td><c:out value="${total_turns}"/></td>
                <td><c:out value="${average_turns}"/></td>
                <td><c:out value="${max_turns}"/></td>
                <td><c:out value="${min_turns}"/></td>
            </tr>
		</table>
    </body>
</sevenislands:layout3>