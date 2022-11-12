package sevenislands.lobby.exceptions;

import lombok.Getter;

@Getter
public class NotExitPlayerException extends Exception {
    private String message = "El player especificado no forma parte del lobby";
}
