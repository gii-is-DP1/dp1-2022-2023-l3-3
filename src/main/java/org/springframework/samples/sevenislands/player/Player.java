package org.springframework.samples.sevenislands.player;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.achievement.Achievement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("player")
public class Player extends User {

    @Column(nullable = false)
    @NotEmpty
    @Past
    @Temporal(TemporalType.DATE)
    private LocalDate bornDate;

    @Column(nullable = false)
    @NotEmpty
    private String avatar;

    @ManyToMany
    private Collection<Achievement> achievements;
}