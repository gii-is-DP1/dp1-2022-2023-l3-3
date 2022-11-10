<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<sevenislands:layout2 pageName="welcome">
    <body style="background: url(resources/images/fondo1920x1080.jpg)">   
        <div>
            <center>
                <img src="/resources/images/letras_7_islas.png" width="700" height="200">
            </center>
        </div>

        <div>
            <center>
                <img src="/resources/images/palmera.gif" width="256" height="256">
            </center>
        </div>
    
        <div>
            <center>
                <a class="btn btn-default" href='<spring:url value="/login" htmlEscape="true"/>'>Iniciar sesión</a>
            </center>
        </div>
    
        <br/>
        <div>
            <center>
                <a class="btn btn-default" href='<spring:url value="/signup" htmlEscape="true"/>'>Registrarse</a>
            </center>
        </div>
    </body>
</sevenislands:layout2>
