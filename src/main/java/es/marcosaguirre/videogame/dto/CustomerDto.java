package es.marcosaguirre.videogame.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class CustomerDto {
	

	private Long id;
	
	@NotBlank(message = "Name can't be empty")
	@Length(max = 30, message = "Name must be under 30 characters")
	@Pattern(regexp = Constants.ONLY_CHARACTERS_REGEX_STRING)
	private String name;
	
	@NotBlank(message = "Last name can't be empty")
	@Length(max = 60, message = "Last name must be under 60 characters")
	@Pattern(regexp = Constants.ONLY_CHARACTERS_REGEX_STRING)
	private String lastName;
	
	@NotBlank(message = "E-mail can't be empty")
	@Length(max = 60, message = "E-mail must be under 60 characters")
	@Email(message = "E-mail must be well formatted")
	private String mail;
	
	private Integer loyaltyPoints;
	
	private List<VideoGame> rentedGames;
	
	public CustomerDto(
			@NotBlank(message = "Name can't be empty") @Length(max = 30, message = "Name must be under 30 characters") @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$") String name,
			@NotBlank(message = "Last name can't be empty") @Length(max = 60, message = "Last name must be under 60 characters") @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$") String lastName,
			@NotBlank(message = "E-mail can't be empty") @Length(max = 60, message = "E-mail must be under 60 characters") @Email(message = "E-mail must be well formatted") String mail,
			Integer loyaltyPoints, List<VideoGame> rentedGames) {
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
		this.loyaltyPoints = loyaltyPoints;
		this.rentedGames = rentedGames;
	}
	
}
