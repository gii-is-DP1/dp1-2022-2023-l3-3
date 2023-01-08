package sevenislands.user.friend;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import sevenislands.enums.Status;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;

@Getter
@Setter
@Entity
public class Friend extends BaseEntity {
    
    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
