package sevenislands.player;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import sevenislands.user.User;
import sevenislands.achievement.Achievement;
import sevenislands.game.turn.Turn;
import sevenislands.lobby.Lobby;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("player")
public class Player extends User {
    
    @ManyToMany()
	@JoinColumn(name = "achievements")
    private Collection<Achievement> achievements;

    @ManyToOne(cascade = CascadeType.PERSIST)
	private Lobby lobby;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.LAZY)
    private Set<Turn> turns;
}