package es.marcosaguirre.videogame.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.model.VideoGame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatableCustomerDto {

	@Length(max = 30, message = "Name must be under 30 characters")
	@Pattern(regexp = Constants.ONLY_CHARACTERS_REGEX_STRING)
	private String name;
	
	@Length(max = 60, message = "Last name must be under 60 characters")
	@Pattern(regexp = Constants.ONLY_CHARACTERS_REGEX_STRING)
	private String lastName;
	
	@Length(max = 60, message = "E-mail must be under 60 characters")
	@Email(message = "E-mail must be well formatted")
	private String mail;
	
	private Integer loyaltyPoints;
	
	private List<VideoGame> rentedGames;
	
}
