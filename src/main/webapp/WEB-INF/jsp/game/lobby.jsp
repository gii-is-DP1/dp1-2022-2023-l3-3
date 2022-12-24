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

#delete_icon {
    width: 20px;
    height: 20px;
}

</style>

<sevenislands:layout2 pageName="lobby">

    <body>
        <div>
            <center>
                <h1>Código de la partida:
                    <c:out value="${lobby.code}" />
                </h1>
            </center>
        </div>

        <c:if test="${host==logged_player}">
            <c:if test="${num_players>1}">
                <br />
                <div>
                    <center>
                        <a class="btn btn-default"
                            href='<spring:url value="/game" htmlEscape="true"/>'>EMPEZAR
                            PARTIDA</a>
                    </center>
                </div>
            </c:if>
            <br />
            <div>
                <center>
                    <a class="btn btn-default"
                        href='<spring:url value="/lobby/players" htmlEscape="true"/>'>LISTA DE
                        JUGADORES</a>
                </center>
            </div>
        </c:if>
        <div>
            <center>
                <br />
                <a class="btn btn-default" id="leave_game"
                    href='<spring:url value="/join" htmlEscape="true"/>'>UNIRSE A PARTIDA</a>
            </center>
        </div>
        <div>
            <center>
                <br />
                <a class="btn btn-default" id="leave_game"
                    href='<spring:url value="/lobby/leave" htmlEscape="true"/>'>ABANDONAR
                    PARTIDA</a>
            </center>
        </div>

        <div id="player_list">
            <h2><br/>Jugadores</h2>
            <table class="table table-striped">
                <tr>
                    <th>Avatar</th>			
                    <th>Nickname</th>
                    <c:if test="${host==logged_player}">
                        <th>Expulsar</th>
                    </c:if>
                </tr>
                <c:forEach items="${players}" var="player">
                    <tr>
                        <td><img src="/resources/images/avatars/${player.avatar}" height="25" width="25"></td>
                        <td><c:out value="${player.nickname}"/></td>
                        <c:if test="${host==logged_player}">
                            <td><a href="/lobby/delete/${player.id}"><img id="delete_icon" src="resources/images/icons/deleteIcon.png"></a></td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</sevenislands:layout2>