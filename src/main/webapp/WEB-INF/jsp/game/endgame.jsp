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
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
    height: 100%;
}

#menu {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
}

#winner {
    color: black;
    background-color: white;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 10px;
    margin-bottom: 50px;
}
</style>

<sevenislands:home pageName="lobby">

    <body>
        <div id="content">
            <div id="menu">
                <div>
                    <a class="btn btn-default" id="leave_game"
                        href='<spring:url value="/lobby/leave" htmlEscape="true"/>'>Abandonar Partida</a>
                </div>
                <div>
                    <br />
                    <a class="btn btn-default" id="create_lobby"
                        href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Volver a Jugar</a>
                </div>
                <div>
                    <br />
                    <a class="btn btn-default" id="leave_game"
                        href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
                </div>
            </div>
            <div>
                <h1>Ganador</h1>
                <img class="img_home" src="/resources/images/avatars/${winner.avatar}">
                <h1>${winner.nickname}</h1>
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Nombre</th>
                    <th>Puntuación</th>
                </tr>
                <c:forEach items="${players}" var="player">
                    <tr>
                        <td>
                            <img src="/resources/images/avatars/${player.getFirst().avatar}" height="40" width="40">
                            <c:out value="${player.getFirst().nickname}"/>
                        </td>
                        <td><c:out value="${player.getSecond()}"/></td>
                    </tr>
                </c:forEach>
            </table>

        </div>
    </body>
</sevenislands:home>