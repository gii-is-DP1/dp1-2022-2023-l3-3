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

                                /*GENERAL*/
                                .tablero {
                                    /* imagen del tablero*/
                                    background-image: url("resources/images/fondo1920x1080.jpg");
                                    background-size: 100% 100%;
                                    background-repeat: no-repeat;
                                    background-position: center;
                                    /*espacios del tablero*/
                                    width: 100%;
                                    height: 100%;
                                    display: flex;
                                }

                                .card {
                                    margin-top: 3%;
                                    margin-bottom: 3%;
                                    margin-left: 10%;
                                    margin-right: 10%;
                                    border-radius: 5px;
                                    display: flex;
                                    align-items: center;
                                }

                                #titulo_seccion {
                                    font-size: 30px;
                                    font-weight: bold;
                                    color: white;
                                    text-shadow: 1px 1px 4px black;
                                    text-align: center;
                                }

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
                                    background-image: url("resources/images/Tablero_recortado.jpg");
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
                                                    <img src="/resources/images/${player.avatar}" width="75">
                                                    <c:out value="${player.getNickname()}" />
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <div id="center">
                                            <game:board board="${board}">
                                                <c:forEach items="${board.cards}" var="card">
                                                    <game:cardBoard card="${card}" />
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