<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
    display: flex;
    flex-direction: column;
    padding: 10px;
    overflow: hidden;
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}

body::-webkit-scrollbar {
  display: none;
}

/* HEADER */
#header {
    height: 50px;
    width: 100%;
    margin-bottom: 10px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

#icon {
    height: 100%;
    width: fit-content;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

#icon-img {
    width: 30px;
    margin: 10px;
}

#menu {
    height: 100%;
    width: 100%;
    margin-left: 10px;
    margin-right: 10px;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
}

#menu li {
    list-style: none;
}

.header-img {
	height: 25px;
	width: 25px;
    margin-right: 5px;
}

#profile {
    height: 100%;
    width: fit-content;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 10px;
}

/* CONTENT */
#content {
    margin-top: 10px;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}

/* SECITION 1 */
#section1 {
    height: 100%;
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

/* SECTION 2 */
#section2 {
    margin-left: 20px;
    margin-right: 20px;
    height: auto;
    width: 40%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
}

#code {
    color: black;
    background-color: white;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    border: 3px solid black;
    padding: 0 10px 0 10px;
    margin-top: 100px;
}

#btn-container {
    justify-self: center;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    margin-bottom: 100px;
}

.game-btn {
    background-color: #a75d21;
    color: white;
    border: 3px solid black;
    border-radius: 25px;
    padding: 5px 10px 5px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    margin: 10px 0 10px 0;
}

/* SECTION 3 */
#section3 {
    height: 100%;
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#pirata {
    width: 100%;
}

/* OTHERS */

a {
    color: white !important; 
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
}

a:hover {
    color: rgb(184, 184, 184) !important;
    text-decoration: none !important;
}

.content-box {
    padding: 10px;
    height: 50%;
    width: 100%;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow-y: scroll;
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}

.content-box::-webkit-scrollbar {
  display: none;
}

.text {
    color: white;
}

.content-box-details {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    color: white;
}

.player-info {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    color: white;
}

.game-img {
    height: 25px;
	width: 25px;
    margin-right: 10px;
}

</style>
<sevenislands:gameContent pageName="lobby">
    <body>
        <div id="header">
            <div id="icon">
                <a href="/home">
                    <img id="icon-img" src="/resources/images/grafics/palmera.gif">
                </a>
            </div>
            <div id="menu">
                <a href="/friends">Amigos</a>
                <a href="/ranking/points">Ranking Puntos</a>
                <a href="/ranking/victories">Ranking Victorias</a>
                <a href="/statistics">Estadísticas Globales</a>
                <sec:authorize access="hasAuthority('player')">
                    <a href="/myStatistics">Mis Estadísticas</a>
                </sec:authorize>
                <a href="/achievements">Logros Globales</a>
                <sec:authorize access="hasAuthority('player')">
                    <a href="/myAchievements">Mis Logros</a>
                </sec:authorize>
            </div>
            <a href="/settings" id="profile">
                <img class="header-img" src="/resources/images/avatars/${logedUser.avatar}">
                ${logedUser.nickname}
            </a>
        </div>

        <div id="content">
            <div id="section1">
                <h2 class="text">Jugadores</h2>
                <div class="content-box">
                    <c:forEach items="${players}" var="player">
                        <div class="content-box-details">
                            <div class="player-info">
                                <img class="game-img" src="/resources/images/avatars/${player.avatar}">
                                <c:choose>
                                    <c:when test="${player==logged_player}">
                                        <strong><c:out value="${player.nickname}"/></strong>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${player.nickname}"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${host==logged_player}">
                                <td><a href="/lobby/delete/${player.id}"><img class="game-img" src="resources/images/icons/deleteIcon.png"></a></td>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="section2">
                <h1 id="code">Código de la partida: <c:out value="${lobby.code}" /></h1>
                <div id="btn-container">
                    <c:if test="${host==logged_player && num_players>1}">
                        <a class="game-btn" href='<spring:url value="/game" htmlEscape="true"/>'>Empezar Partida</a>
                    </c:if>
                        <a class="game-btn" id="leave_game" href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
                    <a class="game-btn" id="leave_game" href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar Partida</a>
                </div>
            </div>

            <div id="section3">
                <h2 class="text">Invitar amigos</h2>
                <div class="content-box">
                    <c:forEach items="${friends}" var="friend">
                        <div class="content-box-details">
                            <div class="player-info">
                                <img class="game-img" src="/resources/images/avatars/${friend.avatar}">
                                <c:out value="${friend.nickname}"/>
                            </div>
                            <a href="/invite/viewer/${friend.id}">Como espectador</a>
                            <a href="/invite/player/${friend.id}">Como jugador</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</sevenislands:gameContent>