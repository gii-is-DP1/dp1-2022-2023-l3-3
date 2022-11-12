package org.springframework.samples.sevenislands.player;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.achievement.Achievement;
import org.springframework.samples.sevenislands.game.turn.Turn;
import org.springframework.samples.sevenislands.lobby.Lobby;

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

    @ManyToOne()
	private Lobby lobby;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.LAZY)
    private Set<Turn> turns;
}