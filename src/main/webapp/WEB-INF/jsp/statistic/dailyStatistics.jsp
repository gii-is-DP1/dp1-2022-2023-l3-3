<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="statistics">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/statistics" htmlEscape="true"/>'>Volver</a>
        <h2><br>Estadísticas Globales Diarias</h2>

        <table class="table table-striped">
			<tr>
                <th>Nombre</th>
				<th>Total</th>
				<th>Promedio Diario</th>
				<th>Máximo Diario</th>
                <th>Mínimo Diario</th>
			</tr>
			<tr>
                <th>Nº de Partidas Jugadas</th>
                <td><c:out value="${total_games}"/></td>
                <td><c:out value="${average_games}"/></td>
                <td><c:out value="${max_games}"/></td>
                <td><c:out value="${min_games}"/></td>
            </tr>
            <tr>
                <th>Duración de las Partidas</th>
                <td><c:out value="${total_time}"/> min</td>
                <td><c:out value="${average_time_day}"/> min</td>
                <td><c:out value="${max_time_day}"/> min</td>
                <td><c:out value="${min_time_day}"/> min</td>
            </tr>
            <tr>
                <th>Nº de Jugadores Diario</th>
                <td><c:out value="${total_players}"/></td>
                <td><c:out value="${average_players_day}"/></td>
                <td><c:out value="${max_players_day}"/></td>
                <td><c:out value="${min_players_day}"/></td>
            </tr>
            <tr>
                <th>Puntos Totales</th>
                <td><c:out value="${total_points}"/></td>
                <td><c:out value="${average_points_day}"/></td>
                <td><c:out value="${max_points_day}"/></td>
                <td><c:out value="${min_points_day}"/></td>
            </tr>
            <tr>
                <th>Turnos Totales</th>
                <td><c:out value="${total_turns}"/></td>
                <td><c:out value="${average_turns_day}"/></td>
                <td><c:out value="${max_turns_day}"/></td>
                <td><c:out value="${min_turns_day}"/></td>
            </tr>
		</table>
    </body>
</sevenislands:layout3>