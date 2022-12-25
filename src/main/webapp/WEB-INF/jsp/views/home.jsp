<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
.content {
	background: url(resources/images/grafics/fondo1920x1080.jpg);
}

#name {
	width: 700;
	height: 200;
}
#manual {
	padding: 10px;
	margin-right: 0;
	align-self: flex-end;
}

#pirata {
	position: absolute;
	right: 0;
	bottom: 20px;
	height: 80%;
	width: 40%;
	z-index: -1;
}

.header {
	padding-right: 10px;
	width: 100%;
	height: 5%;
	display: flex;
	flex-direction: row;
	justify-content: end;
	align-items: center;
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

.img_home {
	height: 25px;
	width: 25px;
	margin-left: 10px;
}

</style>

<sevenislands:home pageName="home">
	<body class="content">
		<div class="header">
			<a href="/settings">
				<strong>${user.nickname}</strong>&thinsp;<img class="img_home" src="/resources/images/avatars/${user.avatar}">
			</a>
			<sec:authorize access="hasAuthority('player')">
				<a href="/achievements">
					<img class="img_home" src="/resources/images/grafics/trofeoIcono.png">
				</a>
			</sec:authorize>
		</div>

		<div class="body">
			<img id="name" src="/resources/images/grafics/letras_7_islas.png">
			<div class="menu">
				<sec:authorize access="hasAuthority('player')">
					<a class="btn btn-default" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Crear Partida</a>
				</sec:authorize>
				<sec:authorize access="hasAuthority('player')">
					<br/>
					<a class="btn btn-default" href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
				</sec:authorize>

				<sec:authorize access="hasAuthority('player')">
					<br/>
					<a class="btn btn-default" href='<spring:url value="/game/gamesAsPlayer" htmlEscape="true"/>'>Partidas jugadas</a>
				</sec:authorize>
		
				<sec:authorize access="hasAuthority('admin')">
					<a class="btn btn-default" href='<spring:url value="/controlPanel?valor=0" htmlEscape="true"/>'>Panel de Control</a>
				</sec:authorize>
		
				<sec:authorize access="hasAuthority('admin')">
					<br/>
					<a class="btn btn-default" href='<spring:url value="/game/finished" htmlEscape="true"/>'>Partidas finalizadas</a>
				</sec:authorize>
		
				<sec:authorize access="hasAuthority('admin')">
					<br/>
					<a class="btn btn-default" href='<spring:url value="/game/InProgress" htmlEscape="true"/>'>Partidas en curso</a>
				</sec:authorize>
		
				<br/>
				<a class="btn btn-default" href='<spring:url value="/logout" htmlEscape="true"/>'>Cerrar Sesión</a>
			</div>
			<img id="pirata" src="/resources/images/grafics/pirataLoroAnimado.gif">
			<div id="manual">
				<button class="btn btn-primary" data-toggle="modal" data-target="#instructionManual">
					¿Cómo se juega?
				</button>
			</div>
			<div id="instructionManual" class="modal fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">¿Cómo se juega?</h4>
						</div>
						<div class="modal-body">
							<p>PREPARACIÓN DEL JUEGO</p>
							<br>
							<p>1) Se reparten 3 cartas de doblones a los jugadores y se baraja el resto de cartas para
								formar un mazo.</p>
							<p>2) Se coloca una carta del mazo boca arriba en cada una de las islas del 1 al 6 y el
								mazo restante boca abajo en la Isla 7.</p>
							<p>3) Definimos quién será el jugador inicial (quién pulse antes el botón que aparezca por 
								pantalla).</p>
							<br>
							<p>SECUENCIA DEL JUEGO</p>
							<br>
							<p>7 Islas se juega por turnos y cada turno tiene dos acciones:</p>
							<p>Acción 1: El jugador lanza el dado.</p>
							<br>
							<p>Opción 1: Llevarse la carta de tesoro de la Isla correspondiente al número obtenido.
								Opción 2: Contratar al barco pirata para llevarse un tesoro ubicado en otra
								isla. Para ello deberá pagar tantas cartas de tesoro (incluido los doblones) como 
								espacios desee moverse sin pasar de la Isla 7 (estas cartas se descartan del juego).</p>
							<br>
							<p>Acción 2: Se repone la isla vacía con una nueva carta de tesoro del mazo colocándola boca arriba.</p>
							<br>
							<p>El juego mantiene esta misma secuencia donde el jugador lanza el dado, coge la carta de tesoro
								correspondiente o contrata al barco pirata para coger otra carta.</p>
							<br>
							<p>FIN DEL JUEGO</p>
							<br>
							<p>Una vez se acabe el mazo de cartas de tesoro, se completará la ronda actual (para que todos 
								jueguen la misma cantidad de turnos) y terminará el juego.</p>
							<br>
							<p>Para calcular el puntaje final, por cada doblón se obtendrá 1 punto y además se otorgarán puntos 
								por cada set de tesoros DISTINTOS (no incluye doblones) como tenga el jugador, guiándonos de la 
								tabla de abajo. Puede tenerse más de un set. Sumamos los puntajes y quien tenga el mayor puntaje, 
								habrá ganado el juego. En caso de empate, quien tenga más doblones será el ganador</p>
							<img src="/resources/images/grafics/puntos.png">
						</div>
					</div>
				</div>
			</div>
		</div>
    </body>
</sevenislands:home>
