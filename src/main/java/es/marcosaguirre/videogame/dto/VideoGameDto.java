package es.marcosaguirre.videogame.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.model.Customer;
import es.marcosaguirre.videogame.validators.EnumNamePattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoGameDto {


	private Long id;

	@NotBlank(message = "Name can't be empty")
	@Length(max = 70, message = "Name must be under 70 characters")
	@NotNull
	private String name;

	@NotNull
	@EnumNamePattern(regexp = "NEW_RELEASE|STANDARD|CLASSIC", message = "Game type must be either NEW_RELEASE, STANDARD or CLASSIC")
	private GameTypes typeOfGame;

	private boolean available = true;

	private LocalDate startRentalDate;
	
	private LocalDate endRentalDate;
	
	@JsonIgnore
	private Customer customer;
	
	public VideoGameDto(Long id,
			@NotBlank(message = "Name can't be empty") @Length(max = 70, message = "Name must be under 70 characters") @NotNull String name,
			@NotNull @EnumNamePattern(regexp = "NEW_RELEASE|STANDARD|CLASSIC", message = "Game type must be either NEW_RELEASE, STANDARD or CLASSIC") GameTypes typeOfGame) {
		this.id = id;
		this.name = name;
		this.typeOfGame = typeOfGame;
	}
	
	public VideoGameDto(
			@NotBlank(message = "Name can't be empty") @Length(max = 70, message = "Name must be under 70 characters") @NotNull String name,
			@NotNull @EnumNamePattern(regexp = "NEW_RELEASE|STANDARD|CLASSIC", message = "Game type must be either NEW_RELEASE, STANDARD or CLASSIC") GameTypes typeOfGame) {
		this.name = name;
		this.typeOfGame = typeOfGame;
	}
	
	
}
