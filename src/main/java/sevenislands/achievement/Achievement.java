package sevenislands.achievement;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;



import sevenislands.enums.AchievementType;
import sevenislands.model.NamedEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que hace referencia a los logros que los jugadores pueden ganar por jugar.
 */
@Getter
@Setter
@Entity
public class Achievement extends NamedEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "achievement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AchievementType achievementType;
   
    @Column(name = "threshold", nullable = false)
    private Integer threshold;

    @Column(name = "badge_image", nullable = false)
    private String badgeImage;
}
