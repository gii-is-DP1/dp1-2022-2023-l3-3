package sevenislands.card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import sevenislands.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "CardTypes")
public class CardType extends BaseEntity {

     @NotNull
     @NotBlank
     @Column(unique = true)
     protected String name;

     @NotNull
     protected Integer multiplicity;

     // falta el enum del tipo de carta

}
