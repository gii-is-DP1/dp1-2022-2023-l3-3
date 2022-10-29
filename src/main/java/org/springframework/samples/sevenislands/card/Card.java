package org.springframework.samples.sevenislands.card;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.Enums.NombreCarta;
import org.springframework.samples.sevenislands.Enums.TipoCarta;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Card extends BaseEntity{

    @NotNull
    protected NombreCarta nombreCarta;

    @NotNull
    @Min(0)
    protected Integer multiplicity;
    protected TipoCarta tipoCarta;

}
