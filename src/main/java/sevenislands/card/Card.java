package sevenislands.card;

import java.util.List;


import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import javax.validation.constraints.NotNull;

import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Card extends BaseEntity {

        @NotNull
        private String tipo;

        @NotNull
        private String name;

        @NotNull
        private Integer multiplicity;

        @ManyToMany
        private List<Card> islands;
}
