package sevenislands.user.invitation;

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
public class Invitation extends BaseEntity {

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @Enumerated(value = EnumType.STRING)
    private Mode mode;

    @ManyToOne
    private Lobby lobby;
    
}
