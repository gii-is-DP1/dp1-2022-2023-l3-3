package sevenislands.player;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import sevenislands.user.User;
import sevenislands.achievement.Achievement;
import sevenislands.game.turn.Turn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("player")
public class Player extends User {
    
    @ManyToMany
	@JoinColumn(name = "achievements")
    private Collection<Achievement> achievements;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Turn> turns;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((achievements == null) ? 0 : achievements.hashCode());
        result = prime * result + ((turns == null) ? 0 : turns.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (achievements == null) {
            if (other.achievements != null)
                return false;
        } else if (!achievements.equals(other.achievements))
            return false;
        if (turns == null) {
            if (other.turns != null)
                return false;
        } else if (!turns.equals(other.turns))
            return false;
        return true;
    }
}