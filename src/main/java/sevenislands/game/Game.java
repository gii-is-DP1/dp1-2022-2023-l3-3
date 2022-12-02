package sevenislands.game;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import sevenislands.card.Card;
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
	private Date endingDate;

	@Column(name = "active", unique = false, nullable = false)
	private boolean active;

	@OneToOne
	@NotNull
	private Lobby lobby;
}
