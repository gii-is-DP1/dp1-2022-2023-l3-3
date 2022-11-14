package sevenislands.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;
import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User extends BaseEntity {
	
	//En caso de cambiar cualquier atribute con unique = true
	//Cambiar también la comprobación en el signUp
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

	@Column(name = "type", insertable = false, updatable = false)
	protected String userType;
}
