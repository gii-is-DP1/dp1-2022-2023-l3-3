package org.springframework.samples.sevenislands.game.turn;





import java.time.LocalDateTime;
import java.time.LocalTime;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.sevenislands.card.Card;
import org.springframework.samples.sevenislands.game.Round;
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
    
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDateTime TimePress;


    // @ManyToMany(mappedBy = "turns")
    // private Set<Card> cards;
}
