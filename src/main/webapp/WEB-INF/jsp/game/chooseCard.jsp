<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
body {
    background-color: rgb(62, 179, 188) !important;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: row;
}


#myCards {
    height: 100%;
    width: 100%;
}

#myCards {
    display: flex;
}


</style>
<div id="myCards">
    <c:forEach items="${card}" var="card">
        <c:if test="${cardAnadida!=card}">
            <img src="/resources/images/cards/${card.key.tipo}.png" width="150"
            height="150"> 
            <a class="btn btn-default"
            href='<spring:url value="/delete/chooseCard/${card.key.id}?islaId=${IslaId}&NumCartasDelete=${cardsToEliminate}" htmlEscape="true"/>'>Entrego
            </a>
            <h3 class="game_text">${card.value}</h3> 
            <br />
        </c:if>
    </c:forEach>
</div>