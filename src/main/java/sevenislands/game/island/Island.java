package sevenislands.game.island;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import sevenislands.card.Card;
import sevenislands.game.Game;
import sevenislands.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Island extends BaseEntity {

    // Número para saber de qué isla se trata
    @Column(name = "island_number", nullable = false)
    @Min(value = 1)
    @Max(value = 7)
    private Integer num;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Card card;
}
