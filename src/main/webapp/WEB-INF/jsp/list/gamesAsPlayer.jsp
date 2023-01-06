<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 pageName="gamesAsPlayer">
    <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
        
    <h2><br>Partidas jugadas</h2>

    <table id="gamesAsPlayerTable" class="table table-striped">
        <thead>
        <tr>
            <th>Id</th>
			<th>Fecha de creación</th>
			<th>Fecha de finalización</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${games}" var="game">
            <tr>
                <td>
                    <c:out value="${game.id}"/>
                </td>
                <td>                    
                    <sevenislands:dateFormatter input="${game.creationDate}"/>                                        
                </td>
                <td>       
                    <c:out value="${game.endingDate} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</sevenislands:layout2>