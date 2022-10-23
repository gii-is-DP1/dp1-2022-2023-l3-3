package org.springframework.samples.sevenislands.game;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime creationDate;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime endingDate;

}
