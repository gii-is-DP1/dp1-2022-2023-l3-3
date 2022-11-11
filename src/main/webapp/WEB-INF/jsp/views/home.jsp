<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 pageName="home">
	<body style="background: url(resources/images/fondo1920x1080.jpg)">   
		<p class="text-right"> <strong><sec:authentication property="name" /></strong> </p>
        <div>
            <center>
                <img src="/resources/images/letras_7_islas.png" width="700" height="200">
            </center>
        </div>

		<div>
			<center>
				<sec:authorize access="hasAuthority('player')">
					<a class="btn btn-default" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Crear Partida</a>
				</sec:authorize>
			</center>
		</div>

		<div>
			<center>
				<sec:authorize access="hasAuthority('player')">
					<br/>
					<a class="btn btn-default" href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
				</sec:authorize>
			</center>
		</div>

		<div>
            <center>
				<sec:authorize access="hasAuthority('admin')">
					<a class="btn btn-default" href='<spring:url value="/controlPanel" htmlEscape="true"/>'>Panel de Control</a>
				</sec:authorize>
			</center>
		</div>

		<div>
            <center>
				<br/>
				<a class="btn btn-default" href='<spring:url value="/logout" htmlEscape="true"/>'>Cerrar Sesión</a>
			</center>
        </div>
    </body>
</sevenislands:layout2>
