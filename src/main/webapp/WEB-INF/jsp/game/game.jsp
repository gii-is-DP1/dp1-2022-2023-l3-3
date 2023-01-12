<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

#content {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

/* SECITION 1 */
#section1 {
    padding: 10px;
    height: 100%;
    width: 30%;
    background-color: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
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
    flex: 1;
    height: 100px;
    padding-top: 10px;
    width: 100%;
    display: flex;
    flex-direction: column;
}

#chat-box {
    padding: 10px;
    height: 100%;
    width: 100%;
    background: white;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    overflow-y: scroll;
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
    margin-bottom: 10px;
}

#chat-box::-webkit-scrollbar {
  display: none;
}

.chat-text{
    color: black;
}

#my-chat-message {
    margin-top: 5px;
    align-self: flex-end;
    display: flex;
    flex-direction: row;
    align-items: center;
}

#chat-message {
    margin-top: 5px;
    align-self: flex-start;
    display: flex;
    flex-direction: row;
    align-items: center;
}

#message {
    margin-left: 5px;
    padding: 0 10px 0 10px;
    background-color: #b1b1b1;
    border: 2px solid black;
    border-radius: 25px 25px 25px 0;
    min-width: 50px;
    text-align: center;
    display: flex;
    flex-direction: row;
}

#sender {
    font-weight: bold;
}

#my-message {
    margin-right: 5px;
    padding: 0 10px 0 10px;
    background-color: #ffa2a2;
    border: 2px solid black;
    border-radius: 25px 25px 0 25px;
    min-width: 50px;
    text-align: center;
}

#message-bar {
    background-color: white;
    border: 3px solid black;
    border-radius: 25px;
    padding-left: 10px;
    width: 100%;
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
    height: fit-content;
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
        <div id="content">
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
                    <div id="chat-box">
                        <c:forEach items="${gameMessages}" var="chat">
                            <c:choose>
                                <c:when test="${chat.sender==player}">
                                    <div id="my-chat-message">
                                        <p class="chat-text" id="my-message">
                                            <c:out value="${chat.message}"/>
                                        </p>
                                        <img id="player-img" src="/resources/images/avatars/${chat.sender.avatar}">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div id="chat-message">
                                        <img id="player-img" src="/resources/images/avatars/${chat.sender.avatar}">
                                        <div id="message">
                                            <p class="chat-text" id="sender">${chat.sender.nickname}:&nbsp;</p>
                                            <p class="chat-text">
                                                <c:out value="${chat.message}"/>
                                            </p>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <form:form modelAttribute="sentMessage" id="chat-form">
                        <form:input id="message-bar" path="message" maxlength="32" required="true" placeholder="Mensaje..." autocomplete="off"/>
                    </form:form>
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
        </div>
    </body>
</sevenislands:gameContent>

<script>
    var messageBody = document.querySelector('#chat-box');
    messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
</script>