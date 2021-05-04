package es.marcosaguirre.videogame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.dto.mapper.OperationMapper;
import es.marcosaguirre.videogame.model.Operation;
import es.marcosaguirre.videogame.repository.IOperationRepo;
import es.marcosaguirre.videogame.service.impl.OperationServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class OperationServiceTest {
	
	@MockBean
	private IOperationRepo repo;
	
	@InjectMocks
	private IOperationService operationService = new OperationServiceImpl();
	
	@Test
	public void registerTest() throws BaseException {
		
		OperationDto inputDto = new OperationDto(1L, OperationTypes.RENTAL, 10.0, LocalDate.now());

		operationService.register(inputDto);

		verify(repo, times(1)).save(OperationMapper.toOperationEntity(inputDto));
	}

	@Test
	public void getAllOperationTest() throws BaseException {

		Operation firstOperation = new Operation(1L, OperationTypes.RENTAL, 10.0, LocalDate.now());
		Operation secondOperation = new Operation(2L, OperationTypes.RETURN, 12.0, LocalDate.now());
		List<Operation> listOfOperations = new ArrayList<>();
		listOfOperations.add(firstOperation);
		listOfOperations.add(secondOperation);

		Mockito.when(repo.findAll()).thenReturn(listOfOperations);

		List<OperationDto> operationsList = operationService.getAll();

		assertEquals(2, operationsList.size());
		verify(repo, times(1)).findAll();
	}

	@Test
	public void getByIdTest() throws BaseException {
		Operation operation = new Operation(1L, OperationTypes.RENTAL, 10.0, LocalDate.now());
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(operation));
		
		OperationDto myOperation = operationService.getById(1L);
		
		assertEquals(OperationTypes.RENTAL, myOperation.getOperationType());
		assertEquals(10.0, myOperation.getTotalAmount());
		
	}
	
	@Test
	public void deleteTest() throws BaseException {
		Operation operation = new Operation(1L, 1L, OperationTypes.RENTAL, 10.0, LocalDate.now());
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(operation));
		operationService.delete(1L);
		verify(repo, times(1)).deleteById(operation.getId());
	}
}
