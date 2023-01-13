<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>
body {
    background: url(resources/images/grafics/fondo1920x1080.jpg);
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 10px;
    overflow: hidden;
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}

body::-webkit-scrollbar {
  display: none;
}

/* HEADER */
#header {
    height: 50px;
    width: 100%;
    margin-bottom: 10px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

#icon {
    height: 100%;
    width: fit-content;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

#icon-img {
    width: 30px;
    margin: 10px;
}

#menu {
    height: 100%;
    width: 100%;
    margin-left: 10px;
    margin-right: 10px;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
}

#menu li {
    list-style: none;
}

.header-img {
	height: 25px;
	width: 25px;
    margin-right: 5px;
}

#profile {
    height: 100%;
    width: fit-content;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 10px;
}

/* CONTENT */
#content {
    margin-top: 10px;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

/* SECITION 1 */
#section1 {
    height: 100%;
    width: 30%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#content-box-details {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    color: white;
}

/* SECTION 2 */
#section2 {
    margin-left: 20px;
    margin-right: 20px;
    height: auto;
    width: 40%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
}

#island-logo {
    width: 100%;
}

#btn-container {
    justify-self: center;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    margin-bottom: 50px;
}

.game-btn {
    background-color: #a75d21;
    color: white;
    border: 3px solid black;
    border-radius: 25px;
    padding: 5px 10px 5px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    margin: 10px 0 10px 0;
}

#code-bar {
    height: 100%;
    width: 100%;
    background: #ffffff;
    border: 3px solid black;
    border-radius: 25px;
    padding-left: 10px;
}

#for {
    display: flex;
    flex-direction: column;
    align-items: center;
}

#error {
    color: black;
    text-align: center;
    background-color: rgb(255, 50, 50);
    border: 3px solid black;
    font-weight: 700;
    border-radius: 25px;
    padding: 0 10px 0 10px;
    width: 100%;
    margin: 10px 0 10px 0;
}

/* SECTION 3 */
#section3 {
    height: 100%;
    width: 30%;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: space-between;
}

#pirata {
    width: 100%;
}

/* OTHERS */

a {
    color: white !important; 
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
}

a:hover {
    color: rgb(184, 184, 184) !important;
    text-decoration: none !important;
}

.content-box {
    padding: 10px;
    height: 50%;
    width: 100%;
    background: #802323;
    border: 3px solid black;
    border-radius: 25px;
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow-y: scroll;
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}

.content-box::-webkit-scrollbar {
  display: none;
}

.text {
    color: white;
}

.player-info {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    color: white;
}

.game-img {
    height: 25px;
	width: 25px;
    margin-right: 10px;
}

</style>
<sevenislands:gameContent pageName="lobby">
    <body>
        <div id="header">
            <div id="icon">
                <a href="/home">
                    <img id="icon-img" src="/resources/images/grafics/palmera.gif">
                </a>
            </div>
            <div id="menu">
                <sec:authorize access="hasAuthority('player')">
                    <a href="/friends">Amigos</a>
                </sec:authorize>
                <a href="/ranking/points">Ranking Puntos</a>
                <a href="/ranking/victories">Ranking Victorias</a>
                <a href="/statistics">Estadísticas Globales</a>
                <sec:authorize access="hasAuthority('player')">
                    <a href="/myStatistics">Mis Estadísticas</a>
                </sec:authorize>
                <a href="/achievements">Logros Globales</a>
                <sec:authorize access="hasAuthority('player')">
                    <a href="/myAchievements">Mis Logros</a>
                </sec:authorize>
            </div>
            <a href="/settings" id="profile">
                <img class="header-img" src="/resources/images/avatars/${logedUser.avatar}">
                ${logedUser.nickname}
            </a>
        </div>

        <div id="content">
            <div id="section1">
                <sec:authorize access="hasAuthority('player')">
                    <h1>Invitaciones recibidas</h1>
                    <div class="content-box">
                        <c:forEach items="${invitations}" var="invitation">
                            <div id="content-box-details">
                                <div class="player-info">
                                    <img class="game-img" src="/resources/images/avatars/${invitation.sender.avatar}">
                                    <c:out value="${invitation.sender.nickname}"/>
                                </div>
                                <c:out value="${invitation.mode}"/>
                                <a href="/lobby/accept/${invitation.id}">Aceptar</a>
                            </div>
                        </c:forEach>
                    </div>
                </sec:authorize>
            </div>

            <div id="section2">
                <img id="island-logo" src="/resources/images/grafics/letras_7_islas.png">
                <div id="btn-container">
                    <div id="for" >
                        <c:forEach items="${errors}" var="error">
                          <p id="error">${error}</p>
                        </c:forEach>
                    </div>
                    <sec:authorize access="hasAuthority('player')">
                        <form:form 
                        method="POST"
                        modelAttribute="code" 
                        class="form-horizontal">
                            <form:input id="code-bar" path="code" maxlength="20" required="true" placeholder="Código de la lobby..." autocomplete="off"/>
                        </form:form>
                        <a class="game-btn" href='<spring:url value="/lobby/create" htmlEscape="true"/>'>Crear Partida</a>
                        <c:if test="${hasPlayed}">
                            <a class="game-btn" href='<spring:url value="/endGame" htmlEscape="true"/>'>Resultados anteriores</a>
                            <a class="game-btn" href='<spring:url value="/game/gamesAsPlayer" htmlEscape="true"/>'>Partidas jugadas</a>
                        </c:if>
                    </sec:authorize>
            
                    <sec:authorize access="hasAuthority('admin')">
                        <a class="game-btn" href='<spring:url value="/controlPanel?valor=0" htmlEscape="true"/>'>Panel de Control</a>
                        <a class="game-btn" href='<spring:url value="/controlAchievements" htmlEscape="true"/>'>Panel de logros</a>
                        <a class="game-btn" href='<spring:url value="/game/finished" htmlEscape="true"/>'>Partidas finalizadas</a>
                        <a class="game-btn" href='<spring:url value="/game/InProgress" htmlEscape="true"/>'>Partidas en curso</a>
                    </sec:authorize>
                    <a class="game-btn" href='<spring:url value="/custom-logout" htmlEscape="true"/>'>Cerrar Sesión</a>
                </div>
            </div>

            <div id="section3">
                <img id="pirata" src="/resources/images/grafics/pirataLoroAnimado.gif">
                <div id="manual-content">
                    <button class="btn btn-primary" data-toggle="modal" data-target="#instructionManual">
                        ¿Cómo se juega?
                    </button>
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
            </div>
        </div>
    </body>
</sevenislands:gameContent>