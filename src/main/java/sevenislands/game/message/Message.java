package sevenislands.game.message;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

import lombok.Getter;
import lombok.Setter;
import sevenislands.game.Game;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;

@Getter
@Setter
@Entity
public class Message extends BaseEntity{
    

    @Max(32)
    private String message;

    @ManyToOne
    private User sender;

    @ManyToOne
    private Game game;
}
