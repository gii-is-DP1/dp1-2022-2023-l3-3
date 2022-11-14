package sevenislands.game.island;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import sevenislands.card.Card;
import sevenislands.game.Game;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "islands")
public class Island extends BaseEntity {

    // número para saber de qué isla se trata
    @Column(name = "island_number", nullable = false)
    @Min(value = 1)
    @Max(value = 7)
    private Integer num;

    @ManyToOne
    @NotNull
    private Game game;

    @ManyToMany(mappedBy = "islands")
    private List<Card> cards;
}
