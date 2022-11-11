package org.springframework.samples.sevenislands.lobby;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.sevenislands.game.Game;
import org.springframework.samples.sevenislands.model.BaseEntity;
import org.springframework.samples.sevenislands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lobby")

public class Lobby extends BaseEntity {

  @Column(name = "code", unique = true, nullable = false)
  private String code;

  @Column(name = "active", unique = false, nullable = false)
  private boolean active;

  @ManyToMany(cascade = CascadeType.PERSIST)
  private List<Player> players;

  @OneToOne(cascade = CascadeType.ALL)
  private Game game;

  public List<Player> getPlayerInternal() {
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
