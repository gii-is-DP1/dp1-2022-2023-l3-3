<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
body {
    background: url(resources/images/grafics/fondo1920x1080.jpg);
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 10px;
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
}

/* SECTION 2 */
#section2 {
    margin-left: 20px;
    margin-right: 20px;
    height: auto;
    width: 40%;
    display: flex;
    flex-direction: column;
}

/* SECTION 3 */
#section3 {
    height: auto;
    width: 30%;
}

#search-bar {
    height: 100%;
    width: 100%;
    background: #ffffff;
    border: 3px solid black;
    border-radius: 25px;
    padding-left: 10px;
}

.user-content {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

#results-box {
    align-items: flex-start;
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

.two-rows {
    height: 50%;
    width: 100%;
    display: flex;
    flex-direction: column;
}

.content-box {
    padding: 10px;
    height: 100%;
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

.first-row {
    padding-bottom: 5px;
}

.second-row {
    padding-top: 5px;
}

.text {
    color: white;
}

.user-content-details {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
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
                <a href="/friends">Amigos</a>
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
                <div id="requests" class="two-rows first-row">
                    <h1>Solicitudes recibidas</h1>
                    <div class="content-box">
                        <c:forEach items="${receivedRequests}" var="receivedRequest">
                            <div class="user-content">
                                <div class="user-content-details">
                                    <img class="header-img" src="/resources/images/avatars/${receivedRequest.user1.avatar}">
                                    <p class="text">${receivedRequest.user1.nickname}</p>
                                </div>
                                <a href="/friends/accept/${receivedRequest.id}">Aceptar solicitud</a>
                                <a href="/friends/reject/${receivedRequest.id}">Rechazar solicitud</a>
                                <a href="/friends/delete/${receivedRequest.id}">Eliminar solicitud</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div id="rejected" class="two-rows second-row">
                    <h1>Solicitudes rechazadas</h1>
                    <div class="content-box">
                        <c:forEach items="${rejected}" var="receivedRequest">
                            <div class="user-content">
                                <div class="user-content-details">
                                    <img class="header-img" src="/resources/images/avatars/${receivedRequest.user1.avatar}">
                                    <p class="text">${receivedRequest.user1.nickname}</p>
                                </div>
                                <a href="/friends/accept/${receivedRequest.id}">Aceptar solicitud</a>
                                <a href="/friends/delete/${receivedRequest.id}">Eliminar solicitud</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div id="section2">
                <h1>Amigos</h1>
                <div class="content-box">
                    <c:forEach items="${friends}" var="friend">
                        <c:choose>
                            <c:when test="${friend.user1==logedUser}">
                                <div class="user-content">
                                    <div class="user-content-details">
                                        <img class="header-img" src="/resources/images/avatars/${friend.user2.avatar}">
                                        <p class="text">${friend.user2.nickname}</p>
                                    </div>
                                    <a href="/friends/delete/${friend.id}">Eliminar amigo</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="user-content">
                                    <div class="user-content-details">
                                        <img class="header-img" src="/resources/images/avatars/${friend.user1.avatar}">
                                        <p class="text">${friend.user1.nickname}</p>
                                    </div>
                                    <a href="/friends/delete/${friend.id}">Eliminar amigo</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>

            <div id="section3">
                <div id="search" class="two-rows first-row">
                    <h1>Enviar solicitud</h1>
                    <form:form 
                    method="GET"
                    action="/friends/search" 
                    modelAttribute="searchedUser" 
                    class="form-horizontal">
                        <form:input id="search-bar" path="nickname" maxlength="20" required="true" placeholder="Buscar..."/>
                    </form:form>
                    <div class="content-box" id="results-box">
                        <c:forEach items="${searchResults}" var="user">
                            <div class="user-content">
                                <div class="user-content-details">
                                    <img class="header-img" src="/resources/images/avatars/${user.avatar}">
                                    <p class="text">${user.nickname}</p>
                                </div>
                                <a href="/friends/add/${user.id}">Enviar solicitud</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div id="my-requests" class="two-rows second-row">
                    <h1>Solicitudes enviadas</h1>
                    <div class="content-box">
                        <c:forEach items="${sentRequests}" var="sentRequest">
                            <div class="user-content">
                                <div class="user-content-details">
                                    <img class="header-img" src="/resources/images/avatars/${sentRequest.user2.avatar}">
                                    <p class="text">${sentRequest.user2.nickname}</p>
                                </div>
                                <a href="/friends/delete/${sentRequest.id}">Eliminar solicitud</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </body>
</sevenislands:gameContent>