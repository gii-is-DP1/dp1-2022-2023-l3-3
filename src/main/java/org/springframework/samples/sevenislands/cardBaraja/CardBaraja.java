package org.springframework.samples.sevenislands.cardBaraja;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CardsBaraja")
public class CardBaraja extends BaseEntity{

    @NotNull
    @OneToOne
    protected CardType cardType;

    @NotNull
    @Min(0)
    protected Integer multiplicity;

    @NotNull
    @ManyToOne
    protected CardPurpose cardPurpose;

}
