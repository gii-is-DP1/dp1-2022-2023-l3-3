package org.springframework.samples.sevenislands.game;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "islands")
public class Island extends BaseEntity {

    @ManyToOne
    @NotNull
    private Round round;

    @OneToMany()
    @Size(min = 1)
    private Set<Turn> turns;
}
