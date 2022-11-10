<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
    body {
        background-image: url("resources/images/tablero.png");
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center center;
        background-attachment: fixed;
        backdrop-filter: blur(8px);
        height: 100%;
        width: 100%;
    }

</style>
<sevenislands:layout2 pageName="lobby">
    <body>
            <div>
                <center>
                    <h1>Código de la partida: <c:out value="${lobby.code}"/></h1>
                </center>
            </div>
            
            <c:if test="${host==player}">
                <br/>
                <div>
                    <center>
                        <a class="btn btn-default" href='<spring:url value="/game" htmlEscape="true"/>'>EMPEZAR PARTIDA</a>
                    </center>
                </div>
                <br/>
                <div>
                    <center>
                        <a class="btn btn-default" href='<spring:url value="/lobby/players" htmlEscape="true"/>'>LISTA DE JUGADORES</a>
                    </center>
                </div>
            </c:if>
    
            <div>
                <center>
                    <br/>
                    <a class="btn btn-default" id="leave_game" href='<spring:url value="/lobby/delete" htmlEscape="true"/>'>ABANDONAR PARTIDA</a>
                </center>
            </div>
    </body>
</sevenislands:layout2>