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
    }
    .fondo {
    background-color: rgb(255, 255, 255);
    }
</style>
<sevenislands:layout2 pageName="lobby_asignTurn">
<body>
    <h1>
        <strong>
            <center><span class="fondo">Sé el más rápido en darle al botón y empieza tirando</span></center>
        </strong>
    </h1>
        <div>
            <a class="btn btn-default" href="<spring:url value='/home' htmlEscape='true'/>">ABANDONAR PARTIDA</a>
        </div>
        <div>
            <center>
                <marquee behavior="alternate" scrollamount="50px">
                <a title="partida" href="/turn/newRound"><img src="/resources/images/grafics/boton.png" alt="partida" height="190" width="190"/></a>
                </marquee>
            </center>
        </div>
    </body>
</sevenislands:layout2>