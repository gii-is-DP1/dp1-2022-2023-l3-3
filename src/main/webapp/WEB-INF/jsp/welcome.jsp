<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2>
    <div class="row">
<<<<<<< HEAD
        <h2> Project ${title}</h2>
        <p><h2> Group ${group}</h2></p>
        <p><ul>
            <c:forEach items="${persons}" var="person">
                <li>
                    <c:out value="${person.firstName} ${person.lastName} "/>
                </li>
            </c:forEach>
        </ul></p>
=======
    <h2>Project ${title}</h2>
    <p><h2>Group ${group}</h2></p>
    <p><ul>
        
    <c:forEach items="${persons}" var="person">
        <li>
            <c:out value = "${person.firstName} ${person.lastName}" />
        </li>
    </c:forEach>
    </ul></p>
    </div>  
    <div class="row">
>>>>>>> 4f9fa771fce97aa2bf2ea553cd7eae6f98daf1db
        <div class="col-md-12">
            <spring:url value="/resources/images/pets.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-5">
            <spring:url value="/resources/images/logo_7_islas.png" htmlEscape="true" var="logo7IslasImage"/>
            <img class="img-responsive" src="${logo7IslasImage}"/>
        </div>
    </div>
</petclinic:layout>
