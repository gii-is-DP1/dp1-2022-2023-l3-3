<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
                    <%@ page contentType="text/html; charset=UTF-8" %>
                        <%@ taglib prefix="game" tagdir="/WEB-INF/tags" %>

                            <style>
                                body {
                                    background-color: aliceblue;
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

                                .card {
                                    margin-top: 3%;
                                    margin-bottom: 3%;
                                    margin-left: 10%;
                                    margin-right: 10%;
                                    border-radius: 5px;
                                    display: flex;
                                    align-items: center;
                                }

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

                                /*SECCIÓN IZQUIERDA --> CARDS DE AVATAR*/
                                #left {
                                    width: 25%;
                                    padding-top: 5%;
                                    padding-left: 10px;
                                }

                                #avatar_card {
                                    background-color: #66472e;
                                    font-size: 20px;
                                    color: white;
                                    font-weight: bold;
                                    padding-left: 10px;
                                    width: 75%;
                                }

                                /*SECCIÓN CENTRAL --> TABLERO, CARTAS, DADO*/
                                #center {
                                    padding-top: 3%;
                                    padding-bottom: 3%;
                                    width: 45%;
                                    text-align: center;
                                }

                                #board {
                                    background-image: url("resources/images/grafics/tablero_recortado.jpg");
                                    background-repeat: no-repeat;
                                    background-size: contain;
                                    background-position: center;
                                    height: 100%;
                                    width: 100%;
                                }

                                .img {
                                    height: 100%;
                                    width: 100%;
                                }

                                /*SECCIÓN DERECHA --> MIS CARTAS*/
                                #right {
                                    width: 30%;
                                }

                                .mis_cartas {
                                    width: 500px;
                                    height: 185px;
                                    background: #581212;
                                    border: 3px solid #000;
                                    position: absolute;
                                    bottom: 50px;
                                    right: 45px;
                                    height: 90%;
                                    border-radius: 5px;
                                }

                                #mis_cartas_card {
                                    justify-content: center;
                                }
                            </style>

                            <sevenislands:gameLayout pageName="lobby">

                                <body>
                                    <div class="tablero">
                                        <div id="left">
                                            <h1 id="titulo_seccion">JUGADORES DE LA SALA</h1>
                                            <c:forEach items="${players}" var="player">
                                                <div class="card" id="avatar_card">
                                                    <img src="/resources/images/avatars/${player.avatar}" width="75">
                                                    <c:out value="${player.getNickname()}" />
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <div id="center">
                                            <game:board board="${game}">
                                                <c:forEach items="${game.islands}" var="island">
                                                    <c:if test="${island.num < 7}">
                                                        <c:forEach items="${island.cards}" var="card">
                                                            <game:cardBoard island="${island}" card="${card}" />
                                                        </c:forEach>
                                                    </c:if>
                                                    <c:if test="${island.num == 7}">
                                                        <game:cardIsland7 island="${island}" />
                                                    </c:if>
                                                </c:forEach>
                                            </game:board>
                                        </div>
                                        <div id="right">
                                            <div class="mis_cartas">
                                                <div class="card" id="mis_cartas_card">
                                                    <h3 id="titulo_seccion">MIS CARTAS</h3>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </body>

                            </sevenislands:gameLayout>