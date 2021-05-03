package es.marcosaguirre.videogame.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReturnInfoResponseDto {
	private Double totalRecharge;
	private List<VideoGameDto> returnedGames;
}
