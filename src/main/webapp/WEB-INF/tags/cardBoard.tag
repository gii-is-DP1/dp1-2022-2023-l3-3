<%@ attribute name="island" required="true" rtexprvalue="true" type="org.springframework.samples.sevenislands.game.island.Island"
 description="Island to be rendered" %>

<%@ attribute name="card" required="true" rtexprvalue="true" type="org.springframework.samples.sevenislands.card.Card"
 description="Card to be rendered" %>
   
   var image = document.getElementById('${card.name}');
   var positions = [[90,5],[300,5], [520,10], [660,250], [470,410], [280,530], [50,550]];
   var xPosition = positions[${island.num - 1}][0];
   var yPosition = positions[${island.num - 1}][1];
   ctx.drawImage(image, xPosition, yPosition, 180, 230);