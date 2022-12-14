<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
}
.img_grafica {
	height: 20px;
	width: 20px;
}
.logros {
	margin-top: 14px;
	margin-right: 10px;
}
.estadisticas {
	margin-top: 12px;
	margin-right: -30px;
}
.menu-horizontal {
	list-style: none;
	display: flex;
	justify-content: space-around;
}
.menu-horizontal>li>a {
	display: block;
	padding: 15px 20px;
	color: rgb(0, 0, 0);
	text-decoration: none;
}
.menu-horizontal>li:hover {
	background-color: #5FA134
}
.menu-vertical {
	position: absolute;
	display: none;
	list-style: none;
	width: 140px;
	background-color: rgba(0, 0, 0, .5);
}
.menu-horizontal li:hover .menu-vertical {
	display: block;
}
.menu-vertical li:hover {
	background-color: black;
}
.menu-vertical li a {
	display: block;
	color: white;
	padding: 15px 15px 15px 20px;
	text-decoration: none;
}
</style>

<sevenislands:home pageName="home">
	<body class="content">
		<div class="header">
			<sec:authorize access="hasAuthority('player')">
				<a href="/friends">Amigos</a>
			</sec:authorize>
			<div class="estadisticas">
				<ul class="menu-horizontal">
					<img class="img_grafica" src="/resources/images/grafics/estadisticas.png">
					<li><strong>Estad??sticas</strong>
						<ul class="menu-vertical">
							<li><a href="/ranking/points">Ranking Puntos</a></li>
							<li><a href="/ranking/victories">Ranking Victorias</a></li>
							<li><a href="/statistics">Estad??sticas Globales</a></li>
							<sec:authorize access="hasAuthority('player')">
								<li><a href="/myStatistics">Mis Estad??sticas</a></li>
							</sec:authorize>
						</ul>
					</li>
				</ul>
			</div>
			<div class="logros">
				<ul class="menu-horizontal">
					<img class="img_home" src="/resources/images/grafics/trofeoIcono.png">
					<li><strong>Logros</strong>
						<ul class="menu-vertical">
							<li><a href="/achievements">Logros Globales</a></li>
							<sec:authorize access="hasAuthority('player')">
								<li><a href="/myAchievements">Mis Logros</a></li>
							</sec:authorize>
						</ul>
					</li>
				</ul>
			</div>
			<a href="/settings">
				<img class="img_home" src="/resources/images/avatars/${user.avatar}"><strong>${user.nickname}</strong>&thinsp;
			</a>
		</div>
		<div class="body">
			<img id="name" src="/resources/images/grafics/letras_7_islas.png">
			<div class="menu">
				<sec:authorize access="hasAuthority('player')">
					<a class="btn btn-default" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Crear Partida</a>
					<a class="btn btn-default" href='<spring:url value="/join" htmlEscape="true"/>'>Unirse a Partida</a>
					<c:if test="${hasPlayed}">
						<a class="btn btn-default" href='<spring:url value="/endGame" htmlEscape="true"/>'>Resultados anteriores</a>
					</c:if>
					<a class="btn btn-default" href='<spring:url value="/game/gamesAsPlayer" htmlEscape="true"/>'>Partidas jugadas</a>
				</sec:authorize>
		
				<sec:authorize access="hasAuthority('admin')">
					<a class="btn btn-default" href='<spring:url value="/controlPanel?valor=0" htmlEscape="true"/>'>Panel de Control</a>
					<a class="btn btn-default" href='<spring:url value="/controlAchievements" htmlEscape="true"/>'>Panel de logros</a>
					<a class="btn btn-default" href='<spring:url value="/game/finished" htmlEscape="true"/>'>Partidas finalizadas</a>
					<a class="btn btn-default" href='<spring:url value="/game/InProgress" htmlEscape="true"/>'>Partidas en curso</a>
				</sec:authorize>
		
				<a class="btn btn-default" href='<spring:url value="/custom-logout" htmlEscape="true"/>'>Cerrar Sesi??n</a>
			</div>
			<img id="pirata" src="/resources/images/grafics/pirataLoroAnimado.gif">
			<div id="manual">
				<button class="btn btn-primary" data-toggle="modal" data-target="#instructionManual">
					??C??mo se juega?
				</button>
			</div>
			<div id="instructionManual" class="modal fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">??C??mo se juega?</h4>
						</div>
						<div class="modal-body">
							<p>PREPARACI??N DEL JUEGO</p>
							<br>
							<p>1) Se reparten 3 cartas de doblones a los jugadores y se baraja el resto de cartas para
								formar un mazo.</p>
							<p>2) Se coloca una carta del mazo boca arriba en cada una de las islas del 1 al 6 y el
								mazo restante boca abajo en la Isla 7.</p>
							<p>3) Definimos qui??n ser?? el jugador inicial (qui??n pulse antes el bot??n que aparezca por 
								pantalla).</p>
							<br>
							<p>SECUENCIA DEL JUEGO</p>
							<br>
							<p>7 Islas se juega por turnos y cada turno tiene dos acciones:</p>
							<p>Acci??n 1: El jugador lanza el dado.</p>
							<br>
							<p>Opci??n 1: Llevarse la carta de tesoro de la Isla correspondiente al n??mero obtenido.
								Opci??n 2: Contratar al barco pirata para llevarse un tesoro ubicado en otra
								isla. Para ello deber?? pagar tantas cartas de tesoro (incluido los doblones) como 
								espacios desee moverse sin pasar de la Isla 7 (estas cartas se descartan del juego).</p>
							<br>
							<p>Acci??n 2: Se repone la isla vac??a con una nueva carta de tesoro del mazo coloc??ndola boca arriba.</p>
							<br>
							<p>El juego mantiene esta misma secuencia donde el jugador lanza el dado, coge la carta de tesoro
								correspondiente o contrata al barco pirata para coger otra carta.</p>
							<br>
							<p>FIN DEL JUEGO</p>
							<br>
							<p>Una vez se acabe el mazo de cartas de tesoro, se completar?? la ronda actual (para que todos 
								jueguen la misma cantidad de turnos) y terminar?? el juego.</p>
							<br>
							<p>Para calcular el puntaje final, por cada dobl??n se obtendr?? 1 punto y adem??s se otorgar??n puntos 
								por cada set de tesoros DISTINTOS (no incluye doblones) como tenga el jugador, gui??ndonos de la 
								tabla de abajo. Puede tenerse m??s de un set. Sumamos los puntajes y quien tenga el mayor puntaje, 
								habr?? ganado el juego. En caso de empate, quien tenga m??s doblones ser?? el ganador</p>
							<img src="/resources/images/grafics/puntos.png">
						</div>
					</div>
				</div>
			</div>
		</div>
    </body>
</sevenislands:home>