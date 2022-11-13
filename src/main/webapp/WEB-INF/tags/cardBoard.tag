<%@ attribute name="card" required="true" rtexprvalue="true" type="org.springframework.samples.sevenislands.card.Card"
 description="card to be rendered" %>
 
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('${card.cardType.name}');
ctx.drawImage(image,100,100);