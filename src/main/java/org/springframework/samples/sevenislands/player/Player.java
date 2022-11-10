package org.springframework.samples.sevenislands.player;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.achievement.Achievement;
import org.springframework.samples.sevenislands.game.turn.Turn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("player")
public class Player extends User {

    @NotEmpty
    private String avatar;

    @ManyToMany()
    @JoinColumn(name = "achievements")
    private Collection<Achievement> achievements;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.LAZY)
    private Set<Turn> turns;
}