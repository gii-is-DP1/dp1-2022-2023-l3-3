package sevenislands.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.Past;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.format.annotation.DateTimeFormat;

import sevenislands.configuration.AuditableEntity;
import sevenislands.enums.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
public class User extends AuditableEntity {
	
	@Column(name = "nickname", unique = true, nullable = false, length = 30)
	private String nickname;

    @NotAudited
	@Column(name = "password", unique = false, nullable = false)
	private String password;

	@Column(name = "enabled", unique = false, nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;

    @NotAudited
	@Column(name = "first_name", unique = false, nullable = false)
	private String firstName;
    
    @NotAudited
	@Column(name = "last_name", unique = false, nullable = false)
	private String lastName;

    @NotAudited
	@Column(name = "email", unique = true, nullable = false, length = 50)
	private String email;

    @NotAudited
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "creation_date", unique = false, nullable = false, columnDefinition = "date default now()")
	private Date creationDate;
	
    @NotAudited
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "birth_date", unique = false, nullable = false)
	private Date birthDate;

    @NotAudited
	@Column(name = "avatar", unique = false, nullable = false)
	private String avatar;

    @NotAudited
	@Column(name = "type", unique = false, updatable = false)
	@Enumerated(value = EnumType.STRING)
	protected UserType userType;

	public User copy(){
        User cloneUser = new User();
        cloneUser.setId(this.getId());
        cloneUser.setAvatar(this.getAvatar());
        cloneUser.setBirthDate(this.getBirthDate());
        cloneUser.setCreationDate(this.getCreationDate());
        cloneUser.setEmail(this.getEmail());
        cloneUser.setEnabled(this.isEnabled());
        cloneUser.setFirstName(this.getFirstName());
        cloneUser.setLastName(this.getLastName());
        cloneUser.setNickname(this.getNickname());
        cloneUser.setPassword(this.getPassword());
        cloneUser.setUserType(this.getUserType());
        return cloneUser;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
        result = prime * result + ((userType == null) ? 0 : userType.hashCode());
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
        if (nickname == null) {
            if (other.nickname != null)
                return false;
        } else if (!nickname.equals(other.nickname))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (enabled != other.enabled)
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
        if (avatar == null) {
            if (other.avatar != null)
                return false;
        } else if (!avatar.equals(other.avatar))
            return false;
        if (userType != other.userType)
            return false;
        return true;
    }

}
