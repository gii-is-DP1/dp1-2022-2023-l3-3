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

                            #dice {
                                color: rgb(0, 0, 0);
                                text-align: center;
                                background-color: rgb(255, 255, 255);
                                border: 2px solid black;
                                font-weight: 700;
                                border-radius: 50px;
                                margin: 3px;
                                width: 25px;
                            }

                            #time_left {
                                color: black;
                                background-color: white;
                                border-radius: 50px;
                                margin: 10px;
                                padding: 5px;
                            }

                            #section1 {
                                margin: 10px;
                                padding: 10px;
                                height: auto;
                                width: 20%;
                                background: #581212;
                                border: 5px solid black;
                            }

                            #section2 {
                                height: auto;
                                width: 45%;
                                display: flex;
                                flex-direction: column;
                            }

                            #section3 {
                                padding: 10px;
                                margin: 10px;
                                height: auto;
                                width: 35%;
                                background: #581212;
                                color: white;
                                display: flex;
                                flex-direction: column;
                                border: 5px solid black;

                            }

                            #cards {
                                padding: 10px;
                                width: 100%;
                                height: 70%;
                                border-bottom: 5px solid black;
                            }

                            #selected_cards {
                                padding: 10px;
                                height: auto;
                                width: 100%;
                            }

                            #board {
                                height: 100%;
                                width: 100%;
                            }

                            #canvas {
                                height: 100%;
                                width: 100%;
                            }

                            #options {
                                padding: 10px;
                                width: 100%;
                                height: 30%;
                                border-bottom: 5px solid black;
                            }

                            #players {
                                padding: 10px;
                                height: 70%;
                                width: 100%;
                                display: flex;
                                flex-direction: column;
                            }

                            .game_text {
                                color: white;
                            }

                            #playerDetails {
                                display: flex;
                                flex-direction: row;
                                align-items: center;
                                margin-bottom: 20px;
                            }

                            #playerImg {
                                width: 20%;
                            }

                            #leaveGame {
                                background-color: rgb(167, 16, 16);
                            }

                            #myCards {
                                height: 100%;
                                width: 100%;
                            }

                            #myCards {
                                display: flex;
                            }

                            #treasure_div {
                                width: 33%;
                                height: 25%;
                                display: flex;
                            }
                        </style>

                        <sevenislands:gameContent pageName="lobby">

                            <body>
                                <div id="section1">
                                    <div id="options">
                                        <a class="btn btn-default" id="leaveGame"
                                            href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar partida</a>
                                        <c:if test="${player_turn==player}">
                                            <br />
                                            <c:if test="${dice==null}">
                                                <br />
                                                <div>
                                                    <center>
                                                        <a class="btn btn-default"
                                                            href='<spring:url value="/turn/dice" htmlEscape="true"/>'>Lanzar
                                                            dado</a>
                                                    </center>
                                                </div>
                                            </c:if>
                                            <c:if test="${dice!=null}">
                                                <br />
                                                <center>
                                                    <h2 id="dice">${dice}</h2>
                                                </center>
                                            </c:if>
                                            <br />
                                            <div>
                                                <center>
                                                    <a class="btn btn-default"
                                                        href='<spring:url value="/turn/endTurn" htmlEscape="true"/>'>Terminar
                                                        turno</a>
                                                </center>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div id="players">
                                        <c:forEach items="${userList}" var="user">
                                            <div id="playerDetails">
                                                <h1 class="game_text">${user.nickname}</h1>
                                                <img id="playerImg" src="/resources/images/avatars/${user.avatar}">
                                                <c:if test="${player_turn==user && player_turn==player}">
                                                    <br />
                                                    <div>
                                                        <center>
                                                            <h2 id="turn">Tu turno</h2>
                                                        </center>
                                                    </div>
                                                </c:if>
                                                <c:if test="${player_turn==user && player_turn!=player}">
                                                    <br />
                                                    <div>
                                                        <center>
                                                            <h2 id="turn">Turno de ${player_turn.nickname}</h2>
                                                        </center>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>

                                <div id="section2">
                                    <center>
                                        <h1 id="time_left">Tiempo restante: ${time_left}</h1>
                                    </center>
                                    <div id="board">
                                        <sevenislands:game>
                                            <c:forEach items="${islandList}" var="island">
                                                <sevenislands:island island="${island}" />
                                            </c:forEach>
                                            <sevenislands:deck />
                                        </sevenislands:game>
                                    </div>
                                </div>

                                <div id="section3">

                                    <div id="cards">
                                        <center>
                                            <h1 class="game_text">Mis cartas</h1>
                                        </center>
                                        <div id="myCards">
                                            <c:forEach items="${playerCardsMap}" var="cards">
                                                <img src="/resources/images/cards/${cards.key.name}.png" width="150"
                                                height="150"> 
                                                  
                                                <h3 class="game_text">${cards.value}</h3> 
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div id="Islas a escoger">
                                        <center>
                                            <h1 class="game_text">Islas a escoger</h1>
                                        </center>
                                        <c:if test="${player_turn==player}">
                                            <br />
                                            <c:if test="${dice!=null && IslasToChose!=null}">
                                                <br />
                                                <center>
                                                    <c:forEach items="${IslasToChose}" var="isla">
                                                        <c:if test="${isla<=NumIslands}">    
                                                            <a class="btn btn-default"
                                                            href='<spring:url value="/turn/endTurn" htmlEscape="true"/>'>Isla ${isla}
                                                            </a>
                                                        </c:if>
                                            </c:forEach>
                                                    
                                                </center>
                                            </c:if>
                                        </c:if>
                                    </div>
                                </div>
                            </body>
                        </sevenislands:gameContent>