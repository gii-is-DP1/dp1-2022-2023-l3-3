package sevenislands.card;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import sevenislands.game.Game;
import sevenislands.model.BaseEntity;
import sevenislands.treasure.Treasure;

@Getter
@Setter
@Entity
public class Card extends BaseEntity{
    
    @ManyToOne
    private Treasure treasure;

    @NotNull
    private Integer multiplicity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;
}
