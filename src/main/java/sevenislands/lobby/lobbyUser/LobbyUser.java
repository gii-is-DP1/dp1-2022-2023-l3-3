package sevenislands.lobby.lobbyUser;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import sevenislands.enums.Mode;
import sevenislands.lobby.Lobby;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;

@Getter
@Setter
@Entity
public class LobbyUser extends BaseEntity{
    
    @ManyToOne
    private Lobby lobby;

    @ManyToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    private Mode mode;
}
