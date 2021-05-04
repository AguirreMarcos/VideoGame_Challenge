package es.marcosaguirre.videogame.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.ConfirmedRentalResponseDto;
import es.marcosaguirre.videogame.dto.RentalInfoResponseDto;
import es.marcosaguirre.videogame.dto.RentalInputDto;
import es.marcosaguirre.videogame.service.ICustomerRentalService;

@RestController
@RequestMapping("/rentals")
public class CustomerRentalController {
	
	@Autowired
	private ICustomerRentalService rentalService;
	
	@PostMapping
	public ResponseEntity<Response<RentalInfoResponseDto>> showRentalInfoResponse(@Valid @RequestBody RentalInputDto inputDto){
		
		Response<RentalInfoResponseDto> response = new Response<>();
		HttpStatus status = null;
		
		try {
			response.setData(rentalService.showRentalInfo(inputDto));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_INFO, Constants.TYPE_INFO, Constants.MSJ_ORIGIN_RENTAL_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<RentalInfoResponseDto>>(response, status);
	}
	
	@PostMapping("{id}/confirm")
	public ResponseEntity<Response<ConfirmedRentalResponseDto>> confirmRentalResponse(@Valid @RequestBody RentalInputDto inputDto, @PathVariable Long id){
		
		Response<ConfirmedRentalResponseDto> response = new Response<>();
		HttpStatus status = null;
		
		try {
			response.setData(rentalService.confirmRentalOperation(inputDto, id));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RENTAL_CONFIRMATION, Constants.TYPE_INFO, Constants.MSJ_ORIGIN_RENTAL_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<ConfirmedRentalResponseDto>>(response, status);
	}
}
