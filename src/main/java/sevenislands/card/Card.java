package sevenislands.card;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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

        
        @Column(name = "name", nullable = false)
        private String name;
        
        @Column(name = "amount", nullable = false)
        private Integer amount;

        @Column(name = "type", nullable = false)
        private String type;

        @ManyToMany(cascade = CascadeType.ALL)
        private List<Turn> turns;

        @ManyToMany(cascade = CascadeType.ALL)
        private List<Island> islands;
}
