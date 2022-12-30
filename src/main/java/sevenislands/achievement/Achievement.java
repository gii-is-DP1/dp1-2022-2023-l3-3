package sevenislands.achievement;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

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
public class Achievement extends BaseEntity {
   
    @Size(min = 3, max = 50)
	@Column(name = "name")
	private String name;

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
