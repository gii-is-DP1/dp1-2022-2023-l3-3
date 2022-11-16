package sevenislands.exceptions;

import lombok.Getter;

@Getter
public class NotExitPlayerException extends Exception {
    private String message = "El user especificado no forma parte del lobby";
}
