package es.marcosaguirre.videogame.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import es.marcosaguirre.videogame.dto.ConfirmedReturnResponseDto;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.ReturnInfoResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.CustomerMapper;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.model.Customer;
import es.marcosaguirre.videogame.model.VideoGame;
import es.marcosaguirre.videogame.service.ICustomerReturnService;

@WebMvcTest(CustomerReturnController.class)
public class CustomerReturnControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ICustomerReturnService returnService;

	@Test
	public void showPartialReturnInfoResponseTest() throws JsonProcessingException, Exception {

		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// identifiers
		Long firstIdentifier = 2L;
		Long secondIdentifier = 3L;
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Integer loyaltyPoints = 3;
		Double expectedRecharche = 8.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		CustomerDto customer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirre@gmail.com", loyaltyPoints, null);
		Customer entityCustomer = CustomerMapper.toCustomerEntity(customer);

		// games

		VideoGameDto firstGame = new VideoGameDto(firstIdentifier, "first", GameTypes.NEW_RELEASE, false, initialDate,
				endDate, entityCustomer);
		VideoGameDto secondGame = new VideoGameDto(secondIdentifier, "second", GameTypes.CLASSIC, false, initialDate,
				endDate, entityCustomer);
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));

		customer.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.add(firstGame);

		// input
		ReturnInputDto inputDto = new ReturnInputDto(gameIdentifiers);

		// response
		ReturnInfoResponseDto returnInfo = new ReturnInfoResponseDto(expectedRecharche, returnedGames);

		Response<ReturnInfoResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customer.getId()));

		Mockito.when(returnService.showPartialReturnInfo(inputDto, customer.getId())).thenReturn(returnInfo);

		String url = "/returns/partial/" + customer.getId();
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());

	}

	@Test
	public void showTotalReturnInfoResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// identifiers
		Long firstIdentifier = 2L;
		Long secondIdentifier = 3L;
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Integer loyaltyPoints = 3;
		Double expectedRecharche = 14.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		CustomerDto customer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirre@gmail.com", loyaltyPoints, null);
		Customer entityCustomer = CustomerMapper.toCustomerEntity(customer);

		// games

		VideoGameDto firstGame = new VideoGameDto(firstIdentifier, "first", GameTypes.NEW_RELEASE, false, initialDate,
				endDate, entityCustomer);
		VideoGameDto secondGame = new VideoGameDto(secondIdentifier, "second", GameTypes.CLASSIC, false, initialDate,
				endDate, entityCustomer);
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));

		customer.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.addAll(VideoGameMapper.toVideoGameDto(listOfRentedGames));

		// response
		ReturnInfoResponseDto returnInfo = new ReturnInfoResponseDto(expectedRecharche, returnedGames);

		Response<ReturnInfoResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customer.getId()));

		Mockito.when(returnService.showTotalReturnInfo(customer.getId())).thenReturn(returnInfo);

		String url = "/returns/total/" + customer.getId();
		mockMvc.perform(post(url).contentType("application/json")).andExpect(status().isOk())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());
	}

	@Test
	public void confirmPartialReturnResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// identifiers
		Long firstIdentifier = 2L;
		Long secondIdentifier = 3L;
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Integer loyaltyPoints = 3;
		Double expectedRecharche = 8.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		CustomerDto customer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirre@gmail.com", loyaltyPoints, null);
		Customer entityCustomer = CustomerMapper.toCustomerEntity(customer);

		// games

		VideoGameDto firstGame = new VideoGameDto(firstIdentifier, "first", GameTypes.NEW_RELEASE, false, initialDate,
				endDate, entityCustomer);
		VideoGameDto secondGame = new VideoGameDto(secondIdentifier, "second", GameTypes.CLASSIC, false, initialDate,
				endDate, entityCustomer);
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));

		customer.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.add(firstGame);

		// input
		ReturnInputDto inputDto = new ReturnInputDto(gameIdentifiers);

		// response
		
		for(VideoGameDto game : returnedGames) {
			game.setAvailable(true);
			game.setStartRentalDate(null);
			game.setEndRentalDate(null);
			game.setCustomer(null);
		}
		
		List<VideoGame> remainingGames = new ArrayList<>();
		remainingGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		
		customer.setRentedGames(remainingGames);
		
		ConfirmedReturnResponseDto returnInfo = new ConfirmedReturnResponseDto(expectedRecharche, returnedGames,
				customer);

		Response<ConfirmedReturnResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customer.getId()));

		Mockito.when(returnService.confirmPartialReturn(inputDto, customer.getId())).thenReturn(returnInfo);

		String url = "/returns/partial/" + customer.getId() + "/confirm";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());
	}

	@Test
	public void confirmTotalReturnResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// identifiers
		Long firstIdentifier = 2L;
		Long secondIdentifier = 3L;
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Integer loyaltyPoints = 3;
		Double expectedRecharche = 14.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		CustomerDto customer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirre@gmail.com", loyaltyPoints, null);
		Customer entityCustomer = CustomerMapper.toCustomerEntity(customer);

		// games
		VideoGameDto firstGame = new VideoGameDto(firstIdentifier, "first", GameTypes.NEW_RELEASE, false, initialDate,
				endDate, entityCustomer);
		VideoGameDto secondGame = new VideoGameDto(secondIdentifier, "second", GameTypes.CLASSIC, false, initialDate,
				endDate, entityCustomer);
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));

		customer.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.addAll(VideoGameMapper.toVideoGameDto(listOfRentedGames));
		
		for(VideoGameDto game: returnedGames) {
			game.setAvailable(true);
			game.setCustomer(null);
			game.setStartRentalDate(null);
			game.setEndRentalDate(null);
		}
		
		customer.setRentedGames(Collections.emptyList());

		// response
		ConfirmedReturnResponseDto returnInfo = new ConfirmedReturnResponseDto(expectedRecharche, returnedGames, customer);

		Response<ConfirmedReturnResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customer.getId()));

		Mockito.when(returnService.confirmTotalReturn(customer.getId())).thenReturn(returnInfo);

		String url = "/returns/total/" + customer.getId() + "/confirm";
		mockMvc.perform(post(url).contentType("application/json")).andExpect(status().isOk())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());
	}
	
	@Test
	public void listOfGameIdentifiersMustNotBeNull() throws JsonProcessingException, Exception {
		
		ReturnInputDto inputDto = new ReturnInputDto();
		
		Long customerId = 1L;
		
		String urlPartial = "/returns/partial/" + customerId ;
		mockMvc.perform(
				post(urlPartial)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(returnService, times(0)).showPartialReturnInfo(inputDto, customerId);
		
		String urlPartialConfirm = "/returns/partial/" + customerId + "/confirm";
		mockMvc.perform(
				post(urlPartialConfirm)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(inputDto))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(returnService, times(0)).confirmPartialReturn(inputDto, customerId);		
	}
}
