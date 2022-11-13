<%@ attribute name="card" required="true" rtexprvalue="true" type="org.springframework.samples.sevenislands.card.Card"
 description="card to be rendered" %>
 
    var image = document.getElementById('${card.cardType.name}');
    var positions = [[90,5],[300,5], [520,10], [660,250], [470,410], [280,530], [50,550]];
    var xPosition = positions[${card.position}][0];
    var yPosition = positions[${card.position}][1];
    ctx.drawImage(image, xPosition, yPosition, 180, 230);