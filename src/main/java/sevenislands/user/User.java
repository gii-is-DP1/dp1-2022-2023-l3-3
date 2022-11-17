package sevenislands.user;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import sevenislands.achievement.Achievement;
import sevenislands.game.turn.Turn;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseEntity {
	
	@Column(name = "nickname", unique = true, nullable = false, length = 30)
	private String nickname;

	@Column(name = "password", unique = false, nullable = false)
	private String password;

	@Column(name = "enabled", unique = false, nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;

	@Column(name = "first_name", unique = false, nullable = false)
	private String firstName;

	@Column(name = "last_name", unique = false, nullable = false)
	private String lastName;

	@Column(name = "email", unique = true, nullable = false, length = 50)
	private String email;

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "creation_date", unique = false, nullable = false, columnDefinition = "date default now()")
	private Date creationDate;
	
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "birth_date", unique = false, nullable = false)
	private Date birthDate;

	@Column(name = "avatar", unique = false, nullable = false)
	private String avatar;

	@Column(name = "type", unique = false, updatable = false)
	protected String userType;

	@ManyToMany
	@JoinColumn(name = "achievements")
    private Collection<Achievement> achievements;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
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
        User other = (User) obj;
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
