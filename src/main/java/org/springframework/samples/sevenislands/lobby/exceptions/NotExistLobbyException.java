package org.springframework.samples.sevenislands.lobby.exceptions;

import lombok.Getter;

@Getter
public class NotExistLobbyException extends Exception {
    private String message = "No se ha encontrado el lobby";
}
