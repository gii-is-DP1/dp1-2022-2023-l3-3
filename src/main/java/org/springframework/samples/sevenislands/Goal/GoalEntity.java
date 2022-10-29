package org.springframework.samples.sevenislands.Goal;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.sevenislands.Enums.TipoLogro;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GoalEntity extends BaseEntity{
    
    @NotEmpty
    protected String name;
    @NotEmpty
    protected String description;
    @NotEmpty
    protected TipoLogro tipoLogro;

}
