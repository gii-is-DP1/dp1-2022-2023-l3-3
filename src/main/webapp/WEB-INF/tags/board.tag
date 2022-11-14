<%@ attribute name="board" required="false" rtexprvalue="true" type="org.springframework.samples.sevenislands.game.Game"
 description="board to be rendered" %>
<canvas id="canvas" width="${board.width}" height="${board.height}"></canvas>
<img id="source" src="${board.background}" style="display:none">
<img id="baraja" src="resources/images/cards/baraja.png" style="display:none">
<img id="barril" src="resources/images/cards/barril.png" style="display:none">
<img id="mapa_tesoro" src="resources/images/cards/mapa_tesoro.png" style="display:none">
<img id="collar" src="resources/images/cards/collar.png" style="display:none">
<img id="caliz" src="resources/images/cards/caliz.png" style="display:none">
<img id="corona" src="resources/images/cards/corona.png" style="display:none">
<img id="diamante" src="resources/images/cards/diamante.png" style="display:none">
<img id="doblon" src="resources/images/cards/doblon.png" style="display:none">
<img id="revolver" src="resources/images/cards/revolver.png" style="display:none">
<img id="rubi" src="resources/images/cards/rubi.png" style="display:none">
<img id="espada" src="resources/images/cards/espada.png" style="display:none">
<script>
function drawBoard(){ 
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");
    var image = document.getElementById('source');
    ctx.drawImage(image, 0, 0, ${board.width}, ${board.height});
    <jsp:doBody/>
}
window.onload = drawBoard();
</script>