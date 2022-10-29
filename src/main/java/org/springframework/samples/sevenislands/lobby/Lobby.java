package org.springframework.samples.sevenislands.lobby;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Lobby extends BaseEntity{
    

    @NotNull
    protected boolean active;
}
