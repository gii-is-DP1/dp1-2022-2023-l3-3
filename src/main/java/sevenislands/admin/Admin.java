package sevenislands.admin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sevenislands.user.User;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {

}
