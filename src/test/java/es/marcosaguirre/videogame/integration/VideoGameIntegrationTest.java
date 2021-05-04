package es.marcosaguirre.videogame.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.service.IVideoGameService;

@SpringBootTest
@AutoConfigureMockMvc
public class VideoGameIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IVideoGameService gameService;

	@Test
	public void addVideoGameTest() throws JsonProcessingException, Exception {

		VideoGameDto newVideoGame = new VideoGameDto("first game name", GameTypes.NEW_RELEASE);

		String url = "/inventory";
		MvcResult mvcResult = mockMvc
				.perform(post(url).contentType("application/json")
						.content(objectMapper.writeValueAsString(newVideoGame)))
				.andExpect(status().isCreated()).andDo(print()).andReturn();

		Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.id");

		String name = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.name");

		VideoGameDto savedVideoGame = gameService.getById(id.longValue());

		assertThat(name.equals(savedVideoGame.getName()));

		gameService.delete(id.longValue());
	}

	@Test
	public void editVideoGameTest() throws JsonProcessingException, Exception {

		VideoGameDto createdVideoGame = gameService
				.register(new VideoGameDto("first game name", GameTypes.NEW_RELEASE));

		VideoGameDto updatedVideoGame = new VideoGameDto("second game name", GameTypes.NEW_RELEASE);

		String url_update = "/inventory/" + createdVideoGame.getId();

		MvcResult mvcResult = mockMvc
				.perform(put(url_update).contentType("application/json")
						.content(objectMapper.writeValueAsString(updatedVideoGame)))
				.andExpect(status().isCreated()).andDo(print()).andReturn();

		String name = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.name");

		assertThat(name.equals(updatedVideoGame.getName()));
		
		gameService.delete(createdVideoGame.getId());

	}

	@Test
	public void deleteVideoGameByIdTest() throws JsonProcessingException, Exception {

		VideoGameDto createdVideoGame = gameService
				.register(new VideoGameDto("first game name", GameTypes.NEW_RELEASE));

		String url = "/inventory/" + createdVideoGame.getId();

		Response<Void> response = new Response<>();
		response.addMessage(
				new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Long.toString(createdVideoGame.getId())));

		mockMvc.perform(delete(url)).andExpect(status().isCreated())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());

	}

	@Test
	public void getVideoGamesTest() throws Exception {

		// clear games
		List<VideoGameDto> previousList = gameService.getAll();

		for (VideoGameDto game : previousList) {
			gameService.delete(game.getId());
		}

		// -----

		// add elements to database

		List<VideoGameDto> actualList = new ArrayList<>();

		actualList.add(new VideoGameDto("first game name", GameTypes.NEW_RELEASE));
		actualList.add(new VideoGameDto("second game name", GameTypes.CLASSIC));
		actualList.add(new VideoGameDto("third game name", GameTypes.STANDARD));

		for (VideoGameDto game : actualList) {
			gameService.register(game);
		}

		List<VideoGameDto> responseList = gameService.getAll();

		String url = "/inventory";

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

		String actualJsonResponse = mvcResult.getResponse().getContentAsString();

		Response<List<VideoGameDto>> expectedResponse = new Response<>();

		expectedResponse.setData(responseList);

		String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}

	@Test
	public void shouldReturnErrorResponseOnEmptyGetList() throws Exception {

		// clear games
		List<VideoGameDto> previousList = gameService.getAll();

		for (VideoGameDto game : previousList) {
			gameService.delete(game.getId());
		}

		Response<List<VideoGameDto>> response = new Response<>();

		List<VideoGameDto> listOfGames = Collections.emptyList();

		response.setData(listOfGames);

		response.addMessage(new Message(Constants.MSJ_EMPTY_LIST));

		String url = "/inventory";

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

		String actualJsonResponse = mvcResult.getResponse().getContentAsString();

		String expectedJsonResponse = objectMapper.writeValueAsString(response);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}

	@Test
	public void videoGameNameMustNotBeBlank() throws JsonProcessingException, Exception {

		VideoGameDto game = new VideoGameDto(1L, "", GameTypes.NEW_RELEASE);

		String url = "/inventory";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(game)))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	
	@Test
	public void videoGameTypeMustNotBeNull() throws JsonProcessingException, Exception {
		VideoGameDto game = new VideoGameDto(1L, "first game", null);
		
		String url = "/inventory";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(game))
				).andExpect(status().isBadRequest());
	}
	
	@Test
	public void videoGameNameMustBeUnder70Characters() throws JsonProcessingException, Exception {
		
		VideoGameDto game = new VideoGameDto(1L, Constants.CHARACTERS_71_STRING, GameTypes.NEW_RELEASE);
		
		String url = "/inventory";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(game))
				).andExpect(status().isBadRequest());
	}
	
	@Test
	public void videoGameTypeMustMatchPattern() throws JsonProcessingException, Exception {
		
		VideoGameDto game = new VideoGameDto(1L, "First game", GameTypes.INVALID);
		
		String url = "/inventory";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(game))
				).andExpect(status().isBadRequest());

	}

}
