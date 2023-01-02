<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
body {
    background-image: url("resources/images/grafics/tablero_fondo.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    background-attachment: fixed;
    backdrop-filter: blur(8px);
    height: 100%;
    width: 100%;
}

#content {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    height: 100%;
}

#menu {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
}

#winner_text {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#points_table {
    width: auto;
}

tr{
    text-align: center;
}

.column_name {
    background-color: #7c7c7c;
    text-align: center;
}

tr:nth-child(even) {
    background-color: #dddddd;
}
tr:nth-child(odd) {
    background-color: #bbbbbb !important;
}

</style>

<sevenislands:home pageName="lobby">

    <body>
        <div id="content">
            <div id="menu">
                <a class="btn btn-default" id="leave_game"
                    href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar Partida</a>
                <a class="btn btn-default" id="join_game"
                    href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
                <a class="btn btn-default" id="create_lobby"
                href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Volver a Jugar</a>
            </div>
            <div id="winner_text">
                <h1>Ganador</h1>
                <img class="img_home" src="/resources/images/avatars/${winner.avatar}">
                <h1>${winner.nickname}</h1>
            </div>
            <table class="table table-striped" id="points_table">
                <tr>
                    <th class="column_name">Nombre</th>
                    <th class="column_name">Puntuación</th>
                </tr>
                <c:forEach items="${players}" var="player">
                    <tr>
                        <td>
                            <img src="/resources/images/avatars/${player.getFirst().avatar}" height="40" width="40">
                            <c:choose>
                                <c:when test="${player.getFirst()==logedUser}">
                                    <strong><c:out value="${player.getFirst().nickname}"/></strong>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${player.getFirst().nickname}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${player.getSecond()}"/></td>
                    </tr>
                </c:forEach>
            </table>

        </div>
    </body>
</sevenislands:home>