package org.springframework.samples.sevenislands.admin;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.samples.sevenislands.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
    /*@Column(name = "avatar", unique = false, nullable = true)
	String avatar;*/
}
