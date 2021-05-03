package es.marcosaguirre.videogame.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.model.Customer;
import es.marcosaguirre.videogame.validators.EnumNamePattern;
import lombok.Data;

@Data
public class UpdatableVideoGameDto {

	@Length(max = 70, message = "Name must be under 70 characters")
	private String name;

	@EnumNamePattern(regexp = "NEW_RELEASE|STANDARD|CLASSIC", message = "Game type must be either NEW_RELEASE, STANDARD or CLASSIC")
	private GameTypes typeOfGame;

	private Boolean available = true;

	private LocalDate startRentalDate;
	
	private LocalDate endRentalDate;
	
	@JsonIgnore
	private Customer customer;

}
