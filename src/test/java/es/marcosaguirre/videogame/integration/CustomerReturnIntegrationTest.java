package es.marcosaguirre.videogame.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import es.marcosaguirre.videogame.dto.ConfirmedReturnResponseDto;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.ReturnInfoResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.CustomerMapper;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.model.VideoGame;
import es.marcosaguirre.videogame.service.ICustomerService;
import es.marcosaguirre.videogame.service.IVideoGameService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerReturnIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IVideoGameService gameService;

	@Autowired
	private ICustomerService customerService;

	@Test
	public void showPartialReturnInfoResponseTest() throws JsonProcessingException, Exception {

		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// customer
		CustomerDto customerDto = customerService
				.register(new CustomerDto("Marcos", "Aguirre", "maguirre@gmail.com", 0, null));

		// games
		VideoGameDto firstGame = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE, false,
				initialDate, endDate, CustomerMapper.toCustomerEntity(customerDto)));
		VideoGameDto secondGame = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC, false, initialDate,
				endDate, CustomerMapper.toCustomerEntity(customerDto)));

		// identifiers
		Long firstIdentifier = firstGame.getId();
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Double expectedRecharche = 8.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		customerDto.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.add(firstGame);

		// input
		ReturnInputDto inputDto = new ReturnInputDto(gameIdentifiers);

		// response
		ReturnInfoResponseDto returnInfo = new ReturnInfoResponseDto(expectedRecharche, returnedGames);

		Response<ReturnInfoResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customerDto.getId()));

		String url = "/returns/partial/" + customerDto.getId();
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());

		gameService.delete(firstGame.getId());
		gameService.delete(secondGame.getId());
		customerService.delete(customerDto.getId());
	}

	@Test
	public void showTotalReturnInfoResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// customer
		CustomerDto customerDto = customerService
				.register(new CustomerDto("Marcos", "Aguirre", "maguirre@gmail.com", 0, null));

		// games
		VideoGameDto firstGame = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE, false,
				initialDate, endDate, CustomerMapper.toCustomerEntity(customerDto)));
		VideoGameDto secondGame = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC, false, initialDate,
				endDate, CustomerMapper.toCustomerEntity(customerDto)));

		// customer
		Double expectedRecharche = 14.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		customerDto.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.addAll(VideoGameMapper.toVideoGameDto(listOfRentedGames));

		// response
		ReturnInfoResponseDto returnInfo = new ReturnInfoResponseDto(expectedRecharche, returnedGames);

		Response<ReturnInfoResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customerDto.getId()));

		String url = "/returns/total/" + customerDto.getId();
		mockMvc.perform(post(url).contentType("application/json")).andExpect(status().isOk())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());

		gameService.delete(firstGame.getId());
		gameService.delete(secondGame.getId());
		customerService.delete(customerDto.getId());

	}

	@Test
	public void confirmPartialReturnResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// customer
		CustomerDto customerDto = customerService
				.register(new CustomerDto("Marcos", "Aguirre", "maguirre@gmail.com", 0, null));

		// games
		VideoGameDto firstGame = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE, false,
				initialDate, endDate, CustomerMapper.toCustomerEntity(customerDto)));
		VideoGameDto secondGame = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC, false, initialDate,
				endDate, CustomerMapper.toCustomerEntity(customerDto)));

		// identifiers
		Long firstIdentifier = firstGame.getId();
		List<Long> gameIdentifiers = new ArrayList<>();
		gameIdentifiers.add(firstIdentifier);

		// customer
		Double expectedRecharche = 8.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		customerDto.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.add(new VideoGameDto(firstGame.getId(), "first", GameTypes.NEW_RELEASE, true, null, null, null));

		// input
		ReturnInputDto inputDto = new ReturnInputDto(gameIdentifiers);

		// response
		List<VideoGame> remainingGames = new ArrayList<>();
		remainingGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		customerDto.setRentedGames(remainingGames);
		ConfirmedReturnResponseDto returnInfo = new ConfirmedReturnResponseDto(expectedRecharche, returnedGames,
				customerDto);

		Response<ConfirmedReturnResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customerDto.getId()));

		String url = "/returns/partial/" + customerDto.getId() + "/confirm";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(response)))
				.andDo(print());

		gameService.delete(firstGame.getId());
		gameService.delete(secondGame.getId());
		customerService.delete(customerDto.getId());
	}

	@Test
	public void confirmTotalReturnResponseTest() throws JsonProcessingException, Exception {
		// dates
		LocalDate currentDate = LocalDate.now();
		LocalDate initialDate = currentDate.minusDays(6);
		LocalDate endDate = currentDate.minusDays(2);

		// customer
		CustomerDto customerDto = customerService
				.register(new CustomerDto("Marcos", "Aguirre", "maguirre@gmail.com", 0, null));

		// games
		VideoGameDto firstGame = gameService.register(new VideoGameDto("first", GameTypes.NEW_RELEASE, false,
				initialDate, endDate, CustomerMapper.toCustomerEntity(customerDto)));
		VideoGameDto secondGame = gameService.register(new VideoGameDto("second", GameTypes.CLASSIC, false, initialDate,
				endDate, CustomerMapper.toCustomerEntity(customerDto)));

		// customer
		Double expectedRecharche = 14.0;
		List<VideoGame> listOfRentedGames = new ArrayList<>();
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(firstGame));
		listOfRentedGames.add(VideoGameMapper.toVideoGameEntity(secondGame));
		customerDto.setRentedGames(listOfRentedGames);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		returnedGames.addAll(VideoGameMapper.toVideoGameDto(listOfRentedGames));
		
		for(VideoGameDto game : returnedGames) {
			game.setAvailable(true);
			game.setCustomer(null);
			game.setStartRentalDate(null);
			game.setEndRentalDate(null);
		}
		
		customerDto.setRentedGames(Collections.emptyList());

		// response
		ConfirmedReturnResponseDto returnInfo = new ConfirmedReturnResponseDto(expectedRecharche, returnedGames, customerDto);

		Response<ConfirmedReturnResponseDto> response = new Response<>();
		response.setData(returnInfo);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO, Constants.TYPE_INFO, "Customer with id: " + customerDto.getId()));

		String url = "/returns/total/" + customerDto.getId() + "/confirm";
		mockMvc.perform(post(url).contentType("application/json")).andExpect(status().isOk())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());

		gameService.delete(firstGame.getId());
		gameService.delete(secondGame.getId());
		customerService.delete(customerDto.getId());
	}

}
