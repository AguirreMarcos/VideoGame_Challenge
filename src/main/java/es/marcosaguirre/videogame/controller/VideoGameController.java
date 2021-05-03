package es.marcosaguirre.videogame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.UpdatableVideoGameDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.service.IVideoGameService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class VideoGameController {
	
	@Autowired
	private IVideoGameService gameService;
	
	@PostMapping
	public ResponseEntity<Response<VideoGameDto>> addGame(@Valid @RequestBody VideoGameDto gameDto){
		Response<VideoGameDto> response = new Response<>();
		HttpStatus status = null;
		try {
			response.setData(gameService.register(gameDto));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_CREATED));
			status = HttpStatus.CREATED;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		return new ResponseEntity<Response<VideoGameDto>>(response, status);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Response<VideoGameDto>> editGame(@Valid @RequestBody UpdatableVideoGameDto gameDto, @PathVariable Long id) throws BaseException{
		Response<VideoGameDto> response = new Response<>();
		HttpStatus status = null;
		VideoGameDto updatedGameDto = null;
		try {
			updatedGameDto = gameService.getById(id);
		} catch (BaseException e) {
			log.warn("Videogame with id " + id + " doesn't exists");
		}
		if(updatedGameDto == null) {
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND, Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id) ));
			return new ResponseEntity<Response<VideoGameDto>>(response, HttpStatus.NOT_FOUND);
		}
		if(gameDto.getName() != null) {
			updatedGameDto.setName(gameDto.getName());
		}
		if(gameDto.getTypeOfGame() != null) {
			updatedGameDto.setTypeOfGame(gameDto.getTypeOfGame());
		}
		if(gameDto.getAvailable() != null) {
			updatedGameDto.setAvailable(gameDto.getAvailable());
		}
		if(gameDto.getStartRentalDate() != null) {
			updatedGameDto.setStartRentalDate(gameDto.getStartRentalDate());
		}
		if(gameDto.getEndRentalDate() != null) {
			updatedGameDto.setEndRentalDate(gameDto.getEndRentalDate());
		}
		if(gameDto.getCustomer() != null) {
			updatedGameDto.setCustomer(gameDto.getCustomer());
		}
		updatedGameDto = gameService.update(updatedGameDto, id);
		response.setData(updatedGameDto);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_UPDATED));
		status = HttpStatus.CREATED;
		return new ResponseEntity<Response<VideoGameDto>>(response, status);
	}
	
	@GetMapping
	public  ResponseEntity<Response<List<VideoGameDto>>> getGamesInventory() throws BaseException {
		List<VideoGameDto> inventory = new ArrayList<>();
		inventory.addAll(gameService.getAll());
		Response<List<VideoGameDto>> response = new Response<>();
		if(inventory.isEmpty()) {
			response.setData(Collections.emptyList());
			response.addMessage(new Message(Constants.MSJ_EMPTY_LIST));
			return new ResponseEntity<Response<List<VideoGameDto>>>(response, HttpStatus.OK);
		}
		response.setData(inventory);
		return new ResponseEntity<Response<List<VideoGameDto>>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response<VideoGameDto>> getGameById(@PathVariable Long id){
		VideoGameDto gameDto = null;
		try {
			gameDto = gameService.getById(id);
		} catch (BaseException e) {
			log.warn("Videogame with id " + id + " doesn't exists");
		}
		Response<VideoGameDto> response = new Response<>();
		if(gameDto == null) {
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND, Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id) ));
			return new ResponseEntity<Response<VideoGameDto>>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(gameDto);
		return new ResponseEntity<Response<VideoGameDto>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Response<Void>> deleteGameById(@PathVariable Long id){
		Response<Void> response = new Response<>();
		HttpStatus status = null;
		try {
			gameService.delete(id);
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Long.toString(id)));
			status = HttpStatus.CREATED;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			if( e.getCode() == Constants.ERROR_CODE_ELEMENT_NOT_FOUND) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.OK;
			}
		}
		return new ResponseEntity<Response<Void>>(response, status);
	}
	
}
