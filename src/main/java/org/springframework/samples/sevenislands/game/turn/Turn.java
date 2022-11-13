package org.springframework.samples.sevenislands.game.turn;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.samples.sevenislands.card.Card;
import org.springframework.samples.sevenislands.game.round.Round;
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
    @Range(min = 0, max = 6)
    private Integer dice;

    @ManyToOne
    @NotNull
    Player player;

    @ManyToOne
    @NotNull
    Round round;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "cards_turns", joinColumns = { @JoinColumn(name = "card_id") }, inverseJoinColumns = {
            @JoinColumn(name = "turn_id") })
    private List<Card> cards;

    public static Integer rollDice() {
        return 1 + (int) Math.floor(Math.random() * 6);
    }
}
