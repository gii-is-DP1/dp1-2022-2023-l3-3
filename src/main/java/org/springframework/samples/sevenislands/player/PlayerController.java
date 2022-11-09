package org.springframework.samples.sevenislands.player;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.user.AuthoritiesService;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class PlayerController {

	private static final String VIEWS_PLAYER_CREATE_OR_UPDATE_FORM = "player/createOrUpdatePlayerForm";

	private final PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService, UserService userService, AuthoritiesService authoritiesService) {
		this.playerService = playerService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/players/new")
	public String initCreationForm(Map<String, Object> model) {
		Player player = new Player();
		model.put("player", player);
		return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
	}

	/**@PostMapping(value = "/players/new")
	public String processCreationForm(@Valid player player, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_player_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating player, user and authorities
			this.playerService.save(player);
			
			return "redirect:/players/" + player.getId();
		}
	}

	@GetMapping(value = "/players/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("player", new player());
		return "players/findplayers";
	}

	@PostMapping(value = "/players/{playerId}/edit")
	public String processUpdateplayerForm(@Valid player player, BindingResult result,
			@PathVariable("playerId") int playerId) {
		if (result.hasErrors()) {
			return VIEWS_player_CREATE_OR_UPDATE_FORM;
		}
		else {
			player.setId(playerId);
			this.playerService.save(player);
			return "redirect:/players/{playerId}";
		}
	}**/

}
