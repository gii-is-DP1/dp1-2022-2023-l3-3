package sevenislands.game.turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import sevenislands.card.Card;
import sevenislands.game.round.Round;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Turn extends BaseEntity {

    @Column(name = "dice", nullable = true)
    @Range(min = 1, max = 6)
    private Integer dice;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Round round;

    @ManyToMany
    private List<Card> cards;

    public static Integer rollDice() {
        return 1 + (int) Math.floor(Math.random() * 6);
    }

    public void addCard(Card card){
        if(cards==null){
            cards = new ArrayList<>();
        }
        cards.add(card);
    }
}
