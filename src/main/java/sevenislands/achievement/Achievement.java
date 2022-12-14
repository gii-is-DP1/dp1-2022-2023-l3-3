package sevenislands.achievement;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

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
<<<<<<< HEAD
public class Achievement extends NamedEntity {
=======
public class Achievement extends BaseEntity {
   
    @Size(min = 3, max = 50)
	@Column(name = "name")
	private String name;
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722

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
