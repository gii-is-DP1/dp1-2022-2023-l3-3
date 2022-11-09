<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>

<sevenislands:layout2 pageName="join">
    <body style="background: url(resources/images/fondo1920x1080.jpg)">     
        <form:form modelAttribute="code" class="form-horizontal" id="validate-code">
        <div class="form-group has-feedback">
            <sevenislands:inputField label="Code" name="code"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-default" type="submit">Unirse</button>
            </div>
        </div>
    </form:form>
    </body>
</sevenislands:layout2>