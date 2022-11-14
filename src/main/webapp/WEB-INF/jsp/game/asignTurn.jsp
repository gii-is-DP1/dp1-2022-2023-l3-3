<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
                    <%@ page contentType="text/html; charset=UTF-8" %>
                        <style>
                            body {
                                background-image: url("/resources/images/grafics/tablero_fondo.png");
                                background-size: cover;
                                background-repeat: no-repeat;
                                background-position: center;
                                background-attachment: fixed;
                                backdrop-filter: blur(8px);
                            }

                            .principal {
                                width: 100%;
                                height: 100%;
                                padding-bottom: 10%;
                                display: grid;
                                justify-content: center;
                            }

                            .sub {
                                width: 100%;
                                text-align: center;
                            }

                            #btn_rojo {
                                height: 50%;
                            }

                            #text {
                                font-size: 100px;
                                color: white;
                                text-shadow: 1px 1px 5px black;
                            }
                        </style>
                        <sevenislands:gameLayout pageName="lobby">

                            <body>
                                <div class="principal">
                                    <div id="btn_rojo" class="sub">
                                        <a title="partida" href="/turn/newRound">
                                            <img src="/resources/images/grafics/boton.png" alt="partida" />
                                        </a>
                                    </div>
                                    <div class="sub">
                                        <h1 id="text">AAAAARGHHH!!</h1>
                                    </div>
                                </div>
                            </body>
                        </sevenislands:gameLayout>