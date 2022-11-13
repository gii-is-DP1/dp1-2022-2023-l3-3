package sevenislands.achievement;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import sevenislands.model.BaseEntity;

import sevenislands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends BaseEntity {

    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "achievementType")
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private AchievementType achievementType;

    @ManyToMany()
    @JoinColumn(name = "players")
    private Collection<Player> players;
}
