<%@ attribute name="island" required="true" rtexprvalue="true" type="sevenislands.game.island.Island"%>

let treasure${island.num} = new Image();
treasure${island.num}.onload=function() {
    var xPosition = positions[${island.num - 1}][0];
    var yPosition = positions[${island.num - 1}][1];
    ctx.drawImage(treasure${island.num}, xPosition, yPosition, canvas.width*0.2, canvas.height*0.28);
}
treasure${island.num}.src="resources/images/cards/${island.treasure.name}.png";
