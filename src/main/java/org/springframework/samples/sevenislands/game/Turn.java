package org.springframework.samples.sevenislands.game;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.samples.sevenislands.model.BaseEntity;
import org.springframework.samples.sevenislands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "turns")
public class Turn extends BaseEntity {

    @NotNull
    @Range(min = 1, max = 6)
    private Integer dado;

    @ManyToOne
    @NotNull
    Player player;

    @ManyToOne
    @NotNull
    Round round;

    @ManyToOne
    @NotNull
    Island island;

    // @OneToMany
    // private Set<Mazo?? o lo que sea> mazos;
}
