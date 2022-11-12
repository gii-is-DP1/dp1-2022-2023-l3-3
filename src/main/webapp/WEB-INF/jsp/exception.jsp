<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>

<sevenislands:layout2 pageName="error">

    <h2>Something happened...</h2>

    <p>${exception.message}</p>

</sevenislands:layout2>
