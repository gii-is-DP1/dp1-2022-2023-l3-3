<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 >
    <h1>Código de la partida: <c:out value="${lobby.code}"/></h1>
    
    <c:if test="${host==player}">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>EMPEZAR PARTIDA</a>
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>LISTA DE JUGADORES</a>
    </c:if>
    <a class="btn btn-default" id="leave_game" href='<spring:url value="/home" htmlEscape="true"/>'>ABANDONAR PARTIDA</a>
</sevenislands:layout2>