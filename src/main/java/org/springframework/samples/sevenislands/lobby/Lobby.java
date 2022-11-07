package org.springframework.samples.sevenislands.lobby;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.sevenislands.model.BaseEntity;
import org.springframework.samples.sevenislands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="lobby")

public class Lobby extends BaseEntity{

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name="active", unique = false, nullable = false)
    private boolean active;

    @OneToMany
    private List<Player> players;

    protected List<Player> getPlayerInternal() {
		if (this.players == null) {
			this.players = new ArrayList<>();
		}
		return this.players;
	}

    public void addPlayer(Player player) {
		getPlayerInternal().add(player);
        player.setLobby(this);
	}
}
