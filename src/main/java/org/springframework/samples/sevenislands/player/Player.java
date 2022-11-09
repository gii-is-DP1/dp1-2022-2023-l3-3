package org.springframework.samples.sevenislands.player;

import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.achievement.Achievement;

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

}