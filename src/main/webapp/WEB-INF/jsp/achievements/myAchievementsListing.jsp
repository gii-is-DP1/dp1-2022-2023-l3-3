<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout3 pageName="myAchievements">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <h2><br>Mis Logros</h2>
        <h3>Nombre de Usuario: <c:out value="${user.nickname}"/></h3>

        <!-- crear una condicion que si tengo numero de logros distinto de cero, muestre una tabla como en achievementsListing -->
        <!-- si es cero, muestra lo que hay aqui debajo -->
        <center>
            <p>Aún no has jugado ninguna partida. Juega tu primera partida y empieza a conseguir logros</p><br>
            <a class="btn btn-default" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Juega Tu Primera Partida</a>
        </center>
    </body>
</sevenislands:layout3>