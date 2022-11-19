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
        background-position: center;
        background-attachment: fixed;
    }
    .cuadrado {
        width: 500px;
        /* Ancho de 150 píxeles */
        height: 185px;
        /* Alto de 150 píxeles */
        background: #581212;
        /* Fondo de color rojo */
        border: 3px solid #000;
        /* Borde color negro y de 1 píxel de grosor */
        position: absolute;
        bottom: 50px;
        right: 45px;
    }
#turn {
    color: black;
    text-align: center;
    background-color: rgb(255, 50, 50);
    border: 2px solid black;
    font-weight: 700;
    border-radius: 50px;
    margin: 3px;
    width: 50%;
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
    color: white;
}
</style>

<sevenislands:layout2 pageName="lobby">

    <body>
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Abandonar partida</a>
        <c:if test="${player_turn==player}">
            <br />
            <div>
                <center>
                    <h1 id="turn">ES TU TURNO</h1>
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
        <div class="cuadrado">
            <center>
                <h3 style="color:rgb(255, 255, 255);">Mis cartas</h3>
                <h2 id="time_left">Tiempo restante: ${time_left}</h2>
            </center>
        </div>
        <c:if test="${player_turn==player}">
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

    </body>

</sevenislands:layout2>