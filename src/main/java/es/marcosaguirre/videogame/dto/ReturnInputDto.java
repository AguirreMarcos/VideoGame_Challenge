package es.marcosaguirre.videogame.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnInputDto {
	@NotNull(message = "List of gameIdentifiers can't be empty")
	private List<Long> gameIdentifiers;
}
