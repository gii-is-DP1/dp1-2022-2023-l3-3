package org.springframework.samples.sevenislands.lobby;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
    private Integer code;

    @Column(name="active", unique = false, nullable = false)
    private boolean active;

    @ManyToMany
    private List<Player> members;
}
