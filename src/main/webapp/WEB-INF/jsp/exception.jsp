<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<style>
    .content {
	background: url(resources/images/grafics/fondo1920x1080.jpg);
}
.contenedor{
    position: relative;
    display: inline-block;
    text-align: center;
}
.texto-encima{
    position: absolute;
    top: 10%;
    left: 10%;
    transform: translate(-10%, -10%);
}
.centrado{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>

<sevenislands:layout2 pageName="error">
    <body class="content">
        <body class="contenedor">
            <div class="texto-encima">
                <h1>HA OCURRIDO UN ERROR</h1>
            </div>
            <div class="centrado">
                <img  src="/resources/images/grafics/formaInfo.png"/>
                <div class="centrado">
                    
                    <h1 style="color: white">
                        <FONT SIZE=6> ${exception}</font>
                       </h1>
                    
                </div>
                
            </div>
        </body>
        

</body>
</sevenislands:layout2>
