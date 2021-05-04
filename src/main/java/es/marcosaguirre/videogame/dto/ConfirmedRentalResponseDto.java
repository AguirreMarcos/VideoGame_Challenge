package es.marcosaguirre.videogame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedRentalResponseDto {
	
	private Double totalPayment;
	private CustomerDto customer;
	
}
