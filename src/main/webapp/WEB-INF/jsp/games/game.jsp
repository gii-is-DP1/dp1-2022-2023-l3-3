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

                            .mis_cartas {
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

                            .img {
                                width: 100%;
                            }

                            .tablero {
                                background-image: url("resources/images/tablero.png");
                                background-size: 100% 100%;
                                background-repeat: no-repeat;
                                background-position: center;
                                width: 100%;
                                height: 100%;
                                display: flex;
                                justify-content: center;
                                align-items: flex-end;
                            }

                            .sub {
                                width: 34%;
                                height: 100%;
                            }

                            #left {
                                width: 25%;
                            }

                            #center {
                                width: 55%;
                            }

                            #right {
                                width: 20%;
                            }

                            .dado {
                                display: flex;
                                justify-content: center;
                                align-items: flex-start;
                            }
                        </style>

                        <sevenislands:gameLayout pageName="lobby">

                            <body>
                                <!-- <div class="dice">
                                    <a href="/turn/rollDice">
                                        <img src="/resources/images/dado.png">
                                    </a>
                                </div>
                                <div class="mis_cartas">
                                    <center>
                                        <h3 style="color:rgb(255, 255, 255);">Mis cartas</h3>
                                    </center>
                                </div> -->
                                <div class="tablero">
                                    <div class="sub" id="left">

                                    </div>
                                    <div class="sub" id="center">
                                        <div class="dado">
                                            <a href="/turn/rollDice">
                                                <img src="/resources/images/dado.png" class="dado">
                                            </a>
                                        </div>
                                    </div>
                                    <div class="sub" id="right">
                                        <div class="mis_cartas">
                                            <center>
                                                <h3 style="color:rgb(255, 255, 255);">Mis cartas</h3>
                                            </center>
                                        </div>
                                    </div>
                                </div>
                            </body>

                        </sevenislands:gameLayout>