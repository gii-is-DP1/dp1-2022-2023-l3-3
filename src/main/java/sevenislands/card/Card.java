package sevenislands.card;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import sevenislands.enums.Tipo;
import sevenislands.game.Game;
import sevenislands.model.BaseEntity;

@Getter
@Setter
@Entity
public class Card extends BaseEntity implements Comparable<Card>{

    @Enumerated(value = EnumType.STRING)
    private Tipo tipo;
    
    @NotNull
    private Integer multiplicity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;

    @Override
    public int compareTo(Card c) {
        if (this.getId() == c.getId()) return 0; 
        else if (this.getId() > c.getId()) return 1; 
        else return -1;
    }
}
