
<style>
    body {
        background-image: url("resources/images/tablero.png");
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        background-attachment: fixed;
    }
    .cuadrado {
    width: 500px;           /* Ancho de 150 píxeles */
    height: 185px;          /* Alto de 150 píxeles */
    background: #581212;        /* Fondo de color rojo */
    border: 3px solid #000; /* Borde color negro y de 1 píxel de grosor */
    position: absolute;
    bottom: 50px;
    right: 45px;
  }
</style>

<sevenislands:layout2 pageName="lobby">
    <body>
        <br>
        <center>
            <h1>LA PARTIDA ESTA A PUNTO DE COMENZAR</h1>
        </center>

        <center>
            <a title="Delocos" href="/welcome"><img src="/resources/images/boton.png" alt="ELPEPE" /></a>

        </center>
        <!--<a title="Los Tejos" href="/welcome"><img src="/resources/images/agregarIcono.png" alt="ELPEPE" /></a>-->

        <div class="cuadrado">
            <center>
                <h3 style="color:rgb(255, 255, 255);">Mis cartas</h3> 
            </center>
        </div>
        
    </body>

</sevenislands:layout2>