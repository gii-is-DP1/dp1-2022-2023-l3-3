<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
body {
    background-color: rgb(62, 179, 188) !important;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: row;
    padding: 10px;
}
#turn {
    color: black;
    text-align: center;
    background-color: rgb(255, 50, 50);
    border: 2px solid black;
    font-weight: 700;
    border-radius: 50px;
    margin: 3px;
    width: 100%;
}

/* SECITION 1 */
#section1 {
    padding: 10px;
    height: auto;
    width: 30%;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
}

#actions {
    padding-bottom: 10px;
}

#leave-game {
    background-color: rgb(174, 24, 24);
}

#dice {
    background-color: #ffffff;
    color: black;
    border: 3px solid black;
    border-radius: 25px;
    padding: 5px 10px 5px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
}

#actions-container {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}

#players {
    border-top: 3px solid black;
    border-bottom: 3px solid black;
    padding-top: 10px;
    padding-bottom: 10px;
    display: flex;
    flex-direction: column;
}

#players-container {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    flex-wrap: wrap;
}

#player-img {
    width: 35px;
}

#player-details {
    display: flex;
    flex-direction: row;
    align-items: center;
}

#player-turn {
    color: #ff9a56;
    font-weight: bold;
}

#my-turn {
    color: #85ff54;
    font-weight: bold;
}

.player-text {
    color: white;
    padding-left: 5px;
}

#chat {
    padding-top: 10px;
}

/* SECTION 2 */
#section2 {
    margin-left: 10px;
    margin-right: 10px;
    height: auto;
    width: 40%;
    display: flex;
    flex-direction: column;
}

#time-left {
    color: black;
    background-color: white;
    border: 3px solid black;
    border-radius: 25px;
    font-weight: bold;
    margin-bottom: 10px;
    padding: 5px;
    text-align: center;
}

#board {
    height: 100%;
    width: 100%;
    border: 3px solid black;
    border-radius: 25px;
}

#canvas {
    height: 100%;
    width: 100%;
    border-radius: 25px;
}

/* SECTION 3 */
#section3 {
    padding: 10px;
    height: auto;
    width: 30%;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
}

#my-cards {
    padding-bottom: 10px;
}

#cards-container {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-wrap: wrap;
}

#cards-details {
    display: flex;
    flex-direction: row;
    align-items: center;
}

#cards-img {
    width: 55px;
}

#selected-cards {
    border-top: 3px solid black;
    border-bottom: 3px solid black;
    padding-top: 10px;
    padding-bottom: 10px;
}

#select-island {
    padding-top: 10px;
}

#select-island-container {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    flex-wrap: wrap;
}

/* OTHERS */

.text {
    color: white;
}

#cards {
    padding: 10px;
    width: 100%;
    height: 70%;
    border-bottom: 3px solid black;
}

#selected_cards {
    padding: 10px;
    height: auto;
    width: 100%;
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
}

.game-btn:hover {
    color: white;
    text-decoration: none;
}

</style>
<sevenislands:gameContent pageName="lobby">

    <body>
        <div id="section1">
            <div id="actions">
                <h1 class="text">Acciones</h1>
                <div id="actions-container">
                    <a class="game-btn" id="leave-game" href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar</a>
                    <c:if test="${player_turn==player}">
                        <c:if test="${dice==null}">
                            <a class="game-btn" href='<spring:url value="/turn/dice" htmlEscape="true"/>'>Lanzar dado</a>
                        </c:if>
                        <c:if test="${dice!=null}">
                            <p id="dice">${dice}</p>
                        </c:if>
                        <a class="game-btn" href='<spring:url value="/turn/endTurn" htmlEscape="true"/>'>Pasar</a>
                    </c:if>
                </div>
            </div>
            <div id="players">
                <h1 class="text">Jugadores</h1>
                <div id="players-container">
                    <c:forEach items="${userList}" var="user">
                        <div id="player-details">
                            <img id="player-img" src="/resources/images/avatars/${user.avatar}">
                            <c:choose>
                                <c:when test="${player_turn==user && player_turn==player}">
                                    <p class="player-text" id="my-turn">${user.nickname}</p>
                                </c:when>
                                <c:when test="${player_turn==user && player_turn!=player}">
                                    <p class="player-text" id="player-turn">${user.nickname}</p>
                                </c:when> 
                                <c:otherwise>
                                    <p class="player-text">${user.nickname}</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div id="chat">
                <h1 class="text">Chat</h1>
            </div>
        </div>

        <div id="section2">
            <p id="time-left">Tiempo restante: ${time_left}</p>
            <div id="board">
                <sevenislands:game>
                    <c:forEach items="${islandList}" var="island">
                        <c:if test="${island.num!=0}">
                        <sevenislands:island island="${island}" />
                        </c:if>
                    </c:forEach>
                    <sevenislands:deck />
                </sevenislands:game>
            </div>
        </div>

        <div id="section3">
            <div id="my-cards">
                <h1 class="text">Mis cartas</h1>
                <div id="cards-container">
                    <c:forEach items="${playerCardsMap}" var="card">
                        <div id="cards-details">
                            <a href="/turn/selectCard/${card.key.id}">
                                <img id="cards-img" src="/resources/images/cards/${card.key.tipo}.png">
                            </a>
                            <p class="text">${card.value}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div id="selected-cards">
                <h1 class="text">Cartas seleccionadas</h1>
                <div id="cards-container">
                    <c:forEach items="${selectedCards}" var="card">
                        <div id="cards-details">
                            <a href="/turn/deselectCard/${card.key.id}">
                                <img id="cards-img" src="/resources/images/cards/${card.key.tipo}.png">
                            </a>
                            <p class="text">${card.value}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div id="select-island">
                <h1 class="text">Islas a escoger</h1>
                <div id="select-island-container">
                    <c:if test="${player_turn==player}">
                        <c:if test="${dice!=null && IslasToChose!=null}">
                                <c:forEach items="${IslasToChose}" var="isla">
                                    <c:if test="${isla.num<=NumIslands}">    
                                        <a class="game-btn"
                                        href='<spring:url value="/turn/chooseIsland/${isla.num}" htmlEscape="true"/>'>Isla ${isla.num}
                                        </a>
                                    </c:if>
                                </c:forEach>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</sevenislands:gameContent>