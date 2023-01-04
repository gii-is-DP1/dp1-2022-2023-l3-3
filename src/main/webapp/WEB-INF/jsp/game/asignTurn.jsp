<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
#content {
    background-image: url("/resources/images/grafics/tablero_fondo.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    height: 100%;
}

#header {
    margin: 10px;
}

#header_text {
    display: flex;
    flex-direction: row;
    justify-content: center;
}

#text {
    color: black;
    background-color: white;
    border-radius: 25px;
    padding: 10px;
    margin-bottom: 50px;
    width: fit-content;
}

#btn_content {
    margin-top: 50px;
    justify-self: center;
}
</style>
<sevenislands:home pageName="lobby_asignTurn">
    <body>
        <div id="content">
            <div id="header">
                <div>
                    <a class="btn btn-default" href="<spring:url value='/home' htmlEscape='true'/>">Abandonar Partida</a>
                </div>
                <div id="header_text">
                    <h1 id="text">Sé el más rápido en darle al botón y empieza tirando</h1>
                </div>
            </div>
            <div id="btn_content">
                <center>
                    <marquee behavior="alternate" scrollamount="50px">
                    <a title="partida" href="/turn/newRound"><img src="/resources/images/grafics/boton.png" alt="partida" height="190" width="190"/></a>
                    </marquee>
                </center>
            </div>
        </div>
    </body>
</sevenislands:home>