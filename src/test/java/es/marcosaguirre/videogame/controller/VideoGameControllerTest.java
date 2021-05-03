package es.marcosaguirre.videogame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.service.IVideoGameService;

@WebMvcTest(VideoGameController.class)
public class VideoGameControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private IVideoGameService gameService;
	
	@Test
	public void getVideogamesTest() throws Exception {
		
		List<VideoGameDto> listGamesDto = new ArrayList<>();
		
		Response<List<VideoGameDto>> response = new Response<>();
		
		listGamesDto.add(new VideoGameDto(1L, "first game name", GameTypes.NEW_RELEASE));
		listGamesDto.add(new VideoGameDto(2L, "second game name", GameTypes.CLASSIC));
		listGamesDto.add(new VideoGameDto(3L, "third game name", GameTypes.STANDARD));
		
		response.setData(listGamesDto);
		
		Mockito.when(gameService.getAll()).thenReturn(listGamesDto);
		
		String url = "/inventory";
		
		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		System.out.println(actualJsonResponse);
		
		String expectedJsonResponse = objectMapper.writeValueAsString(response);
		
		System.out.println(expectedJsonResponse);
		
		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	@Test
	public void shouldReturnErrorResponseOnEmptyGetList() throws Exception {
	
		Response<List<VideoGameDto>> response = new Response<>();
		
		List<VideoGameDto> listOfGames = Collections.emptyList();
		
		response.setData(listOfGames);
		
		response.addMessage(new Message(Constants.MSJ_EMPTY_LIST));
		
		Mockito.when(gameService.getAll()).thenReturn(listOfGames);
		
		String url = "/inventory";
		
		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		String expectedJsonResponse = objectMapper.writeValueAsString(response);
		
		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	@Test
	public void addVideoGameTest() throws JsonProcessingException, Exception {
		
		VideoGameDto newVideoGame = new VideoGameDto("first game name", GameTypes.NEW_RELEASE);
		VideoGameDto savedVideoGame = new VideoGameDto(1L, "first game name", GameTypes.NEW_RELEASE);
		Response<VideoGameDto> response = new Response<>();
		response.setData(savedVideoGame);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_CREATED));
		Mockito.when(gameService.register(newVideoGame)).thenReturn(savedVideoGame);
		
		String url = "/inventory";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(newVideoGame))
		).andExpect(status().isCreated())
		.andExpect(content().string(objectMapper.writeValueAsString(response)));
	}
	
	@Test
	public void editVideoGameTest() throws JsonProcessingException, Exception {
		
		VideoGameDto existingVideoGame = new VideoGameDto(1L, "first game name", GameTypes.NEW_RELEASE);
		VideoGameDto updatedVideoGame = new VideoGameDto(1L, "first game name", GameTypes.CLASSIC);
		
		Long existingVideoGameId = 1L;
		
		Response<VideoGameDto> response = new Response<>();
		response.setData(updatedVideoGame);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_UPDATED));
		
		Mockito.when(gameService.getById(existingVideoGameId)).thenReturn(existingVideoGame);
		
		Mockito.when(gameService.update(updatedVideoGame, existingVideoGameId)).thenReturn(updatedVideoGame);
		
		String url_update = "/inventory/" + existingVideoGameId;
		mockMvc.perform(
				put(url_update)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(updatedVideoGame))
		).andExpect(status().isCreated())
		.andExpect(content().string(objectMapper.writeValueAsString(response)))
		.andDo(print());
	}
	
	@Test
	public void deleteVideoGameByIdTest() throws JsonProcessingException, Exception {
		
		Long videoGameId = 1L;
		String url = "/inventory/" + videoGameId;
		
		Response<Void> response = new Response<>();
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Long.toString(videoGameId)));
		
		Mockito.when(gameService.delete(videoGameId)).thenReturn(true);
		
		mockMvc.perform(delete(url))
			.andExpect(status().isCreated())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
	}
	
	@Test
	public void videoGameNameMustNotBeBlank() throws JsonProcessingException, Exception {
		
		VideoGameDto game = new VideoGameDto(1L, "", GameTypes.NEW_RELEASE);
		
		String url = "/inventory";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(game))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(gameService, times(0)).register(game);
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
		
		Mockito.verify(gameService, times(0)).register(game);
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
		
		Mockito.verify(gameService, times(0)).register(game);
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
		
		Mockito.verify(gameService, times(0)).register(game);
	}
	
	
}
