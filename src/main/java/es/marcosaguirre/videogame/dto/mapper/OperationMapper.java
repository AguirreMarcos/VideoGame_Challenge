package es.marcosaguirre.videogame.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.model.Operation;

public class OperationMapper {
	public static List<OperationDto> toOperationDto(List<Operation> operations) {
		List<OperationDto> dtos = new ArrayList<>();
		if (operations!= null) {
			for (Operation operation : operations) {
				dtos.add(toOperationDto(operation));
			}
		}
		return dtos;
	}

	public static OperationDto toOperationDto(Operation operation) {
		OperationDto dto = new OperationDto();
		if(operation != null) {
			dto.setId(operation.getId());
			dto.setCustomerId(operation.getCustomerId());
			dto.setOperationType(operation.getOperationType());
			dto.setTotalAmount(operation.getTotalAmount());
			dto.setDate(operation.getDate());
		}
		return dto;
	}
	
	public static List<Operation> toOperationEntity(List<OperationDto> dtos) {
		List<Operation> operations = new ArrayList<>();
		if (dtos != null) {
			for (OperationDto dto : dtos) {
				operations.add(toOperationEntity(dto));
			}
		}
		return operations;
	}
	
	public static Operation toOperationEntity(OperationDto operationDto) {
		Operation operationEntity = new Operation();
		if(operationDto != null) {
			if(operationDto.getId() != null) {
				operationEntity.setId(operationDto.getId());				
			}
			operationEntity.setCustomerId(operationDto.getCustomerId());
			operationEntity.setOperationType(operationDto.getOperationType());
			operationEntity.setTotalAmount(operationDto.getTotalAmount());
			operationEntity.setDate(operationDto.getDate());			
		}
		return operationEntity;
	}
}