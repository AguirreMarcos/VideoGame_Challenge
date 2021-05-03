package es.marcosaguirre.videogame.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalInfoResponseDto {
	private List<VideoGameDto> games;
	private Integer loyaltyPoints;
	private Double totalPrice;
}
