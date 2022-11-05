package org.springframework.samples.petclinic.cardBaraja;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CardsBaraja")
public class CardBaraja extends BaseEntity{

    @NotNull
    @OneToOne
    private CardType cardType;

    @NotNull
    @Min(0)
    private Integer multiplicity;

    @NotNull
    @ManyToOne
    private CardPurpose cardPurpose;

}
