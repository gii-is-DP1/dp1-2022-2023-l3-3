package sevenislands.game;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import sevenislands.game.island.Island;

import sevenislands.lobby.Lobby;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "ending_date")
	private Date endingDate;

	@OneToOne
	@NotNull
	private Lobby lobby;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Size(min = 7, max = 7)
	private List<Island> islands;
}
