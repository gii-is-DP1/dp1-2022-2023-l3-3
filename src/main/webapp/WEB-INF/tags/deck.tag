let treasure7 = new Image();
treasure7.onload=function() {
    var xPosition = positions[6][0];
    var yPosition = positions[6][1];
    ctx.drawImage(treasure7, xPosition, yPosition, canvas.width*0.2, canvas.height*0.24);
}
treasure7.src="resources/images/cards/baraja.png";