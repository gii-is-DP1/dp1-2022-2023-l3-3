package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends BaseEntity {
        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime endingDate;
    
}
