package es.marcosaguirre.videogame.dto;

import java.util.List;

import lombok.Data;

@Data
public class RentalInfoResponseDto {
	private List<VideoGameDto> games;
	private Integer loyaltyPoints;
	private Double totalPrice;
}
