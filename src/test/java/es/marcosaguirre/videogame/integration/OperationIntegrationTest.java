package es.marcosaguirre.videogame.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.dto.RentalInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.service.ICustomerRentalService;
import es.marcosaguirre.videogame.service.ICustomerService;
import es.marcosaguirre.videogame.service.IOperationService;
import es.marcosaguirre.videogame.service.IVideoGameService;

@SpringBootTest
@AutoConfigureMockMvc
public class OperationIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ICustomerRentalService rentalService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IOperationService operationService;
	
	@Autowired
	private IVideoGameService gameService;
	
	@Test
	public void getOperationsTest() throws JsonProcessingException, Exception {
		
		Integer daysOfRental = 5;
		Double expectedPrice = 20.0;
		
		VideoGameDto game = gameService.register(new VideoGameDto("first game", GameTypes.NEW_RELEASE));
		CustomerDto customer = customerService.register(new CustomerDto("Marcos", "Aguirre", "maguirre@hotmail.com", 0, null));
		
		List<Long> gameIds = new ArrayList<>();
		gameIds.add(game.getId());
		
		RentalInputDto rentalInput = new RentalInputDto(gameIds, daysOfRental);
		
		rentalService.confirmRentalOperation(rentalInput, customer.getId());
		
		List<OperationDto> operationsList = new ArrayList<>();
		
		OperationDto expectedOperation = new OperationDto(1L, customer.getId(), OperationTypes.RENTAL, expectedPrice, LocalDate.now());
		
		operationsList.add(expectedOperation);
		
		Response<List<OperationDto>> response = new Response<>();
		response.setData(operationsList);
		
		String url = "/operations";
		
		mockMvc.perform(
				get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
		
		gameService.delete(game.getId());
		customerService.delete(customer.getId());
		for(OperationDto operation: operationsList) {
			operationService.delete(operation.getId());
		}
	}
	
	@Test
	public void getOperationByIdTest() throws JsonProcessingException, Exception {
		
		for(VideoGameDto game : gameService.getAll()) {
			gameService.delete(game.getId());
		}
		
		for(CustomerDto customer: customerService.getAll()) {
			customerService.delete(customer.getId());
		}
		
		Integer daysOfRental = 5;
		Double expectedPrice = 20.0;
		
		VideoGameDto game = gameService.register(new VideoGameDto("first game", GameTypes.NEW_RELEASE));
		CustomerDto customer = customerService.register(new CustomerDto("Marcos", "Aguirre", "maguirre@hotmail.com", 0, null));
		
		List<Long> gameIds = new ArrayList<>();
		gameIds.add(game.getId());
		
		RentalInputDto rentalInput = new RentalInputDto(gameIds, daysOfRental);
		
		rentalService.confirmRentalOperation(rentalInput, customer.getId());
		
		OperationDto newOperation = new OperationDto(customer.getId(), OperationTypes.RENTAL, expectedPrice, LocalDate.now());
		
		OperationDto expectedOperation = operationService.register(newOperation);
		Response<OperationDto> response = new Response<>();
		response.setData(expectedOperation);
		
		String url = "/operations/" + expectedOperation.getId();
		
		mockMvc.perform(
				get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
		
		operationService.delete(expectedOperation.getId());
		gameService.delete(game.getId());
		customerService.delete(customer.getId());

	}
	
}
