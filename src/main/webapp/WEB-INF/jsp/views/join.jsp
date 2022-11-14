<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<sevenislands:layout2 pageName="join">
    <a class="btn btn-default" href='<spring:url value="/home" htmlEscape="true"/>'>Volver</a>
    <h2><br/>Unirse a una Partida</h2>
    <form:form modelAttribute="code" class="form-horizontal" id="validate-code">
        <br/>
        <div class="form-group has-feedback">
            <sevenislands:inputField label="Code" name="code" required="required"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-default" type="submit">Unirse</button>
            </div>
        </div>
    </form:form>
    </body>
</sevenislands:layout2>