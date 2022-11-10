package org.springframework.samples.sevenislands.card;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.samples.sevenislands.game.Island;
import org.springframework.samples.sevenislands.game.Turn;
import org.springframework.samples.sevenislands.model.BaseEntity;

public class Card extends BaseEntity {
    
    @ManyToOne
    @NotNull
    private CardType cardType;

    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cards")
    // @JoinTable(
    //     name = "turn_card",
    //     joinColumns = {@JoinColumn(name = "turn_id")},
    //     inverseJoinColumns = {@JoinColumn(name = "card_id")}
    // )
    // private Set<Turn> turns;

    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cards")
    // @JoinTable(
    //     name = "card_island",
    //     joinColumns = {@JoinColumn(name = "card_id")},
    //     inverseJoinColumns = {@JoinColumn(name = "island_id")}
    // )
    // private Set<Island> islands;
}
