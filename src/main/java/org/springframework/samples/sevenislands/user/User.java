package org.springframework.samples.sevenislands.user;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.samples.petclinic.model.Person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User extends Person {

	@Column(unique = true, nullable = false, length = 30)
	private String nickname;

	@Column(unique = true, nullable = false, length = 50)
	private String email;

	@Column(unique = false, nullable = false)
	private String password;

	@Past
	@Temporal(TemporalType.DATE)
	private LocalDate creationDate;

}
