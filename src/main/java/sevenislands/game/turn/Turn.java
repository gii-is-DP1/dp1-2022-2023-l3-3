package sevenislands.game.turn;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import sevenislands.card.Card;
import sevenislands.game.round.Round;
import sevenislands.model.BaseEntity;
import sevenislands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "turns")
public class Turn extends BaseEntity {

    @Column(name = "dice", nullable = true)
    @Range(min = 1, max = 6)
    private Integer dice;

    @ManyToOne
    @NotNull
    private Player player;

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
