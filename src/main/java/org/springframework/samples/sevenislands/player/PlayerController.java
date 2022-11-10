package org.springframework.samples.sevenislands.player;

import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.user.AuthoritiesService;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PlayerController {

	private static final String VIEWS_PLAYER_UPDATE_FORM = "players/updatePlayerForm";

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
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@GetMapping(value = "/settings")
	public String initUpdateOwnerForm(Model model) {
		UserDetails authentication = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String playerName = authentication.getUsername();
		Player player = this.playerService.findPlayersByName(playerName);

		model.addAttribute(player);
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@PostMapping(value = "/settings")
	public String processUpdateplayerForm(@Valid Player player, BindingResult result) {
		
		if (result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		} else {
			UserDetails authentication = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String playerName = authentication.getUsername();
			Player authPlayer = this.playerService.findPlayersByName(playerName);

			player.setId(authPlayer.getId());
			this.playerService.save(player);
			return "redirect:/home";
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

}
