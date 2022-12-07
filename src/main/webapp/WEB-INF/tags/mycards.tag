<%@ attribute name="treasure" required="true" rtexprvalue="true" type="sevenislands.treasure.Treasure"%>
<%@ attribute name="multiplicity" required="true" rtexprvalue="true" type="Integer"%>

<div id="treasure_div">
<canvas id="${treasure.name}_image" ></canvas>
<canvas id="${treasure.name}_multiplicity"></canvas>

<script>

const treasure_div = document.getElementById("treasure_div");
treasure_div.width = treasure_div.offsetWidth;
treasure_div.height = treasure_div.offsetHeight; 

console.log(treasure_div.width);
console.log(treasure_div.height);

function drawImage(){
    const section = document.getElementById("${treasure.name}_image");
    const ctx = section.getContext("2d");
    const width = treasure_div.width*0.60;
    const height = treasure_div.height;
    
    section.setAttribute("width",width);
    section.setAttribute("height",height);

    ${treasure.name}_card = new Image();
    ${treasure.name}_card.onload = function(){
        ctx.drawImage(${treasure.name}_card, 0, 0, width, height);
    }
    ${treasure.name}_card.src="resources/images/cards/${treasure.name}.png";


}

function drawMultiplicity(){
    const section = document.getElementById("${treasure.name}_multiplicity");
    const ctx = section.getContext("2d");
    const width = treasure_div.width*0.40;
    const height = treasure_div.height;

    section.setAttribute("width",width);
    section.setAttribute("height",height);

    ctx.font = "30px Arial";
    ctx.fillStyle = "white";
    ctx.fillText("x${multiplicity}", 0, height/2);
}

function drawInfo(){
    drawImage();
    drawMultiplicity();   
}

window.onload=drawInfo();
</script>
</div>