package es.marcosaguirre.videogame.dto;

import lombok.Data;

@Data
public class ConfirmedRentalResponseDto {
	private Double totalPayment;
	private CustomerDto customer;
}
