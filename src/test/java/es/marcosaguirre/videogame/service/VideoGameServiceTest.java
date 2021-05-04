package es.marcosaguirre.videogame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.model.VideoGame;
import es.marcosaguirre.videogame.repository.IVideoGameRepo;
import es.marcosaguirre.videogame.service.impl.VideoGameServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class VideoGameServiceTest {

	@MockBean
	private IVideoGameRepo repo;

	@InjectMocks
	private IVideoGameService gameService = new VideoGameServiceImpl();

	@Test
	public void registerTest() throws BaseException {
		VideoGameDto inputDto = new VideoGameDto("first game name", GameTypes.NEW_RELEASE);

		gameService.register(inputDto);

		verify(repo, times(1)).save(VideoGameMapper.toVideoGameEntity(inputDto));
	}

	@Test
	public void updateTest() throws BaseException {

		VideoGame game = new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE);

		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(game));

		game.setName("another name updated");

		gameService.update(VideoGameMapper.toVideoGameDto(game), 1L);

		VideoGameDto updatedGame= gameService.getById(1L);

		assertEquals("another name updated", updatedGame.getName());

	}

	@Test
	public void getAllGamesTest() throws BaseException {

		VideoGame firstGame = new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE);
		VideoGame secondGame= new VideoGame(2L, "second game name", GameTypes.CLASSIC);
		List<VideoGame> listOfGames = new ArrayList<>();
		listOfGames.add(firstGame);
		listOfGames.add(secondGame);

		Mockito.when(repo.findAll()).thenReturn(listOfGames);

		List<VideoGameDto> gamesList = gameService.getAll();

		assertEquals(2, gamesList.size());
		verify(repo, times(1)).findAll();
	}

	@Test
	public void getByIdTest() throws BaseException {
		VideoGame game = new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE);
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(game));

		VideoGameDto myGame = gameService.getById(1L);

		assertEquals("first game name", myGame.getName());
		assertEquals(GameTypes.NEW_RELEASE, myGame.getTypeOfGame());

	}

	@Test
	public void deleteTest() throws BaseException {
		VideoGame game = new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE);
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(game));
		gameService.delete(1L);
		verify(repo, times(1)).deleteById(game.getId());
	}
}
