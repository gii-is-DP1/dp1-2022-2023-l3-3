<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<style>
.content {
	background: url(resources/images/grafics/fondo1920x1080.jpg);
}

.body {
	width: 100%;
	height: 95%;
	display: flex;
	flex: 1;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
}
.manual {
	padding: 10px;
	margin-right: 0;
	align-self: flex-end;
}

.menu {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
	padding: 10px;

}
.btn {
	margin: 10px;
}
</style>


<sevenislands:layout2 pageName="welcome">
    <body class="content">   
       <div class="body" >
            <div class="menu">
                <img src="/resources/images/grafics/letras_7_islas.png" width="700" height="200">
                <img src="/resources/images/grafics/palmera.gif" width="256" height="256">
                
                <a class="btn btn-default" href='<spring:url value="/login" htmlEscape="true"/>'>Iniciar sesión</a>
                
                <a class="btn btn-default" href='<spring:url value="/signup" htmlEscape="true"/>'>Registrarse</a>
                
                <button class="btn btn-primary" data-toggle="modal" data-target="#instructionManual">
					Perfiles de Usuario
				</button>
            </div>
            <div id="instructionManual" class="modal fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">PERFILES DE USUARIO Y CONTRASEÑA</h4>
						</div>
						<div class="modal-body">
							<p>ADMINISTRADORES</p>
							<br>
							<p>admin1->admin</p>
							<p>albperleo->admin</p>
                            <p>javnunrui->admin</p>
                            <p>marvicmar->admin</p>
                            <p>juaramlop2->admin</p>
                            <p>alepervaz->admin</p>
							<br>
							<p>PLAYERS</p>
							<p>player1->player</p>
                            <p>player2->player</p>
                            <p>player3->player</p>
                            <p>player4->player</p>
                            <p>player5->player</p>
                            <br>
							<br>
							
						</div>
					</div>
				</div>
			</div>
    </div>
    </body>
</sevenislands:layout2>
