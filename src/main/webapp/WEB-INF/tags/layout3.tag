<%@ tag trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>

<!doctype html>
<html>
	<sevenislands:htmlHeader/>
	<body>
		<div class="container-fluid">
			<div class="container xd-container">
				<c:if test="${not empty message}" >
				<div class="alert alert-${not empty messageType ? messageType : 'info'}" role="alert">
					<c:out value="${message}"></c:out>
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button> 
				</div>
				</c:if>
				<jsp:doBody/>
			</div>
		</div>
		<sevenislands:footer/>
		<jsp:invoke fragment="customScript" />

		<div>
            <audio autoplay loop>
                <source src="/resources/images/grafics/ambientePirata.mp3" type="audio/mp3">
            </audio>
        </div>
	</body>
</html>