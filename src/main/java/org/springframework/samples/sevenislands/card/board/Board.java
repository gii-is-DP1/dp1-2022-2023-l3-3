package org.springframework.samples.sevenislands.card.board;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import org.springframework.samples.sevenislands.card.Card;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "board")
public class Board extends BaseEntity {

    String background;

    @Positive
    Integer width;

    @Positive
    Integer height;

    public Board() {
        this.background = "resources/images/Tablero_recortado.jpg";
        this.width = 1903;
        this.height = 2325;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", fetch = FetchType.EAGER)
    List<Card> cards;
}
