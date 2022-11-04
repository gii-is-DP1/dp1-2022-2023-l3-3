package org.springframework.samples.sevenislands.player;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.samples.sevenislands.model.BaseEntity;
import org.springframework.samples.sevenislands.model.Person;
import org.springframework.samples.sevenislands.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(name = "players")
@DiscriminatorValue("player")
public class Player extends User{
	/**@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nickname", referencedColumnName = "nickname")
	private User user;**/
}
