package org.springframework.samples.sevenisland.cardBaraja;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenisland.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CardTypes")
public class CardType extends BaseEntity {
    
     @NotNull
     @NotBlank
     @Column(unique = true)
     protected String name;


}
