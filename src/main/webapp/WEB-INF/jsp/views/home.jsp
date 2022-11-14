<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
.manual {
	position: absolute;
	bottom: 35px;
	right: 35px;
  }
</style>

<sevenislands:layout2 pageName="home">
	<body style="background: url(resources/images/grafics/fondo1920x1080.jpg)">  
		<div class="text-right" style="margin-bottom: 24px">
			<a href="/settings">
				<strong>${user.nickname}</strong>&thinsp;<img src="/resources/images/avatars/${user.avatar}" height="25" width="25">
			</a>
		</div> 
        <div>
            <center>
                <img src="/resources/images/grafics/letras_7_islas.png" width="700" height="200">
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

		<div class="manual">
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
    </body>
</sevenislands:layout2>
