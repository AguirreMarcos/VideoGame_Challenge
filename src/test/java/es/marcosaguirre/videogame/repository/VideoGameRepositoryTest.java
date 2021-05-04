package es.marcosaguirre.videogame.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.GameTypes;
import es.marcosaguirre.videogame.model.VideoGame;

@DataJpaTest
public class VideoGameRepositoryTest {
	
	@Autowired
	private IVideoGameRepo repo;
	
	@Test
	public void registerTest() {
		VideoGame game = repo.save(new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE));
		assertThat(game).isNotEqualTo(null);
		assertThat(game.getId()).isGreaterThan(0);
	}
	
	@Test
	public void findByIdTest() {
		VideoGame game = repo.save(new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE));
		Long gameId = game.getId();
		Optional<VideoGame> findedGame = repo.findById(gameId);
		assertThat(findedGame.get().getName()).isEqualTo(game.getName());
	}
	
	@Test
	public void findAllTest() {
		repo.save(new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE));
		repo.save(new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE));
		List<VideoGame> allGamesList = repo.findAll();
		assertThat(allGamesList.size()).isEqualTo(2);
	}
	
	@Test
	public void deleteByIdTest() {
		VideoGame game = repo.save(new VideoGame(1L, "first game name", GameTypes.NEW_RELEASE));
		assertThat(repo.findAll().size()).isEqualTo(1);
		repo.deleteById(game.getId());
		assertThat(repo.findAll().size()).isEqualTo(0);
	}
	
	@Test
	public void createShouldFailOnLargeName() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new VideoGame(1L, Constants.CHARACTERS_71_STRING, GameTypes.NEW_RELEASE));
		});
	}
	
	@Test
	public void createShouldFailOnNullName() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new VideoGame(1L, null, GameTypes.NEW_RELEASE));
		});
	}
	
	@Test
	public void createShouldFailOnNullVideoGameType() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new VideoGame(1L, "first game", null));
		});
	}
}
