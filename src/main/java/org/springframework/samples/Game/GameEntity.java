package org.springframework.samples.Game;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends BaseEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDateTime endingDate;

}
