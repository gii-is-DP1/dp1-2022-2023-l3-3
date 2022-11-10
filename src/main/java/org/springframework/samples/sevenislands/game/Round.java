package org.springframework.samples.sevenislands.game;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rounds")
public class Round extends BaseEntity {

    @ManyToOne
    @NotNull
    private Game game;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "round", fetch = FetchType.LAZY)
    private List<Turn> turns;
}
