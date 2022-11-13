<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
                    <%@ page contentType="text/html; charset=UTF-8" %>

                        <style>
                            body {
                                background-color: aliceblue;
                            }

                            /*GENERAL*/
                            .tablero {
                                /* imagen del tablero*/
                                background-image: url("resources/images/tablero.png");
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
                                border-radius: 5px;
                                display: flex;
                                align-items: center;
                                justify-content: center;
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
                                background-color: #F2EED2;
                                font-size: 20px;
                                font-weight: bold;
                            }

                            /*SECCIÓN CENTRAL --> TABLERO, CARTAS, DADO*/
                            #center {
                                width: 50%;
                                text-align: center;
                            }

                            /*SECCIÓN DERECHA --> MIS CARTAS*/
                            #right {
                                width: 25%;
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
                                        <!-- AQUÍ HAY QUE METER LA CLASE CANVAS -->
                                        <p>AQUÍ VA EL DADO</p>
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