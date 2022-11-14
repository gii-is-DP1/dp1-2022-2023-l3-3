package sevenislands.admin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sevenislands.user.User;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
    //Esta entidad está vacía ya que "extiende de user", aunque en realidad en la base de datos
    //se establece como una única tabla para user, admin y player
}
