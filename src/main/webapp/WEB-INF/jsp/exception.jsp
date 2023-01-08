<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>

body {
    background: url(resources/images/grafics/fondo1920x1080.jpg);
}

.content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    padding: 20px;
}

.error_text{
    font-size: 2vw;
    color: white;
}

.error_img{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: auto;
    height: 100%;
    z-index: -1;
}

.error_panel {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

#gif {
    height: 20vh;
    width: 20vh;
}

</style>

<sevenislands:home pageName="error">
    <body>
        <c:choose>
            <c:when test="${game}">
                <a class="btn btn-default" href='<spring:url value="/game" htmlEscape="true"/>'>Volver a la Partida</a>
            </c:when>
            <c:when test="${lobby}">
                <a class="btn btn-default" href='<spring:url value="/lobby" htmlEscape="true"/>'>Volver al Lobby</a>
            </c:when>
        </c:choose>
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver al Home</a>
        <img class="error_img" src="/resources/images/grafics/formaInfo.png"/>
        <div class="error_panel">
            <div class="content">
                <img id="gif" src="/resources/images/grafics/palmera.gif" width="256" height="256">
                <div class="error_text">
                    <h1 class="error_text">HA OCURRIDO UN ERROR</h1>
                    <h1 class="error_text"> ${exception}</h1>
                </div>
            </div>
        </div>
    </body>
</sevenislands:home>
