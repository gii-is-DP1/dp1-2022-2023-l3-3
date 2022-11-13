package sevenislands.card;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import sevenislands.game.island.Island;
import sevenislands.game.turn.Turn;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cards")
public class Card extends BaseEntity {

        @ManyToOne
        @NotNull
        protected CardType cardType;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "cards_turns", joinColumns = { @JoinColumn(name = "card_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "turn_id") })
        protected Set<Turn> turns;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "cards_islands", joinColumns = { @JoinColumn(name = "card_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "island_id") })
        protected Set<Island> islands;
}
