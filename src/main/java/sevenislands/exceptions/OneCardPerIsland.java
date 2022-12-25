package sevenislands.exceptions;

import lombok.Getter;

@Getter
public class OneCardPerIsland extends Exception {
    private String message = "Las islas con número comprendido en el rango 1-6 sólo pueden tener 1 carta al mismo tiempo.";
}
