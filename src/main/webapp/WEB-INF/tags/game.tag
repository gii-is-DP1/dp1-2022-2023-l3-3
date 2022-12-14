<canvas id="canvas"> </canvas>

<script>
function drawBoard(){
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");
    var image = new Image();

    image.onload = function() {
        canvas.width = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;
        var width = canvas.width;
        var height = canvas.height;

        var positions = 
        [[113/1000*width,2/1000*height],
        [353/1000*width,4/1000*height], 
        [612/1000*width,8/1000*height], 
        [770/1000*width,280/1000*height], 
        [553/1000*width,473/1000*height], 
        [322/1000*width,610/1000*height], 
        [65/1000*width,653/1000*height]];

        ctx.drawImage(image, 0, 0, image.width, image.height, 0, 0, canvas.width, canvas.height);
        <jsp:doBody/>
    }
    image.src="resources/images/grafics/tablero_recortado.jpg";
}
window.onload=drawBoard();
</script>