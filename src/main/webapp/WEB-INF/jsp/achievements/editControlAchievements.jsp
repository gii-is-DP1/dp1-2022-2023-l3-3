<%@ page session="false" trimDirectiveWhitespaces="true" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<style>
    #for {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>


<sevenislands:layout2 pageName="editAchievement">
  <jsp:body>
    <a class="btn btn-default" href='<spring:url value="/controlAchievements" htmlEscape="true"/>'>Volver</a>
    <h2><br/>Editar Logro</h2>
    <form:form
        method="put"
        modelAttribute="achievement"
        class="form-horizontal"
        id="edit-achievement-form"
    >
    <input type="hidden" name="imagen" value="${achievement.achievementType}"/>
      <div class="form-group has-feedback">
        <sevenislands:inputField label="Nombre" name="name" required="required" />
        <sevenislands:selectField label="tipo" name="achievementType" names="${tipoLogro}" required="required"/>
        <sevenislands:inputField label="Limite" name="threshold"  required="required"/>
      </div>

      <div class="pull-right">
        <button class="btn btn-default mr-3" type="submit">Actualizar</button>
      </div>
    </form:form>
  </jsp:body>
</sevenislands:layout2>