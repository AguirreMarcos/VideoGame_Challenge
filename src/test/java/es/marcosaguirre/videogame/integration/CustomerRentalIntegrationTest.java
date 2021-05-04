package es.marcosaguirre.videogame.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import es.marcosaguirre.videogame.service.ICustomerService;
import es.marcosaguirre.videogame.service.IVideoGameService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerRentalIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private IVideoGameService gameService;
	
	@Autowired
	private ICustomerService customerService;

	@Test
	public void showRentalInfoResponseTest() throws JsonProcessingException, Exception {
		
		// DB
		VideoGameDto first = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE));
		VideoGameDto second = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC));
			
		// input

		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(first.getId());
		gameIdentifiers.add(second.getId());
		Integer daysOfRental = 7;
		RentalInputDto inputDto = new RentalInputDto(gameIdentifiers, daysOfRental);

		// output

		VideoGameDto firstGame = new VideoGameDto(first.getId(), "first", GameTypes.NEW_RELEASE);
		VideoGameDto secondGame = new VideoGameDto(second.getId(), "second", GameTypes.CLASSIC);
		List<VideoGameDto> responseGamesList = new ArrayList<>();
		responseGamesList.add(firstGame);
		responseGamesList.add(secondGame);
		Double totalPrice = 37.0;
		Integer loyaltyPoints = 3;
		RentalInfoResponseDto rentalResponse = new RentalInfoResponseDto(responseGamesList, loyaltyPoints, totalPrice);

		Response<RentalInfoResponseDto> response = new Response<>();
		response.setData(rentalResponse);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_INFO));

		String url = "/rentals";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());
		
		for(VideoGameDto game : responseGamesList) {
			gameService.delete(game.getId());
		}
	}
	
	@Test
	public void confirmRentalResponseTest() throws JsonProcessingException, Exception {
		
		// DB
		CustomerDto dbCustomer = customerService.register(new CustomerDto("Marcos", "Aguirre", "maguirre@gmail.com", 0, null));
		VideoGameDto first = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE));
		VideoGameDto second = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC));
		
		// input

		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(first.getId());
		gameIdentifiers.add(second.getId());
		Integer daysOfRental = 7;
		RentalInputDto inputDto = new RentalInputDto(gameIdentifiers, daysOfRental);
		
		
		// output
		LocalDate initialDate = LocalDate.now();
		LocalDate endDate = initialDate.plusDays(daysOfRental);
		VideoGameDto firstGame = new VideoGameDto(first.getId(), "first", GameTypes.NEW_RELEASE, false, initialDate, endDate, null);
		VideoGameDto secondGame = new VideoGameDto(second.getId(), "second", GameTypes.CLASSIC, false, initialDate, endDate, null);
		List<VideoGameDto> responseGamesList = new ArrayList<>();
		responseGamesList.add(firstGame);
		responseGamesList.add(secondGame);
		
		List<VideoGame> responseGamesListEntities = VideoGameMapper.toVideoGameEntity(responseGamesList);
		Double totalPrice = 37.0;
		Integer loyaltyPoints = 3;
		
		
		CustomerDto customer = new CustomerDto(dbCustomer.getId(), "Marcos", "Aguirre", "maguirre@gmail.com", loyaltyPoints, responseGamesListEntities);
		
		ConfirmedRentalResponseDto confirmedRentalResponse = new ConfirmedRentalResponseDto(totalPrice, customer);

		Response<ConfirmedRentalResponseDto> response = new Response<>();
		response.setData(confirmedRentalResponse);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_CONFIRMATION));

		String url = "/rentals/" + dbCustomer.getId() + "/confirm";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());
		
		gameService.delete(first.getId());
		gameService.delete(second.getId());
		customerService.delete(dbCustomer.getId());
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
		
		String urlConfirm = "/rentals/1/confirm";
		mockMvc.perform(
				post(urlConfirm)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
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
		
		String urlConfirm = "/rentals/1/confirm";
		mockMvc.perform(
				post(urlConfirm)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
	}
	
}
