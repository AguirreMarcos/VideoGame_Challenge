package es.marcosaguirre.videogame.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.model.VideoGame;

public class VideoGameMapper {
	
	public static List<VideoGameDto> toVideoGameDto(List<VideoGame> games) {
		List<VideoGameDto> dtos = new ArrayList<>();
		if (games != null) {
			for (VideoGame game : games) {
				dtos.add(toVideoGameDto(game));
			}
		}
		return dtos;
	}
	
	public static VideoGameDto toVideoGameDto(VideoGame game) {
		VideoGameDto dto = new VideoGameDto();
		if(game != null) {
			dto.setId(game.getId());
			dto.setName(game.getName());
			dto.setTypeOfGame(game.getTypeOfGame());
			dto.setAvailable(game.isAvailable());
			dto.setStartRentalDate(game.getStartRentalDate());
			dto.setEndRentalDate(game.getEndRentalDate());
			dto.setCustomer(game.getCustomer());
		}
		return dto;
	}
	
	public static List<VideoGame> toVideoGameEntity(List<VideoGameDto> dtos) {
		List<VideoGame> games = new ArrayList<>();
		if (dtos != null) {
			for (VideoGameDto dto : dtos) {
				games.add(toVideoGameEntity(dto));
			}
		}
		return games;
	}
	
	public static VideoGame toVideoGameEntity(VideoGameDto gameDto) {
		VideoGame gameEntity = new VideoGame();
		if(gameDto != null) {
			if(gameDto.getId() != null) {
				gameEntity.setId(gameDto.getId());
			}
			gameEntity.setName(gameDto.getName());
			gameEntity.setTypeOfGame(gameDto.getTypeOfGame());
			gameEntity.setAvailable(gameDto.isAvailable());
			if(gameDto.getStartRentalDate() != null) {
				gameEntity.setStartRentalDate(gameDto.getStartRentalDate());
			}
			if(gameDto.getEndRentalDate() != null) {
				gameEntity.setEndRentalDate(gameDto.getEndRentalDate());
			}
			if(gameDto.getCustomer() != null) {
				gameEntity.setCustomer(gameDto.getCustomer());
			}

		}
		return gameEntity;
	}
}
