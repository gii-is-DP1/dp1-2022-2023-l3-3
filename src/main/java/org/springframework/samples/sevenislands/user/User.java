package org.springframework.samples.sevenislands.user;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.sevenislands.achievement.Achievement;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User extends BaseEntity {
	@Column(name = "nickname", unique = true, nullable = false, length = 30)
	String nickname;

	@Column(name = "password", unique = false, nullable = false)
	String password;

	@Column(name = "enabled", unique = false, nullable = true, columnDefinition = "boolean default true")
	boolean enabled;

	@Column(name = "first_name", unique = false, nullable = false)
	protected String firstName;

	@Column(name = "last_name", unique = false, nullable = false)
	protected String lastName;

	@Column(name = "email", unique = true, nullable = false, length = 50)
	String email;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", unique = false, nullable = true, columnDefinition = "date default now()")
	Date creationDate;
	
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "birth_date", unique = false, nullable = false)
	Date birthDate;

	@Column(name = "avatar", unique = false, nullable = true)
	String avatar;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;

	@Column(name = "type", insertable = false, updatable = false)
	protected String userType;

	@ManyToMany()
	@JoinColumn(name = "achievements")
    private Collection<Achievement> achievements;

	@ManyToOne()
	private Lobby lobby;
}
