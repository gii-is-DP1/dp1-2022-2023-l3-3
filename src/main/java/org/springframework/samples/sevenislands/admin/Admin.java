package org.springframework.samples.sevenislands.admin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.samples.sevenislands.user.User;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
}
