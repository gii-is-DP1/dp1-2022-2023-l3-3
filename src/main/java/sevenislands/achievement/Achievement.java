package sevenislands.achievement;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import sevenislands.enums.AchievementType;
import sevenislands.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que hace referencia a los logros que los jugadores pueden ganar por jugar.
 */
@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "achievementType", nullable = false)
    @Enumerated(EnumType.STRING)
    private AchievementType achievementType;
}
