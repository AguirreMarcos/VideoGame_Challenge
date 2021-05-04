package es.marcosaguirre.videogame.dto;

import java.time.LocalDate;

import es.marcosaguirre.videogame.common.OperationTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {
	

	private Long id;
	
	private Long customerId;
	
	private OperationTypes operationType;
	
	private Double totalAmount;
	
	private LocalDate date;
	
	public OperationDto(Long customerId, OperationTypes operationType, Double totalAmount, LocalDate date) {
		this.customerId = customerId;
		this.operationType = operationType;
		this.totalAmount = totalAmount;
		this.date = date;
	}
	
	
	
}
