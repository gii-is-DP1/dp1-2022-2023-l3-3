<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
.caja{
    padding: 15px;
    border-left: 6px solid #5FA134;
    background:#F5F5F5;
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 50px;
    
}
</style>
<sevenislands:layout3 pageName="myStatistics">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Mis Estadísticas</h2>
        <h3>Nombre de Usuario: <c:out value="${user.nickname}"/></h3><br>

        <div class="caja">
            <h3>Nº de Partidas Jugadas: <c:out value="${num_games_player}"/></h3>
            <h3>Tiempo total jugado: <c:out value="${total_time_played}"/> min</h3>
            <h3>Puntos Totales: <c:out value="${total_points_player}"/></h3>
            <h3>Turnos Totales: <c:out value="${num_turns_player}"/></h3>
        </div>
    </body>
</sevenislands:layout3>