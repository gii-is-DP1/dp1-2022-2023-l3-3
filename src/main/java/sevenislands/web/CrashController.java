package sevenislands.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.game.GameService;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;

@Controller
public class CrashController implements ErrorController{

	private final LobbyService lobbyService;
	private final GameService gameService;

	@Autowired
	public CrashController(LobbyService lobbyService, GameService gameService) {
		this.lobbyService = lobbyService;
		this.gameService = gameService;
	}

	@GetMapping("/error")
	public String triggerException(ModelMap model, HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) {
		model.put("lobby", lobbyService.checkUserLobby(logedUser));
		model.put("game", gameService.checkUserGame(logedUser));

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				model.put("exception", "Lo sentimos, esta página no ha sido encontrada");
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				model.put("exception", "Hubo un error en el servidor");
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				model.put("exception", "No tienes acceso a esta página");
			}
		} else model.put("exception", "Hubo un error inesperado");
		return "exception";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
