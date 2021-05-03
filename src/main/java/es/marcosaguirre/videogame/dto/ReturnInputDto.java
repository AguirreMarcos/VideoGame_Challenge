package es.marcosaguirre.videogame.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReturnInputDto {
	@NotNull(message = "List of gameIdentifiers can't be empty")
	private List<Long> gameIdentifiers;
}
