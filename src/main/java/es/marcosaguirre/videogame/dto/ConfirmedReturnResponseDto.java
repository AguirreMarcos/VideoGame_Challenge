package es.marcosaguirre.videogame.dto;

import java.util.List;

import lombok.Data;

@Data
public class ConfirmedReturnResponseDto {
	
	private Double totalRecharge;
	private List<VideoGameDto> returnedGames;
	private CustomerDto customer;

}
