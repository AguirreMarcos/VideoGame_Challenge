package es.marcosaguirre.videogame.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedReturnResponseDto {
	
	private Double totalRecharge;
	private List<VideoGameDto> returnedGames;
	private CustomerDto customer;

}
