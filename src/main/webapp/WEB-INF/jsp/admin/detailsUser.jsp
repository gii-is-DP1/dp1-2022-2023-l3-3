<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
.rows {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
</style>

<sevenislands:layout2 pageName="userDetails">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <div class="rows">
            <a class="btn btn-default" href='<spring:url value="/controlPanel/?valor=0" htmlEscape="true"/>'>Volver</a>
            <a class="btn btn-default" href="/controlPanel/edit/${user.id}">Editar</a>
        </div>
        <div class="rows">
            <div>
                <h1><br>Detalles de <c:out value="${user.nickname}"/></h1>
                <p>Avatar: <img src="/resources/images/avatars/${user.avatar}" height="25" width="25"></p>
                <p>Nombre de usuario: <c:out value="${user.nickname}"/></p>
                <p>Nombre: <c:out value="${user.firstName}"/></p>
                <p>Apellidos: <c:out value="${user.lastName}"/></p>
                <p>Correo electrónico: <c:out value="${user.email}"/></p>
                
                <p>Fecha de creación: <c:out value="${user.creationDate}"/></p>
                <p>Fecha de nacimiento: <c:out value="${user.birthDate}"/></p>
                <p>Tipo de usuario: <c:out value="${user.userType}"/></p>
                <p>Estado: <c:out value="${user.enabled}"/></p>
            </div>
            <div>
                <h1><br>Detalles de las partidas</h1>
                <p>Número de partidas jugadas: <c:out value="${totalGamesPlayed}"/></p>
                <p>Total de puntos obtenidos: <c:out value="${totalPoints}"/></p>
                <p>Tiempo total jugado: <c:out value="${totalHoursPlayed}"/></p>
                <p>Turnos totales jugados: <c:out value="${totalTurns}"/></p>
            </div>
        </div>
        <div>
            <h1><br>Logros obtenidos</h1>
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
        </div>
    </body>
</sevenislands:layout2>