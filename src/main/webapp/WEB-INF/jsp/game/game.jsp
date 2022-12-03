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
    width: 25%;
    background: #581212;
    border: 5px solid black;
}

#section2 {
    height: auto;
    width: 50%;
    display: flex;
    flex-direction: column;
}

#section3 {
    padding: 10px;
    margin: 10px;
    height: auto;
    width: 25%;
    background: #581212;
    color: white;
    display: flex;
    flex-direction: column;
    border: 5px solid black;

}

#selected_cards {
    padding: 10px;
    width: 100%;
    height: 70%;
    border-bottom: 5px solid black;
}

#cards {
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
}

.game_text{
    color: white;
}

</style>

<sevenislands:gameContent pageName="lobby">

    <body>
        <div id="section1">
            <div id="options">
                <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar partida</a>
                <c:if test="${player_turn==player}">
                    <br />
                    <div>
                        <center>
                            <h1 id="turn">ES TU TURNO</h1>
                        </center>
                    </div>
                    <c:if test="${dice==null}">
                        <br />
                        <div>
                            <center>
                                <a class="btn btn-default"
                                    href='<spring:url value="/turn/dice" htmlEscape="true"/>'>LANZAR DADO</a>
                            </center>
                        </div>
                    </c:if>
                    <c:if test="${dice!=null}">
                        <br/>
                        <center>
                            <h2 id="dice">${dice}</h2>
                        </center>
                    </c:if>
                    <br />
                    <div>
                        <center>
                            <a class="btn btn-default"
                                href='<spring:url value="/turn/endTurn" htmlEscape="true"/>'>TERMINAR TURNO</a>
                        </center>
                    </div>
                </c:if>
                <c:if test="${player_turn!=player}">
                    <br />
                    <div>
                        <center>
                            <h1 id="turn">ES EL TURNO DE ${player_turn.nickname}</h1>
                        </center>
                    </div>
                </c:if>
            </div>
            <div id="players">
                <h1 class="game_text">Aquí irian los jugadores de la partida</h1>
            </div>
        </div>

        <div id="section2">
            <center>
                <h1 id="time_left">Tiempo restante: ${time_left}</h1>
            </center>
            <div id="board">
                <sevenislands:game>
                    <c:forEach items="${islandList}" var="island">
                        <sevenislands:island island="${island}"/>
                    </c:forEach>
                    <sevenislands:deck/>
                </sevenislands:game>
            </div>
        </div>

        <div id="section3">
            <div id="selected_cards">
                <center>
                    <h1 class="game_text">Cartas seleccionadas</h1>
                </center>
            </div>
            <div id="cards">
                <center>
                    <h1 class="game_text">Mis cartas</h1>
                </center>
            </div>
        </div>
    </body>
</sevenislands:gameContent>