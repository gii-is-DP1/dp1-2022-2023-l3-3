package org.springframework.samples.sevenisland.Goal;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.sevenisland.Enums.TipoLogro;
import org.springframework.samples.sevenisland.model.BaseEntity;

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
