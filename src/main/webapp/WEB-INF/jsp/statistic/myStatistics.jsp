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
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
            <tr>
                <th>Tiempo Total Jugado</th>
                <td><c:out value="${total_time_player}"/> min</td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
            <tr>
                <th>Puntos Totales</th>
                <td><c:out value="${total_points_player}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
            <tr>
                <th>Turnos Totales</th>
                <td><c:out value="${total_turns_player}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
                <td><c:out value="${}"/></td>
            </tr>
		</table>
    </body>
</sevenislands:layout3>