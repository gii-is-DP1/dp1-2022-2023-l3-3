package sevenislands.card;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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

        @NotNull
        @NotBlank
        @Column(unique = true)
        protected String name;

        @NotNull
        private Integer multiplicity;

        @ManyToMany(mappedBy = "cards")
        protected List<Turn> turns;

        @ManyToMany(mappedBy = "cards")
        protected List<Island> islands;
}
