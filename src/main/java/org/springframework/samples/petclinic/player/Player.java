package org.springframework.samples.petclinic.player;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.samples.petclinic.achievement.Achievement;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("player")
public class Player extends BaseEntity {


    @Column(nullable = false)
    @NotEmpty
    @Past
    private LocalDate bornDate;

    @Column(nullable = false)
    @NotEmpty
    private String avatar;

    @ManyToMany
    private Collection<Achievement> achievements;
}