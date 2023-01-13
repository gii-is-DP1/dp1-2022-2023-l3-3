package sevenislands.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import sevenislands.lobby.Lobby;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

	@Past
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "creation_date", nullable = false)
	private LocalDateTime creationDate;

	@Column(name = "active", unique = false, nullable = false)
	private boolean active;

	@Past
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endingDate;	

	@OneToOne
	@NotNull
	private Lobby lobby;

	@ManyToOne
    private User winner;

	@Column(name = "tieBreak")
    private boolean tieBreak;

	public String getFormattedCreationDate() {
		return creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public String getFormattedEndingDate() {
		return endingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
