package es.marcosaguirre.videogame.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.common.Prices;
import es.marcosaguirre.videogame.dto.ConfirmedRentalResponseDto;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.dto.RentalInfoResponseDto;
import es.marcosaguirre.videogame.dto.RentalInputDto;
import es.marcosaguirre.videogame.dto.VideoGameDto;
import es.marcosaguirre.videogame.dto.mapper.CustomerMapper;
import es.marcosaguirre.videogame.dto.mapper.VideoGameMapper;
import es.marcosaguirre.videogame.service.ICustomerRentalService;
import es.marcosaguirre.videogame.service.ICustomerService;
import es.marcosaguirre.videogame.service.IOperationService;
import es.marcosaguirre.videogame.service.IVideoGameService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerRentalServiceImpl implements ICustomerRentalService {

	@Autowired
	private IVideoGameService gameService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IOperationService operationService;

	@Override
	public RentalInfoResponseDto showRentalInfo(RentalInputDto inputDto) throws BaseException {
		RentalInfoResponseDto response = this.getResults(inputDto);
		return response;
	}

	@Override
	public ConfirmedRentalResponseDto confirmRentalOperation(RentalInputDto inputDto, Long customerId) throws BaseException {
		RentalInfoResponseDto results = this.getResults(inputDto);
		CustomerDto customer = customerService.getById(customerId);
		LocalDate currentDate = LocalDate.now();
		for(VideoGameDto game: results.getGames()) {
			game.setAvailable(false);
			game.setStartRentalDate(currentDate);
			game.setCustomer(CustomerMapper.toCustomerEntity(customer));
			game.setEndRentalDate(currentDate.plusDays(inputDto.getDaysOfRental()));
			gameService.update(game, game.getId());
		}
		customer.getRentedGames().addAll(VideoGameMapper.toVideoGameEntity(results.getGames()));
		customer.setLoyaltyPoints(customer.getLoyaltyPoints() + results.getLoyaltyPoints());
		customerService.update(customer, customerId);
		operationService.register(new OperationDto(customerId, OperationTypes.RENTAL, results.getTotalPrice(), currentDate));
		ConfirmedRentalResponseDto response = new ConfirmedRentalResponseDto();
		response.setTotalPayment(results.getTotalPrice());
		response.setCustomer(customer);
		return response;
	}
	
	private RentalInfoResponseDto getResults(RentalInputDto inputDto) throws BaseException {
		List<VideoGameDto> listOfGames = new ArrayList<>();
		Integer loyaltyPoints = 0;
		Double totalPrice = 0.0;
		for (Long gameId : inputDto.getGameIdentifiers()) {
			VideoGameDto game = gameService.getById(gameId);
			if(listOfGames.contains(game)) {
				log.warn("List of game Ids contains repeated values");
				BaseException exception = new BaseException(Constants.ERROR_CODE_MALFORMED_REQUEST,
						Constants.MSJ_REPEATED_GAME_IDS, "game identifier: " + Long.toString(game.getId()));
				throw exception;
			}
			if (!game.isAvailable()) {
				log.warn("Videogame with id " + game.getId() + " is not available for rent");
				BaseException exception = new BaseException(Constants.ERROR_CODE_GAME_NOT_AVAILABLE,
						Constants.MSJ_GAME_NOT_AVAILABLE, "game identifier: " + Long.toString(game.getId()));
				throw exception;
			}
			listOfGames.add(game);
			switch(game.getTypeOfGame()) {
				case CLASSIC:
					double classicResult = (inputDto.getDaysOfRental() > 5) ? Prices.REGULAR + Prices.REGULAR * (inputDto.getDaysOfRental() - 5) : Prices.REGULAR;
					totalPrice += classicResult;
					loyaltyPoints += 1;
					break;
				case NEW_RELEASE:
					totalPrice += Prices.PREMIUM * inputDto.getDaysOfRental();
					loyaltyPoints += 2;
					break;
				case STANDARD:
					double normalResult = (inputDto.getDaysOfRental() > 3) ? Prices.REGULAR + Prices.REGULAR * (inputDto.getDaysOfRental() - 3) : Prices.REGULAR;
					totalPrice += normalResult;
					loyaltyPoints += 1;
					break;
				default:
					break;
			}
		}
		RentalInfoResponseDto response = new RentalInfoResponseDto();
		response.setGames(listOfGames);
		response.setLoyaltyPoints(loyaltyPoints);
		response.setTotalPrice(totalPrice);
		return response;
	}

}
