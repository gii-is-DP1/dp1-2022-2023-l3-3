<%@ attribute name="board" required="false" rtexprvalue="true" type="org.springframework.samples.sevenislands.card.board.Board"
 description="board to be rendered" %>
<canvas id="canvas" width="${board.width}" height="${board.height}"></canvas>
<img id="source" src="${board.background}" style="display:none">
<img id="backimage" src="resources/images/cards/backimage.png" style="display:none">
<img id="baraja" src="resources/images/cards/baraja.png" style="display:none">
<img id="barril" src="resources/images/cards/barril.png" style="display:none">
<img id="botella" src="resources/images/cards/botella.png" style="display:none">
<img id="collar" src="resources/images/cards/collar.png" style="display:none">
<img id="copa" src="resources/images/cards/copa.png" style="display:none">
<img id="corona" src="resources/images/cards/corona.png" style="display:none">
<img id="diamante" src="resources/images/cards/diamante.png" style="display:none">
<img id="doblon" src="resources/images/cards/doblon.png" style="display:none">
<img id="pistola" src="resources/images/cards/pistola.png" style="display:none">
<img id="rubi" src="resources/images/cards/rubi.png" style="display:none">
<img id="sable" src="resources/images/cards/sable.png" style="display:none">
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