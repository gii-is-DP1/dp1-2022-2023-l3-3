<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
.posicion {
	position: absolute;
    top: 20px;
    right: 660px;
}
</style>

<sevenislands:layout3 pageName="achievements">
    <body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">
        <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        <div class="posicion">
            <img src="/resources/images/grafics/cofreAbierto.png" width="220" height="220">
        </div>
        <h2><br>Logros globales</h2>

        <table id="achievementsTable" class="table table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Límite</th>
                <th>Insignia</th>
                <th>Total jugadores</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${achievements}" var="achievement">
                <tr>
                    <td>
                        <c:out value="${achievement.name}"/>
                    </td>
                    <td>                    
                        <c:out value="${achievement.description}"/>                                        
                    </td>
                    <td>       
                        <c:out value="${achievement.threshold} "/>
                    </td>
                    <td>                    
                        <c:if test="${achievement.badgeImage == ''}">none</c:if>
                        <c:if test="${achievement.badgeImage != ''}">
                            <img src="${achievement.badgeImage}" width="65px"/> 
                        </c:if>
                    </td>
                    <td> 
                        <c:out value="${achievement.id}"/>
                    </td>
                    <!-- id por poner algo y que se muestre, aquí iría total jugadores que han conseguido el logro -->
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </body>
</sevenislands:layout3>