package es.marcosaguirre.videogame.dto;

import java.time.LocalDate;

import es.marcosaguirre.videogame.common.OperationTypes;
import lombok.Data;

@Data
public class OperationDto {
	
	private Long id;
	
	private Long customerId;
	
	private OperationTypes operationType;
	
	private Double totalAmount;
	
	private LocalDate date;
}
