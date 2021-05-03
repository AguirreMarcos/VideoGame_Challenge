package es.marcosaguirre.videogame.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.model.VideoGame;
import es.marcosaguirre.videogame.repository.IVideoGameRepo;
import es.marcosaguirre.videogame.service.IVideoGameService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoGameServiceImpl implements IVideoGameService{
	
	@Autowired
	private IVideoGameRepo gameRepo;

	@Override
	public VideoGameDto register(VideoGameDto obj) throws BaseException {
		VideoGame newVideoGame = null;
		try {
			newVideoGame = gameRepo.save(VideoGameMapper.toVideoGameEntity(obj));
		} catch (DataIntegrityViolationException dve) {
			log.error(dve.getMessage());
			BaseException e = new BaseException(Constants.ERROR_CODE_DAO, Constants.MSJ_SERVER_ERROR);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			BaseException ex = new BaseException(Constants.ERROR_CODE_SERVER, Constants.MSJ_SERVER_ERROR);
			throw ex;
		}
		return VideoGameMapper.toVideoGameDto(newVideoGame);
	}

	@Override
	public VideoGameDto update(VideoGameDto obj, Long id) throws BaseException {
		Optional<VideoGame> game = gameRepo.findById(id);
		if (!game.isPresent()) {
			log.warn("Videogame with id " + id + " doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(obj.getId()));
			throw exception;
		}
		obj.setId(id);
		VideoGame updatedGame = gameRepo.save(VideoGameMapper.toVideoGameEntity(obj));
		return VideoGameMapper.toVideoGameDto(updatedGame);
	}

	@Override
	public List<VideoGameDto> getAll() throws BaseException {
		return VideoGameMapper.toVideoGameDto(gameRepo.findAll());
	}

	@Override
	public VideoGameDto getById(Long id) throws BaseException {
		Optional<VideoGame> game = gameRepo.findById(id);
		if (!game.isPresent()) {
			log.warn("Videogame with id " + id + " doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id));
			throw exception;
		}
		return VideoGameMapper.toVideoGameDto(game.get());
	}

	@Override
	public boolean delete(Long id) throws BaseException {
		Optional<VideoGame> game = gameRepo.findById(id);
		if (!game.isPresent()) {
			log.warn("Videogame with id " + id + " doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id));
			throw exception;
		}
		gameRepo.deleteById(id);
		return true;
	}



}
