package es.marcosaguirre.videogame.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.common.Prices;
import es.marcosaguirre.videogame.dto.ConfirmedReturnResponseDto;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.dto.ReturnInfoResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.service.ICustomerReturnService;
import es.marcosaguirre.videogame.service.ICustomerService;
import es.marcosaguirre.videogame.service.IOperationService;
import es.marcosaguirre.videogame.service.IVideoGameService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerReturnServiceImpl implements ICustomerReturnService {

	@Autowired
	private IVideoGameService gameService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IOperationService operationService;

	@Override
	public ReturnInfoResponseDto showPartialReturnInfo(ReturnInputDto inputDto, Long id) throws BaseException {
		
		ReturnInfoResponseDto response = this.getResults(inputDto, id);
		return response;
	}

	@Override
	public ReturnInfoResponseDto showTotalReturnInfo(Long id) throws BaseException {
		
		ReturnInfoResponseDto response = this.getResults(id);
		return response;
	}

	@Override
	public ConfirmedReturnResponseDto confirmPartialReturn(ReturnInputDto inputDto, Long id) throws BaseException {
		
		ReturnInfoResponseDto results = this.getResults(inputDto, id);
		List<VideoGameDto> listOfGames = results.getReturnedGames();
		CustomerDto customer = customerService.getById(id);
		List<VideoGameDto> remainingGames = VideoGameMapper.toVideoGameDto(customer.getRentedGames());
		
		for(VideoGameDto game: listOfGames) {
			
			remainingGames.remove(game);
			game.setAvailable(true);
			game.setStartRentalDate(null);
			game.setEndRentalDate(null);
			game.setCustomer(null);
			gameService.update(game, game.getId());
		}
		
		customer.setRentedGames(VideoGameMapper.toVideoGameEntity(remainingGames));
		customerService.update(customer, customer.getId());
		operationService.register(new OperationDto(customer.getId(), OperationTypes.RETURN, results.getTotalRecharge(), LocalDate.now()));
		ConfirmedReturnResponseDto response = new ConfirmedReturnResponseDto();
		response.setTotalRecharge(results.getTotalRecharge());
		response.setCustomer(customer);
		response.setReturnedGames(listOfGames);
		
		return response;
	}

	@Override
	public ConfirmedReturnResponseDto confirmTotalReturn(Long id) throws BaseException {
		
		ReturnInfoResponseDto results = this.getResults(id);
		CustomerDto customer = customerService.getById(id);
		List<VideoGameDto> returnedGames = new ArrayList<>();
		
		for(VideoGameDto game : VideoGameMapper.toVideoGameDto(customer.getRentedGames())) {
			game.setAvailable(true);
			game.setStartRentalDate(null);
			game.setEndRentalDate(null);
			game.setCustomer(null);
			gameService.update(game, game.getId());
			returnedGames.add(game);
		}
		
		customer.setRentedGames(Collections.emptyList());
		customerService.update(customer, customer.getId());
		operationService.register(new OperationDto(customer.getId(), OperationTypes.RETURN, results.getTotalRecharge(), LocalDate.now()));
		ConfirmedReturnResponseDto response = new ConfirmedReturnResponseDto();
		response.setCustomer(customer);
		response.setReturnedGames(returnedGames);
		response.setTotalRecharge(results.getTotalRecharge());
		
		return response;
	}

	private Double totalReturnCalculator(Double totalRecharge, VideoGameDto game) {
		
		if (game.getEndRentalDate().isBefore(LocalDate.now())) {
			Integer period = Period.between(game.getEndRentalDate(), LocalDate.now()).getDays();
			
			switch (game.getTypeOfGame()) {
			
				case CLASSIC:
					totalRecharge += Prices.REGULAR * period;
					break;
					
				case NEW_RELEASE:
					totalRecharge += Prices.PREMIUM * period;
					break;
					
				case STANDARD:
					totalRecharge += Prices.REGULAR * period;
					break;
					
				default:
					break;
			}	
		}
		
		return totalRecharge;
	}
	
	private ReturnInfoResponseDto getResults(ReturnInputDto inputDto, Long id) throws BaseException {
		
		CustomerDto customerDto = customerService.getById(id);
		Double totalRecharge = 0.0;
		List<VideoGameDto> listOfGamesDto = new ArrayList<>();
		
		for (Long gameId : inputDto.getGameIdentifiers()) {
			VideoGameDto game = gameService.getById(gameId);
			
			if (listOfGamesDto.contains(game)) {
				log.warn("List of game Ids contains repeated values");
				BaseException exception = new BaseException(Constants.ERROR_CODE_MALFORMED_REQUEST,
						Constants.MSJ_REPEATED_GAME_IDS, "game identifier: " + Long.toString(gameId));
				throw exception;
			}
			
			if (!customerDto.getRentedGames().contains(VideoGameMapper.toVideoGameEntity(game))) {
				log.warn("The game with id " + game.getId() + " is not rented by this customer in this moment");
				BaseException exception = new BaseException(Constants.ERROR_CODE_GAME_NOT_RENTED,
						Constants.MSJ_GAME_NOT_RENTED, "game identifier: " + Long.toString(game.getId()));
				throw exception;
			}
			
			totalRecharge = totalReturnCalculator(totalRecharge, game);

			listOfGamesDto.add(game);
		}
		
		ReturnInfoResponseDto response = new ReturnInfoResponseDto();
		response.setTotalRecharge(totalRecharge);
		response.setReturnedGames(listOfGamesDto);
		
		return response;
	}


	private ReturnInfoResponseDto getResults(Long id) throws BaseException {
		
		CustomerDto customerDto = customerService.getById(id);
		Double totalRecharge = 0.0;
		List<VideoGameDto> listOfGamesDto = new ArrayList<>();
		
		if(customerDto.getRentedGames() == null) {
			log.warn("Rentals list of customer with id: " + customerDto.getId() + " is empty");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_EMPTY_LIST, "customer identifier: " + Long.toString(customerDto.getId()));
			throw exception;
		}
		
		for (VideoGameDto game : VideoGameMapper.toVideoGameDto(customerDto.getRentedGames())) {

			totalRecharge = totalReturnCalculator(totalRecharge, game);
			listOfGamesDto.add(game);
		}
		
		ReturnInfoResponseDto response = new ReturnInfoResponseDto();
		response.setTotalRecharge(totalRecharge);
		response.setReturnedGames(listOfGamesDto);
		
		return response;
	}

}
