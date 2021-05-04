package es.marcosaguirre.videogame.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.service.IOperationService;

@WebMvcTest(OperationController.class)
public class OperationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private IOperationService operationService;
	
	@Test
	public void getOperationsTest() throws JsonProcessingException, Exception {
		
		Long operationId = 1L;
		Long customerId = 1L;
		Double firstTotalAmount = 10.0;
		Double secondTotalAmount = 15.0;
		LocalDate firstDate = LocalDate.now().minusDays(4);
		LocalDate secondDate = LocalDate.now();
		
		OperationDto firstOperation = new OperationDto(operationId, customerId, OperationTypes.RENTAL, firstTotalAmount, firstDate);
		OperationDto secondOperation = new OperationDto(operationId + 1, customerId, OperationTypes.RETURN, secondTotalAmount, secondDate);
		List<OperationDto> operationsList = new ArrayList<>();
		
		operationsList.add(firstOperation);
		operationsList.add(secondOperation);
		
		Response<List<OperationDto>> response = new Response<>();
		response.setData(operationsList);
		
		Mockito.when(operationService.getAll()).thenReturn(operationsList);
		
		String url = "/operations";
		
		mockMvc.perform(
				get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
	}
	
	@Test
	public void getOperationByIdTest() throws JsonProcessingException, Exception {
		Long operationId = 1L;
		Long customerId = 1L;
		Double firstTotalAmount = 10.0;
		LocalDate firstDate = LocalDate.now().minusDays(4);
		
		OperationDto operation = new OperationDto(operationId, customerId, OperationTypes.RENTAL, firstTotalAmount, firstDate);
		
		Response<OperationDto> response = new Response<>();
		response.setData(operation);
		
		Mockito.when(operationService.getById(operationId)).thenReturn(operation);
		
		String url = "/operations/" + operationId;
		
		mockMvc.perform(
				get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
	
	}
}
