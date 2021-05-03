package es.marcosaguirre.videogame.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import es.marcosaguirre.videogame.common.OperationTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "customer_id", nullable = false)
	private Long customerId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "operation_type", nullable = false, length = 70)
	private OperationTypes operationType;
	
	@Column(name = "total_amount", nullable = false, columnDefinition =  "Double default 0.0")
	private Double totalAmount;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	
	public Operation(Long customerId, OperationTypes operationType, Double totalAmount, LocalDate date) {
		this.customerId = customerId;
		this.operationType = operationType;
		this.totalAmount = totalAmount;
		this.date = date;
	}
	
}
