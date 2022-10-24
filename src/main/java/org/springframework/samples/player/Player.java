package org.springframework.samples.player;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.Person;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends Person {
    
    public Player(String name, String nickname, String email, String password, Date birthDate, String avatar) {
        super(name, nickname, email, password);
        this.avatar = avatar;
        this.birthDate = birthDate;
    }

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull
    protected Date birthDate;

    @NotEmpty
    protected String avatar;
}
