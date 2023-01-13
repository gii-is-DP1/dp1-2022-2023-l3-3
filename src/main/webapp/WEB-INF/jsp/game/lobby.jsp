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

#delete_icon {
    width: 20px;
    height: 20px;
}

#menu {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
}

.player_list {
    padding: 10px;
    margin: 10px;
    height: auto;
    width: 35%;
    background: #802323;
    color: white;
    display: flex;
    flex-direction: column;
    border: 4px solid black;
    border-radius: 25px;
}

#code {
    color: black;
    background-color: white;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 10px;
    margin-bottom: 50px;
}

.text {
    text-align: center;
    color: white;
}

table {
    border-radius: 25px !important;
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
                <h1 id="code">Código de la partida:
                    <c:out value="${lobby.code}" />
                </h1>
        
                <c:if test="${host==logged_player}">
                    <c:if test="${num_players>1}">
                        <br />
                        <div>
                            <a class="btn btn-default"
                                href='<spring:url value="/game" htmlEscape="true"/>'>Empezar Partida</a>
                        </div>
                    </c:if>
                    <br />
                </c:if>
                <div>
                        <br />
                        <a class="btn btn-default" id="leave_game"
                            href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
                </div>
                <div>
                    <br />
                    <a class="btn btn-default" id="leave_game"
                        href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar Partida</a>
                </div>
            </div>
            <div class="player_list">
                <h2 class="text">Jugadores</h2>
                <table class="table table-striped">
                    <tr>	
                        <th class="column_name">Nickname</th>
                        <c:if test="${host==logged_player}">
                            <th class="column_name">Expulsar</th>
                        </c:if>
                    </tr>
                    <c:forEach items="${players}" var="player">
                        <tr>
                            <td>
                                <img src="/resources/images/avatars/${player.avatar}" height="25" width="25">
                                <c:out value="${player.nickname}"/>
                            </td>
                            <c:if test="${host==logged_player}">
                                <td><a href="/lobby/delete/${player.id}"><img id="delete_icon" src="resources/images/icons/deleteIcon.png"></a></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="player_list">
                <h2 class="text">Jugadores</h2>
                <c:forEach items="${friends}" var="friend">
                    <img src="/resources/images/avatars/${friend.avatar}" height="25" width="25">
                    <c:out value="${friend.nickname}"/>
                    <a href="/invite/viewer/${friend.id}">Invitar como espectador</a>
                    <a href="/invite/player/${friend.id}">Invitar como jugador</a>
                </c:forEach>
            </div>
        </div>
    </body>
</sevenislands:home>