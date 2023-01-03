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

import sevenislands.game.Game;
import sevenislands.model.BaseEntity;
import sevenislands.enums.Tipo;

@Getter
@Setter
@Entity
public class Card extends BaseEntity{

    @Enumerated(value = EnumType.STRING)
    private Tipo Tipo;
    
    @NotNull
    private Integer multiplicity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;
}
