package sevenislands.card;



import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

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

        @ManyToMany
        private List<Card> islands;
}
