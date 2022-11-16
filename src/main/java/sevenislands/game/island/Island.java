package sevenislands.game.island;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "islands")
public class Island extends BaseEntity {

    // número para saber de qué isla se trata
    @Column(name = "island_number", nullable = false)
    @Min(value = 1)
    @Max(value = 7)
    private Integer num;
}
