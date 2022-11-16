package sevenislands.lobby;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lobby")

public class Lobby extends BaseEntity {

  @Column(name = "code", unique = true, nullable = false)
  private String code;

  @Column(name = "active", unique = false, nullable = false)
  private boolean active;

  @ManyToMany(cascade = CascadeType.PERSIST)
  private List<User> users;

  public List<User> getPlayerInternal() {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    return this.users;
  }

  public void addPlayer(User user) {
    getPlayerInternal().add(user);
  }
}
