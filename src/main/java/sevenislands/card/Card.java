package sevenislands.card;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import sevenislands.game.turn.Turn;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name= "cards")
public class Card extends BaseEntity {

        @Column(name = "tipo")
        @NotNull
        private String tipo;

        
        @Column(name = "name")
        @NotNull
        private String name;
   
        @Column(name = "multiplicity")
        @NotNull
        private Integer multiplicity;
        
        @ManyToMany(cascade = CascadeType.ALL)
        protected List<Turn> turns;

        @ManyToMany(cascade = CascadeType.ALL)
        private List<Card> islands;
}
