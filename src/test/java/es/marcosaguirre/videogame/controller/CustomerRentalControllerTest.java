package es.marcosaguirre.videogame.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.ConfirmedRentalResponseDto;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.RentalInfoResponseDto;
import es.marcosaguirre.videogame.dto.RentalInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.model.VideoGame;
import es.marcosaguirre.videogame.service.ICustomerRentalService;

@WebMvcTest(CustomerRentalController.class)
public class CustomerRentalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ICustomerRentalService rentalService;

	@Test
	public void showRentalInfoReponseTest() throws JsonProcessingException, Exception {

		// input

		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(1L);
		gameIdentifiers.add(2L);
		Integer daysOfRental = 7;
		RentalInputDto inputDto = new RentalInputDto(gameIdentifiers, daysOfRental);

		// output

		VideoGameDto firstGame = new VideoGameDto(1L, "first", GameTypes.NEW_RELEASE);
		VideoGameDto secondGame = new VideoGameDto(2L, "second", GameTypes.CLASSIC);
		List<VideoGameDto> responseGamesList = new ArrayList<>();
		responseGamesList.add(firstGame);
		responseGamesList.add(secondGame);
		Double totalPrice = 37.0;
		Integer loyaltyPoints = 3;
		RentalInfoResponseDto rentalResponse = new RentalInfoResponseDto(responseGamesList, loyaltyPoints, totalPrice);

		Response<RentalInfoResponseDto> response = new Response<>();
		response.setData(rentalResponse);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_INFO));

		Mockito.when(rentalService.showRentalInfo(inputDto)).thenReturn(rentalResponse);

		String url = "/rentals";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());

	}

	@Test
	public void confirmRentalResponseTest() throws JsonProcessingException, Exception {
		
		// input

		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(1L);
		gameIdentifiers.add(2L);
		Integer daysOfRental = 7;
		RentalInputDto inputDto = new RentalInputDto(gameIdentifiers, daysOfRental);

		// output
		LocalDate initialDate = LocalDate.now();
		LocalDate endDate = initialDate.plusDays(daysOfRental);
		VideoGameDto firstGame = new VideoGameDto(1L, "first", GameTypes.NEW_RELEASE, false, initialDate, endDate, null);
		VideoGameDto secondGame = new VideoGameDto(2L, "second", GameTypes.CLASSIC, false, initialDate, endDate, null);
		List<VideoGameDto> responseGamesList = new ArrayList<>();
		responseGamesList.add(firstGame);
		responseGamesList.add(secondGame);
		List<VideoGame> responseGamesListEntities = VideoGameMapper.toVideoGameEntity(responseGamesList);
		Double totalPrice = 37.0;
		Integer loyaltyPoints = 3;
		
		CustomerDto customer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", loyaltyPoints, responseGamesListEntities);
		
		ConfirmedRentalResponseDto confirmedRentalResponse = new ConfirmedRentalResponseDto(totalPrice, customer);

		Response<ConfirmedRentalResponseDto> response = new Response<>();
		response.setData(confirmedRentalResponse);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_CONFIRMATION));

		Mockito.when(rentalService.confirmRentalOperation(inputDto, customer.getId())).thenReturn(confirmedRentalResponse);

		String url = "/rentals/1/confirm";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());
	}
	
	@Test
	public void listOfGamesMustNotBeBlank() throws JsonProcessingException, Exception {
		
		Integer daysOfRental = 7;
		
		RentalInputDto inputDto = new RentalInputDto(null, daysOfRental);
		
		String url = "/rentals";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(rentalService, times(0)).showRentalInfo(inputDto);
		
		String urlConfirm = "/rentals/1/confirm";
		mockMvc.perform(
				post(urlConfirm)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(rentalService, times(0)).confirmRentalOperation(inputDto, 1L);
	}
	
	@Test
	public void daysOfRentalMustNotBeBlank() throws JsonProcessingException, Exception {
		
		List<Long> listOfGamesIdentifiers = new ArrayList<>();
		listOfGamesIdentifiers.add(1L);
		
		RentalInputDto inputDto = new RentalInputDto(listOfGamesIdentifiers, null);
		
		String url = "/rentals";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(rentalService, times(0)).showRentalInfo(inputDto);
		
		String urlConfirm = "/rentals/1/confirm";
		mockMvc.perform(
				post(urlConfirm)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(rentalService, times(0)).confirmRentalOperation(inputDto, 1L);
	}

}
