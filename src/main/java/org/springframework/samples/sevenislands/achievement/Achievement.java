package org.springframework.samples.sevenislands.achievement;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends NamedEntity{
    
    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "achievementType")
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private AchievementType achievementType;

    /*@ManyToMany(mappedBy = "achievements")
    private Collection<Player> players;*/

}
