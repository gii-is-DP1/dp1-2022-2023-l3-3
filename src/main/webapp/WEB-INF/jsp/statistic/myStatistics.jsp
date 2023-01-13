<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="myStatistics">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Mis Estadísticas</h2>
        <h3>Nombre de Usuario: <c:out value="${user.nickname}"/></h3><br>

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
                <td><c:out value="${total_games_player}"/></td>
                <td><c:out value="${average_games_player}"/>/día</td>
                <td><c:out value="${max_games_player}"/> /dia</td>
                <td><c:out value="${min_games_player}"/> /dia</td>
            </tr>
            <tr>
                <th>Tiempo Jugado</th>
                <td><c:out value="${total_time_player}"/> min</td>
                <td><c:out value="${average_time_player}"/> min/día</td>
                <td><c:out value="${max_time_player}"/> min</td>
                <td><c:out value="${min_time_player}"/> min</td>
            </tr>
            <tr>
                <th>Victorias</th>
                <td><c:out value="${total_victories_player}"/></td>
                <td><c:out value="${average_victoriesByGame_player}"/> /partida</td>
                <td><c:out value="${max_victories_player}"/> /día</td>
                <td><c:out value="${min_victories_player}"/> /día</td>
            </tr>
            <tr>
                <th>Puntos Obtenidos</th>
                <td><c:out value="${total_points_player}"/> pts</td>
                <td><c:out value="${average_points_player}"/> pts/partida</td>
                <td><c:out value="${max_points_player}"/> pts</td>
                <td><c:out value="${min_points_player}"/> pts</td>
            </tr>
            <tr>
                <th>Turnos Realizados</th>
                <td><c:out value="${total_turns_player}"/></td>
                <td><c:out value="${average_turns_player}"/> /partida</td>
                <td><c:out value="${max_turns_player}"/></td>
                <td><c:out value="${min_turns_player}"/></td>
            </tr>
            <tr>
                <th>Nº jugadores en partidas</th>
                <td><c:out value="${total_playersByGame_player}"/></td>
                <td><c:out value="${average_playersByGame_player}"/> /partida</td>
                <td><c:out value="${max_playersByGame_player}"/></td>
                <td><c:out value="${min_playersByGame_player}"/></td>
            </tr>
		</table>
    </body>
</sevenislands:layout3>