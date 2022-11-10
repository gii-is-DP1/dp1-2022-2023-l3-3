package org.springframework.samples.sevenislands.game;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDateTime creationDate;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime endingDate;


	@OneToOne(mappedBy = "game")
	@NotNull
	private Lobby lobby;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
	private Set<Round> rounds;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
	@Size(min = 7, max = 7)
	private Set<Island> islands;
}
