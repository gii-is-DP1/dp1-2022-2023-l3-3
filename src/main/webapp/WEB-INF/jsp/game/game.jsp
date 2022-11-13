<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
    body {
        background-image: url("resources/images/grafics/tablero.png");
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
</style>

<sevenislands:layout2 pageName="lobby">

    <body>
        <div class="cuadrado">
            <center>
                <h3 style="color:rgb(255, 255, 255);">Mis cartas</h3>
            </center>
        </div>

    </body>

</sevenislands:layout2>