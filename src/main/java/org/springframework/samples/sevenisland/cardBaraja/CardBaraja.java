package org.springframework.samples.sevenisland.cardBaraja;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenisland.model.BaseEntity;

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
    @OneToMany
    protected CardPurpose cardPurpose;

}
