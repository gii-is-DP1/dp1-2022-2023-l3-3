package org.springframework.samples.sevenislands.card;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "card_types")
public class CardType extends BaseEntity {

     @NotNull
     @NotBlank
     @Column(unique = true)
     protected String name;

     @NotNull
     protected Integer multiplicity;

     @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardType", fetch = FetchType.EAGER)
     List<Card> cards;
}
